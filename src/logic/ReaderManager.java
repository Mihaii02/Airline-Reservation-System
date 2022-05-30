package logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ReaderManager {
    public static BufferedReader createReader() {

        File inputFile = new File("resources/input.txt");
        /*if (inputFile.exists()) {
            System.out.println(inputFile.isFile());
            System.out.println(inputFile.isDirectory());
        }else {
            System.out.println("File doesn't exist!");
        }
        if(!inputFile.canRead()){
            inputFile.setReadable(true);
        }*/
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(inputFile));
            } catch (FileNotFoundException e) {
                System.out.println("buba");
            }
            return reader;
        }
}
