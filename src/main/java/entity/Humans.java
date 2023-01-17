package entity;

import java.sql.Date;
import java.util.Objects;

public class Humans {

    private Long id;
    private String firstName;
    private String secondName;
    private Date birthday;


    public Humans(Long id, String firstName, String secondName, Date birthday) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthday = birthday;
    }

    public Humans() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Humans humans = (Humans) o;
        return Objects.equals(id, humans.id) && Objects.equals(firstName, humans.firstName) && Objects.equals(secondName, humans.secondName) && Objects.equals(birthday, humans.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, secondName, birthday);
    }

    @Override
    public String toString() {
        return firstName + " " + secondName + " " + birthday;
    }
}
