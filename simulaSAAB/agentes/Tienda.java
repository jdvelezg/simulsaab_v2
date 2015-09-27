package simulaSAAB.agentes;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Recurso;
import simulaSAAB.contextos.AmbienteLocal;
import simulaSAAB.contextos.GeografiaFija;
import simulaSAAB.contextos.SaabContextBuilder;
import simulaSAAB.tareas.TransaccionComercial;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Representa una <code>Tienda</code> operable por el agente {@link VendedorFinal}
 * 
 * @author jdvelezg
 *
 */
public class Tienda implements GeografiaFija{
	
	/**
	 * geometría de la tienda
	 */
	private Geometry Geometria;
	/**
	 * ambiente local donde se encuentra la tienda
	 */
	private AmbienteLocal Ambiente;
	/**
	 * arreglo de productos almacenados en la tienda
	 */
	private Map<String, Recurso> Productos;
	/**
	 * mapa de precios asignados a los productos almacenados en la Tienda
	 */
	private Map<String, Dinero> Precios;
	/**
	 * agente propietario de la tienda
	 */
	private VendedorFinal propietario;
	
	
	/**
	 * Constructor d ela clase
	 * @param geom geometría de la tienda
	 */
	public Tienda(Geometry geom) {	
		
		this.Geometria 	= geom;
		this.Productos	= new ConcurrentHashMap<String, Recurso>();
		this.Precios	= new ConcurrentHashMap<String, Dinero>();
	}
	
	/** 
	 * Devuelve un recurso conformado por el <code>producto</code> y su <code>cantidad</code>, que representa la venta de un producto
	 * 	 
	 * @param producto producto a ser vendido
	 * @param cantidad double, cantidad del producto a ser vendida
	 * @param pago dinero del comprador, para hacer efectivo el pago por la compra
	 * 
	 * @return Recurso <code>Null</code> en caso que no exista el producto solicitado en la tienda
	 */
	public Recurso venderProducto(String producto, Double cantidad, Dinero pago){
		
		Recurso compra 		= new Recurso(new Producto(producto),0);
		double pagoFinal	= new Double(0).doubleValue();
		
		
		if(consultarProducto(producto)){
			
			Recurso rec 			= this.Productos.get(producto);			
			double enStock			= rec.getCantidad();
			double costoUnitario	= rec.getCostoUnitario();			
			
			if(enStock >= cantidad){
				
				rec.removeCantidad(cantidad.doubleValue());
				pagoFinal = costoUnitario*cantidad.doubleValue();
				compra.setCantidad(cantidad.doubleValue());
				
			}else{
				
				rec.removeCantidad(enStock);
				pagoFinal = costoUnitario*enStock;
				compra.setCantidad(enStock);
			}
			
			pago.subtractCantidad(pagoFinal);
			this.propietario.getDinero().addCantidad(pagoFinal);
			compra.setCostoUnitario(costoUnitario);
			
		}else{
			compra.setCostoUnitario(pagoFinal);
		}
		
		return compra;
	}
	
	/**
	 * Realiza una transaccion comercial con recibo
	 * 
	 * @param producto producto a ser vendido
	 * @param cantidad double, cantidad del producto a ser vendida
	 * @param compradoragente que realiza la compra del producto
	 */
	public void venderProductoRecibo(String producto, Double cantidad, Demandante comprador){
		
		Recurso compra 		= new Recurso(new Producto(producto),0);
		double pagoFinal	= new Double(0).doubleValue();
		
		if(consultarProducto(producto)){
			
			Recurso rec 			= this.Productos.get(producto);			
			double enStock			= rec.getCantidad();
			double costoUnitario	= rec.getCostoUnitario();			
			
			if(enStock >= cantidad){
				
				rec.removeCantidad(cantidad.doubleValue());
				pagoFinal = costoUnitario*cantidad.doubleValue();
				compra.setCantidad(cantidad.doubleValue());
				
			}else{
				
				rec.removeCantidad(enStock);
				pagoFinal = costoUnitario*enStock;
				compra.setCantidad(enStock);
			}					
			
		}else{
			compra.setCostoUnitario(pagoFinal);
		}	
		
		/*
		 * Crea un intermediario temporal que recolectara el dinero de la compra
		 */
		int identificador = SaabContextBuilder.getIdentificadorAgente();
		Intermediario tendero = new Intermediario(identificador);
		tendero.setDinero(propietario.getDinero());
		
		TransaccionComercial actividadCompra = new TransaccionComercial(tendero,comprador,compra,pagoFinal);
		actividadCompra.OBSERVABLE.addObserver(SaabContextBuilder.OBSERVADOR);
		actividadCompra.secuenciaPrincipalDeAcciones(tendero);		
	}
	
	/**
	 * Devuelve el valor de un producto en la tienda
	 * 
	 * @param producto string, producto a ser consultado
	 * @return <code>0</code> si el producto no existe en la tienda
	 */
	public Dinero consultarPrecioUnitario(String producto){
				
		if(consultarProducto(producto))
			return new Dinero(this.Productos.get(producto).getCostoUnitario());
		else
			return new Dinero(0);
	}
	
	/**
	 * Devuelve <code>true</code> si el producto se vende en la tienda y hay existencias <code>false</code> en caso contrario
	 * 
	 * @param producto producto a ser consultado
	 * @return boolean
	 */
	public boolean consultarProducto(String producto){
		
		if(this.Productos.containsKey(producto)){
			return this.Productos.get(producto).getCantidad()>0?true:false;
		}else{
			return false;
		}		
	}
	
	/**
	 * Recibe una cantidad de productos y su precio de venta para ser almacenados en la tienda
	 * 
	 * @param producto producto a ser almacenado
	 * @param precioVenta precio den venta unitario del producto
	 */
	public void AlmacenarProductos(Recurso producto, Dinero precioVenta){
		
		String NombreProducto = producto.getProducto().getNombre();
		
		if(this.Productos.containsKey(NombreProducto)){
			
			Recurso r = Productos.get(NombreProducto);
			r.addCantidad(producto.getCantidad());
			r.setCostoUnitario(precioVenta.getCantidad());
		}else{
			this.Productos.put(NombreProducto, producto);
		}		
	}
	
		
	@Override
	public Geometry getGeometria() {
		// TODO Auto-generated method stub
		return Geometria;
	}


	@Override
	public void setGeometria(Geometry geom) {
		this.Geometria=geom;
		
	}

	/**
	 * Devuelve el ambiente del agente
	 * @return AmbienteLocal
	 */
	public AmbienteLocal getAmbiente() {
		return Ambiente;
	}

	/**
	 * Asigna el ambiente al agente
	 * @param ambiente
	 */
	public void setAmbiente(AmbienteLocal ambiente) {
		Ambiente = ambiente;
	}
	
	/**
	 * Devuelve el agente propietario d ela tienda
	 * @return VendedorFinal
	 */
	public VendedorFinal getPropietario() {
		return propietario;
	}
	/**
	 * Asigna el propietario de la tienda
	 * @param propietario agente demandante propietario de la tienda
	 */
	public void setPropietario(VendedorFinal propietario) {
		this.propietario = propietario;
	}	
	
}
