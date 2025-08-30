package island.com.Plants;

public class Leaves extends Plant {

    public Leaves(int startX, int startY) {
        super(startX, startY);
    }

    @Override
    public String getSymbol() {
        return "🍃";
    }
    @Override
    public String getSpeciesName() {
        return "Leaves";
    }

    @Override
    public double getWeight() {
        return 1.0;
    }
}
