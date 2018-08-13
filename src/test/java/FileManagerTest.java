
import org.junit.Assert;
import org.junit.Test;
import sun.misc.MessageUtils;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;


public class FileManagerTest {

    FileManager fileManager = new FileManager();
    StringEqualityTest set = new StringEqualityTest();

    //Testinput
    byte[] testinput = new byte[] {
            0x00, 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77,
            (byte)0x88, (byte)0x99, (byte)0xaa, (byte)0xbb,
            (byte)0xcc, (byte)0xdd, (byte)0xee, (byte)0xff };

    //Text
    String input = "hallo";
    String output;

    //AES Key byte array
    byte[] AESkeyBytes = new byte[] {
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
            0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
            0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17 };


    byte[]		    ivBytes = new byte[] {
            0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00 };

    @Test
    public void encryptionAES () {
        try {
            fileManager.getData().setAlgo(0);
            fileManager.getData().setMode(0);
            fileManager.getData().setPad(2);
            fileManager.getData().setPbe(false);
            fileManager.getData().setBlocksize(16);
            fileManager.getData().setKeysize(128);
            fileManager.getData().setHMAC(false);
            fileManager.getData().setHash(0);
            fileManager.setFileName("test3");
            fileManager.setKeyFileName("test3");
            fileManager.setPath("C:\\Users\\ENGIE\\Desktop\\test3.txt");
            fileManager.setKeyPath("C:\\Users\\ENGIE\\Desktop\\test3.xml");
            fileManager.encryptText(input);
            System.out.println("INPUT: "+input);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public  void decryptAES () {
        fileManager.setFileName("test3");
        fileManager.setKeyFileName("test3");
        fileManager.setPath("C:\\Users\\ENGIE\\Desktop\\test3.txt");
        fileManager.setKeyPath("C:\\Users\\ENGIE\\Desktop\\test3.xml");
        File file = new File("C:\\Users\\ENGIE\\Desktop\\test3.txt");
        try {
            output = fileManager.decryptText(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("OUTPUT: "+output);
        //Assert.assertSame(output, input);
        if(input == output){
            System.out.println("SUCCESS");
        }else {
            System.out.println("FAILED");
        }

    }


}
