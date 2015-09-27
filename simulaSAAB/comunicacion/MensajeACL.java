package simulaSAAB.comunicacion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import simulaSAAB.agentes.AgenteInteligente;

/**
 * Mensaje enviado entre los agentes
 * <p>
 * Sige la estructura del estandar FIPA-ACL
 * 
 * @author jdvelezg
 * 
 *
 */
public class MensajeACL {
	
	private static int SECUENCIAID = 0;
	
	private String performative;
	
	private AgenteInteligente sender;
	
	private  AgenteInteligente reply_to;
	
	private String language;
	
	private String encoding;
	
	private String ontology;
	
	private String protocol;
	
	private int conversationID;
	
	private MensajeACL reply_with;
	
	private Integer inReply_to;
	
	private Date replyBy;
	
	private List<Object> receiver;
	
	private Preposicion content;
	
	private boolean isReply;

	/**
	 * Constructor
	 */
	public MensajeACL() {
		
		conversationID = new Integer(SECUENCIAID+1);
		SECUENCIAID++;
		isReply = false;
	}
	/**
	 * Constructor
	 * @param conversation_id int, identificador de la conversación al que pertenece el mensaje
	 */
	public MensajeACL(int conversation_id){
		this.conversationID = conversation_id;
		isReply = false;
	}
	/**
	 * Constructor
	 * 
	 * @param conversation_id int, identificador de la conversación al que pertenece el mensaje
	 * @param performative string, performativa del mensaje
	 */
	public MensajeACL(int conversation_id, String performative){
		this.conversationID = conversation_id;
		this.performative	= performative;
		isReply = false;
	}
	
	/**
	 * Devuelve la performativa del mensaje
	 * @return	String Type of communicative act
	 */
	public String getPerformative() {
		return performative;
	}
	
	/**
	 * Asigna la performativa del mensaje
	 * @param performative	Type of communicative act
	 */
	public void setPerformative(String performative) {
		this.performative = performative;
	}

	/**
	 * Devuelve agente que envía el mensaje
	 * @return AgenteInteligente
	 */
	public AgenteInteligente getSender() {
		return sender;
	}

	/**
	 * Asigna agente que envía el mensaje
	 * @param sender AgenteInteligente que envía el mensaje
	 */
	public void setSender(AgenteInteligente sender) {
		this.sender = sender;
	}

	/**
	 * Devuelve el agente al que se debe responder el mensaje
	 * @return AgenteInteligente al quien responder el mensaje
	 */
	public AgenteInteligente getReply_to() {
		return reply_to;
	}

	/**
	 * Asigna el agente al que se debe responder el mensaje
	 * @param reply_to	AgenteInteligente al quien responder el mensaje
	 */
	public void setReply_to(AgenteInteligente reply_to) {
		this.reply_to = reply_to;
	}

	/**
	 * Devuelve el lenguaje del mensaje
	 * @return string
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Asigna el lenguaje del mensaje
	 * @param language string Description of Content
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * Devuelve descripción de la codificación usado en el mensaje
	 * @return string 
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * Asigna la descripción de la codificación usado en el mensaje
	 * @param encoding string, descripción de la codificación del mensaje
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * Devuelve la descripción de la ontología usada en el mensaje
	 * @return string 
	 */
	public String getOntology() {
		return ontology;
	}

	/**
	 * Asigna la descripción de la ontología usada en el mensaje
	 * @param ontology string descripción de la ontología usada en el mensaje
	 */
	public void setOntology(String ontology) {
		this.ontology = ontology;
	}

	/**
	 * Devuelve la descripción del protocolo usado en el mensaje
	 * @return string 
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * Asigna la descripción del protocolo usado en el mensaje
	 * @param protocol string descripción del protocolo usado en el mensaje
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * Devuelve el identificador de la conversación a la que pertenece el mensaje
	 * @return string 
	 */
	public int getConversationID() {
		return conversationID;
	}

	/**
	 * Asigna el identificador de la conversación a la que pertenece el mensaje
	 * @param conversationID int identificador de la conversación a la que pertenece el mensaje
	 */
	public void setConversationID(int conversationID) {
		this.conversationID = conversationID;
	}

	/**
	 * Devuelve la estructura del mensaje mediante el cual se debe responder 
	 * @return MensajeACL 
	 */
	public MensajeACL getReply_with() {
		return reply_with;
	}

	/**
	 * Asigna la estructura del mensaje mediante el cual se debe responder 
	 * @param reply_with MensajeACL, estructura del mensaje mediante el cual se debe responder
	 */
	public void setReply_with(MensajeACL reply_with) {
		this.reply_with = reply_with;
	}

	/**
	 * Devuelve el identificador de la conversación a la que pertenece el mensaje de respuesta 
	 * @return int 		
	 */
	public Integer getInReply_to() {
		return inReply_to;
	}

	/**
	 * Asigna el identificador de la conversación a la que pertenece el mensaje de respuesta
	 * @param inReply_to el int identificador de la conversación a la que pertenece el mensaje de respuesta
	 * 		
	 */
	public void setInReply_to(Integer inReply_to) {
		this.inReply_to = inReply_to;
	}
	/**
	 * Devuelve la fecha en la que debe ser contestado el mensaje
	 * @return Date
	 */
	public Date getReplyBy() {
		return replyBy;
	}

	/**
	 * Asigna la fecha en la que debe ser contestado el mensaje
	 * @param replyBy  		
	 */
	public void setReplyBy(Date replyBy) {
		this.replyBy = replyBy;
	}

	/**
	 * Devuelve un arreglo con los agentes receptores del mensaje
	 * @return	List<Object> 
	 */
	public List<Object> getReceiver() {
		return receiver;
	}

	/**
	 * Agrega un agente como receptor del mensaje
	 * @param receiver	Objeto agente receptor del mensaje
	 */
	public void addReceiver(Object receiver) {
		
		if(this.receiver==null)
			this.receiver = new ArrayList<Object>();
		
		this.receiver.add(receiver);
	}

	/**
	 * Devuelve la preposición del mensaje
	 * @return Preposicion
	 */
	public Preposicion getContent() {
		return content;
	}

	/**
	 * Asigna la preposición del mensaje
	 * @param content preposisción del mensaje
	 */
	public void setContent(Preposicion content) {
		this.content = content;
	}
	
	/**
	 * Devuelve <code>true</code> si el mensje es respuesta un mensaje anterior
	 * @return boolean 
	 */
	public boolean isReply(){
		return isReply;//this.isReply;
	}
	
	/**
	 * Fija el mensaje como respuesta un mensaje anterior
	 */
	public void setAsReply(){
		this.isReply = true;
	}
	
	@Override
	public boolean equals(Object obj){
		
		if(obj instanceof MensajeACL){
			MensajeACL mssg = (MensajeACL)obj;
			return (this.getConversationID()==mssg.getConversationID() && this.getPerformative().equals(mssg.getPerformative())
					&& this.getContent().equals(mssg.getContent()))?true:false;
		}else{
			return false;
		}
		
	}

}
