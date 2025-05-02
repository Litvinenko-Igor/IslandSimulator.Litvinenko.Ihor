package island.com.Map;
import island.com.Animals.Animal;
import island.com.Plants.*;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Island {
    private int width;
    private int height;
    private Location[][] grid;

    public Island(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Location[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = new Location(x, y);
            }
        }
    }

    public Location getLocation(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return grid[y][x];
        }
        return null;
    }
    public Map<String, Integer> getPopulationStats() {
        Map<String, Integer> stats = new HashMap<>();
        int totalAnimals = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Location loc = grid[y][x];
                if (loc != null) {
                    List<Animal> animalsInLoc = loc.getAnimals();
                    if (animalsInLoc != null) {
                        for (Animal animal : animalsInLoc) {
                            if (animal != null && animal.isAlive()) {
                                String speciesName = animal.getSpeciesName();
                                stats.put(speciesName, stats.getOrDefault(speciesName, 0) + 1);
                                totalAnimals++;
                            }
                        }
                    }
                }
            }
        }
        stats.put("TOTAL_ANIMALS", totalAnimals);
        return stats;
    }

    public boolean isCellValidForMove(int x, int y, Animal animal) {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height) {
            return false;
        }
        Location targetLocation = getLocation(x, y);
        if (targetLocation == null) {
            return false;
        }
        int limit = animal.getMaxCountPerCell();
        int currentCount = targetLocation.countAnimalsOfType(animal.getClass());
        return currentCount < limit;
    }


    public synchronized boolean attemptMove(Animal animal, int newX, int newY) {
        int oldX = animal.getX();
        int oldY = animal.getY();

        if (oldX == newX && oldY == newY) { return false; }

        Location oldLocation = getLocation(oldX, oldY);
        Location newLocation = getLocation(newX, newY);

        if (oldLocation == null || newLocation == null || !isCellValidForMove(newX, newY, animal)) {
            return false;
        }

        boolean removed = oldLocation.removeAnimal(animal);
        if (removed) {
            newLocation.addAnimal(animal);
            return true;
        } else {
            return false;
        }
    }

    private static final List<Class<? extends Plant>> PLANT_TYPES = List.of(
            Grass.class,
            Clover.class,
            Dandelion.class,
            Leaves.class


    );
    public void growPlants() {
        int newPlantsTarget = (int) (this.width * this.height * 0.01) + 10;
        int plantsAdded = 0;
        int plantLimitPerCell = 200;
        for (int i = 0; i < newPlantsTarget * 5 && plantsAdded < newPlantsTarget; i++) {
            int x = ThreadLocalRandom.current().nextInt(this.width);
            int y = ThreadLocalRandom.current().nextInt(this.height);
            Location loc = getLocation(x, y);
            if (loc != null && loc.getPlants().size() < plantLimitPerCell) {
                try {
                    Class<? extends Plant> randomPlantClass = PLANT_TYPES.get(
                            ThreadLocalRandom.current().nextInt(PLANT_TYPES.size())
                    );
                    Constructor<? extends Plant> constructor = randomPlantClass.getDeclaredConstructor(int.class, int.class);
                    Plant newPlant = constructor.newInstance(x, y);
                    loc.addPlant(newPlant);
                    plantsAdded++;
                } catch (Exception e) {
                }
            }
        }
    }
    public void printIslandState() {
        System.out.println("\n========================= Island State =========================");
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Location currentLocation = this.getLocation(x, y);
                String symbolToShow = ".";
                if (currentLocation != null) {
                    List<Animal> animalHere = currentLocation.getAnimals();
                    List<Plant> plantHere = currentLocation.getPlants();
                    if (!animalHere.isEmpty()) {
                        symbolToShow = animalHere.get(0).getSymbol();
                    } else if(!plantHere.isEmpty()) {
                        symbolToShow = plantHere.get(0).getSymbol();
                    } else {
                    }
                } else {
                    symbolToShow = "?";
                }
                System.out.print(symbolToShow + " ");
            }
            System.out.println();
        }
        System.out.println("================================================================");
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

}
