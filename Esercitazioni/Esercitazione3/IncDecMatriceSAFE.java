package Esercitazioni.Esercitazione3;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class IncDecMatriceSAFE {
    public static class DecRiga extends Thread {
        private final AtomicInteger[][] matrice;
        private final int riga;

        public DecRiga(AtomicInteger[][] matrice, int riga) {
            if (riga >= matrice.length) {
                throw new RuntimeException();
            }

            this.matrice = matrice;
            this.riga = riga;
        }

        @Override
        public void run() {
            for (int i = 0; i < matrice[0].length; i++) {
                matrice[riga][i].addAndGet(-1);
            }
        }
    }



    public static class IncColonna extends Thread {
        private final AtomicInteger[][] matrice;
        private final int colonna;

        public IncColonna(AtomicInteger[][] matrice, int colonna) {
            if (matrice.length == 0 || colonna >= matrice[0].length) {
                throw new RuntimeException();
            }

            this.matrice = matrice;
            this.colonna = colonna;
        }

        @Override
        public void run() {
            for (AtomicInteger[] atomicIntegers : matrice) {
                atomicIntegers[colonna].addAndGet(1);
            }
        }
    }














    public static void main(String[] args) throws InterruptedException{
        int x = 1;
        int n = 6;
        int m = 7;
        AtomicInteger[][] matrice = new AtomicInteger[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrice[i][j] = new AtomicInteger(0);
            }
        }


        for (int i = 0; i < x; i++) {
            DecRiga[] threadRiga = new DecRiga[n];
            IncColonna[] threadColonna = new IncColonna[m];
            for (int j = 0; j < n; j++) {
                threadRiga[j] = new DecRiga(matrice, j);
                threadRiga[j].start();
            }

            for (int j = 0; j < m; j++) {
                threadColonna[j] = new IncColonna(matrice, j);
                threadColonna[j].start();
            }


            for (int j = 0; j < n; j++) {
                threadRiga[j].join();
            }

            for (int j = 0; j < m; j++) {
                threadColonna[j].join();
            }
        }

        for (AtomicInteger[] row : matrice) {
            System.out.println(Arrays.toString(row));
        }
    }
}
