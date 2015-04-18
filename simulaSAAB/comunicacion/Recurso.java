package simulaSAAB.comunicacion;

public class Recurso implements Concepto {
	
	private Producto Producto;
	
	private Double Cantidad;
	
	
	/**
	 * Constructor
	 */
	public Recurso() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Constructor
	 * @param prod Producto que conforma el recurso
	 * @param cant unidades del producto
	 */
	public Recurso(Producto prod, double cant){
		
		Producto = prod;
		Cantidad = new Double(cant);		
	}
	
	/**
	 * Resta una cantidad determianda a las unidades que conforman el recurso
	 * @param cant cantidad a reducir
	 * @return double nueva cantidad que compone el recurso
	 */
	public Double removeCantidad(double cant){
		
		Cantidad -= cant;
		
		return Cantidad;
	}
	
	public Double addCantidad(double cant){
		
		Cantidad += cant;
		
		return Cantidad;
	}
	
	/**getters & setters**/
	
	public Producto getProducto() {
		return Producto;
	}

	public void setProducto(Producto producto) {
		Producto = producto;
	}

	public Double getCantidad() {
		return Cantidad;
	}

	public void setCantidad(Double cantidad) {
		Cantidad = cantidad;
	}

}
