package simulaSAAB.agentes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Logger;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.util.collections.IndexedIterable;
import simulaSAAB.comunicacion.Demanda;
import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.comunicacion.Oferta;
import simulaSAAB.comunicacion.OrdenDeCompra;
import simulaSAAB.comunicacion.OrdenDePedido;
import simulaSAAB.comunicacion.OrdenDeServicio;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Recurso;
import simulaSAAB.contextos.CentroUrbano;
import simulaSAAB.contextos.SaabContextBuilder;

public class SISAABX extends SiSaab {
	
	/**
	* Registro de la clase usado para depuración <code>Debugging</code>
	*/
	private static Logger LOGGER = Logger.getLogger(SISAABX.class.getName());
		
	private static Map<Integer,Oferta> ofertasMap = new ConcurrentHashMap<Integer,Oferta>();
	
	private static Map<Integer,Demanda> demandasMap = new ConcurrentHashMap<Integer,Demanda>();
	
	private static Map<Integer,OrdenDePedido> pedidosMap = new ConcurrentHashMap<Integer,OrdenDePedido>();
	

	public SISAABX() {
		
		dataBaseInitializer();
	}
	
	/**
	 * Registra una oferta en el sistema para ser tomada en cuenta en las transacciones de comercialziación.
	 * @param oferta <code>Oferta</code> a registrar
	 */
	public synchronized static void registrarOferta(Oferta oferta){
		
		oferta.setVigente();
		
		Oferente agente			= oferta.getVendedor();
		int agenteid			= agente.getId();
		String puntoLogistico	= agente.LugarOferta();		
		String rol 				= "OFERENTE";
		
		int ofertaid			= oferta.getId();
		int productoid 			= oferta.getProducto().getId();
		double preciototal		= oferta.getPrecio();
		double cantidad			= oferta.getCantidad();
		double preciounitario	= preciototal/cantidad;
		String estado 			= oferta.getEstado();
		
		try{
			Class.forName("org.h2.Driver");
	        Connection con = DriverManager.getConnection("jdbc:h2:~/sisaab", "sisaab", "" );
	        Statement stmt = con.createStatement();
	        
	        //verifica que el agente este registrado en el sistema
	        ResultSet rs = stmt.executeQuery("SELECT * FROM AGENTES WHERE agente_id="+agenteid);
	        if(!(rs.next())){
	        	//Registra el agente en el sistema
	        	stmt.executeUpdate( "INSERT INTO AGENTES ( agente_id, punto_logistico, rol ) VALUES ( '"+agenteid+"','"+puntoLogistico+"', '"+rol+"' )" );	        	
	        }
	        
	        //comprueba que la oferta no hubiese sido registrada con anterioridad
	        ResultSet rs2 = stmt.executeQuery("SELECT * FROM OFERTAS WHERE oferta_id="+ofertaid);
	        if(!(rs2.next())){
	        	//Registra el agente en el sistema
	        	stmt.executeUpdate( "UPDATE OFERTAS SET "
	        			+ "oferta_id='"+ofertaid+"',agente_id='"+agenteid+"', "
	        			+ "producto_id='"+productoid+"', precio_total='"+preciototal+"', "
	        			+ "precio_unitario='"+preciounitario+"', cantidad='"+cantidad+"', "
	        			+ "estado='"+estado+"';");
	        }else{
	        	//Registra la oferta
	        	stmt.executeQuery( "INSERT INTO OFERTAS ( oferta_id, agente_id, producto_id, precio_total, precio_unitario, cantidad, estado) "
	        		+ "VALUES ( '"+ofertaid+"','"+agenteid+"', '"+productoid+"', '"+preciototal+"', '"+preciounitario+"', '"+cantidad+"', '"+estado+"')" );
	        	ofertasMap.put(oferta.getId(), oferta);
	        }
	        stmt.close();
            con.close();
		}catch(Exception e){
			LOGGER.info(e.getMessage());
		}
				
		/*
		 * Crea un objeto Schedule y agrega el objeto para que su metodo step sea ejecutado 
		 * en cada tick de la simulación
		 */
		double tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		double start = tick < 1 ? 1 : tick + 2;
		int interval = 1;
		
		ScheduleParameters params = ScheduleParameters.createRepeating(start,interval);
		ISchedule sch = RunEnvironment.getInstance().getCurrentSchedule();
		sch.schedule(params, oferta, "step");
	}
	
	/**
	 * Permite actualizar el estado de una oferta fijandola como VENCIDA
	 */
	public synchronized void caducarOferta(int ofertaid){
		Oferta o = ofertasMap.get(ofertaid);		
		try{
			Class.forName("org.h2.Driver");
	        Connection con = DriverManager.getConnection("jdbc:h2:~/sisaab", "sisaab", "" );
	        Statement stmt = con.createStatement();
	        
	        //Actualiza la oferta
	        stmt.executeQuery( "UPDATE OFERTAS SET estado = 'VENCIDA' WHERE oferta_id ='"+ofertaid+"';");            
	        stmt.close();
            con.close();
		}catch(Exception e){
			LOGGER.info(e.getMessage());
		}
		o.setVencida();
	}
	
	/**
	 * Registra una demanda en el sistema para ser tomada en cuenta en las transacciones de comercialización 
	 * @param demanda demanda a ser registrada en el sistema
	 */
	public synchronized static void registrarDemanda(Demanda demanda){
		
		if(demanda.getPresupuesto()>0){
			
			demanda.setVigente();
			
			Demandante agente		= demanda.getComprador();
			int agenteid			= agente.getId();
			String puntoLogistico	= agente.getPuntoDemanda().getNombre();		
			String rol 				= "DEMANDANTE";
			
			int demandaid			= demanda.getId();
			int productoid 			= demanda.getProducto().getId();
			double cantidad			= demanda.getCantidad();
			String estado			= demanda.getEstado();
			
			try{
				Class.forName("org.h2.Driver");
		        Connection con = DriverManager.getConnection("jdbc:h2:~/sisaab", "sisaab", "" );
		        Statement stmt = con.createStatement();
		        
		        //verifica que el agente este registrado en el sistema
		        ResultSet rs = stmt.executeQuery("SELECT * FROM AGENTES WHERE agente_id="+agenteid);
		        if(!(rs.next())){
		        	//Registra el agente en el sistema
		        	stmt.executeUpdate( "INSERT INTO AGENTES ( agente_id, punto_logistico, rol ) VALUES ( '"+agenteid+"','"+puntoLogistico+"', '"+rol+"' )" );		        	
		        }
		        
		        //comprueba que la dmeanda no hubiese sido registrada con anterioridad
		        ResultSet rs2 = stmt.executeQuery("SELECT * FROM DEMANDAS WHERE demanda_id="+demandaid);
		        if(!(rs2.next())){
		        	//Registra el agente en el sistema
		        	stmt.executeUpdate( "UPDATE DEMANDAS SET "
		        			+ "demanda_id='"+demandaid+"',agente_id='"+agenteid+"', "
		        			+ "producto_id='"+productoid+"', cantidad='"+cantidad+"', "
		        			+ "punto_demanda='"+puntoLogistico+"', estado='"+estado+"';");
		        }else{
		        	//Registra la demanda
			        stmt.executeQuery( "INSERT INTO DEMANDAS ( 	demanda_id, agente_id, producto_id, cantidad, punto_demanda, estado) "
			        		+ "VALUES ( '"+demandaid+"','"+agenteid+"', '"+productoid+"', '"+cantidad+"', '"+puntoLogistico+"', '"+estado+"')" );
			        demandasMap.put(demanda.getId(), demanda);
		        }           
		        stmt.close();
	            con.close();
			}catch(Exception e){
				LOGGER.info(e.getMessage());
			}
		}//ENDIF budget zero
		
		/*
		 * Crea un objeto Schedule y agrega el objeto para que su metodo step sea ejecutado 
		 * en cada tick de la simulación
		 */
		/*double tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		double start = tick < 1 ? 1 : tick + 2;
		int interval = 1;
		
		ScheduleParameters params = ScheduleParameters.createRepeating(start,interval);
		ISchedule sch = RunEnvironment.getInstance().getCurrentSchedule();
		sch.schedule(params, demanda, "step");*/
	}
	
