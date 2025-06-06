package Simulazioni.nov24;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public abstract class BancoCheckIn {
    private static final int SECONDI_PER_BAGAGLIO_PASSEGGERO = 15;
    private static final int SECONDI_PER_BAGAGLIO_ADDETTO = 10;
    private static final Random random = new Random();
    private static final int MIN_BAGAGLI = 1;
    private static final int MAX_BAGAGLI = 3;

    public abstract void deponeBagagli(int N) throws InterruptedException;

    public abstract void pesaERegistra() throws InterruptedException;

    public abstract void riceviCartaImbarco() throws InterruptedException;

    public abstract void prossimoPasseggero() throws InterruptedException;

    protected static int scegliNBagagli() {
        return MIN_BAGAGLI + random.nextInt(MAX_BAGAGLI - MIN_BAGAGLI + 1);
    }

    protected static void preparati(int numBagagli) throws InterruptedException {
        TimeUnit.SECONDS.sleep(numBagagli * SECONDI_PER_BAGAGLIO_PASSEGGERO);
    }

    protected static void pesaERegistra(int numBagagli) throws InterruptedException {
        TimeUnit.SECONDS.sleep(numBagagli * SECONDI_PER_BAGAGLIO_ADDETTO);
    }






    protected void test(int numPasseggeri) {
        for (int i = 0; i < numPasseggeri; i++) {
            new Passeggero(this).start();
        }
        new Addetto(this).start();
    }
}
