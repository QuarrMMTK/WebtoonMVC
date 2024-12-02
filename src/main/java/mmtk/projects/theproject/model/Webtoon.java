package mmtk.projects.theproject.model;

import jakarta.persistence.*;
import lombok.*;


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
    private String synopsis;
    private String Author;
    private String coverImage;
    @Enumerated(EnumType.STRING)
    private Status status;

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

    public String getCoverPhoto(){
        return "webtoon/" + coverImage;
    }
}
