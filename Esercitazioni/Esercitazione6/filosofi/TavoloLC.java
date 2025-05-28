package Esercitazioni.Esercitazione6.filosofi;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TavoloLC extends Tavolo {
    private final Lock l = new ReentrantLock();
    private Condition[] possoMangiare = new Condition[NUM_FILOSOFI];

    public TavoloLC() {
        for (int i = 0; i < NUM_FILOSOFI; i++) {
            possoMangiare[i] = l.newCondition();
        }
    }

    public static void main(String[] args) {
        TavoloLC tavolo = new TavoloLC();
        tavolo.test();
    }

    @Override
    public void prendiBacchette(int i) throws InterruptedException{
        l.lock();
        try {
            while (bacchette[i] || bacchette[(i + 1) % NUM_FILOSOFI]) {
                if (bacchette[i]) {
                    possoMangiare[i].await();
                } else {
                    possoMangiare[(i + 1) % NUM_FILOSOFI].await();
                }
            }
            bacchette[i] = true;
            bacchette[(i + 1) % NUM_FILOSOFI] = true;
        } finally {
            l.unlock();
        }
    }

    @Override
    public void rilasciaBacchette(int i) {
        l.lock();
        try {
            bacchette[i] = false;
            bacchette[(i + 1) % NUM_FILOSOFI] = false;
            possoMangiare[i].signal();
            possoMangiare[(i + 1) % NUM_FILOSOFI].signal();
        } finally {
            l.unlock();
        }
    }
}
