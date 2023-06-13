import java.io.Serializable;
import java.util.HashMap;
import java.util.Scanner;
import java.io.*;

public class Main {
    private static HashMap<String, Tamagotchi> petSave = new HashMap<>();

    public static  long currentTime;
    public static long elapsedTime;

    private static GameTick gt;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Tamagotchi!");

        System.out.print("Enter your pet's name: ");
        String petName = scanner.nextLine();

        Tamagotchi pet;
        boolean isNewGame = false;

        System.out.println("Do you want to load a previous game? (Y/N)");
        String loadChoice = scanner.nextLine();

        if (loadChoice.equalsIgnoreCase("Y")) {
            MainSave.Save save = MainSave.loadSave();
            pet = save.getPet();

            for (int i = 0; i < gt.getElapsedTicks(); i++) {
                pet.update();
            }

        } else {
            pet = new Tamagotchi(petName);
            gt = new GameTick();
            isNewGame = true;
        }

        long lastTickTime = System.currentTimeMillis();

        while (true) {
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. Feed");
            System.out.println("2. Play");
            System.out.println("3. Sleep");
            System.out.println("4. Status");
            System.out.println("5. Save");
            System.out.println("6. Quit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> pet.feed();
                case 2 -> pet.play();
                case 3 -> pet.sleep();
                case 4 -> pet.printStatus();
                case 5 -> {
                    MainSave.saveGame(pet, lastTickTime);
                    petSave.put(petName, pet);

                    MainSave.writeSavGameToTxt(petSave);
                    System.out.println("Game saved.");
                }
                case 6 -> {
                    if (isNewGame) {
                        System.out.println("Thanks for playing Tamagotchi!");
                    } else {
                        MainSave.saveGame(pet, lastTickTime);
                        System.out.println("Game saved. Thanks for playing Tamagotchi!");
                    }
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }

            currentTime = System.currentTimeMillis();
            elapsedTime = currentTime - lastTickTime;

            if (elapsedTime >= 10000) {
                int elapsedTicks = (int) (elapsedTime / 10000);
                for (int i = 0; i < elapsedTicks; i++) {
                    pet.update();
                }
                lastTickTime = currentTime;
            }
        }
    }


    static class Tamagotchi implements Serializable {
        private String name;
        private int hunger;
        private int boredom;
        private int tiredness;

        private boolean isAlive;

        public void setName(String name) {
            this.name = name;
        }

        public void setHunger(int hunger) {
            this.hunger = hunger;
        }

        public void setBoredom(int boredom) {
            this.boredom = boredom;
        }

        public void setTiredness(int tiredness) {
            this.tiredness = tiredness;
        }

        public String getName() {
            return name;
        }

        public int getHunger() {
            return hunger;
        }

        public int getBoredom() {
            return boredom;
        }

        public int getTiredness() {
            return tiredness;
        }

        public Tamagotchi(String name) {
            this.name = name;
            this.hunger = 5;
            this.boredom = 5;
            this.tiredness = 5;
            this.isAlive = true;
        }

        public void feed() {
            hunger++;
            if (hunger < 0 ) {
                hunger = 0;
            }
            System.out.println(name + " has been fed.");
        }

        public void play() {
            boredom++;
            if (boredom < 0)
                boredom = 0;
            System.out.println(name + " has played with you.");
        }

        public void sleep() {
            tiredness++;
            if (tiredness < 0)
                tiredness = 0;
            System.out.println(name + " has gone to sleep.");
        }

        public void update() {

            int milliToMin = 60000;
            hunger--;
            boredom--;
            tiredness--;
            long  currentTime;

            System.out.println(name + " has been updated.");
            if(hunger <= 0){
                System.out.println(this.name + " is hungry.");

                currentTime = gt.getLastTickTime();

                if(currentTime - elapsedTime == (milliToMin * 5)){

                    isAlive = false;

                    System.out.println("Your pet had died!");

                    System.out.println("Game Over!");
                    System.exit(0);

                }

            }
        }

        public void printStatus() {
            System.out.println("\n" + name + "'s status:");
            System.out.println("Hunger: " + hunger);
            System.out.println("Boredom: " + boredom);
            System.out.println("Tiredness: " + tiredness);
        }
    }
}





