package Esercitazioni.Esercitazione8;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Cliente extends Thread {
    private static final Random random = new Random();
    private final AziendaAgricola aziendaAgricola;
    private int numeroSacchiDaPrelevare;
    private final int id;

    public Cliente(AziendaAgricola aziendaAgricola, int id) {
        this.aziendaAgricola = aziendaAgricola;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            attendi(0, 20);
            numeroSacchiDaPrelevare = decidi(10);

            aziendaAgricola.paga(numeroSacchiDaPrelevare);

            while (numeroSacchiDaPrelevare > 0) {
                aziendaAgricola.ritira();
                System.out.printf("Il cliente %d ha prelevato 1 sacco dal magazzino. Deve ancora prelevare %d sacchi%n", id, numeroSacchiDaPrelevare);
                spostaInAuto();
                numeroSacchiDaPrelevare--;
            }
        } catch (InterruptedException _) {}
    }

    private void spostaInAuto() throws InterruptedException {
        TimeUnit.MINUTES.sleep(1);
    }

    private int decidi(int max) {
        return random.nextInt(max) + 1;
    }

    private void attendi(int min, int max) throws InterruptedException {
        TimeUnit.SECONDS.sleep(random.nextInt(max - min + 1) + min);
    }
}
