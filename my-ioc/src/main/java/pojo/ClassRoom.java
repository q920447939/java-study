/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月10日
 */
package pojo;

import java.util.List;

/**
 * ClassName: Class
 * @Description:
 * @author leegoo
 * @date 2020年03月10日
 */
public class ClassRoom {
    private  String teacherName;
    private List<Student> sts;

    public ClassRoom() {
    }

    public ClassRoom(String teacherName, List<Student> sts) {
        this.teacherName = teacherName;
        this.sts = sts;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public List<Student> getSts() {
        return sts;
    }

    public void setSts(List<Student> sts) {
        this.sts = sts;
    }

    @Override
    public String toString() {
        return "ClassRoom{" +
                "teacherName='" + teacherName + '\'' +
                ", sts=" + sts +
                '}';
    }
}
