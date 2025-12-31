package RealEstateApp.Pojo;

import java.time.Instant;

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

@Entity
@Table(name = "image_assets")
public class ImageAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name="company_id")
    private Company company;

    @ManyToOne(optional = false)
    @JoinColumn(name="user_id")
    private User user; // uploader

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=50)
    private ImageCategory category;

    // Used when an image belongs to something (propertyId, maintenanceRequestId, etc.)
    @Column(name="context_id")
    private Long contextId;

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

    // getters/setters
    public Long getId() { return id; }

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public ImageCategory getCategory() { return category; }
    public void setCategory(ImageCategory category) { this.category = category; }

    public Long getContextId() { return contextId; }
    public void setContextId(Long contextId) { this.contextId = contextId; }

    public String getOriginalFileName() { return originalFileName; }
    public void setOriginalFileName(String originalFileName) { this.originalFileName = originalFileName; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public long getSizeBytes() { return sizeBytes; }
    public void setSizeBytes(long sizeBytes) { this.sizeBytes = sizeBytes; }

    public String getStoragePath() { return storagePath; }
    public void setStoragePath(String storagePath) { this.storagePath = storagePath; }

    public Instant getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(Instant uploadedAt) { this.uploadedAt = uploadedAt; }

	public ImageAsset orElse(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	public ImageAsset orElseGet(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	
	

	
}