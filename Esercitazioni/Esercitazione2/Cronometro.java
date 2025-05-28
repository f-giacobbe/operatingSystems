package Esercitazioni.Esercitazione2;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

//per esempio di interruzione thread, TimeUnit, Thread daemon
public class Cronometro extends Thread {
    @Override
    public void run() {
        int sec = 1;
        while (!isInterrupted()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                break;
            }

            System.out.println(sec);
            sec++;
        }
    }

    public Runnable asRunnable() {
        return this;
    }




    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Cronometro c = new Cronometro();

        System.out.println("Press enter to start stopwatch");
        in.nextLine();
        c.start();

        System.out.println("Press enter to stop");
        in.nextLine();
        c.interrupt();





        /*settando il thread come daemon, non serve l'interrupt perché per definizione un thread daemon
        termina automaticamente quando tutti i thread non-demon terminano. In questo caso quando il thread
        main termina sono terminati tutti i thread non-daemon, quindi c1, che è un thread daemon, termina
         */
        Cronometro c1 = new Cronometro();

        System.out.println("Press enter to start stopwatch");
        in.nextLine();
        c1.setDaemon(true);//
        c1.start();

        System.out.println("Press enter to stop");
        in.nextLine();
        //no interrupt
    }
}
