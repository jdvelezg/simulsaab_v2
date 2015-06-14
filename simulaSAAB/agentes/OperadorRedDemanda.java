package simulaSAAB.agentes;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.graph.RepastEdge;
import simulaSAAB.comunicacion.Demanda;
import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.comunicacion.MensajeACL;
import simulaSAAB.comunicacion.OrdenDeCompra;
import simulaSAAB.comunicacion.OrdenDePedido;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Recurso;
import simulaSAAB.contextos.PlazaDistrital;
import simulaSAAB.contextos.SaabContextBuilder;
import simulaSAAB.tareas.EstadosActividad;
import simulaSAAB.tareas.RegistrarDemandaConsolidada;
import simulaSAAB.tareas.SistemaActividadHumana;

public class OperadorRedDemanda extends VendedorFinal implements Demandante {
	
	private ConcurrentHashMap<Demandante,Demanda> DemandasConsolidables;
	
	private Queue<Demanda> demandasConsolidadas;
	
	private SistemaActividadHumana<Demandante> registrarDemanda;
	
	private boolean operable = false;
	
	

	/**
	 * Constructor
	 */
	public OperadorRedDemanda() {
		
		super();
		this.demandasConsolidadas	= new ConcurrentLinkedQueue<Demanda>();
		this.DemandasConsolidables 	= new ConcurrentHashMap<Demandante,Demanda>();
		this.registrarDemanda		= new RegistrarDemandaConsolidada();		
		super.formarIntenciones();		
	}
	
	public OperadorRedDemanda(PlazaDistrital puntoDemanda) {
		
		super();
		this.demandasConsolidadas	= new ConcurrentLinkedQueue<Demanda>();
		this.DemandasConsolidables 	= new ConcurrentHashMap<Demandante,Demanda>();
		this.registrarDemanda		= new RegistrarDemandaConsolidada();
		this.setPuntoDemanda(puntoDemanda);
		super.formarIntenciones();		
	}
	
	
	/**
	 * Metodo que ejecuta el comportamiento del agente en cada ciclo de reloj enviado por repast
	 * 
	 */
	public void step () {
		
		if(registrarDemanda.getEstado().equalsIgnoreCase(EstadosActividad.DONE.toString())){
			finalizarNutrired();
		}else{
			registrarDemanda.secuenciaPrincipalDeAcciones(this);
		}
					
	}

	@Override
	public Demanda generarDemanda() {
		
		/**
		 * Verifica que no tenga Demandas en cola (ocurriría si hubiesen mas productos).
		 * En caso de no tener, consolida la demanda de los agentes de la nutrired
		 */
		if(this.demandasConsolidadas.isEmpty()){
			
			Map<Producto,Demanda> productosDemandados = new ConcurrentHashMap<Producto,Demanda>();
			
			//itera los agentes de la nutrired
			Iterable<Object> demandantes = SaabContextBuilder.NutriredesNetwork.getAdjacent(this);
			for(Object o: demandantes){
				
				if(o instanceof Demandante){
					
					//solicita demanda al agente y mapea las demandas
					Demandante vendedor = (Demandante)o;
					Demanda demanda		= vendedor.generarDemanda();
					Producto producto 	= demanda.getProducto();
								
					if(DemandasConsolidables.replace(vendedor, demanda)==null){				
						DemandasConsolidables.put(vendedor, demanda);
					}
								
					/**
					 * Consolida las demandas y mapea los productos demandados (necesario cuando existe más de un producto a demandar)
					 */
					if(productosDemandados.containsKey(producto)){
						
						Demanda novaDemanda 	= productosDemandados.get(producto);				
						Double cantidad 		= novaDemanda.getCantidad()+demanda.getCantidad();
						novaDemanda.setCantidad(cantidad);
									
					}else{				
						Demanda novaDemanda 	= new Demanda(producto.getNombre(),demanda.getCantidad(),192,false);//192 ciclos -> 8 dias
						novaDemanda.setPuntoDemanda(getPuntoDemanda());
						novaDemanda.setComprador(this);
						productosDemandados.put(producto, novaDemanda);
					}
				}
					
				
				
			}
			
			/**
			 * Cuando son varios productos los pone en cola y retorna el primero.
			 */
			Iterator<Producto> productos = productosDemandados.keySet().iterator();
			while(productos.hasNext()){
				
				Producto pKey = productos.next();
				this.demandasConsolidadas.add(productosDemandados.get(pKey));
			}			
			
		}		
				
		return demandasConsolidadas.poll();
	}
	
	@Override
	public void gestionarPedidos(){
		
		while(super.pedidosRecibidos()){
			
			OrdenDePedido ordenPedido	= pedidosRecibidos.poll();
			List<OrdenDeCompra> ordenes = ordenPedido.getOrdenesDeCompra();			
			//Actualiza obserbable
			ordenPedido.OBSERVABLE.PedidoEntregado();
			
			for(OrdenDeCompra orden:ordenes){
				
				Producto ProductoComprado 	= orden.getOferta().getProducto();
				Recurso compra				= orden.getDetalleCompra();
				Double precioCompra			= orden.getOferta().getPrecio();
				Double precioUnitario		= precioCompra/compra.getCantidad();
				
				Iterator<Demandante> demandantes = this.DemandasConsolidables.keySet().iterator();
				
				//itera por cada agente representado
				while(demandantes.hasNext()){
					
					Demandante representado 	= demandantes.next();
					Demanda demanda				= DemandasConsolidables.get(representado);					
					//revisa si la demanda corresponde al producto
					if(demanda.getProducto().equals(ProductoComprado)){
						//si: crea un recurso equivalente a la demanda y se lo enrega
						Double cantidadDemandada 	= demanda.getCantidadDemandanda();
						Double monto 				= cantidadDemandada*precioUnitario;
						Dinero dineroDemandante 	= representado.getDinero();
						//Cobra y entrega los productos
						dineroDemandante.subtractCantidad(monto);
						compra.removeCantidad(cantidadDemandada);						
						representado.distribuirProductosEnTienda(new Recurso(ProductoComprado,demanda.getCantidadDemandanda()));
						/*
						 * Destruye el network de consolidacion, y pone al agente en estado RUNNING
						 * para que continue su ciclo de experiencia-acción
						 * 
						 * TODO en caso de varios productos debe haber una iteración diferente. 
						 */
						RepastEdge<Object> edge = SaabContextBuilder.NutriredesNetwork.getEdge(this, representado);
						SaabContextBuilder.NutriredesNetwork.removeEdge(edge);
						representado.setEstado("RUNNING");	
					}					
				}
			}					
		}//End-While
		
		finalizarNutrired();
	}

	@Override
	public void realizarCompra() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * DEPRECATED: SE USA EL SISTEMA DE ACTIVIDAD HUMANA CONFORMAR NUTRIRED
	 * Agrega un agente Demandante a la nutrired
	 * @param agente
	 */
	public void addAgenteRepresentado(Demandante agente){
		//this.DemandasConsolidables
	}
	
	/**
	 * Una vez registrada la demanda consolidada y realizada la compra
	 * distribuye las ordenes de compra para cada agente representado
	 */
	private void finalizarNutrired(){		
				
		//elimina el operador de red de demanda
		SaabContextBuilder.BogotaContext.remove(this);	
	}	
	
	@Override
	public String printActividadVigente(){
		return this.registrarDemanda.getEnunciado();
	}
	
	@Override
	public void setPuntoDemanda(PlazaDistrital puntoDemanda){
		
		super.setPuntoDemanda(puntoDemanda);
		
		GeometryFactory geofact = new GeometryFactory();		
		Coordinate AgentCoord 	= new Coordinate(puntoDemanda.getCentroid().getCoordinate());//center;			
		Point geom 				= geofact.createPoint(AgentCoord);		
		
		SaabContextBuilder.BogotaContext.add(this);
		SaabContextBuilder.SAABGeography.move(this,geom);
		
		/*
		 * Crea un objeto Schedule y agrega el objeto para que su metodo step sea ejecutado 
		 * en cada tick de la simulación
		 */
		double tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		double start = tick < 1 ? 1 : tick + 2;
		int interval = 1;
		
		ScheduleParameters params = ScheduleParameters.createRepeating(start,interval);
		ISchedule sch = RunEnvironment.getInstance().getCurrentSchedule();
		sch.schedule(params, this, "step");
		
		operable = true;
	}
	
	public boolean isOperable(){
		return this.operable;
	}
	

}
