package mmtk.projects.theproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
* Author : Min Myat Thu Kha
* Created At : 06/12/2024, Dec , 15:55
* Project Name : TheProject
**/
@Data
public class GenreDto {
    @NotBlank
    @NotNull
    @NotEmpty
    private String name;

    @NotBlank
    @NotNull
    @NotEmpty
    private String description;
}
