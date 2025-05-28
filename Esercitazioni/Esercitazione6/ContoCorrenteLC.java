package Esercitazioni.Esercitazione6;

import Esercitazioni.Esercitazione3.ContoCorrente;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ContoCorrenteLC extends ContoCorrente {
    private final Lock l = new ReentrantLock();

    public ContoCorrenteLC(int deposito) {
        super(deposito);
    }

    @Override
    public void deposita(int importo) {
        l.lock();
        try {
            deposito += importo;
        } finally {
            l.unlock();
        }
    }

    @Override
    public void preleva(int importo) {
        l.lock();
        try {
            deposito -= importo;
        } finally {
            l.unlock();
        }
    }
}
