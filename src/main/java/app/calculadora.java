package app;
import edu.escuelaing.arep.htttpServer.Web;

@Web("Services")
public class calculadora {

    @Web("resultadoSuma")
    public static String resultadoSumallamar(String valorUno, String valorDos){
        int numeroUno = Integer.parseInt (valorUno);
        int numeroDos = Integer.parseInt (valorDos);
        String resultadoSumatoria = String.valueOf(numeroUno+numeroDos);
        return"<!DOCTYPE html>\n"
                +"<html>\n"
                +"<head>\n"
                +"  <meta charset=\"utf-8\" />\n"
                +"  <title>Proyecto AREM</title>  \n"
                +"</head>\n"
                +"<body style=\"text-align: center\">   \n"
                + "<h1>Resultado de la Suma de: "+valorUno+" + "+valorDos+" es igual a: "+resultadoSumatoria+"  </h1>"
                +"</body>\n"
                +"</html>\n"
                +"";
    }

    @Web("resultadoResta")
    public static String resultadoRestallamar(String valorUno, String valorDos){
        int numeroUno = Integer.parseInt (valorUno);
        int numeroDos = Integer.parseInt (valorDos);
        String resultadoSubstracion = String.valueOf(numeroUno+numeroDos);
        return"<!DOCTYPE html>\n"
                +"<html>\n"
                +"<head>\n"
                +"  <meta charset=\"utf-8\" />\n"
                +"  <title>Proyecto AREM</title>  \n"
                +"</head>\n"
                +"<body style=\"text-align: center\">   \n"
                + "<h1>Resultado de la Suma de: "+valorUno+" + "+valorDos+" es igual a: "+resultadoSubstracion+"  </h1>"
                +"</body>\n"
                +"</html>\n"
                +"";
    }

    @Web("calculadoraSumar")
    public static String ejemploCalculadoraSuma(){
        return"<!DOCTYPE html>\n"
                +"<html>\n"
                +"<head>\n"
                +"  <meta charset=\"utf-8\" />\n"
                +"  <title>Proyecto AREM</title>  \n"
                +"</head>\n"
                +"<body style=\"text-align: center\">   \n"
                + "<h1>Calculadora que suma dos números </h1>"
                +"<form action=\"/app/resultadoSuma\">	\n"
                + "  <input id='calculadoraIdnumeroUno' type='number' name='primernumero' class='form-control' placeholder='Ingrese el primer número a sumar' >"
                + "<br>"
                + "<br>"
                + "  <input id='calculadoraIdnumeroDos' type='number' name='segundonumero' class='form-control' placeholder='Ingrese el segundo número a sumar' >"
                + "<br>"
                + "<br>"
                + "  <input id='calculadora_button_id' type='submit' value='Realizar la suma' class='btns'>"
                +"</form>"
                +"</body>\n"
                +"</html>\n"
                +"";
    }

    @Web("calculadoraRestar")
    public static String ejemploCalculadoraRestar(){
        return"<!DOCTYPE html>\n"
                +"<html>\n"
                +"<head>\n"
                +"  <meta charset=\"utf-8\" />\n"
                +"  <title>Proyecto AREM</title>  \n"
                +"</head>\n"
                +"<body style=\"text-align: center\">   \n"
                + "<h1>Calculadora que resta dos números </h1>"
                +"<form action=\"/app/resultadoResta\">	\n"
                + "  <input id='calculadoraIdnumeroUno' type='number' name='primernumero' class='form-control' placeholder='Ingrese el primer número a restar.' >"
                + "<br>"
                + "<br>"
                + "  <input id='calculadoraIdnumeroDos' type='number' name='segundonumero' class='form-control' placeholder='Ingrese el segundo número a restar.' >"
                + "<br>"
                + "<br>"
                + "  <input id='calculadora_button_id' type='submit' value='Realizar la suma' class='btns'>"
                +"</form>"
                +"</body>\n"
                +"</html>\n"
                +"";
    }
}
