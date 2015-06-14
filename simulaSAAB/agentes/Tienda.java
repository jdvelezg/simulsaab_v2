package simulaSAAB.agentes;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Recurso;
import simulaSAAB.contextos.AmbienteLocal;
import simulaSAAB.contextos.GeografiaFija;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

public class Tienda implements GeografiaFija{
	
	
	private Geometry Geometria;
	
	private AmbienteLocal Ambiente;
	
	private Map<Producto, Recurso> Productos;
	
	private VendedorFinal propietario;
	
	
	/**
	 * Constructor
	 * @param nombre
	 */
	public Tienda(Geometry geom) {	
		
		this.Geometria 	= geom;
		this.Productos	= new ConcurrentHashMap<Producto, Recurso>();
	}
	
	/**
	 * Devuelve un recurso conformado por el producto y su cantidad, según los parametros.
	 *  
	 * @param cantidad
	 * @return
	 * 		Null en caso que no exista el producto solicitado en la tienda
	 */
	 
	public Recurso venderProducto(String producto, Double cantidad){
		
		Producto p 	= new Producto(producto);
		if(consultarProducto(p)){
			
			this.Productos.get(producto).removeCantidad(cantidad);
			return new Recurso(p,cantidad);
			
		}else{
			return null;
		}	
	}
	
	/**
	 * Devuelve el valor de un producto.
	 * @param producto
	 * @return
	 * Devuelve (cero) 0 si el producto no se vende en la tienda
	 */
	public Dinero consultarPrecioUnitario(String producto){
		
		Producto p = new Producto(producto);
		if(consultarProducto(p))
			return new Dinero(this.Productos.get(p).getCostoUnitario());
		else
			return new Dinero(0);
	}
	
	/**
	 * Devuelve True si el producto se vende en la tienda, False en caso contrario
	 * 
	 * @param producto
	 * @return
	 */
	public boolean consultarProducto(Producto producto){
		
		return this.Productos.containsKey(producto);		
	}
	
	/**
	 * Recibe una cantidad de productos y la almacena en stock
	 * @param producto
	 */
	public void AlmacenarProductos(Recurso producto){
		
		if(this.Productos.containsKey(producto)){
			
			Recurso r = this.Productos.get(producto);
			r.addCantidad(producto.getCantidad());
		}else{
			this.Productos.put(producto.getProducto(), producto);
		}
	}
	
	
		
	/**Getters & setters**/
	
	@Override
	public Geometry getGeometria() {
		// TODO Auto-generated method stub
		return Geometria;
	}


	@Override
	public void setGeometria(Geometry geom) {
		this.Geometria=geom;
		
	}


	public AmbienteLocal getAmbiente() {
		return Ambiente;
	}


	public void setAmbiente(AmbienteLocal ambiente) {
		Ambiente = ambiente;
	}

	public VendedorFinal getPropietario() {
		return propietario;
	}

	public void setPropietario(VendedorFinal propietario) {
		this.propietario = propietario;
	}
	
	
}
