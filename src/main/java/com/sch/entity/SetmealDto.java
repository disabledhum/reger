package com.sch.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SetmealDto extends Setmeal implements Serializable {
    private List<SetmealDish>setmealDishes;
    private String categoryName;
}
