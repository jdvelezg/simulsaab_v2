package simulaSAAB.tareas;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import repast.simphony.engine.environment.RunEnvironment;
import simulaSAAB.agentes.Camioneta;
import simulaSAAB.agentes.Intermediario;
import simulaSAAB.agentes.OperadorRedDemanda;
import simulaSAAB.agentes.Productor;
import simulaSAAB.agentes.Terreno;
import simulaSAAB.comunicacion.EjecutarAccionConProposito;
import simulaSAAB.comunicacion.MensajeACL;
import simulaSAAB.comunicacion.Oferta;
import simulaSAAB.comunicacion.Preposicion;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.contextos.ObjetoMovil;
import simulaSAAB.contextos.SaabContextBuilder;
import simulaSAAB.global.persistencia.MPAConfigurado;
import simulaSAAB.global.persistencia.ProductoConfigurado;
/**
 * Clase para vender productos localmente
 * 
 * @author lfgomezm
 */
public class VenderLocalmente implements SistemaActividadHumana<Productor> {
	
	private final int id;
	/**
	 * Registro de la clase usado para depuración <code>Debugging</code>
	 */
	private static Logger LOGGER = Logger.getLogger(VenderLocalmente.class.getName());
	/**
	 * Propósito de la tarea
	 */
	private final Proposito proposito;
	/**
	 * Enunciado de la tarea
	 */
	private final String Enunciado;
	/**
	 * Ubicación de la venta
	 */
	private Coordinate lugarVenta;
	
	private double CostoEjecucion;	
	/**
	 * Estado actual de la tarea
	 */
	private String Estado;
	/**
	 * Paso actual de la tarea
	 */
	private int paso;
	
	private final double Tickinicial;
	
	private double DineroInicial;
	
	private Oferta productoOfertado;
	/**
	 * Instancia del <code>MPA</code> {@link ProcesoAgenteHumano}
	 */
	private SistemaActividadHumana moverse;
	/**
	 * Tipo de transporte usado para movilizar el producto
	 */
	private ObjetoMovil transporte;
	/**
	 * Identificador de la conversación para envío de mensaje ACL
	 */
	private int idMensajeCFP;


	/**
	 * Constructor
	 */
	public VenderLocalmente() {
		
		MPAConfigurado mpa 	= new MPAConfigurado("VenderLocalmente");
		
		this.id				= mpa.getId();
		this.Enunciado		= mpa.getEnunciado();
		this.proposito 		= mpa.getProposito();
		this.CostoEjecucion	= mpa.getCosto();
		this.paso			= 0;
		this.Estado 		= EstadosActividad.READY.toString();
		
		Tickinicial	= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();	
	}

	
	@Override
	
