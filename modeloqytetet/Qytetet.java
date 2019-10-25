package modeloqytetet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author antonioperea
 */
public class Qytetet {
    
    public static int MAX_JUGADORES = 4;
    public static int NUM_CASILLAS = 20;
    
    static int NUM_SORPRESA = 10;
    static int PRECIO_LIBERTAD = 200;
    static int SALDO_SALIDA = 1000;
    
    private static Qytetet instance;
    
    
    private Dado dado;
    private Sorpresa cartaActual;
    private EstadoJuego estado;
    private Jugador jugadorActual;
    private Tablero tablero;
    private int iterador = 0;
    private ArrayList<Jugador> jugadores;
    private ArrayList<Sorpresa> mazo;
    
    
    
    
    private Qytetet(){
        cartaActual = null;
        dado = Dado.getInstance();
        jugadores = new ArrayList<>();
        mazo = new ArrayList<>();
        jugadorActual = null;
        
    }
    
    public static Qytetet getInstance(){
        if(instance == null){
            instance = new Qytetet();
        }
        return instance;
    }
    
    public Tablero getTablero(){
        return this.tablero;
    }
    
    private void inicializarTablero(){
        this.tablero = new Tablero();
    } 
    
    public void inicializarJuego(ArrayList <String> nombres){
        inicializarJugadores(nombres);
        inicializarTablero();
        inicializarCartasSorpresa();
        salidaJugadores();
    }
    
    private void inicializarJugadores(ArrayList <String> nombres){
        Jugador jugador_aux = null;
        for (int i = 0 ; i < nombres.size(); i++){
            jugador_aux = new Jugador(nombres.get(i));
            jugadores.add(jugador_aux);
        }
    }
    
    ArrayList getMazo(){
        return mazo;
    }
    
    private void inicializarCartasSorpresa(){
        
        
        mazo.add(new Sorpresa("Es tu cumpleaños!! Todos los juegadores te dan 200 euros", 200, TipoSorpresa.PORJUGADOR ));
        mazo.add(new Sorpresa("Has ganado la loteria del barrio. Cobras 400 euros", 400, TipoSorpresa.PAGARCOBRAR));
        mazo.add(new Sorpresa("Te han descubierto una pistola en tu coche.Vas a la carcel", tablero.getCarcel().getNumeroCasilla(), TipoSorpresa.IRACASILLA ));
        mazo.add(new Sorpresa("Tu tia abuela Felisa ha muerto. Has ganado 1000 euros",1000, TipoSorpresa.PAGARCOBRAR));
        mazo.add(new Sorpresa("El ayuntamiento ha sacado una nueva ley, paga 50 euros por casa u hotel", 50, TipoSorpresa.PORCASAHOTEL ));
        mazo.add(new Sorpresa("Menuda tormenta, hay que hacer reparaciones; paga 100 euros por casa u hotel", 100, TipoSorpresa.PORCASAHOTEL ));
        mazo.add(new Sorpresa("No fue buena idea invitar a comer a tus primos. Paga 200 euros",-200, TipoSorpresa.PAGARCOBRAR));
        mazo.add(new Sorpresa("Paga la fianza de tu padre, 300 euros.", -300, TipoSorpresa.PAGARCOBRAR));
        mazo.add(new Sorpresa("Un fan anónimo ha pagado tu fianza. Sales de la cárcel", 0, TipoSorpresa.SALIRCARCEL));
        mazo.add(new Sorpresa("Hay un incendio ¡Ve al Callejón Tenebroso!", 1, TipoSorpresa.IRACASILLA));
        mazo.add(new Sorpresa("Te conviertes en especulador de fianza 3000",3000, TipoSorpresa.CONVERTIRME));
        mazo.add(new Sorpresa("Te conviertes en especulador de fianza 5000", 5000, TipoSorpresa.CONVERTIRME));
        
        Random rndm = new Random();  
        Collections.shuffle(mazo, rndm);
    }

    void actuarSiEnCasillaEdificable(){
        boolean deboPagar = jugadorActual.DeboPagarAlquiler();
        if(deboPagar){
            jugadorActual.pagarAlquiler();
        }
        Casilla casilla = obtenerCasillaJugadorActual();
        boolean tengoPropietario = casilla.tengoPropietario();
        if (tengoPropietario){
            estado = EstadoJuego.JA_PUEDEGESTIONAR;
        }
        else{
            estado = EstadoJuego.JA_PUEDECOMPRAROGESTIONAR;
        }
        
    }
    
    void actuarSiEnCasillaNoEdificable(){
        estado = EstadoJuego.JA_PUEDEGESTIONAR;
        Casilla casillaActual = jugadorActual.getCasillaActual();
        if(casillaActual.getTipo()==TipoCasilla.IMPUESTO){
            jugadorActual.pagarImpuesto();
        }
        else if (casillaActual.getTipo() == TipoCasilla.JUEZ){
            encarcelarJugador();
        }
        else if (casillaActual.getTipo() == TipoCasilla.SORPRESA){
            cartaActual = mazo.get(0);
            mazo.remove(0);
            estado = EstadoJuego.JA_CONSORPRESA;
        }
    }
    
    public void aplicarSorpresa(){
        estado = EstadoJuego.JA_PUEDEGESTIONAR;
        TipoSorpresa tipo = cartaActual.getTipo();
        if(tipo == TipoSorpresa.SALIRCARCEL)
            jugadorActual.setCartaLibertad(cartaActual);
        else if(tipo == TipoSorpresa.PAGARCOBRAR){
            jugadorActual.modificarSaldo(cartaActual.getValor());
            if(jugadorActual.getSaldo()<0)
                estado = EstadoJuego.ALGUNJUGADORENBANCARROTA;
        }
        else{
            mazo.add(cartaActual);
        }
        if (tipo == TipoSorpresa.IRACASILLA){
            int valor = cartaActual.getValor();
            boolean casillaCarcel = tablero.esCasillaCarcel(valor);
            if(casillaCarcel)
                encarcelarJugador();
            else
                mover(valor);
        } else if(tipo == TipoSorpresa.PORCASAHOTEL){
            int cantidad = cartaActual.getValor();
            int numeroTotal = jugadorActual.CuantasCasasHotelesTengo();
            jugadorActual.modificarSaldo(cantidad*numeroTotal);
            if(jugadorActual.getSaldo()<0)
                estado = EstadoJuego.ALGUNJUGADORENBANCARROTA;
        } else if(tipo == TipoSorpresa.PORJUGADOR){
            for(int i = 0; i<jugadores.size()-1; i++){
                Jugador jugador = jugadores.get((iterador+1)%jugadores.size());
                if(jugador != jugadorActual){
                    jugador.modificarSaldo(cartaActual.getValor());
                    
                    if(jugador.getSaldo() < 0)
                       estado = EstadoJuego.ALGUNJUGADORENBANCARROTA;
                    
                    jugadorActual.modificarSaldo(-cartaActual.getValor());
                    
                    if(jugadorActual.getSaldo()<0)
                        estado = EstadoJuego.ALGUNJUGADORENBANCARROTA;
                }
            }
        }else if(tipo == TipoSorpresa.CONVERTIRME){
            jugadores.set(iterador, jugadorActual.convertirme(cartaActual.getValor()));
        }
    }
    
    public boolean cancelarHipoteca(int numeroCasilla){
        Casilla casilla = obtenerCasillaJugadorActual();
        boolean esEdificable = casilla.soyEdificable();
        TituloPropiedad titulo = casilla.getTitulo();
        boolean hipotecada = titulo.getHipotecada();
        boolean cancelar = false;

        if (esEdificable && hipotecada)
            cancelar = jugadorActual.cancelarHipoteca(titulo);
      
        estado = EstadoJuego.JA_PUEDEGESTIONAR;
      
        return cancelar;
    }
    
    public boolean comprarTituloPropiedad(){
        boolean comprado = jugadorActual.comprarTituloPropiedad();
        if(comprado){
            estado = EstadoJuego.JA_PUEDEGESTIONAR;
        }
        return comprado;
    }
    
    public boolean edificarCasa(int numeroCasilla){
        boolean edificada;
        Casilla casilla = tablero.ObtenerCasillaNumero(numeroCasilla);
        TituloPropiedad titulo = casilla.getTitulo();
        edificada = jugadorActual.edificarCasa(titulo);
        if (edificada){
            estado = EstadoJuego.JA_PUEDEGESTIONAR;
        }
        return edificada;
    }
    
    public boolean edificarHotel(int numeroCasilla){
       Casilla casilla = tablero.ObtenerCasillaNumero(numeroCasilla);
       TituloPropiedad titulo = casilla.getTitulo();
       boolean edificada = jugadorActual.edificarHotel(titulo);
       
       if(edificada)
           estado = EstadoJuego.JA_PUEDEGESTIONAR;
       
       return edificada;
    }
    
    private void encarcelarJugador(){
        Casilla casillaCarcel = tablero.getCarcel();
        if(jugadorActual.deboIrACarcel()){
            jugadorActual.irACarcel(casillaCarcel);
            estado = EstadoJuego.JA_ENCARCELADO;
        }
        else{
            Sorpresa carta = jugadorActual.devolverCartaLibertad();
            mazo.add(carta);
            estado = EstadoJuego.JA_PUEDEGESTIONAR;
        }
    }
    
    public Sorpresa getCartaActual() {
        return cartaActual;
    }
    
    Dado getDado(){
        return this.dado;
    }
    
    public EstadoJuego getEstadoJuego(){
        return estado;
    }
    
    public Jugador getJugadorActual(){
        return jugadorActual;
    }
    
    public ArrayList getJugadores() {
        return this.jugadores;
    }
    
    public int getValorDado(){
        return dado.getValor();
    }
    
    public void hipotecarPropiedad(int numeroCasilla){
        Casilla casilla = tablero.ObtenerCasillaNumero(numeroCasilla);
        TituloPropiedad titulo = casilla.getTitulo();
        jugadorActual.hipotecarPropiedad(titulo);
    }
    
    
    
    public boolean intentarSalirCarcel(MetodoSalirCarcel metodo){
        if(metodo == MetodoSalirCarcel.TIRANDODADO){
            int resultado = tirarDado();
            if(resultado >= 5)
                jugadorActual.setEncarcelado(false);
        }else if(metodo == MetodoSalirCarcel.PAGANDOLIBERTAD){
            jugadorActual.pagarLibertad(PRECIO_LIBERTAD);
        }
        boolean libre = jugadorActual.getEncarcelado();
        if(libre)
            estado = EstadoJuego.JA_PREPARADO;
        else
            estado = EstadoJuego.JA_ENCARCELADO;
        return libre;
    }
    
    public void jugar(){
        Casilla casillafinal = tablero.obtenerCasillaFinal(this.obtenerCasillaJugadorActual(), this.tirarDado());
        mover(casillafinal.getNumeroCasilla());
    }
    
    void mover(int numCasillaDestino){
        Casilla casillaInicial = jugadorActual.getCasillaActual();
        Casilla casillaFinal = tablero.ObtenerCasillaNumero(numCasillaDestino);
        jugadorActual.setCasillaActual(casillaFinal);
        if(numCasillaDestino < casillaInicial.getNumeroCasilla())
            jugadorActual.modificarSaldo(SALDO_SALIDA);
        if(casillaFinal.soyEdificable()){
            this.actuarSiEnCasillaEdificable();
        }else
            this.actuarSiEnCasillaNoEdificable();
    }
    
    public Casilla obtenerCasillaJugadorActual(){
        return jugadorActual.getCasillaActual();
    }
    
    public ArrayList obtenerCasillasTablero(){
        return tablero.getCasillas();
    }
    
    public boolean jugadorActualEnCalleLibre(){
        return jugadorActual.estoyEnCalleLibre();
    }
    
    public boolean jugadorActualEncarcelado(){
        return jugadorActual.getEncarcelado();
    }    
    
    public ArrayList obtenerPropiedadesJugador(){
        ArrayList<Integer> propiedades = new ArrayList<>();
        ArrayList<TituloPropiedad> prop = jugadorActual.getPropiedades();
        ArrayList<Casilla> casillas = tablero.getCasillas();
        
        
        for(int i = 0; i < prop.size(); ++i){
            for(int j = 0; j < casillas.size(); ++j){
                if(prop.get(i) == casillas.get(j).getTitulo())
                    propiedades.add(j);
            }
        }
        
        return propiedades;
    }
    
    public ArrayList obtenerPropiedadesJugadorSegunEstadoHipoteca(boolean estadoHipoteca){
        ArrayList<Integer> propiedades = new ArrayList<>();
        ArrayList<TituloPropiedad> prop = jugadorActual.obtenerPropiedades(estadoHipoteca);
        ArrayList<Casilla> casillas = tablero.getCasillas();
        
        
        for(int i = 0; i < prop.size(); ++i){
            for(int j = 0; j < casillas.size(); ++j){
                if(prop.get(i) == casillas.get(j).getTitulo())
                    propiedades.add(j);
            }
        }
        
        return propiedades;
    }
    
    public void obtenerRanking(){
        Collections.sort(jugadores);
    }
    
    public int obtenerSaldoJugadorActual(){
        return jugadores.get(iterador).getSaldo();
    }
    
    private void salidaJugadores(){
        int turno;
        for(int i = 0; i < jugadores.size(); ++i)
            jugadores.get(i).setCasillaActual(tablero.ObtenerCasillaNumero(0));
        Random rndm = new Random();         
        turno = rndm.nextInt(jugadores.size());
        iterador = turno;
        jugadorActual = jugadores.get(turno);
        estado = EstadoJuego.JA_PREPARADO;
    }
    
    private void setCartaActual(Sorpresa cartaActual){
        this.cartaActual = cartaActual;
    }
    
    public void siguienteJugador(){
        iterador++;
        iterador = iterador%jugadores.size();
        jugadorActual = jugadores.get(iterador);
        
        if(jugadores.get(iterador).getEncarcelado())
            estado = EstadoJuego.JA_ENCARCELADOCONOPCIONDELIBERTAD;
        else
            estado = EstadoJuego.JA_PREPARADO;
    }
    
    int tirarDado(){
        return dado.tirar();
    }
    
    public boolean venderPropiedad(int numeroCasilla){
        Casilla casilla = tablero.ObtenerCasillaNumero(numeroCasilla);
        jugadorActual.venderPropiedad(casilla);
        estado = EstadoJuego.JA_PUEDEGESTIONAR;
        return true;
    }
    
    
}
