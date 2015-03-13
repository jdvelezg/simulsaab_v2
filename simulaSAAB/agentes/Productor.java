/**
 * 
 */
package simulaSAAB.agentes;

import java.util.ArrayList;
import java.util.List;

import simulaSAAB.comunicacion.Oferta;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.comunicacion.Experiencia;
import simulaSAAB.inteligencia.Cerebro;
import simulaSAAB.tareas.SistemaActividadHumana;

/**
 * @author dampher
 *
 */
public class Productor implements AgenteInteligente, Oferente {
	
	public static String ROL ="productor";
	
	public static String INTENCION ="producir";
	
	private String Objetivo;
	
	private simulaSAAB.comunicacion.Dinero Dinero;
	
	private Proposito PropositoVigente;
	
	private Cerebro CerebroProductor;
	
	private SistemaActividadHumana ActividadVigente;
	
	private List<Experiencia> Experiencia;
	
	private List<Terreno> TerrenosCultivables;
	
	private List<Oferta> Ofertas;
	
	private List<Producto> Productos;
	
	private List<Producto> ProductosViablesPercibidos;
	
	private List<SistemaActividadHumana> ActividadesEjecutables;
	
	private String Estado;

	/**
	 * 
	 */
	public Productor() {
		
	}
	
	
	/* (non-Javadoc)
	 * @see simulaSAAB.agentes.AgenteInteligente#atribuirSignificadoAlMundoPercibido()
	 */
	@Override
	public void percibirMundoSelectivamente() {
		//Consulta tareas ejecutables en el ambiente
		
		this.ActividadesEjecutables =new ArrayList();				
		
		for(Terreno finca : TerrenosCultivables){			
			//ActividadesEjecutables.addAll(finca.getAmbiente().getActividadesViables());
		}
	}

	/* (non-Javadoc)
	 * @see simulaSAAB.agentes.AgenteInteligente#formarIntenciones()
	 */
	@Override
	public void formarIntenciones() {
		
		//this.PropositoVigente = new PropositosFactory(this.ROL,this.INTENCION).getProposito();
	}
	
	@Override
	public void tomarDecisiones() {
		
		this.ActividadVigente = CerebroProductor.tomarDecision(ActividadesEjecutables);		
	}
	

	/* (non-Javadoc)
	 * @see simulaSAAB.agentes.AgenteInteligente#actuar()
	 */
	@Override
	public void actuar() {
		
		ActividadVigente.secuenciaPrincipalDeAcciones(this);
	}

	/* (non-Javadoc)
	 * @see simulaSAAB.agentes.AgenteInteligente#juzgarMundoSegunEstandares()
	 */
	@Override
	public void juzgarMundoSegunEstandares() {
		
		this.Experiencia.add(CerebroProductor.evaluarExperiencia());

	}
	
	@Override
	public void generarOferta() {
		// TODO Auto-generated method stub
		
	}
	
	/**getter - setters **/
	
	@Override
	public String getEstado() {
		// TODO Auto-generated method stub
		return this.Estado;
	}
	
	/**
	 * Asigna terrenos al productor
	 * @param t: terreno a agregar
	 */
	public void addTerrenos(Terreno t){
		if(this.TerrenosCultivables==null)
			this.TerrenosCultivables=new ArrayList();
		
		this.TerrenosCultivables.add(t);
	}
	
	@Override
	public List<Experiencia> getExperiencia(){
		if(this.Experiencia==null)
			this.Experiencia = new ArrayList();
		
		return this.Experiencia;
	}


}
