package island.com.Animals.Herbivores;

import island.com.Animals.Animal;
import island.com.Map.Location;
import island.com.Plants.Plant;

import java.util.List;

public abstract class Herbivore extends Animal {
    protected Herbivore(int startX, int startY) {
        super(startX, startY);
    }

    @Override
    public void eat(Location location) {
        if (!this.isAlive() || getCurrentSaturation() >= getFoodCapacity()) {
            return;
        }

        List<Plant> availablePlants  = location.getPlants();
        if(!availablePlants.isEmpty()){
            for(Plant plant : availablePlants ){
                double foodWeight = plant.getWeight();
                if(foodWeight > 0){
                    double neededSaturation = getFoodCapacity() - getCurrentSaturation();
                    double gainedSaturation = Math.min(neededSaturation, foodWeight);
                    addSaturation(gainedSaturation);
                    location.removePlants(plant);
                    return;
                }
            }
        }
    }
}
