package yuri.contract.server.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import yuri.contract.server.model.Function;

import java.util.List;

@Mapper
@Component
public interface FunctionMapper {
    @Select("select * from function where num =#{num}")
    Function select(String num);

    @Select("select * from function")
    List<Function> selectAll();

    @Insert("insert into function values(#{num},#{name},#{description})")
    int insert(String num,String name,String description);

    @Update("update function set description =#{description} where num =#{num}")
    int updateDescription(String description,String num);

    @Delete("delete from function where num =#{num}")
    int delete(String num);

    @Delete("delete from function")
    int deleteAll();
}
