package Esercitazioni.Esercitazione9.barmod;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public abstract class Bar {
    static final int CASSA = 0;
    static final int BANCONE = 1;
    static final int[] POSTI = {1, 4};  //indica rispettivamente il numero di posti in cassa e al bancone
    private static final Random random = new Random();
    private static final int[] MIN_SECONDI = {5, 20};
    private static final int[] MAX_SECONDI = {10, 40};

    public abstract int scegli() throws InterruptedException;

    public abstract void inizia(int i) throws InterruptedException;

    public abstract void finisci(int i) throws InterruptedException;

    private static void paga() throws InterruptedException {
        TimeUnit.SECONDS.sleep(random.nextInt(MAX_SECONDI[CASSA] - MIN_SECONDI[CASSA] + 1) + MIN_SECONDI[CASSA]);
    }

    private static void beviCaffe() throws InterruptedException {
        TimeUnit.SECONDS.sleep(random.nextInt(MAX_SECONDI[BANCONE] - MIN_SECONDI[BANCONE] + 1) + MIN_SECONDI[BANCONE]);
    }

    public static void interagisci(int scelta) throws InterruptedException {
        //permette di pagare o di bere il caffé, a seconda del valore di scelta
        if (scelta == CASSA) {
            paga();
        } else {
            beviCaffe();
        }
    }

    public void test(int numClienti) {
        for (int i = 0; i < numClienti; i++) {
            new Cliente(this).start();
        }
    }
}
