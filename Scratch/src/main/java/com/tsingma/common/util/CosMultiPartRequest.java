package com.tsingma.common.util;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.multipart.MultiPartRequest;

import com.oreilly.servlet.MultipartRequest;

public class CosMultiPartRequest implements MultiPartRequest {
	
	private MultipartRequest multi;  
    private boolean maxSizeProvided;  
    private int maxSize;

	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] getContentType(String fieldName) {
		return new String[] { multi.getContentType(fieldName) };  
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getErrors() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public File[] getFile(String fieldName) {
		return new File[] { multi.getFile(fieldName) };
	}

	@Override
	public String[] getFileNames(String fieldName) {
		return new String[] { multi.getFile(fieldName).getName() }; 
	}

	@SuppressWarnings("unchecked")
	@Override
	public Enumeration<String> getFileParameterNames() {
		return multi.getFileNames();
	}

	@Override
	public String[] getFilesystemName(String name) {
		return new String[] { multi.getFilesystemName(name) };
	}

	@Override
	public String getParameter(String name) {
		return multi.getParameter(name); 
	}

	@SuppressWarnings("unchecked")
	@Override
	public Enumeration<String> getParameterNames() {
		return multi.getParameterNames();  
	}

	@Override
	public String[] getParameterValues(String name) {
		return multi.getParameterValues(name);
	}

	@Override
	public void parse(HttpServletRequest request, String saveDir) throws IOException {
		if (maxSizeProvided) {
            multi = new MultipartRequest(request, saveDir, maxSize, "UTF-8");  
        } else {  
            multi = new MultipartRequest(request, saveDir, "UTF-8");  
        }
		
	}

}
