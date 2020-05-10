package ng.com.codetrik.mms.repository;

import java.util.UUID;
import ng.com.codetrik.mms.model.entity.GeneratorRuntime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneratorRuntimeRepository extends JpaRepository<GeneratorRuntime,UUID>{
    
}
