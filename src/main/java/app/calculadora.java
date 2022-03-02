package app;
import edu.escuelaing.arep.htttpServer.Web;

@Web("Services")
public class calculadora {

    @Web("resultadoSuma")
    public static String resultadoSumallamar(String valorUno, int valorDos){
        return"<!DOCTYPE html>\n"
                +"<html>\n"
                +"<head>\n"
                +"  <meta charset=\"utf-8\" />\n"
                +"  <title>Proyecto AREM</title>  \n"
                +"</head>\n"
                +"<body style=\"text-align: center\">   \n"
                + "<h1>Resultado de la Suma de: "+valorUno+" + "+valorDos+" es igual a:   </h1>"
                +"</body>\n"
                +"</html>\n"
                +"";
    }

    @Web("calculadora")
    public static String ejemploCalculadora(){
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
                + "  <input id='calculadoraIdnumeroDos' type='number' name='segundonumero' class='form-control' placeholder='Ingrese el segundo número a sumar' >"
                + "<br>"
                + "  <input id='calculadora_button_id' type='submit' value='Realizar la suma' class='btns'>"
                +"</form>"
                +"</body>\n"
                +"</html>\n"
                +"";
    }
}
