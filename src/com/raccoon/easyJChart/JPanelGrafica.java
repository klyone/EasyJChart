/*
 	-----------------------------------------------------------------
 	 @file   JPanelGrafica.java
     @author Miguel Jimenez Lopez, Juan Hernandez Garcia
     @brief Clase que implementa el panel donde se situarán las gráficas.
 	-----------------------------------------------------------------	
    Copyright (C) 2014  Modesto Modesto T Lopez-Lopez
    					Miguel Jimenez Lopez
    					Juan Hernandez Garcia
    					
						
						University of Granada
	--------------------------------------------------------------------					
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.raccoon.easyJChart;


import java.awt.Graphics;
import javax.swing.JPanel;

public class JPanelGrafica extends JPanel {
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
