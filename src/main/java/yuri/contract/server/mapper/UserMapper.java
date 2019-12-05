package yuri.contract.server.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import yuri.contract.server.model.User;

import java.util.List;

@Mapper
@Component
public interface UserMapper {
    @Select("select * from user where name=#{name} and password=#{password}")
    User select(String name, String password);

    @Select("select * from user where name=#{name}")
    User findUserByName(String name);

    @Select("select count(*) from user where name=#{name}")
    int count(String name);

    @Select("select * from user")
    List<User> selectAll();

    @Insert("insert into user values (#{name}, #{password}")
    int insert(String name, String password);

    @Delete("delete from user where name=#{name}")
    int delete(String name);

    @Update("update user set password=#{password} where name=#{name}")
    int updatePassword(String name, String password);
}
