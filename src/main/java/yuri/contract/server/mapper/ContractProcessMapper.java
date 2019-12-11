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

    @Select("select contractNum from contract_process where type = -1")
    List<String> selectNumOfUnAssigned();

    @Select("select contractNum from contract_process where type = -1 and concat_ws(contractNum,type,state,userName,content,time) like concat('%', #{content}, '%')")
    List<String> fuzzySelectNumOfUnAssigned(String content);

    @Select("select contractNum from contract_process where type = #{type} and userName = #{userName}")
    List<String> selectNumOfNeededProcess(String userName,int type);

    @Select("select contractNum from contract_process where type = #{type} and userName = #{userName} and concat_ws(contractNum,type,state,userName,content,time) like concat('%', #{content}, '%'")
    List<String> fuzzySelectNumOfNeededProcess(String userName,String content,int type);

    @Select("select count(*) from contract_process where type = #{type} and state = #{state}")
    int getNumberOfNeededTypeState(int type, int state);

    @Insert("insert into contract_process values(#{contractNum},#{type},#{state},#{userName},#{content},now())")
    int insert(String contractNum, int type, int state, String userName, String content);

    @Update("update contract_process set content =#{content} where contractNum =#{contractNum} and type = #{type} and userName =#{userName}")
    int updateContent(String content, String contractNum, int type, String userName);

    @Update("update contract_process set state =#{state} where contractNum =#{contractNum} and type = #{type} and userName =#{userName}")
    int updateState(int state, String contractNum, int type, String userName);

    @Delete("delete from contract_process where contractNum =#{contractNum} and type = #{type} and userName =#{userName}")
    int delete(String contractNum, int type, String userName);

    @Delete("delete from contract_process")
    int deleteAll();
}
