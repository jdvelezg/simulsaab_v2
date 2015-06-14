/**
 * 
 */
package simulaSAAB.agentes;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vividsolutions.jts.geom.Coordinate;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import simulaSAAB.comunicacion.MensajeACL;
import simulaSAAB.comunicacion.Oferta;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.comunicacion.Experiencia;
import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.comunicacion.Recurso;
import simulaSAAB.contextos.CentroUrbano;
import simulaSAAB.contextos.SaabContextBuilder;
import simulaSAAB.global.PropositosFactory;
import simulaSAAB.global.persistencia.AgentTrackObservable;
import simulaSAAB.inteligencia.Cerebro;
import simulaSAAB.tareas.ProcesoAgenteHumano;
import simulaSAAB.tareas.ProducirCebollaBulbo;
import simulaSAAB.tareas.SistemaActividadHumana;

/**
 * @author dampher
 *
 */
public class Productor implements AgenteInteligente, Oferente {
	
	private static Logger LOGGER = Logger.getLogger(Productor.class.getName());
	
	public static String ROL ="productor";
	
	public static String INTENCION ="producir";
	
	public static String OBJETIVO = "Garantizar su superviviencia obteniendo recursos para suplir sus necesidades de vida mediante la produccion y comercializacion de productos agrıcolas";
	
	public final ProductorTrack OBSERVABLE = new ProductorTrack(); 
	
	private Proposito PropositoVigente;
	
	private Dinero Dinero;
	
	private Cerebro CerebroProductor;
	
	private SistemaActividadHumana ProcesoHumanoDefinido;
	
	private SistemaActividadHumana ActividadVigente;
	
	private List<Experiencia> Experiencia;
	
	private List<Terreno> TerrenosCultivables;
	
	private CentroUrbano puntoOferta;
	
	private List<Oferta> Ofertas;
	
	private List<Recurso> Productos;
	
	private List<Producto> ProductosViablesPercibidos;
	
	private List<SistemaActividadHumana> ActividadesEjecutables;
	
	private Double MayorUtilidadObtenida;
	
	private Double UltimaUtilidadObtenida;
	
	private String Estado;

	/**
	 * 
	 */
	public Productor() {
								
		Experiencia 		= new ArrayList<Experiencia>();		
		TerrenosCultivables = new ArrayList<Terreno>();		
		Ofertas 			= new ArrayList<Oferta>();		
		Productos 			= new ArrayList<Recurso>();
		
		ProductosViablesPercibidos 	= new ArrayList<Producto>();		
		ActividadesEjecutables 		= new ArrayList<SistemaActividadHumana>();		
		
		CerebroProductor		= new Cerebro(this);
		ProcesoHumanoDefinido 	= new ProcesoAgenteHumano();
		
		Dinero					= new Dinero(new Double(0),"COP");
		UltimaUtilidadObtenida 	= new Double(0);
		MayorUtilidadObtenida	= new Double(0);	
		
		Estado 	= "IDLE";	
	}
	
	
	/**
	 * Metodo que ejecuta el comportamiento del agente en cada ciclo de reloj enviado por repast
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
		//LOGGER.log(Level.INFO, this.toString() + ": percibir mundo selectivamente");	
		
		//Consulta tareas ejecutables en el ambiente		
		for(Terreno finca : TerrenosCultivables){
			
			List<SistemaActividadHumana> actividadesAmbientales = finca.getAmbiente().getActividadesViables();
			
			//En caso que no tenga aun un proposito vigente, lo asigna.
			if(this.PropositoVigente==null)
				formarIntenciones();
			
			//Filtra según su proposito
			for(SistemaActividadHumana a: actividadesAmbientales){
				
				if(a.getProposito().compare(PropositoVigente))
					this.ActividadesEjecutables.add(a.getInstance());				
			}
			//System.out.println("WARNING: TOTAL ACT-EJECUTABLE: "+this.ActividadesEjecutables.size()+PropositoVigente.toString()+"Vs"+actividadesAmbientales.get(0).getProposito().toString()+" VIABLES");
		}
	}

	/* (non-Javadoc)
	 * @see simulaSAAB.agentes.AgenteInteligente#formarIntenciones()
	 */
	@Override
	public void formarIntenciones() {
		//LOGGER.log(Level.INFO, this.toString() + ": formar intenciones");
		
		if(PropositoVigente==null)
			PropositoVigente = new PropositosFactory(this.ROL,this.INTENCION).getProposito();
	}
	
	@Override
	public void tomarDecisiones() {
		//LOGGER.log(Level.INFO, this.toString() + ": Tomar decisiones");
		
		this.ActividadVigente = CerebroProductor.tomarDecision(ActividadesEjecutables);		
	}
	

	/* (non-Javadoc)
	 * @see simulaSAAB.agentes.AgenteInteligente#actuar()
	 */
	@Override
	public void actuar() {
		//LOGGER.log(Level.INFO, this.toString() + ": actuando "+ActividadVigente.toString());
		
		ActividadVigente.secuenciaPrincipalDeAcciones(this);
	}

