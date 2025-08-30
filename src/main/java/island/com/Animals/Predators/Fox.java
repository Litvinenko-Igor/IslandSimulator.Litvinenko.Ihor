package island.com.Animals.Predators;

import island.com.Map.Location;

import java.util.concurrent.ThreadLocalRandom;

public class Fox extends Predators{
    private static final double BASE_WEIGHT = 8.0; // Базова вага виду
    private static final int MAX_COUNT_PER_CELL = 30; // Макс. на клітинці
    private static final int MAX_SPEED = 2; // Макс. швидкість (клітинок за хід)
    private static final double FOOD_CAPACITY = 2.0; // Потрібно їжі для насичення

    public Fox(int startX, int startY){
        super(startX,startY);
        setInitialSaturation(getFoodCapacity() * (0.5 + ThreadLocalRandom.current().nextDouble() * 0.5));
    }

    @Override
    public void eat(Location currentLocation) {
        super.eat(currentLocation);
    }


    @Override
    public String getSymbol() {
        return "🦊";
    }
    public double getFoodCapacity() {
        return FOOD_CAPACITY;
    }

    public int getMaxCountPerCell() {
        return MAX_COUNT_PER_CELL;
    }

    public double getWeight() {
        return BASE_WEIGHT;
    }
    @Override
    public String getSpeciesName() {
        return "Fox";
    }

    @Override
    protected int getMaxSpeed() {
        return MAX_SPEED;
    }

    @Override
    protected double getReproductionChance() {
        return 0.3;
    }

}
