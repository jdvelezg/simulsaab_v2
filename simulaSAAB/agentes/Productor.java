/**
 * 
 */
package simulaSAAB.agentes;

import java.util.List;

import simulaSAAB.comunicacion.Oferta;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.tareas.SistemaActividadHumana;

/**
 * @author dampher
 *
 */
public class Productor implements AgenteInteligente {
	
	private String Rol;
	
	private String Objetivo;
	
	private simulaSAAB.comunicacion.Dinero Dinero;
	
	private Proposito PropositoVigente;
	
	private SistemaActividadHumana ActividadVigente;
	
	private List<simulaSAAB.comunicacion.Experiencia> Experiencia;
	
	private List<Terreno> Terrenos;
	
	private List<Oferta> Ofertas;
	
	private List<Producto> Productos;
	

	/**
	 * 
	 */
	public Productor() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see simulaSAAB.agentes.AgenteInteligente#atribuirSignificadoAlMundoPercibido()
	 */
	@Override
	public void atribuirSignificadoAlMundoPercibido() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see simulaSAAB.agentes.AgenteInteligente#formarIntenciones()
	 */
	@Override
	public void formarIntenciones() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see simulaSAAB.agentes.AgenteInteligente#actuar()
	 */
	@Override
	public void actuar() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see simulaSAAB.agentes.AgenteInteligente#juzgarMundoSegunEstandares()
	 */
	@Override
	public void juzgarMundoSegunEstandares() {
		// TODO Auto-generated method stub

	}

}
