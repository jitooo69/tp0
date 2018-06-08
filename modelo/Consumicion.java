package modelo;

public class Consumicion {
    
    private String codigoProducto;
    private int cantidad;
    
    
    
    public Consumicion(String codigoProducto, int cantidad){
        this.codigoProducto = codigoProducto;
        this.cantidad = cantidad;
    }
    
    public String devuelveCodigo(){
        return codigoProducto;
    }
    
    public int devuelveCantidad(){
        return cantidad;
    }
    
    
    
}
