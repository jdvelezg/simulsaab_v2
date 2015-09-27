package simulaSAAB.tareas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.util.collections.IndexedIterable;
import simulaSAAB.agentes.Demandante;
import simulaSAAB.agentes.SiSaab;
import simulaSAAB.comunicacion.Demanda;
import simulaSAAB.comunicacion.Oferta;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.global.VariablesGlobales;
import simulaSAAB.global.persistencia.MPAConfigurado;
import simulaSAAB.global.persistencia.ProductoConfigurado;
/**
 * Representa el registro de demandas unitarias en el SISAAB
 * @author lfgomezm
 *
 */
public class RegistrarDemandaUnitaria implements
		SistemaActividadHumana<Demandante> {
	/**
	 * Registro de la clase usado para depuración <code>Debugging</code>
	 */
	private static Logger LOGGER = Logger.getLogger(RegistrarDemandaUnitaria.class.getName());
	/**
	 * Propósito de la tarea
	 */
	private final Proposito proposito;
	/**
	 * Identificador de la demanda unitaria
	 */
	private final Integer id;
	/**
	 * Enunciado de la tarea
	 */
	private final String Enunciado;
	
	private final Double CostoEjecucion;
	/**
	 * Estado actual de la tarea
	 */
	private String Estado;
	/**
	 * Paso actual de la tarea
	 */
	private int paso;
	
	protected Demanda demanda;
	
	protected double DineroInicial;
	
	protected double Tickinicial;
	
	/**
	 * Constructor
	 */
	public RegistrarDemandaUnitaria() {
		
		MPAConfigurado mpa 	=new MPAConfigurado("RegistrarDemandaUnitaria");
		
		this.id				= mpa.getId();
		this.Enunciado		= mpa.getEnunciado();
		this.proposito 		= mpa.getProposito();
		this.CostoEjecucion	= mpa.getCosto();
		
		this.paso			= 0;
		this.Estado 		= EstadosActividad.READY.toString();
		
		this.Estado	=EstadosActividad.READY.toString();
		this.paso 	= 0;
	}

	@Override
	public synchronized void secuenciaPrincipalDeAcciones(Demandante actor) {
		
		if(this.Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){
					
			this.DineroInicial 	= new Double(actor.getDinero().getCantidad()).doubleValue();			
			this.Estado			= EstadosActividad.RUNNING.toString();
			this.paso			= 1;			
			Tickinicial			= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
			
			actor.setEstado("RUNNING");
			
			//Registra la demanda
			this.demanda 	= actor.generarDemanda();
			SiSaab.registrarDemanda(demanda);	
			
		}else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){
			
			
			switch(this.paso){
			case 1:
				/**
				 * Lleva a cabo la compra una vez ha sido registrada la demanda
				 */
				List<Oferta> compra = new ArrayList<Oferta>();			
				Double presupuesto	= new Double(demanda.getPresupuesto());
				//Obtiene ofertas registradas
				List<Oferta> ofertas = SiSaab.ofertasRegistradas(demanda.getNombreproducto(),true);
				
				//filtra ofertas de interés para la demanda
				for(Oferta o: ofertas){		
					
					if(o.getPrecio() <= presupuesto && o.vigente()){					
						o.setEstado("SEPARADA");
						compra.add(o);						
						presupuesto	-= o.getPrecio();					
					}										
				}//end for
				
				
				if(compra.size()>0){//verifica que si existan ofertas disponibles para la demanda
					
					SiSaab.realizarCompra(compra, demanda);					
					paso++;	
					
				}else{//No existen ofertas para la demanda, espera a encontrar una o desiste 
					
					Double current = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
					if(current >= Tickinicial+VariablesGlobales.TICKS_VIGENCIA_DEMANDA){
						
						// cambia sus preferencias de consolidaciÃ³n a verdaderas
						actor.setIntencionConsolidacion(true);
						//Da por terminada la actividad
						this.Estado = EstadosActividad.DONE.toString();
						demanda.setVencida();
					}										
				}				
				
			break;
			case 2:
				//Espera el producto comprado
				if(actor.pedidosRecibidos())
					paso++;
				
			break;
			case 3:
				actor.setEstado("RUNNING");
				actor.gestionarPedidos();
				paso++;
				
			break;
			default:				
				this.Estado =EstadosActividad.DONE.toString();
			}
			
		}else if(this.Estado.equalsIgnoreCase(EstadosActividad.DONE.toString())){
			
			//substrae el costo de ejecuciÃ³n del MPA del dinero del agente
			actor.getDinero().subtractCantidad(CostoEjecucion);			
			//fija estado del actor en IDLE
			actor.setEstado("IDLE");
		}
		
		
	}

	@Override
	public SistemaActividadHumana getInstance() {
		// TODO Auto-generated method stub
		return new RegistrarDemandaUnitaria();
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
	public String getEnunciado() {
		return this.Enunciado;
	}

	@Override
	public Proposito getProposito() {
		return proposito;
	}
	/**
	 * Devuelve el identificador de la demanda unitaria
	 * @return id int
	 */
	public int getId() {
		return id;
	}	
	/**
	 * Devuelve el costo total de ejecución de la tarea
	 * @return costoEjecucion double
	 */
	public Double getCostoEjecucion() {
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

	@Override
	public double getCosto() {
		// TODO Auto-generated method stub
		return 0;
	}

}