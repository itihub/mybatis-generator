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

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.VisitableElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.ArrayList;
import java.util.List;

public class BatchInsertElementGenerator extends AbstractXmlElementGenerator {

    private static final String STATEMENT_ID = "batchInsert";

    private final boolean isSimple;

    public BatchInsertElementGenerator(boolean isSimple) {
        super();
        this.isSimple = isSimple;
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("insert");

        answer.addAttribute(new Attribute("id", STATEMENT_ID));
        answer.addAttribute(new Attribute("parameterType", "list"));
        context.getCommentGenerator().addComment(answer);

        StringBuilder insertClause = new StringBuilder();

        insertClause.append("insert into ");
        insertClause.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        insertClause.append(" (");

        StringBuilder valuesClause = new StringBuilder();

        List<VisitableElement> valuesClauses = new ArrayList<>();
        valuesClauses.add(new TextElement("("));
        List<IntrospectedColumn> columns =
                ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns());
        for (int i = 0; i < columns.size(); i++) {
            IntrospectedColumn introspectedColumn = columns.get(i);

            insertClause.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            // 判断字段是否是序列，如果是序列则将序列生成规则写入
            if (introspectedColumn.isSequenceColumn()) {
                XmlElement valuesIsNullElement = new XmlElement("if");
                valuesIsNullElement.addAttribute(new Attribute("test", String.format("row.%s == null", introspectedColumn.getJavaProperty())));
                valuesIsNullElement.addElement(new TextElement(introspectedColumn.getDefaultValue() + ", "));
                valuesClauses.add(valuesIsNullElement);

                XmlElement valuesNotNullElement = new XmlElement("if");
                valuesNotNullElement.addAttribute(new Attribute("test", String.format("row.%s != null", introspectedColumn.getJavaProperty())));
                valuesNotNullElement.addElement(new TextElement(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "row.") + ", "));
                valuesClauses.add(valuesNotNullElement);
            } else {
                valuesClause.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "row."));
            }
            if (i + 1 < columns.size()) {
                insertClause.append(", ");
                if (!introspectedColumn.isSequenceColumn()) {
                    valuesClause.append(", ");
                }
            }

            // 换行处理
            if (valuesClause.length() > 80) {
                answer.addElement(new TextElement(insertClause.toString()));
                insertClause.setLength(0);
                OutputUtilities.xmlIndent(insertClause, 1);

                valuesClauses.add(new TextElement(valuesClause.toString()));
                valuesClause.setLength(0);
                OutputUtilities.xmlIndent(valuesClause, 1);
            }
        }

        insertClause.append(')');
        answer.addElement(new TextElement(insertClause.toString()));

        valuesClause.append(')');
        valuesClauses.add(new TextElement(valuesClause.toString()));

        XmlElement foreachElement = new XmlElement("foreach");
        foreachElement.addAttribute(new Attribute("close", ""));
        foreachElement.addAttribute(new Attribute("collection", "rows"));
        foreachElement.addAttribute(new Attribute("item", "row"));
        foreachElement.addAttribute(new Attribute("open", ""));
        foreachElement.addAttribute(new Attribute("separator", ","));
        for (VisitableElement element : valuesClauses) {
            foreachElement.addElement(element);
        }
        answer.addElement(new TextElement("values"));
        answer.addElement(foreachElement);

        parentElement.addElement(answer);
    }

}
