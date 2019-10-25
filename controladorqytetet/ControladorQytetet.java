package controladorqytetet;

import java.util.ArrayList;
import modeloqytetet.EstadoJuego;
import modeloqytetet.MetodoSalirCarcel;
import modeloqytetet.Qytetet;


public class ControladorQytetet {
    private static ControladorQytetet instance = new ControladorQytetet();
    private ArrayList<String> nombreJugadores = new ArrayList<>();
    private Qytetet modelo = Qytetet.getInstance();
    
    private ControladorQytetet(){}
    
    public static ControladorQytetet getInstance(){
        return instance;
    }
    
    public void setNombreJugadores(ArrayList<String> nombreJugadores){
        this.nombreJugadores = nombreJugadores;
    }
    
    public ArrayList<Integer> obtenerOperacionesJuegoValidas(){
        ArrayList<Integer> permitidas = new ArrayList<>();
        if(modelo.getJugadores().isEmpty())
            permitidas.add(OpcionMenu.INICIARJUEGO.ordinal());
        else{
            if(modelo.getEstadoJuego() == EstadoJuego.ALGUNJUGADORENBANCARROTA){
                permitidas.add(OpcionMenu.OBTENERRANKING.ordinal());
            }else if(modelo.getEstadoJuego() == EstadoJuego.JA_PREPARADO){
                permitidas.add(OpcionMenu.JUGAR.ordinal());
            }else if(modelo.getEstadoJuego() == EstadoJuego.JA_PUEDEGESTIONAR){
                permitidas.add(OpcionMenu.PASARTURNO.ordinal());
                permitidas.add(OpcionMenu.VENDERPROPIEDAD.ordinal());
                permitidas.add(OpcionMenu.HIPOTECARPROPIEDAD.ordinal());
                permitidas.add(OpcionMenu.CANCELARHIPOTECA.ordinal());
                permitidas.add(OpcionMenu.EDIFICARCASA.ordinal());
                permitidas.add(OpcionMenu.EDIFICARHOTEL.ordinal());
                permitidas.add(OpcionMenu.PASARTURNO.ordinal());
            }else if(modelo.getEstadoJuego() == EstadoJuego.JA_PUEDECOMPRAROGESTIONAR){
                permitidas.add(OpcionMenu.COMPRARTITULOPROPIEDAD.ordinal());
                permitidas.add(OpcionMenu.VENDERPROPIEDAD.ordinal());
                permitidas.add(OpcionMenu.HIPOTECARPROPIEDAD.ordinal());
                permitidas.add(OpcionMenu.CANCELARHIPOTECA.ordinal());
                permitidas.add(OpcionMenu.EDIFICARCASA.ordinal());
                permitidas.add(OpcionMenu.EDIFICARHOTEL.ordinal());
                permitidas.add(OpcionMenu.PASARTURNO.ordinal());
            }else if(modelo.getEstadoJuego() == EstadoJuego.JA_CONSORPRESA){
                permitidas.add(OpcionMenu.APLICARSORPRESA.ordinal());
            }else if(modelo.getEstadoJuego() == EstadoJuego.JA_ENCARCELADO){
                permitidas.add(OpcionMenu.PASARTURNO.ordinal());
            }else if(modelo.getEstadoJuego() == EstadoJuego.JA_ENCARCELADOCONOPCIONDELIBERTAD){
                permitidas.add(OpcionMenu.INTENTARSALIRCARCELPAGANDOLIBERTAD.ordinal());
                permitidas.add(OpcionMenu.INTENTARSALIRCARCELTIRANDODADO.ordinal());
            }
            
            permitidas.add(OpcionMenu.MOSTRARJUGADORACTUAL.ordinal());
            permitidas.add(OpcionMenu.MOSTRARJUGADRES.ordinal());
            permitidas.add(OpcionMenu.MOSTRARTABLERO.ordinal());
            permitidas.add(OpcionMenu.TERMINARJUEGO.ordinal());
        }
        
            return permitidas;
    }
    
    public boolean necesitaElegirCasilla(int opcionMenu){
        OpcionMenu opcion = OpcionMenu.values()[opcionMenu];
        return opcion == OpcionMenu.HIPOTECARPROPIEDAD || opcion == OpcionMenu.CANCELARHIPOTECA || 
               opcion == OpcionMenu.EDIFICARCASA || opcion == OpcionMenu.EDIFICARHOTEL ||
               opcion == OpcionMenu.VENDERPROPIEDAD;
    }
    
