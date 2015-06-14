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
import simulaSAAB.global.persistencia.MPAConfigurado;
import simulaSAAB.global.persistencia.ProductoConfigurado;

public class ProducirCebollaBulbo implements SistemaActividadHumana<Productor> {
	
	private final int id;
	
	private static Logger LOGGER = Logger.getLogger(ProducirCebollaBulbo.class.getName());
	
	private static Proposito proposito = new Proposito("Producir cebolla y vender el producto");
	
	private static String Enunciado;
	
	private double CostoEjecucion;
	
	private Terreno Terreno;
	
	private Producto cebollaBulbo;	
	
	private String Estado;
	
	private int paso;
	
	private final double Tickinicial;
	
	private double DineroInicial;
	
	private SistemaActividadHumana moverse;
	
	private RegistrarOfertaUnitaria venderProductos;

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
		
		Tickinicial	= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();	
	}
	
	

	@Override
	public void secuenciaPrincipalDeAcciones(Productor actor) {
		
		if(Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){
					
			if(Terreno==null){
				Terreno	= actor.getTerrenosCultivables().get(0);
			}
			
			this.Estado	= EstadosActividad.RUNNING.toString();
			actor.setEstado("RUNNING");			
			
			DineroInicial = new Double(actor.getDinero().getCantidad());
			paso	= 1;
			
			//LOGGER.log(Level.INFO, this.toString() +this.Estado+ " Listo para iniciar. Actor: "+actor.toString());
		}
		else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){		
			
			switch(this.paso){
			case 1:
				//LOGGER.log(Level.INFO,this.toString()+" paso1 "+actor.toString()+": "+actor.getEstado());
				//Preparación del terreno implica arado, fertilización
				
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
				
				if(CurrentTick-Tickinicial>=1000){//5 meses
					//this.paso++;
				}
			paso++;
				break;
			case 6:				
				
				Recurso cosecha	= this.Terreno.cosechar(cebollaBulbo);

				//Fija costo unitario: CostoEjecucionMPAPorHectarea*#hectareasUsadas/CantidadCosechada 
				double precio 	= CostoEjecucion*Terreno.getHectareas()/cosecha.getCantidad();				
				cosecha.setCostoUnitario(precio);
				
				actor.addProducto(cosecha);			
				venderProductos = new RegistrarOfertaUnitaria();
				//LOGGER.log(Level.INFO,"paso6 "+actor.getProductos().size());
				this.paso++;				
				break;
			case 7:				
				//LOGGER.log(Level.INFO,this.toString()+"paso7 "+actor.getEstado());
				
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
			
			//substrae el costo de ejecución del MPA del dinero del agente
			actor.getDinero().subtractCantidad(CostoEjecucion);
			//Calcula la utilidad obtenida al ejecutar el MPA				
			actor.setUltimaUtilidadObtenida(DineroInicial-actor.getDinero().getCantidad());
			//fija estado del actor en IDLE
			actor.setEstado("IDLE");			
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
		
}
