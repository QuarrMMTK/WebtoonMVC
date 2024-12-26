package mmtk.projects.theproject.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;


import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table
public class Webtoon {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    @Size(min = 10, max = 1000, message = "Synopsis must be between 10 and 1000 characters")
    private String synopsis;
    private String Author;
    private String coverImage;
    @Enumerated(EnumType.STRING)
    private Status status;
    @CreatedDate
    @DateTimeFormat(pattern = "dd-MMM-yyyy")
    private Date uploadedDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MMM-yyyy")
    private Date modificationDate;
    // No cascade for @ManyToMany relationship, as we don't want to delete related Genre entities
    @ManyToMany
    @JoinTable(
            name = "webtoon_genre", // Name of the join table
            joinColumns = @JoinColumn(name = "webtoon_id"), // Foreign key for Webtoon
            inverseJoinColumns = @JoinColumn(name = "genre_id")// Foreign key for Genre
    )
    private List<Genre> genres;

    // CascadeType.REMOVE for @OneToMany, so that related Chapter entities are deleted when Webtoon is deleted
    @OneToMany(mappedBy = "webtoon", cascade = CascadeType.REMOVE)
    private List<Chapter> chapters;
}
