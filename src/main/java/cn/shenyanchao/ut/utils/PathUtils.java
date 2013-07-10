package cn.shenyanchao.ut.utils;

import java.io.File;

/**
 * @author shenyanchao
 *         Date:  6/17/13
 *         Time:  1:32 PM
 */
public class PathUtils {

    public static String packageToPath(final String packageName) {
        String path = packageName.replaceAll("[.]", File.separator);
        return path;
    }
}
