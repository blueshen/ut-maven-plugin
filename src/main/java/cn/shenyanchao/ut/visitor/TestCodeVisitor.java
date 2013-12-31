package cn.shenyanchao.ut.visitor;

import cn.shenyanchao.ut.builder.FieldBuilder;
import cn.shenyanchao.ut.builder.ImportsBuilder;
import cn.shenyanchao.ut.builder.MethodBuilder;
import cn.shenyanchao.ut.factory.BlockStmtFactory;
import cn.shenyanchao.ut.utils.MethodUtils;
import cn.shenyanchao.ut.utils.TypeUtils;
import japa.parser.ASTHelper;
import japa.parser.ast.*;
import japa.parser.ast.body.*;
import japa.parser.ast.expr.*;
import japa.parser.ast.stmt.*;
import japa.parser.ast.type.*;
import japa.parser.ast.visitor.GenericVisitor;
import org.apache.commons.lang.StringUtils;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Date:  13-12-17
 * Time:  下午6:03
 *
 * @author shenyanchao
 */
public class TestCodeVisitor implements GenericVisitor<Node, Object> {

    private static final String TEST_PACKAGE_SUFFIX = ".test";

    private static final String TEST_CLASS_SUFFIX = "Test";

    private static final String TEST_METHOD_SUFFIX = TEST_CLASS_SUFFIX;


    @Override
    public Node visit(CompilationUnit _n, Object _arg) {
        String sourcePackage = _n.getPackage().getName().toString();
        List<String> classFullNames = new ArrayList<String>();
        classFullNames.add(sourcePackage);
//        for (TypeDeclaration type : _n.getTypes()) {
//            classFullNames.add(sourcePackage + "." + type.getName());
//        }
        List<TypeDeclaration> types = visitTypes(_n.getTypes(), _arg);
        if (!types.isEmpty()) {
            PackageDeclaration package_ = (PackageDeclaration) visit(_n.getPackage(), _arg);
            List<ImportDeclaration> imports = visitImports(_n.getImports(), classFullNames);
            List<Comment> comments = visit(_n.getComments(), _arg);
            return new CompilationUnit(package_, imports, types, comments);
        } else {  //不包含需要处理的类型
            return new CompilationUnit();
        }
    }

    @Override
    public Node visit(PackageDeclaration _n, Object _arg) {
        List<AnnotationExpr> annotations = visit(_n.getAnnotations(), _arg);
        NameExpr name = cloneNodes(_n.getName(), _arg);
        String testPackage = "test";//默认无package
        if (StringUtils.isNotBlank(name.getName())) {
            testPackage = name.getName() + TEST_PACKAGE_SUFFIX;
        }
        name.setName(testPackage);
        Comment comment = null;
        PackageDeclaration r = new PackageDeclaration(annotations, name);
        r.setComment(comment);
        return r;
    }

    /**
     * add imports for test
     *
     * @param _nodes
     * @param _arg
     * @return
     */
    public List<ImportDeclaration> visitImports(List<ImportDeclaration> _nodes, Object _arg) {

        ImportsBuilder importsBuilder = new ImportsBuilder();
        importsBuilder.buildTestNGImports().buildMockitoImports();
        for (String arg : (List<String>) _arg) {
            importsBuilder.buildImportByName(arg);
        }
        List<ImportDeclaration> defaultImports = importsBuilder.build();
        if (null == _nodes) {
            return defaultImports;
        }
        List<ImportDeclaration> r = new ArrayList<ImportDeclaration>(_nodes.size());
        for (ImportDeclaration n : _nodes) {
            ImportDeclaration rN = (ImportDeclaration) visit(n, _arg);
            if (rN != null) {
                r.add(rN);
            }
        }
        r.addAll(defaultImports);
        return r;
    }

    public List<TypeDeclaration> visitTypes(List<TypeDeclaration> _nodes, Object _arg) {
        if (_nodes == null) {
            return null;
        }
        List<TypeDeclaration> r = new ArrayList<TypeDeclaration>(_nodes.size());
        for (TypeDeclaration n : _nodes) {
            if ((n instanceof ClassOrInterfaceDeclaration) && !((ClassOrInterfaceDeclaration) n).isInterface()
                    && TypeUtils.isNeedTest(n) && !TypeUtils.isJavaBean(n)) {
                n = (ClassOrInterfaceDeclaration) visit((ClassOrInterfaceDeclaration) n, _arg);
            } else {
                continue;
            }

            r.add(n);
        }
        return r;
    }

