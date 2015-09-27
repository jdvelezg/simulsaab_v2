package simulaSAAB.agentes;

import java.util.List;

import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.comunicacion.Experiencia;
import simulaSAAB.comunicacion.MensajeACL;
import simulaSAAB.comunicacion.Oferta;
import simulaSAAB.tareas.SistemaActividadHumana;


/**
 * Representa al agente <code>Operador de red de oferta<code>
 * <p>
 * Agente Inteligente que opera como un <code>oferente</code> que representa los intereses de otros agentes oferentes
 *  
 * @author jdvelezg
 *
 *TODO Implementar esta clase
 */
public class OperadorRedOferta implements Oferente {

	public OperadorRedOferta() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Oferta generarOferta() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void percibirMundoSelectivamente() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void formarIntenciones() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tomarDecisiones() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actuar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void juzgarMundoSegunEstandares() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getEstado() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEstado(String Estado) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Experiencia> getExperiencia() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getMayorUtilidadObtenida() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMayorUtilidadObtenida(Double valor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<SistemaActividadHumana> getActividadesEjecutables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SistemaActividadHumana getActividadVigente() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getUltimaUtilidadObtenida() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addExperiencia(Experiencia exp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setActividadVigente(SistemaActividadHumana nuevaactividad) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recibirMensaje(MensajeACL mssg) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String printActividadVigente(){
		return null;
	}

	@Override
	public Dinero getDinero() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUltimaUtilidadObtenida(Double ultimaUtilidadObtenida) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void atenderMensajes() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enviarMensaje(MensajeACL mensaje, AgenteInteligente receptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buscarRespuesta(Integer mensajeID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String LugarOferta() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
