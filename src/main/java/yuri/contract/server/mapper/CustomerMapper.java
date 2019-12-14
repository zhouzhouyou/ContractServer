package yuri.contract.server.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import yuri.contract.server.model.Customer;

import java.util.List;

@Mapper
@Component
public interface CustomerMapper {
    @Select("select * from customer where concat_ws (num, name, address, tel, fax, code, bank, account, other) " +
            "like concat('%', #{query}, '%');")
    List<Customer> fuzzyQuery(String query);

    @Select("select * from customer where num =#{num}")
    Customer select(Integer num);

    @Select("select * from customer")
    List<Customer> selectAll();

    @Insert("insert into customer (name, address, tel, fax, code, bank, account, other) " +
            "values(#{name},#{address},#{tel},#{fax},#{code},#{bank},#{account},#{other})")
    int insert(String name, String address,
               String tel, String fax, String code,
               String bank, String account, String other);

    @Update("update customer set name=#{name}, address=#{address}, tel=#{tel}, fax=#{fax}, " +
            "code=#{code}, bank=#{bank}, account=#{account}, other=#{other} where num=#{num}")
    int update(Integer num ,String name, String address,
               String tel, String fax, String code,
               String bank, String account, String other);

    @Update("update customer set name =#{name} where num =#{num}")
    int updateName(String name, Integer num);

    @Update("update customer set address =#{address} where num =#{num}")
    int updateAddress(String address, Integer num);

    @Update("update customer set tel =#{tel} where num =#{num}")
    int updateTel(String tel, Integer num);

    @Update("update customer set fax =#{fax} where num =#{num}")
    int updateFax(String fax, Integer num);

    @Update("update customer set code =#{code} where num =#{num}")
    int updateCode(String code, Integer num);

    @Update("update customer set bank =#{bank} where num =#{num}")
    int updateBank(String bank, Integer num);

    @Update("update customer set account =#{account} where num =#{num}")
    int updateAccount(String account, Integer num);

    @Delete("delete from customer where num =#{num}")
    int delete(Integer num);

    @Delete("delete from customer")
    int deleteAll();
}
