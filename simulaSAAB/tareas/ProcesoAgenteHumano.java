/**
 * 
 */
package simulaSAAB.tareas;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import simulaSAAB.agentes.AgenteInteligente;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Proposito;

/**
 * @author dampher
 *
 */
public class ProcesoAgenteHumano implements SistemaActividadHumana<AgenteInteligente> {
	
	private static Logger LOGGER = Logger.getLogger(ProcesoAgenteHumano.class.getName());
	
	private static String ENUNCIADO = "Proceso modelo de un agente humano activo en el mundo";
	
	private Proposito Proposito;
	
	private String Estado;
	
	private int paso;

	/**
	 * Constructor
	 */
	public ProcesoAgenteHumano() {
		
		Proposito 	= new Proposito("Emular el comportamiento de un agente humano activo en el mundo");
		Estado 		= EstadosActividad.READY.toString();
		paso		= 0;
	}

	/* (non-Javadoc)
	 * @see simulaSAAB.tareas.SistemaActividadHumana#secuenciaPrincipalDeAcciones()
	 */
	@Override
	public void secuenciaPrincipalDeAcciones(AgenteInteligente actor) {
		
		if(this.Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){
			
			this.Estado	=EstadosActividad.RUNNING.toString();
			this.paso	=1;
			//LOGGER.log(Level.INFO, "Actor: "+actor.toString());
		}
		else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){
			
			
			switch(this.paso){
			case 1:
				
				actor.percibirMundoSelectivamente();
				actor.formarIntenciones();				
				
				//LOGGER.log(Level.INFO, this.toString() + " Paso 1.Decision Actor: "+actor.toString());
				
				paso ++;
				break;
			case 2:				
				//LOGGER.log(Level.INFO, this.toString() + " Paso 2." + actor.toString());				
				actor.tomarDecisiones();
				paso++;
				
				break;
			case 3:				
				//LOGGER.log(Level.INFO, actor.toString() + " Actua: "+actor.getActividadVigente().toString());
				actor.actuar();
				
				if(actor.getEstado().equalsIgnoreCase("IDLE"))
					paso++;
				
				break;
			case 4: 				
				//LOGGER.log(Level.INFO, this.toString() + " Paso 4. Actor: " + actor.toString());				
				actor.juzgarMundoSegunEstandares();
				
				paso++;
				break;
			
			default:
				//LOGGER.log(Level.INFO, "TERMINA PROCESO "+actor.toString());
				this.Estado =EstadosActividad.DONE.toString();
				
			}							
		}else if(this.Estado.equalsIgnoreCase(EstadosActividad.DONE.toString())){
			//LOGGER.log(Level.INFO, "PROCESO DONE"+actor.toString());
			this.Estado =EstadosActividad.READY.toString();
		}
		//LOGGER.log(Level.INFO, "Paso: "+(paso-1)+ " ACTOR: "+actor.toString());
	}

	/* (non-Javadoc)
	 * @see simulaSAAB.tareas.SistemaActividadHumana#getInstance()
	 */
	@Override
	public SistemaActividadHumana getInstance() {
		
		return new ProcesoAgenteHumano();
	}

	@Override
	public int getPaso() {
	
		return this.paso;
	}
	
	public void setPaso(int paso){
		
		if(paso==0)
			this.Estado = EstadosActividad.READY.toString();
		else
			this.Estado = EstadosActividad.RUNNING.toString();
		
		this.paso = paso;
	}

	@Override
	public String getEstado() {
		
		return this.Estado;
	}

	@Override
	public String getEnunciado() {
	
		return ENUNCIADO;
	}

	@Override
	public Proposito getProposito() {
	
		return Proposito;
	}
	
	


}
