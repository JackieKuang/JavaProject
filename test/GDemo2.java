package test;

/**
* 将网址转化为JPG
* @author wangzi6hao
*
*/
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class GDemo2 extends JFrame {

    private String url;
    private File file;

    /**
    * 将打开的网址转换成jpg图片<br>
    * 不支持div层网站
    *
    * @param url
    *            网站地址
    * @param file
    *            图片存放地址
    * @throws Exception
    */
    public GDemo2(String url, File file) {
        this.url = url;
        this.file = file;
    }

    /**
    * 根据构造函数内容将url抓屏后的页面生成图片放在file路径对应位置
    *
    * @throws Exception
    */
    public String producePic() throws Exception {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setPage(url);
        JScrollPane jsp = new JScrollPane(editorPane);
        getContentPane().add(jsp);
        this.setLocation(0, 0);

        Thread.sleep(5 * 1000);

        setSize(10000, 10000);
        pack();
        BufferedImage image = new BufferedImage(editorPane.getWidth(),
                editorPane.getHeight(), BufferedImage.TYPE_USHORT_565_RGB);
        Graphics2D graphics2D = image.createGraphics();
        editorPane.paint(graphics2D);
        ImageIO.write(image, "jpg", file);
        dispose();
        return file.getName();
    }

    public static void main(String[] args) throws Exception {
        String url = "http://assessment.104china.com/talent/";
        String filePath = "c:\\file.jpg";
        GDemo2 gd = new GDemo2(url, new File(filePath));
        gd.producePic();
        System.out.println("ok");
    }
}
