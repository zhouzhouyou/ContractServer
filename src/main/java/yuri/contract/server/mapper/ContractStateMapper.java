package yuri.contract.server.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import yuri.contract.server.model.ContractState;

import java.sql.Date;
import java.util.List;

@Mapper
@Component
public interface ContractStateMapper {
    @Select("select * from contract_state where contractNum =#{contractNum} and status =#{status}")
    ContractState select(String contractNum, int status);

    @Select("select * from contract_state")
    List<ContractState> selectAll();

    @Insert("insert into contract_state values(#{contractNum},#{status},now())")
    int insert(String contractNum, int status);

    @Delete("delete from contract_state where contractNum =#{contractNum}")
    int delete(String contractNum);

    @Delete("delete from contract_state")
    int deleteAll();
}
