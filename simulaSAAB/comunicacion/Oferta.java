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

/**
 * Representa el concepto de <code>oferta</code> usado en los registros del SISAAB y en la ontología para comunicación entre los agentes  
 * 
 * @author jdvelezg
 *
 */
public class Oferta implements Concepto {
	
	/**
	 * Utilizada para la asignación de identificadores consecutivos a las ofertas
	 */
	private static int CONSECUTIVO; 
	/**
	* Registro de la clase usado para depuración <code>Debugging</code>
	*/
	private static Logger LOGGER = Logger.getLogger(Oferta.class.getName());
	/**
	 * Identificador unico dle producto
	 */
	private final int id;
	/**
	 * agrega funcionalidad <code>observable</code> a la clase
	 */
	private final OfertaTrack OBSERVABLE = new OfertaTrack(); 	
	/**
	 * nombre del producto ofertado.
	 */
	private String nombreproducto;
	/**
	 * Recurso que describe el producto ofertado y su cantidad 
	 */
	private Recurso Productos;
	/**
	 * número de ciclos que una oferta esta vigente
	 */
	private int Vigencia;
	/**
	 * indica si la oferta puede ser consolidada con otras
	 */
	private boolean Consolidable;
	/**
	 * precio total de la oferta
	 */
	private Double precio;
	/**
	 * describe el estado de la oferta:
	 * - VIGENTE
	 * - VENCIDA
	 * - VENDIDA
	 * - CREADA
	 */
	private String Estado;
	
	private Double Tickinicial;
	/**
	 * agente que realiza la oferta
	 */
	private Oferente vendedor; 
	/**
	 * punto de oferta al que corresponde la oferta
	 */
	private CentroUrbano puntoOferta;
	/**
	 * uUbicacion de la oferta
	 */
	private Coordinate ubicacion;
	
	/**
	 * Cosntructor
	 * @param ofertaid id de la oferta
	 */
	public Oferta(int ofertaid){
		this.id = ofertaid;
	}
	/**
	 * Constructor
	 * @param producto recurso ofertado
	 */
	public Oferta(Recurso producto) {
		this.id = setId();
		this.nombreproducto = producto.getProducto().getNombre();
		this.Productos		= producto;
	}
	/**
	 * Constructor
	 * @param productos recurso ofertado
	 * @param ciclosVigencia vigencia de la oferta
	 * @param consolidable <ocde>true</code> si la oferta es consolidable
	 * @param precio precio total de la oferta
	 */
	public Oferta(Recurso productos, int ciclosVigencia, boolean consolidable, Double precio){
				
		this.id = setId();
		this.nombreproducto = productos.getProducto().getNombre();
		this.Productos		= productos;
		this.Vigencia 		= ciclosVigencia;
		this.Consolidable 	= consolidable;
		this.precio			= new Double(precio.doubleValue());
		this.Estado			="CREADA";
		
		this.Productos.setCostoUnitario(this.precio/this.Productos.getCantidad());
	}
	
	/**
	 * Devuelve el id consecutivo a la oferta, e incrementa el consecutivo estático
	 * @return int id consecutivo
	 */
	private int setId(){		
		int i =this.CONSECUTIVO+1;
		this.CONSECUTIVO++;
		return i;
	}
	
	/**
	 * Verifica en cada ciclo de tiempo si la oferta debe darse por vencida
	 */	
	public void step () {
	
		if(vigente()){
			
			Double CurrentTick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
			if(CurrentTick >Tickinicial+Vigencia)
				this.setVencida();
		}
	}
	
	/**
	 * Devuelve <code>true</code> si la oferta ya fué vendida, <code>false</code> en caso que permanezca sin ser atendida	 * 
	 * @return boolean
	 */
	public boolean vendida(){
		
		 if(this.Estado.equalsIgnoreCase("VENDIDA"))
			 return true;
		 else
			 return false;
	}
	
	/**
	 * Devuelve <code>true</code> si la oferta continua vigente
	 * @return boolean
	 */
	public boolean vigente(){
		
		if(this.Estado.equalsIgnoreCase("VIGENTE"))
			 return true;
		 else
			 return false;
	}
	
	/**
	 * Devuelve el recurso de la oferta
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
	 * Asigna los productos ofertados
	 * @param producto arreglo de recursos a ser ofertados
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
	 * Separa de la oferta inicial una cantidad determinada de producto
	 * @param cantidad cantidad del producto a ser separado
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
	/**
	 * Asigna el precio de la oferta
	 * @param precio double valor de la oferta
	 */
	public void setPrecio(Double precio){
		
		this.precio = precio;
	}
	/**
	 * Devuelve el precio de la oferta
	 * @return double
	 */
	public Double getPrecio(){
		
		return precio;
	}

