package Esercitazioni.Esercitazione10;

import Esercitazioni.Esercitazione3.ContoCorrente;

public class ContoCorrenteSync extends ContoCorrente {
    public ContoCorrenteSync(int depositoIniziale) {
        super(depositoIniziale);
    }

    @Override
    public synchronized void deposita(int importo) {
        deposito += importo;
    }

    @Override
    public synchronized void preleva(int importo) {
        deposito -= importo;
    }
}
