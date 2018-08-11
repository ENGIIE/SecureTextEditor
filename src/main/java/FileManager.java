import chapter2.Utils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.security.Key;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

public class FileManager {

    int count;

    //Cryptography Provider
    Provider provider;

    //Settingsmanager
    SettingsManager settingsManager = new SettingsManager();

    //Data Object
    Data data = new Data();

    //ScureRandom Object
    SecureRandom secRndm = new SecureRandom();

    //FileWriter and FileReader
    FileWriter fileWriter;
    FileReader fileReader;

    //String with the current Path
    String currentPath;

    //String with the current Name
    String tempName;

    String currentKeyPath;
    String tempKeyName;

    /**
     * Constructor
     */
    public FileManager() {
        //add BouncyCastle Provider
        Security.addProvider(new BouncyCastleProvider());
        provider = Security.getProvider("BC");
        //Initialize Writer and Reader
        fileWriter = null;
        fileReader = null;
    }


    /**
     * Save Text into File
     * @param text
     * @throws IOException
     */
    public void saveText (String text) throws IOException {
        try {
            //currentPath is available
            if(currentPath != null) {
                //Create new FileWriter
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
        System.out.println("Load: "+ text);
        return text;
    }


    /**
     * Using Encryption
     * @param text
     * @throws IOException
     */
    public void encryptText (String text) throws IOException {

            try {

                System.out.println("Encrypt text: " + text);

                //Load the Settingsdata.xml File
                File settingsfile = new File("./src/main/java/Settingsdata.xml");
                System.out.println("Settingsdata path: " + settingsfile);
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(settingsfile);

                //Often used Components
                NodeList nodeList;
                org.w3c.dom.Node node;
                Element element;

                //Get Algorithm, Keysize and Blocksize
                String algorithm;
                int keysize = data.getKeysize();
                int blocksize = data.getBlocksize();

                System.out.println("Algorithm id: " + data.getAlgo());
                nodeList = document.getElementsByTagName("algorithm");
                if(data.getAlgo() != -1) {
                    node = nodeList.item(data.getAlgo());
                    element = (Element) node;
                    System.out.println(element.getElementsByTagName("name").item(0).getTextContent());
                    element.getElementsByTagName("name").item(0).getTextContent();
                    algorithm = element.getElementsByTagName("name").item(0).getTextContent();
                }else {
                    algorithm = "notAvailable";
                }

                System.out.println("Algorithm: " + algorithm);
                System.out.println("Keysize: " + keysize);
                System.out.println("Blocksize: " + blocksize);

                //Get Mode and IV
                String mode;
                boolean iv = false;

                nodeList = document.getElementsByTagName("mode");
                if(data.getMode() != -1) {
                    node = nodeList.item(data.getMode());
                    element = (Element) node;
                    element.getElementsByTagName("name").item(0).getTextContent();
                    mode = element.getElementsByTagName("name").item(0).getTextContent();
                    element.getElementsByTagName("iv").item(0).getTextContent();
                    iv = Boolean.parseBoolean(element.getElementsByTagName("iv").item(0).getTextContent());
                }else{
                    mode = "notAvailable";
                }

                System.out.println("Mode: " + mode);
                System.out.println("IV: " + iv);

                String padding;
                if(data.getMode() != -1) {
                    nodeList = document.getElementsByTagName("padding");
                    node = nodeList.item(data.getPad());
                    element = (Element) node;
                    element.getElementsByTagName("name").item(0).getTextContent();
                    padding = element.getElementsByTagName("name").item(0).getTextContent();
                }else{
                    padding = "notAvailable";
                }

                System.out.println("Padding: " + padding);



                //Text into byte array
                byte[] input = text.getBytes();
                System.out.println("Encrypt input text : " + Utils.toHex(input));
                System.out.println("Plain TextString LENGTH: " + input.length);
                //Creating new SecretKey
               // SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);
                //Instantiate Cipher
                Cipher cipher;



                // encryption pass

                if(data.getPbe()==true) {

                    cipher = Cipher.getInstance(algorithm, "BC");

                    char[] password = data.getPassword().toCharArray();
                    byte[] salt = data.getSalt();
                    int iterationCount = data.getIterationCount();
                    System.out.println(iterationCount);

                    PBEKeySpec pbeSpec = new PBEKeySpec(password, salt, iterationCount);
                    SecretKeyFactory keyFact = SecretKeyFactory.getInstance(algorithm, "BC");

                    cipher = Cipher.getInstance(algorithm, "BC");
                    Key sKey = keyFact.generateSecret(pbeSpec);

                    byte[] ivBytes = new byte[8];
                    secRndm.nextBytes(ivBytes);
                    data.iv = ivBytes;
                    IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

                    System.out.println("PASSWORD: "+ Arrays.toString(pbeSpec.getPassword()));
                    cipher.init(Cipher.ENCRYPT_MODE, sKey, ivSpec);

                    byte[] cipherText = cipher.doFinal(input);
                    System.out.println("TEST1: "+cipherText.length);

                    System.out.println("Encrypt cipher text: " + Utils.toHex(cipherText) + " bytes: " + cipherText.length);
                    String cipherTextString = Base64.toBase64String(cipherText);
                    System.out.println("Encrypt CipherTextString LENGTH: " + cipherText.length);
                    System.out.println("Encrypt CipherTextString: " + cipherTextString);

                    data.setCiphertext(cipherTextString);

                    if (currentPath != null) {

                        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(currentPath));
                        out.writeObject(data);
                        out.close();

                    }


                } else {
                    //Modes vorhanden?

                    if(data.mode == -1) {
                        cipher = Cipher.getInstance(algorithm, "BC");
                    } else {
                        cipher = Cipher.getInstance(algorithm + "/" + mode + "/" + padding, "BC");
                    }

                    //Key
                    byte[] keyBytes = null;

                    KeyGenerator generator = KeyGenerator.getInstance(algorithm, "BC");

                    generator.init(data.getKeysize());

                    Key encryptionKey = generator.generateKey();

                    data.encryptionKey = encryptionKey;

                    count = settingsManager.getIterationcount();
                    System.out.println("HIER0 KEY: "+ Utils.toHex(Base64.encode(encryptionKey.getEncoded())));
                    settingsManager.setKey(Base64.encode(encryptionKey.getEncoded()));

                    if (iv == true) {
                        byte[] ivBytes = new byte[blocksize];
                        secRndm.nextBytes(ivBytes);
                        data.iv = ivBytes;
                        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
                        cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, ivSpec);
                    } else {
                        cipher.init(Cipher.ENCRYPT_MODE, encryptionKey);
                    }

                    //Create cipherText output array
                    byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
                    //Block by block encryption from input into cipherText update()
                    int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
                    //Get ctLength, doFinal()
                    ctLength += cipher.doFinal(cipherText, ctLength);
                    //cipherText to String
                    //Base64 encoding
                    System.out.println("Encrypt cipher text: " + Utils.toHex(cipherText) + " bytes: " + ctLength);
                    String cipherTextString = Base64.toBase64String(cipherText);
                    System.out.println("Encrypt CipherTextString LENGTH: " + cipherText.length);
                    System.out.println("Encrypt CipherTextString: " + cipherTextString);

                    data.setCiphertext(cipherTextString);

                    //Writing encrypted text into file
                    //Current Path has to be available
                    if (currentPath != null) {

                        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(currentPath));
                        out.writeObject(data);
                        out.close();

                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

    }


    /**
     * Using Decryption
     * @param file
     * @return text
     * @throws IOException
     */
    public String decryptText (File file) throws IOException {
        //Current Text
        String text = "";
        try {

            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            data = (Data) in.readObject();
            in.close();
            text = data.getCiphertext();


            File settingsfile = new File("./src/main/java/Settingsdata.xml");
            System.out.println(settingsfile);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(settingsfile);

            NodeList nList = document.getElementsByTagName("algorithm");
            org.w3c.dom.Node node = nList.item(data.getAlgo());
            Element eElement = (Element) node;
            eElement.getElementsByTagName("name").item(0).getTextContent();
            String algorithm = eElement.getElementsByTagName("name").item(0).getTextContent();
            if(eElement.getElementsByTagName("keysize").getLength() != 0) {
                eElement.getElementsByTagName("keysize").item(0).getTextContent();
                if (eElement.getElementsByTagName("keysize").item(0).getTextContent() != null) {
                    int keysize = Integer.parseInt(eElement.getElementsByTagName("keysize").item(0).getTextContent());
                    System.out.println("Keysize: " + keysize);
                } else {
                    int keysize = 0;
                    System.out.println("Keysize: " + keysize);
                }
            }

            System.out.println("Algorithm: " + algorithm);
            //System.out.println("Keysize: " + keysize);

            String mode;
            boolean ivNeeded = false;
            nList = document.getElementsByTagName("mode");
            if(data.getMode() != -1) {
                node = nList.item(data.getMode());
                eElement = (Element) node;
                eElement.getElementsByTagName("name").item(0).getTextContent();
                mode = eElement.getElementsByTagName("name").item(0).getTextContent();
                eElement.getElementsByTagName("iv").item(0).getTextContent();
                ivNeeded = Boolean.parseBoolean(eElement.getElementsByTagName("iv").item(0).getTextContent());
            }else{
                mode = "notAvailable";
            }

            System.out.println("Mode: " + mode);
            System.out.println("IV: " + ivNeeded);

            String padding;
            if(data.getMode() != -1) {
                nList = document.getElementsByTagName("padding");
                node = nList.item(data.getPad());
                eElement = (Element) node;
                eElement.getElementsByTagName("name").item(0).getTextContent();
                padding = eElement.getElementsByTagName("name").item(0).getTextContent();
            }else{
                padding = "notAvailable";
            }

            System.out.println("Padding: " + padding);

            //decryption pass
            Cipher cipher;
            if(data.getPbe() == true) {

                cipher = Cipher.getInstance(algorithm, "BC");

                char[] password = data.getPassword().toCharArray();
                byte[] salt = data.getSalt();
                int iterationCount = data.getIterationCount();

                PBEKeySpec pbeSpec = new PBEKeySpec(password, salt, iterationCount);
                SecretKeyFactory keyFact = SecretKeyFactory.getInstance(algorithm, "BC");
                cipher = Cipher.getInstance(algorithm, "BC");
                Key sKey = keyFact.generateSecret(pbeSpec);

                IvParameterSpec ivSpec = new IvParameterSpec(data.iv);

                System.out.println("PASSWORD: "+ Arrays.toString(pbeSpec.getPassword()));
                cipher.init(Cipher.DECRYPT_MODE, sKey, ivSpec);

                // decryption pass
                System.out.println("Decrypt Text: " + text);
                //Decode text into cipherTextRead input array
                byte[] cipherText = Base64.decode(text.getBytes());

                System.out.println("TEST2: "+cipherText.length);
                byte[] plainText = cipher.doFinal(cipherText);

                //plainText array to String
                String plainTextString = new String(plainText);
                System.out.println("Decrypt plainTextString : " + text);
                text = plainTextString;
                System.out.println("Decrypt TextString : " + text);


            }else {
                byte [] keyBytes = null;

                byte[] kkey = settingsManager.getKey(count);
                System.out.println("Hier 2.Key: "+Utils.toHex(kkey));
                settingsManager.setIterationcount(count++);

                Key decryptionKey = new SecretKeySpec(data.encryptionKey.getEncoded(), data.encryptionKey.getAlgorithm());

                if(data.mode == -1) {
                    cipher = Cipher.getInstance(algorithm, "BC");
                }else {
                    cipher = Cipher.getInstance(algorithm + "/" + mode + "/" + padding, "BC");
                }

                //Initialize Cipher
                if(ivNeeded == true) {
                    IvParameterSpec ivSpec = new IvParameterSpec(data.iv);
                    cipher.init(Cipher.DECRYPT_MODE, decryptionKey, ivSpec);
                }else {
                    cipher.init(Cipher.DECRYPT_MODE, decryptionKey);
                }

                // decryption pass
                System.out.println("Decrypt Text: " + text);
                //Decode text into cipherTextRead input array
                byte[] cipherText = Base64.decode(text.getBytes());
                //Important for the right length
                int ctLength = cipherText.length;
                System.out.println("Decrypt cipher text: " + Utils.toHex(cipherText) + " bytes: " + ctLength);
                //Create plainText output array
                byte[] plainText = new byte[ctLength];



                //Decrypt block by block into plainText update()
                int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0); // plainText
                //Cipher doFinal()
                ptLength += cipher.doFinal(plainText, ptLength);
                System.out.println("Decrypt plain text : " + Utils.toHex(plainText) + " bytes: " + ptLength);
                //plainText array to String
                String plainTextString = new String(plainText);
                System.out.println("Decrypt plainTextString : " + text);
                text = plainTextString;
                System.out.println("Decrypt TextString : " + text);

            }

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return text;
    }


    public String getKeyFileText (){
        File file = new File("./src/main/java/KeyFile.xml");

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
        System.out.println("Load: "+ text);
        return text;

    }


    public void saveKeyText (String text) throws IOException {
        try {
            //currentPath is available
            if(currentKeyPath != null) {
                //Create new FileWriter
                fileWriter = new FileWriter(currentKeyPath);
                //Write Text into File
                fileWriter.write(text);
                //Close Writer
                fileWriter.close();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }



    //Set and get currentPath
    public void setPath (String path) {
        currentPath = path;
    }

    public String getPath () {
        return currentPath;
    }

    //Set and get currentName
    public void setFileName (String name) {
        tempName = name;
    }

    public String getFileName () {
        return tempName;
    }

    public void setKeyPath (String keypath) {
        currentKeyPath = keypath;
    }

    public String getKeyPath () {
        return currentKeyPath;
    }

    //Set and get currentName
    public void setKeyFileName (String keyName) {
        tempKeyName = keyName;
    }

    public String getKeyFileName () {
        return tempKeyName;
    }


    //Data
    public Data getData () {
        return data;
    }

}
