package modeloqytetet;
import java.util.ArrayList;

/**
 *
 * @author antonioperea
 */
public class Tablero {
    private ArrayList<Casilla> casillas = new ArrayList<>();
    private Casilla carcel;
    
    public Tablero (){
        inicializar();
    }
    
    ArrayList getCasillas(){
        return casillas;
    }
    
    public Casilla getCarcel () {return carcel;}
    
    private void inicializar(){
        TituloPropiedad titulo;
        this.casillas = new ArrayList();
        this.casillas.add(new OtraCasilla(0, 1000, TipoCasilla.SALIDA));
        titulo = new TituloPropiedad ("Callejon Tenebroso", 200, 80, 0.2f, 100, 200);
        
        this.casillas.add(new Calle (1, titulo));
        
        titulo = new TituloPropiedad ("Calle del Alcaide", 500, 200, 0.1f, 250, 450);
        
        this.casillas.add(new Calle (2, titulo));
        
        this.casillas.add(new OtraCasilla (3, 0, TipoCasilla.SORPRESA));
        
        titulo = new TituloPropiedad ("Plaza Mística", 300, 100, 0.2f, 150, 250);
        
        this.casillas.add(new Calle (4, titulo));
        
        this.casillas.add(new OtraCasilla (5, 0, TipoCasilla.CARCEL));
        
        titulo = new TituloPropiedad ("Calle Sol", 700, 250, 0.1f, 350, 600);
        
        this.casillas.add(new Calle (6, titulo));
        
        this.casillas.add(new OtraCasilla (7, 0, TipoCasilla.SORPRESA));
        
        titulo = new TituloPropiedad ("Calle Finca Menor", 1000, 500, 0.2f, 400, 900);
        
        this.casillas.add(new Calle (8, titulo));
        
        titulo = new TituloPropiedad ("Calle Extravagancia", 1500, 800, 0.25f, 700, 1300);
        
        this.casillas.add(new Calle (9, titulo));
        
        this.casillas.add(new OtraCasilla (10, 0, TipoCasilla.PARKING));
        
        titulo = new TituloPropiedad ("Calle Mercaderes", 1800, 900, 0.2f, 900, 1600);
        
        this.casillas.add(new Calle (11, titulo));
        
        titulo = new TituloPropiedad ("Calle Inocencia", 2200, 1000, 0.1f, 1200, 2000);
        
        this.casillas.add(new Calle (12, titulo));
        
        this.casillas.add(new OtraCasilla (13, 0, TipoCasilla.SORPRESA));
        
        titulo = new TituloPropiedad("Calle Certeza", 2600, 1200, 0.15f, 1300, 2500);
        
        this.casillas.add(new Calle (14, titulo));
        
        this.casillas.add(new OtraCasilla (15, 0, TipoCasilla.JUEZ));
        
        titulo = new TituloPropiedad ("Calle Justicia", 3000, 1500, 0.1f, 1500, 3000);
        
        this.casillas.add(new Calle (16, titulo));
        
        titulo = new TituloPropiedad ("Calle Finca Mayor", 4000, 2000, 0.2f, 2000, 4000);
        
        this.casillas.add(new Calle (17, titulo));
        
        this.casillas.add(new OtraCasilla(18, 250, TipoCasilla.IMPUESTO));
        
        titulo = new TituloPropiedad ("Villa Maravilla", 5000, 2400, 0.25f, 2400, 4500);
        
        this.casillas.add(new Calle (19, titulo));
        
        this.carcel = this.casillas.get(5);
    }
    
    boolean esCasillaCarcel(int numeroCasilla){
        if(numeroCasilla == this.carcel.getNumeroCasilla())
            return true;
        else 
            return false;
    }
    
    Casilla obtenerCasillaFinal(Casilla casilla, int desplazamiento){
       int obtener = casilla.getNumeroCasilla() + desplazamiento;
       
       if(obtener > 19)
           obtener = obtener % 20;
       
       return casillas.get(obtener);
    }
    
     Casilla ObtenerCasillaNumero(int casilla){
        if(casilla >= 0 && casilla <= 19)
            return casillas.get(casilla);
        else
            return null;
    }
    
    @Override
    public String toString() {
        return "Casillas = " + casillas + ", carcel = " + carcel;
    }
    
}
