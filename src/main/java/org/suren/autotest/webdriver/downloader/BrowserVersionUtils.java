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
import java.net.URL;

import org.codehaus.groovy.control.CompilationFailedException;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyObject;

/**
 * 浏览器版本获取工具类
 * @author <a href="http://surenpi.com">suren</a>
 * @since 1.1.0
 */
public abstract class BrowserVersionUtils
{
    public static String getMajorVersion(String type)
    {
        URL url = BrowserVersionUtils.class.getClassLoader().getResource(type + ".groovy");
        if(url == null)
        {
            return null;
        }
        
        GroovyObject obj = null;
        try(GroovyClassLoader loader = new GroovyClassLoader())
        {
            Class<?> clz = loader.parseClass(new GroovyCodeSource(url));
            obj = (GroovyObject) (clz.newInstance());
        }
        catch (CompilationFailedException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        
        return (String) obj.invokeMethod("majorVersion", null);
    }
}
