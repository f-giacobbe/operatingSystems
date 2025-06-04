package Simulazioni.feb25;

public class Addetto extends Thread {
    private Cinema cinema;

    public Addetto(Cinema cinema) {
        this.cinema = cinema;
    }

    @Override
    public void run() {
        try {
            boolean stopVendite = false;
            while (!stopVendite) {
                stopVendite = cinema.consegnaBiglietto();
            }
            //Quando consegnaBiglietto() restituisce true vuol dire che ho venduto l'ultimo biglietto e posso uscire dal while
            cinema.chiudiCinema();
        } catch (InterruptedException _) {

        }
    }
}
