import java.util.Random;

public class TestSC {
    public static final int KEY_SIZE_BYTES = PRF.KEY_SIZE_BYTES;
    public static final int NONCE_SIZE_BYTES = StreamCipher.NONCE_SIZE_BYTES;

    public static void main(String[] args) {

        byte[] k = new byte[KEY_SIZE_BYTES];
        k[2] = 1;
        k[5] = 1;
        k[31] = 1;

        byte[] n = new byte[NONCE_SIZE_BYTES];
        n[3] = 1;
        n[5] = 1;

        boolean failed = false;

        /* SANITY TEST: ENCRYPTION AND DECRYPTION*/
        System.out.println("Encryption and Decryption Tests");
        int NUM_TESTS = 10;
        byte[] text = new byte[NUM_TESTS];
        Random r = new Random();
        r.nextBytes(text);                  // randomize test text bytes
        StreamCipher enc = new StreamCipher(k, n);
        StreamCipher dec = new StreamCipher(k, n);
        System.out.print("    ");
        byte[] result = new byte[NUM_TESTS];
        for (int i = 0; i < NUM_TESTS; i++) {
            byte e = enc.cryptByte(text[i]);
            byte d = dec.cryptByte(e);
            result[i] = e;
            if (text[i] != d) failed = true;
        }
        if (failed) System.out.println("    FAILED: Decryption failed.");
        else System.out.println("    SUCCESS: crypteByte() encryption");
        failed = false;
        System.out.println();

        /* SANITY TEST: DETERMINISTIC ENCRYPTION */
        System.out.println("StreamCipher Tests");
        StreamCipher enc2 = new StreamCipher(k, n);
        System.out.print("    ");
        for (int i = 0; i < NUM_TESTS; i++) {
            byte e = enc2.cryptByte(text[i]);
            if (e != result[i]) failed = true;
        }
        if (failed) System.out.println("  FAILED: StreamCiphers with same k, n encrypted differently.");
        else System.out.println("    SUCCESS: StreamCiphers with same k, n have the same encryption.");
        failed = false;

        n[1] = 1;   // change n
        n[2] = 1;
        n[3] = 0;
        StreamCipher enc3 = new StreamCipher(k, n);
        System.out.print("    ");
        for (int i = 0; i < NUM_TESTS; i++) {
            byte e = enc3.cryptByte(text[i]);
            if (e == result[i]) failed = true;
        }
        if (failed) System.out.println("    FAILED: StreamCiphers with different n have the same encryption.");
        else System.out.println("    SUCCESS: StreamCiphers with different n encrypted differently.");
        failed = false;

        n[1] = 0;   // reset n
        n[2] = 0;
        n[3] = 1;
        k[22] = 1;  // change k
        StreamCipher enc4 = new StreamCipher(k, n);
        System.out.print("    ");
        for (int i = 0; i < NUM_TESTS; i++) {
            byte e = enc4.cryptByte(text[i]);
            if (e == result[i]) failed = true;
        }
        if (failed) System.out.println("    FAILED: StreamCiphers with different k have the same encryption.");
        else System.out.println("    SUCCESS: StreamCiphers with different k encrypted differently.");
        failed = false;
    }
}
