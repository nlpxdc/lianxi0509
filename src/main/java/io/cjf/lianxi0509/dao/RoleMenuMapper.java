package io.cjf.lianxi0509.dao;

import io.cjf.lianxi0509.po.Menu;
import io.cjf.lianxi0509.po.RoleMenuKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMenuMapper {
    int deleteByPrimaryKey(RoleMenuKey key);

    int insert(RoleMenuKey record);

    int insertSelective(RoleMenuKey record);

    List<String> selectUrls(@Param("roleIds") List<Integer> roleIds);

    List<Integer> selectMenuIds(@Param("roleIds") List<Integer> roleIds);
}