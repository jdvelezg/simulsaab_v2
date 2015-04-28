package simulaSAAB.tareas;

import java.util.List;
import java.util.logging.Logger;

import repast.simphony.engine.environment.RunEnvironment;
import simulaSAAB.agentes.AgenteInteligente;
import simulaSAAB.agentes.Productor;
import simulaSAAB.agentes.Terreno;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.comunicacion.Recurso;
import simulaSAAB.global.persistencia.MPAConfigurado;
import simulaSAAB.global.persistencia.ProductoConfigurado;

public class ProducirCebollaBulbo implements SistemaActividadHumana<Productor> {
	
	private final int id;
	
	private static Logger LOGGER = Logger.getLogger(ProducirCebollaBulbo.class.getName());
	
	private static Proposito proposito = new Proposito("Producir cebolla de bulbo en los terrenos aptos y disponibles");
	
	private static String Enunciado;
	
	private double CostoEjecucion;
	
	private Terreno Terreno;
	
	private Producto cebollaBulbo;	
	
	private String Estado;
	
	private int paso;
	
	private final double Tickinicial;
	
	private SistemaActividadHumana moverse;
	
	private SistemaActividadHumana ProcesoHumano;

	/**
	 * Constructor
	 */
	public ProducirCebollaBulbo() {
		// TODO Auto-generated constructor stub
		MPAConfigurado mpa 	=new MPAConfigurado("ProduccionCebollaBulbo");
		
		this.id				= mpa.getId();
		this.cebollaBulbo	= new Producto(new ProductoConfigurado("Cebolla de bulbo"));
		this.Enunciado		= mpa.getEnunciado();
		this.proposito 		= mpa.getProposito();
		this.CostoEjecucion	= mpa.getCosto();
		
		Tickinicial	= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		this.Estado = EstadosActividad.READY.toString();
		
	}
	
	

	@Override
	public void secuenciaPrincipalDeAcciones(Productor actor) {
		
		if(this.Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){
					
			if(this.Terreno==null)
				this.Terreno	= actor.getTerrenosCultivables().get(0);
			
			this.Estado	= EstadosActividad.RUNNING.toString();
			actor.setEstado("RUNNING");
			this.paso	= 1;
			
			//LOGGER.log(Level.INFO, this.toString() + " Listo para iniciar. Actor: "+actor.toString());
		}
		else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){			
			
			switch(this.paso){
			case 1:
				
				//Preparación del terreno implica arado, fertilización
				
				this.Terreno.setEstado("Preparado");	
				
				this.paso++;				
				break;
			case 2:
				
				//LOGGER.log(Level.INFO, this.toString() + " paso 2. Actor: "+actor.toString());
				
				this.Terreno.setEstado("Sembrado");
				
				this.paso++;				
				break;
			case 3:
				
				//LOGGER.log(Level.INFO, this.toString() + " paso 3. Actor: "+actor.toString());
				
				this.Terreno.setEstado("Cultivo");
				
				this.paso++;				
				break;
			case 4:				
				
				//LOGGER.log(Level.INFO, this.toString() + " paso 4. Actor: "+actor.toString());
				
				this.Terreno.setEstado("Mantenimiento");
				
				this.paso++;				
				break;
			case 5:
				
				//LOGGER.log(Level.INFO, this.toString() + " paso 5. Actor: "+actor.toString());
				
				//espera x numero de ticks
				
				Double CurrentTick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
				
				if(CurrentTick-Tickinicial>=1000){//5 meses
					this.paso++;
				}
				//paso++;
				break;
			case 6:
				
				Recurso cosecha	= this.Terreno.cosechar(cebollaBulbo);

				//Fija costo unitario
				double precio 	= CostoEjecucion/cosecha.getCantidad();				
				cosecha.setCostoUnitario(precio);
				
				actor.addProducto(cosecha);	 
				actor.setPropositoVigente(new Proposito("vender"));
				ProcesoHumano =new ProcesoAgenteHumano();
				//VenderCebollaBulbo = new VenderCebollaBulbo();
								
				this.paso++;				
				break;
			case 7:				
								
				ProcesoHumano.secuenciaPrincipalDeAcciones(actor);
				
				//Si el subsistema termino continua al siguiente paso				
				if(ProcesoHumano.getEstado().equalsIgnoreCase(EstadosActividad.DONE.toString()))
					this.paso++;

				break;
			default:
				this.Estado ="DONE";				
			}
			
		}else if(this.Estado.equalsIgnoreCase(EstadosActividad.DONE.toString())){
			actor.setEstado("IDLE");
			actor.setPropositoVigente(null);
			//LOGGER.log(Level.INFO,this.toString()+" DONE: -DOING NOTHING");
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
