package Simulazioni.giu24;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public abstract class ProntoSoccorso {
    private static Random random = new Random();
    protected static final int[] TEMPI_ATTESA  = {40, 20, 15, 10};
    //protected static final int[] TEMPI_ATTESA  = {1, 1, 1, 1};


    public abstract void iniziaVisita() throws InterruptedException;

    public abstract void terminaVisita() throws InterruptedException;

    public abstract void accediPaziente(int codice) throws InterruptedException;

    public abstract void esciPaziente() throws InterruptedException;

    public void test(int numPazienti) {
        int pazientiPerCodice = numPazienti / 3;

        //1/3 codice verde
        for (int i = 0; i < pazientiPerCodice; i++) {
            new Paziente(this, 2).start();
        }

        //1/3 codice giallo
        for (int i = 0; i < pazientiPerCodice; i++) {
            new Paziente(this, 1).start();
        }

        //1/3 codice rosso
        for (int i = 0; i < pazientiPerCodice; i++) {
            new Paziente(this, 0).start();
        }

        Medico m = new Medico(this);
        m.setDaemon(true);
        m.start();
    }

    protected static void attendiMinuti(int min, int max) throws InterruptedException {
        TimeUnit.MINUTES.sleep(random.nextInt(max + 1) + min);
    }

    protected static void visita(int codice) throws InterruptedException {
        attendiMinuti(TEMPI_ATTESA[codice + 1], TEMPI_ATTESA[codice]);
    }
}
