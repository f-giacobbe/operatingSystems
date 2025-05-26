package Esercitazione9;

import java.util.concurrent.TimeUnit;

public class Pilota implements Runnable {
    private Funivia funivia;

    private final int TEMPO_SALITA = 5;
    private final int TEMPO_DISCESA = 2;

    public Pilota(Funivia f) {
        funivia = f;
    }

    @Override
    public void run() {
        try {
            while (true) {
                funivia.pilotaStart();
                attendi(TEMPO_SALITA);
                funivia.pilotaEnd();
                attendi(TEMPO_DISCESA);
            }
        } catch (InterruptedException _) {

        }
    }

    private static void attendi(int min) throws InterruptedException{
        TimeUnit.MINUTES.sleep(min);
    }
}
