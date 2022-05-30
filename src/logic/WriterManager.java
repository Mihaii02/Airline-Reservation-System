package logic;

import java.io.*;

public class WriterManager {

     private BufferedWriter writer;

    public WriterManager(){

            File outputFile = new File("resources/output.txt");
           /* if(outputFile.exists()){
                System.out.println(outputFile.isFile());
                System.out.println(outputFile.isDirectory());
                System.out.println(outputFile.getAbsolutePath());
            }else{
                System.out.println("Output file doesn't exist");
            }
            if(!outputFile.canWrite()){
                outputFile.setWritable(true);
            }*/
            writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outputFile));
        }catch(IOException e){
            System.out.println("buba 2");
        }
    }
        public void write (String string){
            try {
                writer.write(string);
                writer.newLine();
            } catch (IOException e) {
                System.out.println("alta buba");;
            }
        }
        public void close(){
            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

