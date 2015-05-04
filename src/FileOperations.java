import java.io.*;

/**
 * Created by wernermostert on 2015/04/28.
 */

/**
 * This is a simple utility library for file operations.
 */
public class FileOperations {
    /**
     * Reads a text file
     * @param filename The name of the file
     * @return The contents of the file
     * @throws FileNotFoundException The file does not exist
     * @throws IOException An IO error occured
     */
    public static String readFile(String filename) throws FileNotFoundException, IOException
    {
        String content = "";
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null)
            if(!line.equals(""))
            content += line+"\r\n";

        return content;
    }

    /**
     * Writes content to a file.
     * @param content String representation of file content to be written
     * @param filename Name of the file to which is to be written
     */
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
