package simulaSAAB.comunicacion;

public class Producto implements Concepto {
	
	private String Nombre;
	
	//private String TipoProducto;
	
	private double CostoProduccionPorHectarea;
	
	private double CostoProduccionPorUnidad;
	
	private String UnidadMedida;
	
	private double PrecioEnMercado;
	
	private int PromRendimientoHectarea;
	
	
	/**
	 * Constructor
	 */
	public Producto() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Constructor
	 * @param nombre Nombre del producto
	 */
	public Producto (String nombre){
		this.Nombre=nombre;
	}
	
	
	/**getters & setters**/
	
	
	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	public double getCostoProduccionPorHectarea() {
		return CostoProduccionPorHectarea;
	}

	public void setCostoProduccionPorHectarea(double costoProduccionPorHectarea) {
		CostoProduccionPorHectarea = costoProduccionPorHectarea;
	}

	public double getCostoProduccionPorUnidad() {
		return CostoProduccionPorUnidad;
	}

	public void setCostoProduccionPorUnidad(double costoProduccionPorUnidad) {
		CostoProduccionPorUnidad = costoProduccionPorUnidad;
	}

	public String getUnidadMedida() {
		return UnidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		UnidadMedida = unidadMedida;
	}

	public double getPrecioEnMercado() {
		return PrecioEnMercado;
	}

	public void setPrecioEnMercado(double precioEnMercado) {
		PrecioEnMercado = precioEnMercado;
	}

	public int getPromRendimientoHectarea() {
		return PromRendimientoHectarea;
	}

	public void setPromRendimientoHectarea(int promRendimientoHectarea) {
		PromRendimientoHectarea = promRendimientoHectarea;
	}
	
	

}
