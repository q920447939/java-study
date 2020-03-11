/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月10日
 */
package pojo;

/**
 * ClassName: Student
 * @Description:
 * @author leegoo
 * @date 2020年03月10日
 */
public class Student {

    private String name;
    private int sex;

    public Student() {
    }

    public Student(String name, int sex) {
        this.name = name;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", sex=" + sex +
                '}';
    }
}
