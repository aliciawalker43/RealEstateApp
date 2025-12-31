package RealEstateApp.Service;



import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import RealEstateApp.Pojo.Company;
import RealEstateApp.Pojo.Conversation;
import RealEstateApp.Pojo.Message;
import RealEstateApp.Pojo.Role;
import RealEstateApp.Pojo.User;
import RealEstateApp.dao.ConversationDao;
import RealEstateApp.dao.MessageDao;

import java.time.Instant;
import java.util.List;

@Service
public class MessagingService {

    private final ConversationDao conversationDao;
    private final MessageDao messageDao;

    public MessagingService(ConversationDao conversationDao, MessageDao messageDao) {
        this.conversationDao = conversationDao;
        this.messageDao = messageDao;
    }

    @Transactional
    public Conversation getOrCreateConversation(Company company, User tenant) {
        return conversationDao.findByCompanyIdAndTenant(company.getId(), tenant)
                .orElseGet(() -> {
                    Conversation c = new Conversation();
                    c.setCompany(company);
                    c.setTenant(tenant);
                    c.setCreatedAt(Instant.now());
                    return conversationDao.save(c);
                });
    }

    @Transactional
    public Message sendMessage(User sender, Conversation conversation, String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Message cannot be empty.");
        }

        Message m = new Message();
        m.setConversation(conversation);
        m.setSender(sender);
        m.setContent(content.trim());
        m.setSentAt(Instant.now());

        // Mark read flags depending on sender
        if (sender.getRole() == Role.TENANT) {
            m.setReadByTenant(true);
            m.setReadByCompany(false);
        } else { // company user
            m.setReadByCompany(true);
            m.setReadByTenant(false);
        }

        return messageDao.save(m);
    }

    public List<Message> getMessages(Long conversationId) {
        return messageDao.findByConversationIdOrderBySentAtAsc(conversationId);
    }
}
