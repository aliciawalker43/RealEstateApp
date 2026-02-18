package RealEstateApp.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.Message;
import RealEstateApp.Pojo.User;



public interface MessageDao extends JpaRepository<Message, Long> {
   
	List<Message> findByConversationIdOrderBySentAtAsc(Long conversationId);

	int  countByReadByTenantFalse();

	int  countByReadByCompanyFalse();
}
