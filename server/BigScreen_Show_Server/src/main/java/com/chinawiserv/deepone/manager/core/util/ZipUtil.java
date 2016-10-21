package com.chinawiserv.deepone.manager.core.util;

import com.chinawiserv.deepone.manager.core.exception.MsgException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * ZIP 解压工具类
 * <pre>
 * 本类提供 ZIP 解压工具方法
 * </pre>
 * @author zengpzh
 * @version 0.1
 */
public class ZipUtil {

	/**
	 * 压缩文件
	 * @param zipFilePathAndName
	 * @param srcPathName
	 * @return
	 */
	public static boolean compress(String zipFilePathAndName, String srcPathName) {
		boolean  result = false;
		try {
			if (zipFilePathAndName != null && !"".equals(zipFilePathAndName) &&
					srcPathName != null && !"".equals(srcPathName)) {
				File zipFile = new File(zipFilePathAndName);
				File path = new File(zipFile.getParent());
				if (!path.exists()) {
					path.mkdirs();
				}

				File srcdir = new File(srcPathName);
				if (!srcdir.exists()) {
					throw new RuntimeException(srcPathName + "不存在！");
				}
				Project prj = new Project();
				Zip zip = new Zip();
				zip.setProject(prj);
				zip.setDestFile(zipFile);
				FileSet fileSet = new FileSet();
				fileSet.setProject(prj);
				fileSet.setDir(srcdir);
				zip.addFileset(fileSet);
				zip.execute();
				result = true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return  result;
	}

	/**
	 * 解压ZIP文件
	 * @param sourceFileName 待解压ZIP文件名
	 * @param destAbsolutePath 输出绝对目录
	 * @param isIgnoreChineseFileName 是否忽略压缩包里的中文名文件，trur忽略，false相反
	 * @param overwrite 是否重写已经存在的文件，true：重写，false不重写
	 * @throws Exception
	 * @author zengpzh
	 */
	public static ArrayList<String> unzip(String sourceFileName, 
			                              String destAbsolutePath, 
			                              boolean isIgnoreChineseFileName,
			                              boolean overwrite) throws Exception {
		if (sourceFileName == null || (sourceFileName != null && "".equals(sourceFileName.trim()))) {
			throw new MsgException("等解压文件名为空，请重试！");
		}
		else {
			if (!".zip".equalsIgnoreCase(getExtention(sourceFileName))) {
				throw new MsgException("等解压文件必须是zip格式文件，请重试！");
			}
			else {
				sourceFileName = sourceFileName.replace("\\", "/");
			}
		}
		if (destAbsolutePath == null || (destAbsolutePath != null && "".equals(destAbsolutePath.trim()))) {
			throw new MsgException("输出路径为空，请重试！");
		}
		else {
			destAbsolutePath = destAbsolutePath.replace("\\", "/");
		}
		File file = new File(sourceFileName);
		return unzipFromFile(file, destAbsolutePath, isIgnoreChineseFileName, overwrite);
	}

	/**
	 * 解压ZIP文件
	 * @param sourceFile 待解压ZIP文件
	 * @param destAbsolutePath 输出绝对目录
	 * @param isIgnoreChineseFileName 是否忽略压缩包里的中文名文件，trur忽略，false相反
	 * @param overwrite 是否重写已经存在的文件，true：重写，false不重写
	 * @throws Exception
	 * @author zengpzh
	 */
	public static ArrayList<String> unzipFromFile(File sourceFile, 
			                                      String destAbsolutePath, 
			                                      boolean isIgnoreChineseFileName,
			                                      boolean overwrite) throws Exception {
		ZipInputStream zipis = new ZipInputStream(new FileInputStream(sourceFile));
		return ZipUtil.decompressionZIP(zipis, destAbsolutePath, isIgnoreChineseFileName, overwrite);
	}
	
	/**
	 * 解压ZIP资源
	 * @param sourceResources zip资源
	 * @param destAbsolutePath 输出绝对目录
	 * @param isIgnoreChineseFileName 是否忽略压缩包里的中文名文件，trur忽略，false相反
	 * @param overwrite 是否重写已经存在的文件，true：重写，false不重写
	 * @throws Exception
	 * @author zengpzh
	 */
	public static ArrayList<String> unzipFromResource(String sourceResources, 
			                                          String destAbsolutePath, 
			                                          boolean isIgnoreChineseFileName,
			                                          boolean overwrite) throws Exception{
		InputStream resouce = ZipUtil.class.getClassLoader().getResourceAsStream(sourceResources);
		ZipInputStream zipis = new ZipInputStream(resouce);
		return ZipUtil.decompressionZIP(zipis, destAbsolutePath, isIgnoreChineseFileName, overwrite);
	}
	
	/**
	 * 执行解压ZIP文件
	 * @param zipIs ZIP 输入流
	 * @param destAbsolutePath 输出绝对目录
	 * @param isIgnoreChineseFileName 是否忽略压缩包里的中文名文件，trur忽略，false相反
	 * @param overwrite 是否重写已经存在的文件，true：重写，false不重写
	 * @return 解压成功时，压缩包里的文件文件 
	 * @throws Exception
	 * @author zengpzh
	 */
	private static synchronized ArrayList<String> decompressionZIP(ZipInputStream zipIs, 
			                                                       String destAbsolutePath, 
			                                                       boolean isIgnoreChineseFileName,
			                                                       boolean overwrite
			                                                       ) throws Exception {
		ArrayList<String> fileNameList = new ArrayList<String>();;
		if (zipIs == null) {
			throw new MsgException("待解压缩文件输入流为空，请重试！");
		}
		if (destAbsolutePath == null || (destAbsolutePath != null && "".equals(destAbsolutePath.trim()))) {
			throw new MsgException("输出路径为空，请重试！");
		}
		else {
			destAbsolutePath = destAbsolutePath.replace("\\", "/");
		}
		File destFile = new File(destAbsolutePath);
		if (!destFile.exists()) {
			destFile.mkdirs();
		}
		ZipEntry zipEntry = null;
		while ((zipEntry = getZipEntryFromZipInputStream(zipIs, isIgnoreChineseFileName)) != null) {
			//如果是目录
			if (zipEntry.isDirectory()) {
				String currentPathName = zipEntry.getName();
				if (currentPathName != null && !"".equals(currentPathName.trim())) {
					currentPathName = currentPathName.substring(0, currentPathName.length() - 1);
					String currentDestRealPath = destAbsolutePath + File.separator + currentPathName;
					File file = new File(currentDestRealPath);
					if (!file.exists())
						if (!file.mkdirs()) {
							throw new MsgException("创建目录失败:" + currentDestRealPath);
						}
				}
			}
			//如果是文件
			else {
				String currentFileName = destAbsolutePath + File.separator + zipEntry.getName();
				if (currentFileName != null && !"".equals(currentFileName.trim())) {
					currentFileName = currentFileName.replace("\\", "/");
					String addFileName = currentFileName.substring(currentFileName.lastIndexOf("/")+1,currentFileName.length());
					fileNameList.add(addFileName);
					File file = new File(currentFileName);
					/**
					 * 重写已有文件
					 */
					if (overwrite) {
						if (file.exists()) {
							file.delete();
						}
						FileOutputStream fos = new FileOutputStream(file);
						try {
							final int cache = 2048;
							byte[] abyte = new byte[cache];
							int index = 0;
							while ((index = zipIs.read(abyte)) != -1) {
								fos.write(abyte, 0, index);
							}
						} 
						finally {
							fos.close();
						}
					}
					
					/**
					 * 忽略已有文件
					 */
					else {
						if (!file.exists()) {
							FileOutputStream fos = new FileOutputStream(file);
							try {
								final int cache = 2048;
								byte[] abyte = new byte[cache];
								int index = 0;
								while ((index = zipIs.read(abyte)) != -1) {
									fos.write(abyte, 0, index);
								}
							} 
							finally {
								fos.close();
							}
						}
					}
				}
			}
		}
		return fileNameList;
	}

	/** 
	 * 从 ZipInputStream 里取得 ZipEntry
	 * @param zipIs
	 * @param isIgnoreChineseFileName
	 * @return
	 * @throws Exception
	 * @author zengpzh
	 */
	private static ZipEntry getZipEntryFromZipInputStream(ZipInputStream zipIs, boolean isIgnoreChineseFileName) throws Exception {
		ZipEntry zipEntry = null;
		try {
			zipEntry = zipIs.getNextEntry();
		} catch (Exception e) {
			if (!isIgnoreChineseFileName) {
				zipEntry = null;
				throw new MsgException("压缩包内的文件名不能包含中文，请检查后再试！");
			}
			else {
				getZipEntryFromZipInputStream(zipIs, isIgnoreChineseFileName);
			}
		}
		return zipEntry;
	}
	
	/**
	 * 取文件名的扩展名
	 * @param fileName
	 * @return 文件名的扩展名
	 * @author Allen Zhang
	 */
	private static String getExtention(String fileName) throws Exception {
		if (fileName != null) {
			int pos = fileName.lastIndexOf(".");
			return fileName.substring(pos);
		}
		else {
			return "";
		}
	}

	public static void main(String... strs) throws Exception {

	}
}