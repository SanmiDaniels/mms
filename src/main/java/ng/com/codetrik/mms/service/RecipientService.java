
package ng.com.codetrik.mms.service;

import java.util.UUID;
import ng.com.codetrik.mms.model.entity.Recipient;

public interface RecipientService {
    Recipient createRecipient(Recipient recipient);
    Recipient updateRecipient(Recipient recipient);
    Recipient queryById(UUID id);
    Recipient queryByEmail(String email);
}
