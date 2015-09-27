package simulaSAAB.contextos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.operation.distance.DistanceOp;

import simulaSAAB.agentes.Camion;
import simulaSAAB.agentes.Consumidor;
import simulaSAAB.agentes.Intermediario;
import simulaSAAB.agentes.OperadorLogistico;
import simulaSAAB.agentes.Productor;
import simulaSAAB.agentes.SISAABX;
import simulaSAAB.agentes.Terreno;
import simulaSAAB.agentes.Tienda;
import simulaSAAB.agentes.VendedorFinal;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.contextos.environment.Junction;
import simulaSAAB.contextos.environment.NetworkEdge;
import simulaSAAB.contextos.environment.NetworkEdgeCreator;
import simulaSAAB.global.DataCollector;
import simulaSAAB.global.SimulaSAABLogging;
import simulaSAAB.global.VariablesGlobales;
import simulaSAAB.tareas.ComprarEnCorabastos;
import simulaSAAB.tareas.ComprarLocalmente;
import simulaSAAB.tareas.ConsolidarDemanda;
import simulaSAAB.tareas.Moverse;
import simulaSAAB.tareas.ProducirCebollaBulbo;
import simulaSAAB.tareas.ProducirCebollaBulbo2;
import simulaSAAB.tareas.RecolectarProductos;
import simulaSAAB.tareas.RegistrarDemandaUnitaria;
import simulaSAAB.tareas.RegistrarOfertaUnitaria;
import simulaSAAB.tareas.SistemaActividadHumana;
import simulaSAAB.contextos.environment.Junction;
import simulaSAAB.contextos.exceptions.DuplicateRoadException;
import simulaSAAB.contextos.exceptions.NoIdentifierException;
import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.gis.GeographyFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeographyParameters;
import repast.simphony.space.gis.SimpleAdder;
import repast.simphony.space.graph.DefaultEdgeCreator;
import repast.simphony.space.graph.Network;
import repast.simphony.util.collections.IndexedIterable;
/**
 * Configuración inicial del entorno de simulación incluido en <code>Repast Simphony</code>
 * <p>
 * En esta clase se fijan todos los parámetros iniciales de la simulación, los contextos objetos y agentes
 * 
 * @author jdvelezg
 *
 */
public class SaabContextBuilder implements ContextBuilder<Object> {

	/**
	* Registro de la clase usado para depuración <code>Debugging</code>
	*/
	private static Logger LOGGER = Logger.getLogger(SaabContextBuilder.class.getName());
	
	//Contexto principal
	/**
	 * Contexto principal
	 */
	public static Context<Object> SAABContext;
	/**
	 * proyección GIS de la simulación
	 */
	public static Geography<Object> SAABGeography;
	
	//Contexto Rural
	/**
	 * Contexto rural
	 */
	public static Context<Object> RuralContext;	
	
	//Contexto Distrital
	/**
	 * Contexto distrital
	 */
	public static Context<Object> BogotaContext;
	/**
	 * proyeccion {@link Network} para las nutriredes
	 */
	public static Network<Object> NutriredesNetwork;
	
	//SISAAB Context
	/**
	 * Contexto de ejecución del SISAAB
	 */
	public static Context<Object> SISAABContext;
	/**
	 * Poryección <code>Network</code> del SISAAB
	 */
	public static Network<Object> SISAABNetwork;
	
	/**
	 * Contexto comercial del SISAAB
	 */
	public static Context<Object> ComercialContext;
	/**
	 * Contexto de transacciones del SISAAB
	 */
	public static Context<Object> TransaccionContext;
	/**
	 * proyección <code>Network</code> del contexto comercial
	 */
	public static Network<Object> ComercialNetwork;
	/**
	 * Contexto de ordenes del SISAAB
	 */
	public static Context<Object> OrdenesContext;
	
	//Vias Context
	/**
	 * Mapa de coordenadas y vias
	 */
	public static volatile Map<Coordinate, Junction> coordMap;
	/**
	 * Contexto para las vías de la simulación
	 */
	public static Context<Object> RoadContext;
	/**
	 * proyeccion <code>GIS</code> del contexto de vías
	 */
	public static Geography<Object> RoadGeography;
	
	/**
	 * Contexto para las intersecciones de vías
	 */
	public static Context<Junction> JunctionsContext;
	/**
	 * proyección <code>Network</code> del contexto de vías
	 */
	public static Network<Junction> RoadNetwork;
	
	//Recoleccion y guardado de datos
	/**
	 * Observador de objetos de la simulación
	 */
	public static DataCollector OBSERVADOR;
	
	//Referencia statica al nodo de corabastos
	/**
	 * Nodo de corabastos
	 */
	public static Corabastos Corabastos;
	
	/**
	 * consecutivo de identificador de lso agentes 
	 */
	private static int IdentificadorAgentes;
	

	@Override
	public Context<Object> build(Context<Object> context) {
				
		//Colectores de datos
		OBSERVADOR 	= new DataCollector();
		
		//Referencia al contexto principal
		SAABContext =context;		
		SAABContext.setId(VariablesGlobales.CONTEXTO_SAAB);
		
		GeographyParameters<Object>	geoparams 	= new GeographyParameters<Object>();		
		SAABGeography	= GeographyFactoryFinder.createGeographyFactory(null).createGeography(VariablesGlobales.GEOGRAFIA_SAAB, SAABContext, geoparams);
		
		//Contexto Rural
		RuralContext	=new RuralContext();
		SAABContext.addSubContext(RuralContext);
		
		cargarShapeFiles(VariablesGlobales.MUNICIPIOS_SHAPEFILE,"municipios",RuralContext,SAABGeography);
		cargarShapeFiles(VariablesGlobales.CENTROSURBANOS_SHAPEFILE,"urbano",RuralContext,SAABGeography);
		cargarShapeFiles(VariablesGlobales.URBANOSACCESS_SHAPEFILE,"urbano_access",RuralContext,SAABGeography);
		
		//Contexto Distrital
		BogotaContext =new BogotaContext();
		SAABContext.addSubContext(BogotaContext);
		
		cargarShapeFiles(VariablesGlobales.PLAZASDISTRITALES_SHAPEFILE,"plazas",BogotaContext,SAABGeography);
		cargarShapeFiles(VariablesGlobales.PLAZASACCESS_SHAPEFILE,"plazas_access",BogotaContext,SAABGeography);
		cargarShapeFiles(VariablesGlobales.PLAZASVORNOI_SHAPEFILE,"area_plaza",BogotaContext,SAABGeography);
		
		NetworkBuilder<Object> NetNutriredesBuilder	=new NetworkBuilder<Object>(VariablesGlobales.NETWORK_NUTRIREDES,BogotaContext,false);
		NetNutriredesBuilder.setEdgeCreator(new DefaultEdgeCreator<Object>());
		NutriredesNetwork = NetNutriredesBuilder.buildNetwork();
		
		//Contextos SISaab
		SISAABX SI = new SISAABX();
		SISAABContext = new SISaabContext();
		SAABContext.addSubContext(SISAABContext);
		
		//Agrega el operador logístico
		OperadorLogistico operador = new OperadorLogistico();
		SISAABContext.add(operador);
		
		cargarShapeFiles(VariablesGlobales.NODOSSAAB_SHAPEFILE,"nodos",SISAABContext,SAABGeography);
		cargarShapeFiles(VariablesGlobales.NODOSACCESS_SHAPEFILE,"nodo_access",SISAABContext,SAABGeography);
		cargarShapeFiles(VariablesGlobales.BOGOTA_SHAPEFILE,"urbano",BogotaContext,SAABGeography);
		
		ComercialContext = new DefaultContext<Object>(VariablesGlobales.CONTEXTO_COMERCIAL);
		SISAABContext.addSubContext(ComercialContext);
		
		TransaccionContext = new TransaccionContext();
		SISAABContext.addSubContext(ComercialContext);
		
		NetworkBuilder<Object> NetComercialBuilder	=new NetworkBuilder<Object>(VariablesGlobales.NETWORK_COMERCIAL,TransaccionContext,false);
		NetComercialBuilder.setEdgeCreator(new DefaultEdgeCreator<Object>());
		ComercialNetwork = NetComercialBuilder.buildNetwork();		
		
		OrdenesContext = new DefaultContext(VariablesGlobales.CONTEXTO_ORDENES);
		SISAABContext.addSubContext(OrdenesContext);
		
		/*PERFORMANCE	*/	
		//Contextos de rutas
		RoadContext = new RoadContext();
		SAABContext.addSubContext(RoadContext);
						
		cargarShapeFiles(VariablesGlobales.RUTAS_SHAPEFILE,"via",RoadContext,SAABGeography);
		
		//Network de conexiones
		JunctionsContext = new JunctionContext();
		SAABContext.addSubContext(JunctionsContext);
		
		NetworkBuilder<Junction> NetJunctionBuilder	=new NetworkBuilder<Junction>(VariablesGlobales.NETWORK_RUTAS,JunctionsContext,false);
		NetJunctionBuilder.setEdgeCreator(new DefaultEdgeCreator<Junction>());
		RoadNetwork = NetJunctionBuilder.buildNetwork();
		
		creaRutasNetwork(RoadContext, JunctionsContext, RoadNetwork);		
		
		/*
		 * TODO validar la configuracion.
		 * area_plaza, municipio, urbano con actividades y productos viables
		 */		
		crearRedSaab();
		
		return context;
		
	}
	
	
	/**
	 * Crea la proyección <code>Network</code> de las rutas usando dos archivos <code>shapefile</code>, uno con las vias y otro con los nodos de union entre las vias. 
	 * Este algoritmo esta basado en métodos de la clase GISFunctions del proyecto RepastCity3.
	 * 
	 * @author Nick Malleson. 
	 *  
	 */
	private void creaRutasNetwork(Context<Object> rutasContext, Context<Junction> junctionContext, Network<Junction> roadNetwork){
		
		IndexedIterable<Object> rutas =rutasContext.getObjects(ViaTransitable.class);
		// Create a cache of all Junctions and coordinates so we know if a junction has already been created at a
		// particular coordinate
		this.coordMap = new HashMap<Coordinate, Junction>();
		DataCollector textWriter = new DataCollector();
		
		
		//Por cada viaTransitable se crean las conexiones (junctions) y su respectivo vertice (Edge)
		for(Object v: rutas){
			
			ViaTransitable via 	= (ViaTransitable)v;
			Geometry viageom	= via.getGeometria();	
			
			Coordinate c1 = viageom.getCoordinates()[0];// First coord
			Coordinate c2 = viageom.getCoordinates()[viageom.getNumPoints()-1];// Last coord
			
			// Create Junctions from these coordinates and add them to the JunctionGeography (if they haven't been
			// created already)
			Junction junc1, junc2;
			if (coordMap.containsKey(c1)) {
				// A Junction with those coordinates (c1) has been created, get it so we can add an edge to it
				junc1 = coordMap.get(c1);
			} else { // Junction does not exit
				junc1 = new Junction();
				junc1.setCoords(c1);
				junctionContext.add(junc1);
				coordMap.put(c1, junc1);
			}
			if (coordMap.containsKey(c2)) {
				junc2 = coordMap.get(c2);
			} else { // Junction does not exit
				junc2 = new Junction();
				junc2.setCoords(c2);
				junctionContext.add(junc2);
				coordMap.put(c2, junc2);				
			}
			
			// Tell the road object who it's junctions are
			via.addJunction(junc1);
			via.addJunction(junc2);
			// Tell the junctions about this road
			junc1.addRoad(via);
			junc2.addRoad(via);
			
			// Create an edge between the two junctions, assigning a weight equal to it's length			
			//NetworkEdge<Junction> edge = new NetworkEdge<Junction>(junc1, junc2, false, (viageom.getLength()*(VariablesGlobales.MULTIPLICADOR_CONVERSION_RAD_TO_METERS))*(via.getMultiplicador()/100));
			NetworkEdge<Junction> edge = new NetworkEdge<Junction>(junc1, junc2, false, ((viageom.getLength()*(VariablesGlobales.MULTIPLICADOR_CONVERSION_RAD_TO_METERS))));
			textWriter.escribeDatos("freezedried_data/coordMap.txt","\n"+junc1.getCoords().toString()+" edge "+junc2.getCoords().toString());
			// Tell the Road and the Edge about each other
			via.setEdge(edge);
			edge.setRoad(via);
			
			if (!roadNetwork.containsEdge(edge)) {
				roadNetwork.addEdge(edge);
			}
			
		}//for each
		
	}
	
	/**
	 * Carga las formas geometricas de las proyecciones desde archivos <code>ESRI-Shapefile</code>
	 * 
	 * @param filename: archivo a cargar
	 * @param tipoObjeto: Tipo de obejeto que se creara
	 * @param context: contexto donde se crean los objetos
	 * @param geography: referencia a la geografia SAAB
	 */
	private void cargarShapeFiles(String filename, String tipoObjeto, Context<Object> context, Geography<Object> geography){
		
		URL url 						= null;
		SimpleFeatureIterator fiter 	= null;
		ShapefileDataStore store 		= null;
		CoordinateReferenceSystem crs 	= null;
		
		Parameters params = RunEnvironment.getInstance().getParameters();
		
		int numeroConsumidores			= (Integer)params.getValue("num_consumidores");
		int numeroVendedoresFinales 	= (Integer)params.getValue("num_vendfinales");		
		int numeroProductores		 	= (Integer)params.getValue("num_productores");
		int numeroIntermediarios	 	= (Integer)params.getValue("num_intermediarios");
		
		//MPAs a incluir
		boolean producirCebolla_flag	= params.getBoolean("producirSisaab");
		boolean producirCebolla2_flag	= params.getBoolean("producirVentaLocal");
		boolean registrarOferta_flag	= params.getBoolean("registrarOferta");
		boolean registrarDemanda_flag	= params.getBoolean("registrarDemanda");
		boolean consolidarDemanda_flag	= params.getBoolean("consolidarDemanda");
		boolean comprarCorabastos_flag	= params.getBoolean("comprarCorabastos");
		boolean comprarLocalmente_flag	= params.getBoolean("comprarLocalmente");
				
				
		try{
			
			url = new File(filename).toURI().toURL();
			store = new ShapefileDataStore(url);
			
		}catch(MalformedURLException e){
			e.printStackTrace();
		}
		
		try{
			crs = store.getSchema().getCoordinateReferenceSystem();
			fiter = store.getFeatureSource().getFeatures().features();
			
		}catch(IOException e1){
			e1.printStackTrace();
		}
		
		while(fiter.hasNext()){
			
			SimpleFeature feature 	= fiter.next();
			Geometry geom 			= (Geometry)feature.getDefaultGeometry();			
			
			String id				=null;
			String name				=null;
			String tipo				=null;
			//int pesoJunction		=0;
			
			AmbienteLocal region	=null;
			CentroUrbano pueblo		=null;
			NodoSaab nodoSaab		=null;			
			PlazaDistrital plaza	=null;
			ViaTransitable via		=null;
			
			
			if (geom instanceof MultiPolygon){
				
				MultiPolygon mp	=(MultiPolygon)feature.getDefaultGeometry();
				geom 			=(Polygon)mp.getGeometryN(0);				
			}
			else if(geom instanceof Point){
				
				geom =(Point)feature.getDefaultGeometry();
				
			}
			else if(geom instanceof MultiLineString){
				
				MultiLineString linea	=(MultiLineString)feature.getDefaultGeometry();
				geom					=(LineString)linea.getGeometryN(0);				
			}
			
			switch(tipoObjeto){
			
			case "municipios": //Cuando carga municipios	
				
				name 	=(String)feature.getAttribute("NAME_2");
				region 	= new AmbienteLocal(name);
				region.setGeometria(geom);
				
				String nodosCercanos = (String)feature.getAttribute("NL_NAME_2");
				region.setNodosCercanos(nodosCercanos);
				
				context.add(region);
				geography.move(region, geom);
				
				region.addProductoAgricolaViable(new Producto("Cebolla"));
				
				if(!name.equalsIgnoreCase("bogota")){
					
					if(producirCebolla_flag){region.addActividadViable(new ProducirCebollaBulbo());}
					if(producirCebolla2_flag){region.addActividadViable(new ProducirCebollaBulbo2());}
					if(registrarOferta_flag){region.addActividadViable(new RegistrarOfertaUnitaria());}
					
					
				}
				
				//LOGGER.log(Level.INFO, this.toString() + " Agregado municipio: "+name);
				
				break;
			case "urbano"://Cuando carga Centros urbanos					
							
				name 	=(String)feature.getAttribute("Name"); 
				pueblo 	= new CentroUrbano(name);
				pueblo.setGeometria(geom);
				pueblo.addProductoAgricolaViable(new Producto("Cebolla"));
								
				if(!name.equalsIgnoreCase("Bogota")){
					/**
					 * Cuando el centro urbano hace referencia a Bogota, no es agregado al mapa
					 * por estetica 
					 */
					context.add(pueblo);								
					
					if(comprarLocalmente_flag){pueblo.addActividadViable(new ComprarLocalmente());}
												
					geography.move(pueblo, geom);
					
					/**Agrega el centro urbano al ambiente-municipio que lo intercepta
					 * Es necesario que el shpFile de municipios haya sido cargado con anterioridad
					 */
					IndexedIterable<Object> ambientes 	= context.getObjects(AmbienteLocal.class);
					Iterator<Object> iter 				= ambientes.iterator();
					
					while(iter.hasNext()){
						
						Object mun = iter.next();
						if(!(mun instanceof CentroUrbano)){							
							
							AmbienteLocal municipio = (AmbienteLocal)mun;
							if(municipio.getGeometria().intersects(geom)){
								
								//LOGGER.log(Level.INFO," Agrega Urbe:"+pueblo.getNombre()+" a municipio:"+municipio.getNombre());
								/*
								 * Agerga productores al municipio, enlazando sus centros urbanos como puntos de oferta
								 * e intermediarios al centro urbano
								 */
								municipio.addCentroUrbano(pueblo);
								pueblo.setMunicipio(municipio);
								crearProductores(municipio, pueblo,numeroProductores,geography,context);
								crearIntermediarios(pueblo,numeroIntermediarios, geography,context);
							}
						}
					}//End While
					
				}else{
					/*
					 * Cuando el centroUrbano es Bogota, agrega los consumidores
					 */
					crearConsumidores(pueblo,numeroConsumidores,geography,context);
					this.Corabastos.setLugarOferta(pueblo);
					
					if(registrarDemanda_flag){pueblo.addActividadViable(new RegistrarDemandaUnitaria());}
					if(consolidarDemanda_flag){pueblo.addActividadViable(new ConsolidarDemanda());}
					if(comprarCorabastos_flag){pueblo.addActividadViable(new ComprarEnCorabastos());}
				}
				
				break;
			case "urbano_access"://Cuando carga puntos de navegacion desde un shapefile		
								
				name 			= (String)feature.getAttribute("name");				
				Coordinate p1 	= geom.getCoordinates()[0];// First coord
				Coordinate p2 	= geom.getCoordinates()[geom.getNumPoints()-1];// Last coord
				
				IndexedIterable<Object> pbls 	= context.getObjects(CentroUrbano.class);
				Iterator<Object> iter 			= pbls.iterator();
				
				while(iter.hasNext()){
					
					Object obj	= iter.next();					
					if(obj instanceof CentroUrbano){							
						
						CentroUrbano pbl 	= (CentroUrbano)obj;
						LOGGER.log(Level.INFO," compara "+pbl.getNombre()+"equalsIgnoreCase "+name);
						if(pbl.getNombre().equalsIgnoreCase(name)){
							
							if(p1.distance(pbl.getCentroid().getCoordinate())<p2.distance(pbl.getCentroid().getCoordinate())){
								pbl.setRoadAccess(p1);
								LOGGER.log(Level.INFO,pbl.getNombre()+" agregado acceso: "+p1.toString());
							}else{
								pbl.setRoadAccess(p2);
								LOGGER.log(Level.INFO,pbl.getNombre()+" agregado acceso: "+p2.toString());
							}
						}
												
					}
				}//End While
				
				break;
			case "plazas_access"://Cuando carga puntos de navegacion desde un shapefile		
				
				name 			= (String)feature.getAttribute("name");				
				Coordinate pl1 	= geom.getCoordinates()[0];// First coord
				Coordinate pl2 	= geom.getCoordinates()[geom.getNumPoints()-1];// Last coord
				
				IndexedIterable<Object> plzs 	= context.getObjects(PlazaDistrital.class);
				Iterator<Object> pl_iter 		= plzs.iterator();
				
				while(pl_iter.hasNext()){
					
					Object obj	= pl_iter.next();					
					if(obj instanceof PlazaDistrital){							
						
						PlazaDistrital plz 	= (PlazaDistrital)obj;
						LOGGER.log(Level.INFO," compara "+plz.getNombre()+"equalsIgnoreCase "+name);
						if(plz.getNombre().equalsIgnoreCase(name)){
							
							if(pl1.distance(plz.getCentroid().getCoordinate())<pl2.distance(plz.getCentroid().getCoordinate())){
								plz.setRoadAccess(pl1);
								LOGGER.log(Level.INFO,plz.getNombre()+" agregado acceso: "+pl1.toString());
							}else{
								plz.setRoadAccess(pl2);
								LOGGER.log(Level.INFO,plz.getNombre()+" agregado acceso: "+pl2.toString());
							}
						}
												
					}
				}//End While
				
				break;
			case "nodo_access"://Cuando carga puntos de navegacion desde un shapefile		
				
				name 			= (String)feature.getAttribute("name");				
				Coordinate nd1 	= geom.getCoordinates()[0];// First coord
				Coordinate nd2 	= geom.getCoordinates()[geom.getNumPoints()-1];// Last coord
				
				IndexedIterable<Object> nodos 	= context.getObjects(NodoSaab.class);
				
				for(Object o: nodos){
															
					if(o instanceof NodoSaab){							
						
						NodoSaab nd 	= (NodoSaab)o;
						
						if(nd.getNombre().equalsIgnoreCase(name)){
						
							if(nd1.distance(nd.getCentroid().getCoordinate())<nd2.distance(nd.getCentroid().getCoordinate())){
								nd.setRoadAccess(nd1);
								LOGGER.log(Level.INFO,nd.getNombre()+" agregado acceso: "+nd1.toString());
							}else{
								nd.setRoadAccess(nd2);
								LOGGER.log(Level.INFO,nd.getNombre()+" agregado acceso: "+nd2.toString());
							}						
						}												
					}
				}//End For
				
				break;
			case "via"://Cuando carga rutas, calles y vías		
												
				id		=(String)feature.getAttribute("identifier");
				name 	=(String)feature.getAttribute("name");
				tipo 	=(String)feature.getAttribute("type");
				
				via = new ViaTransitable();
				via.setIdentificador(id);
				via.setNombre(name);
				via.setTipo(tipo);
				via.setGeografia(geography);
				via.setGeometria(geom);
				
				context.add(via);
				geography.move(via, geom);			
				
				break;
			case "nodos": //Cuando carga Nodos SAAB
				
				name =(String)feature.getAttribute("Name");
				
				if(name.equalsIgnoreCase("Corabastos")){
					Corabastos 		= new Corabastos(name,geom);
					context.add(Corabastos);
					geography.move(Corabastos, geom);
				}else{
					nodoSaab = new NodoSaab(name);
					nodoSaab.setGeometria(geom);
					context.add(nodoSaab);
					geography.move(nodoSaab, geom);
				}	
				
				break;
			case "plazas"://cuando carga Plazas Distritales
				
				name 	=(String)feature.getAttribute("Name");
				plaza 	=new PlazaDistrital(name);
				
				plaza.setGeometria(geom);
				
				
				context.add(plaza);
				geography.move(plaza, geom);			
				
				break;
			case "area_plaza"://cuando carga diagrama vornoi de Plazas Distritales
				
				name 	=(String)feature.getAttribute("Name");
				region 	= new AmbienteLocal(name);
				region.setGeometria(geom);
				region.addProductoAgricolaViable(new Producto("Cebolla"));
				
				if(registrarDemanda_flag){region.addActividadViable(new RegistrarDemandaUnitaria());}
				if(consolidarDemanda_flag){region.addActividadViable(new ConsolidarDemanda());}
				if(comprarCorabastos_flag){region.addActividadViable(new ComprarEnCorabastos());}
				
				/**
				 * Agrega tenderos dentro del area de correspondencia de cada plaza distrital
				 * Es necesario que las geometrias de Plazas Distritales haya sido cargadas
				 * con anterioridad.
				 */
				IndexedIterable<Object> plazas = context.getObjects(PlazaDistrital.class);
				for(int j=0; j<plazas.size(); j++){
					
					PlazaDistrital pl = (PlazaDistrital)plazas.get(j);
					if(pl.getNombre().equalsIgnoreCase(name)){
						/**
						 * Crea agentes "vendedores finales" por cada plaza distrital para dejarla enlazada como punto de demanda
						 */
						crearTenderos(region,pl,numeroVendedoresFinales,geography,context);
						break;
					}
				}				
				
				break;
			}			
		}
		
		//Ciera los archivos
		fiter.close();
		store.dispose();
		
	}
	/**
	 * Asocia los nodos logísticos a los municipios y centros urbanos según su ubicación geográfica
	 */
	private void crearRedSaab(){
		
		/*
		 * Itera los AmbientesLocales y agrega los nodosSaab cercanos 
		 */
		IndexedIterable<Object> ambientesLocales 	= this.SAABContext.getObjects(AmbienteLocal.class);
		Iterator<Object> nodos 						= this.SAABContext.getObjects(NodoSaab.class).iterator();
		
		while(nodos.hasNext()){
			
			NodoSaab nodo		= (NodoSaab)nodos.next();
			String NombreNodo 	= nodo.getNombre();
			
			for(Object o: ambientesLocales){
				
				if(!(o instanceof CentroUrbano)){
					
					AmbienteLocal amb	 = (AmbienteLocal)o;
					String nodosCercanos = amb.getNodosCercanos();
					//TODO comparar substrigs
					if(nodosCercanos.equalsIgnoreCase(NombreNodo)){
						amb.addNodoSaab(nodo);
					}
				}
			}			
		}//EndWhile
		
		/*
		 * Itera los CentrosUrbanos y agrega el nodo del Ambiente que lo contiene
		 */
		Iterator<Object> centrosUrbanos 	= this.SAABContext.getObjects(CentroUrbano.class).iterator();
		
		while(centrosUrbanos.hasNext()){
			
			CentroUrbano u = (CentroUrbano)centrosUrbanos.next();
			
			for(Object o: ambientesLocales){
				
				if(!(o instanceof CentroUrbano)){
					
					AmbienteLocal amb = (AmbienteLocal)o;
					if(amb.getGeometria().contains(u.getGeometria())){
						u.setNodosSaab(amb.getNodosSaab());			
					}else if(amb.getNombre().equalsIgnoreCase(u.getNombre())){
						u.setNodosSaab(amb.getNodosSaab());
					}
							
				}
			}
			LOGGER.log(Level.INFO,u.getNombre()+" agregado nodo: "+u.getNodosSaab().size());		
		}//EndWhile
		
		
		/**
		 * Prueba de camion
		 */		
		/*Iterator<Object> pueblos 	= SAABContext.getObjects(CentroUrbano.class).iterator();
		Iterator<Object> nodosSaab 	= SAABContext.getObjects(NodoSaab.class).iterator();
				
		NodoSaab nodo1 		= (NodoSaab)nodosSaab.next();
		CentroUrbano pueblo1 = (CentroUrbano)pueblos.next();
						
		Camion transporte = new Camion();
		GeometryFactory geofact = new GeometryFactory();
			
		System.out.println("nodo1: "+nodo1.getNombre());
				
		Point geom = geofact.createPoint(new Coordinate(nodo1.getRoadAccess().x,nodo1.getRoadAccess().y));
		transporte.setGeometria(geom);
			
		RecolectarProductos actividad = new RecolectarProductos(nodo1,pueblo1,new ArrayList()); 
		transporte.setActividadVigente(actividad.getInstance());
		SISAABContext.add(transporte);
		SAABGeography.move(transporte, geom);	*/	
		
		/**
		 * Prueba consumer
		 */
		/*Iterator<Object> tiendas 	= SAABContext.getObjects(Tienda.class).iterator();
		Tienda tienda1 				= (Tienda)tiendas.next();
		
		Consumidor agente = new Consumidor();
		GeometryFactory geofact = new GeometryFactory();
						
		Point geom = geofact.createPoint(new Coordinate(tienda1.getGeometria().getCoordinate().x+0.005,tienda1.getGeometria().getCoordinate().y+0.005));
		agente.setGeometria(geom);
		
		this.BogotaContext.add(agente);
		SAABGeography.move(agente, geom);
		/*
		 * Agrega consumidores a la ciudad
		 */
		//crearConsumidores(pueblo,1,geography,context);
		
		/**
		 * Prueba Intermediario
		 */
		/*Iterator<Object> pueblos 	= SAABContext.getObjects(CentroUrbano.class).iterator();
		CentroUrbano pueblo1 		= (CentroUrbano)pueblos.next();
		Coordinate puebloCoord 		= pueblo1.getCentroid().getCoordinate();
		Coordinate AgentCoord		= new Coordinate(new Double(puebloCoord.x).doubleValue(), new Double(puebloCoord.y).doubleValue());
		
		
		GeometryFactory geofact 			= new GeometryFactory();
		Intermediario agenteIntermediario	= new Intermediario(pueblo1);
		Productor agenteProductor			= new Productor();
		
		
				
		Point geomIntermediario	= geofact.createPoint(AgentCoord);
		Point geomProductor		= geofact.createPoint(puebloCoord);				
		agenteIntermediario.setGeometria(geomIntermediario);		
		
		this.RuralContext.add(agenteIntermediario);
		this.SAABGeography.move(agenteIntermediario, geomIntermediario);
		
		
		Point terrenogeom 		= geofact.createPoint(puebloCoord);
		int hectareas			= RandomHelper.nextIntFromTo(1, 11);//de una a 11 hectareas en promedio por UP
		Terreno terreno			= new Terreno(hectareas,terrenogeom);
		
		
		this.RuralContext.add(agenteProductor);
		this.RuralContext.add(terreno);
						
		this.SAABGeography.move(agenteProductor, geomProductor);
		this.SAABGeography.move(terreno, terrenogeom);
						
		agenteProductor.addTerrenos(terreno);
		agenteProductor.setPuntoOferta(pueblo1);
		
		terreno.setAmbiente(pueblo1.getMunicipio());*/
		
	}
	
	/**
	 * Crea y ubica los productores aleatoriamente en el contexto rural, usando el centroide de la region y sus vertices para acotar el sector.
	 * @param amb Ambiente local al que agregan los agentes
	 * @param cantidad Numero de agentes a agregar
	 * @param geography Geografia a la que se agregan los agentes
	 */
	private void crearProductores(GeografiaFija amb, CentroUrbano puntoOferta, int cantidad, Geography<Object> geography, Context<Object> contexto){	
				
		/**
		 * Obtiene el centroide de la region y las coordenadas de los vertices del ambiente, 
		 * luego genera coordenadas aleatorias que van desde el centroide a cualquiera de los
		 * vertices del ambiente.
		 */
		Coordinate[] coords = amb.getGeometria().getCoordinates();		
		Coordinate center 	= amb.getGeometria().getCentroid().getCoordinate();
		
		
		for(int i=0; i<cantidad; i++){
			
			int idAgente 			= getIdentificadorAgente();			
			GeometryFactory geofact = new GeometryFactory();
			Productor productor		= new Productor(idAgente);
			productor.OBSERVABLE.addObserver(OBSERVADOR);
			
			
			Coordinate AgentCoord 	= new Coordinate(RandomHelper.nextDoubleFromTo(center.x,coords[RandomHelper.nextIntFromTo(0, coords.length-1)].x),RandomHelper.nextDoubleFromTo(center.y,coords[RandomHelper.nextIntFromTo(0, coords.length-1)].y));//center;			
			Point geom 				= geofact.createPoint(AgentCoord);
			
			if(amb.getGeometria().intersects(geom.getGeometryN(0))){//Si esta dentro de la geometria del ambiente
				
				Coordinate TerrCoord 	= new Coordinate(RandomHelper.nextDoubleFromTo(center.x,coords[RandomHelper.nextIntFromTo(0, coords.length-1)].x),RandomHelper.nextDoubleFromTo(center.y,coords[RandomHelper.nextIntFromTo(0, coords.length-1)].y));//center;
				Point terrenogeom 		= geofact.createPoint(TerrCoord);
				int hectareas			= RandomHelper.nextIntFromTo(1, 11);//de una a 11 hectareas en promedio por UP
				Terreno terreno			= new Terreno(hectareas,terrenogeom);
				
				contexto.add(productor);
				contexto.add(terreno);
				
				geography.move(productor, geom);
				geography.move(terreno, terreno.getGeometria());			
								
				productor.addTerrenos(terreno);
				productor.setPuntoOferta(puntoOferta);
				
				terreno.setAmbiente((AmbienteLocal)amb);				
				
			}
			
		}
		
		
	}

	/**
	 * Crea y ubica los vendedores finales aleatoriamente en el contexto rural, usando el centroide de la region y sus vertices para acotar el sector
	 * <p>
	 * A cada vendedor le asigna una cantidad aleatoria de dinero
	 * @param amb
	 * @param cantidad
	 * @param geography
	 * @param contexto
	 */
	private void crearTenderos(AmbienteLocal amb, PlazaDistrital plaza, int cantidad, Geography<Object> geography, Context<Object> contexto){			
		
		/**
		 * Obtiene el centroide de la region y las coordenadas de los vertices del ambiente, 
		 * luego genera coordenadas aleatorias que van desde el centroide a cualquiera de los
		 * vertices del ambiente.
		 */
		Coordinate[] coords = amb.getGeometria().getCoordinates();		
		Coordinate center 	= amb.getGeometria().getCentroid().getCoordinate();		
		
		for(int i=0; i<cantidad; i++){
			
			int identificador 		= getIdentificadorAgente();
			GeometryFactory geofact = new GeometryFactory();			
			VendedorFinal agente	= new VendedorFinal(identificador);	
			
			Coordinate AgentCoord 	= new Coordinate(RandomHelper.nextDoubleFromTo(center.x,coords[RandomHelper.nextIntFromTo(0, coords.length-1)].x),RandomHelper.nextDoubleFromTo(center.y,coords[RandomHelper.nextIntFromTo(0, coords.length-1)].y));//center;			
			Point geom 				= geofact.createPoint(AgentCoord);
			
			if(amb.getGeometria().intersects(geom.getGeometryN(0))){
				
				Point tiendageom 		= geofact.createPoint((Coordinate)AgentCoord.clone());
				Tienda tienda			= new Tienda(tiendageom);
				
				agente.addTienda(tienda);
				agente.addDinero(RandomHelper.nextDoubleFromTo(VariablesGlobales.MONTO_MINIMO_DINERO_INICIAL,VariablesGlobales.MONTO_MAXIMO_DINERO_INICIAL));
				agente.setPuntoDemanda(plaza);
				
				contexto.add(agente);
				contexto.add(tienda);
				
				tienda.setAmbiente(amb);
				tienda.setPropietario(agente);
				
				geography.move(agente, geom);
				geography.move(tienda, tiendageom);			
				
				//LOGGER.log(Level.INFO, this.toString() + "Vendedor inside ");
			}			
		}//EndFor	
	}	
	
	
	/**
	 * Crea y ubica agentes consumidores aleatoriamente en el contexto pasado como parametro (Bogota), usando el centroide de la region y sus vertices para acotar el sector
	 * 
	 * @param amb
	 * @param cantidad
	 * @param geography
	 * @param contexto
	 */
	private void crearConsumidores(CentroUrbano ciudad, int cantidad, Geography<Object> geography, Context<Object> contexto){//num_consumidores
		/**
		 * Obtiene el centroide de la region y las coordenadas de los vertices del ambiente, 
		 * luego genera coordenadas aleatorias que van desde el centroide a cualquiera de los
		 * vertices del ambiente.
		 */
		Coordinate[] coords = ciudad.getGeometria().getCoordinates();		
		Coordinate center 	= ciudad.getGeometria().getCentroid().getCoordinate();		
		
		for(int i=0; i<cantidad; i++){
			
			GeometryFactory geofact = new GeometryFactory();
			Consumidor agente		= new Consumidor();	
			
			Coordinate AgentCoord 	= new Coordinate(RandomHelper.nextDoubleFromTo(center.x,coords[RandomHelper.nextIntFromTo(0, coords.length-1)].x),RandomHelper.nextDoubleFromTo(center.y,coords[RandomHelper.nextIntFromTo(0, coords.length-1)].y));//center;			
			Point geom 				= geofact.createPoint(AgentCoord);
			agente.setGeometria(geom);
			
			if(ciudad.getGeometria().intersects(geom.getGeometryN(0))){
				contexto.add(agente);
				geography.move(agente, geom);
			}
		}//EndFor
	}
	
	/**
	 * Crea y ubica agentes Intermediarios aleatoriamente en el contexto pasado como parametro (rural), usando el centroide de la region y sus vertices para acotar el sector
	 * 
	 * @param amb
	 * @param cantidad
	 * @param geography
	 * @param contexto
	 */
	private void crearIntermediarios(CentroUrbano pueblo, int cantidad, Geography<Object> geography, Context<Object> contexto){
		/**
		 * Obtiene el centroide de la region y las coordenadas de los vertices del ambiente, 
		 * luego genera coordenadas aleatorias que van desde el centroide a cualquiera de los
		 * vertices del ambiente.
		 */
		Coordinate[] coords = pueblo.getGeometria().getCoordinates();		
		Coordinate center 	= pueblo.getGeometria().getCentroid().getCoordinate();		
		
		for(int i=0; i<cantidad; i++){
			
			int identificador		= getIdentificadorAgente();
			GeometryFactory geofact = new GeometryFactory();
			Intermediario agente	= new Intermediario(identificador);	
			
			Coordinate AgentCoord 	= new Coordinate(RandomHelper.nextDoubleFromTo(center.x,coords[RandomHelper.nextIntFromTo(0, coords.length-1)].x),RandomHelper.nextDoubleFromTo(center.y,coords[RandomHelper.nextIntFromTo(0, coords.length-1)].y));//center;			
			Point geom 				= geofact.createPoint(AgentCoord);
			
			if(pueblo.getGeometria().contains(geom.getGeometryN(0))){
				agente.setGeometria(geom);
				agente.setAmbienteLocal(pueblo);
				contexto.add(agente);
				geography.move(agente, geom);
			}
		}//EndFor
	}
	
	/**
	 * Devuelve el identificador consecutivo para ser asignado a un agente creado en la simulación 
	 * @return identificador int
	 */
	public static int getIdentificadorAgente(){
		
		int identificador = IdentificadorAgentes+1;
		IdentificadorAgentes++;
		return identificador;
	}

}
