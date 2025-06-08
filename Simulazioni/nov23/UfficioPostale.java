package Simulazioni.nov23;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public abstract class UfficioPostale {
    protected static final int TICKET_MAX = 50;

    protected final HashMap<String, Integer> TEMPI = new HashMap<>();

    protected HashMap<String, Integer> ticketStampati = new HashMap<>();

    protected UfficioPostale() {
        TEMPI.put("A", 3);
        TEMPI.put("B", 5);
        TEMPI.put("C", 7);

        ticketStampati.put("A", 0);
        ticketStampati.put("C", 0);
        ticketStampati.put("B", 0);
    }

    public abstract boolean ritiraTicket(String operazione) throws InterruptedException;

    public abstract void attendiSportello(String operazione) throws InterruptedException;

    public abstract void prossimoCliente() throws InterruptedException;

    public abstract void eseguiOperazione() throws InterruptedException;

    protected void svolgi(String op) throws InterruptedException {
        TimeUnit.SECONDS.sleep(TEMPI.get(op));
    }

    protected void test(int numClienti) {
        int n = numClienti / 3;

        for (int i = 0; i < n; i++)
            new Cliente(this, "A").start();
        for (int i = 0; i < n; i++)
            new Cliente(this, "B").start();
        for (int i = 0; i < n; i++)
            new Cliente(this, "C").start();

        new Impiegato(this, "A").start();
        new Impiegato(this, "B").start();
        new Impiegato(this, "C").start();
    }
}
