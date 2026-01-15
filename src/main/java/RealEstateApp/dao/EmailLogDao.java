package RealEstateApp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.EmailLog;

public interface EmailLogDao extends JpaRepository<EmailLog, Long> {

	List<EmailLog> findAllByCompanyId(Long companyId);

}
