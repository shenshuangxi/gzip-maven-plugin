package com.gnif.plugin.gzip_maven_plugin.utl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
public class WebZipUtil {

	
	 public static boolean isGZipEncoding(HttpServletRequest request){  
	      boolean flag=false;  
	      String encoding=request.getHeader("Accept-Encoding");  
	      if(encoding.indexOf("gzip")!=-1){  
	        flag=true;  
	      }  
	      return flag;  
	 }
	 
	 public static void gzipResource(Collection<String> resourcePaths,Collection<String> patterns){
		 for(String resourcePath : resourcePaths){
			 File file = new File(resourcePath);
			 if(file.exists()){
				 gzipResource(file,patterns);
			 }
		 }
	 }
	 
	 public static void gzipResource(File resource,Collection<String> patterns){
		 if(resource.isDirectory()){
			 File[] resourceFiles = resource.listFiles();
			 for(File resourceFile : resourceFiles){
				 gzipResource(resourceFile,patterns);
			 }
		 }else{
			 if(isMatch(resource.getName(), patterns)){
				 if(resource.length()>64){
					 BufferedInputStream bufferedInputStream = null;
					 GZIPOutputStream gzipOutputStream = null;
					 try {
						gzipOutputStream = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(new File(resource.getPath()+".gz"))));
						bufferedInputStream =new BufferedInputStream(new FileInputStream(resource));
						byte[] buf = new byte[2048];
						int length = 0;
						while((length=bufferedInputStream.read(buf))>0){
							gzipOutputStream.write(buf, 0, length);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						if(bufferedInputStream!=null){
							try {
								bufferedInputStream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if(gzipOutputStream!=null){
							try {
								gzipOutputStream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				 }
			 }
		 }
	 }
	 
	 public static boolean isMatch(String resource,Collection<String> patterns){
		 for(String pattern : patterns){
			 if(isMatch(resource,pattern)){
				 return true;
			 }
		 }
		 return false;
	 }
	 
	 public static boolean isMatch(String source,String pattern){
		 if(pattern==null||pattern.trim().equals("")){
			 return true;
		 }
		 /*Pattern regexPattern = Pattern.compile(pattern.trim());
		 Matcher matcher = regexPattern.matcher(source);
		 if(matcher.find()){
			 return true;
		 }*/
		 return source.endsWith(pattern);
	 }
	 
	 
	
	
}
