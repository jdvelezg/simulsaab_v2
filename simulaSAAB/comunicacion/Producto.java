package simulaSAAB.comunicacion;

import simulaSAAB.global.persistencia.ProductoConfigurado;

/**
 * Representa el concepto de <code>producto</code> usado en los registros del SISAAB y en la ontología para comunicación e interacción comercial de los agentes
 * 
 * @author jdvelezg
 *
 */
public class Producto implements Concepto {
	
	private int id;
	
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
		this.id					=p.getId();
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
		this.id					=p.getId();
		this.Nombre				=p.getNombre();
		this.TipoProducto		=p.getTipo();
		this.PrecioEnMercado	=p.getPrecioSIPSA();
		this.UnidadMedida		=p.getUnidadmedida();
		
		this.CostoProduccionPorHectarea = p.getCostoproduccionhectarea();
		this.PromRendimientoHectarea	= p.getRendimientohectarea();
		
	}
	/**
	 * Constructor
	 * @param p producto preconfigurado
	 */
	public Producto(ProductoConfigurado p){
		
		this.id					=p.getId();
		this.Nombre				=p.getNombre();
		this.TipoProducto		=p.getTipo();
		this.PrecioEnMercado	=p.getPrecioSIPSA();
		this.UnidadMedida		=p.getUnidadmedida();
		
		this.CostoProduccionPorHectarea = p.getCostoproduccionhectarea();
		this.PromRendimientoHectarea	= p.getRendimientohectarea();		
	}
	
	/*
	 * Un producto se considera igual a otro cuando tiene el mismo nombre y unidad de medida
	 */
	@Override
	public boolean equals(Object producto){
		
		if(producto instanceof Producto){
			
			Producto p = (Producto)producto;
			
			//if(p.getNombre().equalsIgnoreCase(this.Nombre) && p.getUnidadMedida().equalsIgnoreCase(this.UnidadMedida))
			if(p.getNombre().equalsIgnoreCase(Nombre))
				return true;
			else
				return false;
			
		}else{
			return false;
		}
		
	}
	
	/**
	 * Devuelve el nombre del producto	
	 * @return string
	 */
	public String getNombre() {
		return Nombre;
	}
	/**
	 * Asigna el nombre del producto
	 * @param nombre string nombre del producto
	 */
	public void setNombre(String nombre) {
		Nombre = nombre;
	}
	/**
	 * Devuelve el promedio de costo de producción por hectárea del producto
	 * @return double promedio de costo de producción
	 */
	public double getCostoProduccionPorHectarea() {
		return CostoProduccionPorHectarea;
	}
	/**
	 * Asigna el costo de producción por hectarea del producto
	 * @param costoProduccionPorHectarea promedio de costo de producción por hectárea del producto
	 */
	public void setCostoProduccionPorHectarea(double costoProduccionPorHectarea) {
		CostoProduccionPorHectarea = costoProduccionPorHectarea;
	}
	/**
	 * Devuelve el costo de producción unitario
	 * @return double costo de producción unitario
	 */
	public double getCostoProduccionPorUnidad() {
		return CostoProduccionPorUnidad;
	}
	/**
	 * Asigna el costo de producción unitario
	 * @param costoProduccionPorUnidad costo de producción unitario
	 */
	public void setCostoProduccionPorUnidad(double costoProduccionPorUnidad) {
		CostoProduccionPorUnidad = costoProduccionPorUnidad;
	}
	/**
	 * Devuelve la unidad de medida configurada para el producto
	 * @return string unidad de medida
	 */
	public String getUnidadMedida() {
		return UnidadMedida;
	}
	/**
	 * Asigna la unidad de medida del producto
	 * @param unidadMedida unidad de medida a configurar en el producto
	 */
	public void setUnidadMedida(String unidadMedida) {
		UnidadMedida = unidadMedida;
	}
	/**
	 * Devuelve el precio en mercado del producto
	 * @return double
	 */
	public double getPrecioEnMercado() {
		return PrecioEnMercado;
	}
	/**
	 * Asigna el precio en mercado del producto
	 * @param precioEnMercado
	 */
	public void setPrecioEnMercado(double precioEnMercado) {
		PrecioEnMercado = precioEnMercado;
	}
	/**
	 * Devuelve el promedio de rendimeinto por hectárea del producto
	 * @return double 
	 */
	public double getPromRendimientoHectarea() {
		return PromRendimientoHectarea;
	}
	/**
	 * Asigna el promedio de rendimiento por hectárea del producto
	 * @param promRendimientoHectarea int, promedio de rendimiento por hectárea
	 */
	public void setPromRendimientoHectarea(int promRendimientoHectarea) {
		PromRendimientoHectarea = promRendimientoHectarea;
	}
	
	/**
	 * Devuelve el identificador del producto
	 * @return
	 */
	public int getId(){
		return this.id;
	}

}
