package simulaSAAB.comunicacion;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import simulaSAAB.agentes.Demandante;
import simulaSAAB.comunicacion.Oferta.OfertaTrack;
import simulaSAAB.contextos.PlazaDistrital;
import simulaSAAB.global.persistencia.AgentTrackObservable;
import simulaSAAB.global.persistencia.ProductoConfigurado;

/**
 * Representa el concepto de <code>demanda</code> usado en los registros del SISAAB y en la ontología para comunicación entre los agentes  
 * 
 * @author jdvelezg
 *
 */
public class Demanda implements Concepto {
	/**
	 * Utilizada para la asignación de identificadores consecutivos a las demandas
	 */
	private static int CONSECUTIVO;
	/**
	 * Identificador único de la demanda
	 */
	private final int id;
	/**
	 * agrega funcionalidades <code>observable</code> a la clase
	 */
	public final DemandaTrack OBSERVABLE = new DemandaTrack();	
	/**
	 * Nombre del producto demandando
	 */
	private final String nombreproducto; 
	/**
	 * cantidad del producto demandado
	 */
	private Double Cantidad;
	/**
	 * número de ciclos que una oferta debe estar vigente
	 */
	private int Vigencia;
	/**
	 * Indica si la oferta puede ser consolidada con otras
	 */
	private boolean Consolidable;
	/**
	 * Describe el estado de la oferta
	 * - VIGENTE
	 * - VENCIDA
	 * - ATENDIDA
	 * - CREADA
	 */
	private String Estado;
	
	private Double Tickinicial;
	/**
	 * agente que genera la demanda
	 */
	private Demandante comprador;
	/**
	 * punto de demanda de la demanda
	 */
	private PlazaDistrital puntoDemanda;
	
	
	/**
	 * Constructor de la clase
	 * 
	 * @param producto string, nombre dle producto demandado
	 */
	public Demanda(String producto) {
		this.id = setId();
		this.nombreproducto 	= producto;
	}
	
	/**
	 * 
	 * @param producto string, nombre del producto demandado
	 * @param cantidad double, cantidad d eproducto demandado
	 * @param ciclosVigencia int, número de ciclos que debe estar vigente la demanda
	 * @param consolidable boolean, <code>true</code> si la demanda debe ser consolidable
	 */
	public Demanda(String producto, Double cantidad, int ciclosVigencia, boolean consolidable){
		this.id = setId();
		this.nombreproducto = producto;
		this.Cantidad		= cantidad;
		this.Vigencia 		= ciclosVigencia;
		this.Consolidable 	= consolidable;
		this.Estado			="CREADA";
		
	}
	/**
	 * Devuelve un identificador consecutivo para la demanda
	 * @return int identificador
	 */
	private int setId(){
		int i = this.CONSECUTIVO+1;
		this.CONSECUTIVO++;
		return i;
	}
	
	/**
	 * verifica en cada ciclo de reloj <code>Tick</code> si la demanda esta vencida
	 */	
	public void step () {
	
		if(vigente()){			
			Double CurrentTick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
			if(CurrentTick - Tickinicial>Vigencia)
				this.setVencida();
		}
	}
	
	/**
	 * Devuelve <code>true</code> si la oferta ya fué atendida.
	 * 
	 * @return boolean
	 */
	public boolean atendida(){
		
		 if(this.Estado.equalsIgnoreCase("ATENDIDA"))
			 return true;
		 else
			 return false;
	}
	
	/**
	 * Devuelve <code>true</code> si la oferta continua vigente
	 * @return boolean
	 */
	public boolean vigente(){
		
		if(this.Estado.equalsIgnoreCase("VIGENTE"))
			 return true;
		 else
			 return false;
	}	
	
	/**
	 * Devuelve un estimado de la capacidad adquisitiva de la demanda
	 * <p>
	 * Es usado para estimar la capacidad de compra inmersa en la demanda
	 * 
	 * @return double
	 */
	public Double getPresupuesto(){
		
		double precioProducto	= new Producto(new ProductoConfigurado(this.nombreproducto)).getPrecioEnMercado();
		double presupuesto 		= precioProducto*Cantidad;
		
		return presupuesto;
	}
	
	
	/**
	 * Devuelve la cantidad demandada	
	 * @return double
	 */
	public Double getCantidadDemandanda(){		
		return Cantidad;
	}
	/**
	 * Devuelve la vigencia de la demanda
	 * @return int
	 */
	public int getVigencia() {
		return Vigencia;
	}
	/**
	 * Asigna una vigencia a la demanda
	 * @param vigencia int, numero de cilco en que permanecevigente la oferta una vez registrada
	 */
	public void setVigencia(int vigencia) {
		Vigencia = vigencia;
		OBSERVABLE.setDemandaRegistrada();
	}
	/**
	 * Devuelve <code>true</code> si la demanda es consolidable
	 * @return booelan
	 */
	public boolean isConsolidable() {
		return Consolidable;
	}
	/**
	 * Asigna el valor de consolidación
	 * @param consolidable boolean, <code>true</code> si la demanda es consolidable 
	 */
	public void setConsolidable(boolean consolidable) {
		Consolidable = consolidable;
	}
	/**
	 * Devuelve el estado de la demanda
	 * @return string
	 */
	public String getEstado() {
		return Estado;
	}
	/**
	 * Asigna un estado a la demanda
	 * @param estado string, estado a asignar a la demanda
	 */
	public void setEstado(String estado) {		
		Estado = estado;
		OBSERVABLE.setDemandaRegistrada();
	}
	/**
	 * Fija el estado de la demada como <code>atendida</code>
	 */
	public void setAtendida(){
		setEstado("ATENDIDA");
	}
	/**
	 * Fija el estado de la demada como <code>vigente</code>
	 */
	public void setVigente(){
		
		Tickinicial	= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		setEstado("VIGENTE");
		OBSERVABLE.setDemandaRegistrada();
	}
	/**
	 * Fija el estado de la demada como <code>vencida</code>
	 */
	public void setVencida(){
		setEstado("VENCIDA");
		OBSERVABLE.setDemandaRegistrada();
	}
	/**
	 * Devuelve la cantidad demandada
	 * @return double
	 */
	public Double getCantidad() {
		return Cantidad;
	}
	/**
	 * Asigna la cantidad demandada
	 * @param cantidad double cantidad demandada
	 */
	public void setCantidad(Double cantidad) {
		Cantidad = cantidad;
	}
	/**
	 * Devuelve el nombre dle producto demandado
	 * @return string
	 */
	public String getNombreproducto() {
		return nombreproducto;
	}
	/**
	 * Devuelve el producto demandado
	 * @return Producto
	 */
	public Producto getProducto(){
		return new Producto(nombreproducto);
	}
	/**
	 * Devuelve el agente generador de la demanda
	 * @return Demandante agente
	 */
	public Demandante getComprador() {
		return comprador;
	}
	/**
	 * Asigna el agente generador de la demanda
	 * @param comprador demandante, agente que genera la demanda 
	 */
	public void setComprador(Demandante comprador) {
		this.comprador = comprador;
	}
	/**
	 * Devuelve el punto de demanda que le coresponde
	 * @return PlazaDistrital
	 */
	public PlazaDistrital getPuntoDemanda() {
		return puntoDemanda;
	}
	/**
	 * Asigna el punto de demanda que le corresponde
	 * @param puntoDemanda PlazaDistrital que representa el punto de demanda
	 */
	public void setPuntoDemanda(PlazaDistrital puntoDemanda) {
		this.puntoDemanda = puntoDemanda;
	}
	/**
	 * Devuelve el identificador de la demanda
	 * @return id int
	 */
	public int getId() {
		return id;
	}

	/**
	 * Clase anidada a la que se le delega la funcionalidad <code>obserbable</code> de la clase <code>Demanda</code>
	 * @author jdvelezg
	 *
	 */
	public class DemandaTrack extends AgentTrackObservable{
			
			private Double tick;
			
			private final String demandaID;
			
			private String demandanteID;
			
			private Double presupuesto;
			
			private String producto;
			
			private Double cantidad;
			
			private String estado;
			
			
			public DemandaTrack(){
				
				super();
				this.demandaID		= Demanda.this.toString();					
			}
			
			/**
			 * reporta el registro de la demanda 
			 */
			public void setDemandaRegistrada(){
				
				this.tick			= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
				this.demandanteID	= Demanda.this.comprador.toString();
				this.producto		= Demanda.this.getNombreproducto();
				this.cantidad		= Demanda.this.Cantidad;
				this.presupuesto	= Demanda.this.getPresupuesto();
				this.estado			= Demanda.this.Estado;
				
				super.setChanged();
				super.notifyObservers(this);
			}
	
			@Override
			public String dataLineString(String separador) {			
				
				return tick.toString()+separador+demandaID+separador+demandanteID+separador+producto+separador+cantidad.toString()+separador+
						presupuesto.toString()+separador+estado+separador;
			}
	
			@Override
			public String dataLineStringHeader(String separador) {
				
				return "tick"+separador+"demanda_ID"+separador+"demandante_ID"+separador+"producto"+separador+"cantidad"+separador+"presupuesto"+separador+"Estado"+separador;
			}
			
			public Double getTick() {
				return tick;
			}

			public void setTick(Double tick) {
				this.tick = tick;
			}

			public String getDemandaID() {
				return demandaID;
			}			

			public String getDemandanteID() {
				return demandanteID;
			}			

			public Double getPresupuesto() {
				return presupuesto;
			}

			public void setPresupuesto(Double presupuesto) {
				this.presupuesto = presupuesto;
			}

			public String getProducto() {
				return producto;
			}

			public void setProducto(String producto) {
				this.producto = producto;
			}

			public Double getCantidad() {
				return cantidad;
			}

			public void setCantidad(Double cantidad) {
				this.cantidad = cantidad;
			}			
			
	}
	

}
