package Simulazioni.gen24;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BibliotecaLC extends Biblioteca {
    private Lock l = new ReentrantLock();

    private LinkedList<Utente> codaTesserati = new LinkedList<>();
    private LinkedList<Utente> codaEsterni = new LinkedList<>();
    private LinkedList[] code = {codaTesserati, codaEsterni};

    private Condition banco = l.newCondition();     //condition che permette all'utente di attendere il turno al banco dei prestiti
    private Condition utentePresente = l.newCondition();    //condition che permette al bibliotecario di attendere che un utente arrivi al banco
    private Condition confermaPrestito = l.newCondition();  //condition che permette all'utente di attendere la conferma del prestito per poter uscire
    private Condition utenteUscito = l.newCondition();  //condition che permette al bibliotecario di attendere l'uscita del cliente

    private boolean utentePuoVenire = true;    //true se l'utente può recarsi al banco
    private boolean utenteAlBanco = false;  //true se c'è attualmente un utente al banco
    private boolean prestitoConfermato = false;     //true se il bibliotecario ha confermato il prestito

    @Override
    public void richiediPrestito() throws InterruptedException {
        l.lock();
        try {
            Utente utente = (Utente) Thread.currentThread();
            int tipo = utente.getTipo();

            //scelgo il libro da prendere
            String libro = generaStringa();
            System.out.printf("Ho scelto il libro %s. Adesso mi accodo dal bibliotecario%n", libro);

            //mi aggiungo alla coda in base al mio tipo
            code[tipo].add(utente);

            while (!eIlMioTurno(tipo)) {
                banco.await();
            }

            //mi rimuovo dalla coda
            code[tipo].remove();

            //sono al banco
            utentePresente.signal();    //segnalo al bibliotecario che sono pronto per la registrazione del prestito
            System.out.printf("È finalmente il mio turno! Sono un utente %s%n", tipo == Utente.TESSERATO ? "TESSERATO" : "ESTERNO");
            utentePuoVenire = false;
            utenteAlBanco = true;
        } finally {
            l.unlock();
        }
    }

    private boolean eIlMioTurno(int tipo) {
        if (tipo == Utente.TESSERATO) {
            return utentePuoVenire && Thread.currentThread().equals(codaTesserati.getFirst());
        } else {    //utente esterno
            return utentePuoVenire && codaTesserati.isEmpty() && Thread.currentThread().equals(codaEsterni.getFirst());
        }
    }

    @Override
    public void registraPrestito() throws InterruptedException {
        l.lock();
        try {
            while (!utenteAlBanco) {
                utentePresente.await();
            }

            System.out.println("Sto registrando il prestito...");
            registrazione();
            System.out.println("Ho finito di registrare il prestito!");
            prestitoConfermato = true;
            confermaPrestito.signal();
        } finally {
            l.unlock();
        }
    }

    @Override
    public void esci() throws InterruptedException {
        l.lock();
        try {
            while (!prestitoConfermato) {
                confermaPrestito.await();
            }

            //posso uscire
            System.out.println("Ok ciao grazie. Me ne vado!");
            utenteAlBanco = false;
            utenteUscito.signal();  //segnalo al bibliotecario che sono uscito
        } finally {
            l.unlock();
        }
    }

    @Override
    public void prossimoUtente() throws InterruptedException {
        l.lock();
        try {
            while (utenteAlBanco) {
                utenteUscito.await();
            }

            System.out.println("Utente uscito. Avanti il prossimo!");
            prestitoConfermato = false;
            utentePuoVenire = true;
            banco.signalAll();
        } finally {
            l.unlock();
        }
    }







    public static void main(String[] args) {
        int numUtenti = 25;
        new BibliotecaLC().test(numUtenti);
    }
}
