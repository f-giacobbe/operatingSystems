package Esercitazioni.Esercitazione2;

public class StampanteThread{
    public static class StampanteT extends Thread {
        private int da;
        private int a;

        public StampanteT(int da, int a) {
            this.da = da;
            this.a = a;
        }

        @Override
        public void run() {
            for (int i = da; i <= a; i++) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
    }


    public static void main(String[] args) {
        StampanteT s1 = new StampanteT(1, 10);
        StampanteT s2 = new StampanteT(11, 20);
        s1.start();
        s2.start();
        System.out.println("Fine");
        /*in questo caso l'output NON È DETERMINISTICO in quanto non sappiamo in che ordine la cpu deciderà
        a runtime di eseguire i thread.

        Cerchiamo quindi un modo per sincronizzare i due thread in modo da sapere esattamente come verranno
        eseguiti (vedi SommaConcorrente)
         */
    }
}
