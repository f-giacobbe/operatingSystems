package Esercitazione4;

import java.util.concurrent.Semaphore;

public class Esercizio1 {
    private static final Semaphore mutex = new Semaphore(1);

    public static class Stampante extends Thread {
        public Stampante(String name) {
            super(name);
        }

        @Override
        public void run() {
            try {
                mutex.acquire();
                System.out.println(getName());
                mutex.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void main(String[] args) {
        Thread p1 = new Stampante("A");
        Thread p2 = new Stampante("B");
        p1.start();
        p2.start();
    }
}
