package simulaSAAB.tareas;

import java.util.logging.Logger;

import simulaSAAB.agentes.Demandante;
import simulaSAAB.comunicacion.Proposito;

public class ConsolidarDemanda implements SistemaActividadHumana<Demandante> {
	
private static Logger LOGGER = Logger.getLogger(ProcesoAgenteHumano.class.getName());
	
	private static String ENUNCIADO = "Consolidar demandas";
	
	private Proposito Proposito;
	
	private String Estado;
	
	private int paso;
	

	public ConsolidarDemanda() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void secuenciaPrincipalDeAcciones(Demandante actor) {
		
if(this.Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){
			
			this.Estado	=EstadosActividad.RUNNING.toString();
			this.paso	=1;
			
			
			//Crea una nutrired si no posee una
			
				//Busca agentes alrededor
				
				//Envía mensaje solicitando consolidar las ofertas
			
			//agrega las ofertas para consolidacion
			
			
			
			
			
			
			
			//LOGGER.log(Level.INFO, this.toString() + " Esta listo para iniciar. Actor: " + actor.toString());
		}
		else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){
			
			
			switch(this.paso){
			case 1:
				
				//Espera a que la nutrired registre las demandas o aborta si falla la creacion de la nutrired
				
				paso ++;
				break;
			
			default:
				//LOGGER.log(Level.INFO, "TERMINA PROCESO"+actor.toString());
				this.Estado =EstadosActividad.DONE.toString();
				
			}
			
				
		}

		
		
	}

	@Override
	public SistemaActividadHumana getInstance() {
		
		return new ConsolidarDemanda() ;
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
		
		return this.ENUNCIADO;
	}

	@Override
	public Proposito getProposito() {
		
		return this.Proposito;
	}

	

}
