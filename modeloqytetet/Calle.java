package modeloqytetet;

/**
 *
 * @author antonioperea
 */
public class Calle extends Casilla {
    private TituloPropiedad titulo;
    
    Calle (int numCasilla, TituloPropiedad tp){
        super(numCasilla, tp.getPrecioCompra());
        titulo = tp;
    }
    
    public void asignarPropietario(Jugador jugador){
        titulo.setPropietario(jugador);
    }
    
    @Override
    protected TipoCasilla getTipo(){
        return TipoCasilla.CALLE;
    }

    @Override
    protected TituloPropiedad getTitulo(){
        return titulo;
    }

    @Override
    public boolean tengoPropietario(){
        return titulo.tengoPropietario();
    }
    
    public int pagarAlquiler(){
        int costeAlquiler = titulo.pagarAlquiler();
        return costeAlquiler;
    }
    
    private void setTitulo(TituloPropiedad titulo){
        this.titulo = titulo;
    }

    @Override
    protected boolean soyEdificable(){
        return true;
    }
    
    @Override
    public String toString(){
        return super.toString() + "\n - Tipo: CALLE\n - Titulo:\n" + titulo;
    }
    
}