	/**
	 * Devuelve la vigencia configurada para la oferta
	 * @return int
	 */
	public int getVigencia() {
		return Vigencia;
	}
	/**
	 * Asigna la vigencia de la oferta
	 * @param vigencia numero de ciclos que la oferta debe permanecer vigente
	 */
	public void setVigencia(int vigencia) {
		Vigencia = vigencia;
	}
	/**
	 * Devuelve <code>true</code> si la oferta es consolidable, <code>false</code> en caso contrario
	 * @return boolean
	 */
	public boolean isConsolidable() {
		return Consolidable;
	}
	/**
	 * Configura la oferta como consolidable
	 * @param consolidable
	 */
	public void setConsolidable(boolean consolidable) {
		Consolidable = consolidable;
	}
	/**
	 * Devuelve el estado de la oferta
	 * @return string
	 */
	public String getEstado() {
		return Estado;
	}
	/**
	 * Asigna el estado de la oferta a 'vendida'
	 */
	public void setVendida(){
		if(!Estado.equalsIgnoreCase("VENDIDA")){
			setEstado("VENDIDA");
			this.OBSERVABLE.setOfertaRegistrada();
		}		
	}
	/**
	 * Asigna el estado de la oferta
	 * @param estado string estado de la oferta
	 */
	public void setEstado(String estado) {				
			Estado = estado;
			this.OBSERVABLE.setOfertaRegistrada();
	}
	/**
	 * Asigna el estado de la oferta como 'vigente'
	 */
	public void setVigente(){
		
		Tickinicial	= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		setEstado("VIGENTE");
	}
	/**
	 * Asigna el estado de la oferta como 'vencida'
	 */
	public void setVencida(){
		setEstado("VENCIDA");
	}
	/**
	 * Devuelve la cantidad de productos ofertados
	 * @return double
	 */
	public Double getCantidad(){
		
		return Productos.getCantidad();
	}
	/**
	 * Devuelve el agente que genera la oferta
	 * @return Oferente agente oferente
	 */
	public Oferente getVendedor() {
		return vendedor;
	}
	/**
	 * Asigna el agente que genera la oferta
	 * @param vendedor agente oferente que genera la oferta
	 */
	public void setVendedor(Oferente vendedor) {
		this.vendedor = vendedor;
	}
	/**
	 * Devuelve el punto de oferta que le corresponde
	 * @return CentroUrbano punto de oferta
	 */
	public CentroUrbano getPuntoOferta() {
		return puntoOferta;
	}
	/**
	 * Asigna el punto de oferta que le corresponde
	 * @param puntoOferta CentroUrbano correspondiente al punto de oferta
	 */
	public void setPuntoOferta(CentroUrbano puntoOferta) {
		this.puntoOferta = puntoOferta;
	}
	/**
	 * Devuelve la ubicación de la oferta
	 * @return Coordinate coordenadas de ubicación de la oferta
	 */
	public Coordinate getUbicacion() {
		return ubicacion;
	}
	/**
	 * Asigna la ubicación de la oferta
	 * @param ubicacion coordenadas de ubicación de la oferta
	 */
	public void setUbicacion(Coordinate ubicacion) {
		this.ubicacion = ubicacion;
	}
	/**
	 * Devuelve el identificador de la oferta
	 * @return id int 
	 */
	public int getId() {
		return id;
	}
		
	/**
	 * Asigna el recurso ofertado
	 * @param productos
	 */
	public void setProductos(Recurso productos) {
		Productos = productos;
		this.nombreproducto = productos.getProducto().getNombre();
	}



	/** 
	 * Clase anidada a la que se le delega la funcionalidad <code>obserbable</code> a la clase <code>Oferta</code>
	 * 
	 * @author jdvelezg
	 *
	 */
	public class OfertaTrack extends AgentTrackObservable{
		
		private Double tick;
		
		private final String ofertaID;
		
		private String oferenteID;
		
		private Double precio;
		
		private String producto;
		
		private Double cantidad;
		
		private String estado;
		
		/**
		 * Constructor
		 */
		public OfertaTrack(){
			
			super();
			this.ofertaID = Oferta.this.toString();			
		}
		
		/**
		 * reporta el registro de la oferta
		 * <p>
		 * es llamado en setVigente, setVendida, setEstado, SISaab::generarOrdenCompra
		 */
		public void setOfertaRegistrada(){
			
			this.tick		= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
			this.oferenteID	= Oferta.this.vendedor.toString();
			this.producto	= Oferta.this.getNombreProducto();						
			this.cantidad	= Oferta.this.getCantidad();
			this.precio		= Oferta.this.precio;
			this.estado		= Oferta.this.Estado;
			
			super.setChanged();
			super.notifyObservers(this);
		}

		@Override
		public String dataLineString(String separador) {			
			
			return tick.toString()+separador+ofertaID+separador+oferenteID+separador+producto+separador+precio.toString()+separador+cantidad.toString()+separador+estado+separador;
		}

		@Override
		public String dataLineStringHeader(String separador) {
			
			return "tick"+separador+"Oferta_ID"+separador+"Oferente_ID"+separador+"Producto"+separador+"Precio"+separador+"Cantidad"+separador+"Estado";
		}
				
		
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
