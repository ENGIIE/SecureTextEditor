import java.util.ArrayList;
import java.util.List;

public class Settings {


    // Algorithm
    List<String> algolist = new ArrayList();

    // Mode
    List<String> modelist = new ArrayList();

    // Padding
    List<String> paddinglist = new ArrayList();


    public Settings () {
        //Algorithm
        algolist.add("AES");
        algolist.add("DES");
        algolist.add("ARC4");

        //Mode
        modelist.add("ECB");
        modelist.add("CBC");
        modelist.add("CTS");
        modelist.add("CTR");
        modelist.add("OFB");
        modelist.add("CFB");

        //Padding
        paddinglist.add("NoPadding");
        paddinglist.add("PKCS5Padding");
        paddinglist.add("PKCS7Padding");
        paddinglist.add("ISO10126-2");
        paddinglist.add("ISO7816-4");
        paddinglist.add("X9.23");
        paddinglist.add("TBC");
        paddinglist.add("ZeroByte");
    }

}
