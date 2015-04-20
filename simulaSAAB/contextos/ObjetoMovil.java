package simulaSAAB.contextos;

import com.vividsolutions.jts.geom.Geometry;

public interface ObjetoMovil {
	
	public Object getObject();
	
	public void setGeometria(Geometry geom);
	
	public Geometry getGeometria();
	
	

}
