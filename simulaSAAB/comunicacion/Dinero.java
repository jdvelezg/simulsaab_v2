package simulaSAAB.comunicacion;

/**
 * Representa el concepto de <code>dinero</code> usado en la ontología para comunicación e interacción entre los agentes
 * 
 * @author jdvelezg
 *
 */
public class Dinero implements Concepto {
	
	/**
	 * cantidad de dinero
	 */
	private double Cantidad;
	/**
	 * denominación monetaria del dinero
	 */
	private String Moneda;
	
	/**
	 * Constructor
	 */
	public Dinero() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Constructor de la clase
	 * @param monto double, cantidad de dinero que representa el objeto
	 * @param currency string, denominación monetaria
	 */
	public Dinero(double monto, String currency){
		this.Cantidad 	= monto;
		this.Moneda		= currency;
	}
	/**
	 * Constructor
	 * <p>
	 * Por defecto asigna la denominación monetaria a <code>COP</code>
	 * @param monto double, cantidad de dinero que representa el objeto
	 */
	public Dinero(double monto){
		this.Cantidad 	= monto;
		this.Moneda		= "COP";
	}
	/**
	 * Devuelve la cantidad de dinero representada
	 * @return double
	 */
	public double getCantidad() {
		return Cantidad;
	}
	/**
	 * Asigna una cantidad que representa el objeto <code>dinero</code>
	 * @param cantidad double, monto a ser asignado al objeto
	 */
	public void setCantidad(double cantidad) {
		Cantidad = cantidad;
	}
	/**
	 * Agrega una cantidad al monto de <code>dinero</code> representado
	 * @param cantidad double, monto a adicionar al objeto
	 */
	public void addCantidad(double cantidad) {
		Cantidad += cantidad;
	}
	/**
	 * Sustrae una cantidad al monto de <code>dinero</code> representado
	 * @param cantidad double, monto a sustraer 
	 */
	public void subtractCantidad(double cantidad) {
		Cantidad -= cantidad;
	}
	/**
	 * Devuelve la denominación monetaria representada por el objeto
	 * @return string
	 */
	public String getMoneda() {
		return Moneda;
	}
	/**
	 * Asigna una denominación monetaria al objeto
	 * @param moneda string, denominación monetaria
	 */
	public void setMoneda(String moneda) {
		Moneda = moneda;
	}
	
	@Override
	public boolean equals(Object arg0){		
		if(arg0 instanceof Dinero && arg0 != null){
			Dinero obj = (Dinero)arg0;
			return (obj.getMoneda().equalsIgnoreCase(this.Moneda) && obj.getCantidad()==this.Cantidad)?true:false;
		}else
			return false;
	}
	

}
