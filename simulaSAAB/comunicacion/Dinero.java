package simulaSAAB.comunicacion;

public class Dinero implements Concepto {
	
	private double Cantidad;
	
	private String Moneda;
	

	public Dinero() {
		// TODO Auto-generated constructor stub
	}
	
	public Dinero(double monto, String currency){
		this.Cantidad 	= monto;
		this.Moneda		= currency;
	}
	
	public Dinero(double monto){
		this.Cantidad 	= monto;
		this.Moneda		= "COP";
	}

	public double getCantidad() {
		return Cantidad;
	}

	public void setCantidad(double cantidad) {
		Cantidad = cantidad;
	}

	public String getMoneda() {
		return Moneda;
	}

	public void setMoneda(String moneda) {
		Moneda = moneda;
	}	
	

}
