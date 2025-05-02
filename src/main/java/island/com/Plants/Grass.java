package island.com.Plants;

public class Grass extends Plant {

    public Grass(int startX, int startY) {
        super(startX, startY);
    }

    @Override
    public String getSymbol() {
        return "🌱";
    }

    @Override
    public String getSpeciesName() {
        return "Grass";
    }

    @Override
    public double getWeight() {
        return 1.0;
    }
}
