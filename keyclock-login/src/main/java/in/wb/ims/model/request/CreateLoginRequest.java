package in.wb.ims.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class CreateLoginRequest {

    @NotNull(message = "Please provide valid username")
    private String username;

    @NotNull(message = "password can not be null")
    @Pattern(regexp = "^[a-zA-Z0-9@#$_!*]+$\n", message = "Check your Password")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
