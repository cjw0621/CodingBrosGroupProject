import java.io.Serializable;
import java.io.*;
import java.util.Scanner;


    public class Test {
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
                Save1 save = loadSave();
                pet = save.getPet();
                long lastTickTime = save.getLastTickTime();
                long elapsedTime = System.currentTimeMillis() - lastTickTime;
                int elapsedTicks = (int) (elapsedTime / 10000);
                for (int i = 0; i < elapsedTicks; i++) {
                    pet.update();
                }
            } else {
                pet = new Tamagotchi(petName);
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
                        saveGame(pet, lastTickTime);
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

                if (elapsedTime >= 10000) {
                    int elapsedTicks = (int) (elapsedTime / 10000);
                    for (int i = 0; i < elapsedTicks; i++) {
                        pet.update();
                    }
                    lastTickTime = currentTime;
                }
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

    class Tamagotchi implements Serializable {
        private String name;
        private int hunger;
        private int boredom;
        private int tiredness;

        public Tamagotchi(String name) {
            this.name = name;
            this.hunger = 0;
            this.boredom = 0;
            this.tiredness = 0;
        }

        public void feed() {
            hunger--;
            if (hunger < 0)
                hunger = 0;
            System.out.println(name + " has been fed.");
        }

        public void play() {
            boredom--;
            if (boredom < 0)
                boredom = 0;
            System.out.println(name + " has played with you.");
        }

        public void sleep() {
            tiredness--;
            if (tiredness < 0)
                tiredness = 0;
            System.out.println(name + " has gone to sleep.");
        }

        public void update() {
            hunger++;
            boredom++;
            tiredness++;
            System.out.println(name + " has been updated.");
        }

        public void printStatus() {
            System.out.println("\n" + name + "'s status:");
            System.out.println("Hunger: " + hunger);
            System.out.println("Boredom: " + boredom);
            System.out.println("Tiredness: " + tiredness);
        }
    }

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

        public long getLastTickTime() {
            return lastTickTime;
        }
    }



