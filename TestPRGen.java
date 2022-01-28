public class TestPRGen {

    public static final int KEY_SIZE_BYTES = PRF.KEY_SIZE_BYTES;

    public static void main(String[] args) {

        byte[] k = new byte[KEY_SIZE_BYTES];
        k[2] = 1;
        k[5] = 1;
        k[31] = 1;

        /* Sanity Test: next() */
        System.out.println("Testing next(): deterministic, random-looking");
        PRGen prg = new PRGen(k);
        prg.next(9);
        prg.next(31);
        prg.next(2);
        prg.next(32);
        prg.next(0);
        for (int i = 7; i < 30; i++) k[i] = 1;
        PRGen prg2 = new PRGen(k);
        prg2.next(9);
        prg2.next(31);
        prg2.next(2);
        prg2.next(32);
        prg2.next(0);

    }
}
