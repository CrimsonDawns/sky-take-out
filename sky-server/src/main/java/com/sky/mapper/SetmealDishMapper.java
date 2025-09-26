package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    List<Long> getSetmealIdsByDishId(List<Long> dishIds);

    /**
     * 插入套餐与菜品关联的数据
     * @param setmealDishes
     */
    void insertSetmealDish(List<SetmealDish> setmealDishes);
}
