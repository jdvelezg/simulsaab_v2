package simulaSAAB.agentes;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

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
import simulaSAAB.global.VariablesGlobales;
import simulaSAAB.tareas.EstadosActividad;
import simulaSAAB.tareas.RegistrarDemandaConsolidada;
import simulaSAAB.tareas.SistemaActividadHumana;

/**
 * Representa el agente <code>Operador de red de demanda</code>
 * <p>
 * Agente Inteligente que opera como un <code>demandante</code> que representa los intereses de otros agentes demandantes
 * 
 * @author jdvelezg
 *
 */
public class OperadorRedDemanda extends VendedorFinal implements Demandante {
		
	/**
	* Registro de la clase usado para depuración <code>Debugging</code>
	*/
	private static Logger LOGGER = Logger.getLogger(OperadorRedDemanda.class.getName());
	/**
	 * Demandas de cada uno de los demandantes representados
	 */
	private ConcurrentHashMap<Demandante,Demanda> DemandasConsolidables;
	/**
	 * Demandas generadas por el agente
	 */
	private Queue<Demanda> demandasConsolidadas;
	/**
	 * Instancia del <code>MPA RegistrarDemandaConsolidada</code> 
	 */
	private SistemaActividadHumana<Demandante> registrarDemanda;
	/**
	 * indica si el agente es operable
	 */
	private boolean operable = false;
	
	private int delay = 0;
	
	

	/**
	 * Constructor
	 */
	public OperadorRedDemanda(int id) {
		
		super(id);
		this.demandasConsolidadas	= new ConcurrentLinkedQueue<Demanda>();
		this.DemandasConsolidables 	= new ConcurrentHashMap<Demandante,Demanda>();
		this.registrarDemanda		= new RegistrarDemandaConsolidada();		
		super.formarIntenciones();		
	}
	
	/**
	 * Constructor de la clase
	 * @param puntoDemanda plazaDistrital que representa el punto de demanda del agente
	 */
	public OperadorRedDemanda(int id, PlazaDistrital puntoDemanda) {
		
		super(id);
		this.demandasConsolidadas	= new ConcurrentLinkedQueue<Demanda>();
		this.DemandasConsolidables 	= new ConcurrentHashMap<Demandante,Demanda>();
		this.registrarDemanda		= new RegistrarDemandaConsolidada();
		this.setPuntoDemanda(puntoDemanda);
		super.formarIntenciones();		
	}
	
	
	/**
	 * Metodo que ejecuta el comportamiento del agente en cada ciclo <code>Tick</code>
	 * <p>
	 * Itera la actividad <code>RegistrarDemandaConsolidada</code> 
	 */
	@Override
	public void step () {
		/*
		 * actua cuando tenga un centro de demanda fijado.
		 * delay da tiempo a los agentes de formar la nutrired antes de actuar (50 ticks)
		 */
		if(isOperable() && delay>50){
			
			if(registrarDemanda.getEstado().equalsIgnoreCase(EstadosActividad.DONE.toString())){
				finalizarNutrired();				
			}else{
				registrarDemanda.secuenciaPrincipalDeAcciones(this);
			}
		}else{
			delay++;
		}
	}

	@Override
	public Demanda generarDemanda() {
		
		/**
		 * Verifica que no tenga Demandas en cola (ocurriría si hubiesen mas productos).
		 * En caso de no tener, consolida la demanda de los agentes de la nutrired
		 */
		if(this.demandasConsolidadas.isEmpty()){
			
			Map<String,Demanda> productosDemandados = new ConcurrentHashMap<String,Demanda>();			
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
					Double cantidad = new Double(0);				
					if(productosDemandados.containsKey(producto.getNombre())){
						
						Demanda novaDemanda = productosDemandados.get(producto.getNombre());				
						cantidad 			= novaDemanda.getCantidad().doubleValue()+demanda.getCantidad().doubleValue();				
						novaDemanda.setCantidad(cantidad);
									
					}else{	
						cantidad = demanda.getCantidad().doubleValue();				
						Demanda novaDemanda 	= new Demanda(producto.getNombre(),cantidad,VariablesGlobales.TICKS_VIGENCIA_DEMANDA,false);
						novaDemanda.OBSERVABLE.addObserver(SaabContextBuilder.OBSERVADOR);
						novaDemanda.setPuntoDemanda(getPuntoDemanda());
						novaDemanda.setComprador(this);
						productosDemandados.put(producto.getNombre(), novaDemanda);
					}									
				}
			}//EndFor Demandantes
			
