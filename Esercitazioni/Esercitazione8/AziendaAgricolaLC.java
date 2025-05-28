package Esercitazioni.Esercitazione8;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AziendaAgricolaLC extends AziendaAgricola {
    private final Lock l = new ReentrantLock();
    private final Condition paga = l.newCondition();
    private final Condition preleva = l.newCondition();
    private final Condition carica = l.newCondition();
    private final LinkedList<Thread> codaCassa = new LinkedList<>();
    private final LinkedList<Thread> codaMagazzino = new LinkedList<>();

    public AziendaAgricolaLC(int sacchettiIniziali) {
        super(sacchettiIniziali);
    }

    @Override
    public void paga(int numSacchi) throws InterruptedException {
        l.lock();
        try {
            codaCassa.add(Thread.currentThread());
            while (!possoPagare()) {
                paga.await();
            }
            codaCassa.remove();
            incasso += numSacchi * PREZZO_SACCO;
            paga.signalAll();
        } finally {
            l.unlock();
        }
    }

    private boolean possoPagare() {
        return Thread.currentThread() == codaCassa.getFirst();
    }

    @Override
    public void ritira() throws InterruptedException {
        l.lock();
        try {
            codaMagazzino.add(Thread.currentThread());
            while (!possoRitirare()) {
                preleva.await();
            }
            codaMagazzino.remove();
            sacchetti--;
            System.out.printf("Sacchetti = %d%n", sacchetti);

            //se ritiro l'ultimo sacchetto, dò un segnale al magazziniere di caricare
            if (sacchetti == 0) {
                carica.signal();
            }

            preleva.signalAll();
        } finally {
            l.unlock();
        }
    }

    private boolean possoRitirare() {
        return Thread.currentThread() == codaMagazzino.getFirst() && sacchetti > 0;
    }

    @Override
    public void carica() throws InterruptedException {
        l.lock();
        try {
            while (!possoCaricare()) {
                carica.await();
            }
            TimeUnit.MINUTES.sleep(10);
            sacchetti = sacchettiIniziali;
            preleva.signalAll();
        } finally {
            l.unlock();
        }
    }

    private boolean possoCaricare() {
        return sacchetti == 0;
    }











    public static void main(String[] args) {
        AziendaAgricolaLC azienda = new AziendaAgricolaLC(200);
        int numClienti = 100;
        azienda.test(numClienti);
    }
}
