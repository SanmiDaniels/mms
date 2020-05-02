package ng.com.codetrik.mms.repository;

import java.util.UUID;
import ng.com.codetrik.mms.model.Generator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneratorRepository extends JpaRepository<Generator,UUID>{
    Generator findBySerialNumber(String serialNumber);
}
