
package ng.com.codetrik.mms.service;

import java.util.UUID;
import ng.com.codetrik.mms.model.entity.Vendor;

public interface VendorService {
    Vendor createVendor(Vendor newVendor);
    Vendor updateVendor(Vendor newVendor);
    Vendor queryById(UUID id);
    Vendor queryByEmail(String email);
}
