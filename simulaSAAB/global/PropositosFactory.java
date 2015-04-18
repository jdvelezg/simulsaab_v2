package simulaSAAB.global;

import simulaSAAB.comunicacion.Proposito;

public class PropositosFactory {
	
	private Proposito Proposito;
	
	/**
	 * Constructor
	 */
	public PropositosFactory(){
		
	}
	
	/**
	 * Constructor
	 * @param rol
	 * @param Intencion
	 */
	public PropositosFactory(String rol, String Intencion) {
		
		if(rol.compareToIgnoreCase("productor")==0){
			
			Proposito = new Proposito("Producir cebolla y vender el producto");
			
		}else if(rol.compareToIgnoreCase("vendedor")==0){
			
			Proposito = new Proposito("Comprar cebolla y venderla al detal");
			
		}else if(rol.compareToIgnoreCase("consumidor")==0){
			
			Proposito = new Proposito("Comprar cebolla en la tienda mas cercana");
		}
	}
	
	public Proposito generarProposito(String rol, String Intencion){
		
		return new PropositosFactory(rol,Intencion).getProposito();
	}
	
	public Proposito getProposito(){
		
		return Proposito;
	}

}
