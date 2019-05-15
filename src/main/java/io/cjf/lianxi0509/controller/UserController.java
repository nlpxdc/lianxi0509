package io.cjf.lianxi0509.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.cage.Cage;
import io.cjf.lianxi0509.constant.Constant;
import io.cjf.lianxi0509.dao.UserMapper;
import io.cjf.lianxi0509.dto.*;
import io.cjf.lianxi0509.exception.ClientException;
import io.cjf.lianxi0509.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@EnableAutoConfiguration
@CrossOrigin
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SecureRandom secureRandom;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private Cage cage;



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

//    @GetMapping(value = "/getCaptcha", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @GetMapping(value = "/getCaptcha", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getCaptcha(){
        byte[] bytes = secureRandom.generateSeed(2);
        String hexStr = DatatypeConverter.printHexBinary(bytes);
        String sessionId = httpSession.getId();
        httpSession.setAttribute(sessionId,hexStr);

        byte[] draw = cage.draw(hexStr);

        return draw;
    }

    @GetMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, @RequestParam String captcha) throws ClientException {

        String sessionId = httpSession.getId();
        String captchaOrigin = (String) httpSession.getAttribute(sessionId);
        if (!captcha.equalsIgnoreCase(captchaOrigin)){
            throw new ClientException(0,"captcha is invalid");
        }

        User user = userMapper.selectByUsername(username);
        if (user == null){
            throw new ClientException(1,"user doesn't exist");
        }
        String encryptedPassword = user.getEncryptedPassword();
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), encryptedPassword);
        if (!result.verified){
            throw new ClientException(2,"password is incorrect");
        }

        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUserId(user.getUserId());
        userLoginDTO.setUsername(user.getUsername());
        userLoginDTO.setMobile(user.getMobile());
        userLoginDTO.setEmail(user.getEmail());

        String tokenOrigin = JSON.toJSONString(userLoginDTO);
//        String s = DatatypeConverter.printHexBinary(tokenOrigin.getBytes());
        Algorithm algorithm = Algorithm.HMAC256("cjf");
        String token = JWT.create()
                .withIssuer("tecent")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(1657815588152L))
                .withSubject(username)
                .withClaim("mobile",user.getMobile())
                .withClaim("email",user.getEmail())
                .sign(algorithm);

        return token;

//        httpSession.setAttribute(sessionId, user);
    }

    @PostMapping("/changeSelfPassword")
    public void changeSelfPassword(@RequestBody ChangeSelfPasswordDTO changeSelfPasswordDTO, @RequestAttribute("currentUsername") String username) throws ClientException {



        User currentUser = userMapper.selectByUsername(username);

        if (currentUser == null){
            throw new ClientException(3,"user doesn't login");
        }

        String encryptedPassword = currentUser.getEncryptedPassword();
        String originPwd = changeSelfPasswordDTO.getOriginPwd();
        BCrypt.Result result = BCrypt.verifyer().verify(originPwd.toCharArray(), encryptedPassword);
        if (!result.verified){
            throw new ClientException(2,"password is incorrect");
        }
        String newPwd = changeSelfPasswordDTO.getNewPwd();
        String encPwd = BCrypt.withDefaults().hashToString(12, newPwd.toCharArray());
        currentUser.setEncryptedPassword(encPwd);
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
