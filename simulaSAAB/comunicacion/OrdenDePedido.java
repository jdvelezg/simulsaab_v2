package simulaSAAB.comunicacion;

import java.util.List;

import simulaSAAB.contextos.PlazaDistrital;

import com.vividsolutions.jts.geom.Geometry;

public class OrdenDePedido implements Concepto {
		
	private PlazaDistrital puntoDemanda;
	
	private List<OrdenDeCompra> OrdenesDeCompra;
	
	private List<OrdenDeServicio> OrdenesDeServicio;

	/**
	 * Contructor
	 */
	public OrdenDePedido() {
		// TODO Auto-generated constructor stub
	}
	
	public OrdenDePedido(Demanda demanda, List<OrdenDeCompra> compras, List<OrdenDeServicio> servicios ){
		
		this.puntoDemanda		= demanda.getPuntoDemanda();
		this.OrdenesDeCompra 	= compras;
		this.OrdenesDeServicio	= servicios;
		
	}

	public PlazaDistrital getPuntoDemanda() {
		return puntoDemanda;
	}

	public void setPuntoDemanda(PlazaDistrital puntoDemanda) {
		this.puntoDemanda = puntoDemanda;
	}

	public List<OrdenDeCompra> getOrdenesDeCompra() {
		return OrdenesDeCompra;
	}

	public void setOrdenesDeCompra(List<OrdenDeCompra> ordenesDeCompra) {
		OrdenesDeCompra = ordenesDeCompra;
	}

	public List<OrdenDeServicio> getOrdenesDeServicio() {
		return OrdenesDeServicio;
	}

	public void setOrdenesDeServicio(List<OrdenDeServicio> ordenesDeServicio) {
		OrdenesDeServicio = ordenesDeServicio;
	}
	
	

}
