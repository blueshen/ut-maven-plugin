package cn.shenyanchao.ut.builder;

import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.expr.NameExpr;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:  13-12-30
 * Time:  下午5:31
 *
 * @author shenyanchao
 */
public class ImportsBuilder {

    private  List<ImportDeclaration> importsList = new ArrayList<ImportDeclaration>();

    public ImportsBuilder buildTestNGImports() {
        List<ImportDeclaration> testngImports = new ArrayList<ImportDeclaration>();
        testngImports.add(new ImportDeclaration(new NameExpr("org.testng.annotations"),
                false, true));
        testngImports.add(new ImportDeclaration(new NameExpr(Assert.class.getName()),
                false, false));
        importsList.addAll(testngImports);
        return this;
    }

    public ImportsBuilder buildMockitoImports() {
        List<ImportDeclaration> mockitoImports = new ArrayList<ImportDeclaration>();
        mockitoImports.add(new ImportDeclaration(new NameExpr(Mockito.class.getName()),
                true, true));
        mockitoImports.add(new ImportDeclaration(new NameExpr(InjectMocks.class.getName()),
                false, false));
        mockitoImports.add(new ImportDeclaration(new NameExpr(Mock.class.getName()),
                false, false));
        mockitoImports.add(new ImportDeclaration(new NameExpr(MockitoAnnotations.class.getName()),
                false, false));
        importsList.addAll(mockitoImports);
        return this;
    }

    public List<ImportDeclaration> build(){
        return this.importsList;
    }
}
