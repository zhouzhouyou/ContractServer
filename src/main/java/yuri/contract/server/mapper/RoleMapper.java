package yuri.contract.server.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import yuri.contract.server.model.Role;

import java.util.List;

@Mapper
@Component
public interface RoleMapper {
    @Select("select * from role where name =#{name}")
    Role select(String name);

    @Select("select * from role")
    List<Role> selectAll();

    @Insert("insert into role values(#{name},#{description})")
    int insert(String name,String description);

    @Update("update role set description =#{description} where name =#{name}")
    int updateDescription(String description,String name);

    @Delete("delete from role where name =#{name}")
    int delete(String name);

    @Delete("delete from role")
    int deleteAll();
}
