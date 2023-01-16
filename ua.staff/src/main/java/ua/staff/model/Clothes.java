package ua.staff.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import org.hibernate.annotations.NaturalId;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import static java.math.BigDecimal.*;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "article")
@Entity
@ToString(of = "id")
public class Clothes {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @NaturalId
    @Column(nullable = false,unique = true)
    private String article;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String subtype;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime added_at = LocalDateTime.now();

    @Column(nullable = false)
    private BigDecimal price;

    @ColumnDefault("0")
    private BigDecimal discount = ZERO;

    @Column(nullable = false)
    private String sex;

    @Setter(AccessLevel.PRIVATE)
    @ElementCollection
    @CollectionTable(name = "clothes_images",joinColumns = @JoinColumn(name = "clothes_id"),
            indexes = @Index(name = "clothes_images_clothes_id_idx",columnList = "clothes_id"))
    private Set<Image> images = new HashSet<>();

    @Setter(AccessLevel.PRIVATE)
    @ElementCollection
    @CollectionTable(name = "clothes_sizes",joinColumns = @JoinColumn(name = "clothes_id"),
            indexes = @Index(name = "clothes_sizes_clothes_id_idx",columnList = "clothes_id"))
    private Set<Size> sizes = new HashSet<>();


    public void addImage(Image image){
        images.add(image);
    }

    public void removeImage(Image image){
        images.remove(image);
    }

    public void addSize(Size size){
        sizes.add(size);
    }

    public void removeSize(Size size){
        sizes.remove(size);
    }


    public void removeSizes(){
        this.sizes = new HashSet<>();
    }

}
