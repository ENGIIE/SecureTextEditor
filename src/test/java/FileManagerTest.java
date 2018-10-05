import org.junit.Test;
import org.omg.CORBA.Object;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import static org.junit.Assert.assertEquals;


public class FileManagerTest {

    FileManager fileManager = new FileManager();

    //Testinput
    byte[] testinput = new byte[] {
            0x00, 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77,
            (byte)0x88, (byte)0x99, (byte)0xaa, (byte)0xbb,
            (byte)0xcc, (byte)0xdd, (byte)0xee, (byte)0xff };

    //Text
    String input = "PaulHoffmann4708";
    String output;

    //AES Key byte array
    byte[] AESkeyBytes = new byte[] {
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
            0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
            0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17 };


    byte[]		    ivBytes = new byte[] {
            0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00 };

    int nr = 0;


    //URL urltxt = getClass().getResource(".\\main\\java\\Tests\\test3.xml");
    //URL urlxml = getClass().getResource(".\\main\\java\\Tests\\test3.xml");
    File txtfile = new File("./src/main/java/Tests/test3.txt");
    File xmlfile = new File("./src/main/java/Tests/test3.xml");

    //@Test
    public void encryptionDecryption () {
        for(int m = 0; m<=3; m++) { //Algorithm
            for (int n = 0; n <= 5; n++) { //Mode
                for (int o = 0; o <= 7; o++) { //Padding
                    for (int p = 0; p <= 1; p++) { //HMAC
                        for (int q = 0; q <= 6; q++) { //Hashfunkt.
                            System.out.println("INPUT: " + input);
                            try {
                                fileManager.getData().setAlgo(m);

                                if (m == 2 || m == 3) {
                                    fileManager.getData().setMode(-1);
                                    fileManager.getData().setPad(-1);
                                } else {
                                    fileManager.getData().setMode(n);
                                    fileManager.getData().setPad(o);
                                }

                                fileManager.getData().setPbe(false);

                                if (m == 1) {
                                    fileManager.getData().setBlocksize(8);
                                    fileManager.getData().setKeysize(64);
                                } else {
                                    fileManager.getData().setBlocksize(16);
                                    fileManager.getData().setKeysize(128);
                                }

                                if(p==0){
                                    fileManager.getData().setHMAC(false);
                                    fileManager.getData().setHash(q);
                                }else{
                                    fileManager.getData().setHMAC(false);
                                    fileManager.getData().setHash(q);
                                }

                                //File f = new File("C:/a/b/test.txt");

                                fileManager.setFileName(txtfile.getName());
                                fileManager.setKeyFileName(xmlfile.getName());
                                fileManager.setPath(txtfile.getPath());
                                fileManager.setKeyPath(xmlfile.getPath());
                                fileManager.encryptText(input);
                                System.out.println("INPUT: " + input);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            fileManager.setFileName(txtfile.getName());
                            fileManager.setKeyFileName(xmlfile.getName());
                            fileManager.setPath(txtfile.getPath());
                            fileManager.setKeyPath(xmlfile.getPath());
                            File file = new File(txtfile.getPath());
                            try {
                                output = fileManager.decryptText(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println("OUTPUT: " + output);

                            String s1 = input;
                            String s2 = output;

                            //Assert.assertSame(output, input);
                            System.out.println("STRINGS: " + s1 + "    " + s2);
                            if (s1.equals(s2)) {
                                System.out.println("SUCCESS");
                            } else {
                                System.out.println("FAILED");
                            }

                            System.out.println("-----------------------TEST: " + nr + " ---------------------------");
                            nr++;
                            //assertEquals(input, output);

                        }
                    }
                }
            }
        }
    }



    @Test
    public void pbeEncryptionDecryption () {
        for(int m = 4; m<=7; m++) { //Algorithm
            for (int p = 0; p <= 1; p++) { //HMAC
                for (int q = 0; q <= 6; q++) { //Hashfunkt

                    try {

                        fileManager.getData().setPbe(true);
                        fileManager.setPassword("ESS2018");

                        fileManager.getData().setAlgo(m);

                        if(m==5){
                            fileManager.getData().setBlocksize(16);
                            fileManager.getData().setKeysize(128);
                        }else{
                            fileManager.getData().setBlocksize(8);
                            fileManager.getData().setKeysize(128);
                        }

                        fileManager.getData().setMode(-1);
                        fileManager.getData().setPad(-1);

                        if(p==0){
                            fileManager.getData().setHMAC(false);
                            fileManager.getData().setHash(q);
                        }else{
                            fileManager.getData().setHMAC(false);
                            fileManager.getData().setHash(q);
                        }

                        //File f = new File("C:/a/b/test.txt");

                        fileManager.setFileName(txtfile.getName());
                        fileManager.setKeyFileName(xmlfile.getName());
                        fileManager.setPath(txtfile.getPath());
                        fileManager.setKeyPath(xmlfile.getPath());
                        fileManager.encryptText(input);
                        System.out.println("INPUT: " + input);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    fileManager.setFileName(txtfile.getName());
                    fileManager.setKeyFileName(xmlfile.getName());
                    fileManager.setPath(txtfile.getPath());
                    fileManager.setKeyPath(xmlfile.getPath());
                    File file = new File(txtfile.getPath());

                    fileManager.setTest(true);
                    fileManager.getData().setPbe(true);
                    fileManager.setPassword("ESS2018");

                    try {
                        output = fileManager.decryptText(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("OUTPUT: " + output);

                    String s1 = input;
                    String s2 = output;

                    //Assert.assertSame(output, input);
                    System.out.println("STRINGS: " + s1 + "    " + s2);
                    if (s1.equals(s2)) {
                        System.out.println("SUCCESS");
                    } else {
                        System.out.println("FAILED");
                    }

                    System.out.println("-----------------------TEST: " + nr + " ---------------------------");
                    nr++;
                    //assertEquals(input, output);

                        fileManager.setTest(false);

                }
            }
        }
    }



    public void encryptionAES () {
        System.out.println("INPUT: "+input);
        try {
            fileManager.getData().setAlgo(0);
            fileManager.getData().setMode(0);
            fileManager.getData().setPad(2);
            fileManager.getData().setPbe(false);
            fileManager.getData().setBlocksize(16);
            fileManager.getData().setKeysize(128);
            fileManager.getData().setHMAC(false);
            fileManager.getData().setHash(2);
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

        String s1 = input;
        String s2 = output;

        //Assert.assertSame(output, input);
        System.out.println("STRINGS: "+s1+"    "+s2);
        if(s1.equals(s2)){
            System.out.println("SUCCESS");
        }else {
            System.out.println("FAILED");
        }

        //System.out.println("-----------------------TEST: "+ nr +" ---------------------------");
        //assertEquals(input, output);
    }



}
