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
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.ArrayList;
import java.util.List;

public class BatchUpsertElementGenerator extends AbstractXmlElementGenerator {

    private static final String STATEMENT_ID = "batchUpsert";

    private final boolean isSimple;

    public BatchUpsertElementGenerator(boolean isSimple) {
        super();
        this.isSimple = isSimple;
    }

    @Override
    public void addElements(XmlElement parentElement) {
        FullyQualifiedJavaType parameterType;
        if (isSimple) {
            parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        } else {
            parameterType = introspectedTable.getRules().calculateAllFieldsClass();
        }

        XmlElement answer = new XmlElement("insert");

        answer.addAttribute(new Attribute("id", STATEMENT_ID));
        answer.addAttribute(new Attribute("parameterType", "list"));
        context.getCommentGenerator().addComment(answer);

        StringBuilder insertClause = new StringBuilder();

        //PostgreSQL JDBC Driver
        String driverClass = super.context.getProperty("driverClass");

        insertClause.append("insert into ");
        insertClause.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        insertClause.append(" (");

        StringBuilder valuesClause = new StringBuilder();
        valuesClause.append("(");

        // update
        StringBuilder updateClause = new StringBuilder();
        updateClause.append("do update set ");

        List<String> valuesClauses = new ArrayList<>();
        List<String> updateClauses = new ArrayList<>();
        List<IntrospectedColumn> columns =
                ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns());
        for (int i = 0; i < columns.size(); i++) {
            IntrospectedColumn introspectedColumn = columns.get(i);

            insertClause.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            valuesClause.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "row."));
            updateClause.append(this.getEscapedColumnName(introspectedColumn));
            if (i + 1 < columns.size()) {
                insertClause.append(", ");
                valuesClause.append(", ");
                updateClause.append(", ");
            }

            // 换行处理
            if (valuesClause.length() > 80) {
                answer.addElement(new TextElement(insertClause.toString()));
                insertClause.setLength(0);
                OutputUtilities.xmlIndent(insertClause, 1);

                valuesClauses.add(valuesClause.toString());
                valuesClause.setLength(0);
                OutputUtilities.xmlIndent(valuesClause, 1);

                updateClauses.add(updateClause.toString());
                updateClause.setLength(0);
                OutputUtilities.xmlIndent(updateClause, 1);
            }
        }

        insertClause.append(')');
        answer.addElement(new TextElement(insertClause.toString()));

        valuesClause.append(')');
        valuesClauses.add(valuesClause.toString());

        XmlElement foreachElement = new XmlElement("foreach");
        foreachElement.addAttribute(new Attribute("close", ""));
        foreachElement.addAttribute(new Attribute("collection", "rows"));
        foreachElement.addAttribute(new Attribute("item", "row"));
        foreachElement.addAttribute(new Attribute("open", ""));
        foreachElement.addAttribute(new Attribute("separator", ","));
        for (String clause : valuesClauses) {
            foreachElement.addElement(new TextElement(clause));
        }
        answer.addElement(new TextElement("values"));
        answer.addElement(foreachElement);

        StringBuilder conflictClause = new StringBuilder();
        conflictClause.append("on conflict (");

        List<String> conflictClauses = new ArrayList<>();
        List<IntrospectedColumn> introspectedColumns =
                ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getPrimaryKeyColumns());
        for (int i = 0; i < introspectedColumns.size(); i++) {
            IntrospectedColumn introspectedColumn = introspectedColumns.get(i);

            conflictClause.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            if (i + 1 < introspectedColumns.size()) {
                conflictClause.append(", ");
            }

            if (conflictClause.length() > 80) {
                conflictClauses.add(conflictClause.toString());
                valuesClause.setLength(0);
                OutputUtilities.xmlIndent(conflictClause, 1);
            }

        }
        conflictClause.append(')');
        conflictClauses.add(conflictClause.toString());
        for (String clause : conflictClauses) {
            answer.addElement(new TextElement(clause));
        }

        for (String clause : updateClauses) {
            answer.addElement(new TextElement(clause));
        }

        parentElement.addElement(answer);
    }

    private static String getEscapedColumnName(IntrospectedColumn introspectedColumn) {
        StringBuilder sb = new StringBuilder();
        sb.append(introspectedColumn.getActualColumnName()).append(" = excluded.").append(introspectedColumn.getActualColumnName());

        if (introspectedColumn.isColumnNameDelimited()) {
            sb.insert(0, introspectedColumn.getContext().getBeginningDelimiter());
            sb.append(introspectedColumn.getContext().getEndingDelimiter());
        }

        return sb.toString();
    }

    private String buildUpsert() {
        StringBuilder insertClause = new StringBuilder();

        insertClause.append("insert into ");
        insertClause.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        insertClause.append(" (");

        StringBuilder valuesClause = new StringBuilder();
        valuesClause.append("values (");

        // update
        StringBuilder updateClause = new StringBuilder();
        updateClause.append("do update set ");

        List<String> insertClauses = new ArrayList<>();
        List<String> valuesClauses = new ArrayList<>();
        List<String> updateClauses = new ArrayList<>();
        List<IntrospectedColumn> columns =
                ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns());
        for (int i = 0; i < columns.size(); i++) {
            IntrospectedColumn introspectedColumn = columns.get(i);

            insertClause.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            valuesClause.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            updateClause.append(this.getEscapedColumnName(introspectedColumn));
            if (i + 1 < columns.size()) {
                insertClause.append(", ");
                valuesClause.append(", ");
                updateClause.append(", ");
            }

            // 换行处理
            if (valuesClause.length() > 80) {
                insertClauses.add(insertClause.toString());
                insertClause.setLength(0);
                OutputUtilities.xmlIndent(insertClause, 1);

                valuesClauses.add(valuesClause.toString());
                valuesClause.setLength(0);
                OutputUtilities.xmlIndent(valuesClause, 1);

                updateClauses.add(updateClause.toString());
                updateClause.setLength(0);
                OutputUtilities.xmlIndent(updateClause, 1);
            }
        }

        insertClause.append(')');
        insertClauses.add(insertClause.toString());

        valuesClause.append(')');
        valuesClauses.add(valuesClause.toString());

        StringBuilder allClause = new StringBuilder();
        allClause.append(insertClauses);
        allClause.append(valuesClauses);

        StringBuilder conflictClause = new StringBuilder();
        conflictClause.append("on conflict (");

        List<String> conflictClauses = new ArrayList<>();
        List<IntrospectedColumn> introspectedColumns =
                ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getPrimaryKeyColumns());
        for (int i = 0; i < introspectedColumns.size(); i++) {
            IntrospectedColumn introspectedColumn = introspectedColumns.get(i);

            conflictClause.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            if (i + 1 < introspectedColumns.size()) {
                conflictClause.append(", ");
            }
            if (conflictClause.length() > 80) {
                conflictClauses.add(conflictClause.toString());
                valuesClause.setLength(0);
                OutputUtilities.xmlIndent(conflictClause, 1);
            }

        }
        conflictClause.append(')');
        conflictClauses.add(conflictClause.toString());

        allClause.append(conflictClause);
        allClause.append(updateClauses);
        allClause.append(conflictClauses);

        return allClause.toString();
    }
}
