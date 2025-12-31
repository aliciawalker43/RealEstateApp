package RealEstateApp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.TenantProfile;

public interface TenantProfileDao extends JpaRepository<TenantProfile, Long> {}

