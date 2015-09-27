package simulaSAAB.tareas;

import simulaSAAB.agentes.Demandante;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.global.persistencia.MPAConfigurado;
/**
 * Representa el registro de las demandas consolidadas en el SISAAB
 * @author lfgomezm
 *
 */
public class RegistrarDemandaConsolidada extends RegistrarDemandaUnitaria {		
	
	/**
	 * Constructor
	 */
	public RegistrarDemandaConsolidada() {		
		super();
	}
	
	@Override
	public void secuenciaPrincipalDeAcciones(Demandante actor) {
		super.secuenciaPrincipalDeAcciones(actor);
	}
	
	@Override
	public int getId() {
		return 0;
	}
	
}