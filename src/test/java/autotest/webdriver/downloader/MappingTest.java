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

package autotest.webdriver.downloader;

import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.suren.autotest.webdriver.downloader.DriverMapping;

/**
 * 浏览器驱动映射类的单元测试
 * @author suren
 * @date 2017年5月7日 上午10:48:21
 */
public class MappingTest
{
	@Test
	public void chrome()
	{
		DriverMapping driverMapping = new DriverMapping();
		driverMapping.init();
		
		String url = driverMapping.getUrl("chrome", "56");
		
		Assert.assertNotNull(url);
	}

	@Test
	public void browserMap()
	{
		DriverMapping driverMapping = new DriverMapping();
		driverMapping.init();
		
		Map<String, Set<String>> supportBrowser = driverMapping.supportBrowser();
		
		Assert.assertNotNull(supportBrowser);
		Assert.assertTrue(supportBrowser.size() > 0);
		
		for(String type : supportBrowser.keySet())
		{
			Assert.assertNotNull(type);
			System.out.println(type);
			
			Set<String> list = supportBrowser.get(type);
			Assert.assertNotNull(list);
			Assert.assertTrue(list.size() > 0);
			System.out.println(list);
		}
	}
}
