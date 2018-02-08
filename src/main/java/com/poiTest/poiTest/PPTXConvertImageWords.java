package com.poiTest.poiTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.swing.text.html.HTMLDocument.Iterator;
import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.model.Picture;
import org.apache.poi.hslf.usermodel.PictureData;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.xslf.XSLFSlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFNotes;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.apache.xmlbeans.XmlException;

/**
 * @param in
 *            输入文件流
 * @param outPath
 *            输出文件路径
 * @throws Exception
 * @throws IOException
 *             提取pptx中的图片和文字
 */
public class PPTXConvertImageWords {
//	public static void main(String[] args) throws Exception {
//		getImagesPPTX("D:\\testx.pptx","D:\\new");
//	}
	
	/*
	 * 提取图片
	 */
	public static void getImagesPPTX(String filePath,String outPutPath) throws Exception {
		InputStream istream = null;
		istream = new FileInputStream(filePath);
		XMLSlideShow ppt = new XMLSlideShow(istream);
		for (XSLFSlide slide : ppt.getSlides()) { // 遍历每一页pptx
			// content+=slide.getTitle()+"\t";
			
			for (XSLFShape shape : slide.getShapes()) {
				int i=0;
				if (shape instanceof XSLFPictureShape) { // 获取到pptx的文本信息
					List<XSLFPictureData> list = ppt.getAllPictures();
					java.util.Iterator<XSLFPictureData> iterator = list.iterator();
					while (iterator.hasNext()) {
						i++;
						StringBuilder fileName = new StringBuilder(outPutPath+"//");
						XSLFPictureData pData = iterator.next();
						System.out.println(pData);
						switch (pData.getPictureType()) {
						case Picture.JPEG:
							fileName.append(i+".jpg");
							break;
						case Picture.PNG:
							fileName.append(i+".png");
							break;
						default:
							fileName.append(i+".data");
						}
						FileOutputStream fileOut = new FileOutputStream(new File(fileName.toString()));
						fileOut.write(pData.getData());
						fileOut.close();
					}
				}
			}
		}
		getTextPPTX(filePath);
	}

	/*
	 * 提取文字内容
	 */
	public static String getTextPPTX(String filePath) {
		String content = "";
		InputStream istream = null;
		try {
			istream = new FileInputStream(filePath);
			XMLSlideShow ppt = new XMLSlideShow(istream);
			for (XSLFSlide slide : ppt.getSlides()) { // 遍历每一页pptx
				// content+=slide.getTitle()+"\t";
				for (XSLFShape shape : slide.getShapes()) {
					if (shape instanceof XSLFTextShape) { // 获取到pptx的文本信息
						for (java.util.Iterator<XSLFTextParagraph> iterator = ((XSLFTextShape) shape)
								.iterator(); iterator.hasNext();) {
							// 获取到每一段的文本信息
							XSLFTextParagraph paragraph = (XSLFTextParagraph) iterator.next();
							for (XSLFTextRun xslfTextRun : paragraph) {
								content += xslfTextRun.getText() + "\t";
							}
						}
					}
				}
				// 获取一张ppt的内容后 换行
				content += "\n";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (istream != null)
				try {
					istream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return content;
	}
}