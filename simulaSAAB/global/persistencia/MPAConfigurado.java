package simulaSAAB.global.persistencia;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.comunicacion.Recurso;
/**
 * Persiste los datos asociados a las actividades con propósito definido configuradas para ejecución por parte de los agentes 
 * @author lfgomezm
 *
 */
public class MPAConfigurado {
	
	/**
	 * Identificador del <code>MPA</code> configurado
	 */
	private int id;
	/**
	 * <code>Propósito</code> del <code>MPA</code> configurado
	 */
	private simulaSAAB.comunicacion.Proposito proposito;
	/**
	 * Enunciado de la actividad con porpósito definido 
	 */
	private String Enunciado;
	/**
	 * Costo de la actividad con propósito definido
	 */
	private double Costo;
	
	//private List<Recurso> requisitos;
	
	
	/**
	 * Constructor
	 * @param nombreMPA <code>string</code>, nombre de la actividad
	 */
	public MPAConfigurado(String nombreMPA){
		
		ProductoConfigurado cebolla = new ProductoConfigurado("Cebolla de Bulbo");
		
		switch(nombreMPA){
		
		case  "ProduccionCebollaBulbo":
			
			this.id 		=1;
			this.proposito 	=new Proposito("Producir cebolla y vender el producto");
			this.Enunciado	="Sistema de gestion de finca operado por un productor que mediante la transformacion de recursos naturales, materia prima,"+
							"conocimiento e informacion en productos alimenticios comercializables busca suplir sus necesidades de subsistencia";
			this.Costo		=cebolla.getCostoproduccionhectarea();			
						
			
			
			break;
		case  "ProduccionCebollaBulbo2":
			
			this.id 		=2;
			this.proposito 	=new Proposito("Producir cebolla y vender el producto");
			this.Enunciado	="Sistema de gestion de finca operado por un productor que mediante la transformacion de recursos naturales, materia prima,"+
							"conocimiento e informacion en productos alimenticios comercializables busca suplir sus necesidades de subsistencia";
			this.Costo		=cebolla.getCostoproduccionhectarea();			
						
			
			
			break;
		case  "ComprarCebollaBulbo":
			
			this.id 		=3;
			this.proposito 	=new Proposito("Comprar cebolla y venderla al detal");
			this.Enunciado	="Comprar productos";
			this.Costo		=cebolla.getPrecioSIPSA();
				
			
			break;
		case  "VenderCebollaBulbo":
			
			this.id 		=4;
			this.proposito 	=new Proposito("Comprar cebolla y venderla al detal");
			this.Enunciado	="Vender Productos";
			this.Costo		=cebolla.getPrecioSIPSA();
									
			
			break;
		case  "TransportarCebollaBulbo":
			
			this.id 		=5;
			this.proposito 	=new Proposito("Transportar Cebolla de Bulbo disponible");
			this.Enunciado	="Transportar Productos";
			this.Costo		=100000;
						
			
			break;	
		case  "RegistrarOfertaUnitaria":
			
			this.id 		=6;
			this.proposito 	=new Proposito("vender productos");
			this.Enunciado	="Registrar Oferta en el SISAAB";
			this.Costo		=new Double(0).doubleValue();
				
			
			break;
		case  "RegistrarDemandaUnitaria":
			
			this.id 		=7;
			this.proposito 	=new Proposito("Comprar para vender productos");
			this.Enunciado	="Registrar Demanda  en el SISAAB";
			this.Costo		=new Double(0).doubleValue();
				
			
			break;
		case  "ConsolidarDemanda":
			
			this.id 		=8;
			this.proposito 	=new Proposito("Comprar para vender productos");
			this.Enunciado	="Registrar Demanda Consolidada en el SISAAB";
			this.Costo		=new Double(0).doubleValue();
				
			
			break;
		case  "ComprarEnTienda":
			
			this.id 		=9;
			this.proposito 	=new Proposito("Comprar productos");
			this.Enunciado	="Comprar en tienda";
			this.Costo		=new Double(0).doubleValue();
				
			
			break;
		case "ComprarLocalmente":
			
			this.id 		=10;
			this.proposito 	=new Proposito("Comprar para vender productos");
			this.Enunciado	="Comprar localmente";
			this.Costo		=new Double(0).doubleValue();
			
			break;
		case "VenderLocalmente":
			
			this.id 		=11;
			this.proposito 	=new Proposito("vender productos");
			this.Enunciado	="Vender localmente";
			this.Costo		=new Double(0).doubleValue();
			
			break;
		case "ComprarEnCorabastos":
			
			this.id 		=12;
			this.proposito 	=new Proposito("Comprar para vender productos");
			this.Enunciado	="Comprar en Corabastos";
			this.Costo		=new Double(0).doubleValue();
			
			break;
		default:
			this.id			= 99;
			this.proposito 	=new Proposito("Secundario");
			this.Enunciado	="Secundario";
			this.Costo		=new Double(0).doubleValue();
			
		}
	}


	/**
	 * Devuelve el costo asociado a la actividad
	 * @return costo <code>double</code>, costo de ejecución de la actividad
	 */
	public double getCosto() {
		return Costo;
	}


	/**
	 * Asigna el costo asociado a la actividad
	 * @param costo <code>double</code>, costo de ejecución de la actividad
	 */
	public void setCosto(double costo) {
		Costo = costo;
	}


	/**
	 * Devuelve el identificador del <code>MPA</code>
	 * @return id <code>int</code>, identificador de la actividad con propósito definido
	 */
	public int getId() {
		return id;
	}


	/**
	 * Asigna el identificador de la actividad
	 * @param id <code>int</code>, identificador de la actividad con propósito definido
	 */
	public void setId(int id) {
		this.id = id;
	}


	/**
	 * Devuelve el propósito de la actividad
	 * @return proposito objetivo del <code>MPA</code>
	 */
	public Proposito getProposito() {
		return proposito;
	}


	/**
	 * Asigna el propósito a la actividad
	 * @param proposito objetivo del <code>MPA</code>
	 */
	public void setProposito(Proposito proposito) {
		this.proposito = proposito;
	}


	/**
	 * Devuelve el enunciado de la actividad
	 * @return enunciado  <code>string</code>, nombre del <code>MPA</code>
	 */
	public String getEnunciado() {
		return Enunciado;
	}


	/**
	 * Asigna el enunciado a la actividad
	 * @param enunciado <code>string</code>, nombre del <code>MPA</code>
	 */
	public void setEnunciado(String enunciado) {
		Enunciado = enunciado;
	}

	

}