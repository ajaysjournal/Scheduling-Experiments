package all.util;

/**
 * Created by Ajay on 11/29/16.
 * Reference - https://www.mkyong.com/java/how-to-write-to-file-in-java-fileoutputstream-example/
 */


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Files {

    public static void createFile(String fileName, String content) {

        FileOutputStream fop = null;
        File file;

        try {

            file = new File(fileName);
            fop = new FileOutputStream(file);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // get the content in bytes
            byte[] contentInBytes = content.getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*public static void main(String[] args) {
        Files.createFile(""+Math.random()+System.currentTimeMillis(),"ajay");
    }*/
}

