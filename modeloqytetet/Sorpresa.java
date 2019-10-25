/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;

/**
 *
 * @author antonioperea
 */
public class Sorpresa {
    private String texto;
    private TipoSorpresa tipo;
    private int valor;
    
    public Sorpresa(String texto, int valor, TipoSorpresa tipo){
        this.texto = texto;
        this.tipo = tipo;
        this.valor = valor;
    }
    
    public String getSorpresa(){
        return this.texto;
    }
    
    public TipoSorpresa getTipo(){
        return tipo;
    }
    
    public int getValor(){
        return valor;
    }
    

    @Override
    public String toString() {
        return "Sorpresa{" + "texto=" + this.texto + ", valor=" + Integer.toString(valor) + ", tipo=" + tipo + "}";
     } 
}
