package yuri.contract.server.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import yuri.contract.server.model.ContractLog;

import java.sql.Date;
import java.util.List;

@Mapper
@Component
public interface ContractLogMapper {
    @Select("select * from contract_log where userName =#{userName} and time =#{time}")
    ContractLog select(String userName, Date time);

    @Select("select * from contract_log")
    List<ContractLog> selectAll();

    @Insert("Insert into contract_log values(#{userName},#{content},#{time})")
    int insert(String userName,String content,Date time);

    @Update("update contract_log set content =#{content} where userName =#{userName} and time =#{time}")
    int updateContent(String userName,String content,Date time);

    @Delete("delete from contract_log where userName =#{userName} and time =#{time}")
    int delete(String userName,Date time);

    @Delete("delete from contract_log")
    int deleteAll();
}
