package simulaSAAB.comunicacion;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.engine.environment.RunEnvironment;
import simulaSAAB.comunicacion.Oferta.OfertaTrack;
import simulaSAAB.contextos.AmbienteLocal;
import simulaSAAB.contextos.CentroUrbano;
import simulaSAAB.contextos.NodoSaab;
import simulaSAAB.global.persistencia.AgentTrackObservable;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Representa el concepto de <code>orden de servicio</code> usado en los registros del SISAAB y en la ontología para comunicación entre los agentes 
 * <p>
 * Establece la relación de transporte físico de productos entre un operador logístico y la plataforma o entre el operador logístico, el oferente y el demandante.
 * 
 * @author jdvelezg
 *
 */
public class OrdenDeServicio implements Concepto {
	
	private int id;
	
	public final OrdenServicioTrack OBSERVABLE = new OrdenServicioTrack(); 
	
	private Recurso ProductoTransportado;
	
	private List<OrdenDeCompra> ordenesCompra;
	
	private CentroUrbano PuntoOrigen;
	
	private NodoSaab PuntoDestino;
	
	private OrdenDePedido OrdenPedido;
	
	private Double costoServicioLogistico;
	
	private String estado;

	/**
	 * Constructor
	 */
	public OrdenDeServicio() {
		this.ordenesCompra = new ArrayList<OrdenDeCompra>();
		estado = "CREADA";
	}
	/**
	 * Constructor
	 * 
	 * @param punto punto de oferta o demanda asociado a la orden de servicio logístico
	 * @param ofs oferta asociada a la orden de servicio logístico
	 * @param dem demanda asociado a la orden de servicio logístico
	 */
	public OrdenDeServicio(CentroUrbano punto, List<OrdenDeCompra> ofs, Demanda dem){
		
		this.ordenesCompra			= ofs;
		this.ProductoTransportado 	= setProductoTransportado(ofs);
		this.PuntoOrigen			= punto;
		this.PuntoDestino			= dem.getPuntoDemanda();
		estado = "CREADA";
	}
	/**
	 * Asigna las ordenes de compra asociadas a la orden de servicio logístico
	 * @param ofs oferta asociada a la orden de servicio logístico
	 * @return Recurso
	 */
	private Recurso setProductoTransportado(List<OrdenDeCompra> ofs){
		
		
		Double cantidad		= new Double(0);
		Producto producto	= null;
		
		
		for(OrdenDeCompra o: ofs){
			
			if(producto==null)
				producto = o.getOferta().getProducto();
			
			cantidad += o.getOferta().getCantidad();
		}
		
		return new Recurso(producto,cantidad);
		
	}
	/**
	 * Devuelve el producto asociado a la orden de servicio logístico
	 * @return
	 */
	public Recurso getProductoTransportado() {
		return ProductoTransportado;
	}
	/**
	 * Asigna el producto transportado asociado a la orden de servicio logístico
	 * @param productoTransportado productos asociados  a la orden de servicio logístico
	 */
	public void setProductoTransportado(Recurso productoTransportado) {
		ProductoTransportado = productoTransportado;
	}
	/**
	 * Devuelve un arreglo de ofertas asociadas a la orden de servicio logístico
	 * @return List<OrdenDeCompra>
	 */
	public List<OrdenDeCompra> getOfertas() {
		return ordenesCompra;
	}
	/**
	 * Devuelve un arreglo de ordenes de compra asociadas a la orden de servicio logístico
	 * @return List<OrdenDeCompra>
	 */
	public List<OrdenDeCompra> getOrdenesCompra() {
		return ordenesCompra;
	}	
	/**
	 * Asigna las ordenes de compra asociadas a la orden de servicio logístico
	 * @param ofertas arreglo de odenes de compra asociadas a la orden de servicio logístico
	 */
	public void setOfertas(List<OrdenDeCompra> ofertas) {
		this.ordenesCompra = ofertas;
	}
	/**
	 * Devuelve la geometria del punto de origen asociado a la orden de servicio logístico
	 * @return Geometry geometria del punto de origen
	 */
	public Geometry getPuntoOrigen() {
		return PuntoOrigen.getGeometria().getCentroid();
	}
	/**
	 * Devuelve el punto de origen asociado a la orden de servicio logístico
	 * @return CentroUrbano centro urbano punto de origen asociado a la orden de servicio logístico
	 */
	public CentroUrbano getOrigen(){
		return this.PuntoOrigen;
	}
	/**
	 * Asigna el punto de origen asociado a la orden de servicio logístico
	 * @param puntoOrigen centro urbano punto de origen asociado a la orden de servicio logístico
	 */
	public void setPuntoOrigen(CentroUrbano puntoOrigen) {
		PuntoOrigen = puntoOrigen;
	}
	/**
	 * Devuelve la geometria del punto de destino asociado a la orden de servicio logístico
	 * @return Geometry geometria del punto de destino
	 */
	public Geometry getPuntoDestino() {
		return PuntoDestino.getGeometria();
	}
	/**
	 * Devuelve el punto de destino asociado a la orden de servicio logístico
	 * @return NodoSaab nodo saab destino asociado a la orden de servicio logístico
	 */
	public NodoSaab getDestino(){
		return this.PuntoDestino;
	}
	/**
	 * Asigna el punto de destino asociado a la orden de servicio logístico
	 * @param puntoDestino NodoSaab destino asociado a la orden de servicio logístico
	 */
	public void setPuntoDestino(NodoSaab puntoDestino) {
		PuntoDestino = puntoDestino;
	}
	/**
	 * Devuelve el estado de la orden de servicio logístico
	 * @return string
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * Asigna el estado de la orden de servicio logístico
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
		this.OBSERVABLE.costoFijado();
	}
	/**
	 * Devuelve la orden de pedido asociada a la orden de servicio logístico
	 * @return
	 */
	public OrdenDePedido getOrdenPedido() {
		return OrdenPedido;
	}
	/**
	 * Asigna la orden de pedido asociada a la orden de servicio logístico
	 * @param ordenPedido OrdenDePedido, orden de pedido asociada a la orden de servicio logístico
	 */
	public void setOrdenPedido(OrdenDePedido ordenPedido) {
		OrdenPedido = ordenPedido;
	}
	/**
	 * Devuelve el costo asociada a la  orden de servicio logístico
	 * @return double
	 */
	public Double getCostoServicioLogistico() {
		return costoServicioLogistico;
	}
	/**
	 * Asigna el costo logístico asociado a la orden de servicio logístico
	 * @param costoServicioLogistico double costo del servicio
	 */
	public void setCostoServicioLogistico(Double costoServicioLogistico) {
		this.costoServicioLogistico = costoServicioLogistico;
		ajustaPagoOrdenesDeCompra();
		//OBSERVABLE.costoFijado();
	}
	
	/**
	 * Ajusta el pago asociado a la orden de compra, agregando el costo del servicio logístico
	 * <p>
	 * Lo distribuye en el número de ordenes de compra que componen la orden de servicio
	 */
	private void ajustaPagoOrdenesDeCompra(){
		
		int total_ordenes 		= this.ordenesCompra.size();
		double costo_adjudicado	= this.costoServicioLogistico/total_ordenes;
		
		for(OrdenDeCompra o: this.ordenesCompra){
			o.addCostoLogistico(costo_adjudicado);
		}
	}
	/**
	 * Asigna el estado de la orden como 'atendida'
	 */
	public void setAtendida(){
		setEstado("Atendida");
	}
	/**
	 * Devuelve <code>true</code> si la orden no ha sido gestionada por un agnete {@link OperadorLogistico}
	 * @return boolean
	 */
	public boolean isPendiente(){		
		return !(estado.equalsIgnoreCase("Atendida"));
	}
	/**
	 * Devuelve el identificador de la orden	
	 * @return id int
	 */
	public int getId() {
		return id;
	}
	/**
	 * Asigna un identificador a la orden
	 * @param id int
	 */
	public void setId(int id) {
		this.id = id;
	}


	/**
	 * Clase anidada a la que se le delega la funcionalidad <code>obserbable</code> de la clase {@link OrdenDeServicio}
	 * 
	 * @author jdvelezg
	 */
	public class OrdenServicioTrack extends AgentTrackObservable{
				
		private String ordenServicioID;
		private String ordenPedidoID;
		private Double tick;
		private String origen;
		private String destino;
		private Double costoServicio;
		private String estado;
		
		/**
		 * Constructor
		 */
		public OrdenServicioTrack(){			
			super();				
		}
		
		/**
		 * Actualiza los valores y reporta a los observadores una vez fijado el costo de la orden de servicio logístico
		 */
		public void costoFijado(){
			
			this.ordenServicioID=OrdenDeServicio.this.toString();
			this.tick			= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
			this.costoServicio	= OrdenDeServicio.this.getCostoServicioLogistico();	
			this.ordenPedidoID 	= OrdenDeServicio.this.OrdenPedido.toString();
			this.origen			= OrdenDeServicio.this.PuntoOrigen.getNombre();
			this.destino		= OrdenDeServicio.this.PuntoDestino.getNombre();
			this.estado			= OrdenDeServicio.this.estado;
			
			super.setChanged();
			super.notifyObservers(this);
		}

		@Override
		public String dataLineString(String separador) {

			return tick.toString()+separador+ordenServicioID+separador+ordenPedidoID+separador+origen+separador+destino+separador+costoServicio.toString()+separador+estado+separador;
		}

		@Override
		public String dataLineStringHeader(String separador) {
			
			return "tick"+separador+"Orden_Servicio_id"+separador+"ordenPedido_ID"+separador+"Origen"+separador+"Destino"+separador+"Costo_Servicio"+separador+"Estado"+separador;
		}

		public Double getTick() {
			return tick;
		}

		public void setTick(Double tick) {
			this.tick = tick;
		}

		public String getOrigen() {
			return origen;
		}

		public void setOrigen(String origen) {
			this.origen = origen;
		}

		public String getDestino() {
			return destino;
		}

		public void setDestino(String destino) {
			this.destino = destino;
		}

		public Double getCostoServicio() {
			return costoServicio;
		}

		public void setCostoServicio(Double costoServicio) {
			this.costoServicio = costoServicio;
		}

		public String getOrdenPedidoID() {
			return ordenPedidoID;
		}
		
	}

}
