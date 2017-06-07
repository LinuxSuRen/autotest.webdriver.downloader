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

/**
 * 进度信息接口
 * @author suren
 * @date 2017年5月7日 下午12:53:03
 */
public interface Progress
{
	/**
	 * @param len 传输了的字节数
	 */
	void transfer(int len);
	
	/**
	 * 传输结束
	 */
	default void done()
	{
		System.out.println();
	}
}
