package io.cjf.lianxi0509.service.impl;

import io.cjf.lianxi0509.dao.MenuMapper;
import io.cjf.lianxi0509.dto.MenuNode;
import io.cjf.lianxi0509.po.Menu;
import io.cjf.lianxi0509.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> getAll() {
        List<Menu> menus = menuMapper.selectAll();
        return menus;
    }

    @Override
    public List<MenuNode> getChildren(Integer rootMenuId, List<Menu> usedMenus) {
        LinkedList<MenuNode> menuNodes = new LinkedList<>();

        List<Menu> menus = usedMenus.stream().filter(m -> m.getParentId() == rootMenuId).collect(Collectors.toList());
        for (Menu menu : menus) {
            MenuNode menuNode = new MenuNode();
            menuNode.setMenuId(menu.getMenuId());
            menuNode.setName(menu.getName());
            menuNode.setUrl(menu.getUrl());
            menuNode.setSubMenus(getChildren(menu.getMenuId(), usedMenus));
            menuNodes.add(menuNode);
        }
        return menuNodes;
    }

    @Override
    public List<Integer> getMenuChain(Integer childMenuId) {
        LinkedList<Integer> menuIds = new LinkedList<>();

        Integer tempId = childMenuId;
        do {
            menuIds.add(tempId);
            Menu menu = menuMapper.selectByPrimaryKey(tempId);
            tempId = menu.getParentId();
        }while (tempId != null && tempId != 0);

        return menuIds;
    }
}
