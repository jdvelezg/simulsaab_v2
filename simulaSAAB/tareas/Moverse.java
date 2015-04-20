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
import simulaSAAB.contextos.Junction;
import simulaSAAB.contextos.ObjetoMovil;
import simulaSAAB.contextos.SaabContextBuilder;

public class Moverse implements SistemaActividadHumana<ObjetoMovil> {
	
	private static Logger LOGGER = Logger.getLogger(Moverse.class.getName());
	
	private static String ENUNCIADO = "Sistema de actividad para mover un objeto Geometrico de un punto origen a un punto destino sobre una geografia";
	
	private Proposito Proposito;	
	
	private Geography<Object> SAABGeography;
	
	private Context<Object> JunctionsContext;
	
	private Network<Object> JunctionsNetwork;
	
	private List<RepastEdge<Object>> Path;
	
	private boolean Forward;
	
	private Geometry Destino;
	
	private String Estado;
	
	private int paso;
	
	private int Costo;
	
	/**
	 * Constructor
	 */
	public Moverse() {
		
		setProposito();
		SAABGeography	= SaabContextBuilder.SAABGeography;
		JunctionsContext= SaabContextBuilder.JunctionsContext;
		JunctionsNetwork= SaabContextBuilder.JunctionsNetwork;		
	}
	
	
	/**
	 * Constructor
	 * @param destino Punto destino del movimiento
	 */
	public Moverse(Geometry destino){
		
		setProposito();		
		Destino			= destino;
		SAABGeography	= SaabContextBuilder.SAABGeography;
		JunctionsContext= SaabContextBuilder.JunctionsContext;
		JunctionsNetwork= SaabContextBuilder.JunctionsNetwork;
		
		Estado	= EstadosActividad.READY.toString();
	}
	
	public Moverse(List<RepastEdge<Object>> path, boolean forward){
		
		setProposito();		
		this.Path		= path;
		this.Forward	= forward;
		SAABGeography	= SaabContextBuilder.SAABGeography;
		JunctionsContext= SaabContextBuilder.JunctionsContext;
		JunctionsNetwork= SaabContextBuilder.JunctionsNetwork;
		
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
			
			/**
			 * Mediante el switch se controla la ejecución parcial del MPA.
			 * Por motivos de visualización se puede querer que una actividad sea ejecutada
			 * en diferentes ciclos de tiempo. Cada case controla los pasos que quieren ser
			 * desplegados en cada uno, hasta llegar al final.
			 */
			switch(this.paso){
			case 1:
				
								
				
			
				
				paso ++;
				break;
			case 2:				
				
				
				
				paso ++;
				break;
			case 3: 				
				
				
				paso++;
				break;
			default:
				
				
			}
			
				
		}
		
	}

	@Override
	public SistemaActividadHumana getInstance() {
		// TODO Auto-generated method stub
		return new Moverse(this.Destino);
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
		
		if(origen.distance(destino)==0){
			
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
			this.Costo +=4;
		}		
	}
	
	/**
	 * Calcula el camino más corto entre el origen y el destino, usando la proyeccion de Junctions
	 */
	private void calculePath(GeografiaFija destino, ObjetoMovil actor){
		
		ShortestPath helper 	= new ShortestPath(JunctionsNetwork);
		
		Junction source = new Junction("origen","temporal");
		Junction target = new Junction("destino","temporal");
		
		this.JunctionsContext.add(source);
		this.JunctionsContext.add(target);
		
		this.Path = helper.getPath(source, target);		
	}
	
	
	/**getters & setters **/
	
	private void setProposito(){
		this.Proposito = new Proposito("Mover un objeto geometrico de un punto origen a un punto destino");
	}
	
	@Override
	public int getPaso() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getEstado() {
		// TODO Auto-generated method stub
		return null;
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

	public Geometry getDestino() {
		return Destino;
	}

	public void setDestino(Geometry destino) {
		Destino = destino;
	}	
	

}
