//package cn.shenyanchao.utils;
//
//import org.eclipse.jdt.core.dom.Modifier;
//
///**
// * Created with IntelliJ IDEA.
// *
// * @author shenyanchao
// *         Date:  6/14/13
// *         Time:  6:13 PM
// */
//public class ModifierTools {
//
//    public static String int2ModifierName(int modifier){
//        System.out.println("modifier=["+modifier+"]");
//        Modifier.ModifierKeyword keyword = Modifier.ModifierKeyword.fromFlagValue(modifier);
//        String modifierName = keyword.toString();
//        return modifierName;
//    }
//
//    public static int string2Modifier(String modifierName){
//        return Modifier.ModifierKeyword.toKeyword(modifierName).toFlagValue();
//    }
//}
