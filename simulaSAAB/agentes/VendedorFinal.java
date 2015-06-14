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

import bsh.This;
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
import simulaSAAB.global.PropositosFactory;
import simulaSAAB.inteligencia.Cerebro;
import simulaSAAB.tareas.ConsolidarDemanda;
import simulaSAAB.tareas.EstadosActividad;
import simulaSAAB.tareas.ProcesoAgenteHumano;
import simulaSAAB.tareas.SistemaActividadHumana;

/**
 * @author dampher
 *
 */
public class VendedorFinal implements AgenteInteligente, Demandante {
	
	public static String ROL ="vendedor";
	
	public static String INTENCION ="vender";
	
	private static String OBJETIVO;
	
	private Proposito PropositoVigente;
	
	private simulaSAAB.comunicacion.Dinero Dinero;	
	
	private Cerebro CerebroVendedor;
	
	private ProcesoAgenteHumano ProcesoHumanoDefinido;
	
	private SistemaActividadHumana ActividadVigente;
	
	private Queue<MensajeACL> MensajesRecibidos;
	
	protected Queue<OrdenDePedido> pedidosRecibidos;
	
	private Map<Integer, Queue<MensajeACL>> MensajesEnviados;
	
	private boolean MensajeAceptado;
	
	private List<Experiencia> Experiencia;
	
	private List<Tienda> Tiendas;
	
	protected PlazaDistrital puntoDemanda;
	
	private List<Demanda> Demandas;
	
	private List<Producto> Productos;
	
	private List<Producto> ProductosViablesPercibidos;
	
	private List<SistemaActividadHumana> ActividadesEjecutables;
	
	private Double MayorUtilidadObtenida;
	
	private Double UltimaUtilidadObtenida;
	
	private String Estado;
	
	private boolean intencionDeConsolidacion;
	
	/**
	 * Constructor
	 */
	public VendedorFinal(){
		
		Experiencia 		= new ArrayList<Experiencia>();		
		Tiendas				= new ArrayList<Tienda>();		
		Demandas			= new ArrayList<Demanda>();		
		Productos 			= new ArrayList<Producto>();
		
		ProductosViablesPercibidos 	= new ArrayList<Producto>();		
		ActividadesEjecutables 		= new ArrayList<SistemaActividadHumana>();		
		
		CerebroVendedor			= new Cerebro(this);
		ProcesoHumanoDefinido 	= new ProcesoAgenteHumano();
		
		Dinero					= new Dinero(new Double(0),"COP");
		UltimaUtilidadObtenida 	= new Double(0);
		MayorUtilidadObtenida	= new Double(0);	
		
		pedidosRecibidos	= new ConcurrentLinkedDeque<OrdenDePedido>();
		
		MensajesEnviados	= new ConcurrentHashMap<Integer, Queue<MensajeACL>>();
		MensajesRecibidos	= new ConcurrentLinkedDeque<MensajeACL>();
		
		Estado 	= "IDLE";
	}

	
	/**
	 * Metodo que ejecuta el comportamiento del agente en cada ciclo de reloj enviado por repast
	 * Primero verifica que el agente no tenga ningun mensaje pendiente a procesar.
	 * Luego ejecuta el proceso humano definido para un agente humano activo en el mundo
	 */
	@ScheduledMethod (start = 10, interval = 1)
	public void step () {
		
		if(ProcesoHumanoDefinido.getEstado().equalsIgnoreCase(EstadosActividad.READY.toString()) && this.MensajesRecibidos.size()>0){
			//Procesa el mensaje que tiene en espera
			atenderMensajes();
			
		}else{
			ProcesoHumanoDefinido.secuenciaPrincipalDeAcciones(this);	
		}
			
	}
	
	
	
	/* (non-Javadoc)
	 * @see simulaSAAB.agentes.AgenteInteligente#atribuirSignificadoAlMundoPercibido()
	 */
	@Override
	public void percibirMundoSelectivamente() {
		
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
			this.PropositoVigente = new PropositosFactory(this.ROL,this.INTENCION).getProposito();		
	}

	@Override
	public void tomarDecisiones() {
		
		if(ActividadesEjecutables.size()>0)
			this.ActividadVigente = CerebroVendedor.tomarDecision(ActividadesEjecutables);
		else
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
		Producto producto 	= ProductosViablesPercibidos.get(0);		
		Double cantidad		= Math.floor(Dinero.getCantidad()/producto.getPrecioEnMercado());
		
		Demanda novaDemanda = new Demanda(producto.getNombre(),cantidad,192,false);//192ciclos -> 8 dias
		novaDemanda.setPuntoDemanda(puntoDemanda);
		novaDemanda.setComprador(this);
		this.Demandas.add(novaDemanda);
		
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
	
	@Override
	public synchronized void recibirMensaje(MensajeACL mssg) {
		
		Integer MensajeID 	= mssg.getConversationID();
		
		//Si es una respuesta a un mensaje enviado
		if(MensajesEnviados.containsKey(MensajeID)){
			
			Queue mensajes = MensajesEnviados.get(MensajeID);
			mensajes.offer(mssg);	
			
		}else{
			
			if(MensajesRecibidos == null){
				this.MensajesRecibidos = new ConcurrentLinkedDeque();
			}
			this.MensajesRecibidos.offer(mssg);
		}	
	}
	
	public void atenderMensajes() throws NullPointerException{
		
		MensajeACL mensaje = this.MensajesRecibidos.poll();		
		
		if(mensaje.getPerformative().equalsIgnoreCase("propose")){//solo acepta proposals
			
			Preposicion contenido = mensaje.getContent();
			/**
			 * Acepta el mensaje si es una proposicion para consolidacion 
			 */
			if(contenido instanceof EjecutarAccionConProposito){
				
				EjecutarAccionConProposito proposal = (EjecutarAccionConProposito) contenido;
				SistemaActividadHumana actividad	= proposal.getActividad();
				
				if(actividad instanceof ConsolidarDemanda && this.intencionDeConsolidacion()){
					
					mensaje.addReceiver(this);
					MensajeACL respuesta 		= mensaje.getReply_with();
					AgenteInteligente sender 	= mensaje.getReply_to();
					sender.recibirMensaje(respuesta);
					this.MensajeAceptado 		= true;
					
					this.ActividadVigente = actividad.getInstance();
					this.ProcesoHumanoDefinido.setPaso(3);
					
				}else{
					rejectMessage(mensaje);
				}
			}
			
		}else if(mensaje.getPerformative().equalsIgnoreCase("reject-proposal")){
			//No hace nada
		}else if(mensaje.getPerformative().equalsIgnoreCase("accept-proposal")){
			
			Preposicion contenido = mensaje.getContent();
			/**
			 * Evalua si el mensaje si es una aceptacion de consolidacion
			 * TODO evaluar otras accept-proposal 
			 */
			if(contenido instanceof EjecutarAccionConProposito){
				
				EjecutarAccionConProposito proposal = (EjecutarAccionConProposito) contenido;
				SistemaActividadHumana actividad	= proposal.getActividad();
				
				if(actividad instanceof ConsolidarDemanda){
					/**
					 * Se incluye en la nutrired.
					 * 
					 * Es valido mientras el sistema de actividad 'conformarNutrired' no posea pasos secuenciales de lo contrario
					 * TODO se debe generar la inclusion a la nutrired con un iterador
					 *  
					 */
					actividad.secuenciaPrincipalDeAcciones(this);
				}						
			}
			
		}else{			
			rejectMessage(mensaje);			
		}
		//TODO agregar procesamiento de otras preformativas cfp-inform.. etc		
	}
	
	/**
	 * Devuelve un mensaje ACL con la respuesta negativa al mensaje pasado como parametro
	 * @param mensaje
	 * 		Mensaje ACL
	 * @return
	 * 		Mensaje ACL
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
	
	/**
	 * Recibe un pedido despachado en servicio logistico
	 */
	public synchronized void recibirPedido(OrdenDePedido pedido){
		pedidosRecibidos.offer(pedido);
		//actualiza obervador
		pedido.OBSERVABLE.PedidoEntregado();
	}
	
	@Override
	public boolean pedidosRecibidos(){
		return !this.pedidosRecibidos.isEmpty();
	}
	
	@Override
	public void gestionarPedidos() {
		
		while(!(pedidosRecibidos.isEmpty())){
			
			List<OrdenDeCompra> ordenes = pedidosRecibidos.poll().getOrdenesDeCompra();
			
			
			
			for(OrdenDeCompra compra: ordenes){
				
				distribuirProductosEnTienda(compra.getOferta().getProductos());
			}			
		}		
	}
	
	/**
	 * Almacena los productos comprados en las tiendas para su venta
	 * @param productos
	 */
	public void distribuirProductosEnTienda(Recurso productos){
		
		//TODO cambiar si el agente tiene mas de una tienda
		Tienda tienda = this.Tiendas.get(0);
		tienda.AlmacenarProductos(productos);		
	}
	
	/**getter - setters **/
	
	@Override
	public void setActividadVigente(SistemaActividadHumana nuevaactividad) {
		this.ActividadVigente = nuevaactividad;		
	}
	
	@Override
	public String getEstado() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return MayorUtilidadObtenida;
	}

	@Override
	public void setMayorUtilidadObtenida(Double valor) {
		// TODO Auto-generated method stub
		this.MayorUtilidadObtenida = valor;
	}

	@Override
	public List<SistemaActividadHumana> getActividadesEjecutables() {
		// TODO Auto-generated method stub
		return ActividadesEjecutables;
	}

	@Override
	public SistemaActividadHumana getActividadVigente() {
		// TODO Auto-generated method stub
		return ActividadVigente;
	}

	@Override
	public Double getUltimaUtilidadObtenida() {
		// TODO Auto-generated method stub
		return UltimaUtilidadObtenida;
	}

	@Override
	public void addExperiencia(simulaSAAB.comunicacion.Experiencia exp) {
		// TODO Auto-generated method stub
		if(exp!=null)
			this.Experiencia.add(exp);
	}


	@Override
	public void setEstado(String Estado) {
		// TODO Auto-generated method stub
		
	}


	public simulaSAAB.comunicacion.Dinero getDinero() {
		return Dinero;
	}


	public void setDinero(simulaSAAB.comunicacion.Dinero dinero) {
		Dinero = dinero;
	}
	
	public void addDinero(Double dinero) {
		Dinero.addCantidad(dinero);
	}


	public List<Tienda> getTiendas() {
		return Tiendas;
	}


	public void addTienda(Tienda tienda) {
		
		if(Tiendas == null)
			Tiendas = new ArrayList();
		
		Tiendas.add(tienda);
	}


	public PlazaDistrital getPuntoDemanda() {
		return puntoDemanda;
	}


	public void setPuntoDemanda(PlazaDistrital puntoDemanda) {
		this.puntoDemanda = puntoDemanda;
	}
	
	@Override
	public String printActividadVigente(){
		return this.ActividadVigente.toString();
	}

}
