package yuri.contract.server.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface LoginMapper {
    @Select("select username from login where token=#{token}")
    String getUser(String token);

    @Insert("insert into login values (#{username}, #{token}")
    int insert(String username, String token);

    @Update("update login set token=#{token} where username=#{username}")
    int update(String username, String token);
}
