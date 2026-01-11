package RealEstateApp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.TenantProfile;
import RealEstateApp.Pojo.User;

public interface TenantProfileDao extends JpaRepository<TenantProfile, Long> {

	TenantProfile findByUserId(Long id);

	List<TenantProfile> findAllByUserId(Long userId);}

