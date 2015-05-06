package simulaSAAB.comunicacion;

import java.util.ArrayList;
import java.util.List;

import simulaSAAB.contextos.AmbienteLocal;
import simulaSAAB.contextos.CentroUrbano;
import simulaSAAB.contextos.NodoSaab;

import com.vividsolutions.jts.geom.Geometry;

public class OrdenDeServicio implements Concepto {
	
	private Recurso ProductoTransportado;
	
	private List<Oferta> ofertas;
	
	private AmbienteLocal PuntoOrigen;
	
	private NodoSaab PuntoDestino;
	
	private String estado;

	/**
	 * Constructor
	 */
	public OrdenDeServicio() {
		this.ofertas = new ArrayList<Oferta>();
	}
	
	public OrdenDeServicio(CentroUrbano punto, List<Oferta> ofs, Demanda dem){
		
		this.ofertas				= ofs;
		this.ProductoTransportado 	= setProductoTransportado(ofs);
		this.PuntoOrigen			= punto;
		this.PuntoDestino			= dem.getPuntoDemanda();
	}
	
	private Recurso setProductoTransportado(List<Oferta> ofs){
		
		
		Double cantidad		= new Double(0);
		Producto producto	= null;
		
		
		for(Oferta o: ofs){
			
			if(producto==null)
				producto = o.getProducto();
			
			cantidad += o.getCantidad();
		}
		
		return new Recurso(producto,cantidad);
		
	}

	public Recurso getProductoTransportado() {
		return ProductoTransportado;
	}

	public void setProductoTransportado(Recurso productoTransportado) {
		ProductoTransportado = productoTransportado;
	}

	public List<Oferta> getOfertas() {
		return ofertas;
	}

	public void setOfertas(List<Oferta> ofertas) {
		this.ofertas = ofertas;
	}

	public Geometry getPuntoOrigen() {
		return PuntoOrigen.getGeometria().getCentroid();
	}
	
	public AmbienteLocal getOrigen(){
		return this.PuntoOrigen;
	}

	public void setPuntoOrigen(AmbienteLocal puntoOrigen) {
		PuntoOrigen = puntoOrigen;
	}

	public Geometry getPuntoDestino() {
		return PuntoDestino.getGeometria();
	}
	
	public NodoSaab getDestino(){
		return this.PuntoDestino;
	}

	public void setPuntoDestino(NodoSaab puntoDestino) {
		PuntoDestino = puntoDestino;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	
	

}
