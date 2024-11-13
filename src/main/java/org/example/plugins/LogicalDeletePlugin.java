package org.example.plugins;

import org.example.plugins.javamapper.LogicalDeleteByExampleMethodGenerator;
import org.example.plugins.javamapper.LogicalDeleteByPrimaryKeyMethodGenerator;
import org.example.plugins.xmlmapper.LogicalDeleteByExampleWithBLOBsElementGenerator;
import org.example.plugins.xmlmapper.LogicalDeleteByPrimaryKeyElementGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 自定义插件
 * 为mapper添加逻辑删除方法
 */
public class LogicalDeletePlugin extends PluginAdapter {

    private String logicalDeleteByPrimaryKeyStatementId;
    private String logicalDeleteByExampleStatementId;
    private Set<String> updatedColumnNames;
    private String logicalDeleteColumnName;
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
        this.logicalDeleteColumnName = super.properties.getProperty("logicalDeleteColumnName", "deleted");
        this.updatedColumnNames = Arrays.stream(super.properties.getProperty("updatedColumnNames", "").split(",")).map(String::trim).collect(Collectors.toSet());
        this.deletedValue = super.properties.getProperty("deletedValue", "1");
        this.notDeletedValue = super.properties.getProperty("notDeletedValue", "0");
    }

    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        List<IntrospectedColumn> columns = introspectedTable.getAllColumns().stream()
                .filter(item -> this.updatedColumnNames.contains(item.getActualColumnName())).collect(Collectors.toList());

        AbstractJavaMapperMethodGenerator logicalDeleteByPrimaryKeyMethodGenerator
                = new LogicalDeleteByPrimaryKeyMethodGenerator(false, this.logicalDeleteByPrimaryKeyStatementId, introspectedTable.getPrimaryKeyColumns(), columns);
        initializeAndExecuteGenerator(logicalDeleteByPrimaryKeyMethodGenerator, introspectedTable, interfaze);

        AbstractJavaMapperMethodGenerator logicalDeleteByExampleMethodGenerator
                = new LogicalDeleteByExampleMethodGenerator(this.logicalDeleteByExampleStatementId, columns);
        initializeAndExecuteGenerator(logicalDeleteByExampleMethodGenerator, introspectedTable, interfaze);

        return true;
    }


    protected void initializeAndExecuteGenerator(AbstractJavaMapperMethodGenerator methodGenerator,
                                                 IntrospectedTable introspectedTable,
                                                 Interface interfaze) {
        methodGenerator.setContext(context);
        methodGenerator.setIntrospectedTable(introspectedTable);
        methodGenerator.addInterfaceElements(interfaze);
    }


    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        List<String> allUpdatedColumnNames = new ArrayList<>();
        allUpdatedColumnNames.add(this.logicalDeleteColumnName);
        allUpdatedColumnNames.addAll(this.updatedColumnNames);
        List<IntrospectedColumn> columns = introspectedTable.getAllColumns().stream()
                .filter(item -> allUpdatedColumnNames.contains(item.getActualColumnName())).collect(Collectors.toList());
        columns = columns.stream()
                .sorted(Comparator.comparingInt(o -> allUpdatedColumnNames.indexOf(o.getActualColumnName())))
                .collect(Collectors.toList());

        AbstractXmlElementGenerator logicalDeleteByPrimaryKeyElementGenerator
                = new LogicalDeleteByPrimaryKeyElementGenerator(this.logicalDeleteByPrimaryKeyStatementId,
                this.logicalDeleteColumnName, this.deletedValue, columns);
        initializeAndExecuteGenerator(logicalDeleteByPrimaryKeyElementGenerator, introspectedTable, document.getRootElement());

        AbstractXmlElementGenerator logicalDeleteByExampleWithBLOBsElementGenerator
                = new LogicalDeleteByExampleWithBLOBsElementGenerator(this.logicalDeleteByExampleStatementId,
                                                                        this.logicalDeleteColumnName, this.deletedValue, columns);
        initializeAndExecuteGenerator(logicalDeleteByExampleWithBLOBsElementGenerator, introspectedTable, document.getRootElement());
        return true;
    }

    protected void initializeAndExecuteGenerator(AbstractXmlElementGenerator elementGenerator,
                                                 IntrospectedTable introspectedTable,
                                                 XmlElement parentElement) {
        elementGenerator.setContext(context);
        elementGenerator.setIntrospectedTable(introspectedTable);
        elementGenerator.addElements(parentElement);
    }
}
