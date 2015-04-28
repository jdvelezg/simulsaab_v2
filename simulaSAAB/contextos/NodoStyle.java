package simulaSAAB.contextos;

import gov.nasa.worldwind.render.SurfacePolygon;
import gov.nasa.worldwind.render.SurfaceShape;

import java.awt.Color;

import repast.simphony.visualization.gis3D.style.SurfaceShapeStyle;

public class NodoStyle implements SurfaceShapeStyle<NodoSaab> {
	
	@Override
	public SurfaceShape getSurfaceShape(NodoSaab object, SurfaceShape shape) {
		// TODO Auto-generated method stub
		return new SurfacePolygon();
	}

	@Override
	public Color getFillColor(NodoSaab obj) {
		// TODO Auto-generated method stub
		return Color.LIGHT_GRAY;
	}

	@Override
	public double getFillOpacity(NodoSaab obj) {
		// TODO Auto-generated method stub
		return 0.25;
	}

	@Override
	public Color getLineColor(NodoSaab obj) {
		// TODO Auto-generated method stub
		return Color.BLACK;
	}

	@Override
	public double getLineOpacity(NodoSaab obj) {
		// TODO Auto-generated method stub
		return 0.9;
	}

	@Override
	public double getLineWidth(NodoSaab obj) {
		// TODO Auto-generated method stub
		return 1;
	}

}
