package Esercitazione6.filosofi;

import java.util.concurrent.TimeUnit;

public class Filosofo extends Thread {
    private Tavolo tavolo;
    private int posizione;

    public Filosofo(Tavolo tavolo, int posizione) {
        this.tavolo = tavolo;
        this.posizione = posizione;
    }

    @Override
    public void run() {
        try {
            while (true) {
                tavolo.prendiBacchette(posizione);
                System.out.printf("Il filosofo %d ha iniziato a mangiare%n", posizione);
                mangia();
                System.out.printf("Il filosofo %d ha finito di mangiare%n", posizione);
                tavolo.rilasciaBacchette(posizione);
                pensa();
            }
        } catch (InterruptedException _) {

        }
    }

    private void mangia() throws InterruptedException {
        TimeUnit.SECONDS.sleep(10);
    }

    private void pensa() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
    }
}
