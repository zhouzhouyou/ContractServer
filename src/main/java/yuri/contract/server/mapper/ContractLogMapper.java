package yuri.contract.server.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import yuri.contract.server.model.ContractLog;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Mapper
@Component
public interface ContractLogMapper {
    @Select("select * from contract_log where userName =#{userName}")
    List<ContractLog> selectByUserName(String userName);

    @Select("select * from contract_log where userName =#{userName} and (time between #{fromTime} and #{toTime})")
    List<ContractLog> selectLogWithUserName(String userName,Timestamp fromTime, Timestamp toTime);

    @Select("select * from contract_log where time between #{fromTime} and #{toTime}")
    List<ContractLog> selectLogWithoutUserName(Timestamp fromTime, Timestamp toTime);

    @Select("select * from contract_log where concat_ws (userName, content, time) " +
            "like concat('%', #{query}, '%');")
    List<ContractLog> fuzzyQuery(String query);

    @Select("select * from contract_log")
    List<ContractLog> selectAll();

    @Select("select count(*) from contract_log")
    int getNumberOfLog();

    @Insert("insert into contract_log (username, content, time) values(#{userName}, #{content}, now())")
    int insert(String userName, String content);

    @Delete("delete from contract_log where userName =#{userName} and time =#{time}")
    int delete(String userName, Date time);

    @Delete("delete from contract_log")
    int deleteAll();
}
