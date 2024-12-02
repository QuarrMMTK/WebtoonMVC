package mmtk.projects.theproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import mmtk.projects.theproject.model.Role;

/**
 * Author : Min Myat Thu Kha
 * Created At : 02/12/2024, Dec , 10:26
 * Project Name : TheProject
 **/
@Data
public class CreateUserDto {

    @NotBlank(message = "First Name is required")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Role is required")
    private String role;
}
