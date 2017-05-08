package com.zmc.service;

import com.zmc.helper.DataBaseHelper;
import com.zmc.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by zhongmc on 2017/5/8.
 */
public class CustomerService {
    private final static Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
    /**
     * 获取客户列表
     * @return
     */
    public List<Customer> getCustomerList(){
        // TODO
        /*Connection connection = null;
        List<Customer> res = new ArrayList<Customer>();
        try {
            connection = DataBaseHelper.getConnection();
            String sql = "select * from customer";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Customer c = new Customer();
                c.setId(resultSet.getLong("id"));
                c.setName(resultSet.getString("name"));
                c.setContact(resultSet.getString("contact"));
                c.setEmail(resultSet.getString("email"));
                c.setRemark(resultSet.getString("remark"));
                res.add(c);
            }
        } catch (SQLException e) {
            LOGGER.error("execute sql failure",e);
        }finally {
            DataBaseHelper.closeConnection(connection);
        }
        return res;*/
        String sql = "select * from customer";
        return DataBaseHelper.queryEntityList(Customer.class,sql,null);
    }

    /**
     * 获取客户
     * @param id
     * @return
     */
    public Customer getCustomer(long id){
        // TODO
        String sql = "select * from customer where id=?";
        return  DataBaseHelper.queryEntity(Customer.class,sql,id);
    }

    /**
     * 创建客户
     * @param fieldMap
     * @return
     */
    public boolean createCustomer(Map<String,Object> fieldMap){
        // TODO
        return false;
    }

    /**
     * 更新客户
     * @param id
     * @param fieldMap
     * @return
     */
    public boolean updateCustomer(long id,Map<String,Object> fieldMap){
        // TODO
        return false;
    }

    /**
     * 删除客户
     * @param id
     * @return
     */

    public boolean deleteCustomer(long id){
        // TODO
        return false;
    }
}
