package simulaSAAB.comunicacion;

import java.util.List;

public class OrdenDeCompra implements Concepto {
	
	private Oferta Oferta;
	
	private Demanda Demanda;
	
	private Recurso DetalleCompra;
	
	private Dinero PagoAcordado;
	
	private List<Object> OrdenesDeServicio;
	
	/**
	 * Constructor
	 */
	public OrdenDeCompra() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Constructor
	 * @param offer Oferta ligada a la orden de compra
	 * @param demand Demanda ligada a la orden de compra
	 */
	public OrdenDeCompra(Oferta offer, Demanda demand) {
		
		
		this.Oferta 	= offer;
		this.Demanda	= demand;
		
		// TODO Auto-generated constructor stub
	}

	public Oferta getOferta() {
		return Oferta;
	}

	public void setOferta(Oferta oferta) {
		Oferta = oferta;
	}

	public Demanda getDemanda() {
		return Demanda;
	}

	public void setDemanda(Demanda demanda) {
		Demanda = demanda;
	}

	public Recurso getDetalleCompra() {
		return DetalleCompra;
	}

	public void setDetalleCompra(Recurso detalleCompra) {
		DetalleCompra = detalleCompra;
	}

	public Dinero getPagoEfectuado() {
		return PagoAcordado;
	}

	public void setPagoEfectuado(Dinero pagoEfectuado) {
		PagoAcordado = pagoEfectuado;
	}
	
	

}
