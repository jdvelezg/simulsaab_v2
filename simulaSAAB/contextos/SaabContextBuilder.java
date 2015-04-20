package simulaSAAB.contextos;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
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

import simulaSAAB.agentes.Productor;
import simulaSAAB.agentes.Terreno;
import simulaSAAB.agentes.Tienda;
import simulaSAAB.agentes.VendedorFinal;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.global.SimulaSAABLogging;
import simulaSAAB.global.VariablesGlobales;
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
import repast.simphony.space.graph.DefaultEdgeCreator;
import repast.simphony.space.graph.Network;
import repast.simphony.util.collections.IndexedIterable;

public class SaabContextBuilder implements ContextBuilder<Object> {
	
	private static Logger LOGGER = Logger.getLogger(SaabContextBuilder.class.getName());
	
	//Contexto principal
	public static Context<Object> SAABContext;
	public static Geography<Object> SAABGeography;

	
	//Contexto Rural
	public static Context<Object> RuralContext;	
	
	//Contexto Distrital
	public static Context<Object> BogotaContext;
	
	//SISAAB Context
	public static Context<Object> SISAABContext;
	public static Network<Object> SISAABNetwork;
	
	public static Context<Object> ComercialContext;
	public static Network<Object> ComercialNetwork;
	public static Context<Object> OrdenesContext;
	
	//Vias Context
	public static Context<Object> JunctionsContext;
	public static Network<Object> JunctionsNetwork;
	
	
	
	

	@Override
	public Context<Object> build(Context<Object> context) {
		
		//Inicializa el log general
		//SimulaSAABLogging.init();
		
		//Referencia al contexto principal
		SAABContext =context;		
		SAABContext.setId(VariablesGlobales.CONTEXTO_SAAB);
		
		GeographyParameters<Object>	geoparams 	= new GeographyParameters<Object>();		
		SAABGeography	= GeographyFactoryFinder.createGeographyFactory(null).createGeography(VariablesGlobales.GEOGRAFIA_SAAB, SAABContext, geoparams);
		
		//Contexto Rural
		RuralContext	=new RuralContext();
		SAABContext.addSubContext(RuralContext);
		
		cargarShapeFiles(VariablesGlobales.MUNICIPIOS_SHAPEFILE,"municipios",RuralContext,SAABGeography);
		
		//Contexto Distrital
		BogotaContext =new BogotaContext();
		SAABContext.addSubContext(BogotaContext);
		
		cargarShapeFiles(VariablesGlobales.BOGOTA_SHAPEFILE,"urbano",BogotaContext,SAABGeography);
		
		//Contextos SISaab
		SISAABContext = new SISaabContext();
		SAABContext.addSubContext(SISAABContext);
		
		ComercialContext = new DefaultContext<Object>(VariablesGlobales.CONTEXTO_COMERCIAL);
		SISAABContext.addSubContext(ComercialContext);
		
		NetworkBuilder<Object> NetComercialBuilder	=new NetworkBuilder<Object>(VariablesGlobales.NETWORK_COMERCIAL,ComercialContext,false);
		NetComercialBuilder.setEdgeCreator(new DefaultEdgeCreator<Object>());
		ComercialNetwork = NetComercialBuilder.buildNetwork();		
		
		OrdenesContext = new DefaultContext(VariablesGlobales.CONTEXTO_ORDENES);
		SISAABContext.addSubContext(OrdenesContext);
		
		//Contextos de rutas
		JunctionsContext = new RoadContext();
		SAABContext.addSubContext(JunctionsContext);
		
		NetworkBuilder<Object> NetJunctionBuilder	=new NetworkBuilder<Object>(VariablesGlobales.NETWORK_RUTAS,JunctionsContext,false);
		NetJunctionBuilder.setEdgeCreator(new DefaultEdgeCreator<Object>());
		JunctionsNetwork = NetJunctionBuilder.buildNetwork();
		
		cargarShapeFiles(VariablesGlobales.CONEXIONES_SHAPEFILE,"junction",JunctionsContext,SAABGeography);
		
		return context;
		
	}
	
	
	/**
	 * Carga las formas geometricas de las proyecciones desde archivos ESRI-Shapefile
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
		
		int numeroVendedoresFinales 	= (Integer)params.getValue("num_vendfinales");		
		int numeroProductores		 	= (Integer)params.getValue("num_productores");
				
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
			
			String name				=null;
			String tipojunction		=null;
			int pesoJunction		=0;
			
			AmbienteLocal region	=null;
			CentroUrbano pueblo		=null;
			NodoSaab nodoSaab		=null;			
			PlazaDistrital plaza	=null;
			Junction junction		=null;
			
			
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
				
				context.add(region);
				geography.move(region, geom);
				
				region.addProductoAgricolaViable(new Producto("Cebolla"));
				
				if(!name.equalsIgnoreCase("bogota")){
					//region.addActividadViable(new ProducirCebollaBulbo());
					//region.addActividadViable(new ProducirCebolla());
					crearProductores(region,numeroProductores,geography,context);
				}		
				
				break;
			case "urbano"://Cuando carga Centros urbanos					
							
				name 	=(String)feature.getAttribute("Name"); 
				pueblo 	= new CentroUrbano(name);
				pueblo.setGeometria(geom);
				
				context.add(pueblo);
				geography.move(pueblo, geom);
				
				
				//pueblo.addProductoAgricolaViable(new Producto("Cebolla"));
				//pueblo.addActividadViable(new VenderCebollaBulbo());
				//pueblo.addActividadViable(new VenderProductosEnBogota());				
				
				if(name.equalsIgnoreCase("Bogota")){
					//crearTenderos(pueblo,numeroVendedoresFinales,geography,context);
					//pueblo.addActividadViable(new ComprarProductos());
				}
				
				/*
				//Agrega el centro urbano al ambiente-municipio que lo intercepta
				IndexedIterable ambientes 	= RuralContext.getObjects(AmbienteLocal.class);
				Iterator iter 				= ambientes.iterator();
				while(iter.hasNext()){
					AmbienteLocal municipio = (AmbienteLocal)iter.next();
					if(municipio.getGeometria().intersects(pueblo.getGeometria())){
						municipio.addCentroUrbano(pueblo);
						//System.out.println("Agrega Urbe:"+pueblo.getNombre()+" a municipio:"+municipio.getNombre());
					}else{
						//System.out.println("urbe "+pueblo.getNombre()+" no queda en municipio:"+municipio.getNombre());
					}						
				}	*/
				
				break;
			case "junction"://Cuando carga puntos de navegacion				
								
				name 			=(String)feature.getAttribute("Name");
				tipojunction 	=(String)feature.getAttribute("TYPE");
				pesoJunction	=(Integer)feature.getAttribute("weight");
				
				junction = new Junction(name, tipojunction);
				junction.setWeight(pesoJunction);
				junction.setGeometria(geom);				
								
				context.add(junction);
				geography.move(junction, geom);
				
				break;
			case "nodos": //Cuando carga Nodos
				
				name =(String)feature.getAttribute("Name"); 
				nodoSaab = new NodoSaab(name);
				nodoSaab.setGeometria(geom); 
				
				context.add(nodoSaab);
				geography.move(nodoSaab, geom);
				
				break;
			case "plazas"://cuando carga Plazas
				
				name 	=(String)feature.getAttribute("Name");
				plaza 	=new PlazaDistrital(name);
				
				plaza.setGeometria(geom);
				
				context.add(plaza);
				geography.move(plaza, geom);
				
				break;			
			}			
		}
		
		//Ciera los archivos
		fiter.close();
		store.dispose();
		
	}
	
	private void crearRedSaab(){
		
	}
	
	/**
	 * Crea y ubica los productores aleatoriamente en el contexto rural, usando el centroide de la region y sus vertices
	 * para acotar el sector.
	 * @param amb Ambiente local al que agregan los agentes
	 * @param cantidad Numero de agentes a agregar
	 * @param geography Geografia a la que se agregan los agentes
	 */
	private void crearProductores(GeografiaFija amb, int cantidad, Geography<Object> geography, Context<Object> contexto){	
				
		/**
		 * Obtiene el centroide de la region y las coordenadas de los vertices del ambiente, 
		 * luego genera coordenadas aleatorias que van desde el centroide a cualquiera de los
		 * vertices del ambiente.
		 */
		Coordinate[] coords = amb.getGeometria().getCoordinates();		
		Coordinate center 	= amb.getGeometria().getCentroid().getCoordinate();
		
		
		for(int i=0; i<cantidad; i++){
			
			GeometryFactory geofact = new GeometryFactory();
			Productor productor		= new Productor(); 
			
			
			Coordinate AgentCoord 	= new Coordinate(RandomHelper.nextDoubleFromTo(center.x,coords[RandomHelper.nextIntFromTo(0, coords.length-1)].x),RandomHelper.nextDoubleFromTo(center.y,coords[RandomHelper.nextIntFromTo(0, coords.length-1)].y));//center;			
			Point geom 				= geofact.createPoint(AgentCoord);
			
			if(amb.getGeometria().intersects(geom.getGeometryN(0))){
				
				Coordinate TerrCoord 	= new Coordinate(RandomHelper.nextDoubleFromTo(center.x,coords[RandomHelper.nextIntFromTo(0, coords.length-1)].x),RandomHelper.nextDoubleFromTo(center.y,coords[RandomHelper.nextIntFromTo(0, coords.length-1)].y));//center;
				Point terrenogeom 		= geofact.createPoint(TerrCoord);
				int hectareas			= RandomHelper.nextIntFromTo(1, 11);//de una a 11 hectareas en promedio por UP
				Terreno terreno			= new Terreno(hectareas,terrenogeom);
				
				contexto.add(productor);
				contexto.add(terreno);
				
				geography.move(productor, geom);
				geography.move(terreno, terreno.getGeometria());			
				
				//productor.setGis(geography);
				//productor.setGeometria(geom);
				productor.addTerrenos(terreno);
				
				terreno.setAmbiente((AmbienteLocal)amb);
				
				/*System.out.println("Productor inside");
				if(geom.equals(terrenogeom))
					System.out.println("terreno equal");*/
				
			}
			
		}
		
		
	}

	/**
	 * Crea y ubica los vendedores finales aleatoriamente en el contexto rural, usando el centroide
	 * de la region y sus vertices para acotar el sector.
	 * @param amb
	 * @param cantidad
	 * @param geography
	 * @param contexto
	 */
	private void crearTenderos(GeografiaFija amb, int cantidad, Geography<Object> geography, Context<Object> contexto){
		
			
		
		/**
		 * Obtiene el centroide de la region y las coordenadas de los vertices del ambiente, 
		 * luego genera coordenadas aleatorias que van desde el centroide a cualquiera de los
		 * vertices del ambiente.
		 */
		Coordinate[] coords = amb.getGeometria().getCoordinates();		
		Coordinate center 	= amb.getGeometria().getCentroid().getCoordinate();
		
		for(int i=0; i<cantidad; i++){
			
			GeometryFactory geofact = new GeometryFactory();
			VendedorFinal agente	= new VendedorFinal();	
			
			Coordinate AgentCoord 	= new Coordinate(RandomHelper.nextDoubleFromTo(center.x,coords[RandomHelper.nextIntFromTo(0, coords.length-1)].x),RandomHelper.nextDoubleFromTo(center.y,coords[RandomHelper.nextIntFromTo(0, coords.length-1)].y));//center;			
			Point geom 				= geofact.createPoint(AgentCoord);
			
			if(amb.getGeometria().intersects(geom.getGeometryN(0))){
				
				Point tiendageom 		= geofact.createPoint(AgentCoord);
				Tienda tienda			= new Tienda(tiendageom);
								
				contexto.add(agente);
				contexto.add(tienda);
				
				geography.move(agente, geom);
				geography.move(tienda, tiendageom);			
				
			}					
			
		}		
		
	}

}
