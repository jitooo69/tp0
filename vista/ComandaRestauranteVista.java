
package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import modelo.Consumicion;
import control.OyenteVista;
import java.util.Map;
import modelo.Producto;
import modelo.Tupla;


/**
 * Vista principal de la comanda
 * 
 */
public class ComandaRestauranteVista implements ActionListener, Observer {  
  private static ComandaRestauranteVista instancia = null;  // es singleton
  private OyenteVista oyenteVista;
  private JFrame ventana;
  private ProductosVista productosVista;
  private JList comandaVista;
  private DefaultListModel comandaVistaModelo;
  private JButton botonGenerarComanda;
  private JButton botonEliminarConsumicion;
  
  /** Identificadores de textos dependientes del idioma */
  private static final String TITULO = "Comandas Restaurante";
  private static final String ETIQUETA_COMANDA_VISTA = "Comanda";
  private static final String ELIMINAR_CONSUMICION = "Eliminar consumición";
  private static final String GENERAR_COMANDA = "Generar comanda";
  public static final String ERROR_FICHERO 
          = "No se encuentra el fichero de productos";
  
  public static final String ETIQUETA_INTRODUCE_CANTIDAD = "Cantidad";
  public static final String MENSAJE_ERROR_EXISTENCIAS = 
          "No hay suficientes existencias de ";
  public static final String MENSAJE_ERROR_FICHERO_COMANDA = 
          "Error al generar fichero comanda";
  public static final String MENSAJE_ERROR_FICHERO_PRODUCTOS =
          "Error en fichero de productos";
  public static final String MENSAJE_COMANDA_GENERADA =
          "Comanda generada";
 
  /** Constantes para redimensionamiento */
  public static final int MARGEN_HORIZONTAL = 50;
  public static final int MARGEN_VERTICAL = 20;
  
  private static final int ANCHURA_LISTA = 160;
  private static final int ALTURA_LISTA = 200; 
  
  /**
   * Construye la vista principal
   * 
   */
  private ComandaRestauranteVista(OyenteVista oyenteVista) {
    this.oyenteVista = oyenteVista;
    crearVentanaPrincipal();
  }  

  /**
   * Devuelve la instancia Singleton de la vista 
   * 
   */        
  public static synchronized ComandaRestauranteVista 
      devolverInstancia(OyenteVista oyenteIU) {
    if (instancia == null) {
      instancia = new ComandaRestauranteVista(oyenteIU);    
    }
    return instancia;
  } 
  
  /**
   * Crea la ventana principal
   * 
   */  
  private void crearVentanaPrincipal() { 
    ventana = new JFrame(TITULO);
    
    ventana.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        oyenteVista.notificar(OyenteVista.Evento.SALIR, null);
      }
    });
    
    ventana.getContentPane().setLayout(new BorderLayout());
    
    // creamos elementos    
    JPanel panelProductos = new JPanel();
    crearProductosVista(panelProductos);
    ventana.getContentPane().add(panelProductos, BorderLayout.CENTER);
    
    JPanel panelComanda = new JPanel();
    crearComandaVista(panelComanda);
    ventana.getContentPane().add(panelComanda, BorderLayout.EAST);
    
    // hacemos visible la ventana     
    ventana.setResizable(false);    
    
    ventana.pack();  // ajusta ventana y sus componentes
    ventana.setVisible(true);
    ventana.setLocationRelativeTo(null);  // centra en la pantalla
  }  

  /**
   * Crea vista de los productos
   * 
   */   
  private void crearProductosVista(JPanel panel) {      
    panel.setLayout(new FlowLayout());   
    productosVista = 
        new ProductosVista(this, ProductosVista.RECIBIR_EVENTOS_RATON);
    panel.add(productosVista);       
  }
      
  /**
   * Crea vista de la comanda
   * 
   */   
  private void crearComandaVista(JPanel panel) {
    panel.setLayout(new BorderLayout());
    
    panel.add(new JLabel(ETIQUETA_COMANDA_VISTA), BorderLayout.NORTH); 
    
    comandaVistaModelo = new DefaultListModel();    
    comandaVista = new JList(comandaVistaModelo);
    comandaVista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    comandaVista.setLayoutOrientation(JList.VERTICAL);
    JScrollPane panelScroll = new JScrollPane(comandaVista);
    panelScroll.setPreferredSize(new Dimension(ANCHURA_LISTA, ALTURA_LISTA));
    panel.add(panelScroll, BorderLayout.CENTER);
    comandaVista.addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent evt) {
        if (! botonEliminarConsumicion.isEnabled()) {
          botonEliminarConsumicion.setEnabled(true);
        }
      }
    });

    JPanel panelBotones = new JPanel();    
    panelBotones.setLayout(new FlowLayout());
 
    botonGenerarComanda = new JButton(GENERAR_COMANDA);
    botonGenerarComanda.addActionListener(this);
    botonGenerarComanda.setActionCommand(GENERAR_COMANDA);
    botonGenerarComanda.setEnabled(false);
    panelBotones.add(botonGenerarComanda);
    
    botonEliminarConsumicion = new JButton(ELIMINAR_CONSUMICION);
    botonEliminarConsumicion.addActionListener(this);
    botonEliminarConsumicion.setActionCommand(ELIMINAR_CONSUMICION);
    botonEliminarConsumicion.setEnabled(false);
    panelBotones.add(botonEliminarConsumicion);
    
    panel.add(panelBotones, BorderLayout.SOUTH);
  }
  
  /**
   * Recibe eventos de los elementos de la interfaz redirigiéndolos a control
   * 
   */
  @Override
  public void actionPerformed(ActionEvent e)  {
    notificarAControl(e.getActionCommand());
  }  
      
  /**
   * Escribe mensaje con diálogo modal
   * 
   */    
  public void escribirMensajeDialogo(String texto) {
    JOptionPane.showMessageDialog(ventana, texto, TITULO, 
        JOptionPane.INFORMATION_MESSAGE);    
  }  
  
  /**
   * Notifica un evento de la interfaz de usuario a control
   * 
   */
  public void notificarAControl(String evento) {
    switch(evento) {
        case ELIMINAR_CONSUMICION:  
           oyenteVista.notificar(OyenteVista.Evento.ELIMINAR_CONSUMICION, 
                                 comandaVista.getSelectedIndex());
           
           quitarConsumicionComandaVista();
           break;

        case GENERAR_COMANDA:
           oyenteVista.notificar(OyenteVista.Evento.GENERAR_COMANDA, null); 
           break;
     }
  }  
  
  /**
   * Pone productos en la vista
   * 
   */  
  public void ponerProductosVista(Map<String,Producto> productos) {
    productosVista.ponerProductos(productos);
  }  
  
  /**
   * Nueva comanda vista
   * 
   */  
  public void nuevaComandaVista() {
    comandaVistaModelo.clear();
  }  

  
  /**
   * Pone texto descriptivo de consumición en la vista de comanda
   * 
   */  
  public void ponerConsumicionComandaVista(String textoComanda) {
    comandaVistaModelo.addElement(textoComanda);
    
    if (! botonGenerarComanda.isEnabled()) {
      botonGenerarComanda.setEnabled(true);
    }
  }   
  
  /**
   * Quita consumición seleccionada de la vista de comanda
   * 
   */  
  public void quitarConsumicionComandaVista() {
    comandaVistaModelo.removeElement(comandaVista.getSelectedValue());  

    botonEliminarConsumicion.setEnabled(false);              
    if (comandaVistaModelo.getSize() == 0) {
      botonGenerarComanda.setEnabled(false);
    }
  } 
  
  /**
   * Notifica a control una nueva consumición
   * 
   */  
  public void notificarNuevaConsumicion(Consumicion consumicion) {
    oyenteVista.notificar(OyenteVista.Evento.NUEVA_CONSUMICION, consumicion);
  }  
  
  /**
   * Actualiza vista por cambio en modelo
   * 
   */
  public void update(Observable obj, Object arg) {
    
    Tupla tupla;
    
    if (arg instanceof Tupla) {
        tupla = (Tupla) arg;
        
        productosVista.actualizarProductoVista((String)tupla.a,(Integer)tupla.b);
    }
    
  }
}