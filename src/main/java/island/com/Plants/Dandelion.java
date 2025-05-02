package island.com.Plants;

public class Dandelion extends Plant {

    public Dandelion(int startX, int startY) {
        super(startX, startY);
    }

    @Override
    public String getSymbol() {
        return "🌼";
    }

    @Override
    public String getSpeciesName() {
        return "Dandelion";
    }

    @Override
    public double getWeight() {
        return 1.0;
    }
}
