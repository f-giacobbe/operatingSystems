package Esercitazione8;

public abstract class AziendaAgricola {
    protected int incasso;
    protected int sacchetti;
    protected final int sacchettiIniziali;
    protected final int PREZZO_SACCO = 3;

    public AziendaAgricola(int sacchettiIniziali) {
        this.sacchetti = sacchettiIniziali;
        this.sacchettiIniziali = sacchettiIniziali;
    }

    public abstract void paga(int numSacchi) throws InterruptedException;
    public abstract void ritira() throws InterruptedException;
    public abstract void carica() throws InterruptedException;

    public void test(int numClienti) {
        Cliente[] threadClienti = new Cliente[numClienti];

        for (int i = 0; i < numClienti; i++) {
            threadClienti[i] = new Cliente(this, i);
            threadClienti[i].start();
        }

        Magazziniere magazziniere = new Magazziniere(this);
        magazziniere.setDaemon(true);
        magazziniere.start();

        for (int i = 0; i < numClienti; i++) {
            try {
                threadClienti[i].join();        //dopo che finiscono tutti i clienti termina la giornata
            } catch (InterruptedException _) {}
        }

        System.out.printf("Incasso complessivo: %d%n", incasso);
    }

    public int getSacchettiIniziali() {
        return sacchettiIniziali;
    }
}
