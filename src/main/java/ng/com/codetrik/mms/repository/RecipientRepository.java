package ng.com.codetrik.mms.repository;

import java.util.UUID;
import ng.com.codetrik.mms.model.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipientRepository extends JpaRepository<Recipient,UUID>{

    Recipient findByEmail(String email);
    
}
