package hrms.ZMGFH.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.imageio.ImageIO;

import hrms.ZMGFH.Loading.Loading;

import static java.lang.System.*;

public class ImageService {
	private static final File FILE = new File("src/faces");//员工照片的文件夹
	private static final String SUFFIX_STRING = "png";
	
	public static Map<String, BufferedImage> LoadAllImage(){//加载所有人脸的图像文件
		if(!FILE.exists()) {
			err.println("文件夹丢失");
			return null;
		}
		File[] faces = FILE.listFiles();//遍历文件夹下所有的文件
        if (faces != null) {
            for(File f:faces) {
                try {
                    BufferedImage bufferedImage = ImageIO.read(f);
                    String codeString = f.getName().substring(0,f.getName().indexOf('.'));
                    Loading.IMAGE_MAP.put(codeString, bufferedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Collections.emptyMap();
	}
	
	public static void SaveFace(BufferedImage bufferedImage,String code) {//保存人脸图像文件
		try {
			ImageIO.write(bufferedImage, SUFFIX_STRING, new File(FILE,code+"."+SUFFIX_STRING));
			Loading.IMAGE_MAP.put(code, bufferedImage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void DeleteFace(String code) {//删除人脸图像文件
		Loading.IMAGE_MAP.remove(code);
		File file = new File(FILE,code+"."+SUFFIX_STRING);
		if(file.exists()) {
			file.delete();
			out.println("图像删除成功");
		}
	}
}
