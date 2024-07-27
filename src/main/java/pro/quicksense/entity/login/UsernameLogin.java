package pro.quicksense.entity.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsernameLogin {
    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;
}