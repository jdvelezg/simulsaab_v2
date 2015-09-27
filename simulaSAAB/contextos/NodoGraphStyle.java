package simulaSAAB.contextos;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.render.BasicWWTexture;
import gov.nasa.worldwind.render.WWTexture;

import java.net.URL;

import repast.simphony.visualization.gis3D.style.DefaultMarkStyle;
import simulaSAAB.agentes.Intermediario;
import simulaSAAB.global.VariablesGlobales;

/**
 * Ajusta la visualización de {@link NodoSaab} en la representación gráfica <code>GIS 3D</code> de la <code>proyección GIS</code> de <code>repast simphony</code>
 *
 */
public class NodoGraphStyle extends DefaultMarkStyle<NodoSaab> {

	public NodoGraphStyle() {
		super();
	}

	/**
	 * Here we set the appearance of the Agent using a non-changing icon.
	 */
	@Override
	public WWTexture getTexture(NodoSaab agent, WWTexture texture) {
			
		// If the texture is already defined, then just return the same texture since
		//  we don't want to update the tower agent appearance.  The only time the 
		//  below code will actually be used is on the initialization of the display
		//  when the icons are created.
		if (texture != null)
			return texture;
		
		// BasicWWTexture is useful when the texture is a non-changing image.
		URL localUrl = WorldWind.getDataFileStore().requestFile(VariablesGlobales.NODO_ICON);
		if (localUrl != null)	{
			return new BasicWWTexture(localUrl, false);
		}
		
		return null;
	}

}
