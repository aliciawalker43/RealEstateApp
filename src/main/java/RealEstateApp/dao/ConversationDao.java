package RealEstateApp.dao;

import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.Conversation;
import RealEstateApp.Pojo.User;


public interface ConversationDao extends JpaRepository<Conversation, Long> {
	
    Optional<Conversation> findByCompanyIdAndTenant(Long companyId, User user);
    
    List<Conversation> findByCompanyIdOrderByCreatedAtDesc(Long companyId);
}
