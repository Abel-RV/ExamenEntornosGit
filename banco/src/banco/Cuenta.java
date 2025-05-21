package banco;

import java.io.Serializable;

/**
 *
 * @author Abel
 */
public class Cuenta implements Serializable{

    public CuentaData data = new CuentaData();

	public Cuenta(String iban, String titular, String dni) {
        this.data.iban = iban;
        this.data.titular = titular;
        this.data.saldo = 0;
        this.data.dni = dni;
    }

    public Cuenta() {
        this.data.iban = "";
        this.data.titular = "";
        this.data.saldo = 0;
        this.data.dni = "";
    }

    public void ingresar(double cantidad) {
        this.data.saldo += cantidad;
    }

    public void retirar(double cantidad) {
        this.data.saldo -= cantidad;
    }

    @Override
    public String toString() {       
        return String.format("%-24s %-30s %9s %10.2f", this.data.iban, this.data.titular, this.data.dni, this.data.saldo);
    }

}
