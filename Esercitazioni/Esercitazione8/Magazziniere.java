package Esercitazioni.Esercitazione8;

public class Magazziniere extends Thread {
    private AziendaAgricola aziendaAgricola;

    public Magazziniere(AziendaAgricola aziendaAgricola) {
        this.aziendaAgricola = aziendaAgricola;
    }

    @Override
    public void run() {
        while (true) {
            try {
                aziendaAgricola.carica();
                System.out.printf("Il magazziniere ha caricato %d sacchi nel magazzino%n", aziendaAgricola.getSacchettiIniziali());
            } catch (InterruptedException _) {}
        }
    }
}
