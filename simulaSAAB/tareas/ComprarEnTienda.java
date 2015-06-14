package simulaSAAB.tareas;

import java.util.Iterator;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.random.RandomHelper;
import simulaSAAB.agentes.Consumidor;
import simulaSAAB.agentes.Tienda;
import simulaSAAB.comunicacion.OrdenDePedido;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.contextos.SaabContextBuilder;
import simulaSAAB.global.persistencia.AgentTrackObservable;

public class ComprarEnTienda implements SistemaActividadHumana<Consumidor> {
	
	private Double distanciaBusqueda;
	
	private Moverse moverse;
	
	private Iterator<Tienda> tiendasCercanas;
	
	private Tienda destino;
	
	private Coordinate origen;
	
	private String Estado;
	
	private int paso;
	
	public CompraTrack OBSERVABLE;
	
	/**
	 * Constructor
	 */
	public ComprarEnTienda() {
		
		distanciaBusqueda= new Double(10);
	}

	@Override
	public void secuenciaPrincipalDeAcciones(Consumidor actor) {
		
		if(this.Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){			
			
			Estado	=EstadosActividad.RUNNING.toString();
			paso	=1;
			
			Envelope actorEnvelope 	= SaabContextBuilder.SAABGeography.getGeometry(actor).getEnvelopeInternal();
			actorEnvelope.expandBy(distanciaBusqueda);
			
			tiendasCercanas = SaabContextBuilder.SAABGeography.queryInexact(actorEnvelope, Tienda.class).iterator();
			
			
		}
		else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){
			
			switch(paso){
			
			case 1:
				
				
				/*
				 * Escoge la primera tienda del iterador
				 * 
				 * TODO Actuar como agente inteligente escogiendo la tienda que mayores benmeficios le ofrezca.
				 */
				if(tiendasCercanas.hasNext()){
					
					Tienda t = tiendasCercanas.next();					
					Coordinate destino = t.getGeometria().getCoordinate();
					moverse = new Moverse(destino);
					
					this.destino 	= t;
					this.origen		= SaabContextBuilder.SAABGeography.getGeometry(actor).getCoordinate();
					paso++;
					
				}else{
					/*
					 * Reinicia el estado para que el "Envelope" del agente sea extendido a un rango mas grande
					 * y encuentre una tienda cercana
					 */
					distanciaBusqueda +=5;
					this.Estado = EstadosActividad.READY.toString();
				}
				
			break;			
			case 2:				
				moverse.secuenciaPrincipalDeAcciones(actor);
				
				if(moverse.getEstado().equalsIgnoreCase(EstadosActividad.DONE.toString())){
					paso++;
				}
				
			break;			
			case 3:
				//TODO Actuar como agente inteligente escogiendo los productos que necesita
				
				String producto = "Cebolla de Bulbo";
				Double cantidad = RandomHelper.nextDoubleFromTo(1, 10);
				
				this.destino.venderProducto(producto, cantidad);
				//Actualiza el observador
				OBSERVABLE = new CompraTrack();
				OBSERVABLE.compraEfectuada(producto, cantidad);
				paso++;
				
			break;
			case 4:
				/*
				 * devuelve al actor a su lugar de origen.
				 * 
				 * TODO el agente vuelve  de un salto, Deberia moverse de vuelta progresivamente
				 */
				Coordinate c = SaabContextBuilder.SAABGeography.getGeometry(actor).getCoordinate();
				c.x = this.origen.x;
				c.y = this.origen.y;
				SaabContextBuilder.SAABGeography.move(actor, actor.getGeometria());
			break;
			default:								
				this.Estado ="DONE";
			}
			
				
		}
		else if(this.Estado.equalsIgnoreCase(EstadosActividad.DONE.toString())){			
			//fija estado del actor en IDLE
			actor.setEstado("IDLE");				
		}	
		
		
	}

	@Override
	public SistemaActividadHumana getInstance() {
		
		return new ComprarEnTienda();
	}

	@Override
	public int getPaso() {
		
		return paso;
	}

	@Override
	public String getEstado() {
		
		return Estado;
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
	
	public class CompraTrack extends AgentTrackObservable{
		
		private final Tienda Tienda;
		private Double tick;		
		private String vendedorID;
		private String producto;
		private Double precioUnitario;
		private Double cantidad;
		
		
		/**
		 * Constructor
		 */
		public CompraTrack(){
			super();
			this.Tienda 	= ComprarEnTienda.this.destino;
			this.vendedorID	= Tienda.getPropietario().toString();			
		}
		
		/**
		 * Actualiza los valores y reporta a los observadores
		 * 
		 * @param producto nombre del producto comprado
		 * @param precioUnitario precio unitario del producto en la tienda
		 * @param cantidad cantidad comprada
		 */
		public void compraEfectuada(String producto, double cantidad){
			
			this.tick			= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
			this.producto 		= producto;
			this.precioUnitario	= this.Tienda.consultarPrecioUnitario(producto).getCantidad();
			
			super.setChanged();
			super.notifyObservers(this);
		}
		
		@Override
		public String dataLineString(String separador) {
			
			return tick+separador+Tienda.toString()+separador+vendedorID+separador+producto+separador+precioUnitario+separador+cantidad+separador;
		}

		@Override
		public String dataLineStringHeader(String separador) {
			
			return "Tick"+separador+"Tienda"+separador+"Vendedor_ID"+separador+"Producto"+separador+"Precio_Unitario"+separador+"cantidad"+separador;
		}
		
		
	}

}
