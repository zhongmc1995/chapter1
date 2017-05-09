package com.zmc.test;

import com.zmc.model.Customer;
import com.zmc.service.CustomerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhongmc on 2017/5/8.
 * CustomerService 测试
 */
public class CustomerServiceTest {
    private final CustomerService customerService;
    public CustomerServiceTest(){
        customerService = new CustomerService();
    }
    /*public CustomerServiceTest(CustomerService customerService){
        this.customerService = customerService;
    }*/
    @Before
    public void init(){
        // TODO 初始化数据库
        //new CustomerServiceTest(new CustomerService());
    }

    @Test
    public void getCustomerTest(){
        long id = 2;
        Customer customer = customerService.getCustomer(id);
        System.out.println(customer);
        Assert.assertNotNull(customer);
        List<Customer> customerList = customerService.getCustomerList();
        for (Customer c : customerList){
            System.out.println(c);
        }
    }

    @Test
    public void createCustomerTest(){
        Map<String,Object> fieldMap = new HashMap<String,Object>();
        fieldMap.put("name","zmc");
        fieldMap.put("contact","Jim");
        fieldMap.put("telphone","13177898888");
        fieldMap.put("email","zhongmc@163.com");

        boolean result = customerService.createCustomer(fieldMap);
        Assert.assertTrue(result);
    }

    @Test
    public void updaeCustomerTest(){
        long id = 2;
        Map<String,Object> fieldMap = new HashMap<String,Object>();
        fieldMap.put("contact","Zmc");
        fieldMap.put("remark","普通会员");
        boolean result = customerService.updateCustomer(id,fieldMap);
        Assert.assertTrue(result);
    }

    @Test
    public void deleteCustomerTest(){
        long id = 1;
        boolean result = customerService.deleteCustomer(id);
        Assert.assertTrue(result);
    }
}
