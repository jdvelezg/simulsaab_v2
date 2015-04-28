package simulaSAAB.comunicacion;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

public class OrdenDePedido implements Concepto {
	
	private Oferta Oferta;
	
	private Demanda Demanda;
	
	private Geometry PuntoGenerador;
	
	private List<Object> OrdenesDeCompra;
	
	private List<Object> OrdenesDeServicio;

	public OrdenDePedido() {
		// TODO Auto-generated constructor stub
	}

}
