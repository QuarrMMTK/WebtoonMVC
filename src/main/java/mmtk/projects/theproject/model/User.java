package mmtk.projects.theproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

/**
 * Author : Min Myat Thu Kha
 * Created At : 05/11/2024, Nov ,11, 20
 * Project Name : WebtoonMVC
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "users")  // It's a good practice to define the table name explicitly
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Added strategy for clarity (auto-increment)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @Column(nullable = true, length = 64)  // This is for the profile photo's file path
    private String profilePhoto;

    @Enumerated(EnumType.STRING)
    private Role role;

    @CreatedDate
    @Column(updatable = true, nullable = false)
    private Date createdAt;

    // If you need to return the full URL, use a service layer or controller for that
    public String getProfilePhoto() {
        if (profilePhoto == null) {
            return null;  // Or you can return a default image path, such as "/default-avatar.jpg"
        } else {
            return  "/photos/profiles/"+profilePhoto;  // Assuming files are stored in /uploads/profiles/
        }
    }

    public String getFullName(){
        return firstName + " " + lastName;
    }
}
