import java.io.*;


public class FileManager {

    //FileWriter and FileReader
    FileWriter fileWriter;
    FileReader fileReader;

    //String with current Path
    String currentPath;

    //Save current Document Name
    String tempName;




    /**
     * Constructor
     */
    public FileManager() {

        fileWriter = null;
        fileReader = null;
        System.out.println("Aufruf");

    }


    /**
     * Save Text into File
     * @param text
     * @throws IOException
     */
    public void saveText (String text) throws IOException {


        try {
            //Current Path has to be available
            if(currentPath != null) {
                //fileWriter = new FileWriter("./src/main/java/savefiles/"+tempName+".txt");

                //Create new Writer
                fileWriter = new FileWriter(currentPath);

                //Write Text into File
                fileWriter.write(text);

                //Close Writer
                fileWriter.close();
            }


        }catch (Exception ex){
            ex.printStackTrace();
        }

    }


    /**
     * Load File Text into String
     * @param file
     * @return
     * @throws IOException
     */
    public  String loadText (File file) throws IOException {

        //Current Text
        String text = "";

        try {

            //Create new FileReader and BufferedReader
            fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            //Current row
            String row = "";


            //As long as there are still lines
            while( (row = bufferedReader.readLine()) != null )
            {
                //Append row on String text
                text+= row;
            }

            //Close FileReader
            fileReader.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        System.out.println(text);
        return text;
    }


    //Set and get currentPath

    public void setPath (String path) {
        currentPath = path;
    }

    public String getPath () {
        return currentPath;
    }


    ////Set and get tempName

    public void setFileName (String filename) {

        tempName = filename;
    }

    public String getFileName () {

        return tempName;
    }

}
