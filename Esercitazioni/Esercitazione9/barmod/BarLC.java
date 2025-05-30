package Esercitazioni.Esercitazione9.barmod;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BarLC extends Bar {
    private Lock l = new ReentrantLock();   //lock per la mutua esclusione all'interno del monitor
    private Condition[] occupato = {l.newCondition(), l.newCondition()};    //array di 2 condition rispettivamente per l'attesa della cassa e del bancone
    private LinkedList<Cliente>[] fila = new LinkedList[2];     //array di 2 LinkedList di Cliente per gestire rispettivamente la coda alla cassa e al bancone
    private int[] lunghezzaFila = {0, 0};
    private int[] numPostiLiberi = {POSTI[CASSA], POSTI[BANCONE]};

    public BarLC() {
        fila[CASSA] = new LinkedList<>();
        fila[BANCONE] = new LinkedList<>();
    }

    @Override
    public int scegli() throws InterruptedException {
        l.lock();
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
            l.unlock();
        }
    }

    @Override
    public void inizia(int i) throws InterruptedException {
        l.lock();
        try {
            fila[i].add((Cliente) Thread.currentThread());
            lunghezzaFila[i]++;
            System.out.printf("Nuovo cliente in fila %s%n", i == CASSA ? "in cassa" : "al bancone");

            while (!possoProcedere(i)) {
                occupato[i].await();
            }

            fila[i].remove();
            lunghezzaFila[i]--;
            numPostiLiberi[i]--;
            System.out.printf("Un cliente sta iniziando a %s%n", i == CASSA ? "pagare" : "bere");
        } finally {
            l.unlock();
        }
    }

    private boolean possoProcedere(int i) {
        return numPostiLiberi[i] > 0 && Thread.currentThread().equals(fila[i].getFirst());
    }

    @Override
    public void finisci(int i) throws InterruptedException {
        l.lock();
        try {
            numPostiLiberi[i]++;
            System.out.printf("Un cliente ha appena finito di %s%n", i == CASSA ? "pagare" : "bere");
            occupato[i].signalAll();
        } finally {
            l.unlock();
        }
    }


    public static void main(String[] args) {
        BarLC bar = new BarLC();
        int numClienti = 100;
        bar.test(numClienti);
    }
}
