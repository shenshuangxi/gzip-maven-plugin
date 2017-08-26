package com.gnif.plugin.gzip_maven_plugin.utl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class GzipHttpServletResponse extends HttpServletResponseWrapper {
	
	public static final int OT_NONE = 0, OT_WRITER = 1, OT_STREAM = 2;  
	
    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); 
    private PrintWriter writer = null;  
    private int outputType = OT_NONE;  
    private final HttpServletResponse response;
    private ServletOutputStream outputStream = new ServletOutputStream() {
		
		@Override
		public void write(int b) throws IOException {
			byteArrayOutputStream.write(b);
		}
		
		@Override
		public void setWriteListener(WriteListener writeListener) {
		}
		
		@Override
		public boolean isReady() {
			return false;
		}
	};
  
	public GzipHttpServletResponse(HttpServletResponse response) throws IOException {  
        super(response);  
        this.response = response;
    }  
      
    public PrintWriter getWriter() throws IOException {  
        if (outputType == OT_STREAM)  
            throw new IllegalStateException();  
        else if (outputType == OT_WRITER)  
            return writer;  
        else {  
            outputType = OT_WRITER;  
            writer = new PrintWriter(new OutputStreamWriter(outputStream, getCharacterEncoding()), true);  
            return writer;  
        }  
    }  
      
    public ServletOutputStream getOutputStream() throws IOException {  
        if (outputType == OT_WRITER)  
            throw new IllegalStateException();  
        else if (outputType == OT_STREAM)  
            return outputStream;  
        else {  
            outputType = OT_STREAM;  
            return outputStream;  
        }  
    }  
    public void flushBuffer() throws IOException {  
        try{writer.flush();}catch(Exception e){}  
        try{outputStream.flush();}catch(Exception e){}  
    }  
  
    public void reset() {  
        outputType = OT_NONE;  
        byteArrayOutputStream.reset();  
    }  
  
    public byte[] getResponseData() throws IOException {  
    	flushBuffer();
    	if(byteArrayOutputStream.toByteArray().length<64){
    		return byteArrayOutputStream.toByteArray();  
    	}else{
    		GZIPOutputStream gzipOutputStream = null;
    		ByteArrayOutputStream retByteArrayOutputStream = new ByteArrayOutputStream();
    		try {
    			System.out.println(byteArrayOutputStream.toByteArray().length);
				gzipOutputStream = new GZIPOutputStream(retByteArrayOutputStream); 
				gzipOutputStream.write(byteArrayOutputStream.toByteArray());
				gzipOutputStream.flush();
			} finally {
				if(gzipOutputStream!=null){
					gzipOutputStream.close();
				}
			}
    		return retByteArrayOutputStream.toByteArray();  
    	}
    }

	public void transformData()throws IOException {
        flushBuffer();
        ServletOutputStream output = response.getOutputStream(); 
        byte[] originResponseData = byteArrayOutputStream.toByteArray();
    	if(originResponseData.length==0){
    		return;
    	}else if(originResponseData.length<64){
    		output.write(originResponseData);  
    	}else{
    		GZIPOutputStream gzipOutputStream = null;
    		ByteArrayOutputStream retByteArrayOutputStream = new ByteArrayOutputStream();
    		try {
				gzipOutputStream = new GZIPOutputStream(retByteArrayOutputStream); 
				gzipOutputStream.write(originResponseData);
				gzipOutputStream.flush();
			} finally {
				if(gzipOutputStream!=null){
					gzipOutputStream.close();
				}
			}
    		
    		byte[] responseData = retByteArrayOutputStream.toByteArray();
    		response.addHeader("Content-Encoding", "gzip");
    		response.setContentLength(responseData.length); 
    		output.write(responseData); 
    	}
		
	} 
    

}
