package edu.escuelaing.arep.htttpServer;

import java.lang.reflect.Method;

public class Handler {
    Method metodo;
    public Handler(Method metodo){
        this.metodo = metodo;
    }

    public String procesar() {
        try {
            return metodo.invoke(null, null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public  String procesar(Object[] arg) {
        try {
            return metodo.invoke(metodo, arg).toString(); //Estamos invocando lso metodos o metodo del objeto metodo
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
