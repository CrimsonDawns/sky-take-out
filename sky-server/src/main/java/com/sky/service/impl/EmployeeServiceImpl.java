package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //对密码进行MD5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 员工新增
     * 由于持久层进行数据库操作的是实体类，要进行类型的转换
     *
     * @param employeeDTO
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        //spring提供的工具类，将所有属性值拷贝到新的对象中
        BeanUtils.copyProperties(employeeDTO, employee);

        //设置状态
        employee.setStatus(StatusConstant.ENABLE);

        //设置密码，并进行加密
        employee.setPassword(DigestUtils.md5DigestAsHex
                (PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        //进行数据库操作
        employeeMapper.insert(employee);
    }

    /**
     * 员工分页查询
     *
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //startPage(int pageNum, int pageSize)
        //pageNum页数，pageSize当前页显示的页数
        //使用PageHelper协助分页
        PageHelper.startPage(employeePageQueryDTO.getPage(),
                employeePageQueryDTO.getPageSize());

        //在持久层进行数据库查询
        Page<Employee> page = employeeMapper.pageQueryByName(employeePageQueryDTO);

        //获取PageResult相关属性
        long total = page.getTotal();
        List<Employee> records = page.getResult();

        return new PageResult(total, records);
    }

    /**
     * 启用禁用员工
     *
     * @param status
     * @param id
     */
    @Override
    public void enableDisableEmployee(Integer status, Long id) {
        //构建Employee 对象
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();
        //进行数据更新操作
        employeeMapper.updateById(employee);
    }

    /**
     * 根据id查询员工
     *
     * @param id
     * @return
     */
    @Override
    public Employee queryById(Long id) {
        Employee employee = employeeMapper.selectById(id);
        //加强安全性
        employee.setPassword("****");
        return employee;
    }

    /**
     * 修改员工数据
     *
     * @param employeeDTO
     */
    @Override
    public void updateEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        //拷贝对象属性
        BeanUtils.copyProperties(employeeDTO, employee);

        employeeMapper.updateById(employee);
    }
}