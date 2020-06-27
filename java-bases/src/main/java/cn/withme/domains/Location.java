/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年06月22日
 */
package cn.withme.domains;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * ClassName: Location
 * @Description:
 * @author leegoo
 * @date 2020年06月22日
 */
@Getter
@Setter
public class Location {
    private String locationName;
    private int code;

    private List<Object> list;

    @Override
    public String toString() {
        return "Location{" +
                "locationName='" + locationName + '\'' +
                ", code=" + code +
                ", list=" + list +
                '}';
    }
}
