package com.cfm.coffeemanagement.model.request;

import com.google.common.base.Preconditions;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.cfm.coffeemanagement.constants.Constants.NAME_MUST_NOT_NULL;
import static com.cfm.coffeemanagement.constants.Constants.PASSWORD_MUST_NOT_NULL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {
    private String username;
    private String password;

    public void validate() {
        Preconditions.checkArgument(StringUtils.isNotEmpty(this.getUsername()), NAME_MUST_NOT_NULL);
        Preconditions.checkArgument(StringUtils.isNotEmpty(this.getPassword()), PASSWORD_MUST_NOT_NULL);
    }
}
