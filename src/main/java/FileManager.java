import chapter2.Utils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.Charset;
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


    //Testinput
    byte[]        testinput = new byte[] {
            0x00, 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77,
            (byte)0x88, (byte)0x99, (byte)0xaa, (byte)0xbb,
            (byte)0xcc, (byte)0xdd, (byte)0xee, (byte)0xff };

    //Key byte array
    byte[]        keyBytes = new byte[] {
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
            0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
            0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17 };

    //ctLength
    int ctLength;

    //cipherText
    byte[] cipherText;


    /**
     * Constructor
     */
    public FileManager() {


        //add BouncyCastle Provider
        Security.addProvider(new BouncyCastleProvider());
        Provider provider = Security.getProvider("BC");

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
     * Using AES Encryption
     * @param text
     * @throws IOException
     */
    public void aesEncryptText (String text) throws IOException {


        try {


            //Text into byte array
            byte[] input = text.getBytes();
            System.out.println("input length: "+testinput.length);

            //System.out.println(Arrays.toString(input));


            //Creating new SecretKey
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

            //Instantiate Cipher
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding", "BC");


            System.out.println("input text : " + Utils.toHex(testinput));


            // encryption pass

            //Initiate Cipher init()
            cipher.init(Cipher.ENCRYPT_MODE, key);

            //Create cipherText output array
            cipherText = new byte[testinput.length];

            //Block by block encryption from input into cipherText update()
            ctLength = cipher.update(testinput, 0, testinput.length, cipherText, 0);

            //Get ctLength doFinal()
            ctLength += cipher.doFinal(cipherText, ctLength);
            //cipher.doFinal(cipherText);

            System.out.println("cipher text: " + Utils.toHex(cipherText) + " bytes: " + ctLength);


            //cipherText to String
            String cipherTextString = new String(cipherText);

            System.out.println(cipherTextString);

            System.out.println("String to bytes: "+Utils.toHex(cipherTextString.getBytes()));


            //Writing encrypted text into file

            //Current Path has to be available
            if(currentPath != null) {
                //fileWriter = new FileWriter("./src/main/java/savefiles/"+tempName+".txt");

                //Create new Writer
                fileWriter = new FileWriter(currentPath);

                //Write Text into File
                fileWriter.write(cipherTextString);

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



    /**
     * Using AES Decryption
     * @param file
     * @return text
     * @throws IOException
     */
    public String aesDecryptText (File file) throws IOException {

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


            //Creating new SecretKey
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

            //Instantiate Cipher
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding", "BC");

            // decryption pass

            byte[] plainText = new byte[ctLength];

            cipher.init(Cipher.DECRYPT_MODE, key);

            int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);

            ptLength += cipher.doFinal(plainText, ptLength);

            System.out.println("plain text : " + Utils.toHex(plainText) + " bytes: " + ptLength);

            String plainTextString = new String(plainText);

            text = plainTextString;

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
