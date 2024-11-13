package org.example.plugins.xmlmapper;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LogicalDeleteByPrimaryKeyElementGenerator extends AbstractXmlElementGenerator {

    private String statementId;

    private String logicalDeleteColumnName;

    private String deletedValue;

    private List<IntrospectedColumn> columns;

    public LogicalDeleteByPrimaryKeyElementGenerator(String statementId, String logicalDeleteColumnName,
                                                     String deletedValue, List<IntrospectedColumn> columns) {
        this.statementId = statementId;
        this.logicalDeleteColumnName = logicalDeleteColumnName;
        this.deletedValue = deletedValue;
        this.columns = columns;
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = buildLogicalDeleteByPrimaryKeyElement(statementId, columns);

        parentElement.addElement(answer);
    }

    private XmlElement buildLogicalDeleteByPrimaryKeyElement(String statementId,
                                                          List<IntrospectedColumn> columns) {
        XmlElement answer = new XmlElement("update");

        answer.addAttribute(new Attribute("id", statementId));

        String parameterType;

        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = introspectedTable.getRecordWithBLOBsType();
        } else {
            parameterType = introspectedTable.getBaseRecordType();
        }

        answer.addAttribute(new Attribute("parameterType", parameterType));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();

        sb.append("update ");
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement dynamicElement = new XmlElement("set");
        answer.addElement(dynamicElement);

        for (IntrospectedColumn introspectedColumn : ListUtilities.removeGeneratedAlwaysColumns(columns)) {
            if (introspectedColumn.getActualColumnName().equals(this.logicalDeleteColumnName)) {
                sb.setLength(0);
                sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
                sb.append(" = ");
                sb.append(this.deletedValue);
                sb.append(',');
                dynamicElement.addElement(new TextElement(sb.toString()));
            } else {
                sb.setLength(0);
                sb.append(introspectedColumn.getJavaProperty());
                sb.append(" != null");
                XmlElement isNotNullElement = new XmlElement("if");
                isNotNullElement.addAttribute(new Attribute("test", sb.toString()));
                dynamicElement.addElement(isNotNullElement);

                sb.setLength(0);
                sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
                sb.append(" = ");
                sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, null));
                sb.append(',');

                isNotNullElement.addElement(new TextElement(sb.toString()));
            }
        }

        boolean flag = columns.stream().anyMatch(item -> !item.getActualColumnName().equals(this.logicalDeleteColumnName));
        buildPrimaryKeyWhereClause(flag).forEach(answer::addElement);

        return answer;
    }

    protected List<TextElement> buildPrimaryKeyWhereClause(boolean flag) {
        List<TextElement> answer = new ArrayList<>();
        boolean first = true;
        for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
            String line;
            if (first) {
                line = "where "; //$NON-NLS-1$
                first = false;
            } else {
                line = "  and "; //$NON-NLS-1$
            }

            line += MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn);
            line += " = "; //$NON-NLS-1$
            line += MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, flag ? "key." : null);
            answer.add(new TextElement(line));
        }

        return answer;
    }
}
