package simulaSAAB.tareas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import repast.simphony.util.collections.IndexedIterable;
import simulaSAAB.agentes.Demandante;
import simulaSAAB.agentes.SiSaab;
import simulaSAAB.comunicacion.Demanda;
import simulaSAAB.comunicacion.Oferta;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Proposito;

public class RegistrarDemandaUnitaria implements
		SistemaActividadHumana<Demandante> {

	public RegistrarDemandaUnitaria() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void secuenciaPrincipalDeAcciones(Demandante actor) {
		
		//Registra la demanda
		Demanda demanda 	= actor.generarDemanda();
		SiSaab.registrarDemanda(demanda);
		
		/**
		 * Lleva a cabo la compra una vez ha sido registrada la demanda
		 */
		List<Oferta> compra = new ArrayList<Oferta>();			
		Double presupuesto	= demanda.getPresupuesto();
		
		//Obtiene ofertas registradas
		List<Oferta> ofertas = SiSaab.ofertasRegistradas(demanda.getNombreproducto());
		
		//filtra ofertas de interés para la demanda
		for(Oferta o: ofertas){		
			
			if(o.getPrecio() <= presupuesto){			
				
				o.vendida();
				compra.add(o);						
				presupuesto	-= o.getPrecio();					
			}else{
				break;
			}
		}//end for
		
		//verifica que si existan ofertas disponibles para la demanda
		if(compra.size()>0){			
			
			SiSaab.realizarCompra(compra, demanda);
			
		}else{//No existen ofertas para la demanda 
			
			//TODO: consolidar demanda
			
		}
		
		
	}

	@Override
	public SistemaActividadHumana getInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPaso() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getEstado() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEnunciado() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Proposito getProposito() {
		// TODO Auto-generated method stub
		return null;
	}

}
