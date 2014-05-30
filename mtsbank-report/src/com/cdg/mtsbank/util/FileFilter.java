package com.cdg.mtsbank.util;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileFilter;

public class FileFilter implements SmbFileFilter{
    
	
	private String likeFileName;
	
	public FileFilter(String likeFileName){
		this.likeFileName = likeFileName;	
	}
	
	public boolean accept(SmbFile arg0) throws SmbException {
		if(arg0.getName().indexOf(likeFileName) != -1){
			return true;
		}
		return false;
	}
	
	
	
	

}
