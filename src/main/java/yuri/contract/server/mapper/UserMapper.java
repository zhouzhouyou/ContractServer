package yuri.contract.server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import yuri.contract.server.model.User;

@Mapper
@Component
public interface UserMapper {
    @Select("select * from user where name=#{name} and password=#{password}")
    User select(String name, String password);
}
