package vista;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;


public class ProductoVista extends JLabel {
  private String codigo;
  private String denominacion;
  private int cantidad;

  /**
   * ProductoVista
   * 
   */
  public ProductoVista() {
    setHorizontalAlignment(SwingConstants.CENTER);
    setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
  }
  
  /**
   * Devuelve el código del producto
   * 
   */
  public String devolverCodigo() {
    return codigo;
  }
    
  /**
   * Pone código del producto
   * 
   */
  public void ponerCodigo(String codigo) {
    this.codigo = codigo;
  }

  /**
   * Pone denominación del producto
   * 
   */
  public void ponerDenominacion(String denominacion) {
    this.denominacion = denominacion;
  }  
  
  /**
   * Pone cantidad del producto
   * 
   */
  public void ponerCantidad(int cantidad) {
    this.cantidad = cantidad;
    
    setEnabled(cantidad > 0);
  }
  
  /**
   * Actualiza vista del producto
   * 
   */
  public void actualizarVista() {
    setText(denominacion + " ("  + cantidad + ")");
  }
  
  /**
   * Sobreescribe toString
   * 
   */  
  @Override
  public String toString() {
    return codigo;
  }
}
      
      
