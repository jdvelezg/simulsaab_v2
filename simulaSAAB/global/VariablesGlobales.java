package simulaSAAB.global;

public abstract class VariablesGlobales {
	
	/**
	 * Variables que apuntan a la ubicación de los archivos de datos geograficos ESRI Shapefiles
	 */	
	public static final String MUNICIPIOS_SHAPEFILE			="data/municipios.shp";
	public static final String BOGOTA_SHAPEFILE				="data/bogota.shp";		
	public static final String CENTROSURBANOS_SHAPEFILE		="data/centros_urbanos.shp";
	public static final String URBANOSACCESS_SHAPEFILE		="data/centros_urbanos_access.shp";
	public static final String NODOSSAAB_SHAPEFILE			="data/nodos_saab.shp";
	public static final String NODOSACCESS_SHAPEFILE		="data/nodos_saab_access.shp";
	public static final String PLAZASDISTRITALES_SHAPEFILE	="data/plazas_distritales.shp";
	public static final String PLAZASACCESS_SHAPEFILE		="data/plazas_distritales_access.shp";	
	public static final String PLAZASVORNOI_SHAPEFILE		="data/area_plazasdistritales.shp";
	public static final String RUTAS_SHAPEFILE				="data/rutas_anillo_nodos.shp";
	
	
	/**
	 * Variables que definen los nombres de los contextos usados en la simulación.
	 */	
	public static final String CONTEXTO_SAAB 		="SAABContext";
	public static final String GEOGRAFIA_SAAB		="SAABGeography";
	
	public static final String CONTEXTO_RURAL		="RuralContext";
	public static final String CONTEXTO_DISTRITAL	="BogotaContext";
	
	public static final String CONTEXTO_SISAAB		="SISaabContext";
	public static final String CONTEXTO_COMERCIAL	="ComercialContext";
	public static final String NETWORK_COMERCIAL	="ComercialNetwork";
	public static final String CONTEXTO_TRANSACCIONES="TransactionContext";
	public static final String CONTEXTO_ORDENES		="OrderContext";
	
	public static final String CONTEXTO_RUTAS		="RoadContext";
	public static final String CONTEXTO_JUNCTIONS	="JunctionContext";
	public static final String GEOGRAFIA_RUTAS		="RoadGeography";
	public static final String NETWORK_RUTAS		="RoadNetwork";
	
	

}
