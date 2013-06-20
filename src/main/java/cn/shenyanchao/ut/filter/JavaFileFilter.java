package cn.shenyanchao.ut.filter;

import cn.shenyanchao.ut.common.Consts;
import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 *
 * @author shenyanchao
 * Date:  6/15/13
 * Time:  4:36 PM
 */
public class JavaFileFilter implements IOFileFilter {


    @Override
    public boolean accept(File file) {
        String name = file.getName();
        return !file.isHidden() && !(name.equals("package-info.java")) && name.endsWith(Consts.JAVA_SUFFIX);
    }

    @Override
    public boolean accept(File dir, String name) {
        return true;
    }
}
