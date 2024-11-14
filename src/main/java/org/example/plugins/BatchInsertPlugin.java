package org.example.plugins;

import org.example.plugins.javamapper.*;
import org.example.plugins.xmlmapper.*;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.List;

/**
 * 自定义插件
 * 为mapper添加批量插入相关方法
 */
public class BatchInsertPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return super.properties.getProperty("enable").equals("true");
    }

    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {

        AbstractJavaMapperMethodGenerator batchInsertMethodGenerator
                = new BatchInsertMethodGenerator(false);
        initializeAndExecuteGenerator(batchInsertMethodGenerator, introspectedTable, interfaze);

        AbstractJavaMapperMethodGenerator batchInsertSelectiveMethodGenerator
                = new BatchInsertSelectiveMethodGenerator();
        initializeAndExecuteGenerator(batchInsertSelectiveMethodGenerator, introspectedTable, interfaze);

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

        AbstractXmlElementGenerator batchInsertElementGenerator
                = new BatchInsertElementGenerator(false);
        initializeAndExecuteGenerator(batchInsertElementGenerator, introspectedTable, document.getRootElement());

        AbstractXmlElementGenerator batchInsertSelectiveElementGenerator
                = new BatchInsertSelectiveElementGenerator();
        initializeAndExecuteGenerator(batchInsertSelectiveElementGenerator, introspectedTable, document.getRootElement());

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
