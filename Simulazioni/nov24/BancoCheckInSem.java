package Simulazioni.nov24;

import java.util.concurrent.Semaphore;

public class BancoCheckInSem extends BancoCheckIn {
    private Semaphore turnoBanco = new Semaphore(1, true);    //semaforo FIFO per il turno al banco del check-in
    private Semaphore possoPesare = new Semaphore(0);   //semaforo per far capire all'addetto quando il passeggero ha deposto i bagagli sul nastro
    private Semaphore possoAndarmene = new Semaphore(0);    //semaforo per far aspettare la registrazione dei bagagli al passeggero
    private Semaphore bancoLiberato = new Semaphore(0);     //semaforo per segnalare all'addetto che il passeggero ha lasciato il banco del check-in

    private int bagagliDepositati = 0;      //per indicare all'addetto quanti bagagli sono stati depositati sul nastro

    @Override
    public void deponeBagagli(int N) throws InterruptedException {
        turnoBanco.acquire();   //aspetto il mio turno al banco
        bagagliDepositati = N;
        System.out.printf("È il mio turno e ho depositato i miei %d bagagli sul nastro%n", N);
        possoPesare.release();  //segnalo all'addetto di aver depositato i bagagli e che può iniziare a pesarli e a registrarli
    }

    @Override
    public void pesaERegistra() throws InterruptedException {
        possoPesare.acquire();  //aspetto che il passeggero deponga i bagagli sul nastro
        System.out.printf("OK. Peso e registro i tuoi %d bagagli%n", bagagliDepositati);
        pesaERegistra(bagagliDepositati);
        System.out.println("Finito di registrare! Ecco la carta di imbarco. Buon viaggio!");
        possoAndarmene.release();   //segnalo al passeggero che può andarsene
    }

    @Override
    public void riceviCartaImbarco() throws InterruptedException {
        possoAndarmene.acquire();   //aspetto che l'addetto pesi e registri tutti i miei bagagli
        System.out.println("Perfetto grazie mille. Me ne vado che perdo l'aereo");
        bancoLiberato.release();    //segnalo all'addetto che me ne sono andato
    }

    @Override
    public void prossimoPasseggero() throws InterruptedException {
        bancoLiberato.acquire();
        System.out.println("Il prossimo!");
        bagagliDepositati = 0;
        turnoBanco.release();
    }






    public static void main(String[] args) {
        BancoCheckInSem b = new BancoCheckInSem();
        int numPasseggeri = 5;
        b.test(numPasseggeri);
    }
}
