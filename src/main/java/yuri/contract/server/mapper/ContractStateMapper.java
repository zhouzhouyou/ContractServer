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
    ContractState select(int contractNum, int status);

    @Select("select * from contract_state")
    List<ContractState> selectAll();

    @Select("select count(*) from contract_state where contractNum =#{contractNum}")
    int getContractStatus(int contractNum);

    @Insert("insert into contract_state values(#{contractNum},#{status},now())")
    int insert(int contractNum, int status);

    @Delete("delete from contract_state where contractNum =#{contractNum}")
    int delete(int contractNum);

    @Delete("delete from contract_state")
    int deleteAll();
}
