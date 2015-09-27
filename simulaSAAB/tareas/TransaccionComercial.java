package simulaSAAB.tareas;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import repast.simphony.engine.environment.RunEnvironment;
import simulaSAAB.agentes.Demandante;
import simulaSAAB.agentes.Oferente;
import simulaSAAB.agentes.Productor;
import simulaSAAB.agentes.Productor.ProductorTrack;
import simulaSAAB.comunicacion.Demanda;
import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.comunicacion.Oferta;
import simulaSAAB.comunicacion.OrdenDeCompra;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.comunicacion.ReciboCompra;
import simulaSAAB.comunicacion.Recurso;
import simulaSAAB.global.persistencia.AgentTrackObservable;
/**
 * Representa la ejecución de una transacción comercial 
 * 
 * @author lfgomezm
 */
public class TransaccionComercial implements SistemaActividadHumana<Oferente> {
	
	public final TransaccionTrack OBSERVABLE = new TransaccionTrack();
	/**
	 * Enunciado de la tarea
	 */
	private String enunciado;
	/**
	 * Propósito de la tarea
	 */
	private Proposito Proposito;	
	/**
	 * Agente que realiza la venta dentro de la transacción comercial en curso
	 */
	private Oferente vendedor;
	/**
	 * Agente que realiza la compra dentro de la transacción comercial en curso
	 */
	private Demandante comprador;

	private double montoTransaccion;

	private double costoLogistico;
	
	private Recurso productoComercializado;
	/**
	 * Estado actual de la tarea
	 */
	private String Estado;
	/**
	 * Paso actual de la tarea
	 */
	private int paso;
	
	
	/**
	 * Constructor
	 * 
	 * @param vendedor
	 * @param comprador
	 * @param productoComercializado
	 * @param montoTransaccion
	 */
	public TransaccionComercial(Oferente vendedor, Demandante comprador, Recurso productoComercializado, double montoTransaccion) {
		
		this.enunciado 	= "Transacción comercial";
		
		this.vendedor	= vendedor;
		this.comprador	= comprador;
		
		this.montoTransaccion		= montoTransaccion;
		this.productoComercializado	= productoComercializado;
		
		Estado 	= EstadosActividad.READY.toString();		
	}
	/**
	 * Constructor
	 */
	public TransaccionComercial(){		
		this.enunciado 	= "Transacción comercial";
	}


