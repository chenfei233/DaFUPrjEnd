package com.poiTest.poiTest;

//import utils;
import java.io.File;    

import java.io.FileOutputStream;    

import org.apache.poi.hslf.HSLFSlideShow;    
import org.apache.poi.hslf.model.Picture;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.PictureData;    
import org.apache.poi.hslf.usermodel.SlideShow;  
import org.apache.poi.POIDocument;
/**
 * 
 * @author wf
 * 提取ppt的图片以及文字
 *
 */
   public class PPTConvertImageWords {  
//	   public static void main(String[] args) throws Exception {
//		   getImagesPPT2003("D:\\test.ppt","D:\\");
//	}
	   
	   /*
		 * 提取图片
		 */
       public static void getImagesPPT2003(String filePath,String outPutPath) throws Exception {    
    	// 加载PPT    
           HSLFSlideShow _hslf = new HSLFSlideShow(filePath);    
           SlideShow _slideShow = new SlideShow(_hslf);    

           // 获取PPT文件中的图片数据    
           PictureData[] _pictures = _slideShow.getPictureData();    

           // 循环读取图片数据    
           for (int i = 0; i < _pictures.length; i++) {    
               StringBuilder fileName = new StringBuilder(outPutPath+"//");    
               PictureData pic_data = _pictures[i];    
               fileName.append(i);    
               // 设置格式    
               switch (pic_data.getType()) {    
             case Picture.JPEG:    
                   fileName.append(".jpg");    
                   break;    
               case Picture.PNG:    
                   fileName.append(".png");    
                   break;    
               default:    
                   fileName.append(".data");    
               }    
               // 输出文件    
               FileOutputStream fileOut = new FileOutputStream(new File(fileName.toString()));    
               fileOut.write(pic_data.getData());    
               fileOut.close();     
               
               getTextPPT2003(filePath);
           }    
       }    
       /*
        * 提取幻灯片中的文字
        */
       public static String getTextPPT2003(String path) {

           StringBuffer content = new StringBuffer("");
           try {

               SlideShow ss = new SlideShow(new HSLFSlideShow(path));// path为文件的全路径名称，建立SlideShow
               Slide[] slides = ss.getSlides();// 获得每一张幻灯片
               for (int i = 0; i < slides.length; i++) {
                   TextRun[] t = slides[i].getTextRuns();// 为了取得幻灯片的文字内容，建立TextRun
                   for (int j = 0; j < t.length; j++) {
                       content.append(t[j].getText());// 这里会将文字内容加到content中去
                   }
                   content.append(slides[i].getTitle());
               }
           } catch (Exception e) {
               System.out.println(e.toString());
           }
          // System.out.println(content.toString());
           return content.toString();

       }
   }  