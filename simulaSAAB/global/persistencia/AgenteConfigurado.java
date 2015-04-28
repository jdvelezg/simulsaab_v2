package simulaSAAB.global.persistencia;



public class AgenteConfigurado {
	
	private int id;
	
	private String Tipo;
	
	private String EntornoInicial;
	
	private int cantidad;
	

	
	/**
	 * Constructor
	 * @param tipo
	 * @param cantidad
	 */
	public AgenteConfigurado(String tipo, int cantidad){
		
		this.Tipo	= tipo;
		this.cantidad = cantidad;
		
		switch(tipo){
		
		case "Productor":
			
			this.id=1;
			this.EntornoInicial="rural";
			
			break;
		case "Intermediario":
			
			this.id=2;
			this.EntornoInicial="mixto";
			
			break;
		case "VendedorFinal":
			
			this.id=3;
			this.EntornoInicial="urbano";
			
			break;
		case "NodoLogistico":
			
			this.id=4;
			this.EntornoInicial="mixto";
			
			break;
		case "OperadorLogistico":
			
			this.id=5;
			this.EntornoInicial="mixto";
			
			break;
		}
		
	}
	
	/**
	 * Constructor
	 * @param tipo
	 */
	public AgenteConfigurado(String tipo){
		
		this.Tipo	= tipo;		
		
		switch(tipo){
		
		case "Productor":
			
			this.id=1;
			this.EntornoInicial="rural";
			this.cantidad = 500;
			
			//this.Objetivo =new Objetivo(
			//		"Producir y comercializar productos agropecuarios generados a partir de los recursos disponibles, "+
			//		"procurando la obtencion de ganancias economicas mayores al 10% del costo de produccion del producto comercializado");
			
			break;
		case "Intermediario":
			
			this.id=2;
			this.EntornoInicial="mixto";
			this.cantidad = 2000;
			
			break;
		case "VendedorFinal":
			
			this.id=3;
			this.EntornoInicial="urbano";
			this.cantidad = 5000;
			
			break;
		case "NodoLogistico":
			
			this.id=4;
			this.EntornoInicial="mixto";
			this.cantidad = 5;
			
			break;
		case "OperadorLogistico":
			
			this.id=5;
			this.EntornoInicial="mixto";
			this.cantidad = 50;
			
			break;
		}
		
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return Tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		Tipo = tipo;
	}

	/**
	 * @return the entornoInicial
	 */
	public String getEntornoInicial() {
		return EntornoInicial;
	}

	/**
	 * @param entornoInicial the entornoInicial to set
	 */
	public void setEntornoInicial(String entornoInicial) {
		EntornoInicial = entornoInicial;
	}

	/**
	 * @return the cantidad
	 */
	public int getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	
	
	

}
