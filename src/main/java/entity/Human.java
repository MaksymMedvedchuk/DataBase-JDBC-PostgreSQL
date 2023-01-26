package entity;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

public class Human {

    private Long id;
    private String firstName;
    private String secondName;
    private Date birthday;//use joda time: LocalDate, LocalDateTime, Instant, Timestamp(?)


    public Human(Long id, String firstName, String secondName, Date birthday) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthday = birthday;
    }

    public Human() {
    }

    public Human(String firstName, String secondName, Date birthday) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthday = birthday;
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
        Human human = (Human) o;
        return Objects.equals(id, human.id) && Objects.equals(firstName, human.firstName) && Objects.equals(secondName, human.secondName) && Objects.equals(birthday, human.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, secondName, birthday);
    }

    @Override
    public String toString() {
        return id + " " + firstName + " " + secondName + " " + birthday;
    }
}
