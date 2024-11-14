/*
 *    Copyright 2006-2021 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.example.plugins.javamapper;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

import java.util.Set;
import java.util.TreeSet;

public class BatchInsertSelectiveMethodGenerator extends AbstractJavaMapperMethodGenerator {

    private static final String STATEMENT_ID = "batchInsertSelective";

    public BatchInsertSelectiveMethodGenerator() {
        super();
    }

    @Override
    public void addInterfaceElements(Interface interfaze) {
        Method method = new Method(STATEMENT_ID);

        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(true);

        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
        importedTypes.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"));

        FullyQualifiedJavaType columnNameListParameterType = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType stringType = FullyQualifiedJavaType.getStringInstance();
        columnNameListParameterType.addTypeArgument(stringType);
        importedTypes.add(stringType);
        method.addParameter(new Parameter(columnNameListParameterType, "columns", "@Param(\"columns\")"));

        FullyQualifiedJavaType parameterType = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType listType;
        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            listType = new FullyQualifiedJavaType(introspectedTable.getRecordWithBLOBsType());
        } else {
            // the blob fields must be rolled up into the base class
            listType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        }
        importedTypes.add(listType);
        parameterType.addTypeArgument(listType);

        method.addParameter(new Parameter(parameterType, "rows", "@Param(\"rows\")"));

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        addMapperAnnotations(method);

        addExtraImports(interfaze);
        interfaze.addImportedTypes(importedTypes);
        interfaze.addMethod(method);
    }

    public void addMapperAnnotations(Method method) {
        // extension point for subclasses
    }

    public void addExtraImports(Interface interfaze) {
        // extension point for subclasses
    }
}
