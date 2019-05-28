package io.cjf.lianxi0509.controller;

import io.cjf.lianxi0509.dao.MenuMapper;
import io.cjf.lianxi0509.dao.RoleMenuMapper;
import io.cjf.lianxi0509.dao.UserRoleMapper;
import io.cjf.lianxi0509.dto.MenuNode;
import io.cjf.lianxi0509.po.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @GetMapping("/getTree")
    public List<MenuNode> getTree(@RequestParam Integer rootMenuId,
                                  @RequestParam Integer currentUserId){

        List<Integer> myRoleIds = userRoleMapper.selectRoleIds(currentUserId);

        List<Integer> myMenuIds = roleMenuMapper.selectMenuIds(myRoleIds);

        HashSet<Integer> resultMenuIds = new HashSet<>();

        for (Integer myMenuId : myMenuIds) {
            List<Integer> menuChain = getMenuChain(myMenuId);
            for (Integer menuId : menuChain) {
                resultMenuIds.add(menuId);
            }
        }

        List<Menu> menus = menuMapper.selectByMenuIds(resultMenuIds);

        return null;
    }

    private List<Integer> getMenuChain(Integer myMenuId){
        LinkedList<Integer> menuIds = new LinkedList<>();
        menuIds.add(myMenuId);

        Menu myMenu = menuMapper.selectByPrimaryKey(myMenuId);
        Integer tempId = myMenu.getParentId();
        while (tempId != null && tempId != 0){
            menuIds.add(tempId);
            Menu menu = menuMapper.selectByPrimaryKey(tempId);
            tempId = menu.getParentId();
        }
        return menuIds;
    }



}
