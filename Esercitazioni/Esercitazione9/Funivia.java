package Esercitazioni.Esercitazione9;

public abstract class Funivia {
    protected final int TURISTA_A_PIEDI = 0;
    protected final int TURISTA_IN_BICI = 1;
    protected final int[] NUM_TURISTI = {6, 3};
    protected final int POSTI_FUNIVIA = 6;

    public abstract void pilotaStart() throws InterruptedException;

    public abstract void pilotaEnd() throws InterruptedException;

    public abstract void turistaSali(int tipo) throws InterruptedException;

    public abstract void turistaScendi(int tipo) throws InterruptedException;

    public void test(int numTuristiPiedi, int numTuristiBici) {
        for (int i = 0; i < numTuristiPiedi; i++) {
            new Turista(this, TURISTA_A_PIEDI).start();
        }
        for (int i = 0; i < numTuristiBici; i++) {
            new Turista(this, TURISTA_IN_BICI).start();
        }
        Thread p = new Thread(new Pilota(this));
        p.setDaemon(true);
        p.start();
    }
}
