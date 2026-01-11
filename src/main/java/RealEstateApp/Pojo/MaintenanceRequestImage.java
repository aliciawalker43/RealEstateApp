package RealEstateApp.Pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="maintenance_request_image")
public class MaintenanceRequestImage {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(optional=false)
	@JoinColumn (name= "request_id")
	private MaintenanceRequest request;
	
	@ManyToOne(optional=false)
	@JoinColumn (name= "image_asset_id")
	private ImageAsset imageAsset;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MaintenanceRequest getRequest() {
		return request;
	}

	public void setRequest(MaintenanceRequest request) {
		this.request = request;
	}

	public ImageAsset getImageAsset() {
		return imageAsset;
	}

	public void setImageAsset(ImageAsset imageAsset) {
		this.imageAsset = imageAsset;
	}
	
	
}