    public ArrayList<Integer> obtenerCasillasValidas(int opcionMenu){
        OpcionMenu opcion = OpcionMenu.values()[opcionMenu];
        
        if(opcion == OpcionMenu.HIPOTECARPROPIEDAD)
            return modelo.obtenerPropiedadesJugadorSegunEstadoHipoteca(false);
        else if(opcion == OpcionMenu.CANCELARHIPOTECA)
            return modelo.obtenerPropiedadesJugadorSegunEstadoHipoteca(true);
        else if(opcion == OpcionMenu.EDIFICARCASA)
            return modelo.obtenerPropiedadesJugador();
        else if(opcion == OpcionMenu.EDIFICARHOTEL)
            return modelo.obtenerPropiedadesJugador();
        else if(opcion == OpcionMenu.VENDERPROPIEDAD)
            return modelo.obtenerPropiedadesJugador();
            
        return null;
    }
    
    public String realizarOperacion(int opcionElegida, int casillaElegida){
        OpcionMenu opcion = OpcionMenu.values()[opcionElegida];
        String mensaje = "";
        
        if(opcion == OpcionMenu.INICIARJUEGO)
            modelo.inicializarJuego(nombreJugadores);
        else if(opcion == OpcionMenu.JUGAR){
            modelo.jugar();
            mensaje = "El dado ha sido tirado y ha salido un: " + modelo.getValorDado() + ".\n" + modelo.obtenerCasillaJugadorActual();
        }else if(opcion == OpcionMenu.APLICARSORPRESA){
            mensaje = "Sorpresa aplicada:\n" + modelo.getCartaActual();
            modelo.aplicarSorpresa();
        }else if(opcion == OpcionMenu.INTENTARSALIRCARCELPAGANDOLIBERTAD){
            modelo.intentarSalirCarcel(MetodoSalirCarcel.PAGANDOLIBERTAD);
            if(modelo.jugadorActualEncarcelado())
                mensaje = "No se pudo salir de la cárcel.";
        }else if(opcion == OpcionMenu.INTENTARSALIRCARCELTIRANDODADO){
            modelo.intentarSalirCarcel(MetodoSalirCarcel.TIRANDODADO);
            if(modelo.jugadorActualEncarcelado())
                mensaje = "No se pudo salir de la cárcel.";
        }else if(opcion == OpcionMenu.COMPRARTITULOPROPIEDAD){
            boolean comprado = modelo.comprarTituloPropiedad();
            if(!comprado)
                mensaje = "No se pudo comprar: no tienes saldo suficiente.";
        }else if(opcion == OpcionMenu.CANCELARHIPOTECA){
            boolean cancelada = modelo.cancelarHipoteca(casillaElegida);
            if(!cancelada)
                mensaje = "No se pudo cancelar. ";
        }else if(opcion == OpcionMenu.EDIFICARCASA){
            boolean sepudo = modelo.edificarCasa(casillaElegida);
            if(!sepudo)
                mensaje = "No se pudo edificar la casa. ";
        }else if(opcion == OpcionMenu.EDIFICARHOTEL){
            boolean sepudo = modelo.edificarHotel(casillaElegida);
            if(!sepudo)
                mensaje = "No se pudo edificar el hotel. ";
        }else if(opcion == OpcionMenu.VENDERPROPIEDAD){
            boolean vendida = modelo.venderPropiedad(casillaElegida);
            if(!vendida)
                mensaje = "No se pudo vender la propiedad. ";
        }else if(opcion == OpcionMenu.PASARTURNO)
            modelo.siguienteJugador();
        else if(opcion == OpcionMenu.OBTENERRANKING)
            modelo.obtenerRanking();
        else if(opcion == OpcionMenu.TERMINARJUEGO){
            System.out.println("Fin del Juego");
            System.exit(0);
        }else if(opcion == OpcionMenu.MOSTRARJUGADORACTUAL)
            mensaje = modelo.getJugadorActual().toString();
        else if(opcion == OpcionMenu.MOSTRARJUGADRES)
            mensaje = modelo.getJugadores().toString();
        else if(opcion == OpcionMenu.MOSTRARTABLERO)
            mensaje =modelo.getTablero().toString();
        else if(opcion == OpcionMenu.HIPOTECARPROPIEDAD)
            modelo.hipotecarPropiedad(casillaElegida);
        
        return mensaje;
    }
}