    public List<BodyDeclaration> visitMembers(List<BodyDeclaration> _nodes, Object _arg) {
        List<BodyDeclaration> members = new ArrayList<BodyDeclaration>();
        for (BodyDeclaration member : _nodes) {
            if (member instanceof MethodDeclaration && MethodUtils.isNeedTest((MethodDeclaration) member)) {
                member = (MethodDeclaration) visit((MethodDeclaration) member, _arg);
            } else if (member instanceof FieldDeclaration) {
                member = (FieldDeclaration) visit((FieldDeclaration) member, _arg);
            } else {
                continue; // 暂不处理其他类型
            }

            members.add(member);

        }
        return members;
    }

    @Override
    public Node visit(ImportDeclaration _n, Object _arg) {
        NameExpr name = cloneNodes(_n.getName(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        ImportDeclaration r = new ImportDeclaration(
                name, _n.isStatic(), _n.isAsterisk()
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(TypeParameter _n, Object _arg) {
        List<ClassOrInterfaceType> typeBound = visit(_n.getTypeBound(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        TypeParameter r = new TypeParameter(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                _n.getName(), typeBound
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(LineComment _n, Object _arg) {
        return new LineComment(_n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(),
                _n.getEndColumn(), _n.getContent());
    }

    @Override
    public Node visit(BlockComment _n, Object _arg) {
        return new BlockComment(_n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(),
                _n.getEndColumn(), _n.getContent());
    }

    /**
     * 处理interface or class
     *
     * @param _n
     * @param _arg
     * @return
     */
    @Override
    public Node visit(ClassOrInterfaceDeclaration _n, Object _arg) {
        JavadocComment javaDoc = cloneNodes(_n.getJavaDoc(), _arg);
        List<AnnotationExpr> annotations = null;
        List<TypeParameter> typeParameters = visit(_n.getTypeParameters(), _arg);
        List<ClassOrInterfaceType> extendsList = null;
        List<ClassOrInterfaceType> implementsList = null;

        FieldDeclaration targetDeclaration = null;
        MethodDeclaration mockMethod = null;
        if (!_n.isInterface()) {
            String clazzName = _n.getName();
            String varName = StringUtils.lowerCase(clazzName.charAt(0) + "") + clazzName.substring(1);
            FieldBuilder fieldBuilder = new FieldBuilder();
            fieldBuilder.buildModifer(ModifierSet.PRIVATE).buildFieldType(clazzName).buildFieldVarName(varName)
                    .buildFieldAnnotation(InjectMocks.class.getSimpleName());
            targetDeclaration = fieldBuilder.build();

            MethodBuilder methodBuilder = new MethodBuilder();
            methodBuilder.buildMethodModifier(ModifierSet.PUBLIC).buildMethodReturnType(ASTHelper.VOID_TYPE)
                    .buildMethodName("initMocks").buildMethodAnnotations(BeforeClass.class.getSimpleName());

            BlockStmt blockStmt = BlockStmtFactory.createInitMockStmt();
            methodBuilder.buildBody(blockStmt);
            mockMethod = methodBuilder.build();
        }
        List<BodyDeclaration> members = new ArrayList<BodyDeclaration>();
        members.add(targetDeclaration);
        members.add(mockMethod);
        members.addAll(visitMembers(_n.getMembers(), _arg));
        Comment comment = cloneNodes(_n.getComment(), _arg);

        ClassOrInterfaceDeclaration r = new ClassOrInterfaceDeclaration(javaDoc, _n.getModifiers(),
                annotations, _n.isInterface(), _n.getName() + TEST_CLASS_SUFFIX, typeParameters, extendsList,
                implementsList, members
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(EnumDeclaration _n, Object _arg) {
        JavadocComment javaDoc = cloneNodes(_n.getJavaDoc(), _arg);
        List<AnnotationExpr> annotations = visit(_n.getAnnotations(), _arg);
        List<ClassOrInterfaceType> implementsList = visit(_n.getImplements(), _arg);
        List<EnumConstantDeclaration> entries = visit(_n.getEntries(), _arg);
        List<BodyDeclaration> members = visit(_n.getMembers(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        EnumDeclaration r = new EnumDeclaration(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                javaDoc, _n.getModifiers(), annotations, _n.getName(), implementsList, entries, members
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(EmptyTypeDeclaration _n, Object _arg) {
        JavadocComment javaDoc = cloneNodes(_n.getJavaDoc(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        EmptyTypeDeclaration r = new EmptyTypeDeclaration(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                javaDoc
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(EnumConstantDeclaration _n, Object _arg) {
        JavadocComment javaDoc = cloneNodes(_n.getJavaDoc(), _arg);
        List<AnnotationExpr> annotations = visit(_n.getAnnotations(), _arg);
        List<Expression> args = visit(_n.getArgs(), _arg);
        List<BodyDeclaration> classBody = visit(_n.getClassBody(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        EnumConstantDeclaration r = new EnumConstantDeclaration(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                javaDoc, annotations, _n.getName(), args, classBody
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(AnnotationDeclaration _n, Object _arg) {
        JavadocComment javaDoc = cloneNodes(_n.getJavaDoc(), _arg);
        List<AnnotationExpr> annotations = visit(_n.getAnnotations(), _arg);
        List<BodyDeclaration> members = visit(_n.getMembers(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        AnnotationDeclaration r = new AnnotationDeclaration(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                javaDoc, _n.getModifiers(), annotations, _n.getName(), members
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(AnnotationMemberDeclaration _n, Object _arg) {
        JavadocComment javaDoc = cloneNodes(_n.getJavaDoc(), _arg);
        List<AnnotationExpr> annotations = visit(_n.getAnnotations(), _arg);
        Type type_ = cloneNodes(_n.getType(), _arg);
        Expression defaultValue = cloneNodes(_n.getDefaultValue(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        AnnotationMemberDeclaration r = new AnnotationMemberDeclaration(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                javaDoc, _n.getModifiers(), annotations, type_, _n.getName(), defaultValue
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(FieldDeclaration _n, Object _arg) {
        JavadocComment javaDoc = cloneNodes(_n.getJavaDoc(), _arg);
        List<AnnotationExpr> annotations = new ArrayList<AnnotationExpr>();
        annotations.add(new MarkerAnnotationExpr(new NameExpr(MockitoAnnotations.Mock.class.getSimpleName())));
        Type type_ = cloneNodes(_n.getType(), _arg);
        List<VariableDeclarator> variables = visit(_n.getVariables(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        FieldDeclaration r = new FieldDeclaration(
                javaDoc, Modifier.PRIVATE, annotations, type_, variables
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(VariableDeclarator _n, Object _arg) {
        VariableDeclaratorId id = cloneNodes(_n.getId(), _arg);
        Expression init = cloneNodes(_n.getInit(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        VariableDeclarator r = new VariableDeclarator(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                id, init
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(VariableDeclaratorId _n, Object _arg) {
        Comment comment = cloneNodes(_n.getComment(), _arg);

        VariableDeclaratorId r = new VariableDeclaratorId(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                _n.getName(), _n.getArrayCount()
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(ConstructorDeclaration _n, Object _arg) {
        JavadocComment javaDoc = cloneNodes(_n.getJavaDoc(), _arg);
        List<AnnotationExpr> annotations = visit(_n.getAnnotations(), _arg);
        List<TypeParameter> typeParameters = visit(_n.getTypeParameters(), _arg);
        List<Parameter> parameters = visit(_n.getParameters(), _arg);
        List<NameExpr> throws_ = visit(_n.getThrows(), _arg);
        BlockStmt block = cloneNodes(_n.getBlock(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        ConstructorDeclaration r = new ConstructorDeclaration(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                javaDoc, _n.getModifiers(), annotations, typeParameters, _n.getName(), parameters, throws_, block
        );
        r.setComment(comment);
        return r;
    }

    /**
     * method process
     *
     * @param _n
     * @param _arg
     * @return
     */
    @Override
    public Node visit(MethodDeclaration _n, Object _arg) {
        JavadocComment javaDoc = cloneNodes(_n.getJavaDoc(), _arg);
        List<AnnotationExpr> annotations = new ArrayList<AnnotationExpr>();
        MarkerAnnotationExpr markerAnnotationExpr = new MarkerAnnotationExpr(new NameExpr(Test.class.getSimpleName()));
        annotations.add(markerAnnotationExpr);
        List<TypeParameter> typeParameters = null;
        Type type_ = new VoidType();
        List<Parameter> parameters = null;
        List<NameExpr> throws_ = null;
        BlockStmt block = (BlockStmt) visit(_n.getBody(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        MethodDeclaration r = new MethodDeclaration(
                javaDoc, _n.getModifiers(), annotations, typeParameters, type_, _n.getName() + TEST_METHOD_SUFFIX,
                parameters, _n.getArrayCount(), throws_, block
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(Parameter _n, Object _arg) {
        List<AnnotationExpr> annotations = visit(_n.getAnnotations(), _arg);
        Type type_ = cloneNodes(_n.getType(), _arg);
        VariableDeclaratorId id = cloneNodes(_n.getId(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        Parameter r = new Parameter(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                _n.getModifiers(), annotations, type_, _n.isVarArgs(), id
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(EmptyMemberDeclaration _n, Object _arg) {
        JavadocComment javaDoc = cloneNodes(_n.getJavaDoc(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        EmptyMemberDeclaration r = new EmptyMemberDeclaration(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                javaDoc
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(InitializerDeclaration _n, Object _arg) {
        JavadocComment javaDoc = cloneNodes(_n.getJavaDoc(), _arg);
        BlockStmt block = cloneNodes(_n.getBlock(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        InitializerDeclaration r = new InitializerDeclaration(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                javaDoc, _n.isStatic(), block
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(JavadocComment _n, Object _arg) {
        Comment comment = cloneNodes(_n.getComment(), _arg);
        JavadocComment r = new JavadocComment(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                _n.getContent()
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(ClassOrInterfaceType _n, Object _arg) {
        ClassOrInterfaceType scope = cloneNodes(_n.getScope(), _arg);
        List<Type> typeArgs = visit(_n.getTypeArgs(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        ClassOrInterfaceType r = new ClassOrInterfaceType(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                scope, _n.getName(), typeArgs
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(PrimitiveType _n, Object _arg) {
        Comment comment = cloneNodes(_n.getComment(), _arg);

        PrimitiveType r = new PrimitiveType(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                _n.getType()
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(ReferenceType _n, Object _arg) {
        Type type_ = cloneNodes(_n.getType(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        ReferenceType r = new ReferenceType(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                type_, _n.getArrayCount()
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(VoidType _n, Object _arg) {
        Comment comment = cloneNodes(_n.getComment(), _arg);

        VoidType r = new VoidType(_n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn());
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(WildcardType _n, Object _arg) {
        ReferenceType ext = cloneNodes(_n.getExtends(), _arg);
        ReferenceType sup = cloneNodes(_n.getSuper(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        WildcardType r = new WildcardType(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                ext, sup
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(ArrayAccessExpr _n, Object _arg) {
        Expression name = cloneNodes(_n.getName(), _arg);
        Expression index = cloneNodes(_n.getIndex(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        ArrayAccessExpr r = new ArrayAccessExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                name, index
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(ArrayCreationExpr _n, Object _arg) {
        Type type_ = cloneNodes(_n.getType(), _arg);
        List<Expression> dimensions = visit(_n.getDimensions(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        ArrayCreationExpr r = new ArrayCreationExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                type_, dimensions, _n.getArrayCount()
        );
        r.setComment(comment);
        if (_n.getInitializer() != null) {// ArrayCreationExpr has two mutually exclusive constructors
            r.setInitializer(cloneNodes(_n.getInitializer(), _arg));
        }
        return r;
    }

    @Override
    public Node visit(ArrayInitializerExpr _n, Object _arg) {
        List<Expression> values = visit(_n.getValues(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        ArrayInitializerExpr r = new ArrayInitializerExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                values
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(AssignExpr _n, Object _arg) {
        Expression target = cloneNodes(_n.getTarget(), _arg);
        Expression value = cloneNodes(_n.getValue(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        AssignExpr r = new AssignExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                target, value, _n.getOperator());
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(BinaryExpr _n, Object _arg) {
        Expression left = cloneNodes(_n.getLeft(), _arg);
        Expression right = cloneNodes(_n.getRight(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        BinaryExpr r = new BinaryExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                left, right, _n.getOperator()
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(CastExpr _n, Object _arg) {
        Type type_ = cloneNodes(_n.getType(), _arg);
        Expression expr = cloneNodes(_n.getExpr(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        CastExpr r = new CastExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                type_, expr
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(ClassExpr _n, Object _arg) {
        Type type_ = cloneNodes(_n.getType(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        ClassExpr r = new ClassExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                type_
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(ConditionalExpr _n, Object _arg) {
        Expression condition = cloneNodes(_n.getCondition(), _arg);
        Expression thenExpr = cloneNodes(_n.getThenExpr(), _arg);
        Expression elseExpr = cloneNodes(_n.getElseExpr(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        ConditionalExpr r = new ConditionalExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                condition, thenExpr, elseExpr
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(EnclosedExpr _n, Object _arg) {
        Expression inner = cloneNodes(_n.getInner(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        EnclosedExpr r = new EnclosedExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                inner
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(FieldAccessExpr _n, Object _arg) {
        Expression scope = cloneNodes(_n.getScope(), _arg);
        List<Type> typeArgs = visit(_n.getTypeArgs(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        FieldAccessExpr r = new FieldAccessExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                scope, typeArgs, _n.getField()
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(InstanceOfExpr _n, Object _arg) {
        Expression expr = cloneNodes(_n.getExpr(), _arg);
        Type type_ = cloneNodes(_n.getType(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        InstanceOfExpr r = new InstanceOfExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                expr, type_
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(StringLiteralExpr _n, Object _arg) {
        Comment comment = cloneNodes(_n.getComment(), _arg);
        StringLiteralExpr r = new StringLiteralExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                _n.getValue()
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(IntegerLiteralExpr _n, Object _arg) {
        Comment comment = cloneNodes(_n.getComment(), _arg);

        IntegerLiteralExpr r = new IntegerLiteralExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                _n.getValue()
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(LongLiteralExpr _n, Object _arg) {
        Comment comment = cloneNodes(_n.getComment(), _arg);

        LongLiteralExpr r = new LongLiteralExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                _n.getValue()
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(IntegerLiteralMinValueExpr _n, Object _arg) {
        Comment comment = cloneNodes(_n.getComment(), _arg);

        IntegerLiteralMinValueExpr r = new IntegerLiteralMinValueExpr(_n.getBeginLine(),
                _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn());
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(LongLiteralMinValueExpr _n, Object _arg) {
        Comment comment = cloneNodes(_n.getComment(), _arg);

        LongLiteralMinValueExpr r = new LongLiteralMinValueExpr(_n.getBeginLine(),
                _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn());
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(CharLiteralExpr _n, Object _arg) {
        Comment comment = cloneNodes(_n.getComment(), _arg);

        CharLiteralExpr r = new CharLiteralExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                _n.getValue()
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(DoubleLiteralExpr _n, Object _arg) {
        Comment comment = cloneNodes(_n.getComment(), _arg);

        DoubleLiteralExpr r = new DoubleLiteralExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                _n.getValue()
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(BooleanLiteralExpr _n, Object _arg) {
        Comment comment = cloneNodes(_n.getComment(), _arg);

        BooleanLiteralExpr r = new BooleanLiteralExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                _n.getValue()
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(NullLiteralExpr _n, Object _arg) {
        Comment comment = cloneNodes(_n.getComment(), _arg);

        NullLiteralExpr r = new NullLiteralExpr(_n.getBeginLine(), _n.getBeginColumn(),
                _n.getEndLine(), _n.getEndColumn());
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(MethodCallExpr _n, Object _arg) {
        Expression scope = cloneNodes(_n.getScope(), _arg);
        List<Type> typeArgs = visit(_n.getTypeArgs(), _arg);
        List<Expression> args = visit(_n.getArgs(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        MethodCallExpr r = new MethodCallExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                scope, typeArgs, _n.getName(), args
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(NameExpr _n, Object _arg) {
        Comment comment = cloneNodes(_n.getComment(), _arg);

        NameExpr r = new NameExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                _n.getName()
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(ObjectCreationExpr _n, Object _arg) {
        Expression scope = cloneNodes(_n.getScope(), _arg);
        ClassOrInterfaceType type_ = cloneNodes(_n.getType(), _arg);
        List<Type> typeArgs = visit(_n.getTypeArgs(), _arg);
        List<Expression> args = visit(_n.getArgs(), _arg);
        List<BodyDeclaration> anonymousBody = visit(_n.getAnonymousClassBody(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        ObjectCreationExpr r = new ObjectCreationExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                scope, type_, typeArgs, args, anonymousBody
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(QualifiedNameExpr _n, Object _arg) {
        NameExpr scope = cloneNodes(_n.getQualifier(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        QualifiedNameExpr r = new QualifiedNameExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                scope, _n.getName()
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(ThisExpr _n, Object _arg) {
        Expression classExpr = cloneNodes(_n.getClassExpr(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        ThisExpr r = new ThisExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                classExpr
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(SuperExpr _n, Object _arg) {
        Expression classExpr = cloneNodes(_n.getClassExpr(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        SuperExpr r = new SuperExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                classExpr
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(UnaryExpr _n, Object _arg) {
        Expression expr = cloneNodes(_n.getExpr(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        UnaryExpr r = new UnaryExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                expr, _n.getOperator()
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(VariableDeclarationExpr _n, Object _arg) {
        List<AnnotationExpr> annotations = visit(_n.getAnnotations(), _arg);
        Type type_ = cloneNodes(_n.getType(), _arg);
        List<VariableDeclarator> vars = visit(_n.getVars(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        VariableDeclarationExpr r = new VariableDeclarationExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                _n.getModifiers(), annotations, type_, vars
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(MarkerAnnotationExpr _n, Object _arg) {
        NameExpr name = cloneNodes(_n.getName(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        MarkerAnnotationExpr r = new MarkerAnnotationExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                name
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(SingleMemberAnnotationExpr _n, Object _arg) {
        NameExpr name = cloneNodes(_n.getName(), _arg);
        Expression memberValue = cloneNodes(_n.getMemberValue(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        SingleMemberAnnotationExpr r = new SingleMemberAnnotationExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                name, memberValue
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(NormalAnnotationExpr _n, Object _arg) {
        NameExpr name = cloneNodes(_n.getName(), _arg);
        List<MemberValuePair> pairs = visit(_n.getPairs(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        NormalAnnotationExpr r = new NormalAnnotationExpr(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                name, pairs
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(MemberValuePair _n, Object _arg) {
        Expression value = cloneNodes(_n.getValue(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        MemberValuePair r = new MemberValuePair(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                _n.getName(), value
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(ExplicitConstructorInvocationStmt _n, Object _arg) {
        List<Type> typeArgs = visit(_n.getTypeArgs(), _arg);
        Expression expr = cloneNodes(_n.getExpr(), _arg);
        List<Expression> args = visit(_n.getArgs(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        ExplicitConstructorInvocationStmt r = new ExplicitConstructorInvocationStmt(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                typeArgs, _n.isThis(), expr, args
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(TypeDeclarationStmt _n, Object _arg) {
        TypeDeclaration typeDecl = cloneNodes(_n.getTypeDeclaration(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        TypeDeclarationStmt r = new TypeDeclarationStmt(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                typeDecl
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(AssertStmt _n, Object _arg) {
        Expression check = cloneNodes(_n.getCheck(), _arg);
        Expression message = cloneNodes(_n.getMessage(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        AssertStmt r = new AssertStmt(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                check, message
        );
        r.setComment(comment);
        return r;
    }

    /**
     * 处理代码块
     *
     * @param _n
     * @param _arg
     * @return
     */
    @Override
    public Node visit(BlockStmt _n, Object _arg) {
        BlockStmt r = BlockStmtFactory.createAssertStmt();
        Comment comment = null;
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(LabeledStmt _n, Object _arg) {
        Statement stmt = cloneNodes(_n.getStmt(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        LabeledStmt r = new LabeledStmt(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                _n.getLabel(), stmt
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(EmptyStmt _n, Object _arg) {
        Comment comment = cloneNodes(_n.getComment(), _arg);

        EmptyStmt r = new EmptyStmt(_n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn());
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(ExpressionStmt _n, Object _arg) {
        Expression expr = cloneNodes(_n.getExpression(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        ExpressionStmt r = new ExpressionStmt(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                expr
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(SwitchStmt _n, Object _arg) {
        Expression selector = cloneNodes(_n.getSelector(), _arg);
        List<SwitchEntryStmt> entries = visit(_n.getEntries(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        SwitchStmt r = new SwitchStmt(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                selector, entries
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(SwitchEntryStmt _n, Object _arg) {
        Expression label = cloneNodes(_n.getLabel(), _arg);
        List<Statement> stmts = visit(_n.getStmts(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        SwitchEntryStmt r = new SwitchEntryStmt(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                label, stmts
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(BreakStmt _n, Object _arg) {
        Comment comment = cloneNodes(_n.getComment(), _arg);

        BreakStmt r = new BreakStmt(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                _n.getId()
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(ReturnStmt _n, Object _arg) {
        Expression expr = cloneNodes(_n.getExpr(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        ReturnStmt r = new ReturnStmt(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                expr
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(IfStmt _n, Object _arg) {
        Expression condition = cloneNodes(_n.getCondition(), _arg);
        Statement thenStmt = cloneNodes(_n.getThenStmt(), _arg);
        Statement elseStmt = cloneNodes(_n.getElseStmt(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        IfStmt r = new IfStmt(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                condition, thenStmt, elseStmt
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(WhileStmt _n, Object _arg) {
        Expression condition = cloneNodes(_n.getCondition(), _arg);
        Statement body = cloneNodes(_n.getBody(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        WhileStmt r = new WhileStmt(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                condition, body
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(ContinueStmt _n, Object _arg) {
        Comment comment = cloneNodes(_n.getComment(), _arg);

        ContinueStmt r = new ContinueStmt(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                _n.getId()
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(DoStmt _n, Object _arg) {
        Statement body = cloneNodes(_n.getBody(), _arg);
        Expression condition = cloneNodes(_n.getCondition(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        DoStmt r = new DoStmt(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                body, condition
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(ForeachStmt _n, Object _arg) {
        VariableDeclarationExpr var = cloneNodes(_n.getVariable(), _arg);
        Expression iterable = cloneNodes(_n.getIterable(), _arg);
        Statement body = cloneNodes(_n.getBody(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        ForeachStmt r = new ForeachStmt(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                var, iterable, body
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(ForStmt _n, Object _arg) {
        List<Expression> init = visit(_n.getInit(), _arg);
        Expression compare = cloneNodes(_n.getCompare(), _arg);
        List<Expression> update = visit(_n.getUpdate(), _arg);
        Statement body = cloneNodes(_n.getBody(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        ForStmt r = new ForStmt(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                init, compare, update, body
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(ThrowStmt _n, Object _arg) {
        Expression expr = cloneNodes(_n.getExpr(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        ThrowStmt r = new ThrowStmt(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                expr
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(SynchronizedStmt _n, Object _arg) {
        Expression expr = cloneNodes(_n.getExpr(), _arg);
        BlockStmt block = cloneNodes(_n.getBlock(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        SynchronizedStmt r = new SynchronizedStmt(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                expr, block
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(TryStmt _n, Object _arg) {
        BlockStmt tryBlock = cloneNodes(_n.getTryBlock(), _arg);
        List<CatchClause> catchs = visit(_n.getCatchs(), _arg);
        BlockStmt finallyBlock = cloneNodes(_n.getFinallyBlock(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        TryStmt r = new TryStmt(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                tryBlock, catchs, finallyBlock
        );
        r.setComment(comment);
        return r;
    }

    @Override
    public Node visit(CatchClause _n, Object _arg) {
        Parameter except = cloneNodes(_n.getExcept(), _arg);
        BlockStmt catchBlock = cloneNodes(_n.getCatchBlock(), _arg);
        Comment comment = cloneNodes(_n.getComment(), _arg);

        CatchClause r = new CatchClause(
                _n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
                except, catchBlock
        );
        r.setComment(comment);
        return r;
    }

    public <T extends Node> List<T> visit(List<T> _nodes, Object _arg) {
        if (_nodes == null) {
            return null;
        }
        List<T> r = new ArrayList<T>(_nodes.size());
        for (T n : _nodes) {
            T rN = cloneNodes(n, _arg);
            if (rN != null) {
                r.add(rN);
            }
        }
        return r;
    }

    protected <T extends Node> T cloneNodes(T _node, Object _arg) {
        if (_node == null) {
            return null;
        }
        Node r = _node.accept(this, _arg);
        if (r == null) {
            return null;
        }
        return (T) r;
    }
}
