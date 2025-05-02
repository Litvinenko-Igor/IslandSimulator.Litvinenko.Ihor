package island.com.Map;

import island.com.Animals.Animal;
import island.com.Plants.Plant;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;



public class Location {
    private int x;
    private int y;
    private List<Animal> animals;
    private List<Plant> plants;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
        this.animals = Collections.synchronizedList(new ArrayList<>());
        this.plants = Collections.synchronizedList(new ArrayList<>());
    }

    public void addAnimal(Animal animal) {
        this.animals.add(animal);
    }

    public boolean removeAnimal(Animal animal) {
        boolean removed = this.animals.remove(animal);
        return removed;
    }

    public List<Animal> getAnimals() {
        synchronized(this.animals) {
            return new ArrayList<>(this.animals);
        }
    }

    public void addPlant(Plant plant) {
        this.plants.add(plant);
    }

    public void removePlants(Plant plant) {
        this.plants.remove(plant);
    }

    public List<Plant> getPlants() {
        synchronized(this.plants) {
            return new ArrayList<>(this.plants);
        }
    }

    public int countAnimalsOfType(Class<?> animalType) {
        int count = 0;
        synchronized (this.animals) {
            for (Animal animal : animals) {
                if (animalType.isInstance(animal)) {
                    count++;
                }
            }
        }
        return count;
    }
    public int countPlantsOfType(Class<?> plantType) {
        int count = 0;
        synchronized (this.plants) {
            for (Plant plant : plants) {
                if (plantType.isInstance(plant)) {
                    count++;
                }
            }
        }
        return count;
    }
}