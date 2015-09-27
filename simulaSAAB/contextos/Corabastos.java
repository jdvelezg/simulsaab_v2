package simulaSAAB.contextos;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import simulaSAAB.agentes.AgenteInteligente;
import simulaSAAB.agentes.Demandante;
import simulaSAAB.agentes.Intermediario;
import simulaSAAB.agentes.Oferente;
import simulaSAAB.agentes.Tienda;
import simulaSAAB.agentes.VendedorFinal;
import simulaSAAB.comunicacion.Demanda;
import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.comunicacion.Experiencia;
import simulaSAAB.comunicacion.MensajeACL;
import simulaSAAB.comunicacion.OrdenDeCompra;
import simulaSAAB.comunicacion.Recurso;
import simulaSAAB.global.VariablesGlobales;
import simulaSAAB.tareas.ComprarEnTienda;
import simulaSAAB.tareas.SistemaActividadHumana;
import simulaSAAB.tareas.TransaccionComercial;

/**
 * Representa el nodo logístico de CORABASTOS
 *  
 * @author jdvelezg
 *
 */
public class Corabastos extends NodoSaab {
	
	private AmbienteLocal lugarOferta;
	
	private Dinero dinero;
	
	private Tienda ventaComercial;
	
	private VendedorFinal vendedorComercial;
	
	private Intermediario intermediarioComercial;
	
	private List<Recurso> productosAlmacenados;
	
	/*
	 * Constructor
	 */
	public Corabastos(Geometry geom) {
		super();
		super.setGeometria(geom);
		
		dinero 				= new Dinero();
		ventaComercial 		= new LocalCorabastos(this.getGeometria());
		int identificador	= SaabContextBuilder.getIdentificadorAgente();
		vendedorComercial	= new VendedorFinal(identificador);
		vendedorComercial.setDinero(dinero);
		vendedorComercial.addTienda(ventaComercial);
		ventaComercial.setPropietario(vendedorComercial);
	}
	/**
	 * Cosntructor
	 * @param nombre string, nombre del nodo logístico
	 * @param geom geometria del nodo logístico
	 */
	public Corabastos(String nombre, Geometry geom) {
		super(nombre);		
		super.setGeometria(geom);
		
		dinero 				= new Dinero();
		ventaComercial 		= new LocalCorabastos(this.getGeometria());
		int identificador	= SaabContextBuilder.getIdentificadorAgente();
		vendedorComercial	= new VendedorFinal(identificador);
		vendedorComercial.setDinero(dinero);
		vendedorComercial.addTienda(ventaComercial);
		ventaComercial.setPropietario(vendedorComercial);
	}
	
	/**
	 * Permite a un agente comprar un producto en el nodo
	 * <p>
	 * Mediante la generación de un recibo de compra, el agente asociado realiza la transacción comercial correspondiente 
	 *  	 
	 * @param producto string, producto solicitado
	 * @param cantidad double unidades de medida del producto solicitado
	 * @param comprador Demandante, agente que realiza la compra
	 */
	public void venderProducto(String producto, Double cantidad, Demandante comprador){
		ventaComercial.venderProductoRecibo(producto, cantidad, comprador);
	}
	
	/**
	 * Devuelve el valor de venta de un producto
	 * 
	 * @param producto string producto consultado
	 * @return double devuelve <code>0</code> si el producto no existe
	 */
	public Dinero consultarPrecioUnitario(String producto){				
		return this.ventaComercial.consultarPrecioUnitario(producto);
	}
	
	/**
	 * Devuelve <code>true</code> si el producto existe en la tienda <code>false</code> en caso contrario
	 * 
	 * @param producto string, producto a consultar
	 * @return boolean
	 */
	public boolean consultarProducto(String producto){
		return this.ventaComercial.consultarProducto(producto);				
	}
	
	/**
	 * Recibe un recurso para ser almacenado en la tienda
	 * <p>
	 * Actualiza el precio de venta del producto
	 * 
	 * @param producto recurso a ser almacenado y vnedido en la tienda
	 */
	public void AlmacenarProductos(Recurso producto, Dinero precioVenta){
		this.ventaComercial.AlmacenarProductos(producto, precioVenta);				
	}
	
	/**
	 * Permite a los agentes vender productos al interior de Corabastos
	 * 
	 * @param mercancia Recurso ofertado
	 * @param vendedor Oferente agente que vende los productos
	 */
	public void compraProductos(Recurso mercancia, Oferente vendedor){		
		
		double montoTransaccion = mercancia.getCostoUnitario()*mercancia.getCantidad();
		TransaccionComercial venta = new TransaccionComercial(vendedor,this.new IntermediarioCorabastos(),mercancia,montoTransaccion);
		/*
		 * TODO debe estar en un bucle si Transaccion comercial toma mas de un paso
		 */
		venta.secuenciaPrincipalDeAcciones(vendedor);		
	}	
	
	/**
	 * Devuelve el lugar de oferta
	 * @return AmbienteLocal lugar de oferta del nodo
	 */
	public AmbienteLocal getLugarOferta() {
		return lugarOferta;
	}
	/**
	 * Asigna el lugar de oferta
	 * @param AmbienteLocal, lugarOferta lugar de oferta del nodo
	 */
	public void setLugarOferta(AmbienteLocal lugarOferta) {
		this.lugarOferta = lugarOferta;
	}

	/**
	 * Representa las tiendas y locales al interior del nodo de {@link Coorabastos}
	 * 
	 * @author jdvelezg
	 *
	 */
	class LocalCorabastos extends Tienda{

		public LocalCorabastos(Geometry geom) {
			super(geom);
		}		
	}

	/**
	 * Representa los intermediarios presentes al interior de {@link Coorabastos}
	 * 
	 * @author jdvelezg
	 *
	 */
	class IntermediarioCorabastos extends Intermediario{
		
		private Dinero dinero;
		
		
		/**
		 * Constructor
		 */
		public IntermediarioCorabastos(){
			super(0);
			dinero 					= new Dinero(0);
			productosAlmacenados	= new ArrayList<Recurso>();
		}
		
		@Override
		public boolean pedidosRecibidos() {
			
			if(productosAlmacenados.size()>0)
				return true;
			else
				return false;
		}		

		@Override
		public void distribuirProductosEnTienda(Recurso productos,
				double precioCompra) {			
			
			double precioUnitarioCompra = precioCompra/productos.getCantidad();
			//calcula el precio de venta con un margen de ganancia esperado del 20% C/1-Mg
			double precioUnitarioVenta = precioUnitarioCompra/(1-VariablesGlobales.MARGEN_GANANCIA_REQUERIDO_20);
			
			Dinero precioVenta = new Dinero(precioUnitarioVenta);		
			ventaComercial.AlmacenarProductos(productos, precioVenta);			
		}
		
		@Override
		public void recibirPedido(List<OrdenDeCompra> compras) {
			
			for(OrdenDeCompra o: compras){
				
				Recurso compra		= o.getDetalleCompra();
				double precioCompra	= o.getPagoAcordado().getCantidad();
				
				distribuirProductosEnTienda(compra,precioCompra);
				productosAlmacenados.add(compra);
			}
		}
		
		@Override
		public Dinero getDinero() {			
			return dinero;
		}		
		
	}
	

}
