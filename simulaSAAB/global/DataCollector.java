package simulaSAAB.global;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Observable;
import java.util.Observer;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import repast.simphony.engine.environment.RunEnvironment;
import simulaSAAB.agentes.Productor;
import simulaSAAB.agentes.VendedorFinal;
import simulaSAAB.comunicacion.Demanda;
import simulaSAAB.comunicacion.Experiencia;
import simulaSAAB.comunicacion.Oferta;
import simulaSAAB.comunicacion.OrdenDeCompra;
import simulaSAAB.comunicacion.OrdenDePedido;
import simulaSAAB.comunicacion.OrdenDeServicio;
import simulaSAAB.global.persistencia.AgentTrackObservable;
import simulaSAAB.inteligencia.Cerebro;
import simulaSAAB.tareas.ComprarEnCorabastos;
import simulaSAAB.tareas.ComprarEnTienda;
import simulaSAAB.tareas.ComprarEnTienda.CompraTrack;
import simulaSAAB.tareas.TransaccionComercial;
/**
 * Registra datos de la simulación haciendo uso del patrón <code>Observable</code>
 * 
 * @author jdvelezg
 *
 */
public class DataCollector implements Observer{
	
	private boolean newExecFlag;
	
	private int productorCount;
	private int ofertaCount;
	private int demandaCount;
	private int ordencompraCount;
	private int ordenpedidoCount;
	private int ordenservicioCount;
	private int compraCount;
	private int cerebroCount;
	private int transaccionCount;
	private int experienciaCount;
	private int precioFinalCount;
	
	/**
	 * Constructor
	 */
	public DataCollector() {		
		newExecFlag = true;
	}
	/**
	 * impide que al registrar los datos escriba la cabecera
	 */
	private void turnOffHeader(){
		newExecFlag = false;
	}
	/**
	 * Configura el objeto para que al registrar la siguiente linea de datos implrima primero la cabecera
	 */
	private void turnOnHeader(){
		newExecFlag = true;
	}
	/**
	 * Devuelve <code>true</code> si esta configurado para escribir la cabecera de lso datos en el siguiente registro
	 * @return
	 */
	private boolean writeHeader(){
		return newExecFlag;
	}

	@Override
	public synchronized void update(Observable arg0, Object arg1) {
		
		
		/*
		 * Determina que objeto observado se reporta
		 */
			  if(arg1 instanceof Productor.ProductorTrack){
				  
				  Productor.ProductorTrack observado = (Productor.ProductorTrack)arg1;
				  registrarProductor(observado);
				  
		}else if(arg1 instanceof Oferta.OfertaTrack){
				  
				  Oferta.OfertaTrack observado = (Oferta.OfertaTrack)arg1;
				  registrarOferta(observado);
	
		}else if(arg1 instanceof Demanda.DemandaTrack){
			
				  Demanda.DemandaTrack observado = (Demanda.DemandaTrack)arg1;
				  registrarDemanda(observado);
				  
		}else if(arg1 instanceof OrdenDeCompra.OrdenCompraTrack){
			
				  OrdenDeCompra.OrdenCompraTrack observado = (OrdenDeCompra.OrdenCompraTrack)arg1;
				  registrarOrdenCompra(observado);
				  
		}else if(arg1 instanceof OrdenDePedido.OrdenPedidoTrack){
			
				  OrdenDePedido.OrdenPedidoTrack observado = (OrdenDePedido.OrdenPedidoTrack)arg1;
				  registrarOrdenPedido(observado);
				  
		}else if(arg1 instanceof OrdenDeServicio.OrdenServicioTrack){
			
				  OrdenDeServicio.OrdenServicioTrack observado = (OrdenDeServicio.OrdenServicioTrack)arg1;
				  registrarOrdenServicio(observado);
				  
		}else if(arg1 instanceof ComprarEnTienda.CompraTrack){
			
				  ComprarEnTienda.CompraTrack observado = (ComprarEnTienda.CompraTrack)arg1;
				  registrarCompra(observado);	
				  
		}else if(arg1 instanceof Cerebro.CerebroTrack){
				  
				  Cerebro.CerebroTrack observado = (Cerebro.CerebroTrack)arg1;
				  registrarCerebro(observado);
		}else if(arg1 instanceof TransaccionComercial.TransaccionTrack){
				
				TransaccionComercial.TransaccionTrack observado = (TransaccionComercial.TransaccionTrack)arg1;
				registrarTransaccion(observado);
		}else if(arg1 instanceof Experiencia.ExperienciaTrack){
			
				Experiencia.ExperienciaTrack observado = (Experiencia.ExperienciaTrack)arg1;
				registrarExperiencia(observado);
		}else if(arg1 instanceof VendedorFinal.PrecioFinalTrack){
			
				VendedorFinal.PrecioFinalTrack observado = (VendedorFinal.PrecioFinalTrack)arg1;
				registrarPrecioFinal(observado);
		}
		else if(arg1 instanceof ComprarEnCorabastos.CorabastosTrack){
			
				ComprarEnCorabastos.CorabastosTrack observado = (ComprarEnCorabastos.CorabastosTrack)arg1;
				registrarCompraCorabastos(observado);
		}
			  
	}
	
	
	/**
	 * Guarda los datos de interes de la Decision
	 * @param c CerebroTrack con los datos del cerebro que toma la decision
	 */
	private void registrarCerebro(Cerebro.CerebroTrack c){
		
		if(cerebroCount==0) turnOnHeader();
		
		String path	= VariablesGlobales.DECISIONES_DATOS_GENERADOS;				
		guardaDatosCSV(path, c);
		
		cerebroCount++;
	}
	
	/**
	 * Guarda los datos de interes del Productor
	 * @param p ProductorTrack con los datos del productor
	 */
	private void registrarProductor(Productor.ProductorTrack p){
		
		if(productorCount==0) turnOnHeader();
				
		String path	= VariablesGlobales.PRODUCTORES_DATOS_GENERADOS;				
		guardaDatosCSV(path, p);
		
		productorCount++;		
	}
	
	/**
	 * Guarda los datos de interes de la Oferta
	 * @param o OfertaTrack con los datos de la oferta
	 */
	private void registrarOferta(Oferta.OfertaTrack o){
		
		if(ofertaCount==0) turnOnHeader();
		  
		String path	= VariablesGlobales.OFERTAS_DATOS_GENERADOS;
		guardaDatosCSV(path, o);
		
		ofertaCount++;
	}
	
	/**
	 * Guarda los datos de interes de la Demanda
	 * @param o DemandaTrack con los datos de la demanda
	 */
	private void registrarDemanda(Demanda.DemandaTrack o){
		
		if(this.demandaCount==0) turnOnHeader();
		  
		String path	= VariablesGlobales.DEMANDAS_DATOS_GENERADOS;
		guardaDatosCSV(path, o);
		
		this.demandaCount++;
	}
	
	/**
	 * Guarda los datos de interes de la Orden de Compra
	 * @param o OrdenCompraTrack con los datos de la orden de compra
	 */
	private void registrarOrdenCompra(OrdenDeCompra.OrdenCompraTrack o){
		
		if(this.ordencompraCount==0) turnOnHeader();
		
		String path	= VariablesGlobales.ORDENCOMPRA_DATOS_GENERADOS;
		guardaDatosCSV(path, o);
		
		this.ordencompraCount++;
	}
	
	/**
	 * Guarda los datos de interes de la Orden de Pedido
	 * @param o OrdenPedidoTrack con los datos de la orden de pedido
	 */
	private void registrarOrdenPedido(OrdenDePedido.OrdenPedidoTrack o){
		
		if(this.ordenpedidoCount==0) turnOnHeader();
				  
		String path	= VariablesGlobales.ORDENPEDIDO_DATOS_GENERADOS;
		guardaDatosCSV(path, o);
		
		this.ordenpedidoCount++;
	}
	
	/**
	 * Guarda los datos de interes de la Orden de servicio
	 * @param o OrdenServicioTrack con los datos de la orden
	 */
	private void registrarOrdenServicio(OrdenDeServicio.OrdenServicioTrack o){
		
		if(this.ordenservicioCount==0) turnOnHeader();
		
		String path	= VariablesGlobales.ORDENSERVICIO_DATOS_GENERADOS;
		guardaDatosCSV(path, o);
				
		this.ordenservicioCount++;
	}
	
	/**
	 * Guarda los datos de interes al momento de una compra en tienda
	 * @param o CompraTrack con los datos de la compra
	 */
	private void registrarCompra(ComprarEnTienda.CompraTrack o){
		
		if(this.compraCount==0) turnOnHeader();
		  
		String path	= VariablesGlobales.COMPRAS_DATOS_GENERADOS;
		guardaDatosCSV(path, o);
		
		this.compraCount++;
	}
	
	/**
	 * Guarda los datos de interes al momento de una compra en corabastos
	 * @param o CorabastosTrack con los datos de la compra
	 */
	private void registrarCompraCorabastos(ComprarEnCorabastos.CorabastosTrack o){
		
		if(this.compraCount==0) turnOnHeader();
		  
		String path	= VariablesGlobales.COMPRAS_DATOS_GENERADOS;
		guardaDatosCSV(path, o);
		
		this.compraCount++;
	}
	
	/**
	 * Guarda los datos de interes al momento de una trnasacción comercial
	 * @param t TransaccionTrack con los datos de la transacción
	 */
	private void registrarTransaccion(TransaccionComercial.TransaccionTrack t){
		
		if(this.transaccionCount==0) turnOnHeader();
		  
		String path	= VariablesGlobales.TRANSACCIONES_DATOS_GENERADOS;
		guardaDatosCSV(path, t);
		
		this.transaccionCount++;
	}
	
	/**
	 * Guarda los datos de interes al crear una experiencia
	 * @param e ExperienciaTrack con los datos de la experiencia
	 */
	private void registrarExperiencia(Experiencia.ExperienciaTrack e){
		
		if(this.experienciaCount==0) turnOnHeader();
		  
		String path	= VariablesGlobales.EXPERIENCIA_DATOS_GENERADOS;
		guardaDatosCSV(path, e);
		
		this.experienciaCount++;
	}
	
	/**
	 * Guarda los datos de interes al registrar un precio final 
	 * @param p PrecioFinalTrack con los datos del precio asignado
	 */
	private void registrarPrecioFinal(VendedorFinal.PrecioFinalTrack p){
		
		if(this.precioFinalCount==0) turnOnHeader();
		  
		String path	= VariablesGlobales.PRECIO_FINAL_DATOS_GENERADOS;
		guardaDatosCSV(path, p);
		
		this.precioFinalCount++;
	}
	
	/**
	 * Escribe los datos separados por comas, en un archivo plano.
	 * 
	 * @param filePath Ruta del archivo.
	 * @param data string, cadena de datos a escribir en el archivo.
	 */
	private void guardaDatosCSV(String filePath, AgentTrackObservable observado){
		
		File archivo 	= new File(filePath);
		Path filepath 	= Paths.get(archivo.toURI());
		Charset charset = Charset.forName(StandardCharsets.UTF_8.toString());
		String hline	= "\n";
		
		//escribe la cabecera
		if(writeHeader()){
			hline = "\n"+observado.dataLineStringHeader(";")+"\n";
			turnOffHeader();
		}
		
		String data = hline+observado.dataLineString(";");
		
		 
		if(archivo.exists()){//Si existe el archivo
			
			try (BufferedWriter writer = Files.newBufferedWriter(filepath, charset, StandardOpenOption.APPEND)) {			
			    writer.write(data);
			    //writer.close();
			} catch (IOException x) {			
			    System.err.format("IOException: %s%n", x);
			}
			
		}else{//Si no existe el archivo
			
			try (BufferedWriter writer = Files.newBufferedWriter(filepath, charset)) {			
			    writer.write(data);		
			    //writer.close();
			} catch (IOException x) {			
			    System.err.format("IOException: %s%n", x);
			}
		}		
	}
	
	
	/**
	 * Escribe datos en un archivo
	 * @param filePath Ruta del Archivo
	 * @param datos String a escribir en el archivo 
	 */
	public void escribeDatos(String filePath, String datos){
		
		File archivo 	= new File(filePath);
		Path filepath 	= Paths.get(archivo.toURI());
		Charset charset = Charset.forName(StandardCharsets.UTF_8.toString());
		
		if(archivo.exists()){//Si existe el archivo
			
			try (BufferedWriter writer = Files.newBufferedWriter(filepath, charset, StandardOpenOption.APPEND)) {			
			    writer.write(datos);
			    //writer.close();
			} catch (IOException x) {			
			    System.err.format("IOException: %s%n", x);
			}
			
		}else{//Si no existe el archivo
			
			try (BufferedWriter writer = Files.newBufferedWriter(filepath, charset)) {			
			    writer.write(datos);
			    //writer.close();
			} catch (IOException x) {			
			    System.err.format("IOException: %s%n", x);
			}
		}
	}

}
