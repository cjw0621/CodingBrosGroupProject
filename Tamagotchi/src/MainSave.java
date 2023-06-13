import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MainSave {

    private final static File TAMOGATCHI_SAV_FILE = new File("Tamagotchi.txt");

    public static void writeSavGameToTxt (HashMap<String, Main.Tamagotchi> hashMap){
        BufferedWriter bf;

        try
        {
            if(TAMOGATCHI_SAV_FILE.delete()){
                System.out.println("\nFile Has Been Updated");
            }
            if(TAMOGATCHI_SAV_FILE.createNewFile()){

                bf = new BufferedWriter(new FileWriter(TAMOGATCHI_SAV_FILE));

                for(Map.Entry<String, Main.Tamagotchi> entry : hashMap.entrySet()) {
                    bf.write(entry.getKey() + ": " + "["+entry.getValue().getName() +", " +
                            entry.getValue().getHunger() + "," + entry.getValue().getBoredom()+
                            ", " + entry.getValue().getTiredness());
                    bf.newLine();
                }

                bf.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadTamagotchiSav(){

        HashMap<String, String> tempTamagotchiHashMap = new HashMap<>();

        HashMap<String, Main.Tamagotchi> tamagotchiHashMap = new HashMap<>();

        try
        {
            String lineEm;
            FileReader frEm = new FileReader("Tamagotchi.txt");
            BufferedReader brEm = new BufferedReader(frEm);

            while((lineEm = brEm.readLine()) != null)
            {

                String[] parts = lineEm.split(":");
                String index = parts[0].trim();
                String tamagotchi = parts[1].trim();
                tempTamagotchiHashMap.put(index, tamagotchi);
            }
            for(String petname : tempTamagotchiHashMap.keySet()){
                tempTamagotchiHashMap.put(petname, tempTamagotchiHashMap.get(petname).replaceAll("\\[","")
                        .replaceAll("\\]", ""));

                String[] strTamaGotchi = tempTamagotchiHashMap.get(petname).split(",");

                String petName = strTamaGotchi[0];
                int hunger = Integer.parseInt(strTamaGotchi[1]);
                int boredom = Integer.parseInt(strTamaGotchi[2]);
                int tiredness = Integer.parseInt(strTamaGotchi[3]);

                Main.Tamagotchi tg = new Main.Tamagotchi(petName);
                tg.setName(petName);
                tg.setHunger(hunger);
                tg.setTiredness(tiredness);
                tg.setBoredom(boredom);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public static void saveGame(Main.Tamagotchi pet, long lastTickTime) {
        try {
            FileOutputStream fileOut = new FileOutputStream("tamagotchi.sav");
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            Save save = new Save(pet, lastTickTime);
            objectOut.writeObject(save);
            objectOut.close();
            fileOut.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Save loadSave() {
        Save save = null;
        try {
            FileInputStream fileIn = new FileInputStream("tamagotchi.sav");
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            save = (Save) objectIn.readObject();
            objectIn.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return save;
    }

    static class Save implements Serializable {
        private Main.Tamagotchi pet;
        private long lastTickTime;

        public Save(Main.Tamagotchi pet, long lastTickTime) {
            this.pet = pet;
            this.lastTickTime = lastTickTime;
        }

        public Main.Tamagotchi getPet() {
            return pet;
        }

        public long getLastTickTime() {
            return lastTickTime;
        }
    }

}
