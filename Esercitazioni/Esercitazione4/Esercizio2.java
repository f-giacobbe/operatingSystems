package Esercitazioni.Esercitazione4;

import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Esercizio2 {
    private static final Semaphore semaphoreA = new Semaphore(1);
    private static final Semaphore semaphoreB = new Semaphore(0);
    private static boolean hasUserStopped = false;

    private static class A extends Thread {
        @Override
        public void run() {
            try {
                semaphoreA.acquire();
                System.out.print("A");
                semaphoreB.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class B extends Thread {
        @Override
        public void run() {
            try {
                semaphoreB.acquire();
                System.out.print("B ");
                semaphoreA.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class WaitUserInput extends Thread {
        @Override
        public void run() {
            Scanner sc = new Scanner(System.in);

            if (sc.nextLine() != null) {
                hasUserStopped = true;
            }
        }
    }


    public static void main(String[] args) {
        new WaitUserInput().start();
        System.out.println("Press enter to stop execution");

        while (!hasUserStopped) {
            new A().start();
            new B().start();

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
