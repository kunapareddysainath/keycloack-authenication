package in.wb.ims.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class CreateUserRequest {

    private String name;
    @NotNull(message = "Please provide valid username")
    private String username;

    @NotNull(message = "Password cant be null")
    @Pattern(regexp = "^[a-zA-Z0-9@#$_!*]+$\n", message = "Password should contain only A-Z, a-z,0-9 and !,@,#,$,*,_")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
