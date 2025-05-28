package Esercitazioni.Esercitazione2;

public class ProdottoScalare extends Thread{
    private final int[] a;
    private final int[] b;
    private final int inizio;
    private final int fine;
    private int res;

    public ProdottoScalare(int[] a, int[] b, int inizio, int fine) {
        this.a = a;
        this.b = b;
        this.inizio = inizio;
        this.fine = fine;
    }

    @Override
    public void run() {
        for (int i = inizio; i <= fine; i++) {
            res += a[i] * b[i];
        }
    }

    public int getProdottoScalare() {
        try {
            this.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return res;
    }







    public static void main(String[] args) throws InterruptedException {
        long startTime = System.nanoTime();
        int[] a = {5, 6, 3, 7, 5, 6, 3, 7};
        int[] b = {9, 7, 1, 0, 6, 3, 7, 5};

        int n = a.length;
        int m = 2;      //costante definita dal programma, si assume n multiplo di m

        int prodottoScalare = 0;
        ProdottoScalare[] threads = new ProdottoScalare[m];
        for (int i = 0; i < m; i++) {   //faccio partire tutti i thread su porzioni diverse degli array
            ProdottoScalare t = new ProdottoScalare(a, b, i*n/m, (i+1)*n/m-1);
            threads[i] = t;
            t.start();
        }

        /*sommo i risultati parziali assicurandomi però che il singolo thread abbia terminato l'esecuzione
        prima di restituirmi il risultato parziale (c'è il this.join() all'interno del metodo)
         */
        for (ProdottoScalare t : threads) {
            prodottoScalare += t.getProdottoScalare();
        }

        System.out.println(prodottoScalare);

        long endTime = System.nanoTime();
        System.out.println((endTime-startTime)/1000);
    }
}
