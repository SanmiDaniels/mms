package ng.com.codetrik.mms.model.util;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class Login {
    @Email @NotNull @NotEmpty
    private String email;
    
    private String password;
}
