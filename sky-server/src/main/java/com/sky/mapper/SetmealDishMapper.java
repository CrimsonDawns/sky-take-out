package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    List<Long> getSetmealIdsByDishId(List<Long> dishIds);

    /**
     * 插入套餐与菜品关联的数据
     *
     * @param setmealDishes
     */
    void insertSetmealDish(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id删除套餐与菜品的关系
     * @param setmealId
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteByid(Long setmealId);

    @Select("select * from setmeal_dish where setmeal_id =#{setmealId}")
    List<SetmealDish> getSetmealIdByDishId(Long setmealId);

    /**
     * 修改套餐和菜品之间关系的数据
     * @param sd
     */
    void update(SetmealDish sd);
}
