package island.com.Plants;

public abstract class Plant {
    public abstract String getSymbol();
    public abstract String getSpeciesName();
    public abstract double getWeight();
    protected int x;
    protected int y;
    protected Plant(int startX, int startY){
        this.x = startX;
        this.y = startY;
    }
}
