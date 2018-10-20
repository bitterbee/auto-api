package com.netease.libs.neapiprovider_process;

import com.google.auto.service.AutoService;
import com.netease.libs.neapiprovider.anno.NEApiProviderAnno;
import com.netease.libs.neapiprovider_process.generator.ApiInterfaceGenerator;
import com.netease.libs.neapiprovider_process.generator.ApiRegisterGenerator;
import com.netease.libs.neapiprovider_process.generator.ApiStubClassGenerator;
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
public class ApiProviderProcess extends AbstractProcessor {

    private String mPkgName;
    private String mApiProjectPath;

    private Messager mMessager;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        Map<String, String> options = processingEnv.getOptions();
        if (options != null && !options.isEmpty()) {
            mApiProjectPath = options.get("apiProjectPath");
            mPkgName = options.get("packageName");
        }

        mMessager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return singleton(NEApiProviderAnno.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {

        mMessager.printMessage(Diagnostic.Kind.WARNING, "api provider apt run begin");

        List<BaseClassGenerator> classGenerators = new ArrayList<>();
        List<ApiStubClassGenerator> stubClassGenerators = new ArrayList<>();
        for (Element annoElement : roundEnv.getElementsAnnotatedWith(NEApiProviderAnno.class)) {
            TypeElement annoClass = (TypeElement) annoElement;
            //检测是否是支持的注解类型，如果不是里面会报错
            if (!isValidElement(annoClass, NEApiProviderAnno.class)) {
                continue;
            }
            //获取到信息，把注解类的信息加入到列表中
            NEApiProviderAnno anno = annoElement.getAnnotation(NEApiProviderAnno.class);

            NEApiProviderClass providerClass = new NEApiProviderClass();
            providerClass.clazz = annoClass;
            providerClass.name = anno.name();
            providerClass.provideStaticApi = anno.provideStaticApi();
            providerClass.includeSuper = anno.includeSuper();

            ApiInterfaceGenerator apiGenerator = new ApiInterfaceGenerator(providerClass, mMessager, mApiProjectPath, mPkgName);
            classGenerators.add(apiGenerator);

            ApiStubClassGenerator stubGenerator = new ApiStubClassGenerator(providerClass, mMessager, apiGenerator, mPkgName);
            stubClassGenerators.add(stubGenerator);
            classGenerators.add(stubGenerator);
        }

        ApiRegisterGenerator registerGenerator = new ApiRegisterGenerator(mMessager, mPkgName, stubClassGenerators);
        classGenerators.add(registerGenerator);

        for (BaseClassGenerator generator : classGenerators) {
            try {
                TypeSpec generatedClass = generator.generate();
                JavaFile javaFile = builder(generator.packageName(), generatedClass).build();
                generator.writeTo(javaFile, filer);
            } catch (FilerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                StringBuilder sb = new StringBuilder(128);
                sb.append(e.toString()).append("\n");
                for (StackTraceElement element : e.getStackTrace()) {
                    sb.append(element.toString()).append("\n");
                }

                mMessager.printMessage(Diagnostic.Kind.ERROR, sb.toString());
            }
        }

        mMessager.printMessage(Diagnostic.Kind.WARNING, "api provider apt run finish");

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
