package simulaSAAB.agentes;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Logger;

import com.vividsolutions.jts.geom.Geometry;

import repast.simphony.engine.schedule.ScheduledMethod;
import simulaSAAB.agentes.Productor.ProductorTrack;
import simulaSAAB.comunicacion.Demanda;
import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.comunicacion.EjecutarAccionConProposito;
import simulaSAAB.comunicacion.Experiencia;
import simulaSAAB.comunicacion.MensajeACL;
import simulaSAAB.comunicacion.Oferta;
import simulaSAAB.comunicacion.OrdenDeCompra;
import simulaSAAB.comunicacion.Preposicion;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.comunicacion.Recurso;
import simulaSAAB.contextos.CentroUrbano;
import simulaSAAB.contextos.ObjetoMovil;
import simulaSAAB.contextos.PlazaDistrital;
import simulaSAAB.contextos.SaabContextBuilder;
import simulaSAAB.global.PropositosFactory;
import simulaSAAB.global.VariablesGlobales;
import simulaSAAB.inteligencia.Cerebro;
import simulaSAAB.tareas.ConformarNutrired;
import simulaSAAB.tareas.ProcesoAgenteHumano;
import simulaSAAB.tareas.SistemaActividadHumana;
import simulaSAAB.tareas.TransaccionComercial;

/**
 * Representa un agente intermediario
 * <p>
 * Agente Inteligente cuyas acciones están encaminadas a la compra y venta de productos al por mayor
 * 
 * @author jdvelezg
 *
 */
public class Intermediario implements Demandante, Oferente, ObjetoMovil {
	
	private final int id;
	/**
	* Registro de la clase usado para depuración <code>Debugging</code>
	*/
	private static Logger LOGGER = Logger.getLogger(Productor.class.getName());
	/**
	 * Rol del agente
	 */
	public static String ROL ="intermediario";
	/**
	 * Intención dle agente
	 */
	public static String INTENCION ="vender";
	/**
	 * Objetivo del agente
	 */
	public static String OBJETIVO = "Garantizar su superviviencia obteniendo recursos para suplir sus necesidades de vida mediante la comercializacion de productos agrıcolas";
	//public final ProductorTrack OBSERVABLE = new IntermediarioTrack();
	/**
	 * Propósito vigente del agente		
	 */
	private Proposito PropositoVigente;
	/**
	 * Dinero del agente
	 */
	private Dinero Dinero;
	/**
	 * {@link Cerebro}  del agente
	 */
	private Cerebro CerebroIntermediario;
	/**
	 * Instancia del {@link ProcesoAgenteHumano}
	 */
	private SistemaActividadHumana ProcesoHumanoDefinido;
	/**
	 * Actividad vigente del agente
	 */
	private SistemaActividadHumana ActividadVigente;
	/**
	 * Arreglo de experiencias del agente
	 */
	private List<Experiencia> Experiencia;
	/**
	 * Ambiente local del agente
	 */
	private CentroUrbano AmbienteLocal;
		
	private List<Recurso> Productos;
	
	private Camion transporte;
	
	private List<Producto> ProductosViablesPercibidos;
	
	private List<SistemaActividadHumana> ActividadesEjecutables;
	/**
	 * Mayor utilidad obtenida por el agente
	 */
	private Double MayorUtilidadObtenida;
	/**
	 * Ultima utilidad obtenida por el agente
	 */
	private Double UltimaUtilidadObtenida;
	
	private Queue<MensajeACL> MensajesRecibidos;
	
	private ConcurrentHashMap<Integer, Queue<MensajeACL>> MapRespuestas;
	
	private List<MensajeACL> MensajesEnviados;
	
	private Geometry geometria;
	/**
	 * Estado del agente
	 */
	private String Estado;

	
	/**
	 * Contrsuctor de la clase
	 */
	public Intermediario(int id) {
		
		this.id = id;
		
		Experiencia = new ArrayList<Experiencia>();		
		transporte 	= new Camion();
		transporte.setGeometria(SaabContextBuilder.SAABGeography.getGeometry(this));
		
		ProductosViablesPercibidos 	= new ArrayList<Producto>();		
		ActividadesEjecutables 		= new ArrayList<SistemaActividadHumana>();		
		
		CerebroIntermediario		= new Cerebro(this);
		CerebroIntermediario.OBSERVABLE.addObserver(SaabContextBuilder.OBSERVADOR);
		ProcesoHumanoDefinido 	= new ProcesoAgenteHumano();
		
		Dinero					= new Dinero(new Double(0),"COP");
		UltimaUtilidadObtenida 	= new Double(0);
		MayorUtilidadObtenida	= new Double(0);
		Productos				= new ArrayList<Recurso>();
						
		this.MensajesEnviados	= new ArrayList<MensajeACL>(100);
		this.MapRespuestas		= new ConcurrentHashMap<Integer, Queue<MensajeACL>>();
		this.MensajesRecibidos	= new ConcurrentLinkedDeque<MensajeACL>();
		
		Estado 	= "IDLE";
	}
	
	/**
	 * Constructor de la clase
	 * @param puntoDemanda CentroUrbano que representa el punto de demanda del agente
	 */
	public Intermediario(int id, CentroUrbano puntoDemanda) {
		
		this.id = id;
		
		AmbienteLocal				= puntoDemanda;
		Experiencia 				= new ArrayList<Experiencia>();		
		transporte 					= new Camion();
		transporte.setCapacidadAleatoria();
		
		
		ProductosViablesPercibidos 	= new ArrayList<Producto>();		
		ActividadesEjecutables 		= new ArrayList<SistemaActividadHumana>();		
		
		CerebroIntermediario		= new Cerebro(this);
		CerebroIntermediario.OBSERVABLE.addObserver(SaabContextBuilder.OBSERVADOR);
		ProcesoHumanoDefinido 		= new ProcesoAgenteHumano();
		
		Dinero						= new Dinero(new Double(0),"COP");
		UltimaUtilidadObtenida 		= new Double(0);
		MayorUtilidadObtenida		= new Double(0);
		Productos					= new ArrayList<Recurso>();
						
		this.MensajesEnviados		= new ArrayList<MensajeACL>(100);
		this.MapRespuestas			= new ConcurrentHashMap<Integer, Queue<MensajeACL>>();
		this.MensajesRecibidos		= new ConcurrentLinkedDeque<MensajeACL>();
		
		Estado 	= "IDLE";
	}
	
	/**
	 * Metodo que ejecuta el comportamiento del agente en cada ciclo de reloj enviado por repast
	 * <p>
	 * <code>start = 300, interval = 1</code>
	 */
	@ScheduledMethod (start = 300, interval = 1)
	public synchronized void step () {
		ProcesoHumanoDefinido.secuenciaPrincipalDeAcciones(this);		
	}
	

	@Override
	public void percibirMundoSelectivamente() {
		
		//Resetea el listado de actividades viables percibidas
		ActividadesEjecutables.clear();

		List<SistemaActividadHumana> actividadesAmbientales = AmbienteLocal.getActividadesViables();		
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

	@Override
	public void formarIntenciones() {
		
		if(PropositoVigente==null)
			PropositoVigente = new PropositosFactory(Intermediario.ROL,Intermediario.INTENCION).getProposito();
	}

	@Override
	public void tomarDecisiones() {
		
		this.ActividadVigente 		= CerebroIntermediario.tomarDecision(ActividadesEjecutables).getInstance();
		this.UltimaUtilidadObtenida = new Double(0);
	}

	@Override
	public void actuar() {
		ActividadVigente.secuenciaPrincipalDeAcciones(this);
	}

	@Override
	public void juzgarMundoSegunEstandares() {
		Experiencia exp = CerebroIntermediario.evaluarExperiencia();		
		if(!Experiencia.contains(exp)){			
			this.addExperiencia(exp);
		}
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
	public synchronized void recibirMensaje(MensajeACL mssg) {
	
		//System.out.println("recibe mensaje "+mssg.toString());
		
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
	public void buscarRespuesta(Integer mensajeID) {
		
		try{						
			Queue<MensajeACL> colaRespuestas = this.MapRespuestas.get(mensajeID);
			
			if(!colaRespuestas.isEmpty()){
				
				atenderMensaje(colaRespuestas.poll());
			}
			
		}catch(IndexOutOfBoundsException exc1){
			System.err.println("IndexOutOfBoundsException: " + exc1.getMessage());
		}catch(NullPointerException exc2){
			System.err.println("NullPointerException: " + exc2.getMessage());
		}
	}

	@Override
	public void atenderMensajes() {
		
		MensajeACL mensaje = this.MensajesRecibidos.poll();	
		if(mensaje!=null)
			atenderMensaje(mensaje);
		
			//System.out.println(this.toString()+" no hay mensajes");
	}
	
	
	/**
	 * Atiende e interpreta el mensaje pasado como parámetro
	 * 
	 * @param mensaje mensaje a interpretar
	 */
	public void atenderMensaje(MensajeACL mensaje){		
	
	//System.out.println("atiende mensajes "+mensaje.toString());
		
		if(mensaje.getPerformative().equalsIgnoreCase("cfp")){//solo procesa call for proposals
			
			Preposicion contenido = mensaje.getContent();
			/**
			 * Acepta el mensaje si es una proposicion de transacción comercial 
			 */
			if(contenido instanceof EjecutarAccionConProposito){
				
				EjecutarAccionConProposito proposal = (EjecutarAccionConProposito) contenido;
				SistemaActividadHumana actividad	= proposal.getActividad();
				
				if(actividad instanceof TransaccionComercial){
					
					mensaje.addReceiver(this);
					TransaccionComercial actividadVenta = (TransaccionComercial)actividad;
					MensajeACL respuesta = mensaje.getReply_with();	
					/*
					 * Modifica el precio ofertado y envia la propuesta
					 */
					double precioCompra = fijarPrecioCompra(actividadVenta.getProductoComercializado());
					if(actividadVenta.getMontoTransaccion() > precioCompra)
						actividadVenta.setMontoTransaccion(precioCompra);
					
					//proposal.setActividad(actividadVenta);
					respuesta.setSender(this);
					respuesta.setReply_to(this);
					/*
					 * Arma la respuesta al nuevo mensaje enviado
					 */		
					int conversacionID	= mensaje.getConversationID();
					MensajeACL reply 	= new MensajeACL(conversacionID,"accept-proposal");
					reply.setContent(proposal);
					reply.setInReply_to(conversacionID);	
					respuesta.setReply_with(reply);				
					//Envia el mensaje
					enviarMensaje(respuesta,mensaje.getSender());					
				}
			}
			
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see simulaSAAB.agentes.Demandante#recibirPedido(java.util.List)
	 * 
	 * Recibe un pedido y lo almacena en el camion
	 */
	@Override
	public void recibirPedido(List<OrdenDeCompra> compras) {
		
		for(OrdenDeCompra o: compras){
			
			Recurso producto = o.getDetalleCompra(); 
			Productos.add(producto);
			transporte.cargarMercancia(producto);			
		}
	}
	
	/**
	 * Libera la mercancia (<code>Recurso</code>) que posee el agente almacenado en un <code>camion</code>
	 * @return
	 */
	public Recurso descargarMercancia(){
		
		this.Productos.clear();
		return this.transporte.descargarMercancia();
	}
	
	/*
	 * (non-Javadoc)
	 * @see simulaSAAB.agentes.Demandante#gestionarPedidos()
	 * 
	 * No lo implementa, los pedidos son gestionados una tan pronto son recibidos
	 */
	@Override
	public void gestionarPedidos() {
		// TODO Auto-generated method stub		
	}
	
	/**
	 * Determina un precio para una cantidad determinada de un producto especifico,
	 * pasado como parámetro
	 * 
	 * @param productos Recurso al que se le fijar precio de compra 
	 * @return	double valor mìnimo deseado para el producto 
	 */
	public double fijarPrecioCompra(Recurso productos){
		/*
		 * extrae un 10% del precio en mercados del producto
		 */
		Producto prod 			= productos.getProducto();
		double precioSipsa		= prod.getPrecioEnMercado();
		double margenEsperado	= precioSipsa*VariablesGlobales.MARGEN_GANANCIA_REQUERIDO_20;
		
		return (precioSipsa-margenEsperado)*productos.getCantidad();
	}
	
	/**
	 * Fija el precio de venta de una determinada cantidad de un producto específico, pasado como parámetro
	 * 
	 * @param costoTransporte double, costo logistico al transportar los productos 
	 */
	public void fijarPrecioVenta(double costoTransporte){
		
		int cantidadRecursos 	= Productos.size();
		double costoAdjudicado	= costoTransporte/cantidadRecursos;
		for(Recurso producto: Productos){
			/*
			 * Calcula el precio final con un margen de ganancia esperado del 10%, tomando como costo fijo el precioUnitarioDeCompra + costoDeTransportePorUnidad
			 */
			double costoTransporteUnitario = costoAdjudicado/producto.getCantidad();
			double precio = (producto.getCostoUnitario() + costoTransporteUnitario)/(1-VariablesGlobales.MARGEN_GANANCIA_REQUERIDO_10);
			
			producto.setCostoUnitario(precio);
		}		
	}
	
	
	@Override
	public Geometry getGeometria() {
		return geometria;
	}
	
	@Override
	public void setGeometria(Geometry geometria) {
		this.geometria = geometria;
	}
	
	@Override
	public Object getObject(){
		return this;
	}

	@Override
	public String getEstado() {		
		return Estado;
	}

	@Override
	public void setEstado(String Estado) {
		this.Estado = Estado;
	}

	@Override
	public void setActividadVigente(SistemaActividadHumana nuevaactividad) {
		this.ActividadVigente = nuevaactividad;
	}

	@Override
	public List<Experiencia> getExperiencia() {
		return this.Experiencia;
	}

	@Override
	public Double getMayorUtilidadObtenida() {
		return this.MayorUtilidadObtenida;
	}

	@Override
	public void setMayorUtilidadObtenida(Double valor) {
		this.MayorUtilidadObtenida = valor;
	}

	@Override
	public List<SistemaActividadHumana> getActividadesEjecutables() {
		return this.ActividadesEjecutables;
	}

	@Override
	public SistemaActividadHumana getActividadVigente() {
		return this.ActividadVigente;
	}

	@Override
	public String printActividadVigente() {
		return this.ActividadVigente.getEnunciado();
	}

	@Override
	public void setUltimaUtilidadObtenida(Double ultimaUtilidadObtenida) {
		this.UltimaUtilidadObtenida = ultimaUtilidadObtenida;
	}

	@Override
	public Double getUltimaUtilidadObtenida() {
		return this.UltimaUtilidadObtenida;
	}

	@Override
	public void addExperiencia(Experiencia exp) {
		if(exp!=null)
			this.Experiencia.add(exp);	
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
	 * @param propositoVigente proposito a asignar al agente
	 */
	public void setPropositoVigente(Proposito propositoVigente) {
		PropositoVigente = propositoVigente;
	}
	/**
	 * Devuelve el dinero del agente
	 * @return Dinero
	 */
	public Dinero getDinero() {
		return Dinero;
	}
	
	/**
	 * Asigna el dienro del agente
	 * @param dinero dinero a asignar al agente
	 */
	public void setDinero(Dinero dinero) {
		Dinero = dinero;
	}
	
	/**
	 * Devuelve el tranpsorte del agente
	 * @return Camion
	 */
	public Camion getTransporte() {
		return transporte;
	}

	/**
	 * Asigna un transporte al agente
	 * @param transporte Camion que se le asigna al agente
	 */
	public void setTransporte(Camion transporte) {
		this.transporte = transporte;
	}

	/**
	 * Asigna la experiencia del agente
	 * @param experiencia arreglo de experiencias asignadas al agente
	 */
	public void setExperiencia(List<Experiencia> experiencia) {
		Experiencia = experiencia;
	}

	/**
	 * Devuelve el ambiente local del agente
	 * @return centroUrbano 
	 */
	public CentroUrbano getAmbienteLocal() {
		return AmbienteLocal;
	}
	
	/**
	 * Asigna el ambiente local del agente
	 * @param ambienteLocal CentroUrbano a asignar como ambiente local al agente 
	 */
	public void setAmbienteLocal(CentroUrbano ambienteLocal) {
		AmbienteLocal = ambienteLocal;
	}
	
	@Override
	public String LugarOferta() {
		return AmbienteLocal!=null?AmbienteLocal.getNombre():"";
	}

	@Override
	public Oferta generarOferta() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Demanda generarDemanda() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void realizarCompra() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean intencionDeConsolidacion() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setIntencionConsolidacion(boolean bool) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean pedidosRecibidos() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void distribuirProductosEnTienda(Recurso productos, double precioCompra) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPuntoDemanda(PlazaDistrital plaza) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PlazaDistrital getPuntoDemanda() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean productosPendientesVenta() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public int getId(){
		return this.id;
	}

}
