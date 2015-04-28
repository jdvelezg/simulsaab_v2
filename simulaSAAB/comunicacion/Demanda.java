package simulaSAAB.comunicacion;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import simulaSAAB.agentes.Demandante;
import simulaSAAB.contextos.PlazaDistrital;
import simulaSAAB.global.persistencia.ProductoConfigurado;

public class Demanda implements Concepto {
	
	/**
	 * String producto. Nombre del producto demandando
	 */
	private final String nombreproducto; 
	/**
	 * List<Recurso> Producto. Conjunto de Recursos que describen el producto ofertado y su cantidad 
	 */
	private Double Cantidad;
	/**
	 * int Vigencia. Contiene el número de ciclos que una oferta esta vigente
	 */
	private int Vigencia;
	/**
	 * boolean Consolidable. Indica si la oferta puede ser consolidada con otras.
	 */
	private boolean Consolidable;
	/**
	 * String Estado. Describe el estado de la oferta
	 * - VIGENTE
	 * - VENCIDA
	 * - ATENDIDA
	 * - CREADA
	 */
	private String Estado;
	/**
	 * Guarda el ciclo de tiempo en que fue registrada la oferta, para calcular su vencimiento
	 */
	private Double Tickinicial;
	/**
	 * Demandante comprador. Agente que registra la demanda.
	 */
	private Demandante comprador;
	/**
	 * PlazaDistrital puntoDemanda. EL punto de demanda correspondiente
	 */
	private PlazaDistrital puntoDemanda;
	
	

	public Demanda(String producto) {
		
		this.nombreproducto 	= producto;
	}
	
	public Demanda(String producto, Double cantidad, int ciclosVigencia, boolean consolidable){
		
		this.nombreproducto 		= producto;
		this.Cantidad		= cantidad;
		this.Vigencia 		= ciclosVigencia;
		this.Consolidable 	= consolidable;
		this.Estado			="CREADA";
		
	}
	
	/**
	 * Calcula el vencimiento de la Demanda despues de ser registrada
	 */
	@ScheduledMethod (start = 1, interval = 1, priority = 0)
	public void step () {
		
		if(vigente()){
			
			Double CurrentTick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
			if(CurrentTick - Tickinicial>Vigencia)
				this.setVencida();
		}
	}
	
	/**
	 * Devuelve True si la oferta ya fué atendida.
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
	 * Devuelve True si la oferta continua vigente
	 * @return
	 */
	public boolean vigente(){
		
		if(this.Estado.equalsIgnoreCase("VIGENTE"))
			 return true;
		 else
			 return false;
	}	
	
	/**
	 * Devuelve en dinero un estimado de la capacidad adquisitiva de la demanda.
	 * Es usado por el sistema para estimar la capacidad de compra inmersa en la
	 * demanda registrada.
	 * 
	 * @return Double
	 */
	public Double getPresupuesto(){
		
		Producto prod		= new Producto(new ProductoConfigurado(this.nombreproducto));
		Double presupuesto 	= prod.getPrecioEnMercado()*this.Cantidad;
		
		return presupuesto;
	}
	
	
	/**getters & setters **/
	
	
	public Double getCantidadDemandanda(){
		
		return Cantidad;
	}

	public int getVigencia() {
		return Vigencia;
	}

	public void setVigencia(int vigencia) {
		Vigencia = vigencia;
	}

	public boolean isConsolidable() {
		return Consolidable;
	}

	public void setConsolidable(boolean consolidable) {
		Consolidable = consolidable;
	}

	public String getEstado() {
		return Estado;
	}

	public void setEstado(String estado) {
		
		if(estado.equalsIgnoreCase("VIGENTE"))
			setVigente();
		else if(estado.equalsIgnoreCase("VENCIDA"))
			setVencida();
		else
			Estado = estado;
	}
	
	public void setAtendida(){
		setEstado("ATENDIDA");
	}
	
	public void setVigente(){
		
		Tickinicial	= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		setEstado("VIGENTE");
	}
	
	public void setVencida(){
		setEstado("VENCIDA");
	}

	public Double getCantidad() {
		return Cantidad;
	}

	public void setCantidad(Double cantidad) {
		Cantidad = cantidad;
	}

	public String getNombreproducto() {
		return nombreproducto;
	}
	
	public Producto getProducto(){
		return new Producto(nombreproducto);
	}

	public Demandante getComprador() {
		return comprador;
	}

	public void setComprador(Demandante comprador) {
		this.comprador = comprador;
	}

	public PlazaDistrital getPuntoDemanda() {
		return puntoDemanda;
	}

	public void setPuntoDemanda(PlazaDistrital puntoDemanda) {
		this.puntoDemanda = puntoDemanda;
	}
	
	
	

}
