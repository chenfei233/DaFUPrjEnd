package com.cf.run;

import java.io.IOException;

import com.ycy.jodconverter.App;

/**
 * pdf 转换 html 多线程处理
 * @author cf
 *
 */
public class ThreadPdfToHtml implements Runnable {
	String[] split_pdf;
	public ThreadPdfToHtml(String[] split_pdf) {
		super();
		this.split_pdf = split_pdf;
	}
	String[] cmds=new String[3];
	public void run() {
		try {
//			long start=System.currentTimeMillis();
//			String cmd="java -jar input/DConverterok.jar "+split_pdf[0]+".pdf 0 "+split_pdf[1];//执行pdf转换html
			cmds[0]=split_pdf[0]+".pdf";
			cmds[1]="0";
			cmds[2]=split_pdf[1];
			App.DaFuPrj(cmds);//在进行pdf转html的同时，还会html转md
			
//			Process pro = Runtime.getRuntime().exec(cmd);
//	        pro.waitFor();
//	        long end=System.currentTimeMillis();
//	        System.out.println(i+"执行时间： "+(end-start));
//	        System.out.println(cmd);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
