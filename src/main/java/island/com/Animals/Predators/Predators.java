package island.com.Animals.Predators;

import island.com.Animals.Animal;
import island.com.Config;
import island.com.Map.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Predators extends Animal {
    public Predators(int startX, int startY) {
        super(startX, startY);
    }

    public void eat(Location location){
    if (!this.isAlive() || getCurrentSaturation() >= getFoodCapacity()) {
        return;
    }
    Map<String, Integer> predatorEatingMap = Config.EAT_PROBABILITIES.get(this.getSpeciesName());
    List<Animal> animalsLoc = location.getAnimals();
    List<Animal> potentialPrey = new ArrayList<>(animalsLoc);
    for(Animal target : potentialPrey) {
        if(this != target && predatorEatingMap.containsKey(target.getSpeciesName())) {
            int probability = predatorEatingMap.get(target.getSpeciesName());
            if (ThreadLocalRandom.current().nextInt(100) < probability) {
                double foodWeight = target.getWeight();
                double neededSaturation = getFoodCapacity() - getCurrentSaturation();
                double gainedSaturation = Math.min(neededSaturation, foodWeight);
                addSaturation(gainedSaturation);
                target.die();
                return;
            }
        }
    }
}
}