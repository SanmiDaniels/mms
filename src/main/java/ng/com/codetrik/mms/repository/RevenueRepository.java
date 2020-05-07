package ng.com.codetrik.mms.repository;

import ng.com.codetrik.mms.model.Revenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue,Long>{
    
    @Query(value = "select * from revenue where vendor_email = :email order by id desc limit 1", nativeQuery = true)
    Revenue findLastRevenue(@Param("email") String email);

    @Query(value = "select * from revenue where vendor_email = :email order by id desc limit 1,1", nativeQuery = true)
    Revenue findSecondLastRevenue(@Param("email") String email);    
}
