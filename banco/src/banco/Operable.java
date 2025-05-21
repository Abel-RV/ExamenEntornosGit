package banco;

public interface Operable {

	boolean ingresar(String codigo, double importe);

	boolean retirar(String codigo, double importe);

}