package simulaSAAB.tareas;

import simulaSAAB.agentes.Demandante;
import simulaSAAB.comunicacion.Proposito;

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
	public SistemaActividadHumana getInstance() {	
		return new RegistrarDemandaConsolidada();
	}
}
