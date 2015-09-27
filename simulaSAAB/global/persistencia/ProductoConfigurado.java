package simulaSAAB.global.persistencia;

import simulaSAAB.comunicacion.Producto;
/**
 * Persiste los datos de los productos configurados durante la ejecución de la simulación
 * @author lfgomezm
 *
 */
public class ProductoConfigurado{
	/**
	 * Identificador del producto
	 */
	private Integer id;
	/**
	 * Nombre del producto
	 */
	private String nombre;
	/**
	 * Tipo de producto configurado
	 */
	private String tipo;	
	/**
	 * Minima altura requerida para la producción del produto
	 */
	private String MinPisoTermico;
	/**
	 * Máxima altura requerida para la producción del produto
	 */
	private String MaxPisoTermico;
	/**
	 * Costo de producción de una héctarea del producto
	 */
	private double costoproduccionhectarea;
	/**
	 * Cantidad de producto obtenida por héctarea cosechada en la unidad de medida <code>unidadmedida</code> fijada 
	 */
	private double rendimientohectarea;
	/**
	 * Unidad de medida fijada para determinar el rendimiento del producto
	 */
	private String unidadmedida;
	/**
	 * Utilidad registrada por la comercialización del producto
	 */
	private double UtilidadRegistrada;
	/**
	 * Precio del producto según el Sistema de Información de Precios del Sector Agropecuario (SIPSA)
	 */
	private double precioSIPSA;
	
	
	/**
	 * Constructor
	 * @param nombre  <code>string</code>, nombre del producto
	 */
	public ProductoConfigurado(String nombre){
	//(Fuente: EVALUACIONES AGROPECUARIAS 2010 - DEPARTAMENTO DE CUNDINAMARCA)
		this.id							=1;
		this.nombre						="Cebolla de Bulbo";
		this.tipo						="AgrÃ­cola";
		this.MinPisoTermico				="2000";
		this.MaxPisoTermico				="2800";
		this.costoproduccionhectarea	=9139200.0;//$/h.
		this.rendimientohectarea		=15000.0;//15 t./h. 
		this.unidadmedida				="kilogramo";
		this.UtilidadRegistrada			=6887550;//-$/h.
		this.precioSIPSA				=922.22;//-$/Kg(FUENTE: SIPSA)
		//preciopagadoproductor			=1068450;//-$/t.
		
	}

	

	/**
	 * Devuelve el id del producto
	 * @return id  <code>int</code>, identificador del producto 
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Asigna el id al producto
	 * @param id <code>int</code>, identificador del producto
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Devuelve el nombre del producto configurado
	 * @return nombre  <code>string</code>, nombre del producto configurado
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Asigna el nombre al producto configurado
	 * @param nombre <code>string</code>, nombre del producto configurado 
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * Devuelve el tipo de producto configurado 
	 * @return tipo <code>string</code>, nombre del tipo de producto
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * Asigna el tipo de producto 
	 * @param tipo <code>string</code>, nombre del tipo de producto
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	/**
	 * Devuelve el costo de de producir una héctarea del tipo de producto configurado
	 * @return costoproduccionhectarea <code>double</code>, número que representa el costo de producción
	 */
	public double getCostoproduccionhectarea() {
		return costoproduccionhectarea;
	}

	/**
	 * Asigna  el costo de de producir una héctarea del tipo de producto configurado
	 * @param costoproduccionhectarea <code>double</code>, número que representa el costo de producción
	 */
	public void setCostoproduccionhectarea(double costoproduccionhectarea) {
		this.costoproduccionhectarea = costoproduccionhectarea;
	}

	/**
	 * Devuelve el rendimiento por héctarea para el producto configurado
	 * @return rendimientohectarea <code>double</code>, cantidad de producto producido por héctarea cosechada
	 */
	public double getRendimientohectarea() {
		return rendimientohectarea;
	}

	/**
	 * Asigna el rendimiento por héctarea para el producto configurado
	 * @param rendimientohectarea rendimientohectarea <code>double</code>, cantidad de producto producido por héctarea cosechada
	 */
	public void setRendimientohectarea(double rendimientohectarea) {
		this.rendimientohectarea = rendimientohectarea;
	}

	/**
	 * Devuelve la unidad de medida definida para contabilizar el producto configurado
	 * @return unidadmedida <code>string</code>, nombre de la unidad de medida a utilizar
	 */
	public String getUnidadmedida() {
		return unidadmedida;
	}

	/**
	 * Asigna la unidad de medida definida para contabilizar el producto configurado
	 * @param unidadmedida <code>string</code>, nombre de la unidad de medida a utilizar
	 */
	public void setUnidadmedida(String unidadmedida) {
		this.unidadmedida = unidadmedida;
	}

	/**
	 * Devuelve el precio del producto definido por el SIPSA
	 * @return precioSIPSA <code>double</code>, precio por unidad de medida determinado para el producto configurado
	 */
	public double getPrecioSIPSA() {
		return precioSIPSA;
	}

	/**
	 * Asigna el precio del producto definido por el SIPSA
	 * @param precioSIPSA <code>double</code>, precio por unidad de medida determinado para el producto configurado
	 */
	public void setPrecioSIPSA(double precioSIPSA) {
		this.precioSIPSA = precioSIPSA;
	}
	
	/**
	 * Devuelve el nombre del menor piso térmico en el que se produce el producto configurado 
	 * @return minPisoTermico <code>string</code>, nombre del piso térmico mínimo
	 */
	public String getMinPisoTermico() {
		return MinPisoTermico;
	}

	/**
	 * Asigna el nombre del menor piso térmico en el que se produce el producto configurado
	 * @param minPisoTermico <code>string</code>, nombre del piso térmico mínimo
	 */
	public void setMinPisoTermico(String minPisoTermico) {
		MinPisoTermico = minPisoTermico;
	}

	/**
	 * Devuelve el nombre del mayor piso térmico en el que se produce el producto configurado
	 * @return maxPisoTermico <code>string</code>, nombre del piso térmico máximo
	 */
	public String getMaxPisoTermico() {
		return MaxPisoTermico;
	}

	/**
	 * Asigna el nombre del mayor piso térmico en el que se produce el producto configurado
	 * @param maxPisoTermico <code>string</code>, nombre del piso térmico máximo
	 */
	public void setMaxPisoTermico(String maxPisoTermico) {
		MaxPisoTermico = maxPisoTermico;
	}

	/**
	 * Devuelve el valor de la utilidad registrada tras la venta del producto configurado
	 * @return utilidadRegistrada <code>double</code>, valor de la utilidad
	 */
	public double getUtilidadRegistrada() {
		return UtilidadRegistrada;
	}

	/**
	 * Asigna  el valor de la utilidad registrada tras la venta del producto configurado
	 * @param utilidadRegistrada  <code>double</code>, valor de la utilidad
	 */
	public void setUtilidadRegistrada(double utilidadRegistrada) {
		UtilidadRegistrada = utilidadRegistrada;
	}

}