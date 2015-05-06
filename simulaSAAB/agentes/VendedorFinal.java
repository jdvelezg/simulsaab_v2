/**
 * 
 */
package simulaSAAB.agentes;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import bsh.This;
import repast.simphony.engine.schedule.ScheduledMethod;
import simulaSAAB.comunicacion.Demanda;
import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.comunicacion.Experiencia;
import simulaSAAB.comunicacion.MensajeACL;
import simulaSAAB.comunicacion.Oferta;
import simulaSAAB.comunicacion.Preposicion;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.comunicacion.EjecutarAccionConProposito;
import simulaSAAB.contextos.PlazaDistrital;
import simulaSAAB.global.PropositosFactory;
import simulaSAAB.inteligencia.Cerebro;
import simulaSAAB.tareas.ConsolidarDemanda;
import simulaSAAB.tareas.EstadosActividad;
import simulaSAAB.tareas.ProcesoAgenteHumano;
import simulaSAAB.tareas.SistemaActividadHumana;

/**
 * @author dampher
 *
 */
public class VendedorFinal implements AgenteInteligente, Demandante {
	
	public static String ROL ="vendedor";
	
	public static String INTENCION ="comprar al pormayor y vender al pormenor";
	
	private static String OBJETIVO;
	
	private Proposito PropositoVigente;
	
	private simulaSAAB.comunicacion.Dinero Dinero;	
	
	private Cerebro CerebroVendedor;
	
	private ProcesoAgenteHumano ProcesoHumanoDefinido;
	
	private SistemaActividadHumana ActividadVigente;
	
	private Queue<MensajeACL> MensajesRecibidos;
	
	private boolean MensajeAceptado;
	
	private List<Experiencia> Experiencia;
	
	private List<Tienda> Tiendas;
	
	private PlazaDistrital puntoDemanda;
	
	private List<Demanda> Demandas;
	
	private List<Producto> Productos;
	
	private List<Producto> ProductosViablesPercibidos;
	
	private List<SistemaActividadHumana> ActividadesEjecutables;
	
	private Double MayorUtilidadObtenida;
	
	private Double UltimaUtilidadObtenida;
	
	private String Estado;
	
	/**
	 * Constructor
	 */
	public VendedorFinal(){
		
		Experiencia 		= new ArrayList<Experiencia>();		
		Tiendas				= new ArrayList<Tienda>();		
		Demandas			= new ArrayList<Demanda>();		
		Productos 			= new ArrayList<Producto>();
		
		ProductosViablesPercibidos 	= new ArrayList<Producto>();		
		ActividadesEjecutables 		= new ArrayList<SistemaActividadHumana>();		
		
		CerebroVendedor			= new Cerebro(this);
		ProcesoHumanoDefinido 	= new ProcesoAgenteHumano();
		
		Dinero					= new Dinero(new Double(0),"COP");
		UltimaUtilidadObtenida 	= new Double(0);
		MayorUtilidadObtenida	= new Double(0);	
		
		Estado 	= "IDLE";
	}

	
	/**
	 * Metodo que ejecuta el comportamiento del agente en cada ciclo de reloj enviado por repast
	 * Primero verifica que el agente no tenga ningun mensaje pendiente a procesar.
	 * Luego ejecuta el proceso humano definido para un agente humano activo en el mundo
	 */
	@ScheduledMethod (start = 1, interval = 1)
	public void step () {
		
		if(ProcesoHumanoDefinido.getEstado().equalsIgnoreCase(EstadosActividad.READY.toString()) && this.MensajesRecibidos.size()>0){
			//Procesa el mensaje que tiene en espera
			atenderMensajes();
			
		}else{
			ProcesoHumanoDefinido.secuenciaPrincipalDeAcciones(this);	
		}
			
	}
	
	
	
	/* (non-Javadoc)
	 * @see simulaSAAB.agentes.AgenteInteligente#atribuirSignificadoAlMundoPercibido()
	 */
	@Override
	public void percibirMundoSelectivamente() {
		
		//Consulta tareas ejecutables en el ambiente			
		for(Tienda local : Tiendas){
			
			List<SistemaActividadHumana> actividadesAmbientales = local.getAmbiente().getActividadesViables();
			
			//Filtra su proposito
			for(SistemaActividadHumana a: actividadesAmbientales){
				
				if(a.getProposito().compare(PropositoVigente))
					this.ActividadesEjecutables.add(a);				
			}		
		}	
	}

	@Override
	public void formarIntenciones() {
		
		this.PropositoVigente = new PropositosFactory(this.ROL,this.INTENCION).getProposito();
		
	}

