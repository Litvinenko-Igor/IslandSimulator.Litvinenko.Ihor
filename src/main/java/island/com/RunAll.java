package island.com;

import island.com.Animals.Animal;
import island.com.Animals.Herbivores.*;
import island.com.Animals.Predators.*;
import island.com.Map.Island;
import island.com.Map.Location;
import island.com.Plants.*;

import java.util.concurrent.ThreadLocalRandom;


public  class RunAll {
    public void run() {
        Island island = new Island(120, 20);
        populateIsland(island);
        SimulationRunner simulationRunner = new SimulationRunner(island, 150, 100);
        simulationRunner.startSimulation();
    }

    private static <T extends Animal> void placeAnimals(Island island, Class<T> speciesClass, int count, int limitPerCell) {
        System.out.println("Placing " + count + " " + speciesClass.getSimpleName() + "...");
        for (int i = 0; i < count; i++) {
            for (int attempt = 0; attempt < 10; attempt++) {
                int x = ThreadLocalRandom.current().nextInt(island.getWidth());
                int y = ThreadLocalRandom.current().nextInt(island.getHeight());
                Location loc = island.getLocation(x, y);

                if (loc != null && loc.countAnimalsOfType(speciesClass) < limitPerCell) {
                    try {
                        java.lang.reflect.Constructor<T> constructor = speciesClass.getDeclaredConstructor(int.class, int.class);
                        T newAnimal = constructor.newInstance(x, y);
                        loc.addAnimal(newAnimal);
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }
    }

    private static <T extends Plant> void placePlants(Island island, Class<T> speciesClass, int count, int limitPerCell) {
        System.out.println("Placing " + count + " " + speciesClass.getSimpleName() + "...");

        for (int i = 0; i < count; i++) {
            for (int attempt = 0; attempt < 10; attempt++) {
                int x = ThreadLocalRandom.current().nextInt(island.getWidth());
                int y = ThreadLocalRandom.current().nextInt(island.getHeight());
                Location loc = island.getLocation(x, y);

                if (loc != null && loc.countPlantsOfType(speciesClass) < limitPerCell) {
                    try {
                        java.lang.reflect.Constructor<T> constructor = speciesClass.getDeclaredConstructor(int.class, int.class);
                        T newPlants = constructor.newInstance(x, y);
                        loc.addPlant(newPlants);
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }
    }

    private static void populateIsland(Island island) {
        System.out.println("Populating the island...");
        placePlants(island, Grass.class, 200, 50);
        placePlants(island, Clover.class, 50, 50);
        placePlants(island, Dandelion.class, 50, 50);
        placePlants(island, Leaves.class, 50, 50);
        placeAnimals(island, Rabbit.class, 70, 150);
        placeAnimals(island, Mouse.class, 50, 100);
        placeAnimals(island, Caterpillar.class, 200, 1000);
        placeAnimals(island, Sheep.class, 140, 140);
        placeAnimals(island, Goat.class, 140, 140);
        placeAnimals(island, Duck.class, 200, 200);
        placeAnimals(island, Deer.class, 20, 20);
        placeAnimals(island, Boar.class, 50, 50);
        placeAnimals(island, Horse.class, 20, 20);
        placeAnimals(island, Buffalo.class, 10, 10);
        placeAnimals(island, Fox.class, 30, 30);
        placeAnimals(island, Snake.class, 30, 30);
        placeAnimals(island, Wolf.class, 30, 30);
        placeAnimals(island, Eagle.class, 20, 20);
        placeAnimals(island, Bear.class, 5, 5);
        System.out.println("Island population complete.");
    }
}