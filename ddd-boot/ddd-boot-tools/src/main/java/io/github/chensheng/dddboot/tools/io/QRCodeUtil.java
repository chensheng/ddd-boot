package io.github.chensheng.dddboot.tools.io;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import io.github.chensheng.dddboot.tools.text.EncodeUtil;
import io.github.chensheng.dddboot.tools.text.TextUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Hashtable;

public class QRCodeUtil {
    private static final String CHARSET = "UTF-8";
    private static final String FORMAT_NAME = "JPG";
    private static final int QRCODE_SIZE = 200;
    private static final int WIDTH = 60;
    private static final int HEIGHT = 60;

    private static BufferedImage createImage(String content, String logoImgPath, boolean needCompress) throws WriterException, IOException{
        InputStream logoImg = null;
        if (TextUtil.isNotBlank(logoImgPath)) {
            logoImg = new FileInputStream(logoImgPath);
        }
        return createImage(content, logoImg, needCompress);
    }

	private static BufferedImage createImage(String content, InputStream logoImg, boolean needCompress)
			throws WriterException, IOException {
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
		hints.put(EncodeHintType.MARGIN, 1);
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE,
				hints);
		int width = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
			}
		}
		if (logoImg == null) {
			return image;
		}
		QRCodeUtil.insertImage(image, logoImg, needCompress);
		return image;
	}

	private static void insertImage(BufferedImage source, InputStream logoImg, boolean needCompress) throws IOException {
		if (logoImg == null) {
			return;
		}

		Image src = ImageIO.read(logoImg);
		int width = src.getWidth(null);
		int height = src.getHeight(null);
		if (needCompress) {
			if (width > WIDTH) {
				width = WIDTH;
			}

			if (height > HEIGHT) {
				height = HEIGHT;
			}

			Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null);
			g.dispose();
			src = image;
		}

		Graphics2D graph = source.createGraphics();
		int x = (QRCODE_SIZE - width) / 2;
		int y = (QRCODE_SIZE - height) / 2;
		graph.drawImage(src, x, y, width, height, null);
		Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
		graph.setStroke(new BasicStroke(3f));
		graph.draw(shape);
		graph.dispose();
	}

    public static void encode(String content, InputStream logoImg, String destPath, boolean needCompress)
            throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, logoImg, needCompress);
        File file = new File(destPath);
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
        file.createNewFile();
        ImageIO.write(image, FORMAT_NAME, new File(destPath));
    }

	public static void encode(String content, String logoImgPath, String destPath, boolean needCompress)
			throws Exception {
		BufferedImage image = QRCodeUtil.createImage(content, logoImgPath, needCompress);
		File file = new File(destPath);
		File fileParent = file.getParentFile();
		if (!fileParent.exists()) {
			fileParent.mkdirs();
		}
		file.createNewFile();
		ImageIO.write(image, FORMAT_NAME, new File(destPath));
	}

    public static byte[] encode(String content, InputStream logoImg, boolean needCompress) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, logoImg, needCompress);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, FORMAT_NAME, out);
        return out.toByteArray();
    }

	public static byte[] encode(String content, String logoImgPath, boolean needCompress) throws Exception {
		BufferedImage image = QRCodeUtil.createImage(content, logoImgPath, needCompress);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(image, FORMAT_NAME, out);
		return out.toByteArray();
	}

	public static byte[] imageToBytes(BufferedImage bImage, String format) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ImageIO.write(bImage, format, out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}

	public static void encode(String content, String destPath) throws Exception {
		QRCodeUtil.encode(content, "", destPath, false);
	}

	public static void encode(String content, String logoImgPath, OutputStream output, boolean needCompress)
			throws Exception {
		BufferedImage image = QRCodeUtil.createImage(content, logoImgPath, needCompress);
		ImageIO.write(image, FORMAT_NAME, output);
	}

	public static void encode(String content, OutputStream output) throws Exception {
		QRCodeUtil.encode(content, null, output, false);
	}

	public static String decode(File file) throws Exception {
		BufferedImage image;
		image = ImageIO.read(file);
		if (image == null) {
			return null;
		}
		BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		Result result;
		Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
		hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
		result = new MultiFormatReader().decode(bitmap, hints);
		String resultStr = result.getText();
		return resultStr;
	}

	public static String decodeBase64Image(String base64Image) {
		byte[] imgBytes = EncodeUtil.decodeBase64(base64Image);
		if (imgBytes == null) {
			return null;
		}

        try {
            ByteArrayInputStream in = new ByteArrayInputStream(imgBytes);
            BufferedImage image = ImageIO.read(in);
            BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result;
            Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
            result = new MultiFormatReader().decode(bitmap, hints);
            String resultStr = result.getText();
            return resultStr;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

	public static String decode(String path) throws Exception {
		return QRCodeUtil.decode(new File(path));
	}
}
