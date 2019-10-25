/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;


import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author antonioperea
 */
public class Dado {
    private int valor;
    
    private static final Dado instance = new Dado();

    private Dado() {
    }
    
    public static Dado getInstance(){
        return instance;
    }

    public int getValor() {
        return valor;
    }
    
    int tirar(){
        int numero = ThreadLocalRandom.current().nextInt(1,6+1 );
        valor = numero;
        return valor;
    }
}

