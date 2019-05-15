package io.cjf.lianxi0509.dao;

import io.cjf.lianxi0509.po.UserRoleKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRoleMapper {
    int deleteByPrimaryKey(UserRoleKey key);

    int insert(UserRoleKey record);

    int insertSelective(UserRoleKey record);

    List<Integer> selectRoleIds(@Param("userId") Integer userId);
}