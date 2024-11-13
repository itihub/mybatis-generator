package org.example.plugins;

import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.XmlConstants;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;

import java.util.Collections;
import java.util.List;

/**
 * 自定义插件
 * 生成mapper类和xml文件添加固定前缀
 * 并且同时生成没有固定前缀的mapper和xml用于扩展操作,mapper会继承带有固定前缀的mapper
 * 对mapper的自定义扩展应该维护到没有前缀的名对应的mapper和xml文件,
 * 避免将后续扩展和mybatis generator默认生成的方法耦合到一起
 */
public class PrefixPlugin extends PluginAdapter {

    private String prefix;

    private String javaFileEncoding;

    private String daoTargetProject;

    private String daoTargetPackage;

    private String originalJavaMapperType;

    private String javaMapperType;

    private String mapperTargetProject;

    private String mapperTargetPackage;

    private String originalXmlMapperFileName;

    private String xmlMapperFileName;

    private String originalMapNamespace;

    private String mapNamespace;

    @Override
    public boolean validate(List<String> warnings) {
        return super.properties.getProperty("enable").equals("true");
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        this.prefix = properties.getProperty("prefix", "Base");
        this.javaFileEncoding = super.context.getProperty("javaFileEncoding");
        this.daoTargetPackage = super.context.getJavaClientGeneratorConfiguration().getTargetPackage();
        this.daoTargetProject = super.context.getJavaClientGeneratorConfiguration().getTargetProject();
        this.mapperTargetPackage = super.context.getSqlMapGeneratorConfiguration().getTargetPackage();
        this.mapperTargetProject = super.context.getSqlMapGeneratorConfiguration().getTargetProject();

        // Mapper Client添加前缀
//        String tableObjectName = introspectedTable.getMyBatisDynamicSQLTableObjectName();
        String javaMapperType = introspectedTable.getMyBatis3JavaMapperType();
        this.originalJavaMapperType = javaMapperType;
        StringBuilder javaMapperTypeSb = new StringBuilder(javaMapperType);
        javaMapperTypeSb.insert(this.daoTargetPackage.length() + 1, this.prefix);
        this.javaMapperType = javaMapperTypeSb.toString();
        introspectedTable.setMyBatis3JavaMapperType(this.javaMapperType);

        // Mapper.xml文件增加前缀
        this.originalXmlMapperFileName = introspectedTable.getMyBatis3XmlMapperFileName();
        this.xmlMapperFileName = this.prefix + this.originalXmlMapperFileName;
        introspectedTable.setMyBatis3XmlMapperFileName(xmlMapperFileName);
        // Mapper.xml文件中的Namespace增加前缀
        this.originalMapNamespace = introspectedTable.getMyBatis3FallbackSqlMapNamespace();
        StringBuilder mapNamespaceSb = new StringBuilder(this.originalMapNamespace);
        mapNamespaceSb.insert(this.mapperTargetPackage.length() + 1, this.prefix);
        introspectedTable.setMyBatis3FallbackSqlMapNamespace(mapNamespaceSb.toString());
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {

        Interface interfaze = new Interface(this.originalJavaMapperType);
        interfaze.addSuperInterface(new FullyQualifiedJavaType(this.javaMapperType));
        interfaze.setVisibility(JavaVisibility.PUBLIC);

        interfaze.addImportedType(new FullyQualifiedJavaType(this.javaMapperType));
//        interfaze.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Repository"));
//        interfaze.addAnnotation("@Repository");
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper"));
        interfaze.addAnnotation("@Mapper");

        GeneratedJavaFile javaFile = new GeneratedJavaFile(interfaze, this.daoTargetProject, this.javaFileEncoding, super.context.getJavaFormatter());
        return Collections.singletonList(javaFile);
    }

    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles() {
        Document document = new Document(XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID,
                XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);
        XmlElement root = new XmlElement("mapper"); //$NON-NLS-1$
        document.setRootElement(root);
        root.addAttribute(new Attribute("namespace", this.originalJavaMapperType));
        root.addElement(new TextElement("<!--")); //$NON-NLS-1$
        root.addElement(new TextElement("  ")); //$NON-NLS-1$
        root.addElement(new TextElement(
                "  这是一个继承自父xxxMapper.xml的配置文件，扩展的sql语句操作都放在这个文件")); //$NON-NLS-1$
        root.addElement(new TextElement("-->"));
        GeneratedXmlFile xmlFile = new GeneratedXmlFile(document, this.originalXmlMapperFileName, this.mapperTargetPackage, this.mapperTargetProject, false, super.context.getXmlFormatter());
        return Collections.singletonList(xmlFile);
    }

    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {

        if (introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3) {
            FullyQualifiedJavaType type = interfaze.getType();
            String shortName = type.getShortName();
            String fullyQualifiedName = type.getFullyQualifiedName();
            String originalInterfaceName = interfaze.getType().getShortName();
            String newInterfaceName = prefix + originalInterfaceName;
        }

        return true;
    }

    @Override
    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
//        sqlMap.setNamespace(prefix + "." + sqlMap.getNamespace());
        return super.sqlMapGenerated(sqlMap, introspectedTable);
    }

}
