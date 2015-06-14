package simulaSAAB.comunicacion;

import java.util.Date;
import java.util.List;

import simulaSAAB.agentes.AgenteInteligente;

public class MensajeACL {
	
	private static int SECUENCIAID;
	
	private String performative;
	
	private AgenteInteligente sender;
	
	private  AgenteInteligente reply_to;
	
	private String language;
	
	private String encoding;
	
	private String ontology;
	
	private String protocol;
	
	private int conversationID;
	
	private MensajeACL reply_with;
	
	private int inReply_to;
	
	private Date replyBy;
	
	private List<Object> receiver;
	
	private Preposicion content;

	/**
	 * Constructor
	 */
	public MensajeACL() {
		
		conversationID = new Integer(SECUENCIAID+1);
		SECUENCIAID++;
	}
	
	public MensajeACL(int conversation_id){
		this.conversationID = conversation_id;
	}
	
	public MensajeACL(int conversation_id, String performative){
		this.conversationID = conversation_id;
		this.performative	= performative;
	}
	
	/**
	 * 
	 * @return
	 * 		Type of communicative act
	 */
	public String getPerformative() {
		return performative;
	}
	
	/**
	 * 
	 * @param performative
	 * 			Type of communicative act
	 */
	public void setPerformative(String performative) {
		this.performative = performative;
	}

	/**
	 * 
	 * @return
	 * 		Sender participant in communication
	 */
	public AgenteInteligente getSender() {
		return sender;
	}

	/**
	 * 
	 * @param sender
	 * 		Sender participant in communication
	 */
	public void setSender(AgenteInteligente sender) {
		this.sender = sender;
	}

	/**
	 * 
	 * @return
	 * 		Participant in communication who reply to
	 */
	public AgenteInteligente getReply_to() {
		return reply_to;
	}

	/**
	 * 
	 * @param reply_to
	 * 		Participant in communication who reply to
	 */
	public void setReply_to(AgenteInteligente reply_to) {
		this.reply_to = reply_to;
	}

	/**
	 * 
	 * @return
	 * 		Description of Content
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * 
	 * @param language
	 * 		Description of Content
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * 
	 * @return
	 * 		Description of Content
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * 
	 * @param encoding
	 * 		Description of Content
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * 
	 * @return
	 * 		Description of Content
	 */
	public String getOntology() {
		return ontology;
	}

	/**
	 * 
	 * @param ontology
	 * 		Description of Content
	 */
	public void setOntology(String ontology) {
		this.ontology = ontology;
	}

	/**
	 * 
	 * @return
	 * 		Control of conversation
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * 
	 * @param protocol
	 * 		Control of conversation
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * 
	 * @return
	 * 		Control of conversation
	 */
	public int getConversationID() {
		return conversationID;
	}

	/**
	 * 
	 * @param conversationID
	 * 		Control of conversation
	 */
	public void setConversationID(int conversationID) {
		this.conversationID = conversationID;
	}

	/**
	 * 
	 * @return
	 * 	Control of conversation
	 */
	public MensajeACL getReply_with() {
		return reply_with;
	}

	/**
	 * 
	 * @param reply_with
	 * 		Control of conversation
	 */
	public void setReply_with(MensajeACL reply_with) {
		this.reply_with = reply_with;
	}

	/**
	 * 
	 * @return
	 * 		Control of conversation
	 */
	public int getInReply_to() {
		return inReply_to;
	}

	/**
	 * 
	 * @param inReply_to
	 * 		Control of conversation
	 */
	public void setInReply_to(int inReply_to) {
		this.inReply_to = inReply_to;
	}

	public Date getReplyBy() {
		return replyBy;
	}

	/**
	 * 
	 * @param replyBy
	 * 		Control of conversation
	 */
	public void setReplyBy(Date replyBy) {
		this.replyBy = replyBy;
	}

	/**
	 * 
	 * @return
	 * 		Participant in communication
	 */
	public List<Object> getReceiver() {
		return receiver;
	}

	/**
	 * 
	 * @param receiver
	 * 		Participant in communication
	 */
	public void addReceiver(Object receiver) {
		this.receiver.add(receiver);
	}

	/**
	 * 
	 * @return
	 * 		Content of message
	 */
	public Preposicion getContent() {
		return content;
	}

	/**
	 * 
	 * @param content
	 * 		Content of message
	 */
	public void setContent(Preposicion content) {
		this.content = content;
	}
	
	

}
