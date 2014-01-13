package com.raccoon.easyjchart;



import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * Clase que implementa el panel donde se situarán las gráficas.
 * @author Miguel Jiménez López
 */
public class JPanelGrafica extends JPanel {
	
	private static final long serialVersionUID = 6225639831211204554L;
	/**
     * Grafica contenida en el panel
     */
    private Grafica grafica = null;

    /**
     * Construtor por defecto. La grafica queda inicializada a NULL.
     */
    
    public JPanelGrafica() {
        super();
        this.setDoubleBuffered(true);
    }
    
    /**
     * Construtor de clase con un argumento de tipo Grafica. Inicializa el panel 
     * con la gráfica que se para como parámetro.
     * @param grafica1 Gráfica que quedará contenida en el panel
     */

    public JPanelGrafica(Grafica grafica1) {
        super();
        grafica = grafica1;
        this.setDoubleBuffered(true);
    }
    
    /**
     * Función que permite actualizar la gráfica contenida en el panel (es decir, sustituirla por otra distinta).
     * @param grafica1 Nueva gráfica que contendrá el panel.
     */

    public void actualizaGrafica(Grafica grafica1) {
        grafica = grafica1;
    }
    
    /**
     * Pinta en pantalla el panel.
     * @param g Lienzo sobre el que se pinta.
     */

    public void paint(Graphics g) {
        super.paint(g);
        if(grafica != null)
            grafica.pintar(g,this.getBounds());
    }

}
