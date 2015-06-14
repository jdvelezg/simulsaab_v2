package simulaSAAB.tareas;

import java.util.logging.Level;
import java.util.logging.Logger;

import simulaSAAB.agentes.Demandante;
import simulaSAAB.agentes.Oferente;
import simulaSAAB.comunicacion.Demanda;
import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.comunicacion.Oferta;
import simulaSAAB.comunicacion.Proposito;

public class TransaccionComercial implements SistemaActividadHumana<Oferente> {
	
	private static Logger LOGGER = Logger.getLogger(TransaccionComercial.class.getName());
	
	private static String ENUNCIADO = "Realizar Transacción comercial";
	
	private Proposito Proposito;	
	
	private Oferta ofertaComprada;
	
	private Demanda demandaEquivalente;
	
	private Double montoTransaccion;
	
	private String Estado;
	
	private int paso;
	
	/**
	 * Constructor que ejecuta directamente la secuencia principal de acciones 
	 * con los parametros especificados
	 * 
	 * @param demanda
	 * @param oferta
	 */
	public TransaccionComercial(Demanda demanda, Oferta oferta) {
		
		demandaEquivalente 	= demanda;
		ofertaComprada		= oferta;
		montoTransaccion	= new Double(oferta.getPrecio());
		Estado 				= EstadosActividad.READY.toString();
		
		secuenciaPrincipalDeAcciones(oferta.getVendedor());
	}
	
	public TransaccionComercial(Oferta oferta){		
		
		ofertaComprada		= oferta;
		montoTransaccion	= new Double(oferta.getPrecio());
		//Estado 				= EstadosActividad.READY.toString();
	}



	@Override
	public void secuenciaPrincipalDeAcciones(Oferente actor) {
		
		if(this.Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){
			
			//realiza la transacción comercial entre el oferente y el demandante
			Dinero dinerOferente 	= actor.getDinero();		
			Dinero dineroDemandante = demandaEquivalente.getComprador().getDinero();
			
			dineroDemandante.subtractCantidad(montoTransaccion);
			dinerOferente.addCantidad(montoTransaccion);			
			//confirma la oferta como vendida.
			ofertaComprada.setVendida();
		}else{
			LOGGER.log(Level.SEVERE,"No se realizo la transacción, falta algo en el MPA");
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
	}}
