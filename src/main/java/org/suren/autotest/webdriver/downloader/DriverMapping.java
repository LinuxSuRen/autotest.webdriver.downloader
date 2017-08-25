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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.dom4j.xpath.DefaultXPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.surenpi.autotest.utils.StringUtils;

/**
 * webdriver驱动和浏览器版本的映射
 * @author <a href="http://surenpi.com">suren</a>
 */
public class DriverMapping
{
    private static final Logger logger = LoggerFactory.getLogger(DriverMapping.class);
    
	private Document document;
	private Properties osMap = new Properties();

	public void init()
	{
		try(InputStream input = this.getClass().getClassLoader().getResourceAsStream("driver.mapping.xml");
				InputStream enginePro = this.getClass().getClassLoader().getResourceAsStream("os.mapping.properties"))
		{
			SAXReader reader = new SAXReader();
			
			document = reader.read(input);
			
			if(enginePro != null)
			{
				osMap.load(enginePro);
			}
		}
		catch (IOException e)
		{
		    logger.error("", e);
		}
		catch (DocumentException e)
		{
            logger.error("", e);
		}
	}
	
	/**
	 * 根据当前的操作系统来选择
	 * @see #getUrl(String, String, String, String)
	 * @param browser 浏览器类型
	 * @param ver 浏览器版本
	 * @return 驱动下载地址
	 */
	public String getUrl(String browser, String ver)
	{
        //实现对多个操作系统的兼容性设置
        final String os = System.getProperty("os.name");
        final String arch = System.getProperty("os.arch");
        
        String commonOs = osMap.getProperty("os.map.name." + os);
        String commonArch = osMap.getProperty("os.map.arch." + arch);
        
        if(StringUtils.isAnyBlank(commonOs, commonArch))
        {
            throw new RuntimeException(String.format("unknow os [%s] and arch [%s].", os, arch));
        }
        
		return getUrl(browser, ver, commonOs, commonArch);
	}
	
	/**
	 * @param browser
	 * @param ver
	 * @param os 操作系统名称
	 * @param arch CUP架构（32或者64位）
	 * @return 找不到返回null
	 */
	public String getUrl(String browser, String ver, String os, String arch)
	{
		String xpathStr = String.format("//drivers/driver[@type='%s']/supports/browser[@version='%s']",
				browser, ver);
		XPath xpath = new DefaultXPath(xpathStr);

		String path = null;
		@SuppressWarnings("unchecked")
        List<Element> nodes = xpath.selectNodes(document);
		for(Element ele : nodes)
		{
			@SuppressWarnings("unchecked")
            List<Element> itemList = ele.getParent().getParent().element("items").elements("item");
			for(Element item : itemList)
			{
				if(os.equals(item.attributeValue("os")) && arch.equals(item.attributeValue("arch")))
				{
					path = item.attributeValue("path");
					break;
				}
			}
		}
		
		if(path != null && !path.trim().equals(""))
		{
			String base = document.getRootElement().attributeValue("base");
			path = base + path;
		}
		else
		{
		    path = null;
		}
		
		return path;
	}
	
	/**
	 * @return 支持的浏览器以及版本列表
	 */
	public Map<String, Set<String>> supportBrowser()
	{
		Map<String, Set<String>> browserMap = new HashMap<String, Set<String>>();

		String xpathStr = String.format("//drivers/driver");
		XPath xpath = new DefaultXPath(xpathStr);

		@SuppressWarnings("unchecked")
        List<Element> nodes = xpath.selectNodes(document);
		for(Element ele : nodes)
		{
			String type = ele.attributeValue("type");
			
			Set<String> verList = new TreeSet<String>(new Comparator<String>(){

				@Override
				public int compare(String o1, String o2)
				{
					return o2.compareTo(o1);
				}
			});
			@SuppressWarnings("unchecked")
            List<Element> verEleList = ele.element("supports").elements("browser");
			for(Element verEle : verEleList)
			{
				String ver = verEle.attributeValue("version");
				
				verList.add(ver);
			}
			
			Set<String> oldVerList = browserMap.get(type);
			if(oldVerList == null)
			{
				browserMap.put(type, verList);
			}
			else
			{
				oldVerList.addAll(verList);
			}
		}
		
		return browserMap;
	}
	
	/**
	 * @return 驱动与其对应的配置
	 * @see #browserList()
	 * @deprecated remove in 1.2.0
	 */
	public Map<String, String> driverMap()
	{
		Map<String, String> driverMap = new HashMap<String, String>();

		String xpathStr = String.format("//mapping/map");
		XPath xpath = new DefaultXPath(xpathStr);

		@SuppressWarnings("unchecked")
        List<Element> nodes = xpath.selectNodes(document);
		for(Element ele : nodes)
		{
			String type = ele.attributeValue("type");
			String driver = ele.attributeValue("driver");
			
			driverMap.put(type, driver);
		}
		
		return driverMap;
	}
	
    /**
     * @return 浏览器配置列表
     */
    public List<Browser> browserList()
    {
        List<Browser> browserList = new ArrayList<Browser>();

        String xpathStr = String.format("//mapping/map");
        XPath xpath = new DefaultXPath(xpathStr);

        @SuppressWarnings("unchecked")
        List<Element> nodes = xpath.selectNodes(document);
        for(Element ele : nodes)
        {
            String type = ele.attributeValue("type");
            String driver = ele.attributeValue("driver");
            String alias = ele.attributeValue("alias");
            
            Browser browser = new Browser();
            browser.setName(type);
            browser.setDriver(driver);
            browser.setAlias(alias);
            browserList.add(browser);
        }
        
        return browserList;
    }
}
