/**
 * 
 */
package simulaSAAB.tareas;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import simulaSAAB.agentes.AgenteInteligente;
import simulaSAAB.comunicacion.Producto;

/**
 * @author dampher
 *
 */
public class ProcesoAgenteHumano implements SistemaActividadHumana<AgenteInteligente> {
	
	private static Logger LOGGER = Logger.getLogger(ProcesoAgenteHumano.class.getName());
	
	private String Enunciado;
	
	private String Estado;
	
	private int paso;

	/**
	 * 
	 */
	public ProcesoAgenteHumano() {
		// TODO Auto-generated constructor stub
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
			
			List<Producto> opciones = new ArrayList<Producto>();
			
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
		return null;
	}


}
