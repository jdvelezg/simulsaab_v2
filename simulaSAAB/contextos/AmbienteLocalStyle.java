package simulaSAAB.contextos;

import gov.nasa.worldwind.render.SurfacePolygon;
import gov.nasa.worldwind.render.SurfaceShape;

import java.awt.Color;

import repast.simphony.visualization.gis3D.style.SurfaceShapeStyle;

public class AmbienteLocalStyle implements SurfaceShapeStyle<AmbienteLocal> {

	public AmbienteLocalStyle() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public SurfaceShape getSurfaceShape(AmbienteLocal object, SurfaceShape shape) {
		// TODO Auto-generated method stub
		return new SurfacePolygon();
	}

	@Override
	public Color getFillColor(AmbienteLocal obj) {
		// TODO Auto-generated method stub
		return Color.green;
	}

	@Override
	public double getFillOpacity(AmbienteLocal obj) {
		// TODO Auto-generated method stub
		return 0.25;
	}

	@Override
	public Color getLineColor(AmbienteLocal obj) {
		// TODO Auto-generated method stub
		return Color.BLACK;
	}

	@Override
	public double getLineOpacity(AmbienteLocal obj) {
		// TODO Auto-generated method stub
		return 0.9;
	}

	@Override
	public double getLineWidth(AmbienteLocal obj) {
		// TODO Auto-generated method stub
		return 1;
	}

}
