package simulaSAAB.agentes;

import simulaSAAB.comunicacion.Oferta;

public interface Oferente extends AgenteInteligente{
	
	/**
	 * Genera la oferta de un producto
	 */
	Oferta generarOferta();

}
