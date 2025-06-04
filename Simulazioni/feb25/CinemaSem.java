package Simulazioni.feb25;

import java.util.concurrent.Semaphore;

public class CinemaSem extends Cinema {
    private Semaphore cassa = new Semaphore(1, true);   //semaforo per gestire la coda alla cassa in ordine FIFO
    private Semaphore clientiInCodaCassa = new Semaphore(0);
    private int bigliettiVenduti = 0;
    private Semaphore film = new Semaphore(0);     //semaforo che permette ai clienti di attendere la proiezione del film
    private Semaphore uscita = new Semaphore(0);    //semaforo che permette all'addetto di chiudere il cinema dopo che tutti i clienti sono usciti

    public CinemaSem(int numClienti) {
        super(numClienti);
    }

    @Override
    public void acquistaBiglietto() throws InterruptedException {
        clientiInCodaCassa.release();    //segnalo all'addetto la presenza di un addetto in cassa
        cassa.acquire();    //aspetto il mio turno in cassa
        System.out.println("Un cliente è in cassa per acquistare il biglietto");
    }

    @Override
    public boolean consegnaBiglietto() throws InterruptedException {
        clientiInCodaCassa.acquire();    //attendo un cliente
        System.out.printf("L'addetto ha consegnato un biglietto con posto %d%n", assegnaPosto(MIN_POSTO, MAX_POSTO));
        bigliettiVenduti++;

        if (bigliettiVenduti < numClienti) {    //se ancora ci sono clienti
            System.out.println("Il prossimo!");
            cassa.release();
            return false;
        } else {    //ho finito i clienti
            System.out.println("Ho finito i clienti. Può iniziare il film!");
            film.release(numClienti);   //segnalo ai clienti l'inizio del film
            return true;
        }
    }

    @Override
    public void vediFilm() throws InterruptedException {
        film.acquire();     //attendo l'inizio del film
        visioneFilm();
        System.out.println("Ho finito di vedere il film. Me ne vado.");
        uscita.release();   //ho finito di vedere il film e lascio il cinema
    }

    @Override
    public void chiudiCinema() throws InterruptedException {
        uscita.acquire(numClienti);     //aspetto che tutti i clienti escano dal cinema
        System.out.println("Tutti i clienti sono usciti. Chiudo il cinema!");
    }




    public static void main(String[] args) {
        int numClienti = 20;
        CinemaSem cinema = new CinemaSem(numClienti);
        cinema.test();
    }
}
