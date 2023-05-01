package entity;

import lombok.Data;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
@Data
public class Letter {

    private Long id;
    private String topic;
    private String letterText;
    private Date shippingDate;
    private Human sender;
    private List<Human> receivers = new ArrayList<>();
}
