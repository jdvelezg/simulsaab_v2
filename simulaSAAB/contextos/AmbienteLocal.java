/**
 * 
 */
package simulaSAAB.contextos;

import java.util.List;

import repast.simphony.context.DefaultContext;

/**
 * @author dampher
 *
 */
public class AmbienteLocal extends DefaultContext<Object> implements GeografiaFija {
	
	private String Nombre;
	
	private String Clima;
	
	private String PisoTermico;
	
	private List<Object> ProductosAgricolasViables;
	
	private List<Object> ActividadesViables;
	
	private List<Object> NodosSaab;
	

}
