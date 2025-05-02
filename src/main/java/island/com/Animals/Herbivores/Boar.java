package island.com.Animals.Herbivores;

import island.com.Map.Location;

public class Boar extends Herbivore{
    private static final double BASE_WEIGHT = 400.0; // Базова вага виду
    private static final int MAX_COUNT_PER_CELL = 50; // Макс. на клітинці
    private static final int MAX_SPEED = 2; // Макс. швидкість (клітинок за хід)
    private static final double FOOD_CAPACITY = 50.0; // Потрібно їжі для насичення

    public Boar(int startX, int startY){
        super(startX, startY);
        setInitialSaturation(getFoodCapacity());
    }
    public void eat(Location currentLocation) {
        super.eat(currentLocation);
    }
    @Override
    public String getSpeciesName() {
        return "Boar";
    }

    @Override
    protected int getMaxSpeed() {
        return MAX_SPEED;
    }

    @Override
    protected double getReproductionChance() {
        return 0.3;
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
        return "🐗";
    }
}

