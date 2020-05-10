package ng.com.codetrik.mms.repository;

import java.util.UUID;
import ng.com.codetrik.mms.model.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteRepository extends JpaRepository<Site,UUID>{
    Site findBySiteCode(String siteCode);
}
