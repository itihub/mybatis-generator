package org.example.plugins.javamapper;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class LogicalDeleteByPrimaryKeyMethodGenerator extends AbstractJavaMapperMethodGenerator {

    private final boolean isSimple;

    private String statementId;

    private List<IntrospectedColumn> primaryKeyColumns;

    private List<IntrospectedColumn> columns;

    public LogicalDeleteByPrimaryKeyMethodGenerator(boolean isSimple, String statementId,
                                                    List<IntrospectedColumn> primaryKeyColumns,
                                                    List<IntrospectedColumn> columns) {
        super();
        this.isSimple = isSimple;
        this.statementId = statementId;
        this.primaryKeyColumns = primaryKeyColumns;
        this.columns = columns;
    }

    @Override
    public void addInterfaceElements(Interface interfaze) {
        Method method = new Method(statementId);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(true);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());

        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();

        boolean annotate = !columns.isEmpty();
        addPrimaryKeyMethodParameters(false, annotate, method, importedTypes);
        if (annotate) {
            importedTypes.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param")); //$NON-NLS-1$
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

        addMapperAnnotations(method);

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        addExtraImports(interfaze);
        interfaze.addImportedTypes(importedTypes);
        interfaze.addMethod(method);
    }

    protected void addPrimaryKeyMethodParameters(boolean isSimple, boolean flag, Method method,
                                                 Set<FullyQualifiedJavaType> importedTypes) {
        if (!isSimple && introspectedTable.getRules().generatePrimaryKeyClass()) {
            FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType());
            importedTypes.add(type);
            if (flag) {
                importedTypes.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param")); //$NON-NLS-1$
                Parameter parameter = new Parameter(type, "key");
                parameter.addAnnotation("@Param(\"key\")");
                method.addParameter(parameter);
            } else {
                method.addParameter(new Parameter(type, "key"));
            }
        } else {
            // no primary key class - fields are in the base class
            // if more than one PK field, then we need to annotate the
            // parameters for MyBatis3
            List<IntrospectedColumn> introspectedColumns = introspectedTable
                    .getPrimaryKeyColumns();
            boolean annotate = introspectedColumns.size() > 1;
            annotate = annotate || flag;
            if (annotate) {
                importedTypes.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"));
            }
            StringBuilder sb = new StringBuilder();
            for (IntrospectedColumn introspectedColumn : introspectedColumns) {
                FullyQualifiedJavaType type = introspectedColumn.getFullyQualifiedJavaType();
                importedTypes.add(type);
                Parameter parameter = new Parameter(type, introspectedColumn.getJavaProperty());
                if (annotate) {
                    sb.setLength(0);
                    sb.append("@Param(\""); //$NON-NLS-1$
                    sb.append(introspectedColumn.getJavaProperty());
                    sb.append("\")"); //$NON-NLS-1$
                    parameter.addAnnotation(sb.toString());
                }
                method.addParameter(parameter);
            }
        }
    }

    public void addMapperAnnotations(Method method) {
        // extension point for subclasses
    }

    public void addExtraImports(Interface interfaze) {
        // extension point for subclasses
    }
}
