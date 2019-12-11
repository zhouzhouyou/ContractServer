package yuri.contract.server.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import yuri.contract.server.model.Contract;

import java.sql.Date;
import java.util.List;

@Mapper
@Component
public interface ContractMapper {
    @Select("select * from contract where num =#{num}")
    Contract select(String num);

    @Select("select * from contract")
    List<Contract> selectAll();

    @Insert("insert into contract values(#{num},#{name},#{customerNum},#{begin},#{end},#{content},#{userName})")
    int insert(String num, String name, int customerNum, Date begin, Date end, String content, String userName);

    @Update("update contract set content =#{content} where num =#{num}")
    int updateContent(String num, String content);

    @Delete("delete from contract where num =#{num}")
    int delete(String num);

    @Delete("delete from contract")
    int deleteAll();
}
