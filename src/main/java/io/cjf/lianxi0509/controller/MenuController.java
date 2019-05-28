package io.cjf.lianxi0509.controller;

import io.cjf.lianxi0509.dto.MenuNode;
import io.cjf.lianxi0509.po.Menu;
import io.cjf.lianxi0509.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/getTree")
    public List<MenuNode> getTree(@RequestParam Integer rootMenuId) {
        List<Menu> allMenus = menuService.getAll();
        List<MenuNode> menuNodes = menuService.getChildren(rootMenuId, allMenus);
        return menuNodes;
    }

    @GetMapping("/getUserTree")
    public List<MenuNode> getUserTree(@RequestParam Integer rootMenuId,
                                      @RequestParam Integer currentUserId) {
        return null;

    }

//    List<Integer> myRoleIds = userRoleMapper.selectRoleIds(currentUserId);
//
//    List<Integer> myMenuIds = roleMenuMapper.selectMenuIds(myRoleIds);
//
//    HashSet<Integer> usedMenuIds = new HashSet<>();
//
//        for (Integer myMenuId : myMenuIds) {
//        List<Integer> menuChain = getMenuChain(myMenuId);
//        for (Integer menuId : menuChain) {
//            usedMenuIds.add(menuId);
//        }
//    }
//
//    List<MenuNode> menuNodes = getChildren(rootMenuId, allMenus);

}
