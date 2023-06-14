import java.io.Serializable;
import java.io.*;
import java.util.HashMap;
import java.util.*;
import java.util.Scanner;

/**
 *
 * Tamagotchi game: Created by Chase, Ethan, Kam, Kyshawn
 *
 *  The game uses the players system time to track and update the pets status to create a sense of life to the
 *  virtual pet
 *
 *  Methods used: main(), writePetStatusTxt(), readPetStatusTxt(), saveGame(Tamagotchi pet, long lastTickTime),
 *  loadSave().
 *
 **/
public class Game {
    static HashMap<String, List<Integer>> stats = new HashMap<>();

    private final static File PET_SAVE_FILE = new File("tamagotchi.txt");

    private static Tamagotchi pet;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Tamagotchi!");

        boolean isNewGame = false;

        System.out.println("Do you want to load a previous game? (Y/N)");
        String loadChoice = scanner.nextLine();

        if (loadChoice.equalsIgnoreCase("Y")) {

               readPetStatusTxt();
               Save1 save = loadSave();
               pet = save.getPet();
               long lastTickTime = save.getLastTickTime();
               long elapsedTime = System.currentTimeMillis() - lastTickTime;

               //updates the pet status every hour that has been elapsed and not played.
               int elapsedTicks = (int) (elapsedTime / 3600000);
               for (int i = 1; i < elapsedTicks; i++) {
                   pet.update();
               }
        } else {
            System.out.print("Enter your pet's name: ");
            String petName = scanner.nextLine();
            pet = new Tamagotchi(petName);
            isNewGame = true;
        }

        long lastTickTime = System.currentTimeMillis();

