package RealEstateApp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.CalendarEvent;


public interface CalendarEventDao extends JpaRepository< CalendarEvent, Long> {
}