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
import simulaSAAB.agentes.Camion;
import simulaSAAB.agentes.Consumidor;

/**
 * Ajusta la visualización de {@link Consumidor} en la representación gráfica <code>GIS 3D</code> de la <code>proyección GIS</code> de <code>repast simphony</code>
 *
 */
public class ConsumerStyle implements MarkStyle<Consumidor> {

	public ConsumerStyle() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public WWTexture getTexture(Consumidor object, WWTexture texture) {
		// WWTexture is null on first call.
		
		Color color = Color.BLUE;	
					
		BufferedImage image = PatternFactory.createPattern(PatternFactory.PATTERN_CIRCLE, 
				new Dimension(10, 10), 0.7f,  color);
		return new BufferedImageTexture(image);	
	}

	@Override
	public PlaceMark getPlaceMark(Consumidor object, PlaceMark mark) {
		// PlaceMark is null on first call.
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
	public double getElevation(Consumidor obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getScale(Consumidor obj) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public double getHeading(Consumidor obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getLabel(Consumidor obj) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public Color getLabelColor(Consumidor obj) {
		// TODO Auto-generated method stub
		return Color.BLUE;
	}

	@Override
	public Font getLabelFont(Consumidor obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Offset getLabelOffset(Consumidor obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getLineWidth(Consumidor obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Material getLineMaterial(Consumidor obj, Material lineMaterial) {
		// TODO Auto-generated method stub
		return null;
	}
	


}
