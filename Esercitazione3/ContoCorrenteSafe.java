package Esercitazione3;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class ContoCorrenteSafe extends ContoCorrente {
    protected AtomicInteger deposito;

    public ContoCorrenteSafe(int depositoIniziale) {
        super(depositoIniziale);
        deposito = new AtomicInteger(depositoIniziale);
    }

    public abstract void deposita(int importo);
    public abstract void preleva(int importo);

    public int getDeposito() {
        return deposito.get();
    }
}