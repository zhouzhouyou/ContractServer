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
    Contract select(int num);

    @Select("select * from contract")
    List<Contract> selectAll();

    @Select("select * from contract where concat_ws(num,name,customerNum,begin,end) like concat('%', #{content}, '%')")
    List<Contract> fuzzyQuery(String content);

    @Select("SELECT Auto_increment FROM information_schema.tables WHERE Table_Schema='yuri' and table_name='contract'")
    int getLastContractNum();

//    @Insert("insert into contract (name,customerNum,begin,end,content,userName) values(#{name},#{customerNum},#{begin},#{end},#{content},#{userName})")
//    int insert(String name, int customerNum, Date begin, Date end, String content, String userName);

    @Insert("insert into contract (name,customerNum,begin,end,content,userName) " +
            "values(#{contract.name},#{contract.customer},#{contract.begin},#{contract.end}," +
            "#{contract.content},#{userName})")
    @Options(useGeneratedKeys = true, keyProperty = "contract.num")
    int insert(Contract contract, String userName);

    @Update("update contract set content =#{content} where num =#{num}")
    int updateContent(int num, String content);

    @Delete("delete from contract where num =#{num}")
    int delete(int num);

    @Delete("delete from contract")
    int deleteAll();
}
