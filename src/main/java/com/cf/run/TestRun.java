package com.cf.run;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.ycy.jodconverter.App;
import com.ycy.jodconverter.DConverter;

/**
 * java调用linux命令运行测试:把一个pdf文件拆分成n个pdf文件
 * @author cf
 */
public class TestRun{

	public static void main(String[] args) throws Exception {
		String inputFilePath=args[0];
		String outputFilePath=args[1];
		int num=runCmd(inputFilePath, outputFilePath);//获取pdf的页数
		split_File(num,inputFilePath,outputFilePath);//进行拆分的时候，时间很长
	}

	
	/**
	 * 获取PDF的总页数并返回
	 * @param inputFilePath 输入文件路径
	 * @param outputFilePath 输出文件路径
	 * @throws InterruptedException
	 * @throws IOException
	 * @author cf
	 */
	public static int runCmd(String inputFilePath,String outputFilePath) throws InterruptedException, IOException{
		String cmd="qpdf --pages "+inputFilePath+" -- "+inputFilePath+" --show-npages";
		Process pro = Runtime.getRuntime().exec(cmd);  
        pro.waitFor();
        //获取查询返回的信息
        InputStream in = pro.getInputStream();
        BufferedReader read = new BufferedReader(new InputStreamReader(in));
        String line = null;
        line = read.readLine();
        int page=Integer.parseInt(line);
        if((line = read.readLine())!=null || page>0){
        	System.out.println("获取页码成功！");
        	return page;
        }else{
        	System.out.println("获取页码失败！");
        	return 0;
        }
	}
	
	/**
	 * 拆分pdf文件，直接执行，返回拆分文件路径。
	 * @param pagenum 文件页数
	 * @param inputFilePath 输入文件路径
	 * @param outputFilePath 输出文件路径
	 * @author cf
	 * @throws Exception 
	 */
	@SuppressWarnings("static-access")
	public static void split_File(int pagenum,String inputFilePath,String outputFilePath) throws Exception{
		int pagesize=100;
		String cmd=null;
		String[] test=new String[3];
		Thread[] threads = new Thread[pagenum/pagesize+1];
		for (int i=0,j=0,k=0; i*pagesize < pagenum; i++) {
			j=i*pagesize+1;
			k=(i+1)*pagesize;
			if(pagenum < k){
				cmd="qpdf "+inputFilePath+" --pages "+inputFilePath+" "+j+"-"+pagenum+" -- "+outputFilePath+"/"+DConverter.getFileName(inputFilePath)+"_"+num2String(i)+".pdf";
			}else{
				cmd="qpdf "+inputFilePath+" --pages "+inputFilePath+" "+j+"-"+k+" -- "+outputFilePath+"/"+DConverter.getFileName(inputFilePath)+"_"+num2String(i)+".pdf";
			}
			Process pro = Runtime.getRuntime().exec(cmd);  
	        pro.waitFor();
	        test[0]=outputFilePath+"/"+DConverter.getFileName(inputFilePath)+"_"+num2String(i);//拆分后的pdf文件路径不带扩展名
			test[1]=outputFilePath;
			
			//多线程处理   pdf-->html
			ThreadPdfToHtml r = new ThreadPdfToHtml(test);
			threads[i] = new Thread(r);
			threads[i].start();
			threads[i].sleep(5);
		}
		
		while (true) {
			try {
				Thread.currentThread().sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (threadIsAlive(threads) > 0)
				break;
		}
		System.out.println("所有线程都结束了......");
		
		System.out.println("拆分文件已走完，转换html文件已完毕......");
		
		String FileName=outputFilePath+"/"+DConverter.getFileName(inputFilePath);
		System.out.println("html文件转换完成！正在执行转换md文件......");
//		html2md(FileName,pagenum/pagesize,outputFilePath);
		System.out.println("转换md文件完成！！！正在合并md文件......");
		mergemd(FileName,pagenum/pagesize);
        System.out.println("运行完成！！！");
	}
	
	/**
	 * 数字格式化
	 * @param num
	 * @return
	 */
	public static String num2String(int num){
		if(num==0)
			return "000";
		if(0<num && num<10)
			return "00"+num;
		if(10<=num && num<100)
			return "0"+num;
		if(100<=num && num<1000)
			return ""+num;
		return "0";
	}
	
	/**
	 * html文件转换为md文件
	 * @param fileName 
	 * @param num
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("static-access")
	public static void html2md(String inputFile,int num,String outputFile) throws Exception{
//		String cmd=null;
		String[] cmds=new String[2];
		cmds[1]=outputFile;
		for (int i = 0; i < num+1; i++) {
			String filename=inputFile+"_"+num2String(i)+".html";
			File file=new File(filename);
			if(file.exists()){//文件存在
				cmds[0]=filename;
//				cmd="java -jar input/DConverterok.jar "+filename+" output";
//				System.out.println(cmd);
		    	Thread.currentThread().sleep(1);
		    	App.DaFuPrj(cmds);
//		    	Process pro = Runtime.getRuntime().exec(cmd);
//		    	pro.waitFor();
			}
		}
	}

	/**
	 * md文件合并
	 * @param fileName 文件路径，拆分后的编号和后缀在里面补充
	 * @param num 拆分后的文件个数
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@SuppressWarnings("static-access")
	public static void mergemd(String fileName,int num) throws IOException, InterruptedException{
		String cmd=null;
		for (int i = 0; i < num+1; i++) {
			String filename=fileName+"_"+num2String(i)+".md";
			File file=new File(filename);
			if(file.exists()){//文件存在
				cmd="./c.sh "+filename+" "+fileName+".md";
//				System.out.println(cmd);
		    	Thread.currentThread().sleep(1);
		    	Process pro = Runtime.getRuntime().exec(cmd);
		    	pro.waitFor();
			}
		}
	}
	
	/**
	 * 判断多线程是否结束
	 * return  1结束     0没结束
	 */
	public static int threadIsAlive(Thread[] threads){
		for (int i = 0; i < threads.length; i++) {
			if(threads[i].isAlive())//仍在运行
				return 0;
		}
		return 1;
	}
}

