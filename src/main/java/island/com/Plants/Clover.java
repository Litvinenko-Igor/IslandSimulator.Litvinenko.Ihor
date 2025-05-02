package island.com.Plants;


public class Clover extends Plant {
    public Clover(int startX, int startY) {
        super(startX, startY);
    }

    @Override
    public String getSymbol() {
        return "🍀";
    }


    @Override
    public String getSpeciesName() {
        return "Clover";
    }

    @Override
    public double getWeight() {
        return 1.0;
    }
}
