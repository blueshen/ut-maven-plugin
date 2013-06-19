package cn.shenyanchao.filter;

import cn.shenyanchao.common.Consts;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author shenyanchao
 *         Date:  6/14/13
 *         Time:  4:19 PM
 */
public class DirAndJavaFilter implements FilenameFilter {


    @Override
    public boolean accept(File dir, String name) {
        return !(name.equals("package-info.java")) && (dir.isDirectory() || name.endsWith(Consts.JAVA_SUFFIX));
    }
}
