/**
 * 
 */
package simulaSAAB.contextos;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import repast.simphony.context.DefaultContext;
import simulaSAAB.comunicacion.Producto;
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
	
	
	public AmbienteLocal(){
		
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
	
	
	
	

}
