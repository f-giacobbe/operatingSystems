package Esercitazioni.Esercitazione2;

public class SommaConcorrente {
    public static class Sommatore extends Thread {
        private int da;
        private int a;
        private int somma;

        public Sommatore(int da, int a) {
            this.da = da;
            this.a = a;
        }

        @Override
        public void run() {
            somma = 0;
            for (int i = da; i <= a; i++) {
                somma += i;
            }
        }

        public int getSomma() throws InterruptedException {
            this.join();
            /* in questo modo prima di restituire la somma ci assicuriamo di non restituirne una parziale
            aspettando prima che il metodo run abbia terminato tutte le istruzioni.
             */
            return somma;
        }
    }


    public static void main(String[] args) {
        Sommatore s1 = new Sommatore(1, 500000);
        Sommatore s2 = new Sommatore(500001, 1000000);

        /* piuttosto che utilizzare un solo sommatore che sommi da 1 a 1 milione suddividiamo il carico
        di lavoro su più thread (in questo caso 2) in modo tale da dimezzare (quasi) il tempo di esecuzione
         */

        s1.start();
        s2.start();

        try {
            System.out.println(s1.getSomma() + s2.getSomma());
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }
}
