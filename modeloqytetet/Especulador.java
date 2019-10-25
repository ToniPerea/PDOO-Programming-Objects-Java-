package modeloqytetet;

/**
 *
 * @author antonioperea
 */
public class Especulador extends Jugador {
    private int fianza;
    private static int FACTORESPECULADOR = 2;
    
    protected Especulador(Jugador a, int fi){
        super(a);
        fianza = fi;
        
    }
    
    // 1 . La clase hija depende del diseño de la clase padre
    //      i dentro de la padre hay cosas {funciones, atribuos} está privados. El hijo no puede acceder
    //      Solucion A: usar métodos que si sean accesibles.
    //      Solucion B: Cambiar la visibilidad de los atributos y funciones.
    
    public void pagarImpuesto(){
        this.modificarSaldo(this.getCasillaActual().getCoste()/2);
    }
    
    protected int getFactorEspeculador(){
        return FACTORESPECULADOR;
    }
    
    public Especulador convertirme(int fianza){
        return this;
    }
    
    public boolean deboIrACarcel(){
        boolean debo = super.deboIrACarcel() && this.pagarFianza() == false;
        return debo;
    }
    
    private boolean pagarFianza(){
        boolean puedoPagar = this.getSaldo() - fianza >= 0;
        if(puedoPagar){
            this.modificarSaldo(-fianza);
        }
        return puedoPagar;
    }
    
    protected boolean puedoEdificarCasa(TituloPropiedad titulo){
       boolean puedoEdificarCasa = this.getSaldo()-titulo.getPrecioEdificar()>=0 && titulo.getNumCasas()<8; 
       return puedoEdificarCasa;
    }
    
    protected boolean puedoEdificarHotel(TituloPropiedad titulo){
        boolean puedoEdificarHotel = this.getSaldo()-titulo.getPrecioEdificar()>=0 
                                    && titulo.getNumHoteles()<8 
                                    && titulo.getNumCasas()>=4;
        return puedoEdificarHotel;
        
    }

    @Override
    public String toString() {
        return  super.toString() + "Especulador{" + "fianza=" + fianza + '}';
    }
}