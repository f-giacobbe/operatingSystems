package Simulazioni.nov24;

public class Addetto extends Thread {
    private BancoCheckIn banco;

    public Addetto(BancoCheckIn banco) {
        this.banco = banco;
        this.setDaemon(true);   //l'addetto è un thread demone
    }

    @Override
    public void run() {
        while (true) {
            try {
                banco.pesaERegistra();
                banco.prossimoPasseggero();
            } catch (InterruptedException _) {

            }
        }
    }
}
