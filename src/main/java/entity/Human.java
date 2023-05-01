package entity;

import lombok.Data;

import java.sql.Date;

@Data
public class Human {

    private Long id;
    private String firstName;
    private String secondName;
    private Date birthday;
}
