package yuri.contract.server.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import yuri.contract.server.model.ContractProcess;

import java.util.List;

@Mapper
@Component
public interface ContractProcessMapper {
    @Select("select distinct userName from contract_process where contractNum =#{contractNum} and type = #{type}")
    List<String> selectOperator(int contractNum, int type);

    @Select("select distinct userName from contract_process where contractNum =#{contractNum} and type = #{type} and state = #{state}")
    List<String> selectOperatorWithState(int contractNum, int type, int state);

    @Select("select distinct userName from contract_process where contractNum =#{contractNum} and type = 2 and state = 2")
    List<String> rejectOperator(int contractNum);

    @Select("select * from contract_process")
    List<ContractProcess> selectAll();

    @Select("select * from contract_process where contractNum =#{contractNum} and type = #{type} and state = 1")
    List<ContractProcess> selectFinishedProcesses(int contractNum, int type);

    @Select("select contractNum from contract_process where type = -1")
    List<Integer> selectNumOfUnAssigned();

    @Select("select contractNum from contract_process where type = -1 and concat_ws(contractNum,type,state,userName,content,time) like concat('%', #{content}, '%')")
    List<Integer> fuzzySelectNumOfUnAssigned(String content);

    @Select("select contractNum from contract_process where type = #{type} and userName = #{userName} and state = #{state}")
    List<Integer> selectUnfinishedContractNum(String userName, int type, int state);

    @Select("select contractNum from contract_process where type = 2 and state = 2")
    List<Integer> selectRejectContractNums();

    @Select("select contractNum from contract_process where type = #{type} and userName = #{userName} and state = #{state} and concat_ws(contractNum,type,state,userName,content,time) like concat('%',#{content},'%')")
    List<Integer> fuzzySelectNumOfNeededProcess(String userName, String content, int type, int state);

    @Select("select count(*) from contract_process where type = #{type} and state = #{state}")
    int getNumberOfNeededTypeState(int type, int state);

    @Select("select count(*) from contract_process where type = #{type} and state != 1 and contractNum = #{contractNum}")
    int getUnfinishedOrDeniedProcess(int type, int contractNum);

    @Select("select count(*) from contract_process where type = 2 and state = 2 and contractNum = #{contractNum}")
    int getDenyCount(int contractNum);

    @Insert("insert into contract_process (contractNum, type, state, userName, content, time) values(#{contractNum},#{type},#{state},#{userName},#{content},now())")
    int insert(int contractNum, int type, int state, String userName, String content);

    @Update("update contract_process set content =#{content} where contractNum =#{contractNum} and type = #{type} and userName =#{userName}")
    int updateContent(String content, int contractNum, int type, String userName);

    @Update("update contract_process set state =#{state} where contractNum =#{contractNum} and type = #{type} and userName =#{userName}")
    int updateState(int state, int contractNum, int type, String userName);

    @Delete("delete from contract_process where contractNum =#{contractNum} and type = #{type} and userName =#{userName}")
    int delete(int contractNum, int type, String userName);

    @Delete("delete from contract_process")
    int deleteAll();

    @Select("select count(*) from contract_process where type != -1 and state = 0 and userName =#{username}")
    int hasAnyJob(String username);
}
