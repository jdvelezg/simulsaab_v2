/**
 * 
 */
package simulaSAAB.agentes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Logger;

import bsh.This;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import simulaSAAB.comunicacion.Demanda;
import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.comunicacion.Experiencia;
import simulaSAAB.comunicacion.MensajeACL;
import simulaSAAB.comunicacion.Oferta;
import simulaSAAB.comunicacion.OrdenDeCompra;
import simulaSAAB.comunicacion.OrdenDePedido;
import simulaSAAB.comunicacion.OrdenDeServicio;
import simulaSAAB.comunicacion.Preposicion;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.comunicacion.EjecutarAccionConProposito;
import simulaSAAB.comunicacion.Recurso;
import simulaSAAB.contextos.PlazaDistrital;
import simulaSAAB.contextos.SaabContextBuilder;
import simulaSAAB.global.PropositosFactory;
import simulaSAAB.global.VariablesGlobales;
import simulaSAAB.global.persistencia.AgentTrackObservable;
import simulaSAAB.inteligencia.Cerebro;
import simulaSAAB.tareas.ConformarNutrired;
import simulaSAAB.tareas.ConsolidarDemanda;
import simulaSAAB.tareas.EstadosActividad;
import simulaSAAB.tareas.ProcesoAgenteHumano;
import simulaSAAB.tareas.RegistrarDemandaUnitaria;
import simulaSAAB.tareas.SistemaActividadHumana;

/**
 * Representa el agente <code>Vendedor final</code>
 * <p>
 * Agente inteligente cuyas acciones estan encaminadas a la compra de productos al por mayor para su posterior venta al consumidor final
 * 
 * @author jdvelezg
 *
 */
public class VendedorFinal implements AgenteInteligente, Demandante {
	
	/**
	 * Identificador único del agente
	 */
	private final int id;
	/**
	* Registro de la clase usado para depuración <code>Debugging</code>
	*/
	private static Logger LOGGER = Logger.getLogger(VendedorFinal.class.getName());
	/**
	 * rol del agente
	 */
	public static String ROL ="vendedor";
	/**
	 * intención del agente
	 */
	public static String INTENCION ="vender";
	/**
	 * objetivo del agente
	 */
	private static String OBJETIVO;
	
	public final PrecioFinalTrack OBSERVABLE = new PrecioFinalTrack();
	/**
	 * propósito vigente del agente
	 */
	private Proposito PropositoVigente;
	/**
	 * dinero del agente
	 */
	private simulaSAAB.comunicacion.Dinero Dinero;	
	
	private Cerebro CerebroVendedor;
	/**
	 * Instancia del ProcesoAgenteHumano
	 */
	private ProcesoAgenteHumano ProcesoHumanoDefinido;
	/**
	 * actividad vigente del agente
	 */
	private SistemaActividadHumana ActividadVigente;
	/**
	 * mensajes recibidos por el agente
	 */
	private Queue<MensajeACL> MensajesRecibidos;
	
	protected Queue<OrdenDeCompra> pedidosRecibidos;
	
	private ConcurrentHashMap<Integer, Queue<MensajeACL>> MapRespuestas;
	
	private List<MensajeACL> MensajesEnviados;
	
	private boolean MensajeAceptado;
	/**
	 * conjunto experiencias obtenidas por el agente
	 */
	private List<Experiencia> Experiencia;
	/**
	 * conjunto de tiendas poseidas por el agente
	 */
	private List<Tienda> Tiendas;
	/**
	 * punto de demanda del agente
	 */
	protected PlazaDistrital puntoDemanda;
	/**
	 * demandas generadas por el agente
	 */
	private List<Demanda> Demandas;
	
	private List<Recurso> Productos;
	
	private List<Producto> ProductosViablesPercibidos;
	/**
	 * actividades ejecutables por el agente 
	 */
	private List<SistemaActividadHumana> ActividadesEjecutables;
	/**
	 * mayor utilidad obtenida por el agente
	 */
	private Double MayorUtilidadObtenida;
	/**
	 * última utilidad obtenida por el agente
	 */
	private Double UltimaUtilidadObtenida;
	/**
	 * estado del agente
	 */
	private String Estado;
	/**
	 * intención de consolidación del agente
	 */
	private boolean intencionDeConsolidacion;
	
	/**
	 * Constructor de la clase
	 */
	public VendedorFinal(int id){
		
		this.id = id;
		
		Experiencia 		= new ArrayList<Experiencia>();		
		Tiendas				= new ArrayList<Tienda>();		
		Demandas			= new ArrayList<Demanda>();		
		Productos 			= new ArrayList<Recurso>();
		
		ProductosViablesPercibidos 	= new ArrayList<Producto>();		
		ActividadesEjecutables 		= new ArrayList<SistemaActividadHumana>();		
		
		CerebroVendedor			= new Cerebro(this);
		CerebroVendedor.OBSERVABLE.addObserver(SaabContextBuilder.OBSERVADOR);
		ProcesoHumanoDefinido 	= new ProcesoAgenteHumano();
		
		this.OBSERVABLE.addObserver(SaabContextBuilder.OBSERVADOR);
		
		Dinero					= new Dinero(new Double(0),"COP");
		UltimaUtilidadObtenida 	= new Double(0);
		MayorUtilidadObtenida	= new Double(0);	
		
		this.pedidosRecibidos	= new ConcurrentLinkedDeque<OrdenDeCompra>();
		
		this.MensajesEnviados	= new ArrayList<MensajeACL>(100);
		this.MapRespuestas		= new ConcurrentHashMap<Integer, Queue<MensajeACL>>();
		this.MensajesRecibidos	= new ConcurrentLinkedDeque<MensajeACL>();
		
		Estado 	= "IDLE";				
	}

	
	/**
	 * Metodo que ejecuta el comportamiento del agente en cada ciclo de reloj <code>Tick</code>
	 * <p>
	 * Verifica que el agente no tenga ningun mensaje pendiente a procesar, luego ejecuta el proceso humano definido para un agente humano activo en el mundo
	 * <code>start = 500, interval = 1</code>
	 */
	@ScheduledMethod (start = 500, interval = 1)
	public void step () {

		if(ProcesoHumanoDefinido.getEstado().equalsIgnoreCase(EstadosActividad.READY.toString()) && !this.MensajesRecibidos.isEmpty()){
			//Procesa el mensaje que tiene en espera
			atenderMensajes();
			
		}else if(this.Estado.equalsIgnoreCase("WAITING") && this.ActividadVigente instanceof ConformarNutrired){
			//continua esperando proceso de consolidacion
		}else{
			//System.out.println("Dinero: "+this.Dinero.getCantidad());
			ProcesoHumanoDefinido.secuenciaPrincipalDeAcciones(this);	
		}			
	}
	
	
	
	/* (non-Javadoc)
	 * @see simulaSAAB.agentes.AgenteInteligente#atribuirSignificadoAlMundoPercibido()
	 */
	@Override
	public void percibirMundoSelectivamente() {
		
		//Resetea el listado de actividades viables percibidas
		ActividadesEjecutables.clear();
		//Consulta tareas ejecutables en el ambiente			
		for(Tienda local : Tiendas){
			
			List<SistemaActividadHumana> actividadesAmbientales = local.getAmbiente().getActividadesViables();
			ProductosViablesPercibidos.addAll(local.getAmbiente().getProductosAgricolasViables());
			
			if(this.PropositoVigente==null)
				formarIntenciones();
			
			//Filtra su proposito
			for(SistemaActividadHumana a: actividadesAmbientales){
				
				if(a.getProposito().compare(PropositoVigente))
					this.ActividadesEjecutables.add(a.getInstance());				
			}		
		}	
	}

	@Override
	public void formarIntenciones() {
		
		if(PropositoVigente==null)
			this.PropositoVigente = new PropositosFactory(VendedorFinal.ROL,VendedorFinal.INTENCION).getProposito();		
	}

	@Override
	public void tomarDecisiones() {
		
		if(ActividadesEjecutables.size()>0){
			
			this.ActividadVigente 		= CerebroVendedor.tomarDecision(ActividadesEjecutables).getInstance();
			this.UltimaUtilidadObtenida = new Double(0);
		}else
			System.out.println("Imposible tomar decision, No actividad viable size: "+ ActividadesEjecutables.size());
	}

	@Override
	public void actuar() {
	
		ActividadVigente.secuenciaPrincipalDeAcciones(this);
	}

	@Override
	public void juzgarMundoSegunEstandares() {

		Experiencia exp = CerebroVendedor.evaluarExperiencia();		
		if(!Experiencia.contains(exp)){			
			this.addExperiencia(exp);
		}		
	}
	
	@Override
	public Demanda generarDemanda() {
		
		/**
		 * Obtiene el primer producto de la lista de productos percibidos y genera una demanda. 
		 * Para calcular la cantidad estimada de producto demandado, divide su dinero por el valor registrado del producto
		 * 
		 * TODO cuando existan varios productos en el ambiente, debe escoger o iterar los productos que va a demandar
		 */
		Producto producto 			= ProductosViablesPercibidos.get(0);
		double dineroDisponible		= Dinero.getCantidad();
		double precioProducto		= producto.getPrecioEnMercado();
		/*
		 * Tomando como referencia el precio en mercado, toma el dinero total del agente, dividido el precio del producto en el mercado
		 * y pondera un cantidad de producto a comprar.
		 */
		double ponderacion		= dineroDisponible>0 && precioProducto>0?(dineroDisponible/precioProducto):0;
		double cantidad			= Math.floor(ponderacion);
		Demanda novaDemanda 	= new Demanda(producto.getNombre(),cantidad,VariablesGlobales.TICKS_VIGENCIA_DEMANDA,false);
		
		novaDemanda.OBSERVABLE.addObserver(SaabContextBuilder.OBSERVADOR);
		novaDemanda.setPuntoDemanda(puntoDemanda);
		novaDemanda.setComprador(this);
		if(ponderacion>0)
			this.Demandas.add(novaDemanda);
		/*
		 * Si el vendedor no tiene dinero disponible la demanda devuelta es por cero productos
		 */
		return novaDemanda;
	}

	@Override
	public void realizarCompra() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean intencionDeConsolidacion() {		
		return intencionDeConsolidacion;
	}


	@Override
	public void setIntencionConsolidacion(boolean bool) {		
		intencionDeConsolidacion = bool;		
	}
	
	/**
	 * Envia un mensaje a otro agente inteligente
	 * 
	 * @param mensaje
	 * @param receptor
	 */
	@Override
	public synchronized void enviarMensaje(MensajeACL mensaje, AgenteInteligente receptor){
		
		//Evita Nullpointer Exception
		if(MensajesEnviados == null){
			MensajesEnviados 	= new ArrayList<MensajeACL>();
			MapRespuestas		= new ConcurrentHashMap<Integer, Queue<MensajeACL>>();
		}
		
		//Agrega el mensaje al mapa de respuestas
		Queue<MensajeACL> colaRespuestas = new ConcurrentLinkedDeque<MensajeACL>();
		if(MapRespuestas.replace(mensaje.getConversationID(), colaRespuestas)==null){
			MapRespuestas.put(mensaje.getConversationID(), colaRespuestas);
		}
							
		//Agrega el mensaje al mapa de Enviados
		 MensajesEnviados.add(mensaje);
		
		 //Envia el mensaje
		receptor.recibirMensaje(mensaje);
	}
	
	@Override
	public synchronized void recibirMensaje(MensajeACL mssg) {
		
		//Evita NullPointerException
		if(MensajesRecibidos == null)
			MensajesRecibidos = new ConcurrentLinkedDeque<MensajeACL>();
		
		//Si es una respuesta a un mensaje enviado
		if(mssg.getInReply_to()!=null){
			
			int IdConversacion 		= mssg.getInReply_to();
			MensajeACL mssgEnviado 	= null;
			//Busca el mensaje original
			for(MensajeACL m: MensajesEnviados){				
				
				if(m.getConversationID() == IdConversacion){
					mssgEnviado = m;
					break;
				}				
			}//EndFor
			
			if(mssgEnviado!=null){
				
				Queue<MensajeACL> colaRespuestas = MapRespuestas.get(mssg.getConversationID());
				colaRespuestas.offer(mssg);
			}else{
				//El mensaje no fue enviado por el agente
				this.MensajesRecibidos.offer(mssg);
			}			
		}else{
			this.MensajesRecibidos.offer(mssg);
		}
	}
	
	/**
	 * Busca respuesta de un mensaje especifico
	 * 
	 * @param MensajeID Id del mensaje que espera sea contestado
	 */
	@Override
	public void buscarRespuesta(Integer conversacionID){
		
		try{						
			Queue<MensajeACL> colaRespuestas = this.MapRespuestas.get(conversacionID);
			
			if(!colaRespuestas.isEmpty()){
				
				atenderMensaje(colaRespuestas.poll());
			}
			
		}catch(IndexOutOfBoundsException exc1){
			System.err.println("IndexOutOfBoundsException: " + exc1.getMessage());
		}catch(NullPointerException exc2){
			System.err.println("NullPointerException: " + exc2.getMessage());
		}		
	}
	
	/**
	 * interpreta los mensajes recibidos aún pendientes
	 */
	public void atenderMensajes() throws NullPointerException{
		
		MensajeACL mensaje = this.MensajesRecibidos.poll();	
		if(mensaje!=null)
			atenderMensaje(mensaje);
		else
			System.out.println(this.toString()+" no hay mensajes");
	}
	
	/**
	 * Interpreta el mensaje pasado como parámetro
	 * 
	 * @param mensaje mensaje a ser interpretado
	 */
	public void atenderMensaje(MensajeACL mensaje){
		
		if(mensaje.getPerformative().equalsIgnoreCase("propose")){//solo acepta proposals
	
			Preposicion contenido = mensaje.getContent();
			/**
			 * Acepta el mensaje si es una proposicion para consolidacion 
			 */
			if(contenido instanceof EjecutarAccionConProposito){
		
				EjecutarAccionConProposito proposal = (EjecutarAccionConProposito) contenido;
				SistemaActividadHumana actividad	= proposal.getActividad().getInstance();
				
				if(actividad instanceof ConformarNutrired && this.intencionDeConsolidacion()){
					
					mensaje.addReceiver(this);
					MensajeACL respuesta 		= mensaje.getReply_with();
					AgenteInteligente sender 	= mensaje.getReply_to();
					sender.recibirMensaje(respuesta);
				
					this.ActividadVigente = actividad.getInstance();
					this.ProcesoHumanoDefinido.setPaso(3);
					
				}else{
					rejectMessage(mensaje);
				}
			}
			
		}else if(mensaje.getPerformative().equalsIgnoreCase("reject-proposal")){
			//No hace nada
		}else if(mensaje.getPerformative().equalsIgnoreCase("accept-proposal")){
			
			Integer conversacionId = mensaje.getInReply_to();
			MensajeACL mssgEnviado = null;
			//Busca el mensaje original
			for(MensajeACL m: MensajesEnviados){
				if(m.getConversationID()==conversacionId){
					mssgEnviado = m;
					break;
				}
			}
			
			if(mssgEnviado!=null){ 
				Preposicion contenido = mssgEnviado.getContent();
				/**
				 * Evalua si el mensaje si es una aceptacion de consolidacion
				 * TODO evaluar otras accept-proposal 
				 */
				if(contenido instanceof EjecutarAccionConProposito){
						
					EjecutarAccionConProposito proposal = (EjecutarAccionConProposito) contenido;
					SistemaActividadHumana actividad	= proposal.getActividad().getInstance();
					
					if(actividad instanceof ConformarNutrired){
						/**
						 * Se incluye en la nutrired.
						 * 
						 * Es valido mientras el sistema de actividad 'conformarNutrired' no posea pasos secuenciales de lo contrario:
						 * TODO se debe generar la inclusion a la nutrired con un iterador
						 *  
						 */						
						actividad.secuenciaPrincipalDeAcciones(this);
					}					
				}//End AccionProposito
			}//Ed IF ENviado			
			
		}else{			
			rejectMessage(mensaje);			
		}
		//TODO agregar procesamiento de otras preformativas cfp-inform.. etc
	}
	
	/**
	 * Devuelve un mensaje ACL con la respuesta de rechazo al mensaje pasado como parametro
	 * @param mensaje Mensaje ACL mensaje a se contestado
	 * 
	 * @return Mensaje ACL mensaje de respuesta en rechazo
	 */
	private MensajeACL rejectMessage(MensajeACL mensaje){
		
		MensajeACL respuesta = new MensajeACL(mensaje.getConversationID());
		
		respuesta.setContent(mensaje.getContent());
		respuesta.setInReply_to(mensaje.getConversationID());
		respuesta.setSender(this);
		
		String performative = mensaje.getPerformative().equalsIgnoreCase("propose")?"reject-proposal":"refuse";
		respuesta.setPerformative(performative);
		
		AgenteInteligente sender = mensaje.getReply_to();
		sender.recibirMensaje(respuesta);
		
		this.MensajeAceptado = false;
		
		return respuesta;
	}
		
	@Override
	public synchronized void recibirPedido(List<OrdenDeCompra> compras){
		
		for(OrdenDeCompra or: compras){
			pedidosRecibidos.offer(or);
		}		
	}
	
	@Override
	public boolean pedidosRecibidos(){
		return !this.pedidosRecibidos.isEmpty();
	}
	
	@Override
	public void gestionarPedidos() {
		
		while(!(pedidosRecibidos.isEmpty())){
			
			OrdenDeCompra compra 		= pedidosRecibidos.poll();
			Recurso RecursosRecibidos 	= compra.getDetalleCompra();
			Dinero precioPagado			= compra.getPagoAcordado();
			/*
			 * Distribuye los productos recibidos en las tiendas
			 * y fija el precio de venta agregando al valor pagado el 10%
			 */			
			distribuirProductosEnTienda(RecursosRecibidos, precioPagado.getCantidad());
			
			/**ERROR**/
			if(precioPagado.getCantidad()/RecursosRecibidos.getCantidad()<500){
				LOGGER.info(this.toString()+compra.getOferta().toString()+"rec: "+RecursosRecibidos.getCantidad()+" "+RecursosRecibidos.toString()+" prec: "+precioPagado.getCantidad());
			}
			
		}		
	}
	
	
	@Override
	public void distribuirProductosEnTienda(Recurso productos, double precioCompra){
		
		if(precioCompra!=0){//SI 0 es porq no encontro producto
			/*
			 * Toma la primera tienda
			 * TODO Distribuir en diferentes tiendas
			 */
		//LOGGER.info("distribuye en tienda "+this.ActividadVigente.toString());
			Tienda tienda 				= this.Tiendas.get(0);		
			double cantidad				= productos.getCantidad();
			double precioUnitarioCompra = cantidad>0?(precioCompra/cantidad):0;
		
			/*
			 * calcula la utilidad obtenida por unidad de medida del producto
			 */
			double utilidad				= productos.getProducto().getPrecioEnMercado()-precioUnitarioCompra;
			double utilidadObtenida		= utilidad>0?utilidad:0;
			setUltimaUtilidadObtenida(utilidadObtenida);
		
			//calcula el precio C/1-Mg 
			double precioUnitarioVenta = precioUnitarioCompra/(1-VariablesGlobales.MARGEN_GANANCIA_REQUERIDO_10);
			
			Dinero precioVenta = new Dinero(precioUnitarioVenta);		
			tienda.AlmacenarProductos(productos, precioVenta);
			//notifica observadores
			this.OBSERVABLE.precioFijado(tienda.toString(), precioUnitarioVenta);
			
			//evalua la experiencia si hace parte de una nutrired
			if(this.ActividadVigente instanceof ConformarNutrired){
				this.juzgarMundoSegunEstandares();
			}
			
		}
		
	}
	
	/**
	 * Indica si el agente posee el producto para la venta
	 * @param nombre producto a ser consultado
	 * @return boolean <code>true</code> si el producto esta disponible <code>false</code> en caso contrario
	 * 
	 * TODO verificar en varias tiendas
	 */
	public boolean productoDisponible(String nombre){
		/*
		 * verifica solo en la primera tienda
		 */
		return this.Tiendas.get(0).consultarProducto(nombre);
	}
	
	/**
	 * Indica si el agente posee algun producto para la venta
	 * @return booelan <code>true</code> si existen productos sin vender <code>false</code> en caso contrario
	 * 
	 * TODO verificar varios producto varias tiendas
	 */
	@Override
	public boolean productosPendientesVenta(){
		Producto prod = new Producto("cebolla");
		return this.Tiendas.get(0).consultarProducto(prod.getNombre());		
	}
		
	@Override
	public void setActividadVigente(SistemaActividadHumana nuevaactividad) {
		this.ActividadVigente = nuevaactividad;		
	}
	
	@Override
	public String getEstado() {
		return this.Estado;
	}	
	
	@Override
	public List<Experiencia> getExperiencia(){
		if(this.Experiencia==null)
			this.Experiencia = new ArrayList();
		
		return this.Experiencia;
	}
	
	@Override
	public void setUltimaUtilidadObtenida(Double ultimaUtilidadObtenida){
		this.UltimaUtilidadObtenida = ultimaUtilidadObtenida;
	}
	
	@Override
	public Double getMayorUtilidadObtenida() {
		return MayorUtilidadObtenida;
	}

	@Override
	public void setMayorUtilidadObtenida(Double valor) {
		this.MayorUtilidadObtenida = valor;
	}

	@Override
	public List<SistemaActividadHumana> getActividadesEjecutables() {
		return ActividadesEjecutables;
	}

	@Override
	public SistemaActividadHumana getActividadVigente() {
		return ActividadVigente;
	}

	@Override
	public Double getUltimaUtilidadObtenida() {
		return UltimaUtilidadObtenida;
	}

	@Override
	public void addExperiencia(simulaSAAB.comunicacion.Experiencia exp) {
		if(exp!=null)
			this.Experiencia.add(exp);
	}

	@Override
	public void setEstado(String Estado) {
		this.Estado = Estado;
	}
	
	@Override
	public Dinero getDinero() {
		return Dinero;
	}

	/**
	 * asigna dinero al agente
	 * @param dinero dinero a ser asignado al agente
	 */
	public void setDinero(simulaSAAB.comunicacion.Dinero dinero) {
		Dinero = dinero;
	}
	
	/**
	 * Agrega una cantidad al dinero del agente
	 * 
	 * @param dinero double, cantidad a ser agregada al dinero del agente
	 */
	public void addDinero(Double dinero) {
		Dinero.addCantidad(dinero);
	}

	/**
	 * Devuelve un arreglo con las tiendas poseidas por el agente
	 * @return List<Tienda>
	 */
	public List<Tienda> getTiendas() {
		return Tiendas;
	}

	/**
	 * Agrega una tienda al conjunto de tiendas poseidas por el agente
	 * @param tienda
	 */
	public void addTienda(Tienda tienda) {
		
		if(Tiendas == null)
			Tiendas = new ArrayList();
		
		Tiendas.add(tienda);
	}

	@Override
	public PlazaDistrital getPuntoDemanda() {
		return puntoDemanda;
	}

	@Override
	public void setPuntoDemanda(PlazaDistrital puntoDemanda) {
		this.puntoDemanda = puntoDemanda;
	}
	
	@Override
	public String printActividadVigente(){
		return this.ActividadVigente.toString();
	}
	
	/**
	 * Clase anidada a la que se le delega la funcionalidad <code>obserbable</code> del agente <code>VendedorFinal</code>
	 * 
	 * @author jdvelezg
	 */
	public class PrecioFinalTrack extends AgentTrackObservable{
		
		private final String vendedorId;		
		private Double tick;			
		private String tienda;		
		private String actividadEjecutada;		
		private Double precioUnitarioFijado;
		
		public PrecioFinalTrack(){
			super();
			this.vendedorId = VendedorFinal.this.toString();
		}
		
		/**
		 * Obtiene los valores observados del agente {@link VendedorFinal}
		 * 
		 * @param tienda tienda en la que se fija el precio
		 * @param precio precio fijado
		 */
		public void precioFijado(String tienda, double precio){
			
			this.tick					= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();			
			this.tienda					= tienda;		
			this.actividadEjecutada 	= VendedorFinal.this.ActividadVigente.toString();		
			this.precioUnitarioFijado	= new Double(precio);
			
			super.setChanged();
			super.notifyObservers(this);
		}

		@Override
		public String dataLineString(String separador) {
			
			return tick.toString()+separador+vendedorId+separador+actividadEjecutada+separador+tienda+separador+precioUnitarioFijado.toString()+separador;
		}

		@Override
		public String dataLineStringHeader(String separador) {
			
			return "tick"+separador+"vendedorId"+separador+"actividad_ejecutada"+separador+"tienda_id"+separador+"precio_unitario_fijado"+separador;
		}
		
	}
	
	public int getId(){
		return this.id;
	}

}
