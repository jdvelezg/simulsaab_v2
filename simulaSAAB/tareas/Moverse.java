/**
 * Clase: Moverse.
 * 
 * Representea un sistema de actividad humana, usado para deslazarse de un lugar a otro.
 * Representa esta acción graficamente dibujando sobre una proyeccion GIS los pasos que le toma a un objeto movil llegar de un punto del mapa a otro.
 * Para fijar su costo final, usa pesos sobre los nodos usados en el movimiento, registrados en el contexto Junctions y su respectiva proyeccion Network.
 * Las unidades de costo se fijan por unidades según el tipo de vía usada por el objeto.
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

public class Moverse implements SistemaActividadHumana<ObjetoMovil> {
	
	private static Logger LOGGER = Logger.getLogger(Moverse.class.getName());
	
	private static String ENUNCIADO = "Sistema de actividad para mover un objeto Geometrico de un punto origen a un punto destino sobre una geografia";
	
	private Proposito Proposito;	
	
	private Geography<Object> SAABGeography;
	
	private Context<Junction> JunctionsContext;
	
	private Network<Junction> JunctionsNetwork;
	
	private Coordinate destino;
	
	private String Estado;
	
	private int paso;
	
	private Double Costo = new Double(0);
	
	/**
	 * Constructor
	 */
	public Moverse() {
		
		setProposito();
		SAABGeography	= SaabContextBuilder.SAABGeography;
		JunctionsContext= SaabContextBuilder.JunctionsContext;
		JunctionsNetwork= SaabContextBuilder.RoadNetwork;
		
	}
	
	
	/**
	 * Constructor
	 * @param destino Punto destino del movimiento
	 */
	public Moverse(Coordinate destino){
		
		setProposito();		
		this.destino	= destino;
		SAABGeography	= SaabContextBuilder.SAABGeography;
		JunctionsContext= SaabContextBuilder.JunctionsContext;
		JunctionsNetwork= SaabContextBuilder.RoadNetwork;
		
		Estado	= EstadosActividad.READY.toString();
	}

	@Override
	public void secuenciaPrincipalDeAcciones(ObjetoMovil actor) {
		
		if(this.Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){
			
			//obtiene el contexto y la red de movimiento.
			
			this.Estado	=EstadosActividad.RUNNING.toString();
			this.paso	=1;
			//LOGGER.log(Level.INFO, this.toString() + " Esta listo para iniciar. Actor: " + actor.toString());
		}
		else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){		
			
			moveSteps(destino,actor);			
				
		}
		
	}

	@Override
	public SistemaActividadHumana getInstance() {
		// TODO Auto-generated method stub
		return new Moverse(this.destino);
	}
	
	/**
	 * Mueve el objeto de su origen a su destino sin seguir una vía determinada.
	 * Usado apra emular el movimiento en terrenos destapados, no definidos
	 * @param destino Coordenada de destino
	 * @param actor Objeto movil
	 */
	private void moveSteps(Coordinate destino, ObjetoMovil actor ){
		
		Double	Velocidad 	= new Double(1);		
		Coordinate origen	= actor.getGeometria().getCoordinate();
		
		if(origen.distance(destino)!=0){
			
			if(destino.x-origen.x>Velocidad){			
				origen.x 	+=Velocidad;						
			}else{
				origen.x = destino.x;
			}
			
			if(destino.y-origen.y>Velocidad){			
				origen.y 	+=Velocidad;						
			}else{
				origen.y = destino.y;
			}
			
			SAABGeography.move(actor,actor.getGeometria());	
			this.Costo +=12;
		}else{
			this.Estado = EstadosActividad.DONE.toString();
		}
	}
	
	
	/**getters & setters **/
	
	private void setProposito(){
		this.Proposito = new Proposito("Mover un objeto geometrico de un punto origen a un punto destino");
	}
	
	@Override
	public int getPaso() {
		// TODO Auto-generated method stub
		return this.paso;
	}

	@Override
	public String getEstado() {
		// TODO Auto-generated method stub
		return this.Estado;
	}

	@Override
	public String getEnunciado() {
		// TODO Auto-generated method stub
		return this.ENUNCIADO;
	}

	@Override
	public Proposito getProposito() {
		// TODO Auto-generated method stub
		return this.Proposito;
	}
	
	public Geography<Object> getSAABGeography() {
		return SAABGeography;
	}

	public void setSAABGeography(Geography<Object> sAABGeography) {
		SAABGeography = sAABGeography;
	}

	public Coordinate getDestino() {
		return destino;
	}

	public void setDestino(Coordinate destino) {
		destino = destino;
	}	
	

}
