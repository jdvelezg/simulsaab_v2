package simulaSAAB.global.persistencia;

import java.util.ArrayList;
import java.util.List;

import simulaSAAB.tareas.SistemaActividadHumana;
import simulaSAAB.tareas.ProducirCebollaBulbo;

/**
 * Persiste las regiones que fueron configuradas en tiempo de ejecución de la simulación
 * @author lfgomezm
 *
 */
public class RegionConfigurada {
	/**
	 * Identificador de la región configurada
	 */
	private int id;
	/**
	 * Nombre de la región configurada
	 */
	private String nombre;
	/**
	 * Tipo de entorno en el que se configura la región
	 */
	private String TipoEntorno;
	/**
	 * Anillo del <code>SAAB</code> donde se configura la región
	 */
	private int AnilloSAAB;
	/**
	 * Coordenadas de ubicación de la región configurada
	 */
	private String Coordenadas;
	/**
	 * Nombre del clima en el que produce el producto
	 */
	private String Clima;
	/**
	 * Nombre del piso térmico en el que se produce el producto
	 */
	private String PisoTermico;
	/**
	 * Listado de actividades humanas ejecutables
	 */
	private List<SistemaActividadHumana> ActividadesEjecutables;
	
	/**
	 * Constructor
	 * @param nombre <code>string</code>, nombre de la región configurada
	 */
	public RegionConfigurada(String nombre){
		
		this.nombre		=nombre;
		
		switch(nombre){
		
		case "Caqueza":
			
			this.id				=1;
			this.TipoEntorno	="rural";
			this.AnilloSAAB		=2;
			this.Coordenadas	="";
			this.Clima			="frio";
			this.PisoTermico	="2400";
			
			this.ActividadesEjecutables = new ArrayList();
			this.ActividadesEjecutables.add(new ProducirCebollaBulbo());
			
			break;
		case "Chipaque":
			
			this.id				=2;
			this.TipoEntorno	="rural";
			this.AnilloSAAB		=2;
			this.Coordenadas	="";
			this.Clima			="frio";
			this.PisoTermico	="2400";
			
			this.ActividadesEjecutables = new ArrayList();
			this.ActividadesEjecutables.add(new ProducirCebollaBulbo());
			
			break;
		case "Choachi":
			
			this.id				=3;
			this.TipoEntorno	="rural";
			this.AnilloSAAB		=2;
			this.Coordenadas	="";
			this.Clima			="frio";
			this.PisoTermico	="2400";
			
			this.ActividadesEjecutables = new ArrayList();
			this.ActividadesEjecutables.add(new ProducirCebollaBulbo());
			
			break;			
		case "Fomeque":
			
			this.id				=4;
			this.TipoEntorno	="rural";
			this.AnilloSAAB		=2;
			this.Coordenadas	="";
			this.Clima			="frio";
			this.PisoTermico	="2400";
			
			this.ActividadesEjecutables = new ArrayList();
			this.ActividadesEjecutables.add(new ProducirCebollaBulbo());
			
			break;
		case "Fusagasuga":
			
			this.id				=5;
			this.TipoEntorno	="rural";
			this.AnilloSAAB		=2;
			this.Coordenadas	="";
			this.Clima			="frio";
			this.PisoTermico	="2400";
			
			this.ActividadesEjecutables = new ArrayList();
			this.ActividadesEjecutables.add(new ProducirCebollaBulbo());
			
			break;
		case "Pasca":
			
			this.id				=6;
			this.TipoEntorno	="rural";
			this.AnilloSAAB		=2;
			this.Coordenadas	="";
			this.Clima			="frio";
			this.PisoTermico	="2400";
			
			this.ActividadesEjecutables = new ArrayList();
			this.ActividadesEjecutables.add(new ProducirCebollaBulbo());
		
			break;
		case "Ubaque":
			
			this.id				=7;
			this.TipoEntorno	="rural";
			this.AnilloSAAB		=2;
			this.Coordenadas	="";
			this.Clima			="frio";
			this.PisoTermico	="2400";
			
			this.ActividadesEjecutables = new ArrayList();
			this.ActividadesEjecutables.add(new ProducirCebollaBulbo());
			
			break;
		case "Une":
			
			this.id				=8;
			this.TipoEntorno	="rural";
			this.AnilloSAAB		=2;
			this.Coordenadas	="";
			this.Clima			="frio";
			this.PisoTermico	="2400";
			
			this.ActividadesEjecutables = new ArrayList();
			this.ActividadesEjecutables.add(new ProducirCebollaBulbo());
			
			break;
		case "Mosquera":
			
			this.id				=9;
			this.TipoEntorno	="rural";
			this.AnilloSAAB		=1;
			this.Coordenadas	="";
			this.Clima			="frio";
			this.PisoTermico	="2400";
			
			this.ActividadesEjecutables = new ArrayList();			
			this.ActividadesEjecutables.add(new ProducirCebollaBulbo());
			
			break;
		case "La Calera":
			
			this.id				=10;
			this.TipoEntorno	="rural";
			this.AnilloSAAB		=1;
			this.Coordenadas	="";
			this.Clima			="frio";
			this.PisoTermico	="2400";
			
			this.ActividadesEjecutables = new ArrayList();
			this.ActividadesEjecutables.add(new ProducirCebollaBulbo());
			
			break;
		case "Bogota":
			
			this.id				=11;
			this.TipoEntorno	="Urbano";
			this.AnilloSAAB		=1;
			this.Coordenadas	="";
			this.Clima			="frio";
			this.PisoTermico	="2600";
			
			this.ActividadesEjecutables = new ArrayList();

			
			break;
		}
		
	}
	
	
	/**
	 * Devuelve el identificador de la región 
	 * @return id <code>int</code>, identificador numérico de la región configurada
	 */
	public int getId() {
		return id;
	}

