package yuri.contract.server.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import yuri.contract.server.model.Behavior;

import java.util.List;

@Mapper
@Component
public interface BehaviorMapper {
    @Select("select * from behavior where roleName =#{roleName} and num =#{num}")
    Behavior select(String roleName,String num);

    @Select("select * from behavior")
    List<Behavior> selectAll();

    @Insert("insert into behavior values(#{roleName},#{num})")
    int insert(String roleName,String num);

    @Delete("delete from behavior where roleName =#{roleName} nad num =#{num}")
    int delete(String roleName,String num);

    @Delete("delete from behavior")
    int deleteAll();
}
