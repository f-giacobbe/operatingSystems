package Simulazioni.gen24;

import java.util.concurrent.TimeUnit;

public class Bibliotecario extends Thread {
    private Biblioteca biblioteca;
    private static final int PAUSA = 10;
    private static final int MAX_CLIENTI = 15;

    public Bibliotecario(Biblioteca b) {
        biblioteca = b;
        this.setDaemon(true);   //si tratta di un thread demone
    }

    @Override
    public void run() {
        int utentiServiti = 0;
        try {
            while (true) {
                biblioteca.registraPrestito();
                biblioteca.prossimoUtente();
                utentiServiti++;

                if (utentiServiti == MAX_CLIENTI) {
                    System.out.println("Ho lavorato assai. Faccio una pausa.");
                    TimeUnit.SECONDS.sleep(PAUSA);     //pausa di 10 minuti ogni 15 utenti serviti
                    utentiServiti = 0;
                }
            }
        } catch (InterruptedException _) {

        }
    }
}
