package Esercitazione4;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class Esercizio3 {
    private static final Semaphore mutex = new Semaphore(1);
    private static final Semaphore turno = new Semaphore(1);
    private static int threadRigaAttivi;
    private static int threadColonnaAttivi;

    private static final int N = 3;
    private static final int M = 4;
    private static final int X = 50;

    private static class DecrementaRiga extends Thread{
        private final int[][] matrice;
        private final int riga;

        public DecrementaRiga(int[][] matrice, int riga) {
            this.matrice = matrice;
            this.riga = riga;
        }

        @Override
        public void run() {
            try {
                mutex.acquire();
                if (threadRigaAttivi == 0) {    //se sono il primo thread riga aspetto le colonne
                    turno.acquire();
                }
                threadRigaAttivi++;
                mutex.release();

                for (int c = 0; c < X; c++) {
                    for (int i = 0; i < M; i++) {
                        matrice[riga][i]--;
                    }
                }

                mutex.acquire();
                threadRigaAttivi--;
                if (threadRigaAttivi == 0) {    //se non ci sono altri thread riga attivi faccio lavorare le colonne
                    turno.release();
                }
                mutex.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private static class IncrementaColonna extends Thread {
        private final int[][] matrice;
        private final int colonna;

        public IncrementaColonna(int[][] matrice, int colonna) {
            this.matrice = matrice;
            this.colonna = colonna;
        }

        @Override
        public void run() {
            try {
                mutex.acquire();
                if (threadColonnaAttivi == 0) {
                    turno.acquire();
                }
                threadColonnaAttivi++;
                mutex.release();

                for (int c = 0; c < X; c++) {
                    for (int i = 0; i < N; i++) {
                        matrice[i][colonna]++;
                    }
                }

                mutex.acquire();
                threadColonnaAttivi--;
                if (threadColonnaAttivi == 0) {
                    turno.release();
                }
                mutex.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void main(String[] args) throws InterruptedException{
        int[][] matrice = new int[N][M];

        Thread[] threadRiga = new DecrementaRiga[N];
        Thread[] threadColonna = new IncrementaColonna[M];

        for (int i = 0; i < N; i++) {
            threadRiga[i] = new DecrementaRiga(matrice, i);
            threadRiga[i].start();
        }
        //Thread.sleep(100) necessario se aumentiamo X
        for (int i = 0; i < M; i++) {
            threadColonna[i] = new IncrementaColonna(matrice, i);
            threadColonna[i].start();
        }

        for (Thread t : threadRiga) {
            t.join();
        }
        for (Thread t : threadColonna) {
            t.join();
        }

        for (int[] riga : matrice) {
            System.out.println(Arrays.toString(riga));
        }
    }
}
