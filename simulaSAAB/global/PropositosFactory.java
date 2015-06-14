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
		
		if(rol.compareToIgnoreCase("productor")==0 && Intencion.compareToIgnoreCase("producir")==0){
			
			Proposito = new Proposito("Producir cebolla y vender el producto");
			
		}
		else if(rol.compareToIgnoreCase("productor")==0 && Intencion.compareToIgnoreCase("vender")==0){
			
			Proposito = new Proposito("Vender productos");
			
		}else if(rol.compareToIgnoreCase("vendedor")==0 && Intencion.compareToIgnoreCase("vender")==0){
			
			Proposito = new Proposito("Comprar para vender productos");
			
		}else if(rol.compareToIgnoreCase("vendedor")==0 && Intencion.compareToIgnoreCase("comprar")==0){
			
			Proposito = new Proposito("Comprar para vender productos");
		}
		else if(rol.compareToIgnoreCase("consumidor")==0){
			
			Proposito = new Proposito("Comprar productos");
		}
	}
	
	public Proposito generarProposito(String rol, String Intencion){
		
		return new PropositosFactory(rol,Intencion).getProposito();
	}
	
	public Proposito getProposito(){
		
		return Proposito;
	}

}
