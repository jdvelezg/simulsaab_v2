package simulaSAAB.comunicacion;

import simulaSAAB.global.persistencia.ProductoConfigurado;

public class Producto implements Concepto {
	
	private String Nombre;
	
	private String TipoProducto;
	
	private double CostoProduccionPorHectarea;
	
	private double CostoProduccionPorUnidad;
	
	private String UnidadMedida;
	
	private double PrecioEnMercado;
	
	private double PromRendimientoHectarea;
	
	
	/**
	 * Constructor
	 */
	public Producto() {
		
		ProductoConfigurado p = new ProductoConfigurado("");
		
		this.Nombre				=p.getNombre();
		this.TipoProducto		=p.getTipo();
		this.PrecioEnMercado	=p.getPrecioSIPSA();
		this.UnidadMedida		=p.getUnidadmedida();
		
		this.CostoProduccionPorHectarea = p.getCostoproduccionhectarea();
		this.PromRendimientoHectarea	= p.getRendimientohectarea();
	}
	
	/**
	 * Constructor
	 * @param nombre Nombre del producto
	 */
	public Producto (String nombre){
		
		ProductoConfigurado p = new ProductoConfigurado(nombre);
		
		this.Nombre				=p.getNombre();
		this.TipoProducto		=p.getTipo();
		this.PrecioEnMercado	=p.getPrecioSIPSA();
		this.UnidadMedida		=p.getUnidadmedida();
		
		this.CostoProduccionPorHectarea = p.getCostoproduccionhectarea();
		this.PromRendimientoHectarea	= p.getRendimientohectarea();
		
	}
	
	public Producto(ProductoConfigurado p){
		
		this.Nombre				=p.getNombre();
		this.TipoProducto		=p.getTipo();
		this.PrecioEnMercado	=p.getPrecioSIPSA();
		this.UnidadMedida		=p.getUnidadmedida();
		
		this.CostoProduccionPorHectarea = p.getCostoproduccionhectarea();
		this.PromRendimientoHectarea	= p.getRendimientohectarea();		
	}
	
	/**
	 * Devuelve True si los objetos son iguales.
	 * Un producto se considera igual a otro cuando tiene el mismo nombre y unidad de medida.
	 * 
	 * @return boolean
	 */
	@Override
	public boolean equals(Object producto){
		
		if(producto.getClass().getName().equalsIgnoreCase("Producto")){
			
			Producto p = (Producto)producto;
			
			if(p.getNombre().equalsIgnoreCase(this.Nombre) && p.getUnidadMedida().equalsIgnoreCase(this.UnidadMedida))
				return true;
			else
				return false;
			
		}else{
			return false;
		}
		
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

	public double getPromRendimientoHectarea() {
		return PromRendimientoHectarea;
	}

	public void setPromRendimientoHectarea(int promRendimientoHectarea) {
		PromRendimientoHectarea = promRendimientoHectarea;
	}
	
	

}