	@Override
	public void tomarDecisiones() {
		// TODO Auto-generated method stub
		this.ActividadVigente = CerebroVendedor.tomarDecision(ActividadesEjecutables);	
	}

	@Override
	public void actuar() {
		// TODO Auto-generated method stub
		ActividadVigente.secuenciaPrincipalDeAcciones(this);
	}

	@Override
	public void juzgarMundoSegunEstandares() {
		// TODO Auto-generated method stub
		Experiencia exp = CerebroVendedor.evaluarExperiencia();		
		if(!Experiencia.contains(exp)){			
			this.addExperiencia(exp);
		}		
	}
	
	@Override
	public Demanda generarDemanda() {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public void realizarCompra() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean intencionDeConsolidacion() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void setIntencionConsolidacion(boolean bool) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public synchronized void recibirMensaje(MensajeACL mssg) {
		
		this.MensajesRecibidos.offer(mssg);		
	}
	
	private void atenderMensajes(){
		
		MensajeACL mensaje = this.MensajesRecibidos.poll();		
		
		if(mensaje.getPerformative().equalsIgnoreCase("propose")){//solo acepta proposals
			
			Preposicion contenido = mensaje.getContent();
			/**
			 * Acepta el mensaje si es una proposicion para consolidacion 
			 */
			if(contenido instanceof EjecutarAccionConProposito){
				
				EjecutarAccionConProposito proposal = (EjecutarAccionConProposito) contenido;
				SistemaActividadHumana actividad	= proposal.getActividad();
				
				if(actividad instanceof ConsolidarDemanda && this.intencionDeConsolidacion()){
					
					mensaje.addReceiver(this);
					MensajeACL respuesta 		= mensaje.getReply_with();
					AgenteInteligente sender 	= mensaje.getReply_to();
					sender.recibirMensaje(respuesta);
					this.MensajeAceptado 		= true;
					
					this.ActividadVigente = actividad.getInstance();
					this.ProcesoHumanoDefinido.setPaso(3);
					
				}else{
					rejectMessaje(mensaje);
				}
			}
			
		}else{			
			rejectMessaje(mensaje);
		}
		
		
		
	}
	
	/**
	 * Devuelve un mensaje ACL con la respuesta negativa al mensaje pasado como parametro
	 * @param mensaje
	 * 		Mensaje ACL
	 * @return
	 * 		Mensaje ACL
	 */
	private MensajeACL rejectMessaje(MensajeACL mensaje){
		
		MensajeACL respuesta = new MensajeACL(mensaje.getConversationID());
		
		respuesta.setContent(mensaje.getContent());
		respuesta.setInReply_to(mensaje.getConversationID());
		respuesta.setPerformative("reject-proposal");
		respuesta.setSender(this);
		
		AgenteInteligente sender = mensaje.getReply_to();
		sender.recibirMensaje(respuesta);
		
		this.MensajeAceptado = false;
		
		return respuesta;
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

	@Override
	public Double getMayorUtilidadObtenida() {
		// TODO Auto-generated method stub
		return MayorUtilidadObtenida;
	}

	@Override
	public void setMayorUtilidadObtenida(Double valor) {
		// TODO Auto-generated method stub
		this.MayorUtilidadObtenida = valor;
	}

	@Override
	public List<SistemaActividadHumana> getActividadesEjecutables() {
		// TODO Auto-generated method stub
		return ActividadesEjecutables;
	}

	@Override
	public SistemaActividadHumana getActividadVigente() {
		// TODO Auto-generated method stub
		return ActividadVigente;
	}

	@Override
	public Double getUltimaUtilidadObtenida() {
		// TODO Auto-generated method stub
		return UltimaUtilidadObtenida;
	}

	@Override
	public void addExperiencia(simulaSAAB.comunicacion.Experiencia exp) {
		// TODO Auto-generated method stub
		if(exp!=null)
			this.Experiencia.add(exp);
	}


	@Override
	public void setEstado(String Estado) {
		// TODO Auto-generated method stub
		
	}


	public simulaSAAB.comunicacion.Dinero getDinero() {
		return Dinero;
	}


	public void setDinero(simulaSAAB.comunicacion.Dinero dinero) {
		Dinero = dinero;
	}


	public List<Tienda> getTiendas() {
		return Tiendas;
	}


	public void addTienda(Tienda tienda) {
		
		if(Tiendas == null)
			Tiendas = new ArrayList();
		
		Tiendas.add(tienda);
	}


	public PlazaDistrital getPuntoDemanda() {
		return puntoDemanda;
	}


	public void setPuntoDemanda(PlazaDistrital puntoDemanda) {
		this.puntoDemanda = puntoDemanda;
	}

}
