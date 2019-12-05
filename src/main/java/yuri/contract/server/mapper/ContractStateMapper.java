package yuri.contract.server.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import yuri.contract.server.model.ContractState;
import yuri.contract.server.model.util.OperationType;

import java.sql.Date;
import java.util.List;

@Mapper
@Component
public interface ContractStateMapper {
    @Select("select * from contract_state where contractNum =#{contractNum}")
    ContractState select(String contractNum);

    @Select("select * from contract_state")
    List<ContractState> selectAll();

    @Insert("insert into contract_state values(#{contractNum},#{type},#{time})")
    int insert(String contractNum, OperationType type, Date time);

    @Delete("delete from contract_state where contractNum =#{contractNum}")
    int delete(String contractNum);

    @Delete("delete from contract_state")
    int deleteAll();
}
