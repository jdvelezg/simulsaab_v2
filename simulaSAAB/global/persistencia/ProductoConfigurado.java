package simulaSAAB.global.persistencia;

import simulaSAAB.comunicacion.Producto;

public class ProductoConfigurado{
	
	private Integer id;
	
	private String nombre;
	
	private String tipo;	

	private String MinPisoTermico;
	
	private String MaxPisoTermico;
	
	private double costoproduccionhectarea;
	
	private double rendimientohectarea;
	
	private String unidadmedida;
	
	private double UtilidadRegistrada;
	
	private double precioSIPSA;
	
	
	/**
	 * Constructor
	 */
	public ProductoConfigurado(String nombre){
		
		this.id							=1;
		this.nombre						="Cebolla de Bulbo";
		this.tipo						="Agrícola";
		this.MinPisoTermico				="2000";
		this.MaxPisoTermico				="2800";
		this.costoproduccionhectarea	=10280013.00;
		this.rendimientohectarea		=16500.00;
		this.unidadmedida				="kilogramo";
		this.UtilidadRegistrada			=500;
		this.precioSIPSA				=1400;
		
	}

	

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	/**
	 * @return the costoproduccionhectarea
	 */
	public double getCostoproduccionhectarea() {
		return costoproduccionhectarea;
	}

	/**
	 * @param costoproduccionhectarea the costoproduccionhectarea to set
	 */
	public void setCostoproduccionhectarea(double costoproduccionhectarea) {
		this.costoproduccionhectarea = costoproduccionhectarea;
	}

	/**
	 * @return the rendimientohectarea
	 */
	public double getRendimientohectarea() {
		return rendimientohectarea;
	}

	/**
	 * @param rendimientohectarea the rendimientohectarea to set
	 */
	public void setRendimientohectarea(double rendimientohectarea) {
		this.rendimientohectarea = rendimientohectarea;
	}

	/**
	 * @return the unidadmedida
	 */
	public String getUnidadmedida() {
		return unidadmedida;
	}

	/**
	 * @param unidadmedida the unidadmedida to set
	 */
	public void setUnidadmedida(String unidadmedida) {
		this.unidadmedida = unidadmedida;
	}

	/**
	 * @return the precioSIPSA
	 */
	public double getPrecioSIPSA() {
		return precioSIPSA;
	}

	/**
	 * @param precioSIPSA the precioSIPSA to set
	 */
	public void setPrecioSIPSA(double precioSIPSA) {
		this.precioSIPSA = precioSIPSA;
	}
	
	/**
	 * @return the minPisoTermico
	 */
	public String getMinPisoTermico() {
		return MinPisoTermico;
	}

	/**
	 * @param minPisoTermico the minPisoTermico to set
	 */
	public void setMinPisoTermico(String minPisoTermico) {
		MinPisoTermico = minPisoTermico;
	}

	/**
	 * @return the maxPisoTermico
	 */
	public String getMaxPisoTermico() {
		return MaxPisoTermico;
	}

	/**
	 * @param maxPisoTermico the maxPisoTermico to set
	 */
	public void setMaxPisoTermico(String maxPisoTermico) {
		MaxPisoTermico = maxPisoTermico;
	}

	/**
	 * @return the utilidadRegistrada
	 */
	public double getUtilidadRegistrada() {
		return UtilidadRegistrada;
	}

	/**
	 * @param utilidadRegistrada the utilidadRegistrada to set
	 */
	public void setUtilidadRegistrada(double utilidadRegistrada) {
		UtilidadRegistrada = utilidadRegistrada;
	}

}
