package Esercitazione6.barbiere;

import java.util.concurrent.TimeUnit;

public class Barbiere extends Thread {
    private Sala sala;

    public Barbiere(Sala sala) {
        this.sala = sala;
    }

    @Override
    public void run() {
        while (true) {
            try {
                sala.tagliaCapelli();
                taglio();
            } catch (InterruptedException _) {

            }
        }
    }

    private static void taglio() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        System.out.println("Taglio finito!");
    }
}
