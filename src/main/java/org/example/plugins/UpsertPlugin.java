package org.example.plugins;

import org.example.plugins.javamapper.BatchUpsertMethodGenerator;
import org.example.plugins.javamapper.BatchUpsertSelectiveMethodGenerator;
import org.example.plugins.javamapper.UpsertMethodGenerator;
import org.example.plugins.javamapper.UpsertSelectiveMethodGenerator;
import org.example.plugins.xmlmapper.*;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义插件
 * 为mapper添加upsert相关方法
 */
public class UpsertPlugin extends PluginAdapter {

    private String mysql_upsert_Pattern = "INSERT INTO your_table (column1, column2, ...)\n" +
            "VALUES (value1, value2, ...)\n" +
            "ON DUPLICATE KEY UPDATE\n" +
            "    column1 = VALUES(column1),\n" +
            "    column2 = VALUES(column2)";

    private String mysql8_upsert_Pattern = "MERGE INTO your_table AS t\n" +
            "USING (VALUES (value1, value2, ...)) AS s (column1, column2)\n" +
            "ON t.primary_key = s.primary_key\n" +
            "WHEN MATCHED THEN\n" +
            "  UPDATE SET t.column1 = s.column1, t.column2 = s.column2\n" +
            "WHEN NOT MATCHED THEN\n" +
            "  INSERT VALUES (s.column1, s.column2)";

    private String postgre_sql_upsert_pattern = "INSERT INTO table_name (column1, column2, ...)\n" +
            "VALUES (value1, value2, ...)\n" +
            "ON CONFLICT (constraint_name)\n" +
            "DO NOTHING; -- 或 DO UPDATE SET column1 = EXCLUDED.column1";

    @Override
    public boolean validate(List<String> warnings) {
        return super.properties.getProperty("enable").equals("true");
    }

    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {

        AbstractJavaMapperMethodGenerator upsertMethodGenerator
                = new UpsertMethodGenerator(false);
        initializeAndExecuteGenerator(upsertMethodGenerator, introspectedTable, interfaze);

        AbstractJavaMapperMethodGenerator upsertSelectiveMethodGenerator
                = new UpsertSelectiveMethodGenerator();
        initializeAndExecuteGenerator(upsertSelectiveMethodGenerator, introspectedTable, interfaze);

        AbstractJavaMapperMethodGenerator batchUpsertMethodGenerator
                = new BatchUpsertMethodGenerator(false);
        initializeAndExecuteGenerator(batchUpsertMethodGenerator, introspectedTable, interfaze);

        AbstractJavaMapperMethodGenerator batchUpsertSelectiveMethodGenerator
                = new BatchUpsertSelectiveMethodGenerator();
        initializeAndExecuteGenerator(batchUpsertSelectiveMethodGenerator, introspectedTable, interfaze);

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

        AbstractXmlElementGenerator upsertElementGenerator
                = new UpsertElementGenerator(false);
        initializeAndExecuteGenerator(upsertElementGenerator, introspectedTable, document.getRootElement());

        AbstractXmlElementGenerator upsertSelectiveElementGenerator
                = new UpsertSelectiveElementGenerator();
        initializeAndExecuteGenerator(upsertSelectiveElementGenerator, introspectedTable, document.getRootElement());

        AbstractXmlElementGenerator batchUpsertElementGenerator
                = new BatchUpsertElementGenerator(false);
        initializeAndExecuteGenerator(batchUpsertElementGenerator, introspectedTable, document.getRootElement());

        AbstractXmlElementGenerator batchUpsertSelectiveElementGenerator
                = new BatchUpsertSelectiveElementGenerator();
        initializeAndExecuteGenerator(batchUpsertSelectiveElementGenerator, introspectedTable, document.getRootElement());

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
