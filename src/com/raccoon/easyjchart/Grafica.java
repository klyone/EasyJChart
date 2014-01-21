package com.raccoon.easyjchart;


/**
 * Versión 1.0 de la clase Grafica.java
 * @author Miguel Jiménez López
 * @date 31/5/2012
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
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYImageAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
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
 * Buffer to load the image of the background of the chart
 */
private Image backGroundImage;

/**
 * Construtor de la clase Grafica. Permite inicializar la gráfica con una determinada función.
 * @param funcion Conjunto de puntos que determina la función que se quiere representar.
 * @param nombre_grafica Nombre que identifica a la gráfica.
 * @param nombre_funcion Nombre que identifica a la función dentro de la gráfica (puede haber más de una función).
 * @param etiqueta_X Leyenda del eje X
 * @param etiqueta_Y Leyenda del eje Y
 */

public Grafica(Point2D[] funcion,String nombre_grafica, String nombre_funcion, String etiqueta_X, String etiqueta_Y) {
    generaGrafica(funcion,nombre_grafica,nombre_funcion,etiqueta_X,etiqueta_Y,false,Color.RED,1f,true); 
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
    generaGrafica(funcion,nombre_grafica,nombre_funcion,etiqueta_X,etiqueta_Y,mostrar_leyenda,Color.RED,1f,true); 
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
 * @param isContinuous True for continous function. False for discrete function.
 */

public Grafica(Point2D[] funcion,String nombre_grafica, String nombre_funcion, String etiqueta_X, String etiqueta_Y,boolean mostrar_leyenda,Color color_funcion, float grosor, boolean isContinuous) {
    generaGrafica(funcion,nombre_grafica,nombre_funcion,etiqueta_X,etiqueta_Y,mostrar_leyenda,color_funcion,grosor, isContinuous);    
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
        setXRange(rinferior,rsuperior,n_grafica);
    else
        setYRange(rinferior,rsuperior,n_grafica);
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

public void visualizaMuestras(int ngrafica, boolean visible, double tam) {
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
    agregarGrafica(funcion,nombre_funcion,Color.RED,1f,true);
}

/**
 * Añade una nueva función a la gráfica.
 * @param funcion Conjunto de puntos que determina la función que se quiere añadir.
 * @param nombre_funcion Nombre que identifica a la función a añadir.
 * @param color_funcion Color de la función que se quiere añadir.
 * @param grosor Grosor de la función que se quiere añadir.
 * @param isContinuous True for continous function. False for discrete function.
 */

public void agregarGrafica(Point2D[] funcion, String nombre_funcion, Color color_funcion,float grosor, boolean isContinuous) {
    
    XYSeries dataset = new XYSeries(nombre_funcion,isContinuous);
    XYSeriesCollection series_datos = null;
    
    XYPlot plot = grafica.getXYPlot();

    for(int i = 0 ; i < funcion.length ; i++) {
        dataset.add(funcion[i].getX(),funcion[i].getY());
        if(!isContinuous){
        	dataset.add(Double.NaN, Double.NaN);
        }
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
 * Set the Y range of a concrete plot
 * @param inferior  New inferior limit of the Y axis
 * @param superior New superior limit of the Y axis 
 * @param n_grafica Index of the plot. (The first index is 0)
 */
public void setYRange(double inferior, double superior,int n_grafica) {
    grafica.getXYPlot().getRangeAxisForDataset(n_grafica).setAutoRange(false);
    grafica.getXYPlot().getRangeAxisForDataset(n_grafica).setRange(inferior,superior);
}


/**
 * Set the X range of a concrete plot
 * @param inferior  New inferior limit of the X axis
 * @param superior New superior limit of the X axis 
 * @param n_grafica Index of the plot. (The first index is 0)
 */
public void setXRange(double inferior, double superior, int nPlot) {
    grafica.getXYPlot().getDomainAxisForDataset(nPlot).setAutoRange(false);
    grafica.getXYPlot().getDomainAxisForDataset(nPlot).setRange(inferior,superior);
}

/**
 * Set the X range of all plots in the chart
 * @param inferior  New inferior limit of the X axis
 * @param superior New superior limit of the X axis 
 */
public void setXRange(int inferior, int superior){
	int domainsNum  = grafica.getXYPlot().getDomainAxisCount();
	for(int i = 0; i < domainsNum; i++){
		setXRange(inferior, superior, i);
	}
}

/**
 * Set the Y range of all plots in the chart
 * @param inferior  New inferior limit of the Y axis
 * @param superior New superior limit of the Y axis 
 */
public void setYRange(int inferior, int superior){
	int domainsNum  = grafica.getXYPlot().getDomainAxisCount();
	for(int i = 0; i < domainsNum; i++){
		setYRange(inferior, superior, i);
	}
}

/**
 * Set the X-Y range of all plots in the chart
 * @param infXLimit New inferior X limit
 * @param supXLimit New superior X limit
 * @param infYLimit New inferior Y limit
 * @param supYLimit New superior Y limit
 */
public void setRangeAxis(int infXLimit, int supXLimit, int infYLimit, int supYLimit){
	setXRange(infXLimit, supXLimit);
	setYRange(infYLimit, supYLimit);
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
 * @param isContinuous True for continous function. False for discrete function.
 */

private void generaGrafica(Point2D[] funcion,String nombre_grafica, String nombre_funcion, String etiqueta_X, String etiqueta_Y, boolean mostrar_leyenda, Color color_funcion, float grosor_funcion, boolean isContinuous) {
	
    XYSeries dataset = new XYSeries(nombre_funcion, isContinuous);
    XYSeriesCollection series_datos = null;

    n_series = 0;
    
    for(int i = 0 ; i < funcion.length ; i++) {
        dataset.add(funcion[i].getX(),funcion[i].getY());
        if(!isContinuous){
        	dataset.add(Double.NaN, Double.NaN);
        }
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
    grafica.getXYPlot().getRenderer(0).setSeriesStroke(0,new BasicStroke(grosor_funcion)); 
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

public XYItemRenderer getFunction(int i){
	return this.grafica.getXYPlot().getRenderer(i);
}

/**
 * Replaces the plot of the indicated index.
 * @param nPlot Index
 * @param function Function to represent
 * @param functionName Name of the function
 * @param functionColor Color of the function
 * @param stroke Stroke of the function
 * @param isContinuous True if function is continuous. False if is discrete.
 */
public void replacePlot(int nPlot, Point2D[] function, String functionName, Color functionColor,float stroke, boolean isContinuous){
	XYSeries dataset = new XYSeries(functionName,isContinuous);
    XYSeriesCollection series_datos = null;
    
    XYPlot plot = grafica.getXYPlot();

    for(int i = 0 ; i < function.length ; i++) {
        dataset.add(function[i].getX(),function[i].getY());
        if(!isContinuous){
        	dataset.add(Double.NaN, Double.NaN);
        }
    }

    
    series_datos = new XYSeriesCollection(dataset);
    
    plot.setDataset(nPlot,series_datos);
    plot.mapDatasetToRangeAxis(nPlot, 0);
    plot.mapDatasetToDomainAxis(nPlot, 0);
    plot.setRenderer(nPlot,new XYLineAndShapeRenderer());
    
    plot.getRenderer(nPlot).setSeriesPaint(0,functionColor);
    plot.getRenderer(nPlot).setSeriesStroke(0,new BasicStroke(stroke));  
   ((XYLineAndShapeRenderer) plot.getRenderer(nPlot)).setSeriesShapesVisible(0,false);
}

public void setBackGroundImage(String fileName, float alpha) throws IOException{
	this.backGroundImage = ImageIO.read(new File(fileName));	
	grafica.getPlot().setBackgroundImage(backGroundImage);
	grafica.getPlot().setBackgroundImageAlpha(alpha);
}

public void setBackGroundImage(Image img, float alpha) throws IOException{
	
	this.backGroundImage = img;
	
	grafica.getPlot().setBackgroundImage(backGroundImage);
	grafica.getPlot().setBackgroundImageAlpha(alpha);
}

/**
 * Loads and image in a especific point of the chart, and allows to rotate it
 * @param image Image to be loaded
 * @param xCoordinate X Coordinate
 * @param yCoordinate Y Coordinate
 * @param degrees Degrees to rotate
 */
public void setImage(BufferedImage image, double xCoordinate,double yCoordinate, double degrees){

    //Rotation
	AffineTransform transform = new AffineTransform();
	int h = image.getHeight();
	int w = image.getWidth();
    transform.rotate(Math.toRadians(degrees), h/2, w/2);
    AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
    image = op.filter(image, null);
	
	
    //Representation in the point
    XYAnnotation xyannotation = new XYImageAnnotation(xCoordinate, yCoordinate, image); 
	grafica.getXYPlot().addAnnotation(xyannotation); 	
}


public BufferedImage loadImage(String fileName){

    BufferedImage buff = null;
    try {
        buff = ImageIO.read(getClass().getResourceAsStream(fileName));
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return null;
    }
    return buff;

}




public Plot getPlot(){
	return grafica.getPlot();
}

}
