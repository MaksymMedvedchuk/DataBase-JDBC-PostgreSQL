package entity;

public class Test {
    private Long id;
    private String nameTest;
    private String surnameTest;

    public Test(Long id, String nameTest, String surnameTest) {
        this.id = id;
        this.nameTest = nameTest;
        this.surnameTest = surnameTest;
    }

    public Test() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameTest() {
        return nameTest;
    }

    public void setNameTest(String nameTest) {
        this.nameTest = nameTest;
    }

    public String getSurnameTest() {
        return surnameTest;
    }

    public void setSurnameTest(String surnameTest) {
        this.surnameTest = surnameTest;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", nameTest='" + nameTest + '\'' +
                ", surnameTest='" + surnameTest + '\'' +
                '}';
    }
}
