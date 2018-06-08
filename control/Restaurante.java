
package control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import modelo.Consumicion;
import modelo.Producto;
import vista.ComandaRestauranteVista;


public class Restaurante implements OyenteVista {

    private ComandaRestauranteVista vista;

    private Map<String, Producto> listaProductos = new HashMap<>();
    private List<Consumicion> listaConsumiciones = new ArrayList<>();

    private static final String FICHERO_PRODUCTOS = "./productos/productos.txt";
    private static final String FICHERO_COMANDAS = "./comandas/comanda";
    
    private int idComanda = 0;
    private static final String SEPARADOR = ";";

    public Restaurante() {

        vista = ComandaRestauranteVista.devolverInstancia(this);
        
        try {
            leerProductos();
            añadirObservadorProductos(vista);
            
        } catch (FileNotFoundException ex) {
            vista.escribirMensajeDialogo(vista.MENSAJE_ERROR_FICHERO_PRODUCTOS);
        }
        
        vista.ponerProductosVista(listaProductos);

    }

    private void leerProductos() throws FileNotFoundException {

        Scanner sc = new Scanner(new File(FICHERO_PRODUCTOS));

        while (sc.hasNextLine()) {
            añadirProducto(sc.nextLine());
        }

        sc.close();

    }

    private void añadirProducto(String producto) {

        String[] caracteristicasProducto = producto.split(SEPARADOR);

        String codigo = caracteristicasProducto[0];
        String denominacion = caracteristicasProducto[1];
        float precio = Float.parseFloat(caracteristicasProducto[2]);
        int existencias = Integer.parseInt(caracteristicasProducto[3]);

        listaProductos.put(codigo, new Producto(codigo, denominacion, precio,
                                                existencias));

    }
    
    
    private void añadirObservadorProductos(ComandaRestauranteVista vista){
        for (Producto producto: listaProductos.values()) {
            producto.addObserver(vista);
        }
    }
 
    private void nuevaConsumicion(Consumicion consumicion){
        Producto producto = listaProductos.get(consumicion.devuelveCodigo());
        
        if(producto.restarExistencia(consumicion.devuelveCantidad())){
            vista.ponerConsumicionComandaVista(producto.toString() + 
                " " + consumicion.devuelveCantidad());
            listaConsumiciones.add(consumicion);
        }else{
            vista.escribirMensajeDialogo(vista.MENSAJE_ERROR_EXISTENCIAS);
        } 
    }
    
    private void eliminarConsumicion(int indice){
        Consumicion consumicion = listaConsumiciones.get(indice);
        listaConsumiciones.remove(indice);
        
        Producto producto = listaProductos.get(consumicion.devuelveCodigo());
        producto.añadirExistencia(consumicion.devuelveCantidad());
        
    }
    
    private void generarComarda(){
        
        PrintWriter pw = null;
            
        try { 
            idComanda++;
            pw = new PrintWriter(new FileWriter(FICHERO_COMANDAS + 
                    idComanda + ".txt"));
        } catch (IOException ex) {
            vista.escribirMensajeDialogo(vista.MENSAJE_ERROR_FICHERO_COMANDA);
        }
        
        pw.println("comanda número + " + idComanda + "\n");

        for(Consumicion consumicion: listaConsumiciones){
            Producto producto = listaProductos.get(consumicion.devuelveCodigo());
            String mensaje = producto.toString() + " " + 
                    consumicion.devuelveCantidad() + "\n";
            
           pw.println(mensaje);       
        }

        pw.close();
        
        listaConsumiciones.clear();
        vista.escribirMensajeDialogo(vista.MENSAJE_COMANDA_GENERADA);
        vista.nuevaComandaVista();
        
        
    }
    
    private void salir(){
        System.exit(0);
    }
    
    

    public void notificar(Evento evento, Object obj) {
        switch (evento) {

            case NUEVA_CONSUMICION:
                nuevaConsumicion((Consumicion) obj);
                break;

            case ELIMINAR_CONSUMICION:
                eliminarConsumicion((Integer)obj);
                break;

            case GENERAR_COMANDA:
                generarComarda();
                break;

            case SALIR:
                salir();
                break;
        }

    }


    public static void main(String[] args) {
        new Restaurante();
    }

}
