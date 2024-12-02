package mmtk.projects.theproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Author : Min Myat Thu Kha
 * Created At : 05/11/2024, Nov ,11, 20
 * Project Name : WebtoonMVC
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
public class Chapter {
    @Id
    @GeneratedValue
    private Long id;
    private String chapter;

    @ManyToOne
    @JoinColumn(name = "webtoon_id")
    private Webtoon webtoon;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
