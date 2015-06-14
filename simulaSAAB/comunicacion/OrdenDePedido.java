package simulaSAAB.comunicacion;

import java.util.List;

import repast.simphony.engine.environment.RunEnvironment;
import simulaSAAB.contextos.PlazaDistrital;
import simulaSAAB.contextos.environment.Route;
import simulaSAAB.global.persistencia.AgentTrackObservable;
import simulaSAAB.tareas.EstadosActividad;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

public class OrdenDePedido implements Concepto {
		
	private PlazaDistrital puntoDemanda;
	
	private List<OrdenDeCompra> OrdenesDeCompra;
	
	private List<OrdenDeServicio> OrdenesDeServicio;
	
	public OrdenPedidoTrack OBSERVABLE;

	/**
	 * Contructor
	 */
	public OrdenDePedido() {
		
		this.OBSERVABLE = new OrdenPedidoTrack();
	}
	
	public OrdenDePedido(Demanda demanda, List<OrdenDeCompra> compras, List<OrdenDeServicio> servicios ){
		
		this.puntoDemanda		= demanda.getPuntoDemanda();
		this.OrdenesDeCompra 	= compras;
		this.OrdenesDeServicio	= servicios;
		enlazaOrdenes();
		
		this.OBSERVABLE = new OrdenPedidoTrack();
	}
	
	/**
	 * Relaciona las ordenes de servicio
	 * con la orden de Pedido
	 */
	private void enlazaOrdenes(){
		
		for(OrdenDeServicio orden : OrdenesDeServicio){
			orden.setOrdenPedido(this);
		}
	}
	
	/**
	 * Calcula el costo logistico asociado al pedido.
	 * Toma cada orden de servicio logistico (OSL), calcula la ruta y fija su costo asociado.
	 * Devuelve la suma de los costos asociados a cada OSL.
	 * 
	 * @return Costo logistico del pedido
	 */
	private Double calcularCostoLogistico(){
		
		Double costoLogistico = new Double(0);
		
		for(OrdenDeServicio orden: OrdenesDeServicio){
			
			Double costo		= new Double(0);
			Coordinate origen 	= orden.getOrigen().getGeometria().getCoordinate();
			Coordinate destino	= orden.getDestino().getGeometria().getCoordinate();			
			Route ruta 			= new Route(origen,destino);
			
			try{	
				costo 			= ruta.setRoute();
				costoLogistico += costo;
				
			}catch(Exception e){
				
				System.err.format("ERROR al crear la ruta: "+e.getMessage()+": %s%n", e);
			}
			orden.setCostoServicioLogistico(costo);
		}
		
		return costoLogistico;
	}
	
	private Double calcularPagoTotalAsociado(){
		
		Double pagoTotal = new Double(0);
		
		for(OrdenDeCompra orden:OrdenesDeCompra){
			pagoTotal += orden.getPagoAcordado().getCantidad();
		}
		return pagoTotal;
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
	
	
	public class OrdenPedidoTrack extends AgentTrackObservable{
		
		private Double tick;
		private String puntoDemanda;		
		private String demandaID;
		private Double costoLogisticoPedido;		
		private Double pagoTotalAsociado;
		
		public OrdenPedidoTrack(){
			
			super();
			this.puntoDemanda 	= OrdenDePedido.this.puntoDemanda.getNombre();
			this.demandaID		= OrdenDePedido.this.OrdenesDeCompra.get(0).getDemanda().getComprador().toString();
			
			
		}
		
		/**
		 * Actualiza los valores y reporta a los observadores
		 * Se invoca en recibirPedido() de la clase VendedorFinal y 
		 * en gestionarPedidos() de la clase OperadorRedDemanda
		 */
		public void PedidoEntregado(){
			
			this.tick					= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
			this.costoLogisticoPedido 	= OrdenDePedido.this.calcularCostoLogistico();
			this.pagoTotalAsociado		= OrdenDePedido.this.calcularPagoTotalAsociado();
			
			super.setChanged();
			super.notifyObservers(this);
		}

		@Override
		public String dataLineString(String separador) {
			
			return tick+separador+puntoDemanda+separador+demandaID+separador+costoLogisticoPedido+separador+pagoTotalAsociado+separador;
		}

		@Override
		public String dataLineStringHeader(String separador) {
			
			return "tick"+separador+"punto_Demanda"+separador+"demanda_ID"+separador+"costo_Logistico_Pedido"+separador+"pago_Total_Asociado"+separador;
		}
		
		
		
	}

}
