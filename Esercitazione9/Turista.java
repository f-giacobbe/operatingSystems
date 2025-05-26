package Esercitazione9;

public class Turista extends Thread {
    private Funivia funivia;
    private int tipo;

    public Turista(Funivia f, int t) {
        if (t != 0 && t != 1) {
            throw new IllegalArgumentException("Il tipo può essere solo 0 oppure 1");
        }
        funivia = f;
        tipo = t;
    }

    @Override
    public void run() {
        try {
            funivia.turistaSali(tipo);
            funivia.turistaScendi(tipo);
        } catch (InterruptedException _) {

        }
    }

    public int getTipo() {
        return tipo;
    }
}
