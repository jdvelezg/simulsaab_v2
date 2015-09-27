package simulaSAAB.comunicacion;

/**
 * Representa el concepto de <code>recurso</code> usado en la ontología para comunicación e interacción comercial de los agentes
 * 
 * @author jdvelezg
 *
 */
public class Recurso implements Concepto {
	
	private Producto Producto;
	
	private Double Cantidad;
	
	private Double costoUnitario;
	
	
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
		Cantidad = cant;		
	}
	
	/**
	 * Resta una cantidad determinada a las unidades que conforman el recurso
	 * @param cant cantidad a reducir
	 * @return double nueva cantidad que compone el recurso
	 */
	public Double removeCantidad(double cant){
		
		Cantidad = Cantidad-cant<0?0:Cantidad-cant;
		
		return Cantidad;
	}
	/**
	 * Agrega una cantidad determinada a las unidades que conforman el recurso
	 * @param cant cantidad a adicionar
	 * @return double nueva cantidad que compone el recurso
	 */
	public Double addCantidad(double cant){
		
		Cantidad += cant;
		
		return Cantidad;
	}
	
	/**
	 * Devuelve el producto	
	 * @return Producto
	 */
	public Producto getProducto() {
		return Producto;
	}
	/**
	 * Asigna el producto
	 * @param producto producto que compone el recurso
	 */
	public void setProducto(Producto producto) {
		Producto = producto;
	}
	/**
	 * Devuelve la cantidad del recurso
	 * @return double
	 */
	public Double getCantidad() {
		return Cantidad>0?Cantidad:0;
	}
	/**
	 * Asigna la cantidad
	 * @param cantidad double cantidad del recurso en unidades de medida del producto asociado
	 */
	public void setCantidad(Double cantidad) {
		Cantidad = cantidad;
	}
	/**
	 * Devuelve el costo unitario
	 * @return double
	 */
	public Double getCostoUnitario() {
		return costoUnitario;
	}
	/**
	 * Asigna el costo unitario
	 * @param costoUnitario costo unitario del producto que conforma el recurso
	 */
	public void setCostoUnitario(Double costoUnitario) {
		this.costoUnitario = costoUnitario;
	}
	
	

}
