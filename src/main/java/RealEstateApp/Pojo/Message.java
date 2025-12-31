package RealEstateApp.Pojo;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name="conversation_id")
    private Conversation conversation;

    @ManyToOne(optional = false)
    @JoinColumn(name="sender_user_id")
    private User sender;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(nullable = false)
    private Instant sentAt = Instant.now();

    @Column(nullable = false)
    private boolean readByCompany = false;

    @Column(nullable = false)
    private boolean readByTenant = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Conversation getConversation() {
		return conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Instant getSentAt() {
		return sentAt;
	}

	public void setSentAt(Instant sentAt) {
		this.sentAt = sentAt;
	}

	public boolean isReadByCompany() {
		return readByCompany;
	}

	public void setReadByCompany(boolean readByCompany) {
		this.readByCompany = readByCompany;
	}

	public boolean isReadByTenant() {
		return readByTenant;
	}

	public void setReadByTenant(boolean readByTenant) {
		this.readByTenant = readByTenant;
	}

}
