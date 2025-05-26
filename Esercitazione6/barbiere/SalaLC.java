package Esercitazione6.barbiere;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SalaLC extends Sala {
    private Lock l = new ReentrantLock();
    private Condition clienteDisponibile = l.newCondition();
    private Condition poltrona = l.newCondition();
    private LinkedList<Cliente> clienti = new LinkedList<>();
    protected boolean poltronaLibera = false;

    public SalaLC(int numPosti) {
        super(numPosti);
    }

    @Override
    public void tagliaCapelli() throws InterruptedException {
        l.lock();
        try {
            while (!possoTagliare()) {
                clienteDisponibile.await();
            }
            poltronaLibera = true;
            poltrona.signalAll();
        } finally {
            l.unlock();
        }
    }

    private boolean possoTagliare() {
        return sedieLibere < numSedie;
    }

    @Override
    public boolean attendiTaglio() throws InterruptedException {
        l.lock();
        Cliente c = (Cliente) Thread.currentThread();
        try {
            if (sedieLibere == 0) {
                return false;
            }
            clienti.add(c);
            sedieLibere--;
            clienteDisponibile.signal();
            while (devoAttendere()) {
                poltrona.await();
            }
            //è il mio turno
            clienti.remove();
            poltronaLibera = false;
            sedieLibere++;
            return true;
        } finally {
            l.unlock();
        }
    }

    private boolean devoAttendere() {
        return Thread.currentThread() != clienti.getFirst() || !poltronaLibera;
    }


    public static void main(String[] args) throws InterruptedException{
        int numSedie = 5;
        int numClienti = 10;
        SalaLC sala = new SalaLC(numSedie);
        Barbiere b = new Barbiere(sala);
        b.setDaemon(true);
        b.start();
        TimeUnit.SECONDS.sleep(1);
        Cliente[] clienti = new Cliente[numClienti];
        for (int i = 0; i < numClienti; i++) {
            clienti[i] = new Cliente(sala, i);
            clienti[i].start();
            Thread.sleep(100);
        }
    }
}
