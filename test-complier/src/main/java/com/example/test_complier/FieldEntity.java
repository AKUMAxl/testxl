package com.example.test_complier;

import javax.lang.model.element.VariableElement;

public class FieldEntity {
    private VariableElement element;
    private String classSimpleName;

    public FieldEntity(VariableElement element){
        this.element = element;
        this.classSimpleName = element.getEnclosingElement().getSimpleName().toString();
    }

    public VariableElement getElement() {
        return element;
    }

    public String getClassSimpleName() {
        return classSimpleName;
    }
}
