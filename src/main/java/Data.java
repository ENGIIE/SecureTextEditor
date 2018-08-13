import javax.crypto.spec.IvParameterSpec;
import java.io.Serializable;
import java.util.Random;
import java.security.Key;

public class Data implements Serializable {

    //PBE bool
    private boolean pbe = false;

    //Salt
    private byte[] salt;

    //Iterationcount
    private int iterationCount;

    // Key Size
    private int keysize;

    // Algorithm
    private int algo;

    private int blocksize;

    // Mode
    private int mode;

    // IV
    private byte[] iv;

    // Padding
    int pad;

    //Hashfunction
    private int hash;

    //HMAC
    private boolean hmac = false;

    private String ciphertext;

    public Data() {
        keysize = 128;
        algo = 0;
        blocksize = 16;
        mode = 0;
        pad = 2;
        hash = 0;
        salt = new byte[]{
                0x7d, 0x60, 0x43, 0x5f, 0x02, (byte) 0xe9, (byte) 0xe0, (byte) 0xae};
        iterationCount = 0;
    }

    // Setter and Getter

    public void setKeysize (int keysize) { this.keysize = keysize; }
    public int getKeysize () { return this.keysize; }

    public void setIv (byte[] iv) { this.iv = iv; }
    public byte[] getIv () { return this.iv; }

    public void setAlgo (int algo) { this.algo = algo; }
    public int getAlgo () { return this.algo; }

    public void setMode (int mode) { this.mode = mode; }
    public int getMode () { return this.mode; }

    public void setPad (int pad) { this.pad = pad; }
    public int getPad () { return this.pad; }

    public void setHash(int hash) { this.hash = hash; }
    public int getHash () { return this.hash; }

    public void setHMAC(boolean hmac) { this.hmac = hmac; }
    public boolean getHMAC () { return this.hmac; }

    public void setCiphertext (String ciphertext) { this.ciphertext = ciphertext; }
    public String getCiphertext () { return this.ciphertext; }

    public void setPbe (boolean pbe) { this.pbe = pbe; }
    public boolean getPbe () { return this.pbe; }

    public void setSalt (byte[] salt) { this.salt = salt; }
    public byte[] getSalt () { return this.salt; }

    public void setIterationCount (int iterationCount) { this.iterationCount = iterationCount; }
    public int getIterationCount () { return this.iterationCount; }

    public void setBlocksize (int blocksize) { this.blocksize = blocksize; }
    public int getBlocksize () { return this.blocksize; }

}
