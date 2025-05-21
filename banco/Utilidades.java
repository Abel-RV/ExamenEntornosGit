package banco;

import java.util.Scanner;

/**
 *
 * @author Abel
 */


public class Utilidades {  
    // valida DNI/NIE retorna true/false según la cadena pasada sea válida o no
    public static boolean validarDNI(String dniNie) {
        // Eliminar espacios en blanco y convertir a mayúsculas por si acaso
        dniNie = dniNie.trim().toUpperCase();

        if (dniNie.matches("^[0-9]{8}[A-HJ-NP-TV-Z]$")) {
            // Es un NIF (español)
            int numero = Integer.parseInt(dniNie.substring(0, 8));
            char letraCalculada = calcularLetraNIF(numero);
            char letraDNI = dniNie.charAt(8);

            return letraCalculada == letraDNI;
        } else if (dniNie.matches("^[XYZ][0-9]{7}[A-HJ-NP-TV-Z]$")) {
            // Es un NIE (extranjero)
            char letraInicial = dniNie.charAt(0);
            int numero = Integer.parseInt(dniNie.substring(1, 8));
            char letraCalculada = calcularLetraNIE(letraInicial, numero);
            char letraNIE = dniNie.charAt(8);

            return letraCalculada == letraNIE;
        }
        return false;
    }   
    // informa si la cadena representa un email válido
    public static boolean validarMail(String mail){
        return mail.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");        
    }
    // calcula la letra de un NIF español que corresponde al número pasado como parámetro
    public static char calcularLetraNIF(int numero) {
        String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
        int resto = numero % 23;
        return letras.charAt(resto);
    }
    // calcula la letra de un NIE a partir de la letra inicial y el número de 7 dígitos
    private static char calcularLetraNIE(char letraInicial, int numero) {
        String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
        int valorInicial = 0;
        switch (letraInicial) {
            case 'X' -> valorInicial = 0;
            case 'Y' -> valorInicial = 1;
            case 'Z' -> valorInicial = 2;
        }
        int resto = (valorInicial * 10000000 + numero) % 23;
        return letras.charAt(resto);
    }    
    
    public static char leerRespuesta() {
        Scanner scanner = new Scanner(System.in);      
        char respuesta = scanner.next().charAt(0);
        return Character.toLowerCase(respuesta);
    }
}
