import java.util.Arrays;
import java.util.Random;

public class TestSC {
    public static final int KEY_SIZE_BYTES = PRF.KEY_SIZE_BYTES;

    public static void main(String[] args) {

        byte[] k = new byte[KEY_SIZE_BYTES];
        k[2] = 1;
        k[5] = 1;
        k[31] = 1;
        byte[] n = new byte[]{
                1, 0, 1, 0, 1, 1, 1, 1
        };

        boolean failed = false;

        /* SANITY TEST: ENCRYPTION AND DECRYPTION */
        System.out.println("Encryption and Decryption Tests");
        int NUM_TESTS = 10000;
        byte[] text = new byte[NUM_TESTS];
        Random r = new Random();
        r.nextBytes(text);   // randomize test text bytes
        StreamCipher enc = new StreamCipher(k, n);
        StreamCipher dec = new StreamCipher(k, n);
        System.out.print("   ");
        byte[] result = new byte[NUM_TESTS];
        for (int i = 0; i < NUM_TESTS; i++) {
            byte e = enc.cryptByte(text[i]);
            byte d = dec.cryptByte(e);
            result[i] = e;
            if (text[i] != d) failed = true;
        }
        if (failed) System.out.println("   FAILED: Decryption failed.");
        else System.out.println("   SUCCESS: cryptByte() encryption");
        System.out.println();

        /* SANITY TEST: DETERMINISTIC ENCRYPTION */
        System.out.println("StreamCipher Tests");
        StreamCipher enc2 = new StreamCipher(k, n);
        System.out.print("   ");
        byte[] result2 = new byte[NUM_TESTS];
        for (int i = 0; i < NUM_TESTS; i++) {
            byte e = enc2.cryptByte(text[i]);
            result2[i] = e;
        }
        if (Arrays.equals(result, result2))
            System.out.println("   SUCCESS: StreamCiphers with same k, n have the same encryption.");
        else System.out.println("   FAILED: StreamCiphers with same k, n encrypted differently.");


        byte[] n2 = new byte[]{
                1, 1, 1, 1, 1, 1, 1, 1
        };
        StreamCipher enc3 = new StreamCipher(k, n2);
        System.out.print("   ");
        byte[] result3 = new byte[NUM_TESTS];
        for (int i = 0; i < NUM_TESTS; i++) {
            byte e = enc3.cryptByte(text[i]);
            result3[i] = e;
        }
        if (Arrays.equals(result, result3))
            System.out.println("   FAILED: StreamCiphers with different n have the same encryption.");
        else System.out.println("   SUCCESS: StreamCiphers with different n encrypted differently.");

        k[22] = 1;  // change k
        StreamCipher enc4 = new StreamCipher(k, n2);
        System.out.print("   ");
        byte[] result4 = new byte[NUM_TESTS];
        for (int i = 0; i < NUM_TESTS; i++) {
            byte e = enc4.cryptByte(text[i]);
            result4[i] = e;
        }
        if (Arrays.equals(result3, result4))
            System.out.println("   FAILED: StreamCiphers with different k have the same encryption.");
        else System.out.println("   SUCCESS: StreamCiphers with different k encrypted differently.");

        /*
        System.out.println(Arrays.toString(result));
        System.out.println(Arrays.toString(result3));
        System.out.println(Arrays.toString(result4));
         */

        /* PSEUDORANDOM TEST */
        System.out.println();
        System.out.println("Pseudorandomness Tests");
        int[] digitCounts1 = new int[129];
        int[] digitCounts3 = new int[129];
        int[] digitCounts4 = new int[129];
        double mean1 = 0.0;
        double mean3 = 0.0;
        double mean4 = 0.0;
        for (int i = 0; i < NUM_TESTS; i++) {
            if (result[i] <= 0) digitCounts1[Math.abs(result[i])] += 1;
            else digitCounts1[result[i] - 1] += 1;
            if (result3[i] <= 0) digitCounts3[Math.abs(result3[i])] += 1;
            else digitCounts3[result3[i] - 1] += 1;
            if (result4[i] <= 0) digitCounts4[Math.abs(result4[i])] += 1;
            else digitCounts4[result4[i] - 1] += 1;

            mean1 += ((double) result[i]) / 128.0;
            mean3 += ((double) result3[i]) / 128.0;
            mean4 += ((double) result4[i]) / 128.0;
        }

        System.out.println(Arrays.toString(digitCounts1));
        System.out.println(Arrays.toString(digitCounts3));
        System.out.println(Arrays.toString(digitCounts4));

        System.out.println("    mean:" + mean1);
        System.out.println("    mean:" + mean3);
        System.out.println("    mean:" + mean4);
    }
}
