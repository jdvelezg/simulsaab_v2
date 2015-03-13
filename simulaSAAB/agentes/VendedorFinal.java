/**
 * 
 */
package simulaSAAB.agentes;

import java.util.ArrayList;
import java.util.List;

import simulaSAAB.comunicacion.Demanda;
import simulaSAAB.comunicacion.Experiencia;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.inteligencia.Cerebro;
import simulaSAAB.tareas.SistemaActividadHumana;

/**
 * @author dampher
 *
 */
public class VendedorFinal implements AgenteInteligente, Demandante {
	
	public static String ROL ="vendedor";
	
	public static String INTENCION ="comprar al pormayor y vender al pormenor";
	
	private String Objetivo;
	
	private simulaSAAB.comunicacion.Dinero Dinero;
	
	private Proposito PropositoVigente;
	
	private Cerebro Cerebro;
	
	private SistemaActividadHumana ActividadVigente;
	
	private List<Experiencia> Experiencia;
	
	private List<Tienda> Tiendas;
	
	private List<Demanda> Demandas;
	
	private List<Producto> Productos;
	
	private List<SistemaActividadHumana> ActividadesEjecutables;
	
	private String Estado;

	@Override
	public void percibirMundoSelectivamente() {
		//Consulta tareas ejecutables en el ambiente
		
		this.ActividadesEjecutables =new ArrayList();				
			
		for(Tienda local : Tiendas){			
			//ActividadesEjecutables.addAll(local.getAmbiente().getActividadesViables());
		}		
	}

	@Override
	public void formarIntenciones() {
		
		///this.PropositoVigente = new PropositosFactory(this.ROL,this.INTENCION).getProposito();
		
	}

	@Override
	public void tomarDecisiones() {
		// TODO Auto-generated method stub
		this.ActividadVigente = Cerebro.tomarDecision(ActividadesEjecutables);	
	}

	@Override
	public void actuar() {
		// TODO Auto-generated method stub
		ActividadVigente.secuenciaPrincipalDeAcciones(this);
	}

	@Override
	public void juzgarMundoSegunEstandares() {
		// TODO Auto-generated method stub
		this.Experiencia.add(Cerebro.evaluarExperiencia());
	}
	
	
	@Override
	public void generarDemanda() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void realizarCompra() {
		// TODO Auto-generated method stub
		
	}
	
	/**getter - setters **/
	
	@Override
	public String getEstado() {
		// TODO Auto-generated method stub
		return this.Estado;
	}	
	
	@Override
	public List<Experiencia> getExperiencia(){
		if(this.Experiencia==null)
			this.Experiencia = new ArrayList();
		
		return this.Experiencia;
	}

	

	

}
