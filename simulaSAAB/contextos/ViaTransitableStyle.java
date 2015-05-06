package simulaSAAB.contextos;

import gov.nasa.worldwind.render.SurfacePolyline;
import gov.nasa.worldwind.render.SurfaceShape;

import java.awt.Color;

import repast.simphony.visualization.gis3D.style.SurfaceShapeStyle;

public class ViaTransitableStyle implements SurfaceShapeStyle<ViaTransitable> {

	@Override
	public SurfaceShape getSurfaceShape(ViaTransitable object, SurfaceShape shape) {
		// TODO Auto-generated method stub
		return new SurfacePolyline();
	}

	@Override
	public Color getFillColor(ViaTransitable obj) {
		// TODO Auto-generated method stub
		return Color.YELLOW;
	}

	@Override
	public double getFillOpacity(ViaTransitable obj) {
		// TODO Auto-generated method stub
		return 0.9;
	}

	@Override
	public Color getLineColor(ViaTransitable obj) {
		// TODO Auto-generated method stub
		return Color.YELLOW;
	}

	@Override
	public double getLineOpacity(ViaTransitable obj) {
		// TODO Auto-generated method stub
		return 0.9;
	}

	@Override
	public double getLineWidth(ViaTransitable obj) {
		// TODO Auto-generated method stub
		return 2.0;
	}

}
