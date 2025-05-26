package Esercitazione6.barbiere;

public abstract class Sala {
    protected final int numSedie;
    protected int sedieLibere;

    public Sala(int numSedie) {
        this.numSedie = numSedie;
        sedieLibere = numSedie;
    }

    public abstract void tagliaCapelli() throws InterruptedException;
    public abstract boolean attendiTaglio() throws InterruptedException;
}
