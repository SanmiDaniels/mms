
package ng.com.codetrik.mms.service;

import java.util.List;
import java.util.UUID;
import ng.com.codetrik.mms.model.Expenditure;

public interface ExpenditureService {
     Expenditure createExpenditure(Expenditure exp);
     Expenditure updateExpenditire(Expenditure exp);
     Expenditure queryById(UUID id);
     List<Expenditure> queryByRequestorEmail(String requestorEmail);
}
