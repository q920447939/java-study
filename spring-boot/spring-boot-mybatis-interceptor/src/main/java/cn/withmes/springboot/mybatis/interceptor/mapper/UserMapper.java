/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月22日
 */
package cn.withmes.springboot.mybatis.interceptor.mapper;

import cn.withmes.springboot.mybatis.interceptor.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.plugin.Intercepts;

/**
 * ClassName: UserMapper
 * @Description:
 * @author leegoo
 * @date 2019年05月22日
 */

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM USER WHERE NAME = #{name}   ")
    User findByName(User user);


    @Insert("INSERT INTO USER(NAME, AGE) VALUES(#{name}, #{age})")
    int insert(@Param("name") String name, @Param("age") Integer age, Integer sex);

}
