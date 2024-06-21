package hrms.ZMGFH.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.arcsoft.face.EngineConfiguration;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import com.arcsoft.face.FunctionConfiguration;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.enums.ErrorInfo;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;

import hrms.ZMGFH.Loading.Loading;

import static java.lang.System.*;

public class FaceService {
	private static final String app_id = "FfkPDAPRNBE9npre3e1ym5FirjPHec8PcEeBiYwQi1x9";
	private static final String sdk_key = "J1ATain2Szp7EyauGwWTqyuth9WUS1efbQPqpvgj1W7a";
	private static FaceEngine faceEngine = null;
	private static final String pathString = "ArcFace/WIN64";

	static {
		File path = new File(pathString);
		faceEngine = new FaceEngine(path.getAbsolutePath());

		int Error = faceEngine.activeOnline(app_id, sdk_key);
		if (Error != ErrorInfo.MOK.getValue() && Error != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
			err.println("激活失败");
		}

		EngineConfiguration engineConfiguration = new EngineConfiguration();// 引擎配置
		engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);// 单张图像模式
		engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_ALL_OUT);// 检测所有角度
		engineConfiguration.setDetectFaceMaxNum(1);// 检测最多人脸数
		engineConfiguration.setDetectFaceScaleVal(16);// 设置人脸相对于所在图片的长边的占比
		FunctionConfiguration functionConfiguration = new FunctionConfiguration();// 功能配置
		functionConfiguration.setSupportFaceDetect(true);// 支持人脸检测
		functionConfiguration.setSupportFaceRecognition(true);// 支持人脸识别
		engineConfiguration.setFunctionConfiguration(functionConfiguration);// 引擎使用此功能配置
		Error = faceEngine.init(engineConfiguration);// 初始化引擎
		if (Error != ErrorInfo.MOK.getValue()) {
			err.println("初始化失败");
		}
	}

	public static FaceFeature getFaceFeature(BufferedImage bufferedImage) {// 获取一张人脸的特征
		if (bufferedImage == null) {
			throw new NullPointerException("人脸图像为空");
		}
		BufferedImage face = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
				BufferedImage.TYPE_INT_BGR);// 创建一个和原图像一样大的临时图像，临时图像类型为普通BRG图像
		face.setData(bufferedImage.getData());// 临时图像使用原图像中的数据
		ImageInfo imageInfo = ImageFactory.bufferedImage2ImageInfo(face);// 采集图像信息
		List<FaceInfo> faceInfos = new ArrayList<FaceInfo>();// 人脸信息列表
		faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(),
				imageInfo.getImageFormat(), faceInfos);// 从图像信息中采集人脸信息
		if (faceInfos.isEmpty()) {
			return null;
		}
		FaceFeature faceFeature = new FaceFeature();// 人脸特征
		faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(),
				imageInfo.getImageFormat(), faceInfos.get(0), faceFeature);// 从人脸信息中采集人脸特征
		return faceFeature;
	}

	public static void LoadAllface() {// 加载所有的面部特征
		Set<String> keySet = Loading.IMAGE_MAP.keySet();
		for (String code : keySet) {
			BufferedImage bufferedImage = Loading.IMAGE_MAP.get(code);
			FaceFeature faceFeature = getFaceFeature(bufferedImage);
			Loading.FACE_FEATURE_MAP.put(code, faceFeature);
		}
	}

	public static String detectFace(FaceFeature target) {// 从人脸特征库中提取出人脸
		if (target == null) {
			return null;
		}

		Set<String> keySet = Loading.FACE_FEATURE_MAP.keySet();
		float score = 0;
		String ansCode = null;
		for (String code : keySet) {
			FaceFeature source = Loading.FACE_FEATURE_MAP.get(code);
			FaceSimilar faceSimilar = new FaceSimilar();
			faceEngine.compareFaceFeature(target, source, faceSimilar);
			if (faceSimilar.getScore() > score) {
				score = faceSimilar.getScore();
				ansCode = code;
			}
		}
		if (score > 0.9) {
			return ansCode;
		}
		return null;
	}

	public static void release() {// 释放资源
		faceEngine.unInit();
	}
}