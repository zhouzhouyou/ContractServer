package yuri.contract.server.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import yuri.contract.server.model.Act;

import java.util.List;

@Mapper
@Component
public interface ActMapper {
    @Select("select roleName from act where userName =#{userName}")
    List<String> FindByUsername(String userName);

    @Select("select * from act")
    List<Act> selectAll();

    @Insert("insert into act values(#{userName},#{roleName},#{description})")
    int insert(String userName,String roleName,String description);

    @Update("update act set description =#{description} where userName =#{userName} and roleName =#{roleName}")
    int updateDescription(String userName,String roleName,String description);

    @Delete("delete from act where userName =#{userName} and roleName =#{roleName}")
    int delete(String userName,String roleName);

    @Delete("delete from act")
    int deleteAll();
}
