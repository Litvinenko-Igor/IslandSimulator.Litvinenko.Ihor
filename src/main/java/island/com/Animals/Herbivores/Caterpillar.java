package island.com.Animals.Herbivores;

import island.com.Map.Location;

public class Caterpillar extends Herbivore{
    private static final double BASE_WEIGHT = 0.01; // Базова вага виду
    private static final int MAX_COUNT_PER_CELL = 1000; // Макс. на клітинці
    private static final int MAX_SPEED = 0; // Макс. швидкість (клітинок за хід)
    private static final double FOOD_CAPACITY = 0.05; // Потрібно їжі для насичення

    public Caterpillar(int startX, int startY){
        super(startX, startY);
        setInitialSaturation(getFoodCapacity());
    }
    public void eat(Location currentLocation) {
        super.eat(currentLocation);
    }
    @Override
    public String getSpeciesName() {
        return "Caterpillar";
    }

    @Override
    protected int getMaxSpeed() {
        return MAX_SPEED;
    }

    @Override
    protected double getReproductionChance() {
        return 0.7;
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
        return "🐛";
    }
}
