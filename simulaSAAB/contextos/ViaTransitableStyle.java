package simulaSAAB.contextos;

import gov.nasa.worldwind.render.SurfacePolyline;
import gov.nasa.worldwind.render.SurfaceShape;

import java.awt.Color;

import repast.simphony.visualization.gis3D.style.SurfaceShapeStyle;
import simulaSAAB.agentes.VendedorFinal;

/**
 * Ajusta la visualización de {@link ViaTransitable} en la representación gráfica <code>GIS 3D</code> de la <code>proyección GIS</code> de <code>repast simphony</code>
 *
 */
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
