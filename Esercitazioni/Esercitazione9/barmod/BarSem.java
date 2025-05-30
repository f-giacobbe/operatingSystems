package Esercitazioni.Esercitazione9.barmod;

import java.util.concurrent.Semaphore;

public class BarSem extends Bar {
    private Semaphore[] coda = {new Semaphore(POSTI[CASSA], true), new Semaphore(POSTI[BANCONE], true)};    //array di 2 semafori rispettivamente con 1 permesso per la cassa e 4 permessi per il bancone
    private Semaphore mutex = new Semaphore(1, true);     //semaforo per gestire la mutua esclusione
    private int[] lunghezzaFila = {0, 0};   //array che indica rispettivamente la lunghezza della fila alla cassa e al bancone
    private int[] numPostiLiberi = {POSTI[CASSA], POSTI[BANCONE]};    //array che indica rispettivamente il numero di posti liberi per accedere alla cassa e al bancone

    @Override
    public int scegli() throws InterruptedException {
        mutex.acquire();
        try {
            if (numPostiLiberi[CASSA] > 0) {
                return Bar.CASSA;
            }
            if (numPostiLiberi[BANCONE] > 0) {
                return BANCONE;
            }
            //sono tutti e due occupati -> mi devo mettere in fila
            if (lunghezzaFila[CASSA] <= lunghezzaFila[BANCONE]) {
                return CASSA;
            }
            return BANCONE;
        } finally {
            mutex.release();
        }
    }

    @Override
    public void inizia(int i) throws InterruptedException {
        mutex.acquire();
        lunghezzaFila[i]++;
        System.out.printf("Nuovo cliente in fila %s%n", i == CASSA ? "in cassa" : "al bancone");
        mutex.release();

        coda[i].acquire();
        mutex.acquire();
        lunghezzaFila[i]--;
        numPostiLiberi[i]--;
        System.out.printf("Un cliente può adesso %s%n", i == CASSA ? "pagare" : "sedersi al bancone e bere");
        mutex.release();
    }

    @Override
    public void finisci(int i) throws InterruptedException {
        mutex.acquire();
        numPostiLiberi[i]++;
        coda[i].release();
        System.out.printf("Un cliente ha appena finito di %s%n", i == CASSA ? "pagare" : "bere");
        mutex.release();
    }





    public static void main(String[] args) {
        BarSem bar = new BarSem();
        int numClienti = 100;
        bar.test(numClienti);
    }
}
