package simulaSAAB.comunicacion;

import java.util.List;

import repast.simphony.engine.environment.RunEnvironment;
import simulaSAAB.contextos.PlazaDistrital;
import simulaSAAB.contextos.environment.Route;
import simulaSAAB.global.VariablesGlobales;
import simulaSAAB.global.persistencia.AgentTrackObservable;
import simulaSAAB.tareas.EstadosActividad;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Representa el concepto de <code>orden de pedido</code> usado en los registros del SISAAB y en la ontología para comunicación entre los agentes 
 * 
 * @author jdvelezg
 *
 */
public class OrdenDePedido implements Concepto {
	
	private int id;
	
	private Demanda demanda;
	
	private PlazaDistrital puntoDemanda;
	
	private List<OrdenDeCompra> OrdenesDeCompra;
	
	private List<OrdenDeServicio> OrdenesDeServicio;
	
	private double costoLogistico;
	
	private double pagoAsociado;
	
	private String estado;
	
	public OrdenPedidoTrack OBSERVABLE = new OrdenPedidoTrack();

	/**
	 * Contructor
	 */
	public OrdenDePedido() {		
		
	}
	/**
	 * Constructor
	 * 
	 * @param demanda Demanda ligada a la orden de pedido
	 * @param compras arreglo de ordenes de compra ligadas a la orden de pedido
	 * @param servicios arreglo de ordenes de servicio ligadas a la orden de pedido
	 */
	public OrdenDePedido(Demanda demanda, List<OrdenDeCompra> compras, List<OrdenDeServicio> servicios ){
		
		this.demanda			= demanda;
		this.puntoDemanda		= this.demanda.getPuntoDemanda();
		this.OrdenesDeCompra 	= compras;
		this.OrdenesDeServicio	= servicios;
		enlazaOrdenes();
		this.OBSERVABLE.PedidoEntregado();	
	}
	
	/**
	 * Relaciona las ordenes de servicio con la orden de Pedido
	 */
	private void enlazaOrdenes(){
		
		for(OrdenDeServicio orden : OrdenesDeServicio){
			orden.setOrdenPedido(this);
		}
	}
	
	/**
	 * Devuelve la suma de los costos asociados a cada orden de servicio logístico
	 * <p>
	 * Calcula el costo logistico asociado al pedido, toma cada orden de servicio logistico (OSL), calcula la ruta y fija su costo asociado	 *  
	 * @return double costo logístico del pedido
	 */
	public Double calcularCostoLogistico(){
		
		Double costoLogistico = new Double(0);
		
		for(OrdenDeServicio orden: OrdenesDeServicio){
			
			Double costo		= new Double(0);
			Coordinate origen 	= orden.getOrigen().getRoadAccess();
			Coordinate destino	= orden.getDestino().getRoadAccess();			
			Route ruta 			= new Route(origen,destino);
			
			try{	
				costo 			= ruta.setRoute()*VariablesGlobales.COSTO_PROMEDIO_TRANSPORTE_CARGA_POR_METRO;
				costoLogistico += costo;
				
			}catch(Exception e){
				
				System.err.format("ERROR al crear la ruta: "+e.getMessage()+": %s%n", e);
			}
			orden.setCostoServicioLogistico(costo);
		}
		
		this.costoLogistico = costoLogistico;
		
		return costoLogistico;
	}
	
	/**
	 * calcula el pago total asociado al pedido
	 * @return double Pago asociado al pedido
	 */
	public Double calcularPagoTotalAsociado(){
		
		Double pagoTotal = new Double(0);
		
		for(OrdenDeCompra orden:OrdenesDeCompra){
			pagoTotal += orden.getPagoAcordado().getCantidad();
		}
		
		this.pagoAsociado = pagoTotal+calcularCostoLogistico();
		
		return pagoAsociado;
	}
	/**
	 * Devuelve el punto de demanda asociado a la orden de pedido
	 * @return Plaza distrital 
	 */
	public PlazaDistrital getPuntoDemanda() {
		return puntoDemanda;
	}
	/**
	 * Asigna el punto de demanda asociado a la orden de pedido
	 * @param puntoDemanda punto de demanda correspondiente a la orden de pedido
	 */
	public void setPuntoDemanda(PlazaDistrital puntoDemanda) {
		this.puntoDemanda = puntoDemanda;
	}
	/**
	 * Devuelve un arreglo con las ordenes de compra asociadas a la orden de pedido
	 * @return List<OrdenDeCompra>
	 */
	public List<OrdenDeCompra> getOrdenesDeCompra() {
		return OrdenesDeCompra;
	}
	/**
	 * Asigna las ordenes de compra asociadas a la orden de pedido
	 * @param ordenesDeCompra ordenes de compra asociadas a la orden de pedido
	 */
	public void setOrdenesDeCompra(List<OrdenDeCompra> ordenesDeCompra) {
		OrdenesDeCompra = ordenesDeCompra;
	}
	/**
	 * Devuelve las ordenes de servicio asociadas a la orden de pedido
	 * @return List<OrdenDeServicio>
	 */
	public List<OrdenDeServicio> getOrdenesDeServicio() {
		return OrdenesDeServicio;
	}
	/**
	 * Asigna las ordenes de servcio asociadas a la orden de pedido
	 * @param ordenesDeServicio ordenes de servicio asociadas a la orden de pedido
	 */
	public void setOrdenesDeServicio(List<OrdenDeServicio> ordenesDeServicio) {
		OrdenesDeServicio = ordenesDeServicio;
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
	 * Devuelve el costo logistico asociado a la orden de pedido
	 * @return costoLogistico double
	 */
	public double getCostoLogistico() {
		return costoLogistico;
	}
	/**
	 * Devuelve el pago asociado a la orden de pedido
	 * @return pagoAsociado double
	 */
	public double getPagoAsociado() {
		return pagoAsociado;
	}
	/**
	 * Devuelve el estado de la orden de pedido
	 * @return estado String
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * Fija el estado de la orden
	 * @param estado string, nuevo estado de la orden
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}


	/**
	 * Clase anidada a la que se le delega la funcionalidad <code>obserbable</code> de la clase {@link OrdenDePedido}
	 * 
	 * @author jdvelezg
	 *
	 */
	public class OrdenPedidoTrack extends AgentTrackObservable{
		
		private Double tick;
		private String OrdenPedidoID;
		private String puntoDemanda;		
		private String demandaID;
		private Double costoLogisticoPedido;		
		private Double pagoTotalAsociado;
		
		public OrdenPedidoTrack(){
			
			super();			
						
		}
		
		/**
		 * Actualiza los valores y reporta a los observadores
		 * <p>
		 * Se invoca en recibirPedido() de la clase VendedorFinal, en gestionarPedidos() de la clase OperadorRedDemanda
		 */
		public void PedidoEntregado(){
			
			this.tick					= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
			this.OrdenPedidoID			= OrdenDePedido.this.toString();
			this.demandaID				= OrdenDePedido.this.demanda.toString();
			this.puntoDemanda 			= OrdenDePedido.this.puntoDemanda.getNombre();
			this.costoLogisticoPedido 	= OrdenDePedido.this.costoLogistico;
			this.pagoTotalAsociado		= OrdenDePedido.this.pagoAsociado;
			
			super.setChanged();
			super.notifyObservers(this);
		}

		@Override
		public String dataLineString(String separador) {
			
			return tick+separador+OrdenPedidoID+separador+puntoDemanda+separador+demandaID+separador+costoLogisticoPedido+separador+pagoTotalAsociado+separador;
		}

		@Override
		public String dataLineStringHeader(String separador) {
			
			return "tick"+separador+"Orden_pedido_id"+separador+"punto_Demanda"+separador+"demanda_ID"+separador+"costo_Logistico_Pedido"+separador+"pago_Total_Asociado"+separador;
		}		
		
	}

}
