package me.zohar.runscore.common.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class ZxingUtils {

	/**
	 * 二维码生成
	 * @param contents 说明
	 * @param width 宽
	 * @param height 高
	 * @param margin 边框
	 * @return BufferedImage
	 * @throws Exception
	 */
	public static BufferedImage createQRImage(String contents, int width, int height, int margin) throws Exception {
	    BufferedImage qRImage = null;

	    if (contents == null || "".equals(contents)) {
	        throw new Exception("content说明不能为空");
	    }

	    // 二维码参数设置
	    HashMap<EncodeHintType, Object> hints = new HashMap<>();
	    hints.put(EncodeHintType.CHARACTER_SET, CharacterSetECI.UTF8); // 编码设置
	    hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // 安全等级，最高h
	    hints.put(EncodeHintType.MARGIN, margin); // 设置margin=0-10

	    // 二维码图片的生成
	    BarcodeFormat format = BarcodeFormat.QR_CODE;

	    // 创建矩阵容器

	    BitMatrix matrix = null;

	    try {
	        matrix = new MultiFormatWriter().encode(contents, format, width, height, hints);
	    } catch (WriterException e) {
	        e.printStackTrace();
	    }

	    // 设置矩阵转为图片的参数
	    MatrixToImageConfig toImageConfig = new MatrixToImageConfig(Color.black.getRGB(), Color.white.getRGB());

	    // 矩阵转换图像
	    qRImage = MatrixToImageWriter.toBufferedImage(matrix, toImageConfig);

	    return qRImage;
	}

	// 二维码添加logo,这样用工具类来写，之后使用我们可以采用加或者不加logo
	/**
	 * @param qrImage
	 * @param width
	 * @param height
	 * @param logoPath
	 * @param logoSize
	 * @return BufferedImage
	 * @throws Exception
	 */

	public static BufferedImage addQRImagelogo(BufferedImage qrImage, int width, int height, String logoPath, int logoSize) throws Exception {
	    BufferedImage qRImageWithLogo = null;
	    File logoFile = new File(logoPath);
	    if (!logoFile.exists() || !logoFile.isFile()) {
	        throw new Exception("指定的logo图片不存在");
	    }

	    // 处理logo
	    BufferedImage image = ImageIO.read(logoFile);
	    // 设置logo的高和宽
	    int logoHeight = qrImage.getHeight()/logoSize;
	    int logoWidth = qrImage.getWidth()/logoSize;
	    // 设置放置位置
	    int x = (qrImage.getHeight() - logoHeight) / 2;
	    int y = (qrImage.getWidth() - logoWidth) / 2;

	    // 新建画板
	    qRImageWithLogo = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	    // 新建画笔
	    Graphics2D g = (Graphics2D) qRImageWithLogo.getGraphics();
	    // 将二维码绘制到画板
	    g.drawImage(qrImage, 0, 0, null);
	    // 设置头透明度，可设置范围0.0f-1.0f
	    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	    // 绘制logo
	    g.drawImage(image, x, y, logoWidth, logoHeight, null);

	    return qRImageWithLogo;
	}


	public static void encode(String content, String filepath) throws WriterException, IOException {
	    int width = 250;
	    int height = 250;
	    Map<EncodeHintType, Object> encodeHints = new HashMap<EncodeHintType, Object>();
	    encodeHints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
	    BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, encodeHints);
	    Path path = FileSystems.getDefault().getPath(filepath);
	    MatrixToImageWriter.writeToPath(bitMatrix, "png", path);
	}

	public static String decode(String filepath) throws IOException, NotFoundException {
	    BufferedImage bufferedImage = ImageIO.read(new FileInputStream(filepath));
	    LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
	    Binarizer binarizer = new HybridBinarizer(source);
	    BinaryBitmap bitmap = new BinaryBitmap(binarizer);
	    HashMap<DecodeHintType, Object> decodeHints = new HashMap<DecodeHintType, Object>();
	    decodeHints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
	    Result result = new MultiFormatReader().decode(bitmap, decodeHints);
	    return result.getText();
	}
}
