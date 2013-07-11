package cn.shenyanchao.ut.utils.test;

import cn.shenyanchao.ut.utils.JavaParserFactory;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.TypeDeclaration;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * Date:  13-7-11
 * Time:  下午3:13
 *
 * @author shenyanchao
 */
public class JavaParserTest {

    public static void main(String[] args) {
        CompilationUnit sourceCU = JavaParserFactory.getCompilationUnit(new File("/home/shenyanchao/IdeaProjects/" +
                "ut-maven-plugin/src/main/java/cn/shenyanchao/ut/command/NewTestCommand.java"),
                "UTF-8");
        System.out.println(sourceCU.getClass().toString());
        List<TypeDeclaration> types = sourceCU.getTypes();
        TypeDeclaration targetType = types.get(0);
        System.out.println(targetType.getName());
        System.out.println(targetType.getNameExpr().getName());
        if (targetType instanceof ClassOrInterfaceDeclaration) {
           ClassOrInterfaceDeclaration classOrInterfaceDeclaration = ((ClassOrInterfaceDeclaration) targetType);
            boolean isInterface = classOrInterfaceDeclaration.isInterface();
            System.out.println("Interface: "+isInterface);
        }
        int modifiers = targetType.getModifiers();
        System.out.println(Modifier.isInterface(modifiers));
    }
}
