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

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SelectKeyMethodGenerator extends AbstractJavaMapperMethodGenerator {

    private static final String STATEMENT_ID = "selectKey";

    @Override
    public void addInterfaceElements(Interface interfaze) {

        List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();
        if (primaryKeyColumns.get(0).isSequenceColumn()) {
            FullyQualifiedJavaType javaType = primaryKeyColumns.get(0).getFullyQualifiedJavaType();
            Method method = new Method(STATEMENT_ID);
            method.setReturnType(javaType);
            method.setVisibility(JavaVisibility.PUBLIC);
            method.setAbstract(true);

            context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

            addMapperAnnotations(method);

            addExtraImports(interfaze);
            interfaze.addImportedTypes(Collections.singleton(javaType));
            interfaze.addMethod(method);
        }
    }

    public void addMapperAnnotations(Method method) {
        // extension point for subclasses
    }

    public void addExtraImports(Interface interfaze) {
        // extension point for subclasses
    }
}
