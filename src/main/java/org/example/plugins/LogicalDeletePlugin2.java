package org.example.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

import java.util.*;

/**
 * 自定义插件
 * 为mapper添加逻辑删除方法
 */
public class LogicalDeletePlugin2 extends PluginAdapter {

    private static final String BY_PRIMARY_KEY_METHOD_NAME_SUFFIX = "ByPrimaryKey";
    private static final String BY_EXAMPLE_METHOD_NAME_SUFFIX = "ByExample";


    private String logicalDeleteByPrimaryKeyStatementId;
    private String logicalDeleteByExampleStatementId;
    private List<IntrospectedColumn> primaryKeyColumns;
    private String column;
    private String deletedValue;
    private String notDeletedValue;

    @Override
    public boolean validate(List<String> warnings) {
        return super.properties.getProperty("enable").equals("true");
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        String methodNamePrefix = super.properties.getProperty("methodNamePrefix", "softDelete");
        this.logicalDeleteByPrimaryKeyStatementId = methodNamePrefix + "ByPrimaryKey";
        this.logicalDeleteByExampleStatementId = methodNamePrefix + "ByExample";
        this.primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();
        this.column = super.properties.getProperty("column", "deleted");
        this.deletedValue = super.properties.getProperty("deletedValue", "1");
        this.notDeletedValue = super.properties.getProperty("notDeletedValue", "0");
    }

    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        Set<FullyQualifiedJavaType> importedTypes = new HashSet<>();
        Method method = this.buildBasicLogicalDeleteByPrimaryKeyMethod(this.logicalDeleteByPrimaryKeyStatementId, importedTypes);
        interfaze.addMethod(method);
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        FullyQualifiedJavaType exampleType = new FullyQualifiedJavaType(introspectedTable.getExampleType());
        Method logicalDeleteByExampleMethod = this.buildBasicLogicalDeleteByExampleMethod(this.logicalDeleteByExampleStatementId, exampleType, importedTypes);
        interfaze.addMethod(logicalDeleteByExampleMethod);
        context.getCommentGenerator().addGeneralMethodComment(logicalDeleteByExampleMethod, introspectedTable);

