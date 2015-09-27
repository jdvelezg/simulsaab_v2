/**
 * Clase: Moverse.
 * 
 * Representea un sistema de actividad humana, usado para deslazarse de un lugar a otro.
 * Representa esta acciÃ³n graficamente dibujando sobre una proyeccion GIS los pasos que le toma a un objeto movil llegar de un punto del mapa a otro.
 * Para fijar su costo final, usa pesos sobre los nodos usados en el movimiento, registrados en el contexto Junctions y su respectiva proyeccion Network.
 * Las unidades de costo se fijan por unidades segÃºn el tipo de vÃ­a usada por el objeto.
 * 	- Via principal: 		1 Unidad.
 * 	- Via secundaria:  		2 Unidades.
 *  - Sin via (Destapado):	4 Unidades.
 */
package simulaSAAB.tareas;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

import repast.simphony.context.Context;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.space.graph.ShortestPath;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.contextos.GeografiaFija;
import simulaSAAB.contextos.environment.Junction;
import simulaSAAB.contextos.environment.Route;
import simulaSAAB.contextos.ObjetoMovil;
import simulaSAAB.contextos.SaabContextBuilder;
import simulaSAAB.global.VariablesGlobales;
/**
 * Representa el movimiento de un objeto geométrico de un punto de origen a un punto de destino sobre una geografía
 * @author lfgomezm
 *
 */
public class Moverse implements SistemaActividadHumana<ObjetoMovil> {
	/**
	 * Registro de la clase usado para depuración <code>Debugging</code>
	 */
	private static Logger LOGGER = Logger.getLogger(Moverse.class.getName());
	/**
	 * Establece el enunciado de la tarea
	 */
	private static String ENUNCIADO = "Sistema de actividad para mover un objeto geométrico de un punto origen a un punto destino sobre una geografía";
	/**
	 * Propósito de la tarea
	 */
	private Proposito Proposito;	
	/**
	 * Coordenada del punto de destino
	 */
	private Coordinate destino;
	/**
	 * Estado actual de la tarea
	 */
	private String Estado;
	/**
	 * Paso actual de la tarea
	 */
	private int paso;
	/**
	 * Velocidad con la que se mueve el objeto dentro de la geografía
	 */
	private double velocidad;
	
	private Double Costo = new Double(0);
	
	/**
	 * Constructor
	 */
	public Moverse() {		
		setProposito();		
	}
		
	/**
	 * Constructor
	 * @param destino  coordinate, destino del movimiento
	 */
	public Moverse(Coordinate destino){
		
		setProposito();		
		setDestino(destino);		
	}

	@Override
	public synchronized void secuenciaPrincipalDeAcciones(ObjetoMovil actor) {
		
		if(this.Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){		
			
			Coordinate origen	= SaabContextBuilder.SAABGeography.getGeometry(actor).getCoordinate();
			double[] returnVals	= new double[2];
			
			Route.distance(origen, this.destino, returnVals);	
			this.velocidad 		= returnVals[0]/VariablesGlobales.TICKS_UNDIA_DEMORA_MOVIMIENTO;
			this.Costo			= returnVals[0]/2*VariablesGlobales.COSTO_PROMEDIO_TRANSPORTE_CARGA_POR_METRO;
			this.Estado			= EstadosActividad.RUNNING.toString();
		}
		else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){			
			/*
			 *Mueve el agente un paso cada vez
			 */
			Coordinate origen	= SaabContextBuilder.SAABGeography.getGeometry(actor).getCoordinate();
			if(origen.distance(destino)!=0){
				//this.LOGGER.info("destino "+destino+" origen "+origen);
				moveSteps(destino,actor);
			}else{
				this.Estado = EstadosActividad.DONE.toString();	
			}
							
		}		
	}

	@Override
	public SistemaActividadHumana getInstance() {
		// TODO Auto-generated method stub
		return new Moverse(this.destino);
	}
	
	/**
	 * Mueve el objeto de su origen a su destino sin seguir una ruta determinada.
	 * Usado para emular el movimiento en terrenos destapados, no definidos
	 * @param destino coordenada de destino
	 * @param actor objeto movil, agente
	 */
	private void moveSteps(Coordinate destFinal, ObjetoMovil actor){		
		
		Coordinate origen	= SaabContextBuilder.SAABGeography.getGeometry(actor).getCoordinate();
		double[] returnVals	= new double[2];
		Route.distance(origen, this.destino, returnVals);	
		
		//if(origen.distance(destFinal)<=velocidad){
		if(returnVals[0]<velocidad){	
			origen.x = destino.x;
			origen.y = destino.y;			
			this.Estado = EstadosActividad.DONE.toString();			
		}else{			
			//this.LOGGER.info("distancia:  "+returnVals[0]);
			SaabContextBuilder.SAABGeography.moveByVector(actor, velocidad, returnVals[1]);
			
		}
	}
	
	/**
	 * Asigna el propósito a la tarea
	 */
	private void setProposito(){
		this.Proposito = new Proposito("Mover un objeto geometrico de un punto origen a un punto destino");
	}
	
	@Override
	public int getPaso() {		
		return this.paso;
	}

	@Override
	public String getEstado() {		
		return this.Estado;
	}
		
	@Override
	public double getCosto() {
		return Costo;
	}

	@Override
	public String getEnunciado() {		
		return this.ENUNCIADO;
	}

	@Override
	public Proposito getProposito() {		
		return this.Proposito;
	}	
	/**
	 * Devuelve el punto de destino
	 * @return destino coordenada del punto de destino 
	 */
	public Coordinate getDestino() {
		return destino;
	}
	/**
	 * Asigna el punto de destino y actualiza el estado de la tarea
	 * @param destino coordenada del punto de destino
	 */
	public void setDestino(Coordinate destino) {
		this.destino = destino;
		Estado	= EstadosActividad.READY.toString();
	}


	@Override
	public int getId() {		
		return 0;
	}	
	

}