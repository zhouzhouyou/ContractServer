package yuri.contract.server.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import yuri.contract.server.model.ContractAttachment;

import java.sql.Date;
import java.util.List;

@Mapper
@Component
public interface ContractAttachmentMapper {
    @Select("select * from contract_attachment where contractNum =#{contractNum}")
    ContractAttachment select(int contractNum);

    @Select("select * from contract_attachment")
    List<ContractAttachment> selectAll();

    @Select("select count(*) from contract_attachment where fileName is like concat('%', #{originName}, '%'")
    int getNewFileNameCount(String originName);

    @Insert("insert into contract_attachment values(#{contractNum},#{fileName},#{path},#{type},now())")
    int insert(int contractNum, String fileName, String path, String type);

    @Delete("delete from contract_attachment where contractNum =#{contractNum}")
    int delete(int contractNum);

    @Delete("delete from contract_attachment")
    int deleteAll();

}
