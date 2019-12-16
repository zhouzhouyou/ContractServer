package yuri.contract.server.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import yuri.contract.server.model.Act;
import yuri.contract.server.model.ContractLog;

import java.util.List;

@Mapper
@Component
public interface ActMapper {
    @Select("select roleId from act where userName =#{userName}")
    List<Integer> FindByUsername(String userName);

    @Select("select * from act where concat_ws (username, roleId, description) " +
            "like concat('%', #{query}, '%');")
    List<Act> fuzzyQuery(String query);

    @Select("select * from act")
    List<Act> selectAll();

    @Select("select * from act where username = #{userName}")
    List<Act> selectByUser(String userName);

    @Insert("insert into act select #{userName}, id from role where name = #{roleName}")
    int insert(String userName, String roleName, String description);

    @Update("update act set description =#{description} where userName =#{userName} and roleId =#{roleId}")
    int updateDescription(String userName, Integer roleId, String description);

    @Delete("delete from act where userName =#{userName} and roleId =#{roleId}")
    int delete(String userName, Integer roleId);

    @Delete("delete from act")
    int deleteAll();

    @Delete("delete from act where username = #{username}")
    int deleteByUsername(String username);

    @Select("select userName from act where roleId = #{roleId}")
    List<String> selectByRole(Integer roleId);

    @Select("select name from role where id = (select roleId from act where username =#{userName})")
    List<String> findRoleByName(String userName);


}
