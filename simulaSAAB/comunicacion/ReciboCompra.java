package simulaSAAB.comunicacion;

/**
 * Representa el concepto de <code>producto</code> usado en los registros del SISAAB y en la ontología para comunicación e interacción comercial de los agentes
 * 
 * @author jdvelezg
 *
 */
public class ReciboCompra extends OrdenDeCompra {
	
	private Recurso productoComprado;
	
	/**
	 * Constructor
	 */
	public ReciboCompra() {
		super();
	}
	/**
	 * Constructor
	 * 
	 * @param productoComprado recurso asociado al recibo de compra
	 * @param montoPagado double, monto a pagar por la compra
	 */
	public ReciboCompra(Recurso productoComprado, double montoPagado){
		super();
		this.productoComprado 	= productoComprado;
		PagoAcordado 			= new Dinero(montoPagado);
	}
	/**
	 * Constructor
	 * 
	 * @param offer oferta asociada al recibo de compra
	 * @param demand demanda asociada al recibo de compra
	 */
	public ReciboCompra(Oferta offer, Demanda demand) {
		super(offer, demand);
		productoComprado = super.getDetalleCompra();
	}

	@Override
	public Recurso getDetalleCompra(){
		return this.productoComprado;
	}
	
}