	/**
	 * Permite actualizar el estado de una demanda fijandola como VENCIDA
	 */
	public synchronized void caducarDemanda(int demandaid){
		Demanda d = demandasMap.get(demandaid);
		try{
			Class.forName("org.h2.Driver");
	        Connection con = DriverManager.getConnection("jdbc:h2:~/sisaab", "sisaab", "" );
	        Statement stmt = con.createStatement();
	        
	        //Actualiza la oferta
	        stmt.executeQuery( "UPDATE DEMANDAS SET estado = 'VENCIDA' WHERE demanda_id ='"+demandaid+"';");            
	        stmt.close();
            con.close();
		}catch(Exception e){
			LOGGER.info(e.getMessage());
		}
		d.setVencida();
	}
	
	/**
	 * Devuelve las ofertas registradas para el producto pasado como parametro
	 * @param producto producto requerido
	 * @param asc booleano de ser <code>true</code> la lista de ofertas es devuelta en forma ascendente, de ser <code>false</code> de forma descendente
	 * @return List<Oferta>
	 */
	public synchronized static List<Oferta> ofertasRegistradas(String producto, boolean asc){
		
		List<Oferta> ofertas 	= new ArrayList<Oferta>();
		Producto p 				= new Producto(producto);
		//Obtiene ofertas registradas
		try{
			Class.forName("org.h2.Driver");
	        Connection con = DriverManager.getConnection("jdbc:h2:~/sisaab", "sisaab", "" );
	        Statement stmt = con.createStatement();
	        
	        
	        ResultSet rs = stmt.executeQuery("SELECT * FROM OFERTAS WHERE producto_id='"+p.getId()+"' AND estado LIKE 'VIGENTE';");
	        while(rs.next()){
	        	Oferta o = ofertasMap.get(rs.getInt("oferta_id"));
	        	o.setEstado(rs.getString("estado"));
	        	ofertas.add(o);
	        }
	        stmt.close();
            con.close();
		}catch(Exception e){
			LOGGER.info(e.getMessage());
		}
		
		return ofertas;
	}
	
	
	
	/**
	 * Permite la compra de productos relacionando una oferta y una demanda registrada
	 * @param offer listado de <code>Ofertas</code> relacionadas en la compra
	 * @param demanda Demanda relacionada en la compra
	 */
	public synchronized static List<Oferta> realizarCompra(Demanda demanda){
		
		double cantidad 		= demanda.getCantidad();
		int producto_id			= demanda.getProducto().getId();
		List<Oferta> ofertas 	= new ArrayList<Oferta>();
		try{
			Class.forName("org.h2.Driver");
	        Connection con = DriverManager.getConnection("jdbc:h2:~/sisaab", "sisaab", "" );
	        Statement stmt = con.createStatement();
	      //selecciona las ofertas cuya cantidad se igual a la demandanda
	        ResultSet rs = stmt.executeQuery("SELECT * FROM OFERTAS WHERE producto_id='"+producto_id+"' AND estado LIKE 'VIGENTE' AND cantidad='"+cantidad+"' ORDER BY precio_total ASC;");
	        if(rs.next()){//existe una oferta con la misma cantidad de productos
	        	Oferta o = ofertasMap.get(rs.getInt("oferta_id"));
	        	o.setEstado(rs.getString("estado"));
	        	ofertas.add(o);
	        }else{//selecciona las ofertas cuya cantidad se menor a la demandanda
	        	ResultSet rs2 = stmt.executeQuery("SELECT * FROM OFERTAS WHERE producto_id='"+producto_id+"' AND estado LIKE 'VIGENTE' AND cantidad<'"+cantidad+"' ORDER BY cantidad DESC, precio_total ASC;");
	        	while(rs2.next()){
		        	Oferta o = ofertasMap.get(rs.getInt("oferta_id"));
		        	o.setEstado(rs.getString("estado"));
		        	ofertas.add(o);
		        }
	        }
	        //registra la compra
	        registrarCompra(ofertas,demanda);
	        
	        stmt.close();
            con.close();
		}catch(Exception e){
			LOGGER.info(e.getMessage());
		}
		
		return ofertas;
	}
	
	public synchronized static void registrarCompra(List<Oferta> offer,Demanda demanda){
				
		Map<CentroUrbano,List<OrdenDeCompra>> OfertasMap = new ConcurrentHashMap<CentroUrbano,List<OrdenDeCompra>>();
		List<OrdenDeCompra> ordenesCompra				 = new ArrayList<OrdenDeCompra>();
		List<OrdenDeServicio> ordenesServicio			 = new ArrayList<OrdenDeServicio>();
		Demandante demandante						 	 = demanda.getComprador();
		Dinero dineroDemandante							 = demandante.getDinero();
				
		if(offer.size()!=0){
			
			//Crea las ordenes de compra
			for(Oferta o:offer){
				OrdenDeCompra ordenCompra = new OrdenDeCompra(o,demanda);
				ordenesCompra.add(ordenCompra);
				
				/*
				 * Organiza por punto de Oferta
				 * El punto de oferta es genérico, referencia el centro urbano
				 * 
				 * TODO los puntos de oferta deben ser las fincas registradas en el SISAAB
				 */
				if(!OfertasMap.containsKey(o.getPuntoOferta())){
					
					ArrayList<OrdenDeCompra> value = new ArrayList<OrdenDeCompra>();
					value.add(ordenCompra);
					OfertasMap.put(o.getPuntoOferta(), value);					
				}else{	
					
					List<OrdenDeCompra> value = OfertasMap.get(o.getPuntoOferta());
					value.add(ordenCompra);
				}
			}//endFOR
			
			//Crea ordenes de servicio logistico
			Iterator<CentroUrbano> iter = OfertasMap.keySet().iterator();
			while(iter.hasNext()){
				
				CentroUrbano puntoOferta 			= iter.next();
				OrdenDeServicio servicioLogistico	= new OrdenDeServicio(puntoOferta, OfertasMap.get(puntoOferta), demanda);				
				ordenesServicio.add(servicioLogistico);				
			}//endWHILE
			
			//Crea la orden de pedido
			OrdenDePedido ordenPedido = new OrdenDePedido(demanda, ordenesCompra, ordenesServicio);
			ordenPedido.calcularPagoTotalAsociado();
			
			//Registra en Base de datos
			try{
				Class.forName("org.h2.Driver");
		        Connection con = DriverManager.getConnection("jdbc:h2:~/sisaab", "sisaab", "" );
		        Statement stmt = con.createStatement();
		        
		        String pedidoSql 	= "INSERT INTO ORDEN_PEDIDO(orden_pedido_id, demanda_id, punto_logistico_demanda, pago_asociado, estado) "
		        		+ "VALUES ('', '"+demanda.getId()+"', '"+demanda.getPuntoDemanda().getNombre()+"', '"+ordenPedido.getPagoAsociado()+"','"+ordenPedido.getEstado()+"');";		        
		        stmt.executeUpdate(pedidoSql, Statement.RETURN_GENERATED_KEYS);		        
		        ResultSet rsPedido 	= stmt.getGeneratedKeys();
		        
		        if(rsPedido.next()){
		        	int idOrdenPedido = rsPedido.getInt("orden_pedido_id");
		        	ordenPedido.setId(idOrdenPedido);
		        	pedidosMap.put(idOrdenPedido, ordenPedido);
		        	
		        	for(OrdenDeServicio ord: ordenesServicio){
		        		
		        		String servicioSql 		="INSERT INTO ORDEN_SERVICIO(orden_servicio_id, orden_pedido_id, punto_logistico_origen, punto_logistico_destino, costo, estado) "
		        				+ "VALUES('','"+idOrdenPedido+"','"+ord.getOrigen().getNombre()+"','"+ord.getDestino().getNombre()+"','"+ord.getCostoServicioLogistico()+"','"+ord.getEstado()+"');";
			        	stmt.executeUpdate(servicioSql, Statement.RETURN_GENERATED_KEYS);
			        	ResultSet rsServicio 	= stmt.getGeneratedKeys();
			        	if(rsServicio.next()){
			        		int idOrdenServicio = rsServicio.getInt("orden_servicio_id");
			        		ord.setId(idOrdenServicio);
			        		
			        		for(OrdenDeCompra compra:ord.getOfertas()){
			        			
			        			String compraSql 	="INSERT INTO ORDEN_COMPRA(orden_compra_id, orden_servicio_id, oferta_id, demanda_id, pago_asociado, punto_oferta) "
			        					+ "VALUES ('', '"+idOrdenServicio+"', '"+compra.getOferta().getId()+"', '"+compra.getDemanda().getId()+"', '"+compra.getPagoAcordado()+"', '"+compra.getPuntoDeOferta().getNombre()+"')";
					        	stmt.executeUpdate(compraSql, Statement.RETURN_GENERATED_KEYS);		        
						        ResultSet rscompra 	= stmt.getGeneratedKeys();
						        if(rscompra.next()){
						        	int idCompra = rscompra.getInt("orden_compra_id");
						        	compra.setId(idCompra);
						        }						        
			        		}//ENDforCompras
			        	}//IFrsServicio			        	
		        	}//ENDForOrdenServicio
		        }//ENDIf-Pedido
		        
		        stmt.close();
	            con.close();
			}catch(Exception e){
				LOGGER.info(e.getMessage());
			}
		}
	}
	
	
	
	/**
	 * Inicializa la base de datos del SISAAB
	 */
	private void dataBaseInitializer(){		
		
		try
        {
            Class.forName("org.h2.Driver");
            Connection con = DriverManager.getConnection("jdbc:h2:~/sisaab", "sisaab", "" );
            Statement stmt = con.createStatement();
            //stmt.executeUpdate( "DROP TABLE table1" );
            stmt.executeUpdate( "CREATE TABLE PRODUCTOS ( producto_id INTEGER, nombre VARCHAR_IGNORECASE(50), tipo_producto VARCHAR_IGNORECASE(50), unidad_medida VARCHAR_IGNORECASE(50))" );
            stmt.executeUpdate( "CREATE TABLE DEMANDAS ( demanda_id INTEGER AUTO_INCREMENT NOT NULL,  agente_id INTEGER, producto_id INTEGER, cantidad FLOAT, punto_demanda VARCHAR_IGNORECASE(50), estado VARCHAR_IGNORECASE(50))" );
            stmt.executeUpdate( "CREATE TABLE OFERTAS ( oferta_id INTEGER AUTO_INCREMENT NOT NULL,  agente_id INTEGER, producto_id INTEGER, precio_total FLOAT, precio_unitario FLOAT, cantidad FLOAT, estado VARCHAR_IGNORECASE(50) DEFAULT 'CREADA')" );
            stmt.executeUpdate( "CREATE TABLE AGENTES ( agente_id INTEGER, punto_logistico VARCHAR_IGNORECASE(50), rol VARCHAR_IGNORECASE(50) )" );
            stmt.executeUpdate( "CREATE TABLE ORDENES_COMPRA (orden_compra_id INTEGER AUTO_INCREMENT NOT NULL, orden_servicio_id INTEGER NOT NULL, oferta_id INTEGER, demanda_id INTEGER, pago_asociado FLOAT, punto_oferta VARCHAR_IGNORECASE(50))" );
            stmt.executeUpdate( "CREATE TABLE ORDENES_SERVICIO (orden_servicio_id INTEGER AUTO_INCREMENT NOT NULL, orden_pedido_id INTEGER NOT NULL, punto_logistico_origen VARCHAR_IGNORECASE(50), punto_logistico_destino VARCHAR_IGNORECASE(50), costo FLOAT, estado VARCHAR_IGNORECASE(50))" );
            stmt.executeUpdate( "CREATE TABLE ORDENES_PEDIDO (orden_pedido_id INTEGER AUTO_INCREMENT NOT NULL, demanda_id INTEGER, punto_logistico_demanda VARCHAR_IGNORECASE(50), pago_asociado FLOAT, estado VARCHAR_IGNORECASE(50))" );
            
            
            //stmt.executeUpdate( "INSERT INTO table1 ( user ) VALUES ( 'Claudio' )" );
            //stmt.executeUpdate( "INSERT INTO table1 ( user ) VALUES ( 'Bernasconi' )" );
 
           /* ResultSet rs = stmt.executeQuery("SELECT * FROM table1");
            while( rs.next() )
            {
                String name = rs.getString("user");
                System.out.println( name );
            }*/
            stmt.close();
            con.close();
        }
        catch( Exception e )
        {
            LOGGER.info(e.getMessage());
        }
		//LOGGER.info("OK");
		
	}

}
