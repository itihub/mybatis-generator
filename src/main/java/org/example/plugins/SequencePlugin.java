package org.example.plugins;

import org.example.plugins.javamapper.SelectKeyMethodGenerator;
import org.example.plugins.xmlmapper.SelectKeyElementGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.List;

/**
 * 序列主键生成selectKey方法
 */
public class SequencePlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return super.properties.getProperty("enable", "true").equals("true");
    }

    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        AbstractJavaMapperMethodGenerator selectKeyMethodGenerator = new SelectKeyMethodGenerator();
        initializeAndExecuteGenerator(selectKeyMethodGenerator, introspectedTable, interfaze);
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

        AbstractXmlElementGenerator selectKeyElementGenerator
                = new SelectKeyElementGenerator();
        initializeAndExecuteGenerator(selectKeyElementGenerator, introspectedTable, document.getRootElement());
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
