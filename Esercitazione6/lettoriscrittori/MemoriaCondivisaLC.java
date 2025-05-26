package Esercitazione6.lettoriscrittori;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MemoriaCondivisaLC extends MemoriaCondivisa {
    private final Lock l = new ReentrantLock();
    private final Condition possoScrivere = l.newCondition();
    private final Condition possoLeggere = l.newCondition();
    private int numLettoriAttivi = 0;
    private boolean scrittoreAttivo = false;

    @Override
    public void inizioScrittura() throws InterruptedException {
        l.lock();
        try {
            while (numLettoriAttivi > 0 || scrittoreAttivo) {
                possoScrivere.await();
            }
            scrittoreAttivo = true;
        } finally {
            l.unlock();
        }
    }

    @Override
    public void fineScrittura() throws InterruptedException {
        l.lock();
        try {
            scrittoreAttivo = false;
            possoLeggere.signalAll();
            possoScrivere.signal();
        } finally {
            l.unlock();
        }
    }

    @Override
    public void inizioLettura() throws InterruptedException {
        l.lock();
        try {
            while (scrittoreAttivo) {
                possoLeggere.await();
            }
            numLettoriAttivi++;
        } finally {
            l.unlock();
        }
    }

    @Override
    public void fineLettura() throws InterruptedException {
        l.lock();
        try {
            numLettoriAttivi--;
            if (numLettoriAttivi == 0) {
                possoScrivere.signal();
            }
        } finally {
            l.unlock();
        }
    }
}
