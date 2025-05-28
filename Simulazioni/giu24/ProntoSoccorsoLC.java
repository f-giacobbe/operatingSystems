package Simulazioni.giu24;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProntoSoccorsoLC extends ProntoSoccorso {
    private Lock l = new ReentrantLock();
    private Condition medicoPuoVisitare = l.newCondition();
    private Condition pazientePuoEntrare = l.newCondition();
    private Condition pazientePuoUscire = l.newCondition();
    private LinkedList<Paziente> codaRossi = new LinkedList<>();
    private LinkedList<Paziente> codaGialli = new LinkedList<>();
    private LinkedList<Paziente> codaVerdi = new LinkedList<>();
    private LinkedList[] code = {codaRossi, codaGialli, codaVerdi};
    private boolean pazienteEntrato = false;
    private Paziente pazienteCorrente;
    private boolean visitaTerminata = false;

    @Override
    public void accediPaziente(int codice) throws InterruptedException {
        boolean gialloPrioritario = false;
        int turniAttesa = 0;
        l.lock();
        try {
            //mi aggiungo alla coda corrispondente al mio codice
            code[codice].add((Paziente) Thread.currentThread());
            System.out.printf("Un paziente con codice %d si è aggiunto alla coda%n", codice);

            while (!possoEntrare(codice)) {
                pazientePuoEntrare.await();
                turniAttesa++;

                if (codice == 1 && turniAttesa == 5) {      //se ho codice giallo ma sto aspettando da più di 5 turni mi metto in cima alla coda dei codici rossi in modo da essere visitato subito
                    codice = 0;
                    gialloPrioritario = true;
                    codaGialli.remove((Paziente) Thread.currentThread());
                    codaRossi.addFirst((Paziente) Thread.currentThread());
                }
            }

            //posso entrare
            pazienteCorrente = (Paziente) Thread.currentThread();
            pazienteEntrato = true;
            code[codice].remove();
            System.out.printf("Un paziente con codice %d è entrato dal medico%n", gialloPrioritario ? 1 : codice);
            medicoPuoVisitare.signal();
        } finally {
            l.unlock();
        }
    }

    private boolean possoEntrare(int codice) {
        if (pazienteEntrato && !visitaTerminata) {
            return false;
        }

        if (codice == 0) {
            return Thread.currentThread().equals(codaRossi.getFirst());
        } else if (codice == 1) {
            return codaRossi.isEmpty() && Thread.currentThread().equals(codaGialli.getFirst());
        } else {
            return codaRossi.isEmpty() && codaGialli.isEmpty() && Thread.currentThread().equals(codaVerdi.getFirst());
        }
    }

    @Override
    public void iniziaVisita() throws InterruptedException {
        l.lock();
        try {
            while (!possoIniziareVisita()) {
                medicoPuoVisitare.await();
            }

            //il medico entra dal paziente
            System.out.println("Può iniziare la visita!");
            visita(pazienteCorrente.getCodice());
        } finally {
            l.unlock();
        }
    }

    private boolean possoIniziareVisita() {
        return pazienteEntrato;
    }

    @Override
    public void terminaVisita() throws InterruptedException {
        l.lock();
        try {
            System.out.println("La visita è terminata!");
            visitaTerminata = true;
            pazienteEntrato = false;
            pazientePuoUscire.signal();
        } finally {
            l.unlock();
        }
    }

    @Override
    public void esciPaziente() throws InterruptedException {
        l.lock();
        try {
            while (!possoUscire()) {
                pazientePuoUscire.await();
            }

            //il medico ha finito di visitarmi e posso uscire, notificando il prossimo di entrare
            pazienteCorrente = null;
            visitaTerminata = false;
            pazientePuoEntrare.signalAll();
        } finally {
            l.unlock();
        }
    }

    private boolean possoUscire() {
        return visitaTerminata && Thread.currentThread().equals(pazienteCorrente);
    }












    public static void main(String[] args) {
        ProntoSoccorsoLC ps = new ProntoSoccorsoLC();
        ps.test(30);
    }
}