	/**
	 * Asigna el identificador de la región 
	 * @param id <code>int</code>, identificador numérico de la región configurada
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Devuelve el nombre de la región configurada
	 * @return nombre <code>string</code>, nombre de la región 
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Asigna el nombre de la región configurada
	 * @param nombre <code>string</code>, nombre de la región 
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Asigna el tipo de entorno en el que se configura la región 
	 * @return tipoEntorno  <code>string</code>, nombre del entorno en el que se ubica la región 
	 */
	public String getTipoEntorno() {
		return TipoEntorno;
	}

	/**
	 * Asigna el tipo de entorno en el que se configura la región 
	 * @param tipoEntorno  <code>string</code>, nombre del entorno en el que se ubica la región 
	 */
	public void setTipoEntorno(String tipoEntorno) {
		TipoEntorno = tipoEntorno;
	}

	/**
	 * Devuelve el número del anillo en el que se encuentra la región 
	 * @return anilloSAAB <code>int</code>, anillo del <code>SAAB</code> en el que se encuentra la región configurada
	 */
	public int getAnilloSAAB() {
		return AnilloSAAB;
	}

	/**
	 * Asigna el número del anillo en el que se encuentra la región 
	 * @param anilloSAAB <code>int</code>, anillo del <code>SAAB</code> en el que se encuentra la región configurada
	 */
	public void setAnilloSAAB(int anilloSAAB) {
		AnilloSAAB = anilloSAAB;
	}

	/**
	 * Devuelve las coordenadas de la región 
	 * @return coordenadas <code>string</code>, coordenadas de ubicación de la región 
	 */
	public String getCoordenadas() {
		return Coordenadas;
	}

	/**
	 * Asigna las coordenadas de la región 
	 * @param coordenadas <code>string</code>, coordenadas de ubicación de la región 
	 */
	public void setCoordenadas(String coordenadas) {
		Coordenadas = coordenadas;
	}

	/**
	 * Devuelve el nombre del clima en el que puede ser producido el producto seleccionado
	 * @return clima <code>string</code>, nombre del clima
	 */
	public String getClima() {
		return Clima;
	}

	/**
	 * Asigna el nombre del clima en el que puede ser producido el producto seleccionado
	 * @param clima <code>string</code>, nombre del clima
	 */
	public void setClima(String clima) {
		Clima = clima;
	}

	/**
	 * Devuelve el nombre del piso térmico en el que se produce el producto 
	 * @return pisoTermico <code>string</code>, nombre del piso térmico
	 */
	public String getPisoTermico() {
		return PisoTermico;
	}

	/**
	 * Asigna el nombre del piso térmico en el que se produce el producto 
	 * @param pisoTermico <code>string</code>, nombre del piso térmico
	 */
	public void setPisoTermico(String pisoTermico) {
		PisoTermico = pisoTermico;
	}


	/**
	 * Devuelve el listado de actividades ejecutables
	 * @return actividadesEjecutables   listado de las actividades 
	 */
	public List<SistemaActividadHumana> getActividadesEjecutables() {
		return ActividadesEjecutables;
	}
	

}