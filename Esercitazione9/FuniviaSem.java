package Esercitazione9;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class FuniviaSem extends Funivia {
    private int turno = -1;     //se pari salgono i turisti a piedi, se dispari salgono i turisti in bici
    private int postiOccupati = 0;
    private final LinkedList<Turista> turistiInFunivia = new LinkedList<>();
    private final Semaphore mutex = new Semaphore(1);   //per controllare l'accesso alle variabili condivise "postiOccupati" e "turistiInFunivia"
    private final Semaphore[] turistaPossoSalire = {new Semaphore(0), new Semaphore(0)};
    private final Semaphore pilotaPossoSalita = new Semaphore(0);
    private final Semaphore[] turistaPossoScendere = {new Semaphore(0), new Semaphore(0)};
    private final Semaphore pilotaPossoDiscesa = new Semaphore(0);

    @Override
    public void pilotaStart() throws InterruptedException {
        turno = (turno + 1) % 2;
        turistaPossoSalire[turno].release(NUM_TURISTI[turno]);
        pilotaPossoSalita.acquire();
    }

    @Override
    public void pilotaEnd() throws InterruptedException {
        for (Turista t : turistiInFunivia) {
            System.out.printf("Turista di tipo %d con id %d è sceso dalla funivia%n", t.getTipo(), t.threadId());
        }
        turistaPossoScendere[turno].release(NUM_TURISTI[turno]);
        turistiInFunivia.clear();
        pilotaPossoDiscesa.acquire();
    }

    @Override
    public void turistaSali(int tipo) throws InterruptedException {
        turistaPossoSalire[tipo].acquire();
        mutex.acquire();

        postiOccupati += 1 + tipo;  //se sono a piedi occupo un posto (tipo=0), altrimenti 2 posti (tipo=1)
        turistiInFunivia.add((Turista) Thread.currentThread());

        if (postiOccupati == POSTI_FUNIVIA) {   //se sono l'ultimo a salire, segnalo al pilota che può partire
            pilotaPossoSalita.release();
        }

        mutex.release();
    }

    @Override
    public void turistaScendi(int tipo) throws InterruptedException {
        turistaPossoScendere[turno].acquire();
        mutex.acquire();

        postiOccupati -= 1 + tipo;

        if (postiOccupati == 0) {   //se sono l'ultimo a scendere, segnalo al pilota che può iniziare la discesa
            pilotaPossoDiscesa.release();
        }

        mutex.release();
    }


    public static void main(String[] args) {
        Funivia funivia = new FuniviaSem();
        int numTuristiPiedi = 18;
        int numTuristiBici = 9;
        funivia.test(numTuristiPiedi, numTuristiBici);
    }
}
