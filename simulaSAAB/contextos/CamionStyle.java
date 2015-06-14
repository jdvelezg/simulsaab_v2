package simulaSAAB.contextos;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.render.BasicWWTexture;
import gov.nasa.worldwind.render.WWTexture;

import java.net.URL;

import repast.simphony.visualization.gis3D.style.DefaultMarkStyle;
import simulaSAAB.agentes.Camion;

public class CamionStyle extends DefaultMarkStyle<Camion> {
	
	/**
	 * Here we set the appearance of the TowerAgent using a non-changing icon.
	 */
	@Override
	public WWTexture getTexture(Camion agent, WWTexture texture) {
			
		// If the texture is already defined, then just return the same texture since
		//  we don't want to update the tower agent appearance.  The only time the 
		//  below code will actually be used is on the initialization of the display
		//  when the icons are created.
		if (texture != null)
			return texture;
		
		// BasicWWTexture is useful when the texture is a non-changing image.
		URL localUrl = WorldWind.getDataFileStore().requestFile("icons/camion.png");
		if (localUrl != null)	{
			return new BasicWWTexture(localUrl, false);
		}
		
		return null;
	}

}
