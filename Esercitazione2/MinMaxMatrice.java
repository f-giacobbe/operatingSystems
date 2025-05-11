package Esercitazione2;

public class MinMaxMatrice {
    private static class MaxRiga extends Thread {
        private final int[][] m;
        private final int riga;
        private final int lunghezzaRiga;
        private int res;

        public MaxRiga(int[][] m, int riga) {
            this.m = m;
            this.riga = riga;
            lunghezzaRiga = m[riga].length;
            if (lunghezzaRiga == 0) {
                throw new IllegalArgumentException("La riga deve avere dimensione non nulla!");
            }
        }

        @Override
        public void run() {
            //con l'ipotesi che la lunghezza della riga sia > 0
            int maxRiga = m[riga][0];
            for (int i = 1; i < lunghezzaRiga; i++) {
                if (m[riga][i] > maxRiga) {
                    maxRiga = m[riga][i];
                    res = i;
                }
            }
        }

        public int getRes() {
            //!!! Va gestita al di fuori in quanto altrimenti potrebbe restituire risultati incorretti
            return res;
        }

        public int getRiga() {
            return riga;
        }
    }



    private static class MinColonna extends Thread {
        private final int[][] m;
        private final int colonna;
        private final int lunghezzaColonna;
        private int res;

        public MinColonna(int[][] m, int colonna) {
            this.m = m;
            this.colonna = colonna;
            this.lunghezzaColonna = m.length;
            if (lunghezzaColonna == 0) {
                throw new IllegalArgumentException("La colonna deve avere dimensione non nulla!");
            }
        }

        @Override
        public void run() {
            //con l'ipotesi che la lunghezza della colonna sia > 0
            int minColonna = m[0][colonna];
            for (int i = 1; i < lunghezzaColonna; i++) {
                if (m[i][colonna] < minColonna) {
                    minColonna = m[i][colonna];
                    res = i;
                }
            }
        }

        public int getRes() {
            //!!! Va gestita al di fuori in quanto altrimenti potrebbe restituire risultati incorretti
            return res;
        }

        public int getColonna() {
            return colonna;
        }
    }


    public static void verifica(int[][] matrice) {
        int n = matrice.length;
        int m = matrice[0].length;

        MaxRiga[] massimiRighe = new MaxRiga[n];
        MinColonna[] minimiColonne = new MinColonna[m];

        //faccio partire tutti i thread
        for (int i = 0; i < n; i++) {
            massimiRighe[i] = new MaxRiga(matrice, i);
            massimiRighe[i].start();
        }
        for (int i = 0; i < m; i++) {
            minimiColonne[i] = new MinColonna(matrice, i);
            minimiColonne[i].start();
        }

        for (MaxRiga t : massimiRighe) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for (MinColonna t : minimiColonne) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        for (MaxRiga t : massimiRighe) {
            int colonna = t.getRes();

            for (MinColonna u : minimiColonne) {
                int riga = u.getRes();

                if (colonna == u.getColonna() && riga == t.getRiga()) {
                    System.out.printf("Riga %d, Colonna %d\n", riga, colonna);
                }
            }
        }
    }





    public static void main(String[] args) {
        int[][] m1 = {{2, 7, 2, 5, 2},
                     {2, 2, 9, 7, 2},
                     {1, 1, 1, 4, 1},
                     {2, 2, 2, 9, 2}};

        verifica(m1);


        int[][] m2 = {{2, 7, 2, 5, 2},
                {2, 5, 9, 7, 2},
                {1, 4, 1, 4, 1},
                {2, 5, 2, 9, 2}};

        verifica(m2);
    }
}
