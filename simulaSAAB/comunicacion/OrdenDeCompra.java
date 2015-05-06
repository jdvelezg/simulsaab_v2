package simulaSAAB.comunicacion;


import simulaSAAB.contextos.CentroUrbano;

public class OrdenDeCompra implements Concepto {
	
	private Oferta Oferta;
	
	private Demanda Demanda;
	
	private Dinero PagoAcordado;
	
	private CentroUrbano PuntoDeOferta;
	
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
		
		
		this.Oferta 		= offer;
		this.Demanda		= demand;
		this.PagoAcordado	= new Dinero(offer.getPrecio());
		
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
		return Oferta.getProductos();
	}

	public Dinero getPagoAcordado() {
		return PagoAcordado;
	}

	public CentroUrbano getPuntoDeOferta() {
		return Oferta.getPuntoOferta();
	}
	
	

}
