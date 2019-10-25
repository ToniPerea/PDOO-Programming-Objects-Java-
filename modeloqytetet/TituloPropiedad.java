package modeloqytetet;

/**
 *
 * @author antonioperea
 */
public class TituloPropiedad {

    private String nombre;
    private boolean hipotecada;
    private int precioCompra;
    private int alquilerBase;
    private float factorRevalorizacion;
    private int hipotecaBase;
    private int precioEdificar;
    private int numHoteles;
    private int numCasas;
    private Jugador propietario;

    public TituloPropiedad(String name, int precio, int alquiler, float factorRevalorizacion, int hipotecaBase, int precioEdificar) {
        this.nombre = name;
        this.precioCompra = precio;
        this.alquilerBase = alquiler;
        this.factorRevalorizacion = factorRevalorizacion;
        this.hipotecaBase = hipotecaBase;
        this.precioEdificar = precioEdificar;
        hipotecada = false;
        numHoteles = 0;
        numCasas = 0;
        propietario = null;
    }

    public String getnombre() {
        return nombre;
    }

    public int getPrecioCompra() {
        return precioCompra;
    }

    public int getAlquilerBase() {
        return alquilerBase;
    }

    public float getFactorRevalorizacion() {
        return factorRevalorizacion;
    }

    public int getPrecioEdificar() {
        return precioEdificar;
    }
    
    Jugador getPropietario() {
        return propietario;
    }

    void setPropietario(Jugador propietario) {
        this.propietario = propietario;
    }

    public int getNumHoteles() {
        return numHoteles;
    }

    public int getNumCasas() {
        return numCasas;
    }
    
    boolean getHipotecada() {
        return hipotecada;
    }

    int calcularImporteAlquiler() {
        int costeAlquiler = this.alquilerBase + (int) (this.numCasas * 0.5 + this.numHoteles * 2);
        return costeAlquiler;
    }
    
    int calcularCosteCancelar() {
        int costeCancelar = calcularCosteHipotecar();
        costeCancelar = (int) (costeCancelar + (costeCancelar * 0.1));
        return costeCancelar;
    }

    int calcularPrecioVenta() {
        int precioVenta;
        precioVenta = (int) (precioCompra + (numCasas + numHoteles) * precioEdificar * factorRevalorizacion);
        return precioVenta;
    }

    int calcularCosteHipotecar() {
        return (int) (hipotecaBase + numCasas * 0.5 * hipotecaBase + numHoteles * hipotecaBase);
    }

    void cancelarHipoteca() {
        hipotecada = false;
    }

    void edificarCasa() {
        numCasas++;
    }

    void edificarHotel() {
        numCasas -= 4;
        numHoteles++;
    }

    int getHipotecaBase() {
        return hipotecaBase;
    }
    
    boolean propietarioEncarcelado() {
        return propietario.getEncarcelado();
    }
    
    boolean tengoPropietario() {
        return propietario != null;
    }

    int hipotecar() {
        this.hipotecada = true;
        int costeHipoteca = this.calcularCosteHipotecar();
        return costeHipoteca;
    }

    int pagarAlquiler() {
        int costeAlquiler = this.calcularImporteAlquiler();
        this.propietario.modificarSaldo(costeAlquiler);
        return costeAlquiler;
    }
    
    void setHipotecada(boolean hipotecada) {
        this.hipotecada = hipotecada;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + 
                "@ Hipotecada: " + hipotecada + 
                "@ PrecioCompra: " + precioCompra +
                "@ Alquiler Base: " + alquilerBase  +
                "@ FactorRevalorizacion: " + factorRevalorizacion +
                "@ hipotecaBase: " + hipotecaBase +
                "@ Precio Edificar: " + precioEdificar +
                "@ numHoteles: " + numHoteles +
                "@ numCasas: " + numCasas + "\n";
    }

}
