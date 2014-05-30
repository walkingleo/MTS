package com.cdg.mtsbank.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

import org.apache.log4j.Logger;






/**
 * 文件操作工具,新增的方法可以写在这里 
 * @author lihui
 *
 */

public class FileUtil {
	
	private static Logger log = Logger.getLogger(FileUtil.class);
	
	public static String FILE_SUFFIX = "dat";
	
	
	/**
	 * 获取需要导入的目录文件
	 * @param smbFilePath
	 * @param likeFileName yyyyMMdd
	 * @return
	 */
	public static SmbFile[] getSmbFile(String smbFilePath,String likeFileName) {
		SmbFile[] files = null;
		try {
			log.info("开始过滤当前需要导入的目录文件");
			SmbFile rmifile = new SmbFile(smbFilePath);
			files = FileUtil.getImportFile(rmifile,likeFileName);
		} catch (Exception re) {
			log.error(re.getMessage());
			LogUtil.logExceptionStackTrace(log, re);
		}
		return files;
	}
	
	
	/**
	 * 根据likeFileName 过滤需要导入的文件目录
	 * @param smbFile
	 * @param likeFileName  likeFileName yyyyMMdd
	 * @return
	 */
	private static SmbFile[] getImportFile(SmbFile smbFile,String likeFileName) {
		SmbFile[] checkFiles = null;
		if (smbFile == null) {
			return checkFiles;
		}
		try {
			checkFiles = smbFile.listFiles(new FileFilter(likeFileName));
		} catch (Exception re) {
			LogUtil.logExceptionStackTrace(log, re);
		}
		return checkFiles;
	}
	
	/**
	 * 获取文件里的数据信息
	 * @param file
	 * @return
	 */
	public static String getFileToString(SmbFile  file){
		StringBuffer str = new StringBuffer();
		BufferedReader br = null;
		try {
			 br = new BufferedReader(
					new InputStreamReader(file.getInputStream()));
			String dataLine = null;
			while((dataLine = br.readLine()) != null){
				str.append(dataLine);
			}
		} catch (IOException e) {
			LogUtil.logExceptionStackTrace(log, e);
		}finally{
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					LogUtil.logExceptionStackTrace(log,e);
				}
			}
		}
		return str.toString();
	}
	
	public static void copy(File sourceFile, String fileName, String basePath) {
		File baseFoleder = new File(basePath);
		baseFoleder.mkdirs();
		File tFile = new File(basePath + File.separator + fileName);
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			tFile.createNewFile();
			in = new FileInputStream(sourceFile);
			out = new FileOutputStream(tFile);
			byte[] b = new byte[512];
			int len = 0;
			while ((len = in.read(b)) > 0) {
				out.write(b, 0, len);
			}
		} catch (Exception e) {
			LogUtil.logExceptionStackTrace(log,e);
		} finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				LogUtil.logExceptionStackTrace(log,e);
			}
		}
	}

	public static void compress(String srcPathName, File zipFile) {
		File file = new File(srcPathName);
		if (!file.exists())
			throw new RuntimeException(srcPathName + "不存在！");
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
			CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,
					new CRC32());
			ZipOutputStream out = new ZipOutputStream(cos);
			String basedir = "";
			compress(file, out, basedir);
			out.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void compress(File file, ZipOutputStream out, String basedir) {
		/* 判断是目录还是文件 */
		if (file.isDirectory()) {
			log.info("压缩：" + basedir + file.getName());
			compressDirectory(file, out, basedir);
		} else {
			log.info("压缩：" + basedir + file.getName());
			compressFile(file, out, basedir);
		}
	}

	/** 压缩一个目录 */
	private static void compressDirectory(File dir, ZipOutputStream out, String basedir) {
		if (!dir.exists())
			return;

		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			compress(files[i], out, basedir + dir.getName() + "/");
		}
	}

	/** 压缩一个文件 */
	private static void compressFile(File file, ZipOutputStream out, String basedir) {
		if (!file.exists()) {
			return;
		}
		try {
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			ZipEntry entry = new ZipEntry(basedir + file.getName());
			out.putNextEntry(entry);
			int count;
			byte data[] = new byte[512];
			while ((count = bis.read(data, 0, 512)) != -1) {
				out.write(data, 0, count);
			}
			bis.close();
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	/**文件上传**/
	public static boolean uploadPackage(String remoteUrl, String localFilePath) {
		boolean flag = false;
		InputStream in = null;
		OutputStream out = null;
		try {
			File localFile = new File(localFilePath);
			String fileName = localFile.getName();
			SmbFile remoteFile = new SmbFile(remoteUrl + "/" + fileName);
			in = new BufferedInputStream(new FileInputStream(localFile));
			out = new BufferedOutputStream(new SmbFileOutputStream(remoteFile));
			byte[] buffer = new byte[1024];
			while ((in.read(buffer)) != -1) {
				out.write(buffer);
				buffer = new byte[1024];
			}
			flag = true;
			log.info("报表文件 " + localFilePath + " 上传前置机成功!");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			try {
				out.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
				log.error(e.getMessage());
			}
		}
		return flag;
	}

}
