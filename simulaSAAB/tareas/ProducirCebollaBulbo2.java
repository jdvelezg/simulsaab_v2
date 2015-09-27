package simulaSAAB.tareas;

import simulaSAAB.comunicacion.Producto;
import simulaSAAB.global.persistencia.MPAConfigurado;
import simulaSAAB.global.persistencia.ProductoConfigurado;
/**
 * Representa la actividad de producción de cebolla de bulbo para venta local
 * @author lfgomezm
 *
 */
public class ProducirCebollaBulbo2 extends ProducirCebollaBulbo {
	
	private final int id;
	
	/**
	 * Constructor
	 */
	public ProducirCebollaBulbo2() {
		
		super();
		
		MPAConfigurado mpa 		= new MPAConfigurado("ProduccionCebollaBulbo2");
		this.id					= mpa.getId();		
		super.venderProductos 	= new VenderLocalmente();
	}
	
	@Override
	public SistemaActividadHumana getInstance() {
		return new ProducirCebollaBulbo2();
	}
	
	@Override
	public int getId() {
		return id;
	}
	
	@Override 
	public boolean equals(Object obj){		
		if(obj instanceof SistemaActividadHumana){			
			SistemaActividadHumana act = (SistemaActividadHumana)obj;			
			return this.id==act.getId();
		}else{
			return false;
		}
	}
	
}