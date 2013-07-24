package cn.shenyanchao.ut.utils;

import java.io.File;

/**
 * Date:  6/17/13
 * Time:  1:32 PM
 *
 * @author shenyanchao
 */
public class PathUtils {

    public static String packageToPath(final String packageName) {
        String path = packageName.replaceAll("[.]", File.separator);
        return path;
    }
}
