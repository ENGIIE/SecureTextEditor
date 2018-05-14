import chapter2.Utils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.Provider;
import java.security.Security;
import java.util.Arrays;

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
     * Using AES
     * @param text
     * @throws IOException
     */
    public void aesEncryptText (String text) throws IOException {


        try {

            //add BouncyCastle Provider
            Security.addProvider(new BouncyCastleProvider());
            Provider provider = Security.getProvider("BC");

            //Text into byte array
            byte[] input = text.getBytes();

            System.out.println(Arrays.toString(input));

            //Key byte array
            byte[]        keyBytes = new byte[] {
                    0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                    0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
                    0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17 };

            //Creating new SecretKey
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

            //Instantiate Cipher
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding", "BC");


            System.out.println("input text : " + Utils.toHex(input));

            // encryption pass

            //
            byte[] cipherText = new byte[input.length];

            cipher.init(Cipher.ENCRYPT_MODE, key);

            int ctLength = cipher.update(input, 0, input.length, cipherText, 0);

            ctLength += cipher.doFinal(cipherText, ctLength);

            System.out.println("cipher text: " + Utils.toHex(cipherText) + " bytes: " + ctLength);

            // decryption pass

            byte[] plainText = new byte[ctLength];

            cipher.init(Cipher.DECRYPT_MODE, key);

            int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);

            ptLength += cipher.doFinal(plainText, ptLength);

            System.out.println("plain text : " + Utils.toHex(plainText) + " bytes: " + ptLength);


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
