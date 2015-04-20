/**
 * 
 */
package simulaSAAB.tareas;

import java.util.ArrayList;
import java.util.List;
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
		Estado 	= EstadosActividad.UNSET.toString();
	}

	/* (non-Javadoc)
	 * @see simulaSAAB.tareas.SistemaActividadHumana#secuenciaPrincipalDeAcciones()
	 */
	@Override
	public void secuenciaPrincipalDeAcciones(AgenteInteligente actor) {
		
		if(this.Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){
			
			this.Estado	=EstadosActividad.RUNNING.toString();
			this.paso	=1;
			//LOGGER.log(Level.INFO, this.toString() + " Esta listo para iniciar. Actor: " + actor.toString());
		}
		else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){
			
			
			switch(this.paso){
			case 1:
				
				actor.percibirMundoSelectivamente();
				actor.formarIntenciones();				
				
				//LOGGER.log(Level.INFO, this.toString() + " Paso 1.Decision Actor: " + actor.getActividadVigente().toString());
				
				paso ++;
				break;
			case 2:				
				//LOGGER.log(Level.INFO, this.toString() + " Paso 2." + actor.toString()+actor.getActividadVigente().toString());				
				actor.tomarDecisiones();
				actor.actuar();
				
				if(actor.getEstado().equalsIgnoreCase("IDLE"))
					paso++;
				
				break;
			case 3: 				
				//LOGGER.log(Level.INFO, this.toString() + " Paso 3. Actor: " + actor.toString());				
				actor.juzgarMundoSegunEstandares();
				
				paso++;
				break;
			default:
				//LOGGER.log(Level.INFO, "TERMINA PROCESO"+actor.toString());
				this.Estado =EstadosActividad.READY.toString();
				
			}
			
				
		}

	}

	/* (non-Javadoc)
	 * @see simulaSAAB.tareas.SistemaActividadHumana#getInstance()
	 */
	@Override
	public SistemaActividadHumana getInstance() {
		// TODO Auto-generated method stub
		return new ProcesoAgenteHumano();
	}

	@Override
	public int getPaso() {
		// TODO Auto-generated method stub
		return this.paso;
	}

	@Override
	public String getEstado() {
		// TODO Auto-generated method stub
		return this.Estado;
	}

	@Override
	public String getEnunciado() {
		// TODO Auto-generated method stub
		return ENUNCIADO;
	}

	@Override
	public Proposito getProposito() {
		// TODO Auto-generated method stub
		return Proposito;
	}
	
	


}
