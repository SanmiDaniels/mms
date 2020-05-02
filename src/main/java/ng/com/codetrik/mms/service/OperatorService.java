package ng.com.codetrik.mms.service;

import java.util.UUID;
import ng.com.codetrik.mms.model.Operator;

public interface OperatorService {
    Operator createOperator(Operator operator);
    Operator queryByEmail(String email);
    Operator queryById(UUID id);
    Operator updateOperator(Operator operator);
}
