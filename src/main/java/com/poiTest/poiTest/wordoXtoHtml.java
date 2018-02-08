package com.poiTest.poiTest;

import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;  
  
import org.apache.poi.xwpf.converter.core.FileImageExtractor;  
import org.apache.poi.xwpf.converter.core.FileURIResolver;  
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;  
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;  
import org.apache.poi.xwpf.usermodel.XWPFDocument;  
import org.apache.poi.xwpf.usermodel.XWPFPictureData;  
//import org.junit.Assert;  
//import org.junit.Test;  
  
public class wordoXtoHtml {  
  /**
   * 
   * @throws IOException
   * docx转html
   */
//	public static void main(String[] args) {
//    	try {
//			canExtractImage("D:\\testx.docx", "D:\\","index");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//	}
    public static void canExtractImage(String filePath,String outPutPath,String filename) throws IOException {  
        File f = new File(filePath);  
        if (!f.exists()) {  
            System.out.println("Sorry File does not Exists!");  
        } else {  
            if (f.getName().endsWith(".docx") || f.getName().endsWith(".DOCX")) {  
                  
                // 1) Load DOCX into XWPFDocument  
                InputStream in = new FileInputStream(f);  
                XWPFDocument document = new XWPFDocument(in);  
  
                // 2) Prepare XHTML options (here we set the IURIResolver to  
                // load images from a "word/media" folder)  
                File imageFolderFile = new File(outPutPath);  
                XHTMLOptions options = XHTMLOptions.create().URIResolver(  
                        new FileURIResolver(imageFolderFile));  
                options.setExtractor(new FileImageExtractor(imageFolderFile));  
                //options.setIgnoreStylesIfUnused(false);  
                //options.setFragment(true);  
                  
                // 3) Convert XWPFDocument to XHTML  
                OutputStream out = new FileOutputStream(new File(outPutPath+"//"+filename+".html"));  
                XHTMLConverter.getInstance().convert(document, out, options);  
            } else {  
                System.out.println("Enter only MS Office 2007+ files");  
            }  
        }  
    }  
}  