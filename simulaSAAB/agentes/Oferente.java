package simulaSAAB.agentes;

import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.comunicacion.Oferta;

/**
 * Representa al agente Oferente
 * <p>
 * Agente con capcidad de generar Ofertas de un producto
 * 
 * @author jdvelezg
 *
 */
public interface Oferente extends AgenteInteligente{
		
	
	/**
	 * Genera la oferta de un producto
	 */
	Oferta generarOferta();	
	
	/**
	 * Devuelve el dinero del Oferente
	 * @return Dinero
	 */ 
	Dinero getDinero();
	
	/**
	 * Devuelve el nombre del lugar donde surge la oferta
	 * @return string
	 */
	String LugarOferta();
		
}
