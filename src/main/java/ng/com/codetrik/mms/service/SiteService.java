package ng.com.codetrik.mms.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import ng.com.codetrik.mms.model.entity.Site;

public interface SiteService {
    Site createSite(Site site);
    Site queryBySiteCode(String siteCode);
    Site queryById(UUID id);
    Site updateSite(Site site);    
    List<String> allThisOperatorSiteCode(String operatorEmail);
    List<String> allThisOperatorSite(String operatorEmail);
    Map<String,String> allThisOperatorSiteCodeAndSite(String operatorEmail);
}
