package banco;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class EjercicioBanco {
    static Scanner teclado = new Scanner(System.in);
    private static final String PATHNAME = "./archivos/";

    public static int mostrarMenu() {
        System.out.println("    GESTIÓN BANCO");
        System.out.println("==========================");
        System.out.println("1.Crear banco");
        System.out.println("2.Nueva cuenta");
        System.out.println("3.Ingreso en cuenta");
        System.out.println("4.Retirar de cuenta");
        System.out.println("5.Consultar datos de cuenta");
        System.out.println("6.Obtener informe de banco");
        System.out.println("7.Eliminar cuenta");
        System.out.println("8.Guardar datos en disco");
        System.out.println("9.Cargar datos de disco");
        System.out.println("10.Salir");
        int opcion = teclado.nextInt();
        teclado.nextLine(); //consumimos el salto de línea sino al leer el siguiente dato tenemos error.
        return opcion;
    }

    public static String leerCodigoCuenta(Banco banco) {
        System.out.println(banco.mostrarCuentas());
        String codigoCuenta = "";

        do {
            System.out.println("Introduce el código de cuenta:");
            try {
                codigoCuenta = teclado.nextLine();
            } catch (Exception e) {
                System.out.println("Error en lectura de dato ");
                if (e.getMessage() != null) {
                    System.out.println(e.getMessage());
                }
            }
        } while (!banco.existeCuenta(codigoCuenta)
                && // con x o X puede salir 
                (!"x".equals(codigoCuenta) && !"X".equals(codigoCuenta)));
        return codigoCuenta;
    }
    
     public static void guardarDatos(Banco banco, String archivo) {
        try {
            banco.guardarEstado(PATHNAME + archivo);
            System.out.println("Curso guardado en " + PATHNAME + archivo);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static Banco recuperarDatos(String archivo) {
        try {
            Banco bancoRecuperado = Banco.cargarEstado(PATHNAME + archivo);
            System.out.println("Curso recuperado de " + PATHNAME + archivo);
            return bancoRecuperado;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
	public static void main(String[] args) {
        int opcion;
        Banco miBanco = null;
        do {
            opcion = mostrarMenu();
            boolean datoCorrecto;
            switch (opcion) {
                case 0:
                    if (miBanco == null) {
                        System.out.println("BANCO NO CREADO");
                        break;
                    }
                    miBanco.mostrarDatos();
                    break;
                case 1: // creación del curso
                    char respuesta = ' ';
                    if (miBanco != null) {
                        System.out.println("Ya hay datos creados del banco " + miBanco.getNombre() + ". Si continúa se perderán. ¿Desea continuar?");
                        do {
                            System.out.println("Introduzca S o N:");
                            respuesta = Utilidades.leerRespuesta();
                        } while (respuesta != 's' && respuesta != 'n');
                    } else {
                        respuesta = 's';
                    }
                    if (respuesta == 's') {
                        String nombreBanco;
                        do {
                            System.out.println("Introduce nombre del Banco :");
                            nombreBanco = teclado.nextLine();
                        } while (nombreBanco.isEmpty());
                        miBanco = new Banco(nombreBanco);
                        do {
                            System.out.println("Quieres cargar datos de prueba S o N:");
                            respuesta = Utilidades.leerRespuesta();
                        } while (respuesta != 's' && respuesta != 'n');
                        if (respuesta == 's') {
                            miBanco.cargarDatos();
                        }
                    }
                    break;
                case 2: // alta de nueva cuenta

                    if (miBanco == null) {
                        System.out.println("BANCO NO CREADO");
                        break;
                    }
                    String codigo,
                     titular,
                     dni;
                    datoCorrecto = false;
                    do {
                        System.out.println("Introduce código de cuenta:");
                        codigo = teclado.nextLine();
                        if (miBanco.existeCuenta(codigo)) {
                            System.out.println("Ya existe una cuenta con ese código.");
                            datoCorrecto = false;
                        } else {
                            datoCorrecto = true;
                        }
                    } while (!datoCorrecto);
                    do {
                        System.out.println("Introduce nombre del titular:");
                        titular = teclado.nextLine();
                    } while (titular.isEmpty());

                    do {
                        System.out.println("Introduce el DNI/NIE del titular:");
                        dni = teclado.nextLine();
                        datoCorrecto = Utilidades.validarDNI(dni);
                        if (!datoCorrecto) {
                            System.out.println("El DNI introducido no es válido.");
                        }

                    } while (!datoCorrecto);
                    if (miBanco.agregarCuenta(codigo, titular, dni)) {
                        System.out.println("Cuenta agregada con éxito. Hay " + miBanco.getNumeroCuentas() + " cuentas registradas.");
                    } else {
                        System.out.println("No se ha podido agregar la cuenta.");
                    }
                    break;
                case 3: // ingreso en cuenta
                    if (miBanco == null) {
                        System.out.println("BANCO NO CREADO");
                        break;
                    }
                    if (miBanco.getNumeroCuentas() == 0) { // si no hay cuentas nada se puede hacer
                        System.out.println("Todavía no hay datos de cuentas.");
                        break;
                    }

                    codigo = leerCodigoCuenta(miBanco);
                    if (!miBanco.existeCuenta(codigo)) {
                        break;
                    }
                    double cantidad = 0;
                    do {
                        System.out.println("Introduce cantidad a ingresar: (solo numeros positivos o -1 para abandonar).");

                        try {
                            cantidad = teclado.nextDouble();
                        } catch (InputMismatchException e) {
                            System.out.println("Error en lectura de numero ");
                            if (e.getMessage() != null) {
                                System.out.println(e.getMessage());
                            }
                        }
                        teclado.nextLine();
                    } while ((cantidad <= 0) && (cantidad != -1));
                    if (cantidad != -1) {
                        if (miBanco.ingresar(codigo, cantidad)) {
                            System.out.printf("Operación de ingreso de %.2f en cuenta %s Correcta.%n", cantidad, codigo);
                        } else {
                            System.out.println("Error al hacer ingreso.");
                        }
                    } else {
                        System.out.println("Se ha cancaleado la operación de ingreso.");

                    }
                    break;
                case 4:  // reintegro de cuenta
                    if (miBanco == null) {
                        System.out.println("BANCO NO CREADO");
                        break;
                    }
                    if (miBanco.getNumeroCuentas() == 0) { // si no hay cuentas nada se puede hacer
                        System.out.println("Todavía no hay datos de cuentas.");
                        break;
                    }
                    codigo = leerCodigoCuenta(miBanco);
                    if (!miBanco.existeCuenta(codigo)) {
                        break;
                    }
                    cantidad = 0;
                    do {
                        System.out.println("Introduce cantidad a retirar: (solo numeros positivos o -1 para abandonar).");
                        try {
                            cantidad = teclado.nextDouble();
                        } catch (InputMismatchException e) {
                            System.out.println("Error en lectura de numero ");
                            if (e.getMessage() != null) {
                                System.out.println(e.getMessage());
                            }
                        }
                        teclado.nextLine();
                    } while ((cantidad <= 0) && (cantidad != -1));
                    if (cantidad != -1) {
                        if (cantidad > miBanco.informaSaldo(codigo)) {
                            System.out.println("El saldo de la cuenta es inferior a la cantidad que desea retirar. ¿Continúa? (S/N)");
                            do {
                                System.out.println("Introduzca S o N:");
                                respuesta = Utilidades.leerRespuesta();
                            } while (respuesta != 's' && respuesta != 'n');
                        } else {
                            respuesta = 's';
                        }
                        if (miBanco.retirar(codigo, cantidad)) {
                            System.out.printf("Operación de reintegro de %.2f en cuenta %s Correcta.%n", cantidad, codigo);
                        } else {
                            System.out.println("Error al hacer reintegro.");
                        }
                    } else {
                        System.out.println("Se ha cancaleado la operación de reintegro.");
                    }
                    break;
                case 5: // consultar cuenta
                    if (miBanco == null) {
                        System.out.println("BANCO NO CREADO");
                        break;
                    }
                    if (miBanco.getNumeroCuentas() == 0) { // si no hay cuentas nada se puede hacer
                        System.out.println("Todavía no hay datos de cuentas.");
                        break;
                    }
                    codigo = leerCodigoCuenta(miBanco);
                    if (!miBanco.existeCuenta(codigo)) {
                        break;
                    }
                    System.out.println(miBanco.consultarCuenta(codigo));
                    break;
                case 6: // informe general
                    if (miBanco == null) {
                        System.out.println("BANCO NO CREADO");
                        break;
                    }
                    if (miBanco.getNumeroCuentas() == 0) { // si no hay cuentas nada se puede hacer
                        System.out.println("Todavía no hay datos de cuentas.");
                        break;
                    }
                    System.out.println(miBanco.informeCuentas());
                    break;
                case 7: // borrar cuenta
                    if (miBanco == null) {
                        System.out.println("BANCO NO CREADO");
                        break;
                    }
                    if (miBanco.getNumeroCuentas() == 0) { // si no hay cuentas nada se puede hacer
                        System.out.println("Todavía no hay datos de cuentas.");
                        break;
                    }
                    codigo = leerCodigoCuenta(miBanco);
                    if (!miBanco.existeCuenta(codigo)) {
                        break;
                    }
                    miBanco.eliminarCuenta(codigo);
                    break;              
                case 8:
                    if (miBanco == null) {
                        System.out.println("BANCO NO CREADO");
                        break;
                    }
                    guardarDatos(miBanco, "banco.dat");
                    break;
                case 9:
                    if (miBanco != null) {
                        System.out.println("Ya hay datos creados del banco " + miBanco.getNombre() + ". Si continúa se perderán. ¿Desea continuar?");
                        do {
                            System.out.println("Introduzca S o N:");
                            respuesta = Utilidades.leerRespuesta();
                        } while (respuesta != 's' && respuesta != 'n');
                    } else {
                        respuesta = 's';
                    }
                    if (respuesta == 's') {
                        miBanco = recuperarDatos("banco.dat");
                    }
            }
        } while (opcion != 10);
	}

}
