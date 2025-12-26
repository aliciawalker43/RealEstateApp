package RealEstateApp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.NotificationSettings;



public interface NotificationSettingsDao extends JpaRepository<NotificationSettings, Long> {
}
