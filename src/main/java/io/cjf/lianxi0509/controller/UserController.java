package io.cjf.lianxi0509.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.cjf.lianxi0509.constant.Constant;
import io.cjf.lianxi0509.dao.UserMapper;
import io.cjf.lianxi0509.dto.AvatarDTO;
import io.cjf.lianxi0509.dto.ChangeSelfPasswordDTO;
import io.cjf.lianxi0509.dto.ChangeUserPasswordDTO;
import io.cjf.lianxi0509.dto.UserCreateDTO;
import io.cjf.lianxi0509.exception.ClientException;
import io.cjf.lianxi0509.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

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
        String password = userCreateDTO.getPassword();
        String encPwd = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        user.setEncryptedPassword(encPwd);
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
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), encryptedPassword);
        if (!result.verified){
            throw new ClientException(2,"password is incorrect");
        }
        String sessionId = httpSession.getId();
        httpSession.setAttribute(sessionId, user);
    }

    @PostMapping("/changeSelfPassword")
    public void changeSelfPassword(@RequestBody ChangeSelfPasswordDTO changeSelfPasswordDTO) throws ClientException {
        String sessionId = httpSession.getId();
        User currentUser = (User) httpSession.getAttribute(sessionId);
        if (currentUser == null){
            throw new ClientException(3,"user doesn't login");
        }
        String originPwd = changeSelfPasswordDTO.getOriginPwd();
        String encryptedPassword = currentUser.getEncryptedPassword();
        String[] split = encryptedPassword.split("\\$");
        String encPwdOrigin = split[0];
        String saltStr = split[1];
        String toEncPwd = originPwd + saltStr;
        String encPwd = DigestUtils.md5DigestAsHex(toEncPwd.getBytes());
        if (!encPwd.equals(encPwdOrigin)){
            throw new ClientException(4,"origin password is invalid");
        }
        String newPwd = changeSelfPasswordDTO.getNewPwd();
        String newToEncPwd = newPwd + saltStr;
        String newEncPwd = DigestUtils.md5DigestAsHex(newToEncPwd.getBytes());
        String newStorePwd = String.format("%s%s%s",newEncPwd,Constant.passwordSeperator,saltStr);
        currentUser.setEncryptedPassword(newStorePwd);
        userMapper.updateByPrimaryKey(currentUser);
    }

    @PostMapping("/changeUserPassword")
    public void changeUserPassword(@RequestBody ChangeUserPasswordDTO changeUserPasswordDTO){
        String sessionId = httpSession.getId();
        User currentUser = (User) httpSession.getAttribute(sessionId);
        //todo check current user whether is admin
        byte[] salt = secureRandom.generateSeed(Constant.saltLength);
        String saltStr = DatatypeConverter.printHexBinary(salt);
        String newPwd = changeUserPasswordDTO.getNewPwd();
        String toEncPwd = newPwd+saltStr;
        String encPwd= DigestUtils.md5DigestAsHex(toEncPwd.getBytes());
        String toStorePwd = String.format("%s%s%s", encPwd, Constant.passwordSeperator, saltStr);
        String username = changeUserPasswordDTO.getUsername();
        User user = userMapper.selectByUsername(username);
        user.setEncryptedPassword(toStorePwd);
        userMapper.updateByPrimaryKey(user);
    }

    @PostMapping("/uploadAvatar")
    public String uploadAvatar(@RequestBody AvatarDTO avatarDTO) throws IOException {
        String imgDataStr = avatarDTO.getImgDataStr();
        byte[] imgData = Base64.getDecoder().decode(imgDataStr);
        String uuid = UUID.randomUUID().toString();
        FileOutputStream out = new FileOutputStream(uuid);
        out.write(imgData);
        out.close();
        return uuid;
    }

    @PostMapping("/uploadAvatar2")
    public String uploadAvatar2(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String uuid = UUID.randomUUID().toString();
        FileOutputStream out = new FileOutputStream(uuid+".jpg");
        out.write(multipartFile.getBytes());
        out.close();
        return null;
    }

    @PostMapping("/uploadAvatar3")
    public String uploadAvatar3(@RequestParam("file") MultipartFile[] multipartFile) throws IOException {
        return null;
    }
}
