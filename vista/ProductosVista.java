/**
 * ProductosVista.java
 *
 */
package vista;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import modelo.Consumicion;
import modelo.Producto;

/**
 * Define la vista de los productos del restaurante
 * 
 */
public class ProductosVista extends JPanel {
  private static final int ALTURA_FILA = 120;
  private static final int ANCHURA_COLUMNA = 120;
  private ProductoVista[][] productosVista;
  private ComandaRestauranteVista vista;

  private static final int NUM_FILAS = 5;
  private static final int NUM_COLUMNAS = 5;
  public static final boolean RECIBIR_EVENTOS_RATON = true;
  public static final boolean NO_RECIBIR_EVENTOS_RATON = false;
  
  /**
   * Construye la vista de los productos
   * 
   */
  ProductosVista(ComandaRestauranteVista vista, boolean recibe_eventos_raton) {
    this.vista = vista;
    
    setLayout(new GridLayout(NUM_FILAS, NUM_COLUMNAS));
    productosVista = new ProductoVista[NUM_FILAS][NUM_COLUMNAS];
    
    for(int fil = 0; fil < NUM_FILAS; fil++) { 
      for(int col = 0; col < NUM_COLUMNAS; col++) {
        productosVista[fil][col] = new ProductoVista();         
        add(productosVista[fil][col]);    
        
        if (recibe_eventos_raton) {
          productosVista[fil][col].addMouseListener(new MouseAdapter() { 
          @Override
            public void mousePressed(MouseEvent e) {
              String entrada = "";
              int cantidad = 0;
              
              ProductoVista productoVista = (ProductoVista)e.getSource();                     
              if (productoVista.isEnabled() && 
                  productoVista.devolverCodigo() != null) {
                  while(true) {
                    // lee cantidad, por defecto una unidad  
                    entrada = JOptionPane
                        .showInputDialog(vista.ETIQUETA_INTRODUCE_CANTIDAD, 1);
                    
                    // salimos si cancelamos entrada
                    if (entrada == null) {
                      return;    
                    } 
                    try {
                      cantidad = Integer.parseInt(entrada);
                      // con cantidad menor que cero o formato 
                      // incorrecto repetimos entrada
                      if (cantidad > 0) {
                        break;
                      }
                    } catch (NumberFormatException ex) {
                     // vacío
                    }
                 }
                 vista.notificarNuevaConsumicion(
                     new Consumicion(productoVista.devolverCodigo(), cantidad));
                }
            }
 	  });
        }
      } 
    }
    this.setPreferredSize(new Dimension(NUM_FILAS * ALTURA_FILA, 
                                        NUM_COLUMNAS * ANCHURA_COLUMNA));
  }

  /**
   * Devuelve el tamaño del producto vista
   * 
   */  
  public Dimension devolverDimensionProductoVista() {
    return productosVista[0][0].getSize();
  }
  
  /**
   * Pone vista de los productos
   * 
   */   
  public void ponerProductos(Map<String,Producto> productos) {  
    
    int fil = 0;
    int col = 0;
    
    for (Producto producto: productos.values()) {
        ProductoVista productoAux = productosVista[fil][col];
        
        productoAux.ponerCodigo(producto.devuelveCodigo());
        productoAux.ponerDenominacion(producto.devueveDenominacion());
        productoAux.ponerCantidad(producto.devuelveExistencias());
        productoAux.actualizarVista();
        
        
        if(col >= NUM_COLUMNAS - 1){
            col = 0;
            fil++;
        }else{
            col++;
        }
        
        
        
        

    }
  }
  
  /**
   * Actualiza la vista de un producto
   * 
   */
  public void actualizarProductoVista(String codigo, int cantidad) {
      for (int i = 0; i < NUM_FILAS; i++) {
          for (int j = 0; j < NUM_COLUMNAS; j++) {
              if(productosVista[i][j].devolverCodigo().equals(codigo)){
                  productosVista[i][j].ponerCantidad(cantidad);
                  productosVista[i][j].actualizarVista();
                  
                  return;
              }
          }
          
      }
  }
}
