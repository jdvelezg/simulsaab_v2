/**
 * 
 */
package simulaSAAB.agentes;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.engine.schedule.ScheduledMethod;
import simulaSAAB.comunicacion.Oferta;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.comunicacion.Experiencia;
import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.comunicacion.Recurso;
import simulaSAAB.contextos.CentroUrbano;
import simulaSAAB.global.PropositosFactory;
import simulaSAAB.inteligencia.Cerebro;
import simulaSAAB.tareas.ProcesoAgenteHumano;
import simulaSAAB.tareas.SistemaActividadHumana;

/**
 * @author dampher
 *
 */
public class Productor implements AgenteInteligente, Oferente {
	
	public static String ROL ="productor";
	
	public static String INTENCION ="producir";
	
	public static String OBJETIVO = "Garantizar su superviviencia obteniendo recursos para suplir sus necesidades de vida mediante la produccion y comercializacion de productos agrıcolas";
	
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
	@ScheduledMethod (start = 1, interval = 2)
	public void step () {
		ProcesoHumanoDefinido.secuenciaPrincipalDeAcciones(this);		
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see simulaSAAB.agentes.AgenteInteligente#atribuirSignificadoAlMundoPercibido()
	 */
	@Override
	public void percibirMundoSelectivamente() {
		
		//Consulta tareas ejecutables en el ambiente		
		for(Terreno finca : TerrenosCultivables){
			
			List<SistemaActividadHumana> actividadesAmbientales = finca.getAmbiente().getActividadesViables();
			
			//En caso que no tenga aun un proposito vigente, lo asigna.
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
		
		PropositoVigente = new PropositosFactory(this.ROL,this.INTENCION).getProposito();
	}
	
	@Override
	public void tomarDecisiones() {
		
		this.ActividadVigente = CerebroProductor.tomarDecision(ActividadesEjecutables);		
	}
	

	/* (non-Javadoc)
	 * @see simulaSAAB.agentes.AgenteInteligente#actuar()
	 */
	@Override
	public void actuar() {
		
		ActividadVigente.secuenciaPrincipalDeAcciones(this);
	}

	/* (non-Javadoc)
	 * @see simulaSAAB.agentes.AgenteInteligente#juzgarMundoSegunEstandares()
	 */
	@Override
	public void juzgarMundoSegunEstandares() {
		
		Experiencia exp = CerebroProductor.evaluarExperiencia();		
		if(!Experiencia.contains(exp)){			
			this.addExperiencia(exp);
		}
	}
	
	@Override
	public Oferta generarOferta() {
		/**
		 * Obtiene el primer producto de la lista y genera una oferta a partir del mismo.
		 * 
		 * Fija el precio unitario sumando 10% al costo
		 * 
		 * TODO Implementar una forma que el agente decida su ganancia al evaluar variables del mercado
		 */
		Recurso producto = this.Productos.get(0);		
		double precio = producto.getCostoUnitario() + producto.getCostoUnitario()*0.1;
		
		Oferta novaOferta = new Oferta(producto,192,true,precio*producto.getCantidad());
		novaOferta.setPuntoOferta(puntoOferta);
		
		this.Ofertas.add(novaOferta);
		this.Productos.remove(producto);
		
		return novaOferta;
	}
	
	/**getter - setters **/
	
	@Override
	public String getEstado() {
		// TODO Auto-generated method stub
		return this.Estado;
	}
	
	@Override
	public void setEstado(String Estado){
		this.Estado = Estado;
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
		UltimaUtilidadObtenida = ultimaUtilidadObtenida;
	}


	private void setActividadVigente(SistemaActividadHumana actividadVigente) {
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

	public void addProducto(Recurso productos){
		if(this.Productos!=null)
			this.Productos.add(productos);
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
	
	
	


	
	
	
	


}
