package modelo;

import java.util.Observable;
import vista.ComandaRestauranteVista;

public class Producto extends Observable{
    
    private String codigo;
    private String denominacion;
    private float precio;
    private int existencias;

    public Producto(String codigo, String denominacion, float precio,
                     int existencias) {
        
        this.codigo = codigo;
        this.denominacion = denominacion;
        this.precio = precio;
        this.existencias = existencias;

    }
    
    public void añadirExistencia(int cantidad){
        existencias += cantidad;       
        setChanged();
        notifyObservers(new Tupla<>(codigo,existencias));
    }
    
    
    public boolean restarExistencia(int cantidad){
        if((existencias - cantidad) >= 0){
            existencias -= cantidad;
            
            setChanged();
            notifyObservers(new Tupla<>(codigo,existencias));
            
            return true;
        }
        
        return false;
    }
    
    public String devuelveCodigo(){
        return codigo;
    }
    
    public String devueveDenominacion(){
        return denominacion;
    }
    
    public int devuelveExistencias(){
        return existencias;
    }
    
    
    public void añadirObservador(ComandaRestauranteVista observador){
        addObserver(observador);
    }
    
    public void eliminarObservador(ComandaRestauranteVista observador){
        deleteObserver(observador);
    }
    
    
   public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Producto)) {
            return false;
        }
        Producto tmp = (Producto) obj;
        return (codigo == tmp.codigo) && (denominacion == tmp.denominacion)
                && (precio == tmp.precio);
    }

    
    
    public String toString(){
        return codigo + " " + denominacion + " " + precio + "\n";
    }
    
    
    
    
    
    
}
