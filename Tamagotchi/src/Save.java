import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Save {

    private final static String SAVE_FILE = "SaveFile.txt";
    private final static File Player_Save_File = new File(SAVE_FILE);

    public static void wrtieSavefileToTxt(HashMap<Integer, Mood> hashMap) {

        BufferedWriter bf;

        try {
            {
                if (Player_Save_File.delete()) {
                    System.out.println("\nFile has Been Updated\n");
                }
                if (Player_Save_File.createNewFile()) {

                    bf = new BufferedWriter(new FileWriter(Player_Save_File));

                    for (Map.Entry<Integer, Mood> entry : hashMap.entrySet()) {
                        //TODO:: fill out data for mood to be saved in saveFile
//                        bf.write();
                    }
                }
            }
        } catch (IOException ignore) {
        }
    }

}
