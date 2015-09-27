package simulaSAAB.contextos;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Offset;
import gov.nasa.worldwind.render.PatternFactory;
import gov.nasa.worldwind.render.WWTexture;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;

import repast.simphony.visualization.gis3D.BufferedImageTexture;
import repast.simphony.visualization.gis3D.PlaceMark;
import repast.simphony.visualization.gis3D.style.MarkStyle;
import simulaSAAB.agentes.Tienda;
import simulaSAAB.agentes.VendedorFinal;
/**
 * Ajusta la visualización de {@link VendedorFinal} en la representación gráfica <code>GIS 3D</code> de la <code>proyección GIS</code> de <code>repast simphony</code>
 * 
 *
 */
public class VendedorFinalStyle implements MarkStyle<VendedorFinal> {

	public VendedorFinalStyle() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public WWTexture getTexture(VendedorFinal object, WWTexture texture) {

		Color color = Color.DARK_GRAY;	
					
		BufferedImage image = PatternFactory.createPattern(PatternFactory.PATTERN_CIRCLE, 
				new Dimension(10, 10), 0.7f,  color);
		return new BufferedImageTexture(image);
	}

	@Override
	public PlaceMark getPlaceMark(VendedorFinal object, PlaceMark mark) {
		if (mark == null)
			mark = new PlaceMark();
					
				/**
				 * The Altitude mode determines how the mark appears using the elevation.
				 *   WorldWind.ABSOLUTE places the mark at elevation relative to sea level
				 *   WorldWind.RELATIVE_TO_GROUND places the mark at elevation relative to ground elevation
				 *   WorldWind.CLAMP_TO_GROUND places the mark at ground elevation
				 */
		mark.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
		mark.setLineEnabled(false);
				
		return mark;
	}

	@Override
	public double getElevation(VendedorFinal obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getScale(VendedorFinal obj) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public double getHeading(VendedorFinal obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getLabel(VendedorFinal obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getLabelColor(VendedorFinal obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Font getLabelFont(VendedorFinal obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Offset getLabelOffset(VendedorFinal obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getLineWidth(VendedorFinal obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Material getLineMaterial(VendedorFinal obj, Material lineMaterial) {
		// TODO Auto-generated method stub
		return null;
	}

}