        while (pet.isAlive()) {
            pet.mood();
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. Feed");
            System.out.println("2. Play");
            System.out.println("3. Sleep");
            System.out.println("4. Status");
            System.out.println("5. Save");
            System.out.println("6. Quit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    pet.feed();
                    break;
                case 2:
                    pet.play();
                    break;
                case 3:
                    pet.sleep();
                    break;
                case 4:
                    pet.printStatus();
                    break;
                case 5:
                    pet.gameData();
                    saveGame(pet, lastTickTime);
                    writePetStatusTxt(stats);
                    System.out.println("Game saved.");
                    break;
                case 6:
                    if (isNewGame) {
                        System.out.println("Thanks for playing Tamagotchi!");
                    } else {
                        saveGame(pet, lastTickTime);
                        System.out.println("Game saved. Thanks for playing Tamagotchi!");
                    }
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }


            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - lastTickTime;

            //If the elapsed time is past 10 seconds alter the status of John
            if (elapsedTime >= 10000) {
                int elapsedTicks = (int) (elapsedTime / 10000);
                for (int i = 0; i < elapsedTicks; i++) {
                    pet.update();
                }
                lastTickTime = currentTime;
            }
        }
    }

    /*
     * writePetStatusTxt(hashmap moodstats)
     * creates a .txt save file. Mainly used as a redundancy save file for proof of concept.
     */
    private static void writePetStatusTxt(HashMap<String, List<Integer>> moodStats) {

        BufferedWriter bf;

        try {
            if (PET_SAVE_FILE.createNewFile()) {

                bf = new BufferedWriter(new FileWriter("tamagotchi.txt"));
                for (String entry : moodStats.keySet()) {

                    bf.write(moodStats.keySet() + ": " + moodStats.get(entry).toString());
                    bf.newLine();

                }
                bf.flush();
             }
            } catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    /*
     * readPetStatusTxt()
     *
     * checks if the tamagotchi.txt file exists, reads it, and updates the game with loaded information so that
     * the player may continue playing their game.
     */

    public static boolean readPetStatusTxt() {

        try {
            if(PET_SAVE_FILE.exists()){
                String lineEm;
                List<Integer> intMoodStats = new LinkedList<>();
                FileReader frEm = new FileReader(PET_SAVE_FILE);
                BufferedReader brEm = new BufferedReader(frEm);

                while((lineEm = brEm.readLine()) != null) {

                    String[] parts = lineEm.split(": ");

                    String tempHold = parts[1];
                    tempHold = tempHold.replaceAll("[\\[\\](){}]", "");

                    String[] convertedMoodStats = tempHold.split(", ");

                    for(String moodStats : convertedMoodStats){

                        intMoodStats.add(Integer.parseInt(moodStats));
                    }

                    stats.put(parts[0].replaceAll("[\\[\\](){}]", ""), intMoodStats);

                }

                System.out.println("File Loaded Successfully!");

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    private static void saveGame(Tamagotchi pet, long lastTickTime) {
        try {
            FileOutputStream fileOut = new FileOutputStream("tamagotchi.sav");
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            Save1 save = new Save1(pet, lastTickTime);
            objectOut.writeObject(save);
            objectOut.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * loadSave(), reads the'tamagotchi.scv' file (serialized file)
     * @return a save1 object (dycrpted serialized (Tamagotchi pet, lastTickTime) object.
     */
    private static Save1 loadSave() {
        Save1 save = null;
        try {
            FileInputStream fileIn = new FileInputStream("tamagotchi.sav");
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            save = (Save1) objectIn.readObject();
            objectIn.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return save;
    }
}

/**
 * Tamagotchi Class (innerClass of Game (super)) builds ands is the main operation of the game.
 *
 * Methods includes: feed(), play(), sleep(), mood(), update(), gameData(), isAlive(), printStatus(),
 */
class Tamagotchi implements Serializable {
    private String name;
    private int hunger;
    private int boredom;
    private int tiredness;
    private int mood;

    public int randInt(){
        Random rand = new Random();
        int upperbound = 3;

        return rand.nextInt(upperbound);
    }


    public Tamagotchi(String name) {
        this.name = name;
        this.hunger = 0;
        this.boredom = 0;
        this.tiredness = 0;
        this.mood = 100;
    }


    public void feed() {
        hunger--;
        tiredness+=randInt();
        mood++;
        if (hunger < 0)
            hunger = 0;
        System.out.println(name + " has been fed.");

        printStatus();
    }

    public void play() {
        boredom--;
        hunger+=randInt();
        tiredness+=randInt();
        mood++;
        if (boredom < 0)
            boredom = 0;
        System.out.println(name + " has played with you.");

        printStatus();
    }

    public void sleep() {
        tiredness--;
        boredom+=randInt();
        hunger+=randInt();
        mood++;
        if (tiredness < 0)
            tiredness = 0;
        System.out.println(name + " has gone to sleep.");

        printStatus();
    }

    /*
     * checks the mood status and prints the picture of the pet with corresponding mood
     *
     */

    public void mood()
    {
        if (mood > 100)
        {
            mood = 100;
        }

        if (mood > 70)
        {
            System.out.println("       .=\"=.\n" +
                    "     _/.-.-.\\_     _\n" +
                    "    ( ( o o ) )    ))\n" +
                    "     |/  \"  \\|    //\n" +
                    "      \\'---'/    //\n" +
                    "      /`\"\"\"`\\\\  ((\n" +
                    "     / /_,_\\ \\\\  \\\\\n" +
                    "     \\_\\\\_'__/ \\  ))\n" +
                    "     /`  /`~\\  |//\n" +
                    "    /   /    \\  /\n" +
                    ",--`,--'\\/\\    /\n" +
                    " '-- \"--'  '--'");
        }
        if(mood <= 70 && mood>= 40)
        {
            System.out.println("       .=\"=.\n" +
                    "     _/.-.-.\\_     _\n" +
                    "    ( ( o o ) )    ))\n" +
                    "     |/  \"  \\|    //\n" +
                    "      \\ --- /    //\n" +
                    "      /`\"\"\"`\\\\  ((\n" +
                    "     / /_,_\\ \\\\  \\\\\n" +
                    "     \\_\\\\_'__/ \\  ))\n" +
                    "     /`  /`~\\  |//\n" +
                    "    /   /    \\  /\n" +
                    ",--`,--'\\/\\    /\n" +
                    " '-- \"--'  '--'");
        }
        if (mood < 40 && mood > 0)
        {
            System.out.println("       .=\"=.\n" +
                    "     _/.-.-.\\_     _\n" +
                    "    ( ( - - ) )    ))\n" +
                    "     |/  \"  \\|    //\n" +
                    "      \\~~~~ /    //\n" +
                    "      /`\"\"\"`\\\\  ((\n" +
                    "     / /_,_\\ \\\\  \\\\\n" +
                    "     \\_\\\\_'__/ \\  ))\n" +
                    "     /`  /`~\\  |//\n" +
                    "    /   /    \\  /\n" +
                    ",--`,--'\\/\\    /\n" +
                    " '-- \"--'  '--'");
        }

        if (mood < 10 && mood > 0)
        {
            System.out.println("|-|");
        }
        for(int i = 0; i < (mood + 1) / 10; i++)
        {
            if(i == 0)
            {
                System.out.print("|");
            }
            System.out.print("-|");

        }
    }

    /*
     * update() returns nothing, updates the pets status
     */

    public void update() {
        hunger+=randInt();
        boredom+=randInt();
        tiredness+=randInt();
        mood = 100 - hunger - boredom - tiredness;

        System.out.println(name + " has been updated.");
    }

    /*
     * updates the hashMap that is used to be saved
     */
    public void gameData(){
        List<Integer> statusList = new LinkedList<>();

        statusList.add(hunger);
        statusList.add(boredom);
        statusList.add(tiredness);
        statusList.add(mood);

        Game.stats.put(this.name, statusList);
    }

    /*
     * isAlive() returns true if mood is above 0, false if mood <= 0;
     */
    public boolean isAlive()
    {
        if (mood <= 0)
        {
            System.out.println("       .=\"=.\n" +
                    "     _/.-.-.\\_     _\n" +
                    "    ( ( X X ) )    ))\n" +
                    "     |/  \"  \\|    //\n" +
                    "      \\(--u) /    //\n" +
                    "      /`\"\"\"`\\\\  ((\n" +
                    "     / /_,_\\ \\\\  \\\\\n" +
                    "     \\_\\\\_'__/ \\  ))\n" +
                    "     /`  /`~\\  |//\n" +
                    "    /   /    \\  /\n" +
                    ",--`,--'\\/\\    /\n" +
                    " '-- \"--'  '--'");

            System.out.println(name + "Is dead");
            System.out.println("Thanks for playing!!!");

            return false;
        }
        else
        {
            return true;
        }
    }

    /*
     * printStatus() updates the pets database, prints current status inside the database.
     */
    public void printStatus() {

        System.out.println("\n" + name + "'s status:");

        List<Integer> statusList = new LinkedList<>();

        statusList.add(hunger);
        statusList.add(boredom);
        statusList.add(tiredness);
        statusList.add(mood);

        Game.stats.put(this.name, statusList);

        for(int i = 0; i < statusList.size(); i++){
            if(i == 0){
                System.out.println("hunger: " + statusList.get(0));
            } else if( i == 1){
                System.out.println("boredom: " + statusList.get(1));
            } else if( i == 2){
                System.out.println("tiredness: " + statusList.get(2));
            } else if( i == 3){
                System.out.println("mood: " + statusList.get(3));
            }
        }
    }

}

/**
 * serializes the game data to be saved in a .sav file. ensures that save file cant be altered or corrupted.
 *
 * method includes: getPet(), getLastTickTime()
 */

class Save1 implements Serializable {
    private Tamagotchi pet;
    private long lastTickTime;

    public Save1(Tamagotchi pet, long lastTickTime) {
        this.pet = pet;
        this.lastTickTime = lastTickTime;
    }

    public Tamagotchi getPet() {
        return pet;
    }

    /*
     * returns the saved lastTickTime
     */

    public long getLastTickTime() {
        return lastTickTime;
    }
}



