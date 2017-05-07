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
 * @author suren
 * @date 2017年5月7日 上午11:53:48
 */
public class DriverInfo
{
	private String name;
	private String version;
	private String os;
	private String arch;
	private boolean enable;
	public DriverInfo(){}
	public DriverInfo(String name)
	{
		this.name = name;
	}
	public DriverInfo(String name, String version, boolean enable)
	{
		this.name = name;
		this.version = version;
		this.enable = enable;
	}
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	/**
	 * @return the version
	 */
	public String getVersion()
	{
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version)
	{
		this.version = version;
	}
	/**
	 * @return the os
	 */
	public String getOs()
	{
		return os;
	}
	/**
	 * @param os the os to set
	 */
	public void setOs(String os)
	{
		this.os = os;
	}
	/**
	 * @return the arch
	 */
	public String getArch()
	{
		return arch;
	}
	/**
	 * @param arch the arch to set
	 */
	public void setArch(String arch)
	{
		this.arch = arch;
	}
	/**
	 * @return the enable
	 */
	public boolean isEnable()
	{
		return enable;
	}
	/**
	 * @param enable the enable to set
	 */
	public void setEnable(boolean enable)
	{
		this.enable = enable;
	}
}
