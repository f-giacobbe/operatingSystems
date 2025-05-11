package Esercitazione4;

import java.util.concurrent.Semaphore;

public class ContoCorrenteSemafori extends Esercitazione3.ContoCorrente {
    private final Semaphore mutex = new Semaphore(1);

    public ContoCorrenteSemafori(int depositoIniziale) {
        super(depositoIniziale);
    }

    @Override
    public void deposita(int importo) {
        try {
            mutex.acquire();
            deposito += importo;
            mutex.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void preleva(int importo) {
        try {
            mutex.acquire();
            deposito -= importo;
            mutex.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
