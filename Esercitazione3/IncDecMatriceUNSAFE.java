package Esercitazione3;

import java.util.Arrays;

public class IncDecMatriceUNSAFE {
    public static class DecRiga extends Thread {
        private final int[][] matrice;
        private final int riga;

        public DecRiga(int[][] matrice, int riga) {
            if (riga >= matrice.length) {
                throw new RuntimeException();
            }

            this.matrice = matrice;
            this.riga = riga;
        }

        @Override
        public void run() {
            for (int i = 0; i < matrice[0].length; i++) {
                matrice[riga][i] -= 1;
            }
        }
    }



    public static class IncColonna extends Thread {
        private final int[][] matrice;
        private final int colonna;

        public IncColonna(int[][] matrice, int colonna) {
            if (matrice.length == 0 || colonna >= matrice[0].length) {
                throw new RuntimeException();
            }

            this.matrice = matrice;
            this.colonna = colonna;
        }

        @Override
        public void run() {
            for (int i = 0; i < matrice.length; i++) {
                matrice[i][colonna] += 1;
            }
        }
    }














    public static void main(String[] args) throws InterruptedException{
        int x = 2000;
        int[][] matrice = {     //6x7
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0}
                };
        int n = matrice.length;
        int m = matrice[0].length;

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

        for (int[] row : matrice) {
            System.out.println(Arrays.toString(row));
        }
    }
}
