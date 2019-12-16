package yuri.contract.server.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import yuri.contract.server.model.Role;

import java.util.List;

@Mapper
@Component
public interface RoleMapper {
    @Select("select * from role where name = #{name}")
    Role select(String name);

    @Select("select * from role")
    List<Role> selectAll();

    @Select("select name from role")
    List<String> selectAllName();

    @Select("select count(*) from role where id = #{id}")
    int exists(String id);

    @Insert("insert into role (name, description) values(#{name}, #{description})")
    int insert(String name, String description);

    @Update("update role set name = #{name}, description = #{description} where id = #{id}")
    int updateDescription(String name, String description, int id);

    @Delete("delete from role where id =#{id}")
    int delete(int id);

    @Delete("delete from role")
    int deleteAll();
}
