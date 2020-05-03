package ng.com.codetrik.mms.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import ng.com.codetrik.mms.model.Generator;

public interface GeneratorService {
    Generator createGenerator(Generator gen);
    Generator updateGenerator(Generator gen);
    Generator queryBySerialNumber(String serialNumber);
    Generator queryById(UUID id);
    Map<String,String> nameAndSerialOfGeneratorOnSite(String siteCode);
    List<Generator>  generatorsOnSite(String siteCode);
    
}
