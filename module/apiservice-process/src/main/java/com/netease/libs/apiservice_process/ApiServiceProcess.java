package com.netease.libs.apiservice_process;

import com.google.auto.service.AutoService;
import com.netease.libs.apiservice.anno.ApiServiceClassAnno;
import com.netease.libs.apiservice_process.generator.ApiBaseGenerator;
import com.netease.libs.apiservice_process.generator.ApiFactoryGenerator;
import com.netease.libs.apiservice_process.generator.ApiGenerator;
import com.netease.libs.apiservice_process.generator.ApiRegisterGenerator;
import com.netease.libs.apiservice_process.generator.StubClassGenerator;
import com.netease.libs.apiservice_process.generator.StubFactoryGenerator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.FilerException;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import static com.squareup.javapoet.JavaFile.builder;
import static java.util.Collections.singleton;
import static javax.lang.model.SourceVersion.latestSupported;
import static javax.tools.Diagnostic.Kind.ERROR;

/**
 * Created by zyl06 on 2018/10/16.
 */
@AutoService(Processor.class)
public class ApiServiceProcess extends AbstractProcessor {

    private String mPkgName;
    private String mApiProjectPath;

    private Messager mMessager;
    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        Map<String, String> options = processingEnv.getOptions();
        if (options != null && !options.isEmpty()) {
            mApiProjectPath = options.get("apiProjectPath");
            mPkgName = options.get("packageName");
        }

        mMessager = processingEnv.getMessager();
        mFiler = processingEnv.getFiler();
        BaseClassGenerator.sElementUtil = processingEnv.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return singleton(ApiServiceClassAnno.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {

        mMessager.printMessage(Diagnostic.Kind.NOTE, "api provider apt run begin");

        ElementUtil.API_GENERATORS.clear();
        ElementUtil.STUB_GENERATORS.clear();

        List<BaseClassGenerator> classGenerators = new ArrayList<>();
        List<StubFactoryGenerator> stubFactoryGenerators = new ArrayList<>();

        // api 接口的统一父接口
        classGenerators.add(new ApiBaseGenerator(mMessager, mApiProjectPath));

        for (Element annoElement : roundEnv.getElementsAnnotatedWith(ApiServiceClassAnno.class)) {
            TypeElement annoClass = (TypeElement) annoElement;
            //检测是否是支持的注解类型，如果不是里面会报错
            if (!isValidElement(annoClass, ApiServiceClassAnno.class)) {
                continue;
            }
            //获取到信息，把注解类的信息加入到列表中
            ApiServiceClassAnno anno = annoElement.getAnnotation(ApiServiceClassAnno.class);

            ApiServiceClass providerClass = new ApiServiceClass();
            providerClass.clazz = annoClass;
            providerClass.name = anno.name();
            providerClass.allPublicStaticApi = anno.allPublicStaticApi();

            ApiGenerator apiGen = new ApiGenerator(providerClass, mMessager, mApiProjectPath, mPkgName);
            classGenerators.add(apiGen);
            ElementUtil.API_GENERATORS.put(ClassName.get(annoClass), apiGen);

            StubClassGenerator stubGen = new StubClassGenerator(providerClass, mMessager, apiGen, mPkgName);
            classGenerators.add(stubGen);
            ElementUtil.STUB_GENERATORS.put(ClassName.get(annoClass), stubGen);

            ApiFactoryGenerator apiFactoryGen = new ApiFactoryGenerator(providerClass, mMessager, mApiProjectPath, mPkgName, apiGen);
            classGenerators.add(apiFactoryGen);

            StubFactoryGenerator stubFactoryGen = new StubFactoryGenerator(providerClass, mMessager, mPkgName, apiGen, stubGen, apiFactoryGen);
            classGenerators.add(stubFactoryGen);
            stubFactoryGenerators.add(stubFactoryGen);
        }

        ApiRegisterGenerator registerGenerator = new ApiRegisterGenerator(mMessager, mPkgName, stubFactoryGenerators);
        classGenerators.add(registerGenerator);

        for (BaseClassGenerator generator : classGenerators) {
            try {
                TypeSpec generatedClass = generator.generate();
                JavaFile javaFile = builder(generator.packageName(), generatedClass).build();
                generator.writeTo(javaFile, mFiler);
            } catch (FilerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder(128);
                sb.append(e.toString()).append("\n");
                for (StackTraceElement element : e.getStackTrace()) {
                    sb.append(element.toString()).append("\n");
                }

                mMessager.printMessage(Diagnostic.Kind.ERROR, sb.toString());
            }
        }

        mMessager.printMessage(Diagnostic.Kind.NOTE, "api provider apt run finish");

        return true;
    }

    private boolean isValidElement(Element annotatedClass, Class annoClass) {

        if (!ElementUtil.isPublic(annotatedClass)) {
            String message = String.format("Classes annotated with %s must be public.", "@" + annoClass.getSimpleName());
            mMessager.printMessage(ERROR, message, annotatedClass);
            return false;
        }

        if (ElementUtil.isAbstract(annotatedClass)) {
            String message = String.format("Classes annotated with %s must not be abstract.", "@" + annoClass.getSimpleName());
            mMessager.printMessage(ERROR, message, annotatedClass);
            return false;
        }

        return true;
    }
}
