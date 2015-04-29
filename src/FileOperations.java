import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by wernermostert on 2015/04/28.
 */

/**
 * This is a simple utility library for file operations.
 */
public class FileOperations {
    public static String readFile(String filename)
    {
        String content = null;
        File file = new File(filename);
        try {
            FileReader reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    public static void writeToFile(String content, String filename){
        FileWriter fw= null;
        File file =null;
        try {
            file=new File(filename);
            if(!file.exists()) {
                if(!file.createNewFile()) throw new IOException();
            }
            fw = new FileWriter(file);
            fw.write(content);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