	@Override
	public void secuenciaPrincipalDeAcciones(Oferente actor) {
		
		if(this.Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){
			
			ReciboCompra recibo 		= new ReciboCompra(productoComercializado,montoTransaccion);
			List<OrdenDeCompra> compra 	= new ArrayList<OrdenDeCompra>();
			compra.add(recibo);
			
			double cantidad			 	= productoComercializado.getCantidad();
			double precioUnitarioFinal 	= cantidad!=0? montoTransaccion/cantidad:0;
			productoComercializado.setCostoUnitario(precioUnitarioFinal);
		
			Dinero dineroComprador 	= comprador.getDinero();
			Dinero dineroVendedor	= vendedor.getDinero();
			
			dineroComprador.subtractCantidad(montoTransaccion);
			dineroVendedor.addCantidad(montoTransaccion);
			
			comprador.recibirPedido(compra);
			comprador.gestionarPedidos();
			
			OBSERVABLE.transaccionEfectuada(actor.LugarOferta());
			Estado	=EstadosActividad.DONE.toString();			
			actor.setEstado("WAITING");
				
		}else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){	
			
		}else if(this.Estado.equalsIgnoreCase(EstadosActividad.DONE.toString())){
			
		}
		
	}

	@Override
	public SistemaActividadHumana getInstance() {
		
		return new TransaccionComercial(vendedor,comprador,productoComercializado,montoTransaccion);
	}

	@Override
	public int getPaso() {
		
		return paso;
	}

	@Override
	public String getEstado() {
		
		return this.Estado;
	}

	@Override
	public String getEnunciado() {
		
		return this.enunciado;
	}

	@Override
	public Proposito getProposito() {
		
		return this.Proposito;
	}

	@Override
	public int getId() {
		
		return 0;
	}
	/**
	 * Devuelve el objeto que realiza la venta dentro de la transacción
	 * @return vendedor  el oferente que vende el producto
	 */
	public Oferente getVendedor() {
		return vendedor;
	}
	/**
	 * Asigna el objeto que realiza la venta dentro de la transacción 
	 * @param vendedor  oferente
	 */
	public void setVendedor(Oferente vendedor) {
		this.vendedor = vendedor;
	}
	/**
	 * Devuelve el objeto que realiza la compra dentro de la transacción
	 * @return comprador  el demandante que vende el producto
	 */
	public Demandante getComprador() {
		return comprador;
	}
	/**
	 * Asigna el objeto que realiza la compra dentro de la transacción 
	 * @param comprador  demandante
	 */
	public void setComprador(Demandante comprador) {
		this.comprador = comprador;
	}
	/**
	 * Devuelve el monto total de la transacción 
	 * @return montoTransaccion   double, valor de la transacción en ejecución 
	 */
	public double getMontoTransaccion() {
		return montoTransaccion;
	}
	/**
	 * Asigna el monto total de la transacción 
	 * @param montoTransaccion  double
	 */
	public void setMontoTransaccion(double montoTransaccion) {
		this.montoTransaccion = montoTransaccion;
	}
	/**
	 * Devuelve el tipo de producto que se compra y vende en la transacción en ejecución
	 * @return productoComercializado   recurso, nombre del producto comercializado
	 */
	public Recurso getProductoComercializado() {
		return productoComercializado;
	}
	/**
	 * Asigna el monto total de la transacción 
	 * @param productoComercializado  recurso
	 */
	public void setProductoComercializado(Recurso productoComercializado) {
		this.productoComercializado = productoComercializado;
	}

	@Override
	public double getCosto() {
		// TODO Auto-generated method stub
		return 0;
	}
	/**
	 * Permite agregar el costo logístico asociado a la transacción
	 * @param costoLogistico   double, valor de las operaciones logísticas asociadas
	 */
	public void addCostoLogistico(double costoLogistico) {
		this.costoLogistico += costoLogistico;
	}


	/**
	 * Clase anidada para la recoleccion de datos de simulación
	 * asociados a las transacciones comerciales por fuera del SISAAB
	 * @author lfgomezm
	 *
	 */
	public class TransaccionTrack extends AgentTrackObservable{
		
		private Double tick;
		private String vendedor;		
		private String comprador;
		private String lugarTransaccion;
		private Double cantidadVendida;
		private Double pagoAcordado;
		private Double costoUnitario;
		
		/**
		 * Constructor
		 */
		public TransaccionTrack(){
			super();
		}
		
		/**
		 * Actualiza los valores clave de la transacción
		 * @param lugarTransaccion  nombre del lugar donde se efectua la transacción
		 */
		public void transaccionEfectuada(String lugarTransaccion){
			
			this.tick 				= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
			this.vendedor			= TransaccionComercial.this.getVendedor().toString();
			this.comprador			= TransaccionComercial.this.getComprador().toString();
			this.lugarTransaccion	= lugarTransaccion;
			this.cantidadVendida	= TransaccionComercial.this.getProductoComercializado().getCantidad();
			this.pagoAcordado		= TransaccionComercial.this.getMontoTransaccion();
			this.costoUnitario		= pagoAcordado/cantidadVendida;	
			/*
			 * Registra solo si fue una compra exitosa
			 */
			if(cantidadVendida>0){
				super.setChanged();
				super.notifyObservers(this);
			}
			
		}
		
		/*
		 * Devuelve una cadena de texto con los datos separados por el carácter entregado como
		 * parámetro. EL orden de la salida es :
		 * tick + ID del productor + hectáreas del terreno + nombre centro urbano + coordenadas del terreno + cantidad cosechada + precio unitario
		 * 
		 * @param separador   carácter(es) usados como separador de los datos
		 * @return string  cadena de texto con información del productor
		 */
		@Override
		public String dataLineString(String separador) {
			
			 return tick.toString()+separador+vendedor+separador+comprador+separador+lugarTransaccion+separador+cantidadVendida.toString()+separador+pagoAcordado.toString()+separador+costoUnitario.toString();
		}

		@Override
		public String dataLineStringHeader(String separador) {
			
			return "tick"+separador+"vendedor"+separador+"comprador"+separador+"lugar_transaccion"+separador+"cantidad_vendida"+separador+"pago_acordado"+separador+"costo_unitario";
		}

		/**
		 * Devuelve el objeto que realiza la venta dentro de la transacción en ejecución 
		 * @return vendedor  string
		 */
		public String getVendedor() {
			return vendedor;
		}
		/**
		 * Asigna el objeto que realiza la venta dentro de la transacción en ejecución 
		 * @param vendedor  string
		 */
		public void setVendedor(String vendedor) {
			this.vendedor = vendedor;
		}
		/**
		 * Devuelve el objeto que realiza la compra dentro de la transacción en ejecución
		 * @return comprador  string
		 */
		public String getComprador() {
			return comprador;
		}
		/**
		 * Asigna el objeto que realiza la compra dentro de la transacción en ejecución 
		 * @param comprador string
		 */
		public void setComprador(String comprador) {
			this.comprador = comprador;
		}
		/**
		 * Devuelve el lugar en el que se realiza la transacción en ejecución
		 * @return lugarTransaccion  string
		 */
		public String getLugarTransaccion() {
			return lugarTransaccion;
		}
		/**
		 * Asigna el lugar en el que se realiza la transacción en ejecución
		 * @param lugarTransaccion  string
		 */
		public void setLugarTransaccion(String lugarTransaccion) {
			this.lugarTransaccion = lugarTransaccion;
		}
		/**
		 * Devuelve la cantidad de productos que se vendieron en la transacción
		 * @return cantidadVendida  double
		 */
		public Double getCantidadVendida() {
			return cantidadVendida;
		}
		/**
		 * Asigna la cantidad de productos que se vendieron en la transacción
		 * @param cantidadVendida  double
		 */
		public void setCantidadVendida(Double cantidadVendida) {
			this.cantidadVendida = cantidadVendida;
		}
		/**
		 * Devuelve el valor que se acordó como pago por la compra del producto
		 * @return pagoAcordado  double
		 */
		public Double getPagoAcordado() {
			return pagoAcordado;
		}
		/**
		 * Asigna el valor que se acordó como pago por la compra del producto
		 * @param pagoAcordado  double
		 */
		public void setPagoAcordado(Double pagoAcordado) {
			this.pagoAcordado = pagoAcordado;
		}
		/**
		 * Devuelve el costo por unidad de producto
		 * @return costoUnitario double
		 */
		public Double getCostoUnitario() {
			return costoUnitario;
		}
		/**
		 * Asigna el costo lor por unidad de producto
		 * @param costoUnitario double
		 */
		public void setCostoUnitario(Double costoUnitario) {
			this.costoUnitario = costoUnitario;
		}
		
	}
	

}