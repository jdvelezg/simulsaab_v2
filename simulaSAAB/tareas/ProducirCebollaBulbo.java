package simulaSAAB.tareas;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import repast.simphony.engine.environment.RunEnvironment;
import simulaSAAB.agentes.AgenteInteligente;
import simulaSAAB.agentes.Productor;
import simulaSAAB.agentes.Terreno;
import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.comunicacion.Recurso;
import simulaSAAB.global.PropositosFactory;
import simulaSAAB.global.VariablesGlobales;
import simulaSAAB.global.persistencia.MPAConfigurado;
import simulaSAAB.global.persistencia.ProductoConfigurado;
/**
 * Representa la actividad de producción de cebolla de bulbo
 * @author lfgomezm
 *
 */
public class ProducirCebollaBulbo implements SistemaActividadHumana<Productor> {
	/**
	 * Identificador de la actividad
	 */
	private final int id;
	/**
	 * Registro de la clase usado para depuración <code>Debugging</code>
	 */
	private static Logger LOGGER = Logger.getLogger(ProducirCebollaBulbo.class.getName());
	/**
	 * Establece el propósito de la tarea
	 */
	private static Proposito proposito = new Proposito("Producir cebolla y vender el producto");
	/**
	 * Enunciado de la tarea
	 */
	private static String Enunciado;
	
	private double CostoEjecucion;
	
	private Terreno Terreno;
	/**
	 * Define el número de meses que demora un terreno en tener una cosecha
	 */
	private int tiempoCosechaTerreno;
	/**
	 * Instancia de <code>Producto</code> a producir
	 */
	private Producto cebollaBulbo;	
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
	/**
	 * Instancia del <code>MPA</code> {@link ProcesoAgenteHumano}
	 */
	private SistemaActividadHumana moverse;
	/**
	 * Instancia del <code>MPA</code> {@link ProcesoAgenteHumano}
	 */
	protected SistemaActividadHumana venderProductos;

	/**
	 * Constructor
	 */
	public ProducirCebollaBulbo() {

		MPAConfigurado mpa 	=new MPAConfigurado("ProduccionCebollaBulbo");
		
		this.id				= mpa.getId();
		this.cebollaBulbo	= new Producto(new ProductoConfigurado("Cebolla de bulbo"));
		this.Enunciado		= mpa.getEnunciado();
		this.proposito 		= mpa.getProposito();
		this.CostoEjecucion	= mpa.getCosto();
		this.paso			= 0;			
		this.Estado 		= EstadosActividad.READY.toString();
		
		Tickinicial		= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		venderProductos = new RegistrarOfertaUnitaria();
	}
	
	

	@Override
	public synchronized void secuenciaPrincipalDeAcciones(Productor actor) {
		
		if(Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){
					
			if(Terreno==null){
				Terreno					= actor.getTerrenosCultivables().get(0);
				tiempoCosechaTerreno 	= Terreno.tiempoParaCosecha();
			}					
			
			DineroInicial 	= new Double(actor.getDinero().getCantidad());			
			paso			= 1;
			Estado			= EstadosActividad.RUNNING.toString();
			
			this.CostoEjecucion		*= this.Terreno.getHectareas();
						
			actor.setEstado("RUNNING");			
		}
		else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){		
			
			switch(this.paso){
			case 1:
				//LOGGER.log(Level.INFO,this.toString()+" paso1 "+actor.toString()+": "+actor.getEstado());
				//PreparaciÃ³n del terreno implica arado, fertilizaciÃ³n
				
				this.Terreno.setEstado("Preparado");	
				
				this.paso++;				
				break;
			case 2:
				
				//LOGGER.log(Level.INFO,this.toString()+"paso2 "+actor.getEstado());
				
				this.Terreno.setEstado("Sembrado");
				
				this.paso++;				
				break;
			case 3:
				
				//LOGGER.log(Level.INFO,this.toString()+"paso3 "+actor.getEstado());
				
				this.Terreno.setEstado("Cultivo");
				
				this.paso++;				
				break;
			case 4:				
				
				//LOGGER.log(Level.INFO,this.toString()+"paso4 "+actor.getEstado());
				
				this.Terreno.setEstado("Mantenimiento");
				
				this.paso++;				
				break;
			case 5:
				
				//LOGGER.log(Level.INFO,this.toString()+"paso5 "+actor.getEstado());
				
				//espera x numero de ticks
				
				Double CurrentTick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
				
				if(CurrentTick-Tickinicial>=VariablesGlobales.TICKS_UNMES_DEMORA_MOVIMIENTO*tiempoCosechaTerreno)//de 3 a 6 meses
					this.paso++;					
				else if(Tickinicial<1000)//la primera vez se cosecha inmediatamente
					paso++;
							
				break;
			case 6:				
				//LOGGER.log(Level.INFO,"paso6 ");
				
				Recurso cosecha	= this.Terreno.cosechar(cebollaBulbo);

				//Fija costo unitario: CostoEjecucionMPAPorHectarea*#hectareasUsadas/CantidadCosechada 
				double precio 	= CostoEjecucion/cosecha.getCantidad();				
				cosecha.setCostoUnitario(precio);
				
				actor.addProducto(cosecha);
				
				this.paso++;				
				break;
			case 7:					
				
				venderProductos.secuenciaPrincipalDeAcciones(actor);
				/*
				 * Para controlar que el agente continue ProcesoHumano anidado al igual que el presente
				 * se evalua su estado, impidiendo que caiga en "IDLE" 
				 */
				if(actor.getEstado().equalsIgnoreCase("IDLE")){
					actor.setEstado("RUNNING");
				}
					
				//Si el subsistema termino continua al siguiente paso				
				if(venderProductos.getEstado().equalsIgnoreCase(EstadosActividad.DONE.toString())){			
					
					this.paso++;					
				}
					
				break;
			default:				
				//Da la actividad por terminada
				this.Estado ="DONE";				
			}
			
		}else if(this.Estado.equalsIgnoreCase(EstadosActividad.DONE.toString())){
			
			//substrae el costo de ejecuciÃ³n del MPA del dinero del agente
			actor.getDinero().subtractCantidad(CostoEjecucion);
			
			//Calcula la utilidad obtenida al ejecutar el MPA
			double valorActual		= new Double(actor.getDinero().getCantidad()).doubleValue();
			double ultimaUtilidad 	= valorActual>DineroInicial?Math.abs(valorActual-DineroInicial):0;	//valorActual-DineroInicial;		
			actor.setUltimaUtilidadObtenida(ultimaUtilidad);
			//fija estado del actor en IDLE
			actor.setEstado("IDLE");
			//LOGGER.info("termina "+actor.toString()+" "+valorActual+"-"+DineroInicial+"="+ultimaUtilidad);
		}
		
	}

	@Override
	public SistemaActividadHumana getInstance() {
		// TODO Auto-generated method stub
		return new ProducirCebollaBulbo();
	}

	@Override
	public int getPaso() {
		// TODO Auto-generated method stub
		return paso;
	}

	@Override
	public String getEstado() {
		// TODO Auto-generated method stub
		return Estado;
	}

	@Override
	public String getEnunciado() {
		// TODO Auto-generated method stub
		return Enunciado;
	}

	@Override
	public Proposito getProposito() {
		// TODO Auto-generated method stub
		return proposito;
	}
	
	public int getId() {
		return id;
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
		return this.CostoEjecucion;
	}
		
}