package Simulazioni.nov24;

public class Passeggero extends Thread {
    private BancoCheckIn banco;

    public Passeggero(BancoCheckIn banco) {
        this.banco = banco;
    }

    @Override
    public void run() {
        try {
            int numBagagli = BancoCheckIn.scegliNBagagli();
            System.out.printf("Sto preparando %d bagagli%n", numBagagli);
            BancoCheckIn.preparati(numBagagli);
            banco.deponeBagagli(numBagagli);
            banco.riceviCartaImbarco();
        } catch (InterruptedException _) {

        }
    }
}
