package ng.com.codetrik.mms.repository;

import java.util.List;
import java.util.UUID;
import ng.com.codetrik.mms.model.Expenditure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenditureRepository extends JpaRepository<Expenditure,UUID>{
    List<Expenditure> findByRequestorEmail(String requestorEmail);
}
