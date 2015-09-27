/**
 * 
 */
package simulaSAAB.contextos;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import repast.simphony.context.DefaultContext;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.global.VariablesGlobales;
import simulaSAAB.tareas.SistemaActividadHumana;

/**
 * Representa el ambiente local presente en la simulación
 * <p>
 * Delimitación geográfica en el que se ubica a los agentes del modelo 
 * 
 * @author jdvelezg
 *
 */
public class AmbienteLocal extends DefaultContext<Object> implements GeografiaFija {
	
	private String Nombre;
	
	private Geometry Geometria;
	
	private String Clima;
	
	private String PisoTermico;
	
	private List<Producto> ProductosAgricolasViables;
	
	private List<SistemaActividadHumana> ActividadesViables;
	
	private List<NodoSaab> NodosSaab;
	
	private List<CentroUrbano> CentrosUrbanos;
	
	private Coordinate roadAccess;
	
	private String nodosCercanos;
	
	/**
	 * Constructor
	 */
	public AmbienteLocal(){
		
		super();
		ActividadesViables 			= new ArrayList<SistemaActividadHumana>();
		ProductosAgricolasViables	= new ArrayList<Producto>();
		NodosSaab					= new ArrayList<NodoSaab>();
		CentrosUrbanos				= new ArrayList<CentroUrbano>();
		nodosCercanos				= "";
	}
	
	/**
	 * Constructor
	 * @param nombre nombre del ambiente local
	 */
	public AmbienteLocal(String nombre){
		
		super();
		this.Nombre	= nombre;
		
		ActividadesViables 			= new ArrayList<SistemaActividadHumana>();
		ProductosAgricolasViables	= new ArrayList<Producto>();
		NodosSaab					= new ArrayList<NodoSaab>();
		CentrosUrbanos				= new ArrayList<CentroUrbano>();
		nodosCercanos				= "";
	}
	/**
	 * Agrega una actividad viable en el ambiente
	 * @param act SistemaActividadHumana, actividad ejecutable en el ambiente
	 */
	public void addActividadViable(SistemaActividadHumana act){
		
		if(this.ActividadesViables==null)
			this.ActividadesViables = new ArrayList<SistemaActividadHumana>();
		this.ActividadesViables.add(act);
	}
	/**
	 * Agrega un producto agrícola viable en el ambiente
	 * @param prod producto viable en el ambiente
	 */
	public void addProductoAgricolaViable(Producto prod){
		
		if(this.ProductosAgricolasViables ==null)
			this.ProductosAgricolasViables = new ArrayList();
		this.ProductosAgricolasViables.add(prod);
	}
	/**
	 * Agrega un centro urbano al ambiente
	 * @param pbl CentroUrbano perteneciente al ambiente local
	 */
	public void addCentroUrbano(CentroUrbano pbl){
		if(this.CentrosUrbanos==null)
			this.CentrosUrbanos = new ArrayList();
		this.CentrosUrbanos.add(pbl);
	}

	@Override
	public Geometry getGeometria() {
		return this.Geometria;		
	}

	@Override
	public void setGeometria(Geometry geom) {
		this.Geometria=geom;
		
	}

	/**
	 * Devuelve el nombre
	 * @return
	 */
	public String getNombre() {
		return Nombre;
	}

	/**
	 * Asigna el nombre
	 * @param nombre nombre del ambiente local
	 */
	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	/**
	 * Devuelve un listado de los producto viables en el ambiente
	 * @return List<Producto>
	 */
	public List<Producto> getProductosAgricolasViables() {
		return ProductosAgricolasViables;
	}

	/**
	 * Asigna el listado de productos agricolas viables
	 * @param productosAgricolasViables
	 */
	public void setProductosAgricolasViables(
			List<Producto> productosAgricolasViables) {
		ProductosAgricolasViables = productosAgricolasViables;
	}

	/**
	 * Devuelve un lsitado de las actividades <code>MPA</code> viables en el ambiente
	 * @return List<SistemaActividadHumana>
	 */
	public List<SistemaActividadHumana> getActividadesViables() {
		return ActividadesViables;
	}

	/**
	 * Asigna el listado de actividades viables en el ambiente
	 * @param actividadesViables
	 */
	public void setActividadesViables(
			List<SistemaActividadHumana> actividadesViables) {
		ActividadesViables = actividadesViables;
	}

	/**
	 * Devuelve el listado de nodos saab asociados al ambiente
	 * @return
	 */
	public List<NodoSaab> getNodosSaab() {
		return NodosSaab;
	}

	/**
	 * Asigna el listado de nodos saab asociados al ambiente
	 * @param nodosSaab
	 */
	public void setNodosSaab(List<NodoSaab> nodosSaab) {
		NodosSaab = nodosSaab;
	}
	/**
	 * Agrega un nodo saab al ambiente
	 * @param nodo nodo saab asociado al ambiente
	 */
	public void addNodoSaab(NodoSaab nodo){
		
		if(this.NodosSaab == null){
			this.NodosSaab = new ArrayList<NodoSaab>();
		}
		this.NodosSaab.add(nodo);
	}
	/**
	 * Devuelve el listado de centros urbanos asociados al ambiente
	 * @return List<CentroUrbano>
	 */
	public List<CentroUrbano> getCentrosUrbanos() {
		return CentrosUrbanos;
	}

	/**
	 * asigna el listado de centros urbanos 
	 * @param centrosUrbanos listado de centros urbanos asociados al ambiente
	 */
	public void setCentrosUrbanos(List<CentroUrbano> centrosUrbanos) {
		CentrosUrbanos = centrosUrbanos;
	}

	/**
	 * Devuelve las coordenadas del punto de acceso por carretera del ambiente
	 * @return Coordinate 
	 */
	public Coordinate getRoadAccess() {
		return roadAccess;
	}

	/**
	 * Asigna las coordenadas del punto de acceso 
	 * @param roadAccess Coordinate, coordenadas de acceso por carretera al ambiente
	 */
	public void setRoadAccess(Coordinate roadAccess) {
		this.roadAccess = roadAccess;
	}

	/**
	 * Devuelve el nombre de los nodos cercanos al ambiente
	 * @return string nombre de los nodos cercanos
	 */
	public String getNodosCercanos() {
		return nodosCercanos;
	}

	/**
	 * Asigna el nombre de los nodos cercanos al ambiente
	 * @param nodosCercanos nombre del nodo cercano
	 */
	public void setNodosCercanos(String nodosCercanos) {
		this.nodosCercanos = nodosCercanos;
	}
		

}
