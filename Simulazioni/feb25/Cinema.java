package Simulazioni.feb25;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public abstract class Cinema {
    protected static final int MIN_POSTO = 1;
    protected static final int MAX_POSTO = 60;
    protected static final int DURATA_FILM_MINUTI = 120;
    private static final Random random = new Random();

    protected int numClienti;

    public Cinema(int numClienti) {
        this.numClienti = numClienti;
    }

    public abstract void acquistaBiglietto() throws InterruptedException;

    public abstract boolean consegnaBiglietto() throws InterruptedException;

    public abstract void vediFilm() throws InterruptedException;

    public abstract void chiudiCinema() throws InterruptedException;

    protected static int assegnaPosto(int minPosto, int maxPosto) {
        //restituisce un numero casuale tra minPosto e maxPosto per simulare l'assegnazione del posto
        return MIN_POSTO + random.nextInt(MAX_POSTO - MIN_POSTO + 1);
    }

    protected static void visioneFilm() throws InterruptedException {
        //metodo che simula la visione del film dalla durata di 120 minuti
        TimeUnit.MINUTES.sleep(DURATA_FILM_MINUTI);
    }

    protected void test() {
        for (int i = 0; i < numClienti; i++) {
            new Cliente(this).start();
        }
        new Addetto(this).start();
    }
}
