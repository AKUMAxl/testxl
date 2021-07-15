package com.example.test_complier;

import com.example.test_annotation.LiveData;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.sun.tools.javac.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
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
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class LiveDataProcessor extends AbstractProcessor {

    // 操作元素的工具
    private Elements mElementsUtils;
    // 用来创建文件
    private Filer mFiler;
    // 用来输出日志 错误 警告信息
    private Messager mMessager;

    private Map<String,ClassEntity> mClassEntityMap = new HashMap<>();
    private Map<String,FieldEntity> mFieldEntityMap = new HashMap<>();
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.mElementsUtils = processingEnv.getElementUtils();
        this.mFiler = processingEnv.getFiler();
        this.mMessager = processingEnv.getMessager();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(LiveData.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        for (Element element:roundEnvironment.getElementsAnnotatedWith(LiveData.class)) {
            if (element.getKind()== ElementKind.FIELD){
                handleField((VariableElement) element);
            }
            if (element.getKind()==ElementKind.CLASS){
                handleClass((TypeElement) element);
            }

        }
        for (Map.Entry<String, ClassEntity> item : mClassEntityMap.entrySet()) {
            try {
                brewViewModel(item).writeTo(mFiler);
            } catch (Exception e) {
                mMessager.printMessage(Diagnostic.Kind.ERROR, e.getMessage(), item.getValue().getElement());
            }
        }
        return true;
    }

    private void handleClass(TypeElement element){
        ClassEntity classEntity = new ClassEntity(element);
        String className = element.getSimpleName().toString();
        if (mClassEntityMap.get(className)==null){
            mClassEntityMap.put(className,classEntity);
        }
    }

    private void handleField(VariableElement element){
        FieldEntity fieldEntity = new FieldEntity(element);
        String className = fieldEntity.getClassSimpleName();
        if (mClassEntityMap.get(className) == null) {
            mClassEntityMap.put(className,
                    new ClassEntity((TypeElement) element.getEnclosingElement()));
        }
        ClassEntity classEntity = mClassEntityMap.get(className);
        classEntity.addFieldEntity(fieldEntity);
    }

    private JavaFile brewViewModel(Map.Entry<String,ClassEntity> item){
        ClassEntity classEntity = item.getValue();
        LiveData liveData = classEntity.getElement().getAnnotation(LiveData.class);
        String className = classEntity.getElement().getSimpleName().toString()+"ViewMode";
        ClassName viewModelClazz = ClassName.get("android.lefecycle","ViewModel");
        TypeSpec.Builder builder = TypeSpec
                .classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .superclass(viewModelClazz);
        // 优先执行类LiveData注解
        if (liveData != null){
            TypeName valueTypeName = ClassName.get(classEntity.getElement());
            brewLiveData(classEntity.getClassSimpleName(), valueTypeName, builder);
        }else {
            Map<String, FieldEntity> fields = classEntity.getFields();

            for (FieldEntity fieldEntity : fields.values()){
                String fieldName = StringUtils.toUpperCase(fieldEntity.getElement().getSimpleName().toString());
                TypeName valueTypeName = ClassName.get(fieldEntity.getElement().asType());
                brewLiveData(fieldName, valueTypeName, builder);
            }
        }

        TypeSpec typeSpec = builder.build();
        // 指定包名
        return JavaFile.builder("com.zl.weilu.saber.viewmodel", typeSpec).build();
    }

    private void brewLiveData(String fieldName,TypeName valueTypeName,TypeSpec.Builder builder) {
        String liveDataType;
        ClassName liveDataTypeClassName;
        liveDataType = "m$L = new MutableLiveData<>()";
        liveDataTypeClassName = ClassName.get("android.lifecycle","MutableLiveData");
        ParameterizedTypeName typeName = ParameterizedTypeName.get(liveDataTypeClassName,valueTypeName);
        FieldSpec field = FieldSpec.builder(typeName,"m"+fieldName,Modifier.PRIVATE).build();
        MethodSpec getMethod = MethodSpec
                .methodBuilder("get"+fieldName)
                .addModifiers(Modifier.PUBLIC)
                .returns(field.type)
                .beginControlFlow("if (m$L == NULL)",fieldName)
                .addStatement(liveDataType,fieldName)
                .endControlFlow()
                .addStatement("return m$L",fieldName)
                .build();
        MethodSpec getValue = MethodSpec
                .methodBuilder("get" + fieldName + "Value")
                .addModifiers(Modifier.PUBLIC)
                .returns(valueTypeName)
                .addStatement("return this.$N().getValue()", getMethod)
                .build();

        MethodSpec setMethod = MethodSpec
                .methodBuilder("set" + fieldName)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(valueTypeName, "mValue")
                .beginControlFlow("if (this.m$L == null)", fieldName)
                .addStatement("return")
                .endControlFlow()
                .addStatement("this.m$L.setValue(mValue)", fieldName)
                .build();

        MethodSpec postMethod = MethodSpec
                .methodBuilder("post" + fieldName)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(valueTypeName, "mValue")
                .beginControlFlow("if (this.m$L == null)", fieldName)
                .addStatement("return")
                .endControlFlow()
                .addStatement("this.m$L.postValue(mValue)", fieldName)
                .build();

        builder.addField(field)
                .addMethod(getMethod)
                .addMethod(getValue)
                .addMethod(setMethod)
                .addMethod(postMethod);

    }
}
