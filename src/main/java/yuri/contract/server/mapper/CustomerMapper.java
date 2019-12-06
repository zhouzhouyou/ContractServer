package yuri.contract.server.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import yuri.contract.server.model.Customer;

import java.util.List;

@Mapper
@Component
public interface CustomerMapper {
    @Select("select * from customer where num =#{num}")
    Customer select(String num);

    @Select("select * from customer")
    List<Customer> selectAll();

    @Insert("insert into customer values(#{num},#{name},#{address},#{tel},#{fax},#{code},#{bank},#{account})")
    int insert(String num, String name, String address, String tel, String fax, String code, String bank, String account);

    @Update("update customer set name =#{name} where num =#{num}")
    int updateName(String name, String num);

    @Update("update customer set address =#{address} where num =#{num}")
    int updateAddress(String address, String num);

    @Update("update customer set tel =#{tel} where num =#{num}")
    int updateTel(String tel, String num);

    @Update("update customer set fax =#{fax} where num =#{num}")
    int updateFax(String fax, String num);

    @Update("update customer set code =#{code} where num =#{num}")
    int updateCode(String code, String num);

    @Update("update customer set bank =#{bank} where num =#{num}")
    int updateBank(String bank, String num);

    @Update("update customer set account =#{account} where num =#{num}")
    int updateAccount(String account, String num);

    @Delete("delete from customer where num =#{num}")
    int delete(String num);

    @Delete("delete from customer")
    int deleteAll();
}
