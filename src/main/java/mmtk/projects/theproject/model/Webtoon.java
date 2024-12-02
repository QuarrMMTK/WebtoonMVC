package mmtk.projects.theproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Author : Min Myat Thu Kha
 * Created At : 05/11/2024, Nov ,11, 20
 * Project Name : WebtoonMVC
 **/
@Data
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

    @OneToMany(mappedBy = "webtoon")
    private List<Chapter> chapters;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDate;
    @LastModifiedDate
    private LocalDateTime modificationDate;
}
