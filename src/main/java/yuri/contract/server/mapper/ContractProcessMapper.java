package yuri.contract.server.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import yuri.contract.server.model.ContractProcess;

import java.sql.Date;
import java.util.List;

@Mapper
@Component
public interface ContractProcessMapper {
    @Select("select * from contract_process where contractNum =#{contractNum} and type = #{type} and userName =#{userName}")
    ContractProcess select(String contractNum, int type, String userName);

    @Select("select * from contract_process")
    List<ContractProcess> selectAll();

    @Insert("insert into contract_process values(#{contractNum},#{type},#{state},#{userName},#{content},#{time})")
    int insert(String contractNum, int type, int state, String userName, String content, Date time);

    @Update("update contract_process set content =#{content}")
    int updateContent(String content);

    @Update("update contract_process set state =#{state}")
    int updateState(int state);

    @Delete("delete from contract_process where contractNum =#{contractNum} and type = #{type} and userName =#{userName}")
    int delete(String contractNum, int type, String userName);

    @Delete("delete from contract_process")
    int deleteAll();
}
