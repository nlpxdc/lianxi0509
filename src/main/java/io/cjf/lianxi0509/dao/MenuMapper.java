package io.cjf.lianxi0509.dao;

import io.cjf.lianxi0509.dto.MenuNode;
import io.cjf.lianxi0509.po.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface MenuMapper {
    int deleteByPrimaryKey(Integer menuId);

    int insert(Menu record);

    int insertSelective(Menu record);

    Menu selectByPrimaryKey(Integer menuId);

    List<MenuNode> selectChildren(@Param("menuId") Integer menuId);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);

//    custom
    List<Menu> selectByMenuIds(@Param("menuIds") Set<Integer> menuIds);
}