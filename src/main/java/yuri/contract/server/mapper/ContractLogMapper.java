package yuri.contract.server.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import yuri.contract.server.model.ContractLog;

import java.sql.Date;
import java.util.List;

@Mapper
@Component
public interface ContractLogMapper {
    @Select("select * from contract_log where userName =#{userName}")
    List<ContractLog> selectByUserName(String userName);

    @Select("select * from contract_log")
    List<ContractLog> selectAll();

    @Insert("insert into contract_log (username, content, time) values(#{userName}, #{content}, now())")
    int insert(String userName,String content);

    @Delete("delete from contract_log where userName =#{userName} and time =#{time}")
    int delete(String userName,Date time);

    @Delete("delete from contract_log")
    int deleteAll();
}
