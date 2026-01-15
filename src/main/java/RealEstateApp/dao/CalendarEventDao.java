package RealEstateApp.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import RealEstateApp.Pojo.CalendarEvent;


public interface CalendarEventDao extends JpaRepository< CalendarEvent, Long> {
	
	  List<CalendarEvent> findByCompanyIdAndStartAtBetweenOrderByStartAtAsc(
	            Long companyId, LocalDateTime start, LocalDateTime end
	    );

	    @Query("""
	        select e from CalendarEvent e
	        where e.company.id = :companyId
	          and e.active = true
	          and (e.startAt < :end and e.endAt > :start)
	        order by e.startAt asc
	    """)
	    List<CalendarEvent> findOverlappingRange(@Param("companyId") Long companyId,
	                                            @Param("start") LocalDateTime start,
	                                            @Param("end") LocalDateTime end);
	    
	    
	    
	}
