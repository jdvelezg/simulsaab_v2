package simulaSAAB.tareas;

import java.util.logging.Logger;

import com.vividsolutions.jts.geom.Geometry;

import repast.simphony.context.Context;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.graph.Network;
import simulaSAAB.comunicacion.Proposito;

public class Moverse implements SistemaActividadHumana<Geometry> {
	
	private static Logger LOGGER = Logger.getLogger(Moverse.class.getName());
	
	private static String ENUNCIADO = "Sistema de actividad para mover un objeto Geometrico de un punto origen a un punto destino sobre una geografia";
	
	private Proposito Proposito;	
	
	private Geography<Object> SAABGeography;
	
	private Geometry Destino;
	
	private String Estado;
	
	private int paso;
	
	/**
	 * Constructor
	 */
	public Moverse() {
		
		setProposito();
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * Constructor
	 * @param destino Punto destino del movimiento
	 */
	public Moverse(Geometry destino){
		
		setProposito();
		this.Destino	= destino;
	}
	

	@Override
	public void secuenciaPrincipalDeAcciones(Geometry actor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SistemaActividadHumana getInstance() {
		// TODO Auto-generated method stub
		return new Moverse(this.Destino);
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
		return null;
	}

	@Override
	public Proposito getProposito() {
		// TODO Auto-generated method stub
		return null;
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
