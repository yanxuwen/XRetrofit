package com.xretrofit.compiler;

import com.google.auto.service.AutoService;
import com.xretrofit.compiler.create.CreateClassUtils;
import com.squareup.javapoet.JavaFile;
import com.yanxuwen.xretrofit_annotations.annotation.service.NetServiceClass;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * @author bsnl_yanxuwen
 * @date 2021/1/28 15:49
 * Description :
 */
@AutoService(Processor.class)
public class HttpServiceProcessor2 extends AbstractProcessor {

    private Messager mMessager;//可以用来打印一些信息
    private Elements mElementUtils;
    private Filer mFiler;//生成java源码
    private Map<String, CreateClassUtils> mapClass = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mMessager = processingEnv.getMessager();
        mElementUtils = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
    }

    /**
     * 指定注解
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> supportTypes = new LinkedHashSet<>();
        supportTypes.add(NetServiceClass.class.getCanonicalName());
        return supportTypes;
    }

    /**
     * 用来指定你使用的Java版本
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }



    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, "processing...");

        //step1:得到所有的注解
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(NetServiceClass.class);
        if (elements == null || elements.isEmpty()) {
            mMessager.printMessage(Diagnostic.Kind.NOTE, "elements is null");
            return true;
        }
        for (Element element : elements) {
            if (element.getKind() != ElementKind.INTERFACE) {
                continue;
            }
            TypeElement classElement = (TypeElement) element;

            CreateClassUtils creatorUtils = mapClass.get(getKey(classElement));
            if (creatorUtils == null) {
                creatorUtils = new CreateClassUtils(mElementUtils, classElement);
            }
            mapClass.put(getKey(classElement), creatorUtils);
        }

//        //step3:创建DataCallBack
//        ClassCallBackUtils backUtils = new ClassCallBackUtils(mElementUtils);
//        //创建文件
//        try {
//            JavaFile javaFile = JavaFile.builder(ElementUtils.packageName, backUtils.generateJavaCode()).build();
//            javaFile.writeTo(processingEnv.getFiler());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //step3:遍历存储的类信息，创建java文件
        for (String key : mapClass.keySet()) {
            CreateClassUtils creatorUtils = mapClass.get(key);
            try {
                //创建文件
                JavaFile javaFile = JavaFile.builder(ElementUtils.packageName, creatorUtils.generateJavaCode()).build();
                javaFile.writeTo(processingEnv.getFiler());

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return true;
    }

    /**
     * 根据类元素获取 key
     * 包名 + 类名
     */
    public String getKey(TypeElement classElement) {
        if (mElementUtils == null) {
            return "";
        }
        PackageElement packageElement = mElementUtils.getPackageOf(classElement);
        String packageName = packageElement.getQualifiedName().toString();
        String className = classElement.getSimpleName().toString();
        return packageName + "." + className;
    }
}
