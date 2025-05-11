package Esercitazione1;

class Contatore implements Runnable {
    private int n = 0;

    public Contatore(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }

        this.n = n;
    }

    @Override
    public void run() {
        for (int i = 0; i < n; i++) {
            System.out.println(i);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


public class ThreadTest {
    public static void main(String[] args) {
        Contatore contaFinoADieci = new Contatore(10);

        Thread t1 = new Thread(contaFinoADieci);
        Thread t2 = new Thread(contaFinoADieci);
        t1.start();
        t2.start();
    }
}