	/* (non-Javadoc)
	 * @see simulaSAAB.agentes.AgenteInteligente#juzgarMundoSegunEstandares()
	 */
	@Override
	public void juzgarMundoSegunEstandares() {
		//LOGGER.log(Level.INFO, this.toString() + ": Juzgando");
		
		Experiencia exp = CerebroProductor.evaluarExperiencia();		
		if(!Experiencia.contains(exp)){			
			this.addExperiencia(exp);
		}
	}
	
	@Override
	public Oferta generarOferta() {
		//LOGGER.log(Level.INFO, this.toString() + ": generarOferta");
		
		/**
		 * Obtiene el primer producto de la lista y genera una oferta a partir del mismo.
		 * 
		 * Fija el precio unitario sumando 10% al costo
		 * 
		 * TODO Implementar una forma que el agente decida su ganancia al evaluar variables del mercado
		 */
		if(Productos.size()>0){
			
			Recurso producto = Productos.get(0);		
			double precio = producto.getCostoUnitario() + producto.getCostoUnitario()*0.1;
			
			Oferta novaOferta = new Oferta(producto,192,true,precio*producto.getCantidad());//192 8 dias
			novaOferta.setPuntoOferta(puntoOferta);
			novaOferta.setUbicacion(ubicacionOfertas());
			novaOferta.setVendedor(this);
			
			this.Ofertas.add(novaOferta);
			this.Productos.remove(producto);
			
			return novaOferta;
		}else{
			LOGGER.log(Level.SEVERE,this.toString()+" NO POSEE PRODUCTOS PARA OFERTAR");
			return null;
		}
		
	}
	
	/**getter - setters **/
	
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
	 * @param t: terreno a agregar
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
		// TODO Auto-generated method stub
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


	public List<SistemaActividadHumana> getActividadesEjecutables() {
		return ActividadesEjecutables;
	}


	private void setActividadesEjecutables(
			List<SistemaActividadHumana> actividadesEjecutables) {
		ActividadesEjecutables = actividadesEjecutables;
	}


	public Dinero getDinero() {
		return Dinero;
	}


	public void setDinero(Dinero dinero) {
		Dinero = dinero;
	}


	public List<Terreno> getTerrenosCultivables() {
		return TerrenosCultivables;
	}


	public void setTerrenosCultivables(List<Terreno> terrenosCultivables) {
		TerrenosCultivables = terrenosCultivables;
	}
	
	/**
	 * Agrega un producto al agente.
	 * Notifica a los observadores la adquisición del producto
	 * @param productos
	 */
	public void addProducto(Recurso productos){						
			
		this.Productos.add(productos);
		//Notifica cosecha o adquisición de productos
		this.OBSERVABLE.nuevaCosecha(productos.getCantidad(), productos.getCostoUnitario());			
	}
	
	public List<Recurso> getProductos(){
		
		return Productos;
	}


	public Proposito getPropositoVigente() {
		return PropositoVigente;
	}


	public void setPropositoVigente(Proposito propositoVigente) {
		PropositoVigente = propositoVigente;
	}


	public CentroUrbano getPuntoOferta() {
		return puntoOferta;
	}


	public void setPuntoOferta(CentroUrbano puntoOferta) {
		this.puntoOferta = puntoOferta;
	}
	
	public Coordinate ubicacionOfertas(){
		
		return this.TerrenosCultivables.get(0).getGeometria().getCoordinate();
	}


	@Override
	public void recibirMensaje(MensajeACL mssg) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String printActividadVigente(){
		return this.ActividadVigente.toString();
	}
	

	/**
	 * Clase anidada para la recoleccion de datos de simulación
	 * asociados al productor
	 * 
	 * @author dampher
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
		
		/**
		 * Constructor
		 * @param productorId ID del Agente productor al que corresponde
		 */
		public ProductorTrack(){
			
			super();
			this.productorId 	= Productor.this.toString();		
		}
					
		
		/**
		 * Actualiza los valores clave de recoleccion
		 * 
		 * @param cantidad
		 * @param precioUnitario
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
		
		/**
		 * Devuelve una cadena de texto con los datos separados por el caracter pasado como
		 * parámetro. EL orden d ela salida es :
		 * tick + ID del productor + hectareas del terreno + nombre centro urbano + coordenadas del terreno + cantidad cosechada + precio unitario
		 * 
		 * @param separador Caracter(es) usados como separador de los datos
		 * @return
		 */
		@Override
		public String dataLineString(String separador){
									
			return tick.toString()+separador+productorId+separador+hectareas.toString()+separador+
					centroUrbano+separador+coordenadas+separador+cosecha.toString()+separador+"$"+precioUnitario.toString()+separador;
		}
		
		@Override
		public String dataLineStringHeader(String separador){
			
			return "tick"+separador+"ID_productor"+separador+"hectareas"+separador+
					"centro_urbano"+separador+"coordenadas"+separador+"cantidad"+separador+"precio_unitario"+separador;
		}
		
		/**
		 * Getters & Setters 
		 */
		
		
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


	@Override
	public void atenderMensajes() {
		// TODO Auto-generated method stub
		
	}
	
	
}

