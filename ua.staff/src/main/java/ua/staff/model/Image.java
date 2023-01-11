package ua.staff.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.NaturalId;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@ToString
@EqualsAndHashCode(of = "url")
public class Image {

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isMain;

    @Column(nullable = false,unique = true)
    private String url;
}
