package ng.com.codetrik.mms.repository;

import java.util.UUID;
import ng.com.codetrik.mms.model.Operator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperatorRepository extends JpaRepository<Operator,UUID>{
    Operator findByEmail(String email);
}
