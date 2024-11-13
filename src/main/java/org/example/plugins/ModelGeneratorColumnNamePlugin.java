package org.example.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.Context;

import java.util.List;
import java.util.Properties;

/**
 * 自定义插件
 * 为Model添加字段名常量
 */
public class ModelGeneratorColumnNamePlugin implements Plugin {

    protected Context context;
    protected final Properties properties = new Properties();

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties.putAll(properties);
    }

    @Override
    public boolean validate(List<String> warnings) {
        return properties.getProperty("enable").equals("true");
    }


    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        // 生成字段名常量
        String constantName = "COLUMN_" + introspectedColumn.getActualColumnName().toUpperCase();
        field = new Field(constantName, FullyQualifiedJavaType.getStringInstance());
        field.setVisibility(JavaVisibility.PUBLIC);
        field.setStatic(true);
        field.setFinal(true);
        field.setInitializationString("\"" + introspectedColumn.getActualColumnName() + "\"");
        topLevelClass.addField(field);
        return true;
    }

}
