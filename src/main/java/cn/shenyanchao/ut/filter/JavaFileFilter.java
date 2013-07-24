package cn.shenyanchao.ut.filter;

import cn.shenyanchao.ut.common.Consts;
import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.File;

/**
 * Date:  6/15/13
 * Time:  4:36 PM
 *
 * @author shenyanchao
 */
public class JavaFileFilter implements IOFileFilter {

    private static final String PACKAGE_INFO = "package-info.java";

    @Override
    public boolean accept(File file) {
        String name = file.getName();
        return !file.isHidden() && !(PACKAGE_INFO.equalsIgnoreCase(name))
                && name.endsWith(Consts.JAVA_SUFFIX);
    }

    @Override
    public boolean accept(File dir, String name) {
        return true;
    }
}
