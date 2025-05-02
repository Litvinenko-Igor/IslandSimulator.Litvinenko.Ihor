package island.com.Animals.Herbivores;

import island.com.Animals.Animal;
import island.com.Config;
import island.com.Map.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Duck extends Herbivore{
    private static final double BASE_WEIGHT = 1.0; // Базова вага виду
    private static final int MAX_COUNT_PER_CELL = 200; // Макс. на клітинці
    private static final int MAX_SPEED = 4; // Макс. швидкість (клітинок за хід)
    private static final double FOOD_CAPACITY = 0.15; // Потрібно їжі для насичення

    public Duck(int startX, int startY){
        super(startX, startY);
        setInitialSaturation(getFoodCapacity());
    }

    public void eat(Location currentLocation) {
        if (!this.isAlive() || getCurrentSaturation() >= getFoodCapacity()) {
            return;
        }

        Map<String, Integer> predatorEatingMap = Config.EAT_PROBABILITIES.get(this.getSpeciesName());
        if (predatorEatingMap == null || predatorEatingMap.isEmpty()) {
            super.eat(currentLocation);
            return;
        }
        List<Animal> potentialPrey = new ArrayList<>();
        for (Animal animal : currentLocation.getAnimals()) {
            if (animal.isAlive() && animal != this && predatorEatingMap.containsKey(animal.getSpeciesName())) {
                potentialPrey.add(animal);
            }
        }
        if (!potentialPrey.isEmpty()) {
            Animal target = potentialPrey.get(ThreadLocalRandom.current().nextInt(potentialPrey.size()));
            String targetSpecies = target.getSpeciesName();

            int probability = predatorEatingMap.get(targetSpecies);
            if (ThreadLocalRandom.current().nextInt(100) < probability) {
                double foodWeight = target.getWeight();
                double neededSaturation = getFoodCapacity() - getCurrentSaturation();
                double gainedSaturation = Math.min(neededSaturation, foodWeight);
                addSaturation(gainedSaturation);
                target.die();
                System.out.println(this.getSymbol() + " ate " + target.getSymbol() + " at (" + this.x + "," + this.y + ")");
            }
        }
    }

    @Override
    public String getSpeciesName() {
        return "Duck";
    }

    @Override
    protected int getMaxSpeed() {
        return MAX_SPEED;
    }

    @Override
    protected double getReproductionChance() {
        return 0.5;
    }

    @Override
    public double getWeight() {
        return BASE_WEIGHT;
    }

    @Override
    public double getFoodCapacity() {
        return FOOD_CAPACITY;
    }

    @Override
    public int getMaxCountPerCell() {
        return MAX_COUNT_PER_CELL;
    }
    @Override
    public String getSymbol() {
        return "🦆";
    }
}
