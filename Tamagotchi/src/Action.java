import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Action {


//Establishes the base for health
//Is  changed throughout the two classes (Action and Mood)
    //private int health = 10, hunger = 10 , happiness = 10, fitness = 10;

    static Map<String, Double> emotions = new HashMap<>();

    public static void mood(double mood) {
        //int health = 10, hunger = 10, happiness = 10, fitness = 10;

        emotions.put("Health", 10.0);
        emotions.put("Hunger", 10.0);
        emotions.put("Happiness", 10.0);
        emotions.put("Fitness", 10.0);

        for (String key : emotions.keySet()) {
            mood = mood + emotions.get(key);
        }
        mood /= 4;

        emotions.put("Mood", mood);

        System.out.println(emotions);



    }

    public static void actions(Scanner s) {

    }

    public static boolean isAlive(double health) {
        // health = emotions.get("Health");
        if (health == 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static void main(String[] args) {

        //System.out.println(mood(0.0));

        while(isAlive(emotions.get("Health")))
        {

        }
    }


}