package Esercitazione8;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class AziendaAgricolaSem extends AziendaAgricola {
    private final Semaphore mutex = new Semaphore(1);
    private final Semaphore cassa = new Semaphore(1, true);
    private final Semaphore magazzino;
    private final Semaphore magazziniere = new Semaphore(0);

    public AziendaAgricolaSem(int sacchettiIniziali) {
        super(sacchettiIniziali);
        magazzino = new Semaphore(sacchettiIniziali, true);
    }

    @Override
    public void paga(int numSacchi) throws InterruptedException {
        cassa.acquire();
        incasso += numSacchi * PREZZO_SACCO;
        cassa.release();
        // NOTA - In questo caso non occorre il mutex in quanto cassa ha un solo permesso
    }

    @Override
    public void ritira() throws InterruptedException {
        magazzino.acquire();
        mutex.acquire();
        sacchetti--;
        System.out.printf("Sacchetti = %d%n", sacchetti);
        if (sacchetti == 0) {
            magazziniere.release();
        }
        mutex.release();
    }

    @Override
    public void carica() throws InterruptedException {
        magazziniere.acquire();
        TimeUnit.MINUTES.sleep(10);
        sacchetti = sacchettiIniziali;
        magazzino.release(sacchettiIniziali);
    }
}
