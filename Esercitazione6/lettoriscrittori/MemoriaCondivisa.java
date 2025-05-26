package Esercitazione6.lettoriscrittori;

public abstract class MemoriaCondivisa {
    public abstract void inizioScrittura() throws InterruptedException;
    public abstract void fineScrittura() throws InterruptedException;
    public abstract void inizioLettura() throws InterruptedException;
    public abstract void fineLettura() throws InterruptedException;
}
