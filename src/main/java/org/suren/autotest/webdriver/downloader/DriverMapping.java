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
import java.util.HashSet;
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

/**
 * webdriver驱动和浏览器版本的映射
 * @author suren
 * @date 2017年2月20日 下午3:52:07
 */
public class DriverMapping
{
	private Document document;

	public void init()
	{
		try(InputStream input = this.getClass().getClassLoader().getResourceAsStream("driver.mapping.xml");
				InputStream enginePro = this.getClass().getClassLoader().getResourceAsStream("engine.properties"))
		{
			SAXReader reader = new SAXReader();
			
			document = reader.read(input);
			
			if(enginePro != null)
			{
				Properties pro = new Properties();
				pro.load(enginePro);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * @see #getUrl(String, String, String, String)
	 * @param browser
	 * @param ver
	 * @return
	 */
	public String getUrl(String browser, String ver)
	{
		return getUrl(browser, ver, "win32", "32");
	}
	
	/**
	 * @param browser
	 * @param ver
	 * @param os
	 * @param arch
	 * @return 找不到返回null
	 */
	public String getUrl(String browser, String ver, String os, String arch)
	{
		String xpathStr = String.format("//drivers/driver[@type='%s']/supports/browser[@version='%s']",
				browser, ver);
		XPath xpath = new DefaultXPath(xpathStr);

		String path = null;
		List<Element> nodes = xpath.selectNodes(document);
		for(Element ele : nodes)
		{
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

		String path = null;
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
	 */
	public Map<String, String> driverMap()
	{
		Map<String, String> driverMap = new HashMap<String, String>();

		String xpathStr = String.format("//mapping/map");
		XPath xpath = new DefaultXPath(xpathStr);

		List<Element> nodes = xpath.selectNodes(document);
		for(Element ele : nodes)
		{
			String type = ele.attributeValue("type");
			String driver = ele.attributeValue("driver");
			
			driverMap.put(type, driver);
		}
		
		return driverMap;
	}
}