        return true;
    }

    private Method buildBasicLogicalDeleteByPrimaryKeyMethod(String statementId,
                                                             Set<FullyQualifiedJavaType> importedTypes) {
        Method method = new Method(statementId);
        // 设置方法可见性
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(true);
        // 设置返回值类型 int类型
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        // 设置参数列表
        boolean annotate = this.primaryKeyColumns.size() > 1;
        if (annotate) {
            importedTypes.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param")); //$NON-NLS-1$
        }
        StringBuilder sb = new StringBuilder();
        for (IntrospectedColumn introspectedColumn : this.primaryKeyColumns) {
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
        return method;
    }

    private XmlElement buildBasicLogicalDeleteByExampleElement(IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("update");

        answer.addAttribute(new Attribute("id", this.logicalDeleteByExampleStatementId)); //$NON-NLS-1$

        answer.addAttribute(new Attribute("parameterType", "map")); //$NON-NLS-1$ //$NON-NLS-2$
        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("update "); //$NON-NLS-1$
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        // set up for first column
        sb.setLength(0);
        sb.append("set "); //$NON-NLS-1$

        Optional<IntrospectedColumn> introspectedColumn = introspectedTable.getAllColumns().stream().filter(item -> item.getActualColumnName().equals(this.column)).findFirst();
        sb.append(this.column);
        sb.append(" = ");
        sb.append(this.deletedValue);
        answer.addElement(new TextElement(sb.toString()));

        answer.addElement(getUpdateByExampleIncludeElement(introspectedTable));
        return answer;
    }

    private XmlElement getUpdateByExampleIncludeElement(IntrospectedTable introspectedTable) {
        XmlElement ifElement = new XmlElement("if"); //$NON-NLS-1$
        ifElement.addAttribute(new Attribute("test", "example != null")); //$NON-NLS-1$ //$NON-NLS-2$

        XmlElement includeElement = new XmlElement("include"); //$NON-NLS-1$
        includeElement.addAttribute(new Attribute("refid", //$NON-NLS-1$
                introspectedTable.getMyBatis3UpdateByExampleWhereClauseId()));
        ifElement.addElement(includeElement);

        return ifElement;
    }

    private XmlElement buildBasicLogicalDeleteByPrimaryKeyElement(IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("update"); //$NON-NLS-1$

        answer.addAttribute(new Attribute(
                "id", this.logicalDeleteByPrimaryKeyStatementId));

        String parameterType;

        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = introspectedTable.getRecordWithBLOBsType();
        } else {
            parameterType = introspectedTable.getBaseRecordType();
        }

        answer.addAttribute(new Attribute("parameterType", parameterType)); //$NON-NLS-1$

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();

        sb.append("update ");
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        answer.addElement(new TextElement(String.format("set deleted = %s", deletedValue)));

        buildPrimaryKeyWhereClause().forEach(answer::addElement);
        return answer;
    }

    protected List<TextElement> buildPrimaryKeyWhereClause() {
        List<TextElement> answer = new ArrayList<>();
        boolean first = true;
        for (IntrospectedColumn introspectedColumn : this.primaryKeyColumns) {
            String line;
            if (first) {
                line = "where "; //$NON-NLS-1$
                first = false;
            } else {
                line = "  and "; //$NON-NLS-1$
            }

            line += MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn);
            line += " = "; //$NON-NLS-1$
            line += MyBatis3FormattingUtilities.getParameterClause(introspectedColumn);
            answer.add(new TextElement(line));
        }

        return answer;
    }

    private Method buildBasicLogicalDeleteByExampleMethod(String statementId, FullyQualifiedJavaType exampleType,
                                                          Set<FullyQualifiedJavaType> importedTypes) {
        Method method = new Method(statementId);
        // 设置方法可见性
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(true);
        // 设置返回值类型 int类型
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        // 设置参数列表
        method.addParameter(new Parameter(exampleType,"example", "@Param(\"example\")"));
        // 引用
        importedTypes.add(exampleType);
        importedTypes.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"));
        return method;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
//        XmlElement logicDeleteByPKElement = new XmlElement("update");
//        logicDeleteByPKElement.addAttribute(new Attribute("id", this.logicalDeleteByPrimaryKeyStatementId));
//        logicDeleteByPKElement.addAttribute(new Attribute("parameterType", "java.lang.Long"));
//        logicDeleteByPKElement.addElement(new TextElement("update " + introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));
//        logicDeleteByPKElement.addElement(new TextElement(String.format("set deleted = %s where id = #{id,jdbcType=BIGINT}", deletedValue)));
//        document.getRootElement().addElement(logicDeleteByPKElement);

        XmlElement logicDeleteByPKElement = this.buildBasicLogicalDeleteByPrimaryKeyElement(introspectedTable);
        document.getRootElement().addElement(logicDeleteByPKElement);

        XmlElement logicDeleteWithUIndexElement = this.buildBasicLogicalDeleteByExampleElement(introspectedTable);
        document.getRootElement().addElement(logicDeleteWithUIndexElement);
//        XmlElement logicDeleteWithUIndexElement = new XmlElement("update");
//        logicDeleteWithUIndexElement.addAttribute(new Attribute("id", "logicDeleteWithUIndex"));
//        logicDeleteWithUIndexElement.addAttribute(new Attribute("parameterType", "map"));
//        logicDeleteWithUIndexElement.addElement(new TextElement("update " + introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime() + " A,"));
//        logicDeleteWithUIndexElement.addElement(new TextElement("(select C.deleted  from " + introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime() + " C,"));
//        logicDeleteWithUIndexElement.addElement(new TextElement("(select"));
//        XmlElement foreachElement = new XmlElement("foreach");
//        foreachElement.addAttribute(new Attribute("collection", "uIndexes"));
//        foreachElement.addAttribute(new Attribute("index", "index"));
//        foreachElement.addAttribute(new Attribute("item", "column"));
//        foreachElement.addAttribute(new Attribute("separator", ","));
//        foreachElement.addElement(new TextElement("${column}"));
//        logicDeleteWithUIndexElement.addElement(foreachElement);
//        logicDeleteWithUIndexElement.addElement(new TextElement("from " + introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime() + " where id = #{id,jdbcType=BIGINT}) D"));
//        XmlElement whereElement = new XmlElement("where");
//        XmlElement whereForachElement = new XmlElement("foreach");
//        whereForachElement.addAttribute(new Attribute("collection", "uIndexes"));
//        whereForachElement.addAttribute(new Attribute("index", "index"));
//        whereForachElement.addAttribute(new Attribute("item", "column"));
//        whereForachElement.addElement(new TextElement(" and C.${column} = D.${column}"));
//        whereElement.addElement(whereForachElement);
//        logicDeleteWithUIndexElement.addElement(whereElement);
//        logicDeleteWithUIndexElement.addElement(new TextElement("order by C.deleted desc limit 1) B"));
//        logicDeleteWithUIndexElement.addElement(new TextElement("set A.deleted = B.deleted + 1, A.update_time = REPLACE(unix_timestamp(current_timestamp(3)),'.','')"));
//        logicDeleteWithUIndexElement.addElement(new TextElement("where A.id = #{id,jdbcType=BIGINT}"));
//        logicDeleteWithUIndexElement.addElement(new TextElement("and A.deleted = 0"));
//        document.getRootElement().addElement(logicDeleteWithUIndexElement);
        return true;
    }
}
