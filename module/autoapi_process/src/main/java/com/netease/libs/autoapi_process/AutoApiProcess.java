package com.netease.libs.autoapi_process;

import com.google.auto.service.AutoService;
import com.netease.libs.autoapi.anno.AutoApiClassAnno;
import com.netease.libs.autoapi_process.generator.ApiBaseGenerator;
import com.netease.libs.autoapi_process.generator.ApiRegisterGenerator;
import com.netease.libs.autoapi_process.generator.CallbackClassGenerator;
import com.netease.libs.autoapi_process.generator.StubClassGenerator;
import com.netease.libs.autoapi_process.generator.StubFactoryGenerator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.FilerException;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import static com.squareup.javapoet.JavaFile.builder;
import static java.util.Collections.singleton;
import static javax.lang.model.SourceVersion.latestSupported;

/**
 * Created by zyl06 on 2018/10/16.
 */
@AutoService(Processor.class)
public class AutoApiProcess extends AbstractProcessor {

    private String mPkgName;
    private String mApiProjectPath;

    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        Logger.sMessager = processingEnv.getMessager();

        Map<String, String> options = processingEnv.getOptions();
        if (options != null && !options.isEmpty()) {
            mApiProjectPath = options.get("apiProjectPath");
            mPkgName = options.get("packageName");

            String strLogOpen = options.get("logOpen");
            Logger.sLogOpen = Boolean.parseBoolean(strLogOpen);

            String strApiBuildEnable = options.get("apiBuildEnable");
            if (strApiBuildEnable != null) {
                FileUtil.sApiBuildEnable = Boolean.parseBoolean(strApiBuildEnable);
            }

            Logger.w("apiBuildEnable " + strApiBuildEnable);
        }

        mFiler = processingEnv.getFiler();
        ElementUtil.sElementUtil = processingEnv.getElementUtils();

        FileUtil.sFromPkgName = mPkgName;
        FileUtil.sToProjectPath = mApiProjectPath;
        FileUtil.deleteRecord();
        Logger.i("delete record.txt");
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return singleton(AutoApiClassAnno.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {

        Logger.i("api provider apt run begin");

        ElementUtil.ORIGIN_TO_API.clear();
        ElementUtil.ORIGIAN_TO_STUB.clear();
        ElementUtil.ORIGIN_TO_CALLBACK.clear();

        List<com.netease.libs.autoapi_process.BaseClassGenerator> classGenerators = new ArrayList<>();
        List<StubFactoryGenerator> stubFactoryGenerators = new ArrayList<>();

        // api 接口的统一父接口
        classGenerators.add(new ApiBaseGenerator());

        for (Element annoElement : roundEnv.getElementsAnnotatedWith(AutoApiClassAnno.class)) {
            TypeElement annoClass = (TypeElement) annoElement;
            //检测是否是支持的注解类型，如果不是里面会报错
            if (!isValidElement(annoClass, AutoApiClassAnno.class)) {
                continue;
            }
            //获取到信息，把注解类的信息加入到列表中
            AutoApiClassAnno anno = annoElement.getAnnotation(AutoApiClassAnno.class);

            AutoApiClass providerClass = new AutoApiClass();
            providerClass.clazz = annoClass;
            providerClass.name = anno.name();
            providerClass.allPublicStaticApi = anno.allPublicStaticApi();
            providerClass.allPublicNormalApi = anno.allPublicNormalApi();
            providerClass.includeSuperApi = anno.includeSuperApi();

            com.netease.libs.autoapi_process.generator.ApiGenerator apiGen = new com.netease.libs.autoapi_process.generator.ApiGenerator(providerClass, mPkgName);
            classGenerators.add(apiGen);
            ElementUtil.ORIGIN_TO_API.put(ClassName.get(annoClass), ElementUtil.getClassName(apiGen));

            if (ElementUtil.isInterface(annoClass)) {
                CallbackClassGenerator callbackGen = new CallbackClassGenerator(providerClass, apiGen, mPkgName);
                classGenerators.add(callbackGen);
                ElementUtil.ORIGIN_TO_CALLBACK.put(ClassName.get(annoClass), ElementUtil.getClassName(callbackGen));
            }

            StubClassGenerator stubGen = new StubClassGenerator(providerClass, apiGen, mPkgName);
            classGenerators.add(stubGen);
            ElementUtil.ORIGIAN_TO_STUB.put(ClassName.get(annoClass), ElementUtil.getClassName(stubGen));

            com.netease.libs.autoapi_process.generator.ApiFactoryGenerator apiFactoryGen = new com.netease.libs.autoapi_process.generator.ApiFactoryGenerator(providerClass, mPkgName, apiGen);
            classGenerators.add(apiFactoryGen);

            StubFactoryGenerator stubFactoryGen = new StubFactoryGenerator(providerClass, mPkgName, apiGen, stubGen, apiFactoryGen);
            classGenerators.add(stubFactoryGen);
            stubFactoryGenerators.add(stubFactoryGen);
        }

        ApiRegisterGenerator registerGenerator = new ApiRegisterGenerator(mPkgName, stubFactoryGenerators);
        classGenerators.add(registerGenerator);

        for (com.netease.libs.autoapi_process.BaseClassGenerator generator : classGenerators) {
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

                Logger.e(sb.toString());
            }
        }

        Logger.i("api provider apt run finish");

        return true;
    }

    private boolean isValidElement(Element elem, Class annoClass) {

        if (!ElementUtil.isPublic(elem)) {
            String message = String.format("Classes annotated with %s must be public.", "@" + annoClass.getSimpleName());
            Logger.e(message, elem);
            return false;
        }

        // public interface 直接 true
        Logger.w("isValidElement " + elem.toString());
        if (elem instanceof TypeElement && ElementUtil.isInterface((TypeElement) elem)) {
            Logger.w("isInterface " + elem.toString());
            return true;
        }

        if (ElementUtil.isAbstract(elem)) {
            String message = String.format("Classes annotated with %s must not be abstract.", "@" + annoClass.getSimpleName());
            Logger.e(message, elem);
            return false;
        }

        return true;
    }
}
