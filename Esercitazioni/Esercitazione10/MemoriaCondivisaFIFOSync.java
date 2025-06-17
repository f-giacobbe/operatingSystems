package Esercitazioni.Esercitazione10;

import Esercitazioni.Esercitazione6.lettoriscrittori.MemoriaCondivisa;

import java.util.LinkedList;

public class MemoriaCondivisaFIFOSync extends MemoriaCondivisa {
    private int numLettoriAttivi;
    private boolean scrittoreAttivo = false;
    private LinkedList<Thread> codaThread = new LinkedList<>();

    @Override
    public synchronized void inizioLettura() throws InterruptedException {
        codaThread.add(Thread.currentThread());

        while (!possoLeggere()) {
            wait();
        }

        codaThread.remove();
        numLettoriAttivi++;
    }

    private boolean possoLeggere() {
        return !scrittoreAttivo && Thread.currentThread().equals(codaThread.getFirst());
    }

    @Override
    public synchronized void fineLettura() {
        numLettoriAttivi--;

        if (!codaThread.isEmpty()) {
            notifyAll();
        }
    }

    @Override
    public synchronized void inizioScrittura() throws InterruptedException {
        codaThread.add(Thread.currentThread());

        while (!possoScrivere()) {
            wait();
        }

        codaThread.remove();
        scrittoreAttivo = true;
    }

    private boolean possoScrivere() {
        return numLettoriAttivi == 0 && !scrittoreAttivo && Thread.currentThread().equals(codaThread.getFirst());
    }

    @Override
    public synchronized void fineScrittura() {
        scrittoreAttivo = false;

        if (!codaThread.isEmpty()) {
            notifyAll();
        }
    }
}
