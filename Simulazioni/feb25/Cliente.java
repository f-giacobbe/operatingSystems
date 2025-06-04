package Simulazioni.feb25;

public class Cliente extends Thread {
    private Cinema cinema;

    public Cliente(Cinema cinema) {
        this.cinema = cinema;
    }

    @Override
    public void run() {
        try {
            cinema.acquistaBiglietto();
            cinema.vediFilm();
        } catch (InterruptedException _) {

        }
    }
}
