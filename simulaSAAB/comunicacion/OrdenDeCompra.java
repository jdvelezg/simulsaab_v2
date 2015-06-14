package simulaSAAB.comunicacion;


import repast.simphony.engine.environment.RunEnvironment;
import simulaSAAB.comunicacion.OrdenDeServicio.OrdenServicioTrack;
import simulaSAAB.contextos.CentroUrbano;
import simulaSAAB.global.persistencia.AgentTrackObservable;

/**
 * Clase OrdenDeCompra
 * 
 * Un pedido unitario genera tantas órdenes de compra como puntos de oferta tenga el pedido
 * 
 * @author dampher
 *
 */
public class OrdenDeCompra implements Concepto {
	
	public final OrdenCompraTrack OBSERVABLE = new OrdenCompraTrack(); 
	
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
		this.PuntoDeOferta	= offer.getPuntoOferta();		
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
	
	/**
	 * 
	 * @author dampher
	 *
	 */
	public class OrdenCompraTrack extends AgentTrackObservable{		
		
		private String ofertaID;
		private String demandaID;
		private Double tick;
		private String puntoOferta;
		private String producto;
		private Double pago;
		private Double cantidad;
		
		public OrdenCompraTrack(){			
			super();			
		}
		
		/**
		 * Actualiza los valores y reporta a los observadores.
		 * Se invoca en generarOrdenDeCompra de la clase SISaaB
		 */
		public void pagoAcordado(){
			
			this.tick		= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
			this.ofertaID	= OrdenDeCompra.this.Oferta.toString();
			this.demandaID	= OrdenDeCompra.this.Demanda.toString();
			this.puntoOferta= OrdenDeCompra.this.PuntoDeOferta.getNombre();
			this.producto	= OrdenDeCompra.this.getDetalleCompra().getProducto().getNombre();
			this.cantidad	= OrdenDeCompra.this.getDetalleCompra().getCantidad();
			this.pago		= OrdenDeCompra.this.getPagoAcordado().getCantidad();	
			
			super.setChanged();
			super.notifyObservers(this);
		}
		
		@Override
		public String dataLineString(String separador) {
			
			return tick.toString()+separador+ofertaID+separador+demandaID+separador+puntoOferta+separador+producto+separador+cantidad.toString()+separador+pago.toString()+separador;
		}
		@Override
		public String dataLineStringHeader(String separador) {
			
			return "tick"+separador+"oferta_ID"+separador+"demanda_ID"+separador+"punto_Oferta"+separador+"producto"+separador+"cantidad"+separador+"pago"+separador;
		}


		public String getOfertaID() {
			return ofertaID;
		}


		public void setOfertaID(String ofertaID) {
			this.ofertaID = ofertaID;
		}


		public String getDemandaID() {
			return demandaID;
		}


		public void setDemandaID(String demandaID) {
			this.demandaID = demandaID;
		}


		public Double getTick() {
			return tick;
		}


		public void setTick(Double tick) {
			this.tick = tick;
		}


		public String getPuntoOferta() {
			return puntoOferta;
		}


		public void setPuntoOferta(String puntoOferta) {
			this.puntoOferta = puntoOferta;
		}


		public String getProducto() {
			return producto;
		}


		public void setProducto(String producto) {
			this.producto = producto;
		}


		public Double getPago() {
			return pago;
		}


		public void setPago(Double pago) {
			this.pago = pago;
		}


		public Double getCantidad() {
			return cantidad;
		}


		public void setCantidad(Double cantidad) {
			this.cantidad = cantidad;
		}		
		
	}

}
