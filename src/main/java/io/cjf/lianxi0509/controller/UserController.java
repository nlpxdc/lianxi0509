package io.cjf.lianxi0509.controller;

import io.cjf.lianxi0509.constant.Constant;
import io.cjf.lianxi0509.dao.UserMapper;
import io.cjf.lianxi0509.dto.UserCreateDTO;
import io.cjf.lianxi0509.exception.ClientException;
import io.cjf.lianxi0509.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;

@RestController
@RequestMapping("/user")
@EnableAutoConfiguration
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SecureRandom secureRandom;

    @Autowired
    private HttpSession httpSession;

    @PostMapping("/create")
    public Integer create(@RequestBody UserCreateDTO userCreateDTO) throws ClientException {
        String username = userCreateDTO.getUsername();
        User userOrigin = userMapper.selectByUsername(username);
        if (userOrigin != null){
            throw new ClientException(88,"user exist");
        }
        User user = new User();
        user.setUsername(username);
        secureRandom = new SecureRandom();
        byte[] salt = secureRandom.generateSeed(4);
        String saltStr = DatatypeConverter.printHexBinary(salt);
        String password = userCreateDTO.getPassword();
        String toEncPwd = password + saltStr;
        String encPwd = DigestUtils.md5DigestAsHex(toEncPwd.getBytes());
        String passwordSeperator = Constant.passwordSeperator;
        String pwdToStore = String.format("%s%s%s",encPwd, passwordSeperator, saltStr);
        user.setEncryptedPassword(pwdToStore);
        userMapper.insert(user);
        return user.getUserId();
    }

    @GetMapping("/login")
    public void login(@RequestParam String username, @RequestParam String password) throws ClientException {
        User user = userMapper.selectByUsername(username);
        if (user == null){
            throw new ClientException(1,"user doesn't exist");
        }
        String encryptedPassword = user.getEncryptedPassword();
//        String passwordSeperator = Constant.passwordSeperator;
        String[] encPwdSaltStr = encryptedPassword.split("\\$");
        String encPwdOrigin = encPwdSaltStr[0];
        String saltStr = encPwdSaltStr[1];
        String toEncPwd = password+saltStr;
        String encPwd = DigestUtils.md5DigestAsHex(toEncPwd.getBytes());
        if (!encPwd.equals(encPwdOrigin)){
            throw new ClientException(2,"password is incorrect");
        }
        String sessionId = httpSession.getId();
        httpSession.setAttribute(sessionId, user);
    }
}
