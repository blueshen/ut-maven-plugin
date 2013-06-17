package cn.shenyanchao.filter;

import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 *
 * @author shenyanchao
 *         Date:  6/15/13
 *         Time:  4:36 PM
 */
public class JavaFileFilter implements IOFileFilter {

    private static final String JAVA_SUFFIX = ".java";

    @Override
    public boolean accept(File file) {
        String name =file.getName();
        return !file.isHidden()&&!(name.equals("package-info.java")) && name.endsWith(JAVA_SUFFIX);
    }

    @Override
    public boolean accept(File dir, String name) {
        return true;
    }
}
