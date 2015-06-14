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
 * Clase OrdenDeServicio
 * 
 * Establece la relación de transporte físico de productos entre un operador logístico y la plataforma,
 * o entre el operador logístico y el oferente, y a su vez demandante.
 * 
 * @author dampher
 *
 */
public class OrdenDeServicio implements Concepto {
	
	public final OrdenServicioTrack OBSERVABLE = new OrdenServicioTrack(); 
	
	private Recurso ProductoTransportado;
	
	private List<Oferta> ofertas;
	
	private CentroUrbano PuntoOrigen;
	
	private NodoSaab PuntoDestino;
	
	private OrdenDePedido OrdenPedido;
	
	private Double costoServicioLogistico;
	
	private String estado;

	/**
	 * Constructor
	 */
	public OrdenDeServicio() {
		this.ofertas = new ArrayList<Oferta>();
	}
	
	public OrdenDeServicio(CentroUrbano punto, List<Oferta> ofs, Demanda dem){
		
		this.ofertas				= ofs;
		this.ProductoTransportado 	= setProductoTransportado(ofs);
		this.PuntoOrigen			= punto;
		this.PuntoDestino			= dem.getPuntoDemanda();
	}
	
	private Recurso setProductoTransportado(List<Oferta> ofs){
		
		
		Double cantidad		= new Double(0);
		Producto producto	= null;
		
		
		for(Oferta o: ofs){
			
			if(producto==null)
				producto = o.getProducto();
			
			cantidad += o.getCantidad();
		}
		
		return new Recurso(producto,cantidad);
		
	}

	public Recurso getProductoTransportado() {
		return ProductoTransportado;
	}

	public void setProductoTransportado(Recurso productoTransportado) {
		ProductoTransportado = productoTransportado;
	}

	public List<Oferta> getOfertas() {
		return ofertas;
	}

	public void setOfertas(List<Oferta> ofertas) {
		this.ofertas = ofertas;
	}

	public Geometry getPuntoOrigen() {
		return PuntoOrigen.getGeometria().getCentroid();
	}
	
	public CentroUrbano getOrigen(){
		return this.PuntoOrigen;
	}

	public void setPuntoOrigen(CentroUrbano puntoOrigen) {
		PuntoOrigen = puntoOrigen;
	}

	public Geometry getPuntoDestino() {
		return PuntoDestino.getGeometria();
	}
	
	public NodoSaab getDestino(){
		return this.PuntoDestino;
	}

	public void setPuntoDestino(NodoSaab puntoDestino) {
		PuntoDestino = puntoDestino;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public OrdenDePedido getOrdenPedido() {
		return OrdenPedido;
	}

	public void setOrdenPedido(OrdenDePedido ordenPedido) {
		OrdenPedido = ordenPedido;
	}

	public Double getCostoServicioLogistico() {
		return costoServicioLogistico;
	}

	public void setCostoServicioLogistico(Double costoServicioLogistico) {
		this.costoServicioLogistico = costoServicioLogistico;
		OBSERVABLE.costoFijado();
	}
	
	/**
	 * 
	 * @author dampher
	 *
	 */
	public class OrdenServicioTrack extends AgentTrackObservable{
				
		private String ordenPedidoID;
		private Double tick;
		private String origen;
		private String destino;
		private Double costoServicio;
		
		/**
		 * Constructor
		 */
		public OrdenServicioTrack(){			
			super();				
		}
		
		/**
		 * Actualiza los valores y reporta a los observadores
		 */
		public void costoFijado(){
			
			this.tick			= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
			this.costoServicio	= OrdenDeServicio.this.getCostoServicioLogistico();	
			this.ordenPedidoID 	= OrdenDeServicio.this.OrdenPedido.toString();
			this.origen			= OrdenDeServicio.this.PuntoOrigen.getNombre();
			this.destino		= OrdenDeServicio.this.PuntoDestino.getNombre();
			
			super.setChanged();
			super.notifyObservers(this);
		}

		@Override
		public String dataLineString(String separador) {

			return tick.toString()+separador+ordenPedidoID+separador+origen+separador+destino+separador+costoServicio.toString()+separador;
		}

		@Override
		public String dataLineStringHeader(String separador) {
			
			return "tick"+separador+"ordenPedido_ID"+separador+"Origen"+separador+"Destino"+separador+"Costo_Servicio"+separador;
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
