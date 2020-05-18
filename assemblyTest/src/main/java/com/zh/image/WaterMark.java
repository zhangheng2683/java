package com.zh.image;

import sun.font.FontDesignMetrics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * 测试图片加文字水印
 */
public class WaterMark {

    public static void main(String[] args) {
//        String imagePath = "file:\\C:\\Users\\Administrator\\Desktop\\1.jpg";
        String imagePath = "file:\\D:\\头像.png";
        String text = "Bing";
        WaterMark waterMark = new WaterMark();
        try {
//            BufferedImage bufferedImage = waterMark.addWaterMark(imagePath, text);
//            BufferedImage bufferedImage = waterMark.waterMarkImage(text);
//            BufferedImage bufferedImage = waterMark.waterMarkTextImage(imagePath, "这是一张水印，hahahah");
            BufferedImage bufferedImage = waterMark.waterMarkTextImageRotate(imagePath, "这是一张水印，hahahah");
            FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\2.jpg");
            ImageIO.write(bufferedImage, "png", fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("完成...");
    }

    /**
     * @param imagePath 图片路径
     * @param text 文字
     * @return
     */
    public BufferedImage addWaterMark(String imagePath, String text) throws IOException {
        try {
            BufferedImage srcImage = ImageIO.read(new URL(imagePath));
            int width = srcImage.getWidth();
            int height = srcImage.getHeight();
            int imageType = srcImage.getType();

            // 画原图
            BufferedImage destImage = new BufferedImage(width, height, imageType);
            Graphics2D graphics2D = destImage.createGraphics();
            graphics2D.setComposite(AlphaComposite.Src);
            graphics2D.fillRect(0, 0, width, height);

            graphics2D.drawImage(srcImage,
                    0, 0 , width, height,
                    0, 0, width, height,
                    null);

            // 画文字水印
            // 设置文字边缘抗锯齿
            graphics2D.setRenderingHint (RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            // 设置水印文字透明度
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.4f));
            // 文字旋转
//            graphics2D.rotate(Math.toRadians(-45), (double) width / 2, (double) height / 2);
            // 文字颜色
            graphics2D.setColor(Color.WHITE);
            // 文字字体
            graphics2D.setFont(new Font("微软雅黑", Font.PLAIN, 50));
            graphics2D.drawString(text, 1460, 980);
            graphics2D.dispose();

            graphics2D = destImage.createGraphics();
            // 设置文字边缘抗锯齿
            graphics2D.setRenderingHint (RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            // 设置水印文字透明度
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            // 文字旋转
//            graphics2D.rotate(Math.toRadians(-45), (double) width / 2, (double) height / 2);
            // 字体旋转

            Font font = new Font("微软雅黑", Font.PLAIN, 50);
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.rotate(Math.toRadians(0), 0, 0);
            Font rotatedFont = font.deriveFont(affineTransform);
            // 文字颜色
            graphics2D.setColor(Color.BLACK);
            // 文字字体
            graphics2D.setFont(rotatedFont);
            graphics2D.drawString(text, 460, 980);
            graphics2D.dispose();

            destImage.flush();

            return destImage;
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * jpg不支持透明背景
     * 需要用png格式
     * @param text
     * @return
     */
    public BufferedImage waterMarkImage(String text) {
        int width = 1920;
        int height = 1200;
        int imageType = BufferedImage.TYPE_4BYTE_ABGR;

        // 画原图
        BufferedImage destImage = new BufferedImage(width, height, imageType);
        Graphics2D graphics2D = destImage.createGraphics();

        destImage = graphics2D.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        graphics2D.dispose();
        graphics2D = destImage.createGraphics();

        // 设置文字边缘抗锯齿
        graphics2D.setRenderingHint (RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//        graphics2D.rotate(70, 0, 50);
        // 设置水印文字透明度
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 0.5f));
        graphics2D.setColor(Color.BLACK);

        // 字体旋转
        Font font = new Font("微软雅黑", Font.PLAIN, 50);
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(Math.toRadians(45), 0, 0);
        Font rotatedFont = font.deriveFont(affineTransform);

        graphics2D.setFont(rotatedFont);
        graphics2D.drawString(text, 460, 980);
        graphics2D.dispose();
        destImage.flush();
        return destImage;
    }


    /**
     * 图片和文字拼接后，旋转一定角度，生成一整张水印
     * @param imagePath
     * @param text
     * @return
     */
    public BufferedImage waterMarkTextImageRotate(String imagePath, String text) {

        BufferedImage waterMark = waterMarkTextImage(imagePath, text);

        BufferedImage bufferedImage = new BufferedImage(1024, 768, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setColor(Color.RED);
        graphics2D.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        int waterMarkWidth = waterMark.getWidth();
        int waterMarkHeight =waterMark.getHeight();
        // 图片旋转， 弧度数, 旋转中心点坐标
        graphics2D.rotate(Math.toRadians(20), waterMarkWidth / 2, waterMarkHeight / 2);
        graphics2D.drawImage(waterMark, 0, 0, waterMarkWidth, waterMarkHeight, null);

        graphics2D.dispose();
        bufferedImage.flush();
        return bufferedImage;
    }

    /**
     * 图片和文字拼接
     * @param imagePath
     * @param text
     * @return
     */
    public BufferedImage waterMarkTextImage(String imagePath, String text) {
        //1. 文字转图片
        BufferedImage textBufferedImage =  textToImage(text);

        int textImageWidth = textBufferedImage.getWidth();
        int textImageHeight = textBufferedImage.getHeight();

        int destImageWidth = textImageWidth;
        int destImageHeight = textImageHeight;
        //2. 图片和文字联合
        try {
            BufferedImage imageBufferedImage = ImageIO.read(new URL(imagePath));

            // TODO 根据布局计算, 例子默认左右，并且不重叠
            int imageWidth = imageBufferedImage.getWidth();
            int imageHeight = imageBufferedImage.getHeight();

            destImageHeight = textImageHeight > imageHeight ? textImageHeight :imageHeight;
            destImageWidth += imageWidth;

            BufferedImage destImage = new BufferedImage(destImageWidth, destImageHeight, BufferedImage.TYPE_4BYTE_ABGR);
            // 透明背景
            Graphics2D graphics2D = destImage.createGraphics();
            destImage = graphics2D.getDeviceConfiguration().createCompatibleImage(destImageWidth, destImageHeight, Transparency.TRANSLUCENT);
            graphics2D.dispose();

            // 图片联合
            graphics2D = destImage.createGraphics();
            graphics2D.setColor(Color.BLACK);
            graphics2D.fillRect(0, 0, destImageWidth, destImageHeight);
            graphics2D.drawImage(imageBufferedImage, 0, 0, imageWidth, imageHeight, null);
            graphics2D.drawImage(textBufferedImage, imageWidth, 0,  textImageWidth, textImageHeight, null);
            graphics2D.dispose();
            destImage.flush();
            return destImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文字变成等高等宽图片
     * @param text
     * @return
     */
    public BufferedImage textToImage(String text) {
        // 字体旋转
        Font font = new Font("微软雅黑", Font.BOLD, 32);
        FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font);
        int width = caculateTextWidth(metrics, text);
        int height = metrics.getHeight();

        BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics2D = destImage.createGraphics();

        // 设置文字边缘抗锯齿
        graphics2D.setRenderingHint (RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // 设置水印文字透明度
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 0.5f));

        graphics2D.setColor(Color.RED);
        graphics2D.setFont(font);
        graphics2D.drawString(text, 0, metrics.getAscent());

        graphics2D.dispose();
        destImage.flush();

        return destImage;
    }

    public static int caculateTextWidth(FontDesignMetrics metrics, String content) {
        int width = 0;
        for (int i = 0; i < content.length(); i++) {
            width += metrics.charWidth(content.charAt(i));
        }
        return width;
    }
}