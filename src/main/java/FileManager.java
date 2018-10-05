import chapter3.Utils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.security.*;
import java.util.Arrays;

public class FileManager {

    //Cryptography Provider
    private Provider provider;
    //Settingsmanager
    private SettingsManager settingsManager = new SettingsManager();
    //Data Object
    private Data data = new Data();
    //ScureRandom Object
    private SecureRandom secRndm = new SecureRandom();
    //FileWriter and FileReader
    private FileWriter fileWriter;
    private FileReader fileReader;


    //Temporary data

    //String with the current File Path
    private String currentPath;
    //String with the current File Name
    private String tempName;
    //String with the current KeyFile Path
    private String currentKeyPath;
    //String with the current KeyFile Name
    private String tempKeyName;
    //Alertbox Password
    private char[] password;
    //Iterationcount
    private int count;

    //TEST CASE
    boolean test = false;


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

            //Get Algorithm, Keysize and Blocksize
            String algorithm;
            int keysize = data.getKeysize();
            int blocksize = data.getBlocksize();
            System.out.println("Algorithm id: " + data.getAlgo());
            if(data.getAlgo() != -1) {
                algorithm = settingsManager.getElementName("algorithm", data.getAlgo());
            }else {
                algorithm = "notAvailable";
            }
            System.out.println("Algorithm: " + algorithm);
            System.out.println("Keysize: " + keysize);
            System.out.println("Blocksize: " + blocksize);

            //Get Mode and IV
            String mode;
            boolean iv = false;
            if(data.getMode() != -1) {
                mode = settingsManager.getElementName("mode", data.getMode());
                iv = Boolean.parseBoolean(settingsManager.getSubelement("mode", data.getMode(), "iv"));
            }else{
                mode = "notAvailable";
            }
            System.out.println("Mode: " + mode);
            System.out.println("IV: " + iv);

            //Get Padding
            String padding;
            if(data.getMode() != -1) {
                padding = settingsManager.getElementName("padding", data.getPad());
            }else{
                padding = "notAvailable";
            }
            System.out.println("Padding: " + padding);

            //Get Hashfunction
            String hashfunct;
            if(data.getHash() != -1 && data.getHash() != 0) {
                hashfunct = settingsManager.getElementName("hashfunction", data.getHash());
            }else{
                hashfunct = "notAvailable";
            }
            System.out.println("Hashfunction: " + hashfunct);


            // encryption pass

            //Get Text into input byte array
            byte[] input = text.getBytes();
            System.out.println("Encrypt input text : " + Utils.toHex(input));
            System.out.println("Plain TextString LENGTH: " + input.length);

            //Cipher
            Cipher cipher;

            //cipher getInstance()
            if(data.getMode() == -1) {
                cipher = Cipher.getInstance(algorithm, "BC");
            } else {
                cipher = Cipher.getInstance(algorithm + "/" + mode + "/" + padding, "BC");
            }
            Mac hMac=null;
            if(data.getHMAC()){
                hMac = Mac.getInstance("Hmac"+hashfunct, "BC");
            }

            //IterationCount save
            count = settingsManager.getIterationcount(currentKeyPath);
            data.setIterationCount(count);
            System.out.println("1. ITERATION COUNT: "+count);

            //Generating key + save
            Key key;
            Key hMacKey = null;
            if(data.getPbe()) {
                System.out.println("USED PASSWORD: "+Arrays.toString(password));
                byte[] salt = data.getSalt();
                int iterationCount = data.getIterationCount();
                //blocksize=8;
                //Creating key
                //Key
                PBEKeySpec pbeSpec = new PBEKeySpec(password, salt, iterationCount+213);
                System.out.println("PASSWORD: "+ Arrays.toString(pbeSpec.getPassword()));
                SecretKeyFactory keyFact = SecretKeyFactory.getInstance(algorithm, "BC");
                key = keyFact.generateSecret(pbeSpec);
                if(data.getHMAC()){
                    System.out.println("PBE WITH HMAC !!!!!!!!!!!!");
                    hMacKey = new SecretKeySpec(key.getEncoded(), "Hmac"+algorithm);
                    settingsManager.setKey(Base64.encode(hMacKey.getEncoded()), currentKeyPath);
                }else {
                    System.out.println("PBE !!!!!!!!!!!!");
                    settingsManager.setPassword(password, currentKeyPath);
                }
                count++;
                System.out.println("2. ITERATION COUNT: " + count);
                settingsManager.setIterationcount(count, currentKeyPath);
                setPassword("");
            }else {
                //Creating key
                //Key
                byte[] keyBytes = null;
                KeyGenerator generator = KeyGenerator.getInstance(algorithm, "BC");
                System.out.println("KEYSIZE: "+data.getKeysize());
                generator.init(data.getKeysize());
                key = generator.generateKey();
                if(data.getHMAC()){
                    System.out.println("NO PBE WITH HMAC !!!!!!!!!!!!");
                    hMacKey = new SecretKeySpec(key.getEncoded(), "Hmac"+hashfunct);
                    settingsManager.setKey(Base64.encode(hMacKey.getEncoded()), currentKeyPath);
                } else {
                    System.out.println("NO PBE!!!!!!!!!!!!");
                    settingsManager.setKey(Base64.encode(key.getEncoded()), currentKeyPath);
                }
                System.out.println("KEY ENC: "+Utils.toHex(settingsManager.getKey(data.getIterationCount(), currentKeyPath)));
                System.out.println(Utils.toHex(settingsManager.getKey(data.getIterationCount(), currentKeyPath)).length());
                count++;
                System.out.println("2. ITERATION COUNT " + count);
                settingsManager.setIterationcount(count, currentKeyPath);
            }

            //Creating iv + cipher.init()
            if (iv | data.getPbe()) {
                byte[] ivBytes = new byte[blocksize];
                secRndm.nextBytes(ivBytes);
                data.setIv(ivBytes);
                System.out.println("IV: "+data.getIv());
                IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
                cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, key);
            }

            //cipherText + ctLength update()+doFinal()
            byte[] cipherText;
            if(data.getPbe()){
                cipherText = cipher.doFinal(input);
            }
            int ctLength;

            //Message Digest
            if(hashfunct != "notAvailable"){
                if(data.getHMAC()){
                    System.out.println("HASHFUNCT WITH HMAC !!!!!!!!!!!!");
                    cipherText = new byte[cipher.getOutputSize(text.length() + hMac.getMacLength())];

                    ctLength = cipher.update(Utils.toByteArray(text), 0, text.length(), cipherText, 0);

                    hMac.init(hMacKey);
                    hMac.update(Utils.toByteArray(text));

                    ctLength += cipher.doFinal(hMac.doFinal(), 0, hMac.getMacLength(), cipherText, ctLength);

                    // tampering step
                    //cipherText[9] ^= '0' ^ '9';
                    byte[] digest = hMac.doFinal();
                    settingsManager.setHash(DatatypeConverter.printHexBinary(digest).toUpperCase(), currentKeyPath);
                }else {
                    System.out.println("HASHFUNCT !!!!!!!!!!!!");
                    MessageDigest hash = MessageDigest.getInstance(hashfunct, "BC");
                    cipherText = new byte[cipher.getOutputSize(input.length + hash.getDigestLength())];
                    //Block by block encryption from input into cipherText update()
                    ctLength = cipher.update(input, 0, input.length, cipherText, 0);
                    hash.update(input);
                    //Get ctLength, doFinal()
                    ctLength += cipher.doFinal(hash.digest(), 0, hash.getDigestLength(), cipherText, ctLength);
                    //tampering
                    //cipherText[9] ^= '0' ^ '9';
                    byte[] digest = hash.digest();
                    settingsManager.setHash(DatatypeConverter.printHexBinary(digest).toUpperCase(), currentKeyPath);
                }

            }else{
                System.out.println("NO HASHFUNCT !!!!!!!!!!!!");
                cipherText = new byte[cipher.getOutputSize(input.length)];
                //Block by block encryption from input into cipherText update()
                ctLength = cipher.update(input, 0, input.length, cipherText, 0);
                //Get ctLength, doFinal()
                ctLength += cipher.doFinal(cipherText, ctLength);
                settingsManager.setHash("0", currentKeyPath);
            }

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
        System.out.println("DECRYPTION");
        try {
            //Ciphertxt
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            data = (Data) in.readObject();
            in.close();
            text = data.getCiphertext();

            System.out.println("PBE: "+data.getPbe());
            //Alertbox
            if(data.getPbe()){

                if(!test) {
                    System.out.println("PBE");
                    AlertBox.display("Password", "Please Close");
                    password = AlertBox.password;
                }

                System.out.println("USER PASSWORD: "+Arrays.toString(password));
                char[] checkPassword = settingsManager.getPassword(data.getIterationCount(), currentKeyPath);
                System.out.println("Hier 2.Key: "+Arrays.toString(checkPassword));

                if(Arrays.toString(password).equals(Arrays.toString(checkPassword))) {
                    System.out.println("CORRECT PASSWORD");
                }else{
                    System.out.println("WRONG PASSWORD");
                }
            }

            //Get Algorithm, Keysize and Blocksize
            String algorithm;
            int keysize = data.getKeysize();
            int blocksize = data.getBlocksize();
            System.out.println("Algorithm id: " + data.getAlgo());
            if(data.getAlgo() != -1) {
                algorithm = settingsManager.getElementName("algorithm", data.getAlgo());
            }else {
                algorithm = "notAvailable";
            }
            System.out.println("Algorithm: " + algorithm);
            System.out.println("Keysize: " + keysize);
            System.out.println("Blocksize: " + blocksize);

            //Get Mode and IV
            String mode;
            boolean iv = false;
            if(data.getMode() != -1) {
                mode = settingsManager.getElementName("mode", data.getMode());
                iv = Boolean.parseBoolean(settingsManager.getSubelement("mode", data.getMode(), "iv"));
            }else{
                mode = "notAvailable";
            }
            System.out.println("Mode: " + mode);
            System.out.println("IV: " + iv);

            //Get Padding
            String padding;
            if(data.getMode() != -1) {
                padding = settingsManager.getElementName("padding", data.getPad());
            }else{
                padding = "notAvailable";
            }
            System.out.println("Padding: " + padding);

            //Get Hashfunction
            String hashfunct;
            if(data.getHash() != -1 && data.getHash() != 0) {
                hashfunct = settingsManager.getElementName("hashfunction", data.getHash());
            }else{
                hashfunct = "notAvailable";
            }
            System.out.println("Hashfunction: " + hashfunct);


            // decryption pass

            //Cipher
            Cipher cipher;

            //cipher getInstance()
            if(data.getMode() == -1) {
                cipher = Cipher.getInstance(algorithm, "BC");
            } else {
                cipher = Cipher.getInstance(algorithm + "/" + mode + "/" + padding, "BC");
            }

            //Get IterationCount
            System.out.println("3. ITERATION COUNT: "+data.getIterationCount());

            //Get key
            Key key;
            Key hMacKey=null;
            if(data.getPbe()) {
                System.out.println("USED PASSWORD: "+Arrays.toString(password));
                byte[] salt = data.getSalt();
                int iterationCount = data.getIterationCount();
                blocksize=8;
                //Key
                PBEKeySpec pbeSpec = new PBEKeySpec(password, salt, iterationCount+213);
                System.out.println("PASSWORD: "+ Arrays.toString(pbeSpec.getPassword()));
                SecretKeyFactory keyFact = SecretKeyFactory.getInstance(algorithm, "BC");
                key = keyFact.generateSecret(pbeSpec);
                if(data.getHMAC()){
                    byte[] keyBytes = settingsManager.getKey(data.getIterationCount(), currentKeyPath);
                    hMacKey = new SecretKeySpec(keyBytes, hashfunct);
                }
                setPassword("");
            }else {
                //Key
                byte[] keyBytes = null;
                keyBytes = settingsManager.getKey(data.getIterationCount(), currentKeyPath);
                System.out.println("KEY DEC: "+Utils.toHex(keyBytes));
                System.out.println(Utils.toHex(keyBytes).length());
                key = new SecretKeySpec(keyBytes, algorithm);
                if(data.getHMAC()){
                    byte[] keyBytes2 = settingsManager.getKey(data.getIterationCount(), currentKeyPath);
                    hMacKey = new SecretKeySpec(keyBytes2, hashfunct);
                }
                //key = new SecretKeySpec(keyBytes, algorithm);
            }


            //iv + cipher.init()
            if (iv | data.getPbe()) {
                IvParameterSpec ivSpec = new IvParameterSpec(data.getIv());
                cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, key);
            }

            System.out.println("Decrypt Text: " + text);
            //Decode text into cipherTextRead input array
            byte[] cipherText = Base64.decode(text.getBytes());

            //cipherText + ctLength update()+doFinal()
            int ctLength = cipherText.length;
            String plainTextString = null;

            System.out.println("TEST2: " + cipherText.length);
            byte[] plainText;
            if(data.getPbe()){
                plainText = cipher.doFinal(cipherText);
            }else{
                plainText = new byte[ctLength];
            }

            //Message Digest
            int ptLength;
            if(hashfunct != "notAvailable"){
                if(data.getHMAC()){
                    Mac hMac = Mac.getInstance("Hmac"+hashfunct, "BC");
                    int messageLength;
                    if (!data.getPbe()) {
                        //Decrypt block by block into plainText update()
                        ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0); // plainText
                        //Cipher doFinal()
                        ptLength += cipher.doFinal(plainText, ptLength);
                        System.out.println("Decrypt plain text : " + Utils.toHex(plainText) + " bytes: " + ptLength);
                        messageLength = ptLength - hMac.getMacLength();
                    } else {
                        messageLength = plainText.length - hMac.getMacLength();
                    }
                    hMac.init(hMacKey);
                    hMac.update(plainText, 0, messageLength);

                    byte[] messageHash = new byte[hMac.getMacLength()];
                    System.arraycopy(plainText, messageLength, messageHash, 0, messageHash.length);

                    System.out.println("plain: " + Utils.toString(plainText, messageLength) + " verified: " + MessageDigest.isEqual(hMac.doFinal(), messageHash));
                    //plainText array to String
                    plainTextString = new String(plainText);
                    System.out.println("Decrypt plainTextString : " + text);
                    text = Utils.toString(plainText, messageLength);
                }else {
                    MessageDigest hash;
                    int messageLength;
                    if (!data.getPbe()) {
                        //Decrypt block by block into plainText update()
                        ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0); // plainText
                        //Cipher doFinal()
                        ptLength += cipher.doFinal(plainText, ptLength);
                        System.out.println("Decrypt plain text : " + Utils.toHex(plainText) + " bytes: " + ptLength);
                        hash = MessageDigest.getInstance(hashfunct, provider);
                        messageLength = ptLength - hash.getDigestLength();
                    } else {
                        hash = MessageDigest.getInstance(hashfunct);
                        messageLength = plainText.length - hash.getDigestLength();
                    }
                    hash.update(plainText, 0, messageLength);
                    byte[] messageHash = new byte[hash.getDigestLength()];
                    System.arraycopy(plainText, messageLength, messageHash, 0, messageHash.length);
                    System.out.println("plain: " + Utils.toString(plainText, messageLength) + " verified: " + MessageDigest.isEqual(hash.digest(), messageHash));
                    //plainText array to String
                    plainTextString = new String(plainText);
                    System.out.println("Decrypt plainTextString : " + text);
                    text = Utils.toString(plainText, messageLength);
                }
            }else {

                if(!data.getPbe()) {
                    //Decrypt block by block into plainText update()
                    ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0); // plainText
                    //Cipher doFinal()
                    ptLength = cipher.doFinal(plainText, ptLength);
                    System.out.println("Decrypt plain text : " + Utils.toHex(plainText) + " bytes: " + ptLength);
                }

                //plainText array to String
                plainTextString = new String (plainText);
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


    /**
     *
     * @return
     */
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


    /**
     *
     * @param text
     * @throws IOException
     */
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
    /**
     *
     * @param path
     */
    public void setPath (String path) {
        currentPath = path;
    }

    /**
     *
     * @return
     */
    public String getPath () {
        return currentPath;
    }

    //Set and get currentName
    /**
     *
     * @param name
     */
    public void setFileName (String name) {
        tempName = name;
    }

    /**
     *
     * @return
     */
    public String getFileName () {
        return tempName;
    }

    //Set and get currentKeyPath
    /**
     *
     * @param keypath
     */
    public void setKeyPath (String keypath) {
        currentKeyPath = keypath;
    }

    /**
     *
     * @return
     */
    public String getKeyPath () {
        return currentKeyPath;
    }

    //Set and get currentKeyName
    /**
     *
     * @param keyName
     */
    public void setKeyFileName (String keyName) {
        tempKeyName = keyName;
    }

    /**
     *
     * @return
     */
    public String getKeyFileName () {
        return tempKeyName;
    }

    //Set Password
    /**
     *
     * @param pw
     */
    public void setPassword (String pw) {
        System.out.println(pw);
        password = pw.toCharArray();
    }
    //Get Data

    /**
     *
     * @return
     */
    public Data getData () {
        return data;
    }

    public void setTest (boolean tst) {
        test = tst;
    }

}
