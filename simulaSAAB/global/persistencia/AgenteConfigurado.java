package simulaSAAB.global.persistencia;


/**
 * Persiste los datos de los agentes configurados, en tiempo de ejecución  
 * @author lfgomezm
 *
 */
public class AgenteConfigurado {
	/**
	 * Identificador del agente
	 */
	private int id;
	/**
	 * Clase de agente a configurar
	 */
	private String Tipo;
	/**
	 * Tipo de entorno en el que es creado el agente
	 */
	private String EntornoInicial;
	/**
	 * Número de agentes a agregar con las mismas caracteristicas dentro de la simulación
	 */
	private int cantidad;
	

	
	/**
	 * Constructor
	 * @param tipo string, tipo de agente
	 * @param cantidad int, número de agentes a configurar
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
	 * @param tipo string, tipo de agente a crear
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
	 * Devuelve el id de los agentes configurados
	 * @return the id int, identificador de los agentes
	 */
	public int getId() {
		return id;
	}

	/**
	 * Asigna el id de los agentes configurados
	 * @param id int, identificador a asignar
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Devuelve el tipo de agente
	 * @return tipo string, clase de agente
	 */
	public String getTipo() {
		return Tipo;
	}

	/**
	 * Asigna el tipo de agente 
	 * @param tipo string, tipo de agente
	 */
	public void setTipo(String tipo) {
		Tipo = tipo;
	}

	/**
	 * Devuelve el entorno de inicio del agente
	 * @return entornoInicial <code>string</code>,tipo de entorno en el que se crea el agente
	 */
	public String getEntornoInicial() {
		return EntornoInicial;
	}

	/**
	 * Asigna el entorno inicial del agente
	 * @param entornoInicial <code>string</code>,tipo de entorno en el que se crea el agente
	 */
	public void setEntornoInicial(String entornoInicial) {
		EntornoInicial = entornoInicial;
	}

	/**
	 * Devuelve el número de agentes a configurar
	 * @return cantidad int, número de agentes
	 */
	public int getCantidad() {
		return cantidad;
	}

	/**
	 * Asigna el número de agentes a cofigurar
	 * @param cantidad <code>int</code>, número de agentes
	 */
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	
	
	

}