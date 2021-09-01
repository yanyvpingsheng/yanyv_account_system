package com.yanyv.account.controller;

import com.yanyv.account.entity.Account;
import com.yanyv.account.execptions.ParamsException;
import com.yanyv.account.model.RespBean;
import com.yanyv.account.service.AccountService;
import com.yanyv.account.util.AssertUtil;
import com.yanyv.account.util.Email;
import com.yanyv.account.util.StringUtil;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Random;

@Controller
@RequestMapping("api")
public class ApiController {
    @Autowired
    Email emailUtil;
    @Autowired
    private AccountService accountService;

    @RequestMapping("login")
    @ResponseBody
    public RespBean login(String email, String password, String pictureCode, Boolean remember, HttpSession session, HttpServletResponse response) {
        boolean needPictureCode = (boolean) session.getAttribute("needPictureCode");
        session.setAttribute("needPictureCode", true);
        AssertUtil.isTrue(needPictureCode && StringUtil.isEmpty(pictureCode), "图片验证码不能为空");
        AssertUtil.isTrue(needPictureCode && !isPictureCodeRight(session, pictureCode), "图片验证码错误");
        Account account = accountService.login(email, password);
        session.setAttribute("account", account);
        if (remember) {
            Cookie cookieEmail = new Cookie("email", email);
            cookieEmail.setPath("/");
            cookieEmail.setMaxAge(864000);
            response.addCookie(cookieEmail);
        }
        session.setAttribute("needPictureCode", false);
        return RespBean.success("登录成功");
    }

    /**
     * 注册API
     */
    @RequestMapping("register")
    @ResponseBody
    public RespBean apiRegister(String email, String name, String password, String surePassword, String pictureCode, String emailCode, HttpSession session) {
        AssertUtil.isTrue(StringUtil.isEmpty(email), "邮箱不能为空");
        AssertUtil.isTrue(StringUtil.isNotEmail(email), "邮箱格式错误");
        AssertUtil.isTrue(!(null == accountService.findByEmail(email)), "邮箱已注册，忘记密码请访问找回页面");
        AssertUtil.isTrue(StringUtil.isEmpty(name), "用户名不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(password), "密码不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(surePassword), "确认密码不能为空");
        AssertUtil.isTrue(!password.equals(surePassword), "密码与确认密码不同");
        AssertUtil.isTrue(StringUtil.isEmpty(pictureCode), "图片验证码不能为空");
        AssertUtil.isTrue(!isPictureCodeRight(session, pictureCode), "图片验证码错误");
        AssertUtil.isTrue(!email.equals(session.getAttribute("email")), "尚未发送邮箱验证码");
        AssertUtil.isTrue(StringUtil.isEmpty(emailCode), "邮箱验证码不能为空");
        AssertUtil.isTrue(!emailCode.equals(session.getAttribute("emailCode")), "邮箱验证码错误");
        AssertUtil.isTrue((new Date().getTime() - ((Date) session.getAttribute("emailCodeTime")).getTime()) > 5 * 60 * 1000, "邮箱验证码已过期");
        Long id = accountService.register(email, password, name);
        return RespBean.success("注册成功, id为" + id);
    }

    /**
     * 随机生成验证码
     */
    private int getPin() {
        return (int) (Math.random() * 9000 + 1000);
    }

    /**
     * 获取图片验证码 向resp中输出生成的验证码图片
     */
    @RequestMapping("picture-code")
    public void getCodeImg(HttpSession session, HttpServletResponse response) throws Exception {
        response.setHeader("Cache-Control", "no-cache");
        //在内存中创建图像，设置图像的宽和高
        int width = 60, height = 30;
        //实例化java.awt.image.BufferedImage,作用是访问图像数据缓冲区
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);   //第三个参数：使用的颜色模式为RGB模式
        //获得画笔
        Graphics g = image.getGraphics();
        //设置背景颜色 RGB
        g.setColor(new Color(200, 200, 200));
        g.fillRect(0, 0, width, height);
        //取随机产生的验证码（4位数字）
        String ranStr = String.valueOf(getPin());
        //将验证码存到session中
        session.setAttribute("pictureCode", ranStr);
        //将验证码显示到图像中
        g.setColor(Color.red);
        g.setFont(new Font("", Font.PLAIN, 20));  //名称   样式    磅值大小
        g.drawString(ranStr, 10, 22);
        //随机产生100个干扰点，使图像中的验证码不易被其他程序检测到
        Random rnd = new Random();
        for (int i = 0; i < 100; i++) {
            int x = rnd.nextInt(width);
            int y = rnd.nextInt(height);
            g.drawOval(x, y, 1, 1);
        }
        //输出图像到页面
        ImageIO.write(image, "JPEG", response.getOutputStream());
    }

    /**
     * 判断图片验证码是否正确 传入验证码
     */
    public boolean isPictureCodeRight(HttpSession session, String pictureCode) {
        String realCode = (String) session.getAttribute("pictureCode");
        return realCode.equals(pictureCode);
    }

    /**
     * 发送邮箱验证码API
     */
    @RequestMapping("email-code")
    @ResponseBody
    public RespBean apiSendEmail(String email, String pictureCode, HttpSession session) throws Exception {
        AssertUtil.isTrue(StringUtil.isEmpty(email), "邮箱不能为空");
        AssertUtil.isTrue(StringUtil.isNotEmail(email), "邮箱格式错误");
        AssertUtil.isTrue(!(null == accountService.findByEmail(email)), "邮箱已注册，忘记密码请访问找回页面");
        AssertUtil.isTrue(StringUtil.isEmpty(pictureCode), "图片验证码不能为空");
        AssertUtil.isTrue(!isPictureCodeRight(session, pictureCode), "图片验证码错误");
        String code = (String) session.getAttribute("emailCode");
        code = (code == null) ? "" + getPin() : code;
        session.setAttribute("email", email);
        session.setAttribute("emailCode", code);
        session.setAttribute("emailCodeTime", new Date());
        String finalCode = code;
        new Thread(() -> {
            try {
                emailUtil.sendEmail(email, finalCode + "");
            } catch (EmailException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }).start();
        return RespBean.success("发送成功，五分钟内有效，注意查收");
    }

}
