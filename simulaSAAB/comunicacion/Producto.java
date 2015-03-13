package simulaSAAB.comunicacion;

public class Producto implements Concepto {
	
	private String Nombre;
	
	private String TipoProducto;
	
	private double CostoProduccionPorHectarea;
	
	private double CostoProduccionPorUnidad;
	
	private String UnidadMedida;
	
	private double PrecioEnMercado;
	
	private int PromRendimientoHectarea;

	public Producto() {
		// TODO Auto-generated constructor stub
	}
	
	public Producto (String nombre){
		this.Nombre=nombre;
	}

}
