package island.com.Animals;

import island.com.Config;
import island.com.Map.Island;
import island.com.Map.Location;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Animal {
    public abstract void eat(Location currentLocation);

    public abstract String getSymbol();

    public abstract double getFoodCapacity();

    public abstract int getMaxCountPerCell();

    public abstract double getWeight();

    public abstract String getSpeciesName();

    protected abstract int getMaxSpeed();

    protected abstract double getReproductionChance();

    private static final int[] dx = {0, 1, 1, 1, 0, -1, -1, -1};
    private static final int[] dy = {-1, -1, 0, 1, 1, 1, 0, -1};
    private static final double MOVEMENT_COST_FACTOR = 0.01;


    protected int x;
    protected int y;
    private boolean isAlive = true;
    private double currentSaturation;

    protected Animal(int startX, int startY) {
        this.x = startX;
        this.y = startY;

    }

    public boolean isAlive() {
        return this.isAlive;
    }

    public void die() {
        this.isAlive = false;
    }

    public double getCurrentSaturation() {
        return this.currentSaturation;
    }

    public void decreaseSaturation(double amount) {
        if (this.isAlive && amount > 0) {
            this.currentSaturation -= amount;
            if (this.currentSaturation < 0) {
                this.currentSaturation = 0;
            }
        }
    }

    protected void addSaturation(double amount) {
        if (this.isAlive && amount > 0) {
            this.currentSaturation = Math.min(this.currentSaturation + amount, getFoodCapacity());
        }
    }

    protected void setInitialSaturation(double initialSaturation) {
        this.currentSaturation = Math.min(initialSaturation, getFoodCapacity());
        if (this.currentSaturation < 0) this.currentSaturation = 0;
    }

    public void move(Island island) {
        int maxSpeed = getMaxSpeed();
        if (maxSpeed <= 0) {
            return;
        }
        int directionIndex = ThreadLocalRandom.current().nextInt(dx.length);

        int distance = ThreadLocalRandom.current().nextInt(1, maxSpeed + 1);

        int deltaX = dx[directionIndex] * distance;

        int deltaY = dy[directionIndex] * distance;

        int potentialNewX = this.x + deltaX;

        int potentialNewY = this.y + deltaY;

        int clampedX = Math.max(0, Math.min(potentialNewX, island.getWidth() - 1));

        int clampedY = Math.max(0, Math.min(potentialNewY, island.getHeight() - 1));

        boolean moved = island.attemptMove(this, clampedX, clampedY);
        if (moved) {
            this.x = clampedX;
            this.y = clampedY;
        }
        if (isAlive()) {
            double energyCost = getFoodCapacity() * MOVEMENT_COST_FACTOR;
            decreaseSaturation(energyCost);
        }
    }

    public List<Animal> reproduce(Island island) {
        if (!this.isAlive()) {
            return Collections.emptyList();
        }
        String species = this.getSpeciesName();
        int globalCount = island.getPopulationStats().getOrDefault(species, 0);
        int globalLimit = Config.GLOBAL_POPULATION_LIMITS.getOrDefault(species, Integer.MAX_VALUE);

        if (globalCount >= globalLimit) {
            return Collections.emptyList();
        }
        Location currentLocation = island.getLocation(this.x, this.y);
        if (currentLocation == null) {
            return Collections.emptyList();
        }
        List<Animal> animalsHere = currentLocation.getAnimals();
        Animal partner = null;
        for (Animal potentialMate : animalsHere) {
            if (this.getSpeciesName().equals(potentialMate.getSpeciesName())
                    && potentialMate.isAlive()
                    && potentialMate.getCurrentSaturation() >= potentialMate.getFoodCapacity() * 0.5
                    && potentialMate != this) {
                partner = potentialMate;
                break;
            }
        }

        if (partner == null) {
            return Collections.emptyList();
        }

        if (ThreadLocalRandom.current().nextDouble() < getReproductionChance()) {
            int limitPerCell = getMaxCountPerCell();
            int currentCellCount = currentLocation.countAnimalsOfType(this.getClass());
            if (currentCellCount < limitPerCell) {
                try {
                    Constructor<? extends Animal> constructor = this.getClass().getDeclaredConstructor(int.class, int.class);
                    Animal offspring = constructor.newInstance(this.x, this.y);
                    offspring.setInitialSaturation(offspring.getFoodCapacity() * 0.6);
                    this.decreaseSaturation(this.getFoodCapacity() * 0.2);
                    partner.decreaseSaturation(partner.getFoodCapacity() * 0.2);
                    return Collections.singletonList(offspring);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Collections.emptyList();
                }
            } else {
                return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();
        }
    }


    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
}
