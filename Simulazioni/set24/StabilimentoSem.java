package Simulazioni.set24;

import java.util.concurrent.Semaphore;

public class StabilimentoSem extends Stabilimento {
    private Semaphore mutex = new Semaphore(1);     //semaforo per gestire la mutua esclusione dei thread Bagnante
    private Semaphore postazioniScelte = new Semaphore(0);  //semaforo per far aspettare al gestore che tutti i bagnanti abbiano scelto una postazione
    private Semaphore postazioniPreparate = new Semaphore(0);   //semaforo per segnalare ai bagnanti l'avvenuta preparazione delle postazioni
    private Semaphore cassa = new Semaphore(1, true);
    private Semaphore postazioniLasciate = new Semaphore(0);    //semaforo per segnalare al gestore che i bagnanti hanno lasciato lo stabilimento

    private int postazioni[] = new int[2];  //in posizione 0 il numero di lettini; in posizione 1 il numero di ombrelloni
    private int incasso;

    public StabilimentoSem(int N) {
        super(N);
    }

    @Override
    public void scegliAccesso() throws InterruptedException {
        int scelta = scegliPosto();     //scelgo il posto
        ((Bagnante) Thread.currentThread()).setScelta(scelta);
        System.out.printf("Ho appena scelto %s%n", scelta == LETTINO ? "LETTINO" : "OMBRELLONE");

        mutex.acquire();
        postazioni[scelta]++;   //aggiorno l'array condiviso in mutua esclusione
        mutex.release();

        postazioniScelte.release();     //segnalo al gestore l'avvenuta scelta
        postazioniPreparate.acquire();      //mi metto in attesa che il gestore prepari le postazioni
    }

    @Override
    public void preparaPostazioni() throws InterruptedException {
        postazioniScelte.acquire(N);    //aspetto che tutta la comitiva scelga la postazione
        System.out.println("Ok siccome avete scelto tutti adesso preparo le vostre postazioni");
        prepara(postazioni);
        System.out.printf("Perfetto! Ho appena finito di preparare %d lettini e %d ombrelloni%n", postazioni[LETTINO], postazioni[OMBRELLONE]);
        postazioniPreparate.release(N);     //segnalo ai bagnanti che possono accedere alle loro postazioni
    }

    @Override
    public void paga() throws InterruptedException {
        int scelta = ((Bagnante) Thread.currentThread()).getScelta();   //0 se il bagnante aveva scelto lettino, 1 se aveva scelto ombrellone
        cassa.acquire();    //mi metto in coda alla cassa

        mutex.acquire();
        System.out.printf("Ok allora pago %d. Grazie mille me ne vado%n", PREZZO[scelta]);
        incasso += PREZZO[scelta];
        mutex.release();

        cassa.release();    //do il turno al prossimo bagnante in fila alla cassa
        postazioniLasciate.release();   //segnalo al gestore che ho lasciato lo stabilimento
    }

    @Override
    public void chiusura() throws InterruptedException {
        postazioniLasciate.acquire(N);  //aspetto che tutti i bagnanti abbiano lasciato lo stabilimento
        System.out.printf("Stabilimento chiuso! Oggi abbiamo guadagnato %d euro. Grandioso!%n", incasso);
    }






    public static void main(String[] args) {
        int numBagnanti = 10;
        StabilimentoSem s = new StabilimentoSem(numBagnanti);
        s.test();
    }
}
