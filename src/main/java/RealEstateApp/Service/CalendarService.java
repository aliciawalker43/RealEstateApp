package RealEstateApp.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import RealEstateApp.Pojo.CalendarEvent;
import RealEstateApp.Pojo.Company;
import RealEstateApp.dao.CalendarEventDao;

@Service
public class CalendarService {

    private final CalendarEventDao eventDao;

    public CalendarService(CalendarEventDao eventDao) {
        this.eventDao = eventDao;
    }

    public void add7DayCalendarToModel(Model model, Company company) {
        LocalDate today = LocalDate.now();
        LocalDate endDay = today.plusDays(6);

        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = endDay.plusDays(1).atStartOfDay();

        List<CalendarEvent> events = eventDao.findOverlappingRange(company.getId(), start, end)
        		.stream()
        	    .filter(Objects::nonNull)
        	    .filter(e -> e.getStartAt() != null)
        	    .collect(Collectors.toList());
        		
        		
        Map<LocalDate, List<CalendarEvent>> byDay = events.stream()
                .collect(Collectors.groupingBy(e -> e.getStartAt().toLocalDate(),
                        TreeMap::new, Collectors.toList()));

        model.addAttribute("calStart", today);
        model.addAttribute("calEnd", endDay);
        model.addAttribute("eventsByDay", byDay);
        model.addAttribute("weekEvents", events);
    }
    
    
}