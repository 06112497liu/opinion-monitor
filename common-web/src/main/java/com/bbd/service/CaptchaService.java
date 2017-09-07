package com.bbd.service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

/**
 * 验证码服务类，可以生成随机验证码和相应的图片。用在系统中图片验证码，短信/邮件验证码，生成随机字符串等场景
 *
 * @author tjwang
 */
public class CaptchaService {

    /**
     * 默认产生字符长度
     */
    public static int DEFAULT_STRING_COUNT = 4;
    /**
     * 随机字符参数的范围值，默认剔除了1，i,l这些容易混淆的字符
     */
    public String     stringsRange         = "123456789ABCDEFGHJKMNPQRSTUVWXY";
    /**
     * 字体
     */
    public String     font                 = "Times New Roman";
    /**
     * 字体大小
     */
    public int        fontSize             = 24;
    /**
     * 图片宽
     */
    private int       width                = 85;
    /**
     * 图片高
     */
    private int       height               = 25;
    /**
     * 干扰线数量
     */
    private int       lineNum              = 50;
    private Random    random               = new Random();

    public static void main(String[] args) throws Exception {
        CaptchaService captchaService = new CaptchaService();
        Captcha captcha = captchaService.generateRandomCodeAndImage();
        BufferedImage image = captcha.getBuffImage();
        // 将内存中的图片通过流动形式输出到客户端
        ImageIO.write(image, "JPEG", new FileOutputStream(new File("D:/random-code.jpg")));
        System.out.println(captcha.getCode());
    }

    /**
     * 生成随机图片, 同时为参数randomCode附上图片中的随机字符串
     *
     * @param stringCount
     * @return
     */
    public Captcha generateRandomCodeAndImage(int stringCount) {
        Captcha capUtil = new Captcha();
        StringBuffer randomCode = new StringBuffer();
        // BufferedImage类是具有缓冲区的Image类
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取Graphics对象,便于对图像进行各种绘制操作
        Graphics g = image.getGraphics();
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        // g.setColor(new Color());
        // g.drawRect(0,0,width-1,height-1);
        g.setColor(getRandColor(160, 200));

        // 绘制干扰线
        for (int i = 0; i <= lineNum; i++) {
            drowLine(g);
        }
        // 绘制随机字符
        g.setFont(new Font(font, Font.PLAIN, fontSize));

        for (int i = 0; i < stringCount; i++) {
            randomCode.append(drowString(g, i));
        }
        capUtil.setCode(randomCode.toString());
        g.dispose();
        capUtil.setBuffImage(image);
        return capUtil;
    }

    /**
     * 生成随机数和图片验证码（默认四位）
     *
     * @param randomCode
     * @return
     */
    public Captcha generateRandomCodeAndImage() {
        return generateRandomCodeAndImage(DEFAULT_STRING_COUNT);
    }

    /**
     * 生成指定长度的随机字符串
     *
     * @param stringCount 字符串长度
     * @return
     */
    public String generateRandomCode(int stringCount) {
        StringBuilder randomCode = new StringBuilder(stringCount);
        for (int i = 0; i < stringCount; i++) {
            randomCode.append(String.valueOf(stringsRange.charAt(random.nextInt(stringsRange.length()))));
        }
        return randomCode.toString();
    }

    /**
     * 给定范围获得随机颜色
     */
    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    /**
     * 绘制字符串
     */
    private String drowString(Graphics g, int i) {
        g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
        String rand = String.valueOf(stringsRange.charAt(random.nextInt(stringsRange.length())));
        // g.translate(random.nextInt(3), random.nextInt(3));
        g.drawString(rand, 20 * i + 4, 19);
        return rand;
    }

    /**
     * 绘制干扰线
     */
    private void drowLine(Graphics g) {
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int x0 = random.nextInt(12);
        int y0 = random.nextInt(12);
        g.drawLine(x, y, x + x0, y + y0);
    }

    public String getStringsRange() {
        return stringsRange;
    }

    public void setStringsRange(String stringsRange) {
        this.stringsRange = stringsRange;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    public class Captcha {
        // 图片验证码
        private String        code;
        // 图片信息
        private BufferedImage buffImage;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public BufferedImage getBuffImage() {
            return buffImage;
        }

        public void setBuffImage(BufferedImage buffImage) {
            this.buffImage = buffImage;
        }

    }
}