	public void secuenciaPrincipalDeAcciones(Productor actor) {
		
		if(this.Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){
						
			DineroInicial 	= actor.getDinero().getCantidad();
			productoOfertado= actor.generarOferta();
			lugarVenta		= actor.getPuntoOferta().getRoadAccess();
			moverse 		= new Moverse(lugarVenta);
			
			//Ubica al agente en un tranporte
			transporte 		= new Camioneta();
			Point geom 		= new GeometryFactory().createPoint(SaabContextBuilder.SAABGeography.getGeometry(actor).getCoordinate());
			transporte.setGeometria(geom);
			SaabContextBuilder.SAABContext.add(transporte);
			SaabContextBuilder.SAABGeography.move(transporte, transporte.getGeometria());
			
			actor.setEstado("RUNNING");
			this.Estado = EstadosActividad.RUNNING.toString();
			this.paso	= 1;
			
		}
		else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){
			
			switch(paso){
			
			case 1: //LOGGER.log(Level.INFO,"paso1 :");
				/*
				 * Productor se mueve al centro urbano mÃ¡s cercano
				 */
				this.moverse.secuenciaPrincipalDeAcciones(transporte);
				
				Coordinate ubicacionActual = SaabContextBuilder.SAABGeography.getGeometry(transporte).getCoordinate();				
				if(ubicacionActual.distance(lugarVenta)==0){
					SaabContextBuilder.SAABContext.remove(transporte);
					this.CostoEjecucion += moverse.getCosto();
					paso++;
				}
					
								
			break;
			case 2:
				//LOGGER.log(Level.INFO,"paso2 : Envia mensaje cfp");
				/*
				 * Envia mensaje cfp
				 */
				MensajeACL mensaje 	= new MensajeACL();				
				
				int contactados = 0;
				
				if(productoOfertado != null){
					
					TransaccionComercial transaccionComercial = new TransaccionComercial(actor,null,productoOfertado.getProductos(),productoOfertado.getPrecio());								
					Preposicion contenido 	= new EjecutarAccionConProposito(transaccionComercial);  
					idMensajeCFP 			= mensaje.getConversationID();
					
					mensaje.setContent(contenido);
					mensaje.setPerformative("cfp");
					mensaje.setReply_to(actor);
					 
					MensajeACL replyWhit	= new MensajeACL(idMensajeCFP, "propose");
					replyWhit.setContent(contenido);
					replyWhit.setInReply_to(idMensajeCFP);			
					
					mensaje.setReply_with(replyWhit);
					mensaje.setSender(actor);
					
					//busca intermediarios en el centroUrbano
					Geometry puebloGeom				= actor.getPuntoOferta().getGeometria();
					Envelope envelope 				= puebloGeom.getEnvelopeInternal();
					Iterable<Object> intermediarios	= SaabContextBuilder.SAABGeography.queryInexact(envelope);
										
					for(Object o: intermediarios){
						
						if(o instanceof Intermediario){
							
							Intermediario intermediario = (Intermediario)o;
							Geometry intGeom = SaabContextBuilder.SAABGeography.getGeometry(intermediario);
							if(puebloGeom.contains(intGeom)){
								
								//personaliza el mensaje enviado
								transaccionComercial.setComprador(intermediario);
								actor.enviarMensaje(mensaje, intermediario);
								contactados++;
							}
						}			
					}//EndFor
				}//End IfOfertaNUll				
				
				if(contactados>0){
					actor.setEstado("LISTENING");
					paso++;
				}/*else
					this.LOGGER.info("No contacto ningun intermediario");*/
				
			break;
			case 3: //LOGGER.log(Level.INFO,"paso3 :");
				/*
				 * Espera respuestas
				 */
				actor.buscarRespuesta(this.idMensajeCFP);
				
				if(!(actor.getEstado().equalsIgnoreCase("LISTENING")))
					paso++;
				
			break;		
			default://LOGGER.log(Level.INFO,"paso-sale :");
				productoOfertado.setEstado("OFRECIDA");
				this.Estado =EstadosActividad.DONE.toString();
			}
		}
		else if(this.Estado.equalsIgnoreCase(EstadosActividad.DONE.toString())){
		
			//substrae el costo de ejecuciÃ³n del MPA del dinero del agente
			actor.getDinero().subtractCantidad(CostoEjecucion);
			//Calcula la utilidad obtenida al ejecutar el MPA
			//fija estado del actor en IDLE
			actor.setEstado("IDLE");			
		}
		
	}

	@Override
	public SistemaActividadHumana getInstance() {
		
		return new VenderLocalmente();
	}

	@Override
	public int getPaso() {
		
		return paso;
	}

	@Override
	public String getEstado() {
		
		return this.Estado;
	}

	@Override
	public String getEnunciado() {
		
		return Enunciado;
	}

	@Override
	public Proposito getProposito() {
		
		return proposito;
	}

	@Override
	public int getId() {
		
		return id;
	}
	@Override
	public double getCosto() {
		return CostoEjecucion;
	}
	
	@Override 
	public boolean equals(Object obj){
		
		if(obj instanceof SistemaActividadHumana){
			
			SistemaActividadHumana act = (SistemaActividadHumana)obj;			
			return this.id==act.getId();
		}else{
			return false;
		}
	}

}