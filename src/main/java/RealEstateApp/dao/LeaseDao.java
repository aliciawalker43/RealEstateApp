package RealEstateApp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.Lease;



public interface LeaseDao extends JpaRepository<Lease, Long> {
}
