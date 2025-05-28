package Esercitazioni.Esercitazione5;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class BufferInfinito extends Buffer{
    private LinkedList<Integer> buffer = new LinkedList<>();
    private Semaphore ciSonoElementi = new Semaphore(0);
    private Semaphore mutex = new Semaphore(1);

    @Override
    public void put(int i) {
        try {
            mutex.acquire();
            buffer.add(i);
            mutex.release();
            ciSonoElementi.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int get() {
        try {
            ciSonoElementi.acquire();
            mutex.acquire();
            int prodotto = buffer.pop();
            mutex.release();
            return prodotto;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    //TEST
    public static void main(String[] args) throws InterruptedException{
        BufferInfinito b = new BufferInfinito();
        Produttore p = new Produttore(b);
        Consumatore c = new Consumatore(b);

        c.start();
        TimeUnit.SECONDS.sleep(5);
        p.start();
    }
}
