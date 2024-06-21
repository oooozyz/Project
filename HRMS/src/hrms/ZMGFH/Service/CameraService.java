package hrms.ZMGFH.Service;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CameraService {
    //摄像头服务
    private static final Webcam webcam = Webcam.getDefault();
    //开启摄像头
    public static boolean StartCamera(){
        if(webcam == null){
            return false;
        }
        webcam.setViewSize(new Dimension(640, 480));
        return webcam.open();
    }
    //摄像头是否开启
    public static boolean cameraIsOpen() {
        if (webcam == null) {// 如果计算机没有连接摄像头
            return false;
        }
        return webcam.isOpen();
    }
    //获取摄像头画面面板
    public static JPanel getCameraPanel() {
        // 摄像头画面面板
        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setMirrored(true);// 开启镜像
        return panel;
    }
    //获取摄像头补货的帧画面
    public static BufferedImage getCameraFrame() {
        // 获取当前帧画面
        return webcam.getImage();
    }
    //释放摄像头资源
    public static void releaseCamera() {
        if (webcam != null) {
            webcam.close();// 关闭摄像头
        }
    }
}
