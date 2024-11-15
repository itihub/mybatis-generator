# Mybatis Generator 使用

MyBatis Generator 是一款强大的工具，可以根据数据库表结构自动生成 MyBatis 的 Mapper 接口、XML 映射文件和实体类，大大提高了开发效率。  
为了生成的代码更贴近实际开发工作，通过编写 Mybatis Generator 插件进行扩展定制。   
已扩展的场景  
- **通用 Example 类生成：** 生成一个通用的 Example 类，避免为每个表都生成一个 Example 类，减少代码冗余，避免类爆炸
- **Lombok 插件：** 使用 Lombok 注解来替代冗长的 getter 和 setter 方法，让代码更加简洁
- **逻辑删除功能支持：** 生成逻辑删除Mapper接口和对应XML实现  
- **数据库字段名常量化：** 方便管理提高代码的可读性、可维护性以及降低硬编码带来的风险
- **Upsert操作支持：** 生成 upsert 和 batchUpsert 方法，支持插入或更新操作
- **批量插入支持：** 生成批量插入方法，提高插入效率
- **序列值获取支持：** 为使用序列作为主键的表生成获取序列值的方法
- **Mapper 接口分离：** 将生成的 CRUD Mapper 接口和 XML 映射文件按配置的前缀单独生成，并让表对应的 Mapper 接口继承CRUD Mapper，将生成的代码和后续自定义 SQL 使用不同文件隔离，方便管理和扩展 

## 安装和使用说明

首次使用时需要将本工程进行 Maven 打包并安装到本地```mvn clean install```
因为需要使用自定义Plugin插件  

创建你的 MyBatis Generator 的配置文件，配置数据库连接、表信息、生成目标等，并将自定义插件。具体配置项可以参考[官方文档](https://mybatis.org/mybatis-3/zh_CN/configuration.html)  
本工程下```src/main/resources/generatorConfig.xml```是一个已经配置好样例配置，并且结合```src/main/resources/db.properties```外部配置文件，可以动态替换更方便的管理和配置  
```xml
<generatorConfiguration>
    <context id="default" targetRuntime="MyBatis3">
        <plugin type="org.example.plugins.LombokPlugin"/>
        <plugin type="org.example.plugins.ModelGeneratorColumnNamePlugin">
            <property name="enable" value="${plugin.lombok-plugin.enable}"/>
        </plugin>
        <plugin type="org.example.plugins.PrefixPlugin">
            <property name="enable" value="true|false"/>
            <property name="prefix" value="your_prefix"/>
        </plugin>
        <plugin type="org.example.plugins.LogicalDeletePlugin">
            <property name="enable" value="true|false"/>
            <property name="methodNamePrefix" value="your_method_name_prefix"/>
            <property name="logicalDeleteColumnName" value="your_logical_delete_column_name}"/>
            <property name="updatedColumnNames" value="your_logical_update_column_names"/>
            <property name="deletedValue" value="your_deleted_value"/>
            <property name="notDeletedValue" value="your_not_deleted_value"/>
        </plugin>
        <plugin type="org.example.plugins.UpsertPlugin">
            <property name="enable" value="true|false"/>
        </plugin>
        <plugin type="org.example.plugins.BatchInsertPlugin">
            <property name="enable" value="true|false"/>
        </plugin>
        <plugin type="org.example.plugins.SequencePlugin">
            <property name="enable" value="true|false"/>
        </plugin>
    </context>
</generatorConfiguration>
```
