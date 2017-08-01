package autotest.webdriver.downloader;

import org.junit.Test;
import org.suren.autotest.webdriver.downloader.Progress;

/**
 * @author suren
 */
public class ProgressTest
{
    @Test
    public void transfer()
    {
        new DemoProgress().transfer(10);
    }
}

class DemoProgress implements Progress
{

    @Override
    public void transfer(int len)
    {
        System.setProperty("file.encoding", "utf8");
        System.out.print("acb");
        System.out.print("\b");
        System.out.print(" ");
    }
    
}
