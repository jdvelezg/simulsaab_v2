package simulaSAAB.global;

import java.io.File;

/**
 * Almacena la configuración global de distintos parámetros de simulación
 * 
 * @author jdvelezg
 *
 */
public abstract class VariablesGlobales {
	
	/*
	 * Variables que apuntan a la ubicación de los archivos de datos geograficos ESRI Shapefiles
	 */	
	public static final String MUNICIPIOS_SHAPEFILE			="data"+File.pathSeparator+"municipios.shp";
	public static final String BOGOTA_SHAPEFILE				="data"+File.pathSeparator+"bogota.shp";		
	public static final String CENTROSURBANOS_SHAPEFILE		="data"+File.pathSeparator+"centros_urbanos.shp";
	public static final String URBANOSACCESS_SHAPEFILE		="data"+File.pathSeparator+"centros_urbanos_access.shp";
	public static final String NODOSSAAB_SHAPEFILE			="data"+File.pathSeparator+"nodos_saab.shp";
	public static final String NODOSACCESS_SHAPEFILE		="data"+File.pathSeparator+"nodos_saab_access.shp";
	public static final String PLAZASDISTRITALES_SHAPEFILE	="data"+File.pathSeparator+"plazas_distritales.shp";
	public static final String PLAZASACCESS_SHAPEFILE		="data"+File.pathSeparator+"plazas_distritales_access.shp";	
	public static final String PLAZASVORNOI_SHAPEFILE		="data"+File.pathSeparator+"area_plazasdistritales.shp";
	public static final String RUTAS_SHAPEFILE				="data"+File.pathSeparator+"rutas_anillo_nodos.shp";
	
	/*
	 * Variables que apuntan a la ubicación de los archivos de datos registrados
	 */
	public static final String PRODUCTORES_DATOS_GENERADOS		="freezedried_data"+File.pathSeparator+"productor_track.csv";
	public static final String OFERTAS_DATOS_GENERADOS			="freezedried_data"+File.pathSeparator+"ofertas_track.csv";
	public static final String DEMANDAS_DATOS_GENERADOS			="freezedried_data"+File.pathSeparator+"demandas_track.csv";
	public static final String ORDENCOMPRA_DATOS_GENERADOS		="freezedried_data"+File.pathSeparator+"ordencompra_track.csv";
	public static final String ORDENPEDIDO_DATOS_GENERADOS		="freezedried_data"+File.pathSeparator+"ordenpedido_track.csv";
	public static final String ORDENSERVICIO_DATOS_GENERADOS	="freezedried_data"+File.pathSeparator+"ordenservicio_track.csv";
	public static final String COMPRAS_DATOS_GENERADOS			="freezedried_data"+File.pathSeparator+"compras_track.csv";
	public static final String DECISIONES_DATOS_GENERADOS		="freezedried_data"+File.pathSeparator+"decisiones_track.csv";
	public static final String TRANSACCIONES_DATOS_GENERADOS	="freezedried_data"+File.pathSeparator+"intermediaciones_track.csv";
	public static final String EXPERIENCIA_DATOS_GENERADOS		="freezedried_data"+File.pathSeparator+"experiencias_track.csv";
	public static final String PRECIO_FINAL_DATOS_GENERADOS		="freezedried_data"+File.pathSeparator+"preciosfinales_track.csv";
	/*
	 * Variables que definen los iconos de presentacion
	 */
	public static final String TIENDA_ICON		="icons"+File.pathSeparator+"Shop_store_frontal_building_32.png";
	public static final String NODO_ICON		="icons"+File.pathSeparator+"Factory_stock_house_32.png";
	public static final String CAMION_ICON		="icons"+File.pathSeparator+"camion.png";
	public static final String CAMION_ICON2		="icons"+File.pathSeparator+"frontal_truck_32.png";
	public static final String CAMIONETA_ICON	="icons"+File.pathSeparator+"delivery_truck_24.png";
	public static final String TERRENO_ICON		="icons"+File.pathSeparator+"terreno.png";
	
	
	/*
	 * Variables que definen los nombres de los contextos usados en la simulación.
	 */	
	public static final String CONTEXTO_SAAB 		="SAABContext";
	public static final String GEOGRAFIA_SAAB		="SAABGeography";
	
	public static final String CONTEXTO_RURAL		="RuralContext";
	public static final String CONTEXTO_DISTRITAL	="BogotaContext";
	public static final String NETWORK_NUTRIREDES	="NutriredesNetwork";
	
	public static final String CONTEXTO_SISAAB		="SISaabContext";
	public static final String CONTEXTO_COMERCIAL	="ComercialContext";
	public static final String NETWORK_COMERCIAL	="ComercialNetwork";
	public static final String CONTEXTO_TRANSACCIONES="TransactionContext";
	public static final String CONTEXTO_ORDENES		="OrderContext";
	
	public static final String CONTEXTO_RUTAS		="RoadContext";
	public static final String CONTEXTO_JUNCTIONS	="JunctionContext";
	public static final String GEOGRAFIA_RUTAS		="RoadGeography";
	public static final String NETWORK_RUTAS		="RoadNetwork";
	
	/*
	 * Variables que definen rangos de busqueda en las proyecciones
	 */
	public static final Double DISTANCIA_BUSQUEDA_NUTRIRED_METROS	= new Double(0.0000000001);
	public static final Double DISTANCIA_BUSQUEDA_NUTRIRED_ANGULAR	= DISTANCIA_BUSQUEDA_NUTRIRED_METROS/(Math.PI/180)*6378137;// angular
	
	/*
	 * Variables que definen el costo de transporte y logistico
	 */
	public static final Double COSTO_PROMEDIO_TRANSPORTE_CARGA_POR_METRO	= new Double(0.5);
	public static final Double MULTIPLICADOR_CONVERSION_RAD_TO_METERS		= (Math.PI/180)*6378137;
	
	/*
	 * Variables de configuracion de espera, vigencias 
	 * 
	 * Se toma como punto de referencia 100 ticks => 1 Día
	 */
	public static final int TICKS_VIGENCIA_OFERTA					= 3000;//30dias
	public static final int TICKS_VIGENCIA_DEMANDA					= 6000;//2 meses
	public static final int TICKS_UNDIA_DEMORA_MOVIMIENTO			= 100;
	public static final int TICKS_UNASEMANA_DEMORA_MOVIMIENTO		= 500;
	public static final int TICKS_UNMES_DEMORA_MOVIMIENTO			= 3000;
	
	/*
	 * variables de Dinero y calculo de costos y precios
	 */
	public static final double MONTO_MINIMO_DINERO_INICIAL			= 1000000;//1MILLON
	public static final double MONTO_MAXIMO_DINERO_INICIAL			= 100000000;//100MILLONES
	public static final double MARGEN_GANANCIA_REQUERIDO_10			= new Double(0.1).doubleValue();//10%
	public static final double MARGEN_GANANCIA_REQUERIDO_20			= new Double(0.2).doubleValue();//20%

}
