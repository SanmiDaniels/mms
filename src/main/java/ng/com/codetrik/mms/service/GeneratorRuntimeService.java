package ng.com.codetrik.mms.service;

import java.util.List;
import java.util.UUID;
import ng.com.codetrik.mms.model.Generator;
import ng.com.codetrik.mms.model.GeneratorRuntime;

public interface GeneratorRuntimeService {
    GeneratorRuntime createRuntime(GeneratorRuntime genRun);
    List<Generator>  generatorsOnSite(String siteCode);
    GeneratorRuntime queryById(UUID id);
    GeneratorRuntime updateRuntime(GeneratorRuntime genRun);
    
}
