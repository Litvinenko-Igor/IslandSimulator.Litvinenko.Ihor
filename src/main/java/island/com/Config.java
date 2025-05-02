package island.com;


import java.util.Map;
import java.util.HashMap;

public class Config {

    public static final Map<String,Map<String, Integer>> EAT_PROBABILITIES = new HashMap<>();
    public static final Map<String, Integer> GLOBAL_POPULATION_LIMITS = new HashMap<>();

    static {
        Map<String, Integer> bearProb = new HashMap<>();
        bearProb.put("Boa", 80);
        bearProb.put("Horse", 40);
        bearProb.put("Deer", 80);
        bearProb.put("Rabbit", 80);
        bearProb.put("Mouse", 90);
        bearProb.put("Goat", 70);
        bearProb.put("Sheep", 70);
        bearProb.put("Boar", 50);
        bearProb.put("Buffalo", 20);
        bearProb.put("Duck", 10);
        EAT_PROBABILITIES.put("Bear", bearProb);
        Map<String, Integer> eagleProb = new HashMap<>();
        eagleProb.put("Fox", 10);
        eagleProb.put("Rabbit", 90);
        eagleProb.put("Mouse", 90);
        eagleProb.put("Duck", 80);
        EAT_PROBABILITIES.put("Eagle", eagleProb);
        Map<String, Integer> foxProb = new HashMap<>();
        foxProb.put("Rabbit", 70);
        foxProb.put("Mouse", 90);
        foxProb.put("Duck", 60);
        foxProb.put("Caterpillar", 40);
        EAT_PROBABILITIES.put("Fox", foxProb);
        Map<String, Integer> snakeProb = new HashMap<>();
        snakeProb.put("Fox", 15);
        snakeProb.put("Rabbit", 20);
        snakeProb.put("Mouse", 40);
        snakeProb.put("Duck", 10);
        EAT_PROBABILITIES.put("Snake", snakeProb);
        Map<String, Integer> wolfProb = new HashMap<>();
        wolfProb.put("Horse", 10);
        wolfProb.put("Deer", 15);
        wolfProb.put("Rabbit", 60);
        wolfProb.put("Mouse", 80);
        wolfProb.put("Goat", 60);
        wolfProb.put("Sheep", 70);
        wolfProb.put("Buffalo", 10);
        wolfProb.put("Boar", 15);
        wolfProb.put("Duck", 40);
        EAT_PROBABILITIES.put("Wolf", wolfProb);
        Map<String, Integer> duckProb = new HashMap<>();
        duckProb.put("Caterpillar",90);
        EAT_PROBABILITIES.put("Duck", duckProb);

        GLOBAL_POPULATION_LIMITS.put("Wolf", 50);
        GLOBAL_POPULATION_LIMITS.put("Snake", 75);
        GLOBAL_POPULATION_LIMITS.put("Fox", 100);
        GLOBAL_POPULATION_LIMITS.put("Bear", 10);
        GLOBAL_POPULATION_LIMITS.put("Eagle", 40);

        GLOBAL_POPULATION_LIMITS.put("Horse", 40);
        GLOBAL_POPULATION_LIMITS.put("Deer", 75);
        GLOBAL_POPULATION_LIMITS.put("Rabbit", 750);
        GLOBAL_POPULATION_LIMITS.put("Mouse", 1000);
        GLOBAL_POPULATION_LIMITS.put("Goat", 200);
        GLOBAL_POPULATION_LIMITS.put("Sheep", 200);
        GLOBAL_POPULATION_LIMITS.put("Boar", 100);
        GLOBAL_POPULATION_LIMITS.put("Buffalo", 25);
        GLOBAL_POPULATION_LIMITS.put("Duck", 300);
        GLOBAL_POPULATION_LIMITS.put("Caterpillar", 2000);
    }


}
