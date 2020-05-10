package ng.com.codetrik.mms.service;

import java.util.UUID;
import ng.com.codetrik.mms.model.entity.GeneratorRuntime;

public interface GeneratorRuntimeService {
    GeneratorRuntime createRuntime(GeneratorRuntime generatorRuntime);
    GeneratorRuntime queryById(UUID id);
    GeneratorRuntime updateRuntime(GeneratorRuntime newGeneratorRuntime);
    
}
