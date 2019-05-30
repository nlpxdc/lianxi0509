package io.cjf.lianxi0509.controller;

import io.cjf.lianxi0509.dao.RoleMenuMapper;
import io.cjf.lianxi0509.dao.UserRoleMapper;
import io.cjf.lianxi0509.dto.MenuNode;
import io.cjf.lianxi0509.po.Menu;
import io.cjf.lianxi0509.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private MenuService menuService;

    @GetMapping("/getTree")
    @Cacheable("MenuTree")
    public List<MenuNode> getTree(@RequestParam Integer rootMenuId) {
        List<Menu> allMenus = menuService.getAll();
        List<Integer> allMenuIds = allMenus.stream().map(m -> m.getMenuId()).collect(Collectors.toList());
        List<MenuNode> menuNodes = menuService.getChildren(rootMenuId, allMenus, allMenuIds);
        return menuNodes;
    }

    @GetMapping("/getUserTree")
    public List<MenuNode> getUserTree(@RequestParam Integer rootMenuId,
                                      @RequestParam Integer userId) {
        List<Integer> userRoleIds = userRoleMapper.selectRoleIds(userId);
        List<Integer> userMenuIds = roleMenuMapper.selectMenuIds(userRoleIds);
        Set<Integer> usedMenuIds = menuService.getUsedMenuIds(userMenuIds);
        List<Menu> userAllMenus = menuService.getByMenuIds(usedMenuIds);
        List<MenuNode> userMenuNodes = menuService.getChildren(rootMenuId, userAllMenus, userMenuIds);
        return userMenuNodes;
    }

}
