package ua.staff.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
@ToString
@EqualsAndHashCode(of = "size")
public class Size {

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer amount;

    public Size(String size) {
        this.size = size;
    }
}
