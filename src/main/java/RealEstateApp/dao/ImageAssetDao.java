package RealEstateApp.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.Company;
import RealEstateApp.Pojo.ImageAsset;
import RealEstateApp.Pojo.ImageCategory;
import RealEstateApp.Pojo.User;

public interface ImageAssetDao extends JpaRepository<ImageAsset, Long> {

    List<ImageAsset> findByCompanyIdAndCategoryOrderByUploadedAtDesc(Long companyId, ImageCategory category);

    List<ImageAsset> findByCompanyIdAndCategoryAndContextIdOrderByUploadedAtDesc(
            Long companyId, ImageCategory category, Long contextId);

    List<ImageAsset> findByUserIdAndCategoryOrderByUploadedAtDesc(Long userId, ImageCategory category);

	ImageAsset findTopByUserIdAndCategoryOrderByUploadedAtDesc(Long id, ImageCategory profile);

	ImageAsset findTopByCompanyAndUserAndCategoryOrderByUploadedAtDesc(Company company, User currentUser,
			ImageCategory category);

	ImageAsset findTopByCompanyIdAndUserIdAndCategoryOrderByUploadedAtDesc(Long id, Long id2, ImageCategory category);

	Optional<ImageAsset> findTopByCompanyIdAndUserIdAndCategoryAndContextIdOrderByUploadedAtDesc(Long id, Long id2,
			ImageCategory category, Long contextId);
}
