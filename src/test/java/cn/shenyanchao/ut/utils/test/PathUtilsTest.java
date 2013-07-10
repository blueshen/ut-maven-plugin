package cn.shenyanchao.ut.utils.test;

import cn.shenyanchao.ut.utils.PathUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

/**
 *
 * @author shenyanchao
 *         Date:  6/17/13
 *         Time:  1:35 PM
 */
public class PathUtilsTest {


    @Test()
    public void packageToPathTest(){
        String packageName = "cn.shenyanchao.ut";
        String path = PathUtils.packageToPath(packageName);
        Assert.assertEquals(path,"cn"+ File.separator+"shenyanchao"+File.separator+"ut");
    }

}
