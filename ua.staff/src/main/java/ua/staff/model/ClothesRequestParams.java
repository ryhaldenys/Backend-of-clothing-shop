package ua.staff.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClothesRequestParams {
    private String sex = "male";
    private String subtype;
    private String type;
}
