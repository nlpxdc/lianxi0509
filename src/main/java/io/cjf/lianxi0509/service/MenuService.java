package io.cjf.lianxi0509.service;

import io.cjf.lianxi0509.dto.MenuNode;
import io.cjf.lianxi0509.po.Menu;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface MenuService {

    List<Menu> getAll();

    List<Menu> getByMenuIds(Collection<Integer> menuIds);

    List<MenuNode> getChildren(Integer rootMenuId, List<Menu> usedMenus, List<Integer> accessMenuIds);

    List<Integer> getMenuChain(Integer childMenuId);

    Set<Integer> getUsedMenuIds(List<Integer> menuIds);
}
