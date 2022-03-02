package edu.escuelaing.arep.htttpServer;

import java.lang.reflect.Method;

/**
 * Esta clase define las clases necesarias para poder llevar a cabo los metodos reflect de java, en otras palabras poder trabajar con varios metodos de dsitintas clases.
 * @author: Deivid Medina
 * @version: 02/03/2022
 */

public class Handler {
    Method metodo;
    public Handler(Method metodo){
        this.metodo = metodo;
    }

    /**
     * Clase que procesa los metodos de una clase, en caso de que lleguen valores vacios.
     * @return nulo - ya que no se le esta dando ningun parametro.
     */
    public String procesar() {
        try {
            return metodo.invoke(null, null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * Clase que me permite obtener los métodos que tiene un objeto.
     * @param arg - Recibe un Object[] como argumento
     * @return String - retorna los métodos de una clase correspondiente oun objeto.
     */
    public  String procesar(Object[] arg) {
        try {
            return metodo.invoke(metodo, arg).toString(); //Estamos invocando lso metodos o metodo del objeto metodo
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
