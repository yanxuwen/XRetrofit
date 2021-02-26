package com.xretrofit.compiler.create;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.yanxuwen.xretrofit_annotations.annotation.service.NetServiceClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Elements;

/**
 * @author bsnl_yanxuwen
 * @date 2021/1/28 15:51
 * Description :
 */
public class CreateClassUtils {
    private String mBindingClassName;
    private String mClassName;
    private String mPackageName;
    private TypeElement mTypeElement;
    private final String packagename_api = "com.xretrofit.api";
    private final String packagename_bean = "com.xretrofit.bean";
    private final String packagename_utils = "com.xretrofit.utils";
    private final String packagename_method = "com.xretrofit.method";
    private Elements elementUtils;

    public CreateClassUtils(Elements elementUtils, TypeElement classElement) {
        this.mTypeElement = classElement;
        this.elementUtils = elementUtils;
        PackageElement packageElement = elementUtils.getPackageOf(mTypeElement);
        String packageName = packageElement.getQualifiedName().toString();
        String className = mTypeElement.getSimpleName().toString();
        this.mPackageName = packageName;
        this.mClassName = className;
        this.mBindingClassName = className + "$Impl";
    }


    /**
     * 创建Java代码
     *
     * @return
     */
    public TypeSpec generateJavaCode() {

        ClassName className = ClassName.get(mPackageName, mClassName);

        NetServiceClass service = (NetServiceClass) mTypeElement.getAnnotation(NetServiceClass.class);
        FieldSpec baseUrl = FieldSpec.builder(String.class, "baseUrl", Modifier.PRIVATE)
                .initializer(service != null ? "\"" + service.value() + "\"" : " null ").build();

        TypeSpec bindingClass = TypeSpec.classBuilder(mBindingClassName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(className)
                .addField(baseUrl)//添加baseUrl变量
                .addMethods(addMethod())//添加方法
                .build();
        return bindingClass;

    }


    /**
     * 循环添加方法
     */
    private List<MethodSpec> addMethod() {
        List<MethodSpec> list = new ArrayList<>();
        //获取元素集合
        List<? extends Element> listType = mTypeElement.getEnclosedElements();
        if (listType == null || listType.isEmpty()) {
            return list;
        }
        for (Element element : listType) {
            if (element.getKind() != ElementKind.METHOD) {
                continue;
            }
            ExecutableElement executableElement = (ExecutableElement) element;
            String methodName = element.getSimpleName().toString();


            MethodSpec.Builder methodSpec = MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.get(executableElement.getReturnType()))
                    .addParameters(addParameters(executableElement));
            addStatement(methodSpec, executableElement);//判断是否含有Query的注解，则添加
            list.add(methodSpec.build());
        }
        return list;
    }

    /**
     * 循环添加参数
     */
    private List<ParameterSpec> addParameters(ExecutableElement executableElement) {
        List<ParameterSpec> list = new ArrayList<>();

        //获取元素集合
        List<? extends VariableElement> listType = executableElement.getParameters();
        if (listType == null || listType.isEmpty()) {
            return list;
        }

        for (VariableElement variableElement : listType) {
            String name = variableElement.getSimpleName().toString();
            TypeName typeName = TypeName.get(variableElement.asType());
            ParameterSpec parameterSpec = ParameterSpec.builder(typeName, name)
//                .addAnnotation(nullable)//注解参数
                    .build();
            list.add(parameterSpec);
        }
        return list;
    }


    /**
     * 添加代码
     */
    private void addStatement(MethodSpec.Builder builder, ExecutableElement executableElement) {

        ClassName ServiceMethod = ClassName.get(packagename_method, "ServiceMethod");
        ClassName MethodAnnotation = ClassName.get(packagename_method, "MethodAnnotation");
        ClassName ParamAnnotation = ClassName.get(packagename_method, "ParamAnnotation");
        ClassName HashMap = ClassName.get("java.util", "HashMap");

        ClassName List = ClassName.get("java.util", "List");
        ClassName ArrayList = ClassName.get("java.util", "ArrayList");
        ClassName Object = ClassName.get("java.lang", "Object");
        builder.addStatement("$T<Class, $T> mapMa= new $T<>()", HashMap, MethodAnnotation, HashMap);
        //获取方法注解
        int numM = 0;
        for (AnnotationMirror annotationMirror : executableElement.getAnnotationMirrors()) {
            Class annotationClass = null;
            try {
                annotationClass = Class.forName(annotationMirror.getAnnotationType().toString());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            boolean isValue = false;
            builder.addStatement("$T<$T> list_ma_$N = new $T<>()", List, Object, String.valueOf(numM), ArrayList);
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet()) {
                isValue = true;
                //如果注解里面是数组，如@Headers
                if (entry.getValue().getValue() instanceof List) {
                    for (int i = 0; i < ((List) entry.getValue().getValue()).size(); i++) {
                        builder.addStatement("list_ma_$N.add($N)", String.valueOf(numM), String.valueOf(((List) entry.getValue().getValue()).get(i)));
                    }
                } else {
                    builder.addStatement("list_ma_$N.add($N)", String.valueOf(numM), String.valueOf(entry.getValue()));
                }
            }
            builder.addStatement("mapMa.put($N.class,new $T($N.class,$N))", annotationClass.getName(), MethodAnnotation, annotationClass.getName(), isValue ? "list_ma_" + numM : "null");
            numM++;
        }
        builder.addCode("\n");

        //参数添加
        builder.addStatement("List<$T> listPA = new $N<>()", ParamAnnotation, String.valueOf(ArrayList));
        int numP = 0;
        //获取参数注解
        List<? extends VariableElement> list = executableElement.getParameters();
        if (list != null) {
            for (VariableElement variableElement : list) {
                for (AnnotationMirror annotationMirror : variableElement.getAnnotationMirrors()) {
                    Class annotationClass = null;
                    try {
                        annotationClass = Class.forName(annotationMirror.getAnnotationType().toString());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    boolean isValue = false;
                    builder.addStatement("List<$T> list_pa_$N = new $N<>()", Object, String.valueOf(numP), String.valueOf(ArrayList));
                    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet()) {
                        isValue = true;
                        builder.addStatement("list_pa_$N.add($N)", String.valueOf(numP), String.valueOf(entry.getValue()));
                    }
                    builder.addStatement("listPA.add(new $T($N.class,$N,$N))", ParamAnnotation, annotationClass.getName(),
                            isValue ? "list_pa_" + numP : "null",
                            variableElement.getSimpleName());
                }
                numP++;
            }
        }

        builder.addStatement("$T serviceMethod = new $T()", ServiceMethod, ServiceMethod);

        if (executableElement.getReturnType().getKind() != TypeKind.VOID) {
            ClassName TypeToken = ClassName.get("com.google.common.reflect", "TypeToken");
            builder.addStatement("return serviceMethod.request(mapMa,listPA,new $T<$N>(){}.getType())", TypeToken, String.valueOf(executableElement.getReturnType()));
        } else {
            builder.addStatement("serviceMethod.request(mapMa,listPA,Void.class)");
        }
    }
}
