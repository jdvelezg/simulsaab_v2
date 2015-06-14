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

import repast.simphony.engine.environment.RunEnvironment;
import simulaSAAB.agentes.Productor;
import simulaSAAB.comunicacion.Demanda;
import simulaSAAB.comunicacion.Oferta;
import simulaSAAB.comunicacion.OrdenDeCompra;
import simulaSAAB.comunicacion.OrdenDePedido;
import simulaSAAB.comunicacion.OrdenDeServicio;
import simulaSAAB.global.persistencia.AgentTrackObservable;
import simulaSAAB.tareas.ComprarEnTienda;
import simulaSAAB.tareas.ComprarEnTienda.CompraTrack;

public class DataCollector implements Observer{
	
	private boolean newExecFlag;
	
	private int productorCount;
	private int ofertaCount;
	private int demandaCount;
	private int ordencompraCount;
	private int ordenpedidoCount;
	private int ordenservicioCount;
	private int compraCount;

	public DataCollector() {		
		newExecFlag = true;
	}
	
	private void turnOffHeader(){
		newExecFlag = false;
	}
	
	private void turnOnHeader(){
		newExecFlag = true;
	}
		
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
		}
			  
	}
	
	/**
	 * Guarda los datos de interes del Productor
	 * @param p Datos del productor
	 */
	private void registrarProductor(Productor.ProductorTrack p){
		
		if(productorCount==0) turnOnHeader();
				
		String path	= VariablesGlobales.PRODUCTORES_DATOS_GENERADOS;				
		guardaDatosCSV(path, p);
		
		productorCount++;		
	}
	
	/**
	 * Guarda los datos de interes de la Oferta
	 * @param o
	 */
	private void registrarOferta(Oferta.OfertaTrack o){
		
		if(ofertaCount==0) turnOnHeader();
		  
		String path	= VariablesGlobales.OFERTAS_DATOS_GENERADOS;
		guardaDatosCSV(path, o);
		
		ofertaCount++;
	}
	
	/**
	 * Guarda los datos de interes de la Demanda
	 * @param o
	 */
	private void registrarDemanda(Demanda.DemandaTrack o){
		
		if(this.demandaCount==0) turnOnHeader();
		  
		String path	= VariablesGlobales.DEMANDAS_DATOS_GENERADOS;
		guardaDatosCSV(path, o);
		
		this.demandaCount++;
	}
	
	/**
	 * Guarda los datos de interes de la Orden de Compra
	 * @param o
	 */
	private void registrarOrdenCompra(OrdenDeCompra.OrdenCompraTrack o){
		
		if(this.ordencompraCount==0) turnOnHeader();
		
		String path	= VariablesGlobales.ORDENCOMPRA_DATOS_GENERADOS;
		guardaDatosCSV(path, o);
		
		this.ordencompraCount++;
	}
	
	/**
	 * Guarda los datos de interes de la Orden de Pedido
	 * @param o
	 */
	private void registrarOrdenPedido(OrdenDePedido.OrdenPedidoTrack o){
		
		if(this.ordenpedidoCount==0) turnOnHeader();
				  
		String path	= VariablesGlobales.ORDENPEDIDO_DATOS_GENERADOS;
		guardaDatosCSV(path, o);
		
		this.ordenpedidoCount++;
	}
	
	/**
	 * @param o
	 */
	private void registrarOrdenServicio(OrdenDeServicio.OrdenServicioTrack o){
		
		if(this.ordenservicioCount==0) turnOnHeader();
		
		String path	= VariablesGlobales.ORDENSERVICIO_DATOS_GENERADOS;
		guardaDatosCSV(path, o);
				
		this.ordenservicioCount++;
	}
	
	/**
	 * 
	 * @param o
	 */
	private void registrarCompra(ComprarEnTienda.CompraTrack o){
		
		if(this.compraCount==0) turnOnHeader();
		  
		String path	= VariablesGlobales.COMPRAS_DATOS_GENERADOS;
		guardaDatosCSV(path, o);
		
		this.compraCount++;
	}
	
	/**
	 * Escribe los datos separados por comas, en un archivo plano.
	 * 
	 * @param filePath Ruta del archivo.
	 * @param data cadena de datos a escribir en el archivo.
	 */
	private void guardaDatosCSV(String filePath, AgentTrackObservable observado){
		
		File archivo 	= new File(filePath);
		Path filepath 	= Paths.get(archivo.toURI());
		Charset charset = Charset.forName(StandardCharsets.UTF_8.toString());
		String hline	= "\n";
		
		//escribe la cabecera
		if(writeHeader()){
			hline = observado.dataLineStringHeader(";")+"\n";
			turnOffHeader();
		}
		
		String data = hline+observado.dataLineString(";");
		
		 
		if(archivo.exists()){//Si existe el archivo
			
			try (BufferedWriter writer = Files.newBufferedWriter(filepath, charset, StandardOpenOption.APPEND)) {			
			    writer.write(data);
			    writer.close();
			} catch (IOException x) {			
			    System.err.format("IOException: %s%n", x);
			}
			
		}else{//Si no existe el archivo
			
			try (BufferedWriter writer = Files.newBufferedWriter(filepath, charset)) {			
			    writer.write(data);		
			    writer.close();
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
			    writer.close();
			} catch (IOException x) {			
			    System.err.format("IOException: %s%n", x);
			}
			
		}else{//Si no existe el archivo
			
			try (BufferedWriter writer = Files.newBufferedWriter(filepath, charset)) {			
			    writer.write(datos);
			    writer.close();
			} catch (IOException x) {			
			    System.err.format("IOException: %s%n", x);
			}
		}
	}

}
