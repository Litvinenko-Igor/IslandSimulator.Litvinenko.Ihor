package island.com.Animals.Herbivores;

import island.com.Map.Location;

public class Buffalo extends Herbivore{
    private static final double BASE_WEIGHT = 700.0; // Базова вага виду
    private static final int MAX_COUNT_PER_CELL = 10; // Макс. на клітинці
    private static final int MAX_SPEED = 3; // Макс. швидкість (клітинок за хід)
    private static final double FOOD_CAPACITY = 100.0; // Потрібно їжі для насичення

    public Buffalo(int startX, int startY){
        super(startX, startY);
        setInitialSaturation(getFoodCapacity());
    }
    public void eat(Location currentLocation) {
        super.eat(currentLocation);
    }
    @Override
    public String getSpeciesName() {
        return "Buffalo";
    }

    @Override
    protected int getMaxSpeed() {
        return MAX_SPEED;
    }

    @Override
    protected double getReproductionChance() {
        return 0.08;
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
        return "🐃";
    }
}
