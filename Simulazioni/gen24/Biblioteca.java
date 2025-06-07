package Simulazioni.gen24;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public abstract class Biblioteca {
    protected static final int[] TIPO = {Utente.TESSERATO, Utente.ESTERNO};
    protected static final int CIFRE_STRINGA = 4;
    protected static final int MIN_TEMPO_REGISTRAZIONE = 1;
    protected static final int MAX_TEMPO_REGISTRAZIONE = 5;
    protected static Random random = new Random();

    public abstract void richiediPrestito() throws InterruptedException;

    public abstract void registraPrestito() throws InterruptedException;

    public abstract void esci() throws InterruptedException;

    public abstract void prossimoUtente() throws InterruptedException;

    protected static String generaStringa() {
        return "" + random.nextInt((int) Math.pow(10, CIFRE_STRINGA));
    }

    protected static void registrazione() throws InterruptedException {
        int minutiAttesa = MIN_TEMPO_REGISTRAZIONE + random.nextInt(MAX_TEMPO_REGISTRAZIONE - MIN_TEMPO_REGISTRAZIONE + 1);
        TimeUnit.MINUTES.sleep(minutiAttesa);
    }







    protected void test(int numUtenti) {
        for (int i = 0; i < numUtenti; i++) {
            new Utente(this, i < numUtenti / 2 ? Utente.TESSERATO : Utente.ESTERNO).start();
        }
        new Bibliotecario(this).start();
    }
}
