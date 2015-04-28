package simulaSAAB.global.persistencia;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.comunicacion.Recurso;

public class MPAConfigurado {
	
	
	private int id;
	
	private simulaSAAB.comunicacion.Proposito proposito;
	
	private String Enunciado;
	
	private double Costo;
	
	//private List<Recurso> requisitos;
	
	
	/**
	 * Constructor
	 * @param nombreMPA
	 */
	public MPAConfigurado(String nombreMPA){
		
		ProductoConfigurado cebolla = new ProductoConfigurado("Cebolla de Bulbo");
		
		switch(nombreMPA){
		
		case  "ProduccionCebollaBulbo":
			
			this.id 		=1;
			this.proposito 	=new Proposito("Producir");
			this.Enunciado	="Sistema de gestion de finca operado por un productor que mediante la transformacion de recursos naturales, materia prima,"+
							"conocimiento e informacion en productos alimenticios comercializables busca suplir sus necesidades de subsistencia";
			this.Costo		=cebolla.getCostoproduccionhectarea();			
						
			
			
			break;
		case  "ComprarCebollaBulbo":
			
			this.id 		=2;
			this.proposito 	=new Proposito("Comprar");
			this.Enunciado	="Comprar productos";
			this.Costo		=cebolla.getPrecioSIPSA();
				
			
			break;
		case  "VenderCebollaBulbo":
			
			this.id 		=3;
			this.proposito 	=new Proposito("Vender");
			this.Enunciado	="Vender Productos";
			this.Costo		=cebolla.getPrecioSIPSA();
									
			
			break;
		case  "TransportarCebollaBulbo":
			
			this.id 		=4;
			this.proposito 	=new Proposito("Transportar Cebolla de Bulbo disponible");
			this.Enunciado	="Transportar Productos";
			this.Costo		=100000;
						
			
			break;		
		}
	}


	/**
	 * @return the costo
	 */
	public double getCosto() {
		return Costo;
	}


	/**
	 * @param costo the costo to set
	 */
	public void setCosto(double costo) {
		Costo = costo;
	}


	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}


	/**
	 * @return the proposito
	 */
	public Proposito getProposito() {
		return proposito;
	}


	/**
	 * @param proposito the proposito to set
	 */
	public void setProposito(Proposito proposito) {
		this.proposito = proposito;
	}


	/**
	 * @return the enunciado
	 */
	public String getEnunciado() {
		return Enunciado;
	}


	/**
	 * @param enunciado the enunciado to set
	 */
	public void setEnunciado(String enunciado) {
		Enunciado = enunciado;
	}

	

}
