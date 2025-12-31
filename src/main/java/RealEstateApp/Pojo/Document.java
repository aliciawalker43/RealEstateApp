package RealEstateApp.Pojo;


import RealEstateApp.Pojo.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "document")
public class Document {

	  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @ManyToOne(optional = false) @JoinColumn(name="company_id")
	    private Company company;

	    @ManyToOne(optional = false) @JoinColumn(name="user_id")
	    private User user;
	   
	    @Column(nullable=false) 
	    private String originalFileName;
	    
	    @Column(nullable=false) 
	    private String contentType;
	    
	    @Column(nullable=false) 
	    private long sizeBytes;

	    @Column(nullable=false, length=1000)
	    private String storagePath;

	    @Column(nullable=false)
	    private Instant uploadedAt = Instant.now();

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Company getCompany() {
			return company;
		}

		public void setCompany(Company company) {
			this.company = company;
		}

		

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public String getOriginalFileName() {
			return originalFileName;
		}

		public void setOriginalFileName(String originalFileName) {
			this.originalFileName = originalFileName;
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public long getSizeBytes() {
			return sizeBytes;
		}

		public void setSizeBytes(long sizeBytes) {
			this.sizeBytes = sizeBytes;
		}

		public String getStoragePath() {
			return storagePath;
		}

		public void setStoragePath(String storagePath) {
			this.storagePath = storagePath;
		}

		public Instant getUploadedAt() {
			return uploadedAt;
		}

		public void setUploadedAt(Instant uploadedAt) {
			this.uploadedAt = uploadedAt;
		}
	    
	    
}
	    
	    
