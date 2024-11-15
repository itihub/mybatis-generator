# Mybatis Generator 扩展项目  

MyBatis Generator 是一款强大的工具，可以根据数据库表结构自动生成 MyBatis 的 Mapper 接口、XML 映射文件和实体类，大大提高了开发效率。  
为了进一步优化生成的代码，贴合实际开发需求，我开发了一套 MyBatis Generator 扩展插件，主要实现了以下功能：   
- **通用 Example 类生成：** 生成一个通用的 Example 类，避免为每个表都生成一个 Example 类，减少代码冗余，避免类爆炸
- **Lombok 插件：** 使用 Lombok 注解来替代冗长的 getter 和 setter 方法，让代码更加简洁
- **逻辑删除功能支持：** 生成逻辑删除Mapper接口和对应XML实现  
- **数据库字段名常量化：** 方便管理提高代码的可读性、可维护性以及降低硬编码带来的风险
- **Upsert操作支持：** 生成 upsert 和 batchUpsert 方法，支持插入或更新操作
- **批量插入支持：** 生成批量插入方法，提高插入效率
- **序列值获取支持：** 为使用序列作为主键的表生成获取序列值的方法
- **Mapper 接口分离：** 将生成的 CRUD Mapper 接口和 XML 映射文件按配置的前缀单独生成，并让表对应的 Mapper 接口继承CRUD Mapper，将生成的代码和后续自定义 SQL 使用不同文件隔离，方便管理和扩展  

**通过使用本扩展插件，您可以更轻松地生成高质量的 MyBatis 代码，提高开发效率。**    

## 安装和使用说明

1. **Maven安装：**  
首次使用时需要将本项目通过 Maven 打包并安装到本地 Maven 仓库，因为需要使用自定义Plugin插件  
```Bash
mvn clean install
```
2. **配置生成器：**  
创建一个 MyBatis Generator 的配置文件，配置数据库连接、表信息、生成目标等，并将自定义插件。    
本项目提供了```src/main/resources/generatorConfig.xml```作为示例（也可以直接修改使用），并结合```src/main/resources/db.properties```外部配置文件，可以动态替换更方便的管理和配置  
MyBatis Generator 的配置文件可以参考[官方文档](https://mybatis.org/mybatis-3/zh_CN/configuration.html)  

3. **执行生成**  
使用 Maven 命令或 IDE 插件执行生成任务。

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
