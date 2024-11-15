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
package org.example.plugins.xmlmapper;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

public class SelectKeyElementGenerator extends AbstractXmlElementGenerator {

    private static final String STATEMENT_ID = "selectKey";

    @Override
    public void addElements(XmlElement parentElement) {
        if (introspectedTable.getPrimaryKeyColumns().get(0).isSequenceColumn()) {
            FullyQualifiedJavaType javaType = introspectedTable.getPrimaryKeyColumns().get(0).getFullyQualifiedJavaType();

            XmlElement answer = new XmlElement("select");
            answer.addAttribute(new Attribute("id", STATEMENT_ID));
            answer.addAttribute(new Attribute("resultType", javaType.toString()));
            context.getCommentGenerator().addComment(answer);
            answer.addElement(new TextElement("select " + introspectedTable.getPrimaryKeyColumns().get(0).getDefaultValue()));
            parentElement.addElement(answer);
        }
    }

}
