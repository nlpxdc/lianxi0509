package io.cjf.lianxi0509.service;

import io.cjf.lianxi0509.dto.MenuNode;
import io.cjf.lianxi0509.po.Menu;

import java.util.List;

public interface MenuService {

    List<Menu> getAll();

    List<MenuNode> getChildren(Integer rootMenuId, List<Menu> usedMenus);

    List<Integer> getMenuChain(Integer childMenuId);
}
