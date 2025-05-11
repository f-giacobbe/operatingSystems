package Esercitazione3;

public abstract class ContoCorrente {
    protected int deposito;

    public ContoCorrente(int depositoIniziale) {
        deposito = depositoIniziale;
    }

    public abstract void deposita(int importo);
    public abstract void preleva(int importo);

    public int getDeposito() {
        return deposito;
    }
}