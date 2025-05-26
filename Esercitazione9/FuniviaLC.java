package Esercitazione9;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FuniviaLC extends Funivia {
    private int turno = -1;
    private int postiOccupati = 0;
    private final LinkedList<Turista> turistiInFunivia = new LinkedList<>();
    private final Lock l = new ReentrantLock();
    private final Condition[] turistaPossoSalire = {l.newCondition(), l.newCondition()};
    private final Condition pilotaPossoSalita = l.newCondition();
    private final Condition[] turistaPossoScendere = {l.newCondition(), l.newCondition()};
    private final Condition pilotaPossoDiscesa = l.newCondition();
    private boolean turistiPossonoSalire = false;
    private boolean turistiPossonoScendere = false;

    @Override
    public void pilotaStart() throws InterruptedException {
        l.lock();
        try {
            turno = (turno + 1) % 2;
            turistiPossonoSalire = true;
            turistaPossoSalire[turno].signalAll();

            while(!possoIniziareSalita()) {
                pilotaPossoSalita.await();
            }

            turistiPossonoSalire = false;
        } finally {
            l.unlock();
        }
    }

    private boolean possoIniziareSalita() {
        return postiOccupati == POSTI_FUNIVIA;
    }

    @Override
    public void pilotaEnd() throws InterruptedException {
        l.lock();
        try {
            for (Turista t : turistiInFunivia) {
                System.out.printf("Turista di tipo %d con id %d è sceso dalla funivia%n", t.getTipo(), t.threadId());
            }

            turistaPossoScendere[turno].signalAll();
            turistiInFunivia.clear();
            turistiPossonoScendere = true;

            while (!sonoScesiTutti()) {
                pilotaPossoDiscesa.await();
            }

            turistiPossonoScendere = false;
        } finally {
            l.unlock();
        }
    }

    private boolean sonoScesiTutti() {
        return postiOccupati == 0;
    }

    @Override
    public void turistaSali(int tipo) throws InterruptedException {
        l.lock();
        try {
            while (!possoSalire(tipo)) {
                turistaPossoSalire[tipo].await();
            }

            postiOccupati += 1 + tipo;
            turistiInFunivia.add((Turista) Thread.currentThread());

            if (postiOccupati == 6) {
                pilotaPossoSalita.signal();
            }
        } finally {
            l.unlock();
        }
    }

    private boolean possoSalire(int tipo) {
        return turistiPossonoSalire && turno == tipo && postiOccupati < POSTI_FUNIVIA;
    }

    @Override
    public void turistaScendi(int tipo) throws InterruptedException {
        l.lock();
        try {
            while (!possoScendere(tipo)) {
                turistaPossoScendere[tipo].await();
            }

            postiOccupati -= 1 + tipo;

            if (postiOccupati == 0) {
                pilotaPossoDiscesa.signal();
            }
        } finally {
            l.unlock();
        }
    }

    private boolean possoScendere(int tipo) {
        return turistiPossonoScendere && tipo == turno && postiOccupati > 0;
    }
}
