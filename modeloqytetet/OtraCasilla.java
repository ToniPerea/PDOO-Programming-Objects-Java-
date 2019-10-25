package modeloqytetet;


/**
 *
 * @author antonioperea
 */
public class OtraCasilla extends Casilla{
    private TipoCasilla tipo;
    OtraCasilla (int numCasilla, int coste, TipoCasilla tipo){
        super(numCasilla, coste);
        this.tipo = tipo;
    }

    @Override
    protected TipoCasilla getTipo() {
        return tipo;
    }
    
    @Override
    protected TituloPropiedad getTitulo(){
        return null;
    }
    
    @Override
    protected boolean soyEdificable(){
        return false;
    }
    
    @Override
    public boolean tengoPropietario(){
        return false;
    }
    
    @Override
    public String toString(){
        return super.toString() + "\n - Tipo: " + tipo;
    }
}
