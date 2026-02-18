package RealEstateApp.Controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import RealEstateApp.Pojo.CalendarEvent;
import RealEstateApp.Pojo.CalendarEventType;
import RealEstateApp.Pojo.Company;
import RealEstateApp.Pojo.Role;
import RealEstateApp.Pojo.User;
import RealEstateApp.dao.CalendarEventDao;
import RealEstateApp.dao.PropertyDao;
import RealEstateApp.dao.UserDao;

import java.time.*;
import java.util.*;


@Controller
@RequestMapping("/landlord/calendar")
public class CompanyCalendarController {

    private final CalendarEventDao eventDao;
    private final PropertyDao propertyDao; // if you want property dropdown
    private final UserDao userDao;         // if you want employee dropdown

    public CompanyCalendarController(CalendarEventDao eventDao, PropertyDao propertyDao, UserDao userDao) {
        this.eventDao = eventDao;
        this.propertyDao = propertyDao;
        this.userDao = userDao;
    }

    
    
    
    /** Full calendar page (HTML) */
    @GetMapping
    public String calendarPage(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || currentUser.getCompany() == null) return "redirect:/login";

        Company company = currentUser.getCompany();
        model.addAttribute("company", company);

        // Optional dropdowns for create form modal/page
        model.addAttribute("properties", propertyDao.findAllByCompany(company));
        model.addAttribute("employees", userDao.findByCompanyIdAndRoleIn(
                company.getId(), List.of(Role.EMPLOYEE, Role.LANDLORD)
       ));

        model.addAttribute("types", CalendarEventType.values());
        return "landlord/calendar";
    }

    
    /** JSON feed for FullCalendar */
    @GetMapping("/events")
    @ResponseBody
    public ResponseEntity<?> eventsFeed(@RequestParam("start") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                        @RequestParam("end")   @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
                                        HttpSession session) {

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || currentUser.getCompany() == null) return ResponseEntity.status(401).build();

        Company company = currentUser.getCompany();
        List<CalendarEvent> events = eventDao.findOverlappingRange(company.getId(), start, end);

        // FullCalendar expects fields: id, title, start, end, allDay
        List<Map<String, Object>> payload = events.stream().map(e -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", e.getId());
            m.put("title", e.getTitle());
            m.put("start", e.getStartAt().toString());
            m.put("end", e.getEndAt().toString());
            m.put("allDay", e.isAllDay());

			Map<String, Object> extendedProps = new HashMap<>();
			extendedProps.put("type", e.getType().name());
			if (e.getProperty() != null) {
				extendedProps.put("property", e.getProperty().getRentalAddress());
			}
			m.put("extendedProps", extendedProps);
          
            return m;
        }).toList();

        return ResponseEntity.ok(payload);
    }

    /** Create event (simple POST) */
    @RequestMapping("/event/add")
	public String showAddEventForm(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || currentUser.getCompany() == null) return "redirect:/login";

        Company company = currentUser.getCompany();
        model.addAttribute("company", company);

        // Optional dropdowns for create form modal/page
        model.addAttribute("properties", propertyDao.findAllByCompany(company));
        model.addAttribute("employees", userDao.findByCompanyIdAndRoleIn(
                company.getId(), List.of(Role.EMPLOYEE, Role.LANDLORD)
       ));

        model.addAttribute("types", CalendarEventType.values());
    	
		return "landlord/calendareventform";
	}
    
    
    @PostMapping("/create")
    public String createEvent(@RequestParam String title,
                              @RequestParam CalendarEventType type,
                              @RequestParam(required=false) Long propertyId,
                              @RequestParam(required=false) Long assignedUserId,
                              @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime startAt,
                              @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime endAt,
                              @RequestParam(required=false) String description,
                              @RequestParam(defaultValue="false") boolean allDay,
                              HttpSession session,
                              RedirectAttributes ra) {

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || currentUser.getCompany() == null) return "redirect:/login";

        if (endAt.isBefore(startAt)) {
            ra.addFlashAttribute("error", "End time must be after start time.");
            return "redirect:/landlord/calendar";
        }

        Company company = currentUser.getCompany();

        CalendarEvent e = new CalendarEvent();
        e.setCompany(company);
        e.setTitle(title);
        e.setType(type);
        e.setStartAt(startAt);
        e.setEndAt(endAt);
        e.setDescription(description);
        e.setAllDay(allDay);

        if (propertyId != null) {
            e.setProperty(propertyDao.findById(propertyId).orElse(null));
        }
        if (assignedUserId != null) {
            e.setAssignedUser(userDao.findById(assignedUserId).orElse(null));
        }

        eventDao.save(e);
        ra.addFlashAttribute("success", "Event created.");
        return "redirect:/landlord/calendar";
    }
}
