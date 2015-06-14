package simulaSAAB.comunicacion;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vividsolutions.jts.geom.Coordinate;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import simulaSAAB.agentes.Oferente;
import simulaSAAB.agentes.Productor.ProductorTrack;
import simulaSAAB.contextos.CentroUrbano;
import simulaSAAB.global.persistencia.AgentTrackObservable;
import simulaSAAB.global.persistencia.ProductoConfigurado;
import simulaSAAB.tareas.ProducirCebollaBulbo;

public class Oferta implements Concepto {
	
	private static Logger LOGGER = Logger.getLogger(Oferta.class.getName());
	
	public final OfertaTrack OBSERVABLE = new OfertaTrack(); 
	
	/**
	 * String producto. Nombre del producto ofertado.
	 */
	private final String nombreproducto;
	/**
	 * Recurso Productos. Conjunto de Recursos que describen el producto ofertado y su cantidad 
	 */
	private Recurso Productos;
	/**
	 * int Vigencia. Contiene el número de ciclos que una oferta esta vigente
	 */
	private int Vigencia;
	/**
	 * boolean Consolidable. Indica si la oferta puede ser consolidada con otras.
	 */
	private boolean Consolidable;
	/**
	 * Double precio. Precio total de la oferta
	 */
	private Double precio;
	/**
	 * String Estado. Describe el estado de la oferta
	 * - VIGENTE
	 * - VENCIDA
	 * - VENDIDA
	 * - CREADA
	 */
	private String Estado;
	/**
	 * Guarda el ciclo de tiempo en que fue registrada la oferta, para calcular su vencimiento
	 */
	private Double Tickinicial;
	/**
	 * Oferente vendedor. EL agente que realiza la oferta
	 */
	private Oferente vendedor; 
	/**
	 * CentroUrbano puntoOferta. El punto de oferta al que corresponde la oferta
	 */
	private CentroUrbano puntoOferta;
	/**
	 * Ubicacion fisica de la oferta
	 */
	private Coordinate ubicacion;
	
	/**
	 * Constructor
	 */
	public Oferta(Recurso producto) {		
		this.nombreproducto = producto.getProducto().getNombre();
		this.Productos		= producto;
	}
	
	public Oferta(Recurso productos, int ciclosVigencia, boolean consolidable, Double precio){
				
		
		this.nombreproducto = productos.getProducto().getNombre();
		this.Productos		= productos;
		this.Vigencia 		= ciclosVigencia;
		this.Consolidable 	= consolidable;
		this.precio			= precio;
		this.Estado			="CREADA";		
	}
	
	/**
	 * Calcula el vencimiento de la oferta despues de ser registrada
	 */
	//@ScheduledMethod (interval = 1)
	public void step () {
	
		if(vigente()){
			
			Double CurrentTick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
			if(CurrentTick - Tickinicial>Vigencia)
				this.setVencida();
		}
	}
	
	/**
	 * Devuelve True si la oferta ya fué vendida, Flase en caso que permanezca sin ser atendida
	 * 
	 * @return boolean
	 */
	public boolean vendida(){
		
		 if(this.Estado.equalsIgnoreCase("VENDIDA"))
			 return true;
		 else
			 return false;
	}
	
	/**
	 * Devuelve True si la oferta continua vigente
	 * @return
	 */
	public boolean vigente(){
		
		if(this.Estado.equalsIgnoreCase("VIGENTE"))
			 return true;
		 else
			 return false;
	}
	
	/**
	 * Devuelve el listado de recursos incluidos en la oferta
	 * @return Recurso
	 */
	public Recurso getProductos() {
		return Productos;
	}
	
	/**
	 * Devuelve el nombre del producto ofertado
	 * @return String
	 */
	public String getNombreProducto(){
		
		return nombreproducto;
	}
	
	/**
	 * Devuelve una ejemplificación del producto ofertado
	 * @return
	 */
	public Producto getProducto(){
		return new Producto(nombreproducto);
	}
	
	/**
	 * Define los productos que son ofertados
	 * @param producto
	 */
	public void setProductosOfertados(List<Recurso> producto) {
		
		Double cantidadTotal = 0.0;
		Producto p = new Producto(new ProductoConfigurado(this.nombreproducto));
		
		for(Recurso r: producto){
			
			cantidadTotal += r.getCantidad();			
		}
		
		Productos = new Recurso(p,cantidadTotal);
	}
	
	/**
	 * Separa de la oferta inicial una cantidad determinada de producto para ser vendido
	 * @param cantidad
	 */
	public Oferta separeCompra(Double cantidad){
		
		//calcula precio unitario
		Double precioUnitario = this.precio/this.getCantidad();
		
		//Separa productos de la oferta		
		this.Productos.removeCantidad(cantidad);
		this.precio = precioUnitario*this.getCantidad();
		
		Recurso separacion = new Recurso(this.Productos.getProducto(),cantidad);
		
		//crea suboferta con los productos solicitados
		Oferta subOferta = new Oferta(separacion,this.Vigencia,this.Consolidable,cantidad*precioUnitario);
		
		//Devuelve la suboferta separada	
		return subOferta;
		
	}
	
	public void setPrecio(Double precio){
		
		this.precio = precio;
	}
	
	public Double getPrecio(){
		
		return precio;
	}

	public int getVigencia() {
		return Vigencia;
	}

	public void setVigencia(int vigencia) {
		Vigencia = vigencia;
	}

	public boolean isConsolidable() {
		return Consolidable;
	}

	public void setConsolidable(boolean consolidable) {
		Consolidable = consolidable;
	}

	public String getEstado() {
		return Estado;
	}
	
	public void setVendida(){
		setEstado("VENDIDA");
	}

	public void setEstado(String estado) {				
			Estado = estado;
	}
	
	public void setVigente(){
		
		Tickinicial	= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		setEstado("VIGENTE");
		OBSERVABLE.setOfertaRegistrada();
	}
	
	public void setVencida(){
		setEstado("VENCIDA");
	}
	
	public Double getCantidad(){
		
		return Productos.getCantidad();
	}

	public Oferente getVendedor() {
		return vendedor;
	}

	public void setVendedor(Oferente vendedor) {
		this.vendedor = vendedor;
	}

	public CentroUrbano getPuntoOferta() {
		return puntoOferta;
	}

	public void setPuntoOferta(CentroUrbano puntoOferta) {
		this.puntoOferta = puntoOferta;
	}

	public Coordinate getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(Coordinate ubicacion) {
		this.ubicacion = ubicacion;
	}
	
	
	public class OfertaTrack extends AgentTrackObservable{
		
		private Double tick;
		
		private final String ofertaID;
		
		private String oferenteID;
		
		private Double precio;
		
		private String producto;
		
		private Double cantidad;
		
		/**
		 * Constructor
		 */
		public OfertaTrack(){
			
			super();
			this.ofertaID = Oferta.this.toString();			
		}
		
		/**
		 * reporta el registro de la demanda
		 */
		public void setOfertaRegistrada(){
			
			this.tick		= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
			this.oferenteID	= Oferta.this.vendedor.toString();
			this.producto	= Oferta.this.getNombreProducto();						
			this.cantidad	= Oferta.this.getCantidad();
			this.precio		= Oferta.this.precio;
			
			super.setChanged();
			super.notifyObservers(this);
		}

		@Override
		public String dataLineString(String separador) {			
			
			return tick.toString()+separador+ofertaID+separador+oferenteID+separador+producto+separador+precio.toString()+separador+cantidad.toString()+separador;
		}

		@Override
		public String dataLineStringHeader(String separador) {
			
			return "tick"+separador+"Oferta_ID"+separador+"Oferente_ID"+separador+"Producto"+separador+"Precio"+separador+"Cantidad"+separador;
		}
		
		
		/**Getters & Setters **/
		
		
		public Double getTick() {
			return tick;
		}

		public void setTick(Double tick) {
			this.tick = tick;
		}

		public String getOfertaID() {
			return ofertaID;
		}

		public String getProductorID() {
			return oferenteID;
		}

		public Double getPrecio() {
			return precio;
		}

		public void setPrecio(Double precio) {
			this.precio = precio;
		}

		public String getProducto() {
			return producto;
		}

		public void setProducto(String producto) {
			this.producto = producto;
		}

		public Double getCantidad() {
			return cantidad;
		}

		public void setCantidad(Double cantidad) {
			this.cantidad = cantidad;
		}		
		
	}
	

}
