package Simulazioni.feb25;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CinemaLC extends Cinema {
    private Lock l = new ReentrantLock();
    private Condition attesaCliente = l.newCondition();     //condition sulla quale l'addetto si mette in attesa se non ci sono clienti in coda alla cassa
    private Condition attesaCassa = l.newCondition();   //condition sulla quale il cliente si mette in attesa di pagare
    private Condition attesaFilm = l.newCondition();    //condition sulla quale il cliente si mette in attesa di vedere il film
    private Condition attesaChiusura = l.newCondition();    //condition sulla quale l'addetto si mette in attesa per chiudere il cinema
    private LinkedList<Cliente> codaCassa = new LinkedList<>();     //per gestire la coda FIFO in cassa
    private int bigliettiVenduti = 0;
    private int clientiInCodaCassa = 0;
    private int clientiUsciti = 0;
    private boolean filmVisibile = false;
    private boolean clienteInCassa = false;

    public CinemaLC(int numClienti) {
        super(numClienti);
    }

    @Override
    public void acquistaBiglietto() throws InterruptedException {
        l.lock();
        try {
            codaCassa.add((Cliente) Thread.currentThread());    //mi aggiungo alla coda della cassa
            clientiInCodaCassa++;
            attesaCliente.signal();

            while (!possoPagare()) {
                attesaCassa.await();
            }

            codaCassa.remove();
            clientiInCodaCassa--;
            clienteInCassa = true;
            System.out.println("Un cliente è in cassa per acquistare il biglietto!");
        } finally {
            l.unlock();
        }
    }

    private boolean possoPagare() {
        return !clienteInCassa && Thread.currentThread().equals(codaCassa.getFirst());  //posso pagare se non c'è nessuno in cassa e se sono il primo della coda
    }

    @Override
    public boolean consegnaBiglietto() throws InterruptedException {
        l.lock();
        try {
            while (!possoConsegnareBiglietto()) {
                attesaCliente.await();
            }

            System.out.printf("L'addetto ha consegnato un biglietto con posto %d%n", assegnaPosto(MIN_POSTO, MAX_POSTO));
            bigliettiVenduti++;
            clienteInCassa = false;

            if (bigliettiVenduti < numClienti) {    //se ancora ci sono clienti
                System.out.println("Il prossimo!");
                attesaCassa.signalAll();
                return false;
            } else {    //ho finito i clienti
                System.out.println("Ho finito i clienti. Può iniziare il film!");
                filmVisibile = true;
                attesaFilm.signalAll();   //segnalo ai clienti l'inizio del film
                return true;
            }
        } finally {
            l.unlock();
        }
    }

    private boolean possoConsegnareBiglietto() {
        return clientiInCodaCassa > 0;
    }

    @Override
    public void vediFilm() throws InterruptedException {
        l.lock();
        try {
            while (!possoVedereFilm()) {
                attesaFilm.await();
            }

            visioneFilm();
            System.out.println("Ho finito di vedere il film. Me ne vado.");
            clientiUsciti++;
            attesaChiusura.signal();
        } finally {
            l.unlock();
        }
    }

    private boolean possoVedereFilm() {
        return filmVisibile;
    }

    @Override
    public void chiudiCinema() throws InterruptedException {
        l.lock();
        try {
            while (!possoChiudereCinema()) {
                attesaChiusura.await();
            }

            System.out.println("Tutti i clienti sono usciti. Chiudo il cinema!");
        } finally {
            l.unlock();
        }
    }

    private boolean possoChiudereCinema() {
        return clientiUsciti == numClienti;
    }





    public static void main(String[] args) {
        int numClienti = 20;
        CinemaLC cinema = new CinemaLC(numClienti);
        cinema.test();
    }
}
