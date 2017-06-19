/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.suren.autotest.webdriver.downloader;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * webdriver驱动下载器
 * @author suren
 * @date 2017年5月6日 上午6:58:28
 */
public class DriverDownloader
{
	private int errorTimes = 0;

	private Progress progress = new Progress()
	{
		
		@Override
		public void transfer(int len)
		{
			System.out.print(".");
		}
	};
	
	/**
	 * 从指定地址中获取驱动文件，并拷贝到框架根目录中。如果是zip格式的话，会自动解压。
	 * @param url
	 * @return 获取到的驱动文件绝对路径，如果没有找到返回空字符串
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public String getLocalFilePath(URL url) throws FileNotFoundException, IOException
	{
		if(url == null)
		{
			return "";
		}
		
 		final String driverPrefix = "surenpi.com."; 
		
		File driverFile = null;
		String protocol = url.getProtocol();
		if("jar".equals(protocol) || "http".equals(protocol))
		{
			String driverFileName = (driverPrefix + new File(url.getFile()).getName());
			try(InputStream inputStream = url.openStream())
			{
				FilterInputStream filterInputStream = new FilterInputStream(inputStream){

					@Override
					public int read(byte[] b) throws IOException
					{
						getProgress().transfer(b.length);
						return super.read(b);
					}
				};
				
				driverFile = PathUtil.copyFileToRoot(filterInputStream, driverFileName);
				
				getProgress().done();
			}
		}
		else
		{
			driverFile = new File(URLDecoder.decode(url.getFile(), "utf-8"));
		}

		try
		{
			return fileProcess(driverFile, driverPrefix);
		}
		catch (EOFException e)
		{
			if(errorTimes++ > 3)
			{
				throw e;
			}

			return getLocalFilePath(url);
		}
	}
	
	/**
	 * 处理经过压缩和没有压缩的文件
	 * @param driverFile
	 * @param driverPrefix
	 * @return 返回非压缩文件的绝对路径
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private String fileProcess(File driverFile, String driverPrefix) throws FileNotFoundException, IOException
	{
		if(driverFile != null && driverFile.isFile())
		{
			//如果是以.zip为后缀的文件，则自动解压
			if(driverFile.getName().endsWith(".zip"))
			{
				try(ZipInputStream zipIn = new ZipInputStream(new FileInputStream(driverFile)))
				{
					ZipEntry entry = zipIn.getNextEntry();
					if(entry != null)
					{
						driverFile = new File(driverFile.getParent(), driverPrefix + entry.getName());
						
						if(needCopy(driverFile, entry.getSize()))
						{
							PathUtil.copyFileToRoot(zipIn, driverFile);
						}
					}
				}
			}
			
			return driverFile.getAbsolutePath();
		}
		else
		{
			return "";
		}
	}

	/**
	 * 是否需要拷贝
	 * @param driverFile
	 * @param orginalSize
	 * @return
	 */
	private boolean needCopy(File driverFile, long orginalSize)
	{
		if(driverFile.isDirectory())
		{
			String[] childList = driverFile.list();
			
			if((childList != null && childList.length > 0) || !driverFile.delete())
			{
				throw new RuntimeException(String.format("Directory [%s] is not empty or can not bean delete.", driverFile.getAbsolutePath()));
			}
		}
		
		if(!driverFile.isFile() || driverFile.length() != orginalSize)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public Progress getProgress()
	{
		return progress;
	}

	public void setProgress(Progress progress)
	{
		this.progress = progress;
	}

}
