package com.raccoon.easyJChart;

/*
 	-----------------------------------------------------------------
 	 @file   Grafica.java
     @author Miguel Jimenez Lopez, Juan Hernandez Garcia
     @brief
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


/**
 * Dependencias necesarias
 * 
 * Esta clase utiliza otras pertenecientes a la biblioteca JFreeChart (v14) y JCommon.
 * @see http://www.jfree.org/jfreechart/
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
*
* Clase que permite pintar distintas gráficas y definir sus características (por ejemplo, colores y/o grosores).
*
*/

public class Grafica {

/**
 * Numero de funciones que tiene la grafica.
 */
private int n_series = 0;

/**
 * Clase que almacena las funciones y los parametros de las mismas (colores, grosores, leyendas, etc).
 */
private JFreeChart grafica = null;

/**
 * Se utiliza para almacenar el fondo por defecto (por si mas tarde se quiere restaurar).
 */
private Paint fondo_defecto = null;


/**
 * Construtor de la clase Grafica. Permite inicializar la gráfica con una determinada función.
 * @param funcion Conjunto de puntos que determina la función que se quiere representar.
 * @param nombre_grafica Nombre que identifica a la gráfica.
 * @param nombre_funcion Nombre que identifica a la función dentro de la gráfica (puede haber más de una función).
 * @param etiqueta_X Leyenda del eje X
 * @param etiqueta_Y Leyenda del eje Y
 */

public Grafica(Point2D[] funcion,String nombre_grafica, String nombre_funcion, String etiqueta_X, String etiqueta_Y) {
    generaGrafica(funcion,nombre_grafica,nombre_funcion,etiqueta_X,etiqueta_Y,false,Color.RED,1f); 
}

/**
 * Construtor de la clase Grafica. Permite inicializar la gráfica con una determinada función.
 * @param funcion Conjunto de puntos que determina la función que se quiere representar.
 * @param nombre_grafica Nombre que identifica a la gráfica.
 * @param nombre_funcion Nombre que identifica a la función dentro de la gráfica (puede haber más de una función).
 * @param etiqueta_X Leyenda del eje X
 * @param etiqueta_Y Leyenda del eje Y
 * @param mostrar_leyenda Indica si se quiere o no visualizar la leyenda de la gráfica.
 */

public Grafica(Point2D[] funcion,String nombre_grafica, String nombre_funcion, String etiqueta_X, String etiqueta_Y,boolean mostrar_leyenda) {
    generaGrafica(funcion,nombre_grafica,nombre_funcion,etiqueta_X,etiqueta_Y,mostrar_leyenda,Color.RED,1f); 
}

/**
 * Construtor de la clase Grafica. Permite inicializar la gráfica con una determinada función.
 * @param funcion Conjunto de puntos que determina la función que se quiere representar.
 * @param nombre_grafica Nombre que identifica a la gráfica.
 * @param nombre_funcion Nombre que identifica a la función dentro de la gráfica (puede haber más de una función).
 * @param etiqueta_X Leyenda del eje X
 * @param etiqueta_Y Leyenda del eje Y
 * @param mostrar_leyenda Indica si se quiere o no visualizar la leyenda de la gráfica.
 * @param color_funcion Indica el color con el que se quiere representar la función.
 * @param grosor Indica el grosor de linea con el que se quiere representar la función.
 */

public Grafica(Point2D[] funcion,String nombre_grafica, String nombre_funcion, String etiqueta_X, String etiqueta_Y,boolean mostrar_leyenda,Color color_funcion, float grosor) {
    generaGrafica(funcion,nombre_grafica,nombre_funcion,etiqueta_X,etiqueta_Y,mostrar_leyenda,color_funcion,grosor);    
}

/**
 * Fija el rango de valores que se visualiza de un determinado eje para una gráfica concreta.
 * @param rinferior Representa el límite inferior.
 * @param rsuperior Representa el límite superior.
 * @param n_grafica Indica la función a la que se le aplicará el rango (la primera es la 0)
 * @param ejeXY Indica el eje sobre el que se aplicará el rango (0 = Eje X, en otro caso = Eje Y)
 */

public void fijaRango(double rinferior, double rsuperior, int n_grafica, int ejeXY) {
    if(ejeXY == 0)
        fijaRangoX(rinferior,rsuperior,n_grafica);
    else
        fijaRangoY(rinferior,rsuperior,n_grafica);
}

/**
 * Elimina el rango de valores sobre un eje (Es decir, el eje podrá variar su rango dinámicamente dependiendo de
 * la función que se introduzca)
 * @param n_grafica Indica la función a la que se le aplicará el rango (la primera es la 0)
 * @param ejeXY Indica el eje sobre el que se aplicará el rango (0 = Eje X, en otro caso = Eje Y)
 */

public void quitaRango(int n_grafica,int ejeXY) {
    if(ejeXY == 0)
        quitaRangoX(n_grafica);
    else
        quitaRangoY(n_grafica);
    
}

/**
 * Visualiza la función como un conjunto de puntos discretos y no de forma continua.
 * @param ngrafica Indica la función a la que se le aplicará el rango (la primera es la 0)
 * @param visible Indica si se quiere visualizar la función como muestras discretas
 * @param tam Representa el tamaño del círculo
 */

public void visualizaMuestras(int ngrafica, boolean visible, int tam) {
    Shape circulo = null;
    
    if(visible == true) {
        circulo = new Ellipse2D.Double(-tam,-tam,2*tam,2*tam);
        ((XYLineAndShapeRenderer) grafica.getXYPlot().getRenderer(ngrafica)).setSeriesShape(0, circulo);
    }
    
    ((XYLineAndShapeRenderer) grafica.getXYPlot().getRenderer(ngrafica)).setSeriesShapesVisible(0, visible);
}

/**
 * Cambia el grosor de una función.
 * @param ngrafica Indica la función a la que se le aplicará el rango (la primera es la 0)
 * @param grosor Representa el grosor con el que se representará la función.
 */

public void fijaGrosor(int ngrafica, float grosor) {
    grafica.getXYPlot().getRenderer(ngrafica).setSeriesStroke(0,new BasicStroke(grosor));  
}

/**
 * Cambia el color de una función.
 * @param ngrafica Indica la función a la que se le aplicará el rango (la primera es la 0)
 * @param color Representa el color con el que se representará la función.
 */

public void fijaColor(int ngrafica, Color color) {
    grafica.getXYPlot().getRenderer(ngrafica).setSeriesPaint(0, color);
}

/**
 * Añade una nueva función a la gráfica.
 * @param funcion Conjunto de puntos que determina la función que se quiere añadir.
 * @param nombre_funcion Nombre que identifica a la función a añadir.
 */

public void agregarGrafica(Point2D[] funcion, String nombre_funcion) {
    agregarGrafica(funcion,nombre_funcion,Color.RED,1f);
}

/**
 * Añade una nueva función a la gráfica.
 * @param funcion Conjunto de puntos que determina la función que se quiere añadir.
 * @param nombre_funcion Nombre que identifica a la función a añadir.
 * @param color_funcion Color de la función que se quiere añadir.
 * @param grosor Grosor de la función que se quiere añadir.
 */

public void agregarGrafica(Point2D[] funcion, String nombre_funcion, Color color_funcion,float grosor) {
    
    XYSeries dataset = new XYSeries(nombre_funcion);
    XYSeriesCollection series_datos = null;
    
    XYPlot plot = grafica.getXYPlot();

    for(int i = 0 ; i < funcion.length ; i++) {
        dataset.add(funcion[i].getX(),funcion[i].getY());
    }

    
    series_datos = new XYSeriesCollection(dataset);
    
    plot.setDataset(n_series,series_datos);
    plot.mapDatasetToRangeAxis(n_series, 0);
    plot.mapDatasetToDomainAxis(n_series, 0);
    plot.setRenderer(n_series,new XYLineAndShapeRenderer());
    
    plot.getRenderer(n_series).setSeriesPaint(0,color_funcion);
    plot.getRenderer(n_series).setSeriesStroke(0,new BasicStroke(grosor));  
   ((XYLineAndShapeRenderer) plot.getRenderer(n_series)).setSeriesShapesVisible(0,false);
    
    n_series++;
}

/**
 * Asigna un eje a una determinada función de la gráfica.
 * @param eje Representa al eje que se va a asignar.
 * @param n_grafica Indica la función a la que se le aplicará el rango (la primera es la 0)
 * @param ejeXY Indica el eje sobre el que se aplicará el rango (0 = Eje X, en otro caso = Eje Y) 
 */

public void asignarEje(NumberAxis eje, int n_grafica, int ejeXY) {
    if(ejeXY == 0) {
        asignarEjeX(eje,n_grafica);
    }
    else {
        asignarEjeY(eje,n_grafica);
    }
}

/**
 * Asigna un eje de abcisas a una determinada función de la gráfica.
 * @param eje Representa al eje que se va a asignar.
 * @param n_grafica Indica la función a la que se le aplicará el rango (la primera es la 0)
 */

private void asignarEjeX(NumberAxis eje, int n_grafica) {
    XYPlot plot = grafica.getXYPlot();
    
    plot.setDomainAxis(1,eje);
    
    plot.mapDatasetToDomainAxis(n_grafica,1);    
}


/**
 * Asigna un eje de ordenadas a una determinada función de la gráfica.
 * @param eje Representa al eje que se va a asignar.
 * @param n_grafica Indica la función a la que se le aplicará el rango (la primera es la 0)
 */

private void asignarEjeY(NumberAxis eje, int n_grafica) {
    XYPlot plot = grafica.getXYPlot();
    
    plot.setRangeAxis(1, eje);  
    
    plot.mapDatasetToRangeAxis(n_grafica,1);
}

/**
 * Fija el fondo de la gráfica de un determinado color.
 * @param color Color con el que se pintará el fondo de la gráfica.
 */

public void fijaFondo(Color color) {
    ((XYPlot) grafica.getPlot()).setBackgroundPaint(color);
}

/**
 * Restaura el fondo por defecto de la gráfica.
 */

public void restableceFondo() {
    ((XYPlot) grafica.getPlot()).setBackgroundPaint(fondo_defecto);
}

/**
 * Fija el rango de valores que se visualiza del eje de ordenadas para una gráfica concreta.
 * @param rinferior Representa el límite inferior.
 * @param rsuperior Representa el límite superior.
 * @param n_grafica Indica la función a la que se le aplicará el rango (la primera es la 0)
 */

private void fijaRangoY(double inferior, double superior,int n_grafica) {
    grafica.getXYPlot().getRangeAxisForDataset(n_grafica).setAutoRange(false);
    grafica.getXYPlot().getRangeAxisForDataset(n_grafica).setRange(inferior,superior);
}

/**
 * Fija el rango de valores que se visualiza del eje de abcisas para una gráfica concreta.
 * @param rinferior Representa el límite inferior.
 * @param rsuperior Representa el límite superior.
 * @param n_grafica Indica la función a la que se le aplicará el rango (la primera es la 0)
 */

private void fijaRangoX(double inferior, double superior, int n_grafica) {
    grafica.getXYPlot().getDomainAxisForDataset(n_grafica).setAutoRange(false);
    grafica.getXYPlot().getDomainAxisForDataset(n_grafica).setRange(inferior,superior);
}

/**
 * Elimina el rango de valores sobre el eje de ordenadas (Es decir, el eje podrá variar su rango dinámicamente dependiendo de
 * la función que se introduzca)
 * @param n_grafica Indica la función a la que se le aplicará el rango (la primera es la 0)
 */

private void quitaRangoY(int n_grafica) {
    grafica.getXYPlot().getRangeAxisForDataset(n_grafica).setAutoRange(true);
}

/**
 * Elimina el rango de valores sobre el eje de abcisas (Es decir, el eje podrá variar su rango dinámicamente dependiendo de
 * la función que se introduzca)
 * @param n_grafica Indica la función a la que se le aplicará el rango (la primera es la 0)
 */


private void quitaRangoX(int n_grafica) {
    grafica.getXYPlot().getDomainAxisForDataset(n_grafica).setAutoRange(true);
}

/**
 * Construye la gráfica a partir de los datos pasados como argumento: función, nombre de la gráfica, nombre de la función, Leyendas
 * para los ejes y opciones (mostrar leyenda, color y grosor de la función)
 * @param funcion Conjunto de puntos que determina la función que se quiere representar.
 * @param nombre_grafica Nombre que identifica a la gráfica.
 * @param nombre_funcion Nombre que identifica a la función dentro de la gráfica (puede haber más de una función).
 * @param etiqueta_X Leyenda del eje X
 * @param etiqueta_Y Leyenda del eje Y
 * @param mostrar_leyenda Indica si se quiere o no visualizar la leyenda de la gráfica.
 * @param color_funcion Indica el color con el que se quiere representar la función.
 * @param grosor Indica el grosor de linea con el que se quiere representar la función.
 */

private void generaGrafica(Point2D[] funcion,String nombre_grafica, String nombre_funcion, String etiqueta_X, String etiqueta_Y, boolean mostrar_leyenda, Color color_funcion, float grosor_funcion) {

    XYSeries dataset = new XYSeries(nombre_funcion);
    XYSeriesCollection series_datos = null;

    n_series = 0;
    
    for(int i = 0 ; i < funcion.length ; i++) {
        dataset.add(funcion[i].getX(),funcion[i].getY());
    }

    series_datos = new XYSeriesCollection(dataset);
    
    n_series++;

    grafica = ChartFactory.createXYLineChart(nombre_grafica,
    etiqueta_X,
    etiqueta_Y,
    series_datos,
    PlotOrientation.VERTICAL,
    mostrar_leyenda,
    true,
    false
    );
    
    fondo_defecto = ((XYPlot) grafica.getPlot()).getBackgroundPaint();


    grafica.getXYPlot().getRenderer(0).setSeriesPaint(0, color_funcion);

}

/**
 * Consulta el número de funciones contenidas en la gráfica.
 * @return Un entero con el número de funciones contenidas en la gráfica.
 */
public int obtenerNumeroGraficas() {
    return n_series;
}

/**
 * Función que pinta las funciones contenidas en la gráfica en pantalla.
 * @param g Lienzo donde se representarán las funciones
 * @param area �?rea donde se dibujarán las funciones
 */

public void pintar(Graphics g, Rectangle area) {
    grafica.draw((Graphics2D) g,area);
}


}
