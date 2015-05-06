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
 * @author dampher
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
	
	
	public AmbienteLocal(){
		super();
	}
	
	
	public AmbienteLocal(String nombre){
		this.Nombre=nombre;
	}
	
	public void addActividadViable(SistemaActividadHumana act){
		
		if(this.ActividadesViables==null)
			this.ActividadesViables = new ArrayList();
		this.ActividadesViables.add(act);
	}
	
	public void addProductoAgricolaViable(Producto prod){
		
		if(this.ProductosAgricolasViables ==null)
			this.ProductosAgricolasViables = new ArrayList();
		this.ProductosAgricolasViables.add(prod);
	}
	
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


	public String getNombre() {
		return Nombre;
	}


	public void setNombre(String nombre) {
		Nombre = nombre;
	}


	public List<Producto> getProductosAgricolasViables() {
		return ProductosAgricolasViables;
	}


	public void setProductosAgricolasViables(
			List<Producto> productosAgricolasViables) {
		ProductosAgricolasViables = productosAgricolasViables;
	}


	public List<SistemaActividadHumana> getActividadesViables() {
		return ActividadesViables;
	}


	public void setActividadesViables(
			List<SistemaActividadHumana> actividadesViables) {
		ActividadesViables = actividadesViables;
	}


	public List<NodoSaab> getNodosSaab() {
		return NodosSaab;
	}


	public void setNodosSaab(List<NodoSaab> nodosSaab) {
		NodosSaab = nodosSaab;
	}


	public List<CentroUrbano> getCentrosUrbanos() {
		return CentrosUrbanos;
	}


	public void setCentrosUrbanos(List<CentroUrbano> centrosUrbanos) {
		CentrosUrbanos = centrosUrbanos;
	}


	public Coordinate getRoadAccess() {
		return roadAccess;
	}


	public void setRoadAccess(Coordinate roadAccess) {
		this.roadAccess = roadAccess;
	}
	
	
	
	

}
