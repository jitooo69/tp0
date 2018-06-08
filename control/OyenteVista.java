
package control;

public interface OyenteVista {
    
    public enum Evento {
        NUEVA_CONSUMICION, GENERAR_COMANDA, ELIMINAR_CONSUMICION, SALIR
    }

    public void notificar(Evento evento, Object obj);
    
}
