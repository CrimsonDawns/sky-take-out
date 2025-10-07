package com.sky.service.impl;


import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;


    /**
     * 添加购物车
     *
     * @param shoppingCartDTO
     */
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        //进行属性拷贝
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());

        //判断当前加入购物车的商品是否已经存在
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);


        if (list != null && list.size() > 0) {
            ShoppingCart cart = list.get(0);
            //查到数据对购物车数据+1
            cart.setNumber(cart.getNumber() + 1);

            shoppingCartMapper.updateById(cart);
        } else {
            //不存在对购物车数据进行插入
            Long setmealId = shoppingCart.getSetmealId();
            Long dishId = shoppingCart.getDishId();

            //判断添加的是菜品还是套餐
            if (dishId != null) {
                //添加的是菜品
                Dish dish = dishMapper.selectById(dishId);

                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());

            } else {
                //添加的是套餐
                Setmeal setmeal = setmealMapper.selectById(setmealId);

                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());

            //插入数据
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 查看购物车
     *
     * @return
     */
    @Override
    public List<ShoppingCart> list() {

        Long userId = BaseContext.getCurrentId();

        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(userId)
                .build();

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        return list;
    }
}
