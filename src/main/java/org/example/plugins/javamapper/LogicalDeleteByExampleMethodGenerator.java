package org.example.plugins.javamapper;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class LogicalDeleteByExampleMethodGenerator extends AbstractJavaMapperMethodGenerator {

    private String statementId;

    private List<IntrospectedColumn> columns;

    public LogicalDeleteByExampleMethodGenerator(String statementId, List<IntrospectedColumn> columns) {
        super();
        this.statementId = statementId;
        this.columns = columns;
    }

    @Override
    public void addInterfaceElements(Interface interfaze) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
        FullyQualifiedJavaType exampleType = new FullyQualifiedJavaType(introspectedTable.getExampleType());
        importedTypes.add(exampleType);

        Method method = new Method(statementId);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(true);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());

        boolean annotate = !columns.isEmpty();
        if (annotate) {
            importedTypes.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param")); //$NON-NLS-1$
        } else {
            method.addParameter(new Parameter(exampleType, "example"));
        }
        StringBuilder sb = new StringBuilder();
        for (IntrospectedColumn column : columns) {
            FullyQualifiedJavaType type = column.getFullyQualifiedJavaType();
            importedTypes.add(type);
            Parameter parameter = new Parameter(type, column.getJavaProperty());
            if (annotate) {
                sb.setLength(0);
                sb.append("@Param(\"");
                sb.append(column.getJavaProperty());
                sb.append("\")");
                parameter.addAnnotation(sb.toString());
            }
            method.addParameter(parameter);
        }
        if (annotate) {
            Parameter parameter = new Parameter(exampleType, "example");
            sb.setLength(0);
            sb.append("@Param(\"");
            sb.append("example");
            sb.append("\")");
            parameter.addAnnotation(sb.toString());
            method.addParameter(parameter);
        }

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        addMapperAnnotations(method);

        addExtraImports(interfaze);
        interfaze.addImportedTypes(importedTypes);
        interfaze.addMethod(method);
    }

    public void addMapperAnnotations(Method method) {
        // extension point for subclasses
    }

    public void addExtraImports(Interface interfaze) {
        // extension point for subclasses
    }
}
