package island.com;

import island.com.Animals.Animal;
import island.com.Map.Island;
import island.com.Map.Location;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class SimulationRunner {
    private final Island island;
    private List<Animal> allAnimals;
    private final int simulationDurationSteps;
    private final int stepDelayMILLISECONDS;
    private long stepCounter = 0;
    private ScheduledExecutorService mainLoopExecutor;
    private ExecutorService animalActionExecutor;
    private volatile boolean stopping = false;

    public SimulationRunner(Island island, int simulationDurationSteps, int stepDelayMILLISECONDS) {
        this.island = island;
        this.simulationDurationSteps = simulationDurationSteps;
        this.stepDelayMILLISECONDS = stepDelayMILLISECONDS;
        this.stepCounter = 0L;
        this.allAnimals = collectAllAnimalsFromIsland();
        this.mainLoopExecutor = Executors.newSingleThreadScheduledExecutor();
        int coreCount = Runtime.getRuntime().availableProcessors();
        this.animalActionExecutor = Executors.newFixedThreadPool(coreCount);

    }

    public void startSimulation(){
        mainLoopExecutor.scheduleAtFixedRate(this::runOneTick, 0, stepDelayMILLISECONDS, TimeUnit.MILLISECONDS);
    }
    public void stopSimulation() {
        if (stopping) {
            System.out.println("Stop already in progress...");
        }
        stopping = true;
        if (mainLoopExecutor != null && !mainLoopExecutor.isShutdown()) {
            mainLoopExecutor.shutdown();
            try {
                System.out.println("Awaiting main loop termination (5 sec)...");
                if (!mainLoopExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    mainLoopExecutor.shutdownNow();
                } else {
                    System.out.println("Main loop terminated gracefully.");
                }
            } catch (InterruptedException e) {
                mainLoopExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        } else {
            System.out.println("Main loop executor already null or shut down.");
        }

        if (animalActionExecutor != null && !animalActionExecutor.isShutdown()) {
            animalActionExecutor.shutdown();
            try {
                System.out.println("Awaiting animal actions termination (5 sec)...");
                if (!animalActionExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    animalActionExecutor.shutdownNow();
                } else {
                    System.out.println("Animal action executor terminated gracefully.");
                }
            } catch (InterruptedException e) {
                animalActionExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        } else {
            System.out.println("Animal action executor already null or shut down.");
        }
        System.out.println(">>> stopSimulation() finished <<<");
    }
    private void runOneTick() {
        if (stopping) {
            return;
        }
        try {
            stepCounter++;
            System.out.println("\n--- Крок: " + stepCounter + " ---");

            if (animalActionExecutor.isShutdown()) {
                if (!stopping) {
                    stopping = true;
                    stopSimulation();
                }
                return;
            }
            doSimulationStep();
            Map<String, Integer> stats = island.getPopulationStats();
            System.out.println("--- Population Stats ---");
            stats.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> System.out.printf("%-15s: %d%n", entry.getKey(), entry.getValue()));
            System.out.println("------------------------");
            island.printIslandState();
            int totalAnimalCount = stats.getOrDefault("TOTAL_ANIMALS", 0);
            long maxSteps = 500;
            if (!stopping && (totalAnimalCount == 0 || stepCounter >= maxSteps)) {
                if(totalAnimalCount == 0) {
                    System.out.println("!!! All animals died out! Stopping simulation... !!!");
                } else {
                    System.out.println("!!! Reached max step limit (" + maxSteps + ")! Stopping simulation... !!!");
                }
                stopping = true;
                stopSimulation();
            }
        } catch (Exception e) {
            if (!stopping) {
                stopping = true;
                e.printStackTrace();
                stopSimulation();
            }
        }
    }

    private void doSimulationStep(){
        if (animalActionExecutor.isShutdown()) {
            return;
        }
        island.growPlants();
        List<Animal> allNewbornsThisStep = new ArrayList<>();
        List<Future<?>> taskFutures = new ArrayList<>();
        List<Animal> liveAnimals = new ArrayList<>(this.allAnimals);
        for(Animal animal : liveAnimals){
            if(animal.isAlive()){
                Runnable animalTask = () -> {
                    try {
                        double hungerPerStep = animal.getFoodCapacity() * 0.01;
                        animal.decreaseSaturation(hungerPerStep);
                        if(animal.getCurrentSaturation() <= 0){
                            animal.die();
                        }
                        if(animal.isAlive()){
                            Location locEat = island.getLocation(animal.getX(), animal.getY());
                            if(locEat != null){
                                animal.eat(locEat);
                            }
                            animal.move(island);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                };
                Future<?> future = animalActionExecutor.submit(animalTask);
                taskFutures.add(future);
            }
        }
        long taskTimeoutMillis = 500;
        for (Future<?> future : taskFutures) {
            try {
                future.get(taskTimeoutMillis, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                future.cancel(true);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                future.cancel(true);
            }
        }
        System.out.println("Animal action tasks finished or timed out.");
        for(Animal animal : liveAnimals){
            if(animal.isAlive()){
                Location currentLoc = island.getLocation(animal.getX(), animal.getY());
                if(currentLoc != null){
                    List<Animal> newborns = animal.reproduce(this.island);
                    if(newborns != null && !newborns.isEmpty()){
                        allNewbornsThisStep.addAll(newborns);
                    }
                }
            }
        }
        if(!allNewbornsThisStep.isEmpty()){
            System.out.println("Adding " + allNewbornsThisStep.size() + " newborns...");
            synchronized (this.allAnimals) {
                this.allAnimals.addAll(allNewbornsThisStep);
                for(Animal newborn : allNewbornsThisStep){
                    Location loc = island.getLocation(newborn.getX(), newborn.getY());
                    if(loc != null){
                        loc.addAnimal(newborn);
                    }
                }
            }
        }

        synchronized (this.allAnimals) {
            Iterator<Animal> cleanupIterator = this.allAnimals.iterator();
            while (cleanupIterator.hasNext()){
                Animal animal = cleanupIterator.next();
                if(!animal.isAlive()){
                    Location loc = island.getLocation(animal.getX(),animal.getY());
                    if(loc != null){
                        loc.removeAnimal(animal);
                    }
                    cleanupIterator.remove();
                }
            }
        }
    }

    private List<Animal> collectAllAnimalsFromIsland() {
        List<Animal> collectedAnimals = new ArrayList<>();
        for (int y = 0; y < this.island.getHeight(); y++) {
            for (int x = 0; x < this.island.getWidth(); x++) {
                Location location = this.island.getLocation(x, y);
                if (location != null && location.getAnimals() != null && !location.getAnimals().isEmpty()) {
                    collectedAnimals.addAll(location.getAnimals());
                }
            }
        }
        return collectedAnimals;
    }
}
