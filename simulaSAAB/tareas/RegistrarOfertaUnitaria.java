package simulaSAAB.tareas;

import java.util.logging.Level;
import java.util.logging.Logger;

import repast.simphony.engine.environment.RunEnvironment;
import simulaSAAB.agentes.Oferente;
import simulaSAAB.agentes.Productor;
import simulaSAAB.agentes.SiSaab;
import simulaSAAB.agentes.Terreno;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.comunicacion.Oferta;
import simulaSAAB.comunicacion.Recurso;
import simulaSAAB.global.persistencia.MPAConfigurado;
import simulaSAAB.global.persistencia.ProductoConfigurado;

public class RegistrarOfertaUnitaria implements
		SistemaActividadHumana<Oferente> {
	
	
	private final int id;
	
	private static Logger LOGGER = Logger.getLogger(RegistrarOfertaUnitaria.class.getName());
	
	private static Proposito proposito = new Proposito("Vender productos");
	
	private static String Enunciado;
	
	private double CostoEjecucion;
	
	private double DineroInicial;
	
	private Oferta Oferta;	
	
	private String Estado;
	
	private int paso;
	
	//private final double Tickinicial;
	

	public RegistrarOfertaUnitaria() {
		
		MPAConfigurado mpa 	=new MPAConfigurado("RegistrarOfertaUnitaria");
		
		this.id				= mpa.getId();
		this.Enunciado		= mpa.getEnunciado();
		this.CostoEjecucion	= mpa.getCosto();		
				
		this.Estado = EstadosActividad.READY.toString();
	}

	@Override
	public void secuenciaPrincipalDeAcciones(Oferente actor) {
		
		if(this.Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){
			
			this.Estado	= EstadosActividad.RUNNING.toString();
			actor.setEstado("RUNNING");
			DineroInicial = new Double(actor.getDinero().getCantidad());
			this.paso	= 1;
			
		}
		else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){			
			
			switch(this.paso){
			case 1:
				
				//obtiene la oferta del agente
				this.Oferta = actor.generarOferta();
				
				//registra la oferta en el SISAAB
				if(Oferta ==null){
					
					//Si no tiene productos, no puede ofertar nada
					this.Estado	= EstadosActividad.DONE.toString();					
				}else{					
					
					SiSaab.registrarOferta(Oferta);
					//LOGGER.log(Level.INFO,actor.toString()+" OFERTA REGISTRADA");
				}
				
				this.paso++;				
				break;
			case 2:	
				//LOGGER.log(Level.INFO,this.toString()+"paso2 "+actor.getEstado());
				
				/**
				 * Monitorea la Oferta registrada. El agente espera durante la vigencia de la oferta registrada.
				 * La espera es necesaria, dado que el agente debe evaluar su experiencia después de realizada la venta
				 * 
				 * TODO Buscar una forma que el agente ejecute diferentes actividades y luego sea capaz de evaluarlas separadamente.
				 */
				if(!Oferta.vigente() || Oferta.vendida())
					paso++;
				
				break;
			case 3:
				//LOGGER.log(Level.INFO,this.toString()+"paso3 "+actor.getEstado());
				
				if(!this.Oferta.vendida())//TODO deberia modificar el precio y volver a ofertar
					this.paso++;
				
				
				break;				
			default:			
				
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
		return new RegistrarOfertaUnitaria();
	}

	@Override
	public int getPaso() {
		// TODO Auto-generated method stub
		return paso ;
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
