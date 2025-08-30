package island.com.Animals.Predators;

import island.com.Map.Location;
import island.com.Plants.Plant;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Bear extends Predators{
    private static final double BASE_WEIGHT = 500.0; // Базова вага виду
    private static final int MAX_COUNT_PER_CELL = 5; // Макс. на клітинці
    private static final int MAX_SPEED = 2; // Макс. швидкість (клітинок за хід)
    private static final double FOOD_CAPACITY = 80.0; // Потрібно їжі для насичення

    public Bear(int startX, int startY){
        super(startX, startY);
        setInitialSaturation(getFoodCapacity() * (0.5 + ThreadLocalRandom.current().nextDouble() * 0.5));
    }

    @Override
    public void eat(Location currentLocation) {
        super.eat(currentLocation);
        if(this.isAlive() && getCurrentSaturation() < getFoodCapacity()){
            eatPlants(currentLocation);
        }
        // Ну вдруг у нас мишка не зможе бігати за швидкими кроликами то хай спробує травку може сподобається =)
    }
    public void eatPlants(Location currentLocation){
        List<Plant> plantInLocation = currentLocation.getPlants();
        if(!plantInLocation.isEmpty()){
            Plant plantToEat  = plantInLocation.get(ThreadLocalRandom.current().nextInt(plantInLocation.size()));
            double foodWeight = plantToEat.getWeight();
            if(foodWeight <= 0){
                return;
            }
            double neededSaturation = FOOD_CAPACITY - getCurrentSaturation();
            double gainedSaturation = Math.min(neededSaturation, foodWeight);
            this.addSaturation(gainedSaturation);
            currentLocation.removePlants(plantToEat);
            System.out.println(this.getSymbol() + " ate " + plantToEat.getSymbol() + " (plant) at (" + this.x + "," + this.y + ")");
        }
    }



    @Override
    public String getSymbol() {
        return "🐻";
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
        return "Bear";
    }

    @Override
    protected int getMaxSpeed() {
        return MAX_SPEED;
    }

    @Override
    protected double getReproductionChance() {
        return 0.1;
    }

}
