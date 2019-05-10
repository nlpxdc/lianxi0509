package io.cjf.lianxi0509.controller;

import io.cjf.lianxi0509.dao.UserMapper;
import io.cjf.lianxi0509.dto.UserCreateDTO;
import io.cjf.lianxi0509.exception.ClientException;
import io.cjf.lianxi0509.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        String pwdToStore = String.format("%s$%s",encPwd,saltStr);
        user.setEncryptedPassword(pwdToStore);
        userMapper.insert(user);
        return user.getUserId();
    }
}
