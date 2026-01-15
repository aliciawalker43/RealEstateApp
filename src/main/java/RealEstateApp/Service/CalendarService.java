package RealEstateApp.Service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import RealEstateApp.Pojo.Company;
import RealEstateApp.dao.CalendarEventDao;

@Service
public class CalendarService {

    private final CalendarEventDao eventDao;

    public CalendarService(CalendarEventDao eventDao) {
        this.eventDao = eventDao;
    }

    public void add7DayCalendarToModel(Model model, Company company) {
        // same logic you already wrote
    }
}