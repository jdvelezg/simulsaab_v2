/**
 * 
 */
package simulaSAAB.agentes;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vividsolutions.jts.geom.Coordinate;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import simulaSAAB.comunicacion.EjecutarAccionConProposito;
import simulaSAAB.comunicacion.MensajeACL;
import simulaSAAB.comunicacion.Oferta;
import simulaSAAB.comunicacion.Preposicion;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.comunicacion.Experiencia;
import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.comunicacion.Recurso;
import simulaSAAB.contextos.CentroUrbano;
import simulaSAAB.contextos.SaabContextBuilder;
import simulaSAAB.global.PropositosFactory;
import simulaSAAB.global.VariablesGlobales;
import simulaSAAB.global.persistencia.AgentTrackObservable;
import simulaSAAB.inteligencia.Cerebro;
import simulaSAAB.tareas.ConformarNutrired;
import simulaSAAB.tareas.ProcesoAgenteHumano;
import simulaSAAB.tareas.ProducirCebollaBulbo;
import simulaSAAB.tareas.SistemaActividadHumana;
import simulaSAAB.tareas.TransaccionComercial;

/**
 * Representa al agente Productor
 * <p>
 * Agente Inteligente cuyas acciones estan encaminadas a la producción agrícola
 * 
 * @author jdvelezg
 *
 */
public class Productor implements Oferente {
	
	/**
	 * Identificador único del agente
	 */
	private final int id;
	/**
	* Registro de la clase usado para depuración <code>Debugging</code>
	*/
	private static Logger LOGGER = Logger.getLogger(Productor.class.getName());
	/**
	 * rol del agente
	 */
	public static String ROL ="productor";
	/**
	 * intención del agente
	 */
	public static String INTENCION ="producir";
	/**
	 * objetivo del agente
	 */
	public static String OBJETIVO = "Garantizar su superviviencia obteniendo recursos para suplir sus necesidades de vida mediante la produccion y comercializacion de productos agrıcolas";
	
	public final ProductorTrack OBSERVABLE = new ProductorTrack(); 
	/**
	 * propósito vigente del agente
	 */
	private Proposito PropositoVigente;
	/**
	 * dinero del agente
	 */
	private Dinero Dinero;
	
	private Cerebro CerebroProductor;
	/**
	 * instancia del <code>MPA<code> {@link ProcesoAgenteHumano}
	 */
	private SistemaActividadHumana ProcesoHumanoDefinido;
	/**
	 * Actividad vigente del agente
	 */
	private SistemaActividadHumana ActividadVigente;
	/**
	 * Arreglo con la experiencia adquirida por el agente
	 */
	private List<Experiencia> Experiencia;
	/**
	 * arreglo con el conjunto de terrenos cultivables por el agente
	 */
	private List<Terreno> TerrenosCultivables;
	/**
	 * punto de oferta del agente
	 */
	private CentroUrbano puntoOferta;
	/**
	 * arreglo de ofertas generadas por el agente
	 */
	private List<Oferta> Ofertas;
	
	private Queue<Recurso> Productos;
	/**
	 * Productos percibidos como viables de producir por el agente
	 */
	private List<Producto> ProductosViablesPercibidos;
	/**
	 * actividades ejecutables por el agente
	 */
	private List<SistemaActividadHumana> ActividadesEjecutables;
	/**
	 * mayoe utilidad obtenida por el agente
	 */
	private Double MayorUtilidadObtenida;
	/**
	 * Ultima utilidad obtenida por el agente
	 */
	private Double UltimaUtilidadObtenida;
	/**
	 * Cola de mensajes recibidos por el agente
	 */
	private Queue<MensajeACL> MensajesRecibidos;
	
	private ConcurrentHashMap<Integer, Queue<MensajeACL>> MapRespuestas;
	/**
	 * Arreglo de mensajes enviados por el agente
	 */
	private List<MensajeACL> MensajesEnviados;
	/**
	 * Estado del agente
	 */
	private String Estado;

	/**
	 * Constructor de la clase
	 */
	public Productor(int id) {
		
		this.id = id;
								
		Experiencia 		= new ArrayList<Experiencia>();		
		TerrenosCultivables = new ArrayList<Terreno>();		
		Ofertas 			= new ArrayList<Oferta>();		
		Productos 			= new ConcurrentLinkedDeque<Recurso>();
		
		ProductosViablesPercibidos 	= new ArrayList<Producto>();		
		ActividadesEjecutables 		= new ArrayList<SistemaActividadHumana>();		
		
		CerebroProductor		= new Cerebro(this);
		CerebroProductor.OBSERVABLE.addObserver(SaabContextBuilder.OBSERVADOR);
		ProcesoHumanoDefinido 	= new ProcesoAgenteHumano();
		
		Dinero					= new Dinero(new Double(0),"COP");
		UltimaUtilidadObtenida 	= new Double(0);
		MayorUtilidadObtenida	= new Double(0);	
		
		Estado 	= "IDLE";
		
		/*
		 * Agrega el productor al schedule con inicio aleatorios de forma que la produccion 
		 * no sea uniforme en el tiempo
		 */
		
		/*int start = RandomHelper.nextIntFromTo(1, 100);
		ScheduleParameters params = ScheduleParameters.createRepeating(start,1);
		ISchedule sch = RunEnvironment.getInstance().getCurrentSchedule();
		sch.schedule(params, this, "step");*/
	}
	
	
	/**
	 * Metodo que ejecuta el comportamiento del agente en cada ciclo de reloj <code>Tick</code>
	 * <p>
	 * <code>start = 1, interval = 1</code>
	 */
	@ScheduledMethod (start = 1, interval = 1)
	public synchronized void step () {
		ProcesoHumanoDefinido.secuenciaPrincipalDeAcciones(this);		
	}
	
	
	/* (non-Javadoc)
	 * @see simulaSAAB.agentes.AgenteInteligente#atribuirSignificadoAlMundoPercibido()
	 */
	@Override
	public void percibirMundoSelectivamente() {
		
		//Resetea el listado de actividades viables percibidas
		ActividadesEjecutables.clear();
		//Consulta tareas ejecutables en el ambiente		
		for(Terreno finca : TerrenosCultivables){
			
			List<SistemaActividadHumana> actividadesAmbientales = finca.getAmbiente().getActividadesViables();			
			/*
			 * En caso que no tenga aun un proposito vigente, lo asigna.
			 */
			if(this.PropositoVigente==null)
				formarIntenciones();
			
			//Filtra según su proposito
			for(SistemaActividadHumana a: actividadesAmbientales){
				
				if(a.getProposito().compare(PropositoVigente))
					this.ActividadesEjecutables.add(a);				
			}
		}
	}

	/* (non-Javadoc)
	 * @see simulaSAAB.agentes.AgenteInteligente#formarIntenciones()
	 */
	@Override
	public void formarIntenciones() {
		//LOGGER.log(Level.INFO, this.toString() + ": formar intenciones");
		
		if(PropositoVigente==null)
			PropositoVigente = new PropositosFactory(Productor.ROL,Productor.INTENCION).getProposito();
	}
	
	@Override
	public void tomarDecisiones() {
		//LOGGER.log(Level.INFO, ": Tomar decisiones");
		
		this.ActividadVigente = CerebroProductor.tomarDecision(ActividadesEjecutables).getInstance();
		this.UltimaUtilidadObtenida = new Double(0);
	}
	

	/* (non-Javadoc)
	 * @see simulaSAAB.agentes.AgenteInteligente#actuar()
	 */
	@Override
	public void actuar() {
		//LOGGER.log(Level.INFO, ": actuando "+ActividadVigente.toString()+" Estado productor: "+this.Estado);
		
		ActividadVigente.secuenciaPrincipalDeAcciones(this);
	}

	/* (non-Javadoc)
	 * @see simulaSAAB.agentes.AgenteInteligente#juzgarMundoSegunEstandares()
	 */
	@Override
	public void juzgarMundoSegunEstandares() {
		//LOGGER.log(Level.INFO, ": Juzgando");
		
		Experiencia exp = CerebroProductor.evaluarExperiencia();		
		if(!Experiencia.contains(exp)){			
			this.addExperiencia(exp);
		}
	}
	
	@Override
	public Oferta generarOferta() {		
		
		/**
		 * Obtiene el primer producto (Recurso) de la lista y genera una oferta a partir del mismo.
		 * 
		 * Fija el precio unitario sumando 10% al costo
		 * 
		 * TODO Implementar una forma que el agente decida su ganancia al evaluar variables del mercado
		 */
		if(!Productos.isEmpty()){
			
			Recurso producto = Productos.poll();
			/*
			 * Calcula el precio con un margen de ganancia esperado del 10% C/1-Mg
			 */
			double costoUnitario	= producto.getCostoUnitario();
			double cantidad			= producto.getCantidad();
			double precio 			= costoUnitario/(1-VariablesGlobales.MARGEN_GANANCIA_REQUERIDO_10);
			double precioTotal		= precio*cantidad;
			
			Oferta novaOferta = new Oferta(producto,VariablesGlobales.TICKS_VIGENCIA_OFERTA,true,precioTotal);
			//novaOferta.OBSERVABLE.addObserver(SaabContextBuilder.OBSERVADOR);
			novaOferta.setPuntoOferta(puntoOferta);
			novaOferta.setUbicacion(ubicacionOfertas());
			novaOferta.setVendedor(this);
			
			this.Ofertas.add(novaOferta);			
			
			//LOGGER.info(this.toString()+"oferta "+novaOferta.toString()+" params: costo: "+costoUnitario+" cant: "+cantidad+"total:"+precioTotal);	
			
			return novaOferta;
			
		}else if(this.Ofertas.size()!=0){			
			
			Oferta ofertavigente = null;				
			for(Oferta o: Ofertas){				
				if(!o.vendida() && (o.getEstado().equalsIgnoreCase("CREADA") && o.getEstado().equalsIgnoreCase("VENCIDA"))){
					ofertavigente = o;
					break;
				}											
			}//endFor
			LOGGER.log(Level.SEVERE,this.toString()+" NO POSEE OFERTAS PARA registrar");
			return ofertavigente;
			
		}else{
			
			LOGGER.log(Level.SEVERE,this.toString()+" NO POSEE PRODUCTOS PARA OFERTAR");
			return null;
		}
		
	}
	
	@Override
	public synchronized void recibirMensaje(MensajeACL mssg) {
		
	//System.out.println("productor recibe mensaje "+mssg.toString());
		
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
				this.MensajesRecibidos.offer(mssg);
			}			
		}else{
			this.MensajesRecibidos.offer(mssg);
		}
		
	}
	
	@Override
	public void atenderMensajes() {
		
		MensajeACL mensaje = this.MensajesRecibidos.poll();	
		if(mensaje!=null)
			atenderMensaje(mensaje);
		
			//System.out.println(this.toString()+" no hay mensajes");
		
	}


	@Override
	public synchronized void enviarMensaje(MensajeACL mensaje, AgenteInteligente receptor) {

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
	public void buscarRespuesta(Integer mensajeID) {
		
		try{						
			Queue<MensajeACL> colaRespuestas = this.MapRespuestas.get(mensajeID);
			
			if(!colaRespuestas.isEmpty()){
				
				atenderMensaje(colaRespuestas.poll());
			}
			
		}catch(IndexOutOfBoundsException exc1){
			System.err.println("IndexOutOfBoundsException: " + exc1.getMessage());
		}catch(NullPointerException exc2){
			System.err.println("NullPointerException err: " + exc2.getMessage());
		}		
	}
	
	/**
	 * Interpreta el mensaje pasado como parámetro
	 * 
	 * @param mensaje MensajeACL a ser interpretado
	 */
	public void atenderMensaje(MensajeACL mensaje){
	
		if(mensaje.getPerformative().equalsIgnoreCase("propose")){//solo acepta proposals
	
			Preposicion contenido = mensaje.getContent();
			/**
			 * Acepta el mensaje si es una proposicion de transaccion comercial 
			 */
			if(contenido instanceof EjecutarAccionConProposito){
		
				EjecutarAccionConProposito proposal = (EjecutarAccionConProposito) contenido;
				SistemaActividadHumana actividad	= proposal.getActividad();
			
				if(actividad instanceof TransaccionComercial){
								
					mensaje.addReceiver(this);
					MensajeACL nuevaRespuesta 	= mensaje.getReply_with();
					AgenteInteligente sender 	= mensaje.getReply_to();
					
			
					TransaccionComercial actividadVenta = (TransaccionComercial)actividad;
					/*
					 * ejecuta la actividad de transaccion comercial
					 * 
					 * TODO si la actividad tiene más de un paso, se debería iterar
					 * hasta terminarla
					 */				
					actividadVenta.getInstance().secuenciaPrincipalDeAcciones(this);
					enviarMensaje(nuevaRespuesta,sender);				
				}else{
					rejectMessage(mensaje);
				}				
			}			
		}//END IF propose
	}
	
	/**
	 * Devuelve un mensaje ACL con la respuesta de rechazo al mensaje pasado como parametro
	 * @param mensaje mensajeACL a ser contestado
	 * @return	Mensaje ACL
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
		
		return respuesta;
	}
	
	/**getter - setters **/
	
	@Override
	public String LugarOferta() {		
		return this.puntoOferta.getNombre();
	}
	
	@Override
	public String getEstado() {
		return this.Estado;
	}
	
	@Override
	public void setEstado(String Estado){
		this.Estado = Estado;
		if(Estado.compareToIgnoreCase("IDLE")==0)
			setPropositoVigente(null);
	}
	
	/**
	 * Asigna terrenos al productor
	 * @param t terreno a ser agregado al <code>Productor</code>
	 */
	public void addTerrenos(Terreno t){
		if(this.TerrenosCultivables==null)
			this.TerrenosCultivables=new ArrayList();
		
		this.TerrenosCultivables.add(t);
	}
	
	@Override
	public List<Experiencia> getExperiencia(){
		if(this.Experiencia==null)
			this.Experiencia = new ArrayList();
		
		return this.Experiencia;
	}

	@Override
	public Double getMayorUtilidadObtenida() {
		return MayorUtilidadObtenida;
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
	public void addExperiencia(Experiencia exp) {
		if(exp!=null)
			this.Experiencia.add(exp);		
	}
	
	@Override
	public void setMayorUtilidadObtenida(Double mayorUtilidadObtenida) {
		MayorUtilidadObtenida = mayorUtilidadObtenida;
	}


	public void setUltimaUtilidadObtenida(Double ultimaUtilidadObtenida) {
		this.UltimaUtilidadObtenida = ultimaUtilidadObtenida;
	}

	@Override
	public void setActividadVigente(SistemaActividadHumana actividadVigente) {
		ActividadVigente = actividadVigente;
	}

	@Override
	public List<SistemaActividadHumana> getActividadesEjecutables() {
		return ActividadesEjecutables;
	}

	/**
	 * Asigna un arreglo de actividades ejecutables por el agente
	 * @param actividadesEjecutables arreglo de actividades (<code>SistemaActividadHumana</code>) ejecutables
	 */
	private void setActividadesEjecutables(
			List<SistemaActividadHumana> actividadesEjecutables) {
		ActividadesEjecutables = actividadesEjecutables;
	}

	@Override
	public Dinero getDinero() {
		return Dinero;
	}

	/**
	 * Asigna dienro al agente
	 * @param dinero dinero a ser asignado
	 */
	public void setDinero(Dinero dinero) {
		Dinero = dinero;
	}

	/**
	 * Devuelve el arreglo de terrenos que posee el agente
	 * @return List<Terreno>
	 */
	public List<Terreno> getTerrenosCultivables() {
		return TerrenosCultivables;
	}

	/**
	 * Asigna un arreglo de terrenos cultivables al agente
	 * @param terrenosCultivables <code>List<Terreno></code> arreglo de terrenos a ser asignados
	 */
	public void setTerrenosCultivables(List<Terreno> terrenosCultivables) {
		TerrenosCultivables = terrenosCultivables;
	}
	
	/**
	 * Agrega un producto al agente
	 * @param productos recurso a ser agregado al agente
	 */
	public void addProducto(Recurso productos){						
			
		this.Productos.offer(productos);
		//Notifica cosecha o adquisición de productos
		this.OBSERVABLE.nuevaCosecha(productos.getCantidad(), productos.getCostoUnitario());			
	}
	
	/**
	 * Devuelve el arreglo de productos poseido por el agente
	 * @return List<Recurso>
	 */
	public Queue<Recurso> getProductos(){		
		return Productos;
	}

	/**
	 * Devuelve el propósito vigente del agente
	 * @return Proposito
	 */
	public Proposito getPropositoVigente() {
		return PropositoVigente;
	}

	/**
	 * Asigna el propósito vigente del agente
	 * @param propositoVigente proposito a se asignado al agente
	 */
	public void setPropositoVigente(Proposito propositoVigente) {
		PropositoVigente = propositoVigente;
	}

	/**
	 * Devuelve el punto de oferta del agente
	 * @return CentroUrbano
	 */
	public CentroUrbano getPuntoOferta() {
		return puntoOferta;
	}

	/**
	 * Asigna el punto de oferta del agente
	 * @param puntoOferta CentroUrbano que representa el punto de oferta del agente
	 */
	public void setPuntoOferta(CentroUrbano puntoOferta) {
		this.puntoOferta = puntoOferta;
	}
	
	/**
	 * Devuelve la ubicación de las ofertas generadas por el agente
	 * @return Coordinate 
	 */
	public Coordinate ubicacionOfertas(){
		
		return this.TerrenosCultivables.get(0).getGeometria().getCoordinate();
	}
	
	public int getId(){
		return this.id;
	}
	
	@Override
	public String printActividadVigente(){
		return this.ActividadVigente.toString();
	}
	

	/** 
	 * Clase anidada a la que se le delega la funcionalidad <code>observable</code> del agente <code>Productor</code>
	 * asociados al productor
	 * 
	 * @author jdvelezg
	 *
	 */
	public class ProductorTrack extends AgentTrackObservable{
		
		private final String productorId;
		
		private Double tick;	
		
		private Double hectareas;
		
		private String centroUrbano;
		
		private String coordenadas;
		
		private Double cosecha;
		
		private Double precioUnitario;
		
		
		public ProductorTrack(){
			
			super();
			this.productorId 	= Productor.this.toString();
		}
					
		
		/**
		 * Actualiza los valores de recoleccion
		 * 
		 * @param cantidad double, cantidad de productos cosechados por el agente observado
		 * @param precioUnitario double, precio asignado al producto cosechado por el agente observado
		 */
		public void nuevaCosecha(double cantidad, double precioUnitario){
			
			this.tick 			= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
			this.hectareas		= Productor.this.getTerrenosCultivables().get(0).getHectareas();
			this.centroUrbano	= Productor.this.getPuntoOferta().getNombre();
			this.coordenadas	= SaabContextBuilder.SAABGeography.getGeometry(Productor.this).getCoordinate().toString();
			this.cosecha		= cantidad;
			this.precioUnitario	= precioUnitario;
			
			super.setChanged();
			super.notifyObservers(this);
		}
		
		/*
		 * (non-Javadoc)
		 * @see simulaSAAB.global.persistencia.AgentTrackObservable#dataLineString(String separador)
		 * tick + ID del productor + hectareas del terreno + nombre centro urbano + coordenadas del terreno + cantidad cosechada + precio unitario
		 */
		@Override
		public String dataLineString(String separador){
									
			return tick.toString()+separador+productorId+separador+hectareas.toString()+separador+
					centroUrbano+separador+coordenadas+separador+cosecha.toString()+separador+precioUnitario.toString()+separador;
		}
		
		@Override
		public String dataLineStringHeader(String separador){
			
			return "tick"+separador+"ID_productor"+separador+"hectareas"+separador+
					"centro_urbano"+separador+"coordenadas"+separador+"cantidad"+separador+"precio_unitario"+separador;
		}
		
				
		public Double getTick() {
			return tick;
		}
	
		public void setTick(Double tick) {
			this.tick = tick;			
		}
	
		public String getProductorId() {
			return productorId;
		}
		
		public Double getHectareas() {
			return hectareas;
		}
	
		public void setHectareas(Double hectareas) {
			this.hectareas = hectareas;
		}
	
		public String getCentroUrbano() {
			return centroUrbano;
		}
	
		public void setCentroUrbano(String centroUrbano) {
			this.centroUrbano = centroUrbano;
		}
	
		public String getCoordenadas() {
			return coordenadas;
		}
	
		public void setCoordenadas(String coordenadas) {
			this.coordenadas = coordenadas;
		}
	
		public Double getCosecha() {
			return cosecha;
		}
	
		public void setCosecha(Double cosecha) {
			this.cosecha = cosecha;
			super.setChanged();
		}
	
		public Double getPrecioUnitario() {
			return precioUnitario;
		}
	
		public void setPrecioUnitario(Double precioUnitario) {
			this.precioUnitario = precioUnitario;
			super.setChanged();
		}
		
	}
	
}

