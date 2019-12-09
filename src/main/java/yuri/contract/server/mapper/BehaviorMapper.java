package yuri.contract.server.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import yuri.contract.server.model.Behavior;

import java.util.List;

@Mapper
@Component
public interface BehaviorMapper {
    @Select("select * from behavior where roleId =#{roleId} and num =#{num}")
    Behavior select(Integer roleId, String num);

    @Select("select num from behavior where roleId =#{roleId}")
    List<String> selectByRole(Integer roleId);

    @Select("select * from behavior")
    List<Behavior> selectAll();

    @Insert("insert into behavior values(#{roleId}, #{num})")
    int insert(Integer roleId, String num);

    @Delete("delete from behavior where roleId =#{roleId} nad num =#{num}")
    int delete(Integer roleId, String num);

    @Delete("delete from behavior")
    int deleteAll();
}