			/**
			 * Cuando son varios productos los pone en cola y retorna el primero.
			 */
			Iterator<String> productos = productosDemandados.keySet().iterator();
			while(productos.hasNext()){
				
				String pKey = productos.next();
				this.demandasConsolidadas.offer(productosDemandados.get(pKey));
			}			
			
		}		
				
		return demandasConsolidadas.poll();
	}
	
	@Override
	public void gestionarPedidos(){
		
		while(pedidosRecibidos()){
			
			OrdenDeCompra orden			= pedidosRecibidos.poll();				
			Producto ProductoComprado 	= orden.getOferta().getProducto();
			Recurso compra				= orden.getDetalleCompra();
			Dinero precioCompra			= orden.getPagoAcordado();
			Double precioUnitario		= precioCompra.getCantidad()/compra.getCantidad();
			
			Iterator<Demandante> demandantes = this.DemandasConsolidables.keySet().iterator();
		
			//itera por cada agente representado
			while(demandantes.hasNext()){
				
				Demandante representado 	= demandantes.next();
				Demanda demanda				= DemandasConsolidables.get(representado);
				Producto productoRequerido	= demanda.getProducto();
				//revisa si la demanda corresponde al producto
				if(productoRequerido.equals(ProductoComprado)){
					//si: crea un recurso equivalente a la demanda y se lo enrega
					Double cantidadDemandada 	= demanda.getCantidadDemandanda();
					Double monto 				= cantidadDemandada*precioUnitario;
					//Cobra y entrega los productos
					representado.getDinero().subtractCantidad(monto);					
					
					//compra.removeCantidad(cantidadDemandada);					
					representado.distribuirProductosEnTienda(new Recurso(ProductoComprado,cantidadDemandada),monto.doubleValue());
					//LOGGER.info(representado.toString()+" demando: "+cantidadDemandada+" comprado a: "+monto.toString());
				}					
			}
								
		}//End-While
	}

	@Override
	public void realizarCompra() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Agrega un agente Demandante a la nutrired
	 * @deprecated La nutrired se conforma como parte de las actividades del <code>MPA</code>	 * 
	 * @param agente agente a agregar a la nutrired
	 */
	public void addAgenteRepresentado(Demandante agente){
		//this.DemandasConsolidables
	}
	
	/**
	 * Distribuye las ordenes de compra para cada agente representado una vez registrada la demanda consolidada y realizada la compra	 * 
	 */
	private void finalizarNutrired(){			
		
		/*
		 * Destruye el network de consolidacion, y pone al agente en estado RUNNING
		 * para que continue su ciclo de experiencia-acción
		 * 
		 * TODO en caso de varios productos debe haber una iteración diferente. 
		 */
		try{			
			Iterable<Object> demandantes = SaabContextBuilder.NutriredesNetwork.getAdjacent(this);
			for(Object o: demandantes){
				
				if(o instanceof Demandante){				
				
					Demandante representado = (Demandante)o;			
					representado.setEstado("RUNNING");
				}
				RepastEdge<Object> edge = SaabContextBuilder.NutriredesNetwork.getEdge(this, o);			
				SaabContextBuilder.NutriredesNetwork.removeEdge(edge);
			}		
			//elimina el operador de red de demanda, y lo elimina del schedule
			operable = false;
			SaabContextBuilder.BogotaContext.remove(this);
			
		}catch(Exception e){
			//no hace nada
		}
			
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
		//NO HACE FALTA - POR HERENCIA EL SCHEDULER LO EJECUTA
		
		/*double tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		double start = tick < 1 ? 1 : tick + 2;
		int interval = 2;
		
		ScheduleParameters params = ScheduleParameters.createRepeating(start,interval);
		ISchedule sch = RunEnvironment.getInstance().getCurrentSchedule();
		sch.schedule(params, this, "step");*/
		
		operable = true;
	}
	
	/**
	 * Indica si el agente puede iniciar su operación al tener todos sus parámetros internos configurados para operar la nutrired
	 * @return booelan <code>true</code> en caso de ser operable
	 */
	public boolean isOperable(){
		return this.operable;
	}
	

}
