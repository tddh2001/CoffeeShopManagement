package com.cfm.coffeemanagement.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Preconditions;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import static com.cfm.coffeemanagement.constants.Constants.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    private String username;
    private String password;
    private String phone;
    private String email;
    private String roles;
//    private String status;

    public void validate() {
        if (roles.compareTo("ROLE_ADMIN") != 0 && roles.compareTo("ROLE_USER") !=0) {
            throw new IllegalArgumentException(INCORRECT_ROLE);
        }
        Preconditions.checkArgument(StringUtils.isNotEmpty(this.getUsername()), NAME_MUST_NOT_NULL);
        Preconditions.checkArgument(StringUtils.isNotEmpty(this.getPassword()), PASSWORD_MUST_NOT_NULL);
        Preconditions.checkArgument(StringUtils.isNotEmpty(this.getRoles()), ROLE_MUST_NOT_NULL);
        Preconditions.checkArgument(StringUtils.isNotEmpty(this.getPhone()), PHONE_MUST_NOT_NULL);
        Preconditions.checkArgument(StringUtils.isNotEmpty(this.getEmail()), EMAIL_MUST_NOT_NULL);
    }
}
