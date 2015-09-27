package simulaSAAB.global;

import simulaSAAB.comunicacion.Proposito;

/**
 *  Facilita la creación consistente de propósitos en la simulación
 *  
 * @author jdvelezg
 *
 */
public class PropositosFactory {
	
	private Proposito Proposito;
	
	/**
	 * Constructor
	 */
	public PropositosFactory(){
		
	}
	
	/**
	 * Constructor
	 * @param rol string, rol del agente para el cual se crea el propósito
	 * @param Intencion string, intención del agente al que se le crea el propósito
	 */
	public PropositosFactory(String rol, String Intencion) {
		
		if(rol.compareToIgnoreCase("productor")==0 && Intencion.compareToIgnoreCase("producir")==0){
			
			Proposito = new Proposito("Producir cebolla y vender el producto");
			
		}
		else if(rol.compareToIgnoreCase("productor")==0 && Intencion.compareToIgnoreCase("vender")==0){
			
			Proposito = new Proposito("Vender productos");
			
		}else if(rol.compareToIgnoreCase("vendedor")==0 && Intencion.compareToIgnoreCase("vender")==0){
			
			Proposito = new Proposito("Comprar para vender productos");
			
		}else if(rol.compareToIgnoreCase("vendedor")==0 && Intencion.compareToIgnoreCase("comprar")==0){
			
			Proposito = new Proposito("Comprar para vender productos");
			
		}else if(rol.compareToIgnoreCase("intermediario")==0 && Intencion.compareToIgnoreCase("comprar")==0){
			
			Proposito = new Proposito("Comprar para vender productos");
			
		}else if(rol.compareToIgnoreCase("intermediario")==0 && Intencion.compareToIgnoreCase("vender")==0){
			
			Proposito = new Proposito("Comprar para vender productos");
		}
		else if(rol.compareToIgnoreCase("consumidor")==0){
			
			Proposito = new Proposito("Comprar productos");
		}
	}
	
	/**
	 * Devuelve el propósito consistente con el rol y a intención del agente
	 * 
	 * @param rol string, rol del agente para el cual se crea el propósito
	 * @param Intencion string, intención del agente al que se le crea el propósito
	 * @return Proposito 
	 */
	public Proposito generarProposito(String rol, String Intencion){
		
		return new PropositosFactory(rol,Intencion).getProposito();
	}
	/**
	 * Devuelve el propósito
	 * @return
	 */
	public Proposito getProposito(){
		
		return Proposito;
	}

}
