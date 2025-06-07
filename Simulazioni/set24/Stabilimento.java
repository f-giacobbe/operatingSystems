package Simulazioni.set24;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public abstract class Stabilimento {
    protected static final int LETTINO = 0;
    protected static final int OMBRELLONE = 1;
    protected static final int[] TEMPO = {1, 2};    //tempo di preparazione lettino e ombrellone
    protected static final int[] PREZZO = {10, 15};     //prezzo di lettino e ombrellone

    private static Random random = new Random();

    protected int N;    //il numero di bagnanti

    public Stabilimento(int N) {
        this.N = N;
    }

    public abstract void scegliAccesso() throws InterruptedException;

    public abstract void preparaPostazioni() throws InterruptedException;

    public abstract void paga() throws InterruptedException;

    public abstract void chiusura() throws InterruptedException;

    protected static int scegliPosto() {
        return random.nextInt(2);   //0 per il lettino o 1 per l'ombrellone
    }

    protected static void prepara(int[] p) throws InterruptedException {
        /* riceve un array di due elementi p dove l'elemento 0 indica il numero di lettini mentre l'elemento 1
        indica il numero di ombrelloni che il gestore deve preparare
         */
        int numLettini = p[LETTINO];
        int numOmbrelloni = p[OMBRELLONE];

        int minutiLettini = numLettini * TEMPO[LETTINO];
        int minutiOmbrelloni = numOmbrelloni * TEMPO[OMBRELLONE];

        int minutiTotali = minutiLettini + minutiOmbrelloni;

        TimeUnit.MINUTES.sleep(minutiTotali);
    }






    protected void test() {
        for (int i = 0; i < N; i++) {
            new Bagnante(this).start();
        }

        new Gestore(this).start();
    }
}
