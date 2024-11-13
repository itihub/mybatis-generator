package org.example.plugins.xmlmapper;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.List;

public class LogicalDeleteByExampleElementGenerator extends AbstractXmlElementGenerator {

    private String statementId;

    private String logicalDeleteColumnName;

    private String deletedValue;

    private List<IntrospectedColumn> columns;

    public LogicalDeleteByExampleElementGenerator(String statementId, String logicalDeleteColumnName,
                                                  String deletedValue, List<IntrospectedColumn> columns) {
        this.statementId = statementId;
        this.logicalDeleteColumnName = logicalDeleteColumnName;
        this.deletedValue = deletedValue;
        this.columns = columns;
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = buildLogicalDeleteByExampleElement(statementId, columns);

        parentElement.addElement(answer);
    }

    private XmlElement buildLogicalDeleteByExampleElement(String statementId,
                                                          List<IntrospectedColumn> columns) {
        XmlElement answer = new XmlElement("update");

        answer.addAttribute(new Attribute("id", statementId));

        answer.addAttribute(new Attribute("parameterType", "map"));
        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("update ");
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        // set up for first column
//        sb.setLength(0);
//        sb.append("set ");
//
//        Iterator<IntrospectedColumn> iter = ListUtilities.removeGeneratedAlwaysColumns(columns).iterator();
//        while (iter.hasNext()) {
//            IntrospectedColumn introspectedColumn = iter.next();
//
//            sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
//            sb.append(" = "); //$NON-NLS-1$
//            if (introspectedColumn.getActualColumnName().equals(this.logicalDeleteColumnName)) {
//                sb.append(this.deletedValue);
//            } else {
//                sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, null)); //$NON-NLS-1$
//            }
//
//            if (iter.hasNext()) {
//                sb.append(',');
//            }
//
//            answer.addElement(new TextElement(sb.toString()));
//
//            // set up for the next column
//            if (iter.hasNext()) {
//                sb.setLength(0);
//                OutputUtilities.xmlIndent(sb, 1);
//            }
//        }

        XmlElement dynamicElement = new XmlElement("set"); //$NON-NLS-1$
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

        answer.addElement(getUpdateByExampleIncludeElement());
        return answer;
    }
}
