package com.gnif.plugin.gzip_maven_plugin.utl;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class GzipFilter implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		
		
//		String uri = httpServletRequest.getRequestURI();
//		String source = httpServletRequest.getServletContext().getRealPath(uri);
//		String ext = FilenameUtils.getExtension(source);
		
		if(WebZipUtil.isGZipEncoding((HttpServletRequest) request)){
			/*String gzippPattern=",.jpg,.png,.css,.html,.js,";
            if(StringUtils.indexOf(gzippPattern, ",."+ext+",")!=-1){  */
            	GzipHttpServletResponse gzipResponse = new GzipHttpServletResponse(httpServletResponse);
                chain.doFilter(request, gzipResponse);
                gzipResponse.transformData();
            /*} else {  
                chain.doFilter(request, response);  
            } */ 
            return;  
        }  
        chain.doFilter(request, response);  
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	

}
