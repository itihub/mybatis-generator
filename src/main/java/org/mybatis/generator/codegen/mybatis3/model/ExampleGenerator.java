package org.mybatis.generator.codegen.mybatis3.model;

import static org.mybatis.generator.internal.util.JavaBeansUtil.getGetterMethodName;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

public class ExampleGenerator extends AbstractJavaGenerator {

    public ExampleGenerator(String project) {
        super(project);
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        String exampleTargetPackage = context.getJavaModelGeneratorConfiguration().getTargetPackage();
        introspectedTable.setExampleType(exampleTargetPackage + ".QueryExample");
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(getString("Progress.6", table.toString())); //$NON-NLS-1$
        CommentGenerator commentGenerator = context.getCommentGenerator();

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getExampleType());
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(topLevelClass);

        // add default constructor
        Method method = new Method(type.getShortName());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setConstructor(true);
        method.addBodyLine("oredCriteria = new ArrayList<>();"); //$NON-NLS-1$

        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        // add field, getter, setter for orderby clause
        Field field = new Field("orderByClause", FullyQualifiedJavaType.getStringInstance()); //$NON-NLS-1$
        field.setVisibility(JavaVisibility.PROTECTED);
        commentGenerator.addFieldComment(field, introspectedTable);
        topLevelClass.addField(field);

        method = new Method("setOrderByClause"); //$NON-NLS-1$
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "orderByClause")); //$NON-NLS-1$
        method.addBodyLine("this.orderByClause = orderByClause;"); //$NON-NLS-1$
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        method = new Method("getOrderByClause"); //$NON-NLS-1$
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.addBodyLine("return orderByClause;"); //$NON-NLS-1$
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        // add field, getter, setter for distinct
        field = new Field("distinct", FullyQualifiedJavaType.getBooleanPrimitiveInstance()); //$NON-NLS-1$
        field.setVisibility(JavaVisibility.PROTECTED);
        commentGenerator.addFieldComment(field, introspectedTable);
        topLevelClass.addField(field);

        method = new Method("setDistinct"); //$NON-NLS-1$
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(
                new Parameter(FullyQualifiedJavaType.getBooleanPrimitiveInstance(), "distinct")); //$NON-NLS-1$
        method.addBodyLine("this.distinct = distinct;"); //$NON-NLS-1$
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        method = new Method("isDistinct"); //$NON-NLS-1$
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getBooleanPrimitiveInstance());
        method.addBodyLine("return distinct;"); //$NON-NLS-1$
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        // add field and methods for the list of ored criteria
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("java.util.List<Criteria>"); //$NON-NLS-1$
        field = new Field("oredCriteria", fqjt); //$NON-NLS-1$
        field.setVisibility(JavaVisibility.PROTECTED);

        commentGenerator.addFieldComment(field, introspectedTable);
        topLevelClass.addField(field);

        method = new Method("getOredCriteria"); //$NON-NLS-1$
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(fqjt);
        method.addBodyLine("return oredCriteria;"); //$NON-NLS-1$
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        method = new Method("or"); //$NON-NLS-1$
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(FullyQualifiedJavaType.getCriteriaInstance(), "criteria")); //$NON-NLS-1$
        method.addBodyLine("oredCriteria.add(criteria);"); //$NON-NLS-1$
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        method = new Method("or"); //$NON-NLS-1$
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getCriteriaInstance());
        method.addBodyLine("Criteria criteria = createCriteriaInternal();"); //$NON-NLS-1$
        method.addBodyLine("oredCriteria.add(criteria);"); //$NON-NLS-1$
        method.addBodyLine("return criteria;"); //$NON-NLS-1$
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        method = new Method("createCriteria"); //$NON-NLS-1$
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getCriteriaInstance());
        method.addBodyLine("Criteria criteria = createCriteriaInternal();"); //$NON-NLS-1$
        method.addBodyLine("if (oredCriteria.size() == 0) {"); //$NON-NLS-1$
        method.addBodyLine("oredCriteria.add(criteria);"); //$NON-NLS-1$
        method.addBodyLine("}"); //$NON-NLS-1$
        method.addBodyLine("return criteria;"); //$NON-NLS-1$
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        method = new Method("createCriteriaInternal"); //$NON-NLS-1$
        method.setVisibility(JavaVisibility.PROTECTED);
        method.setReturnType(FullyQualifiedJavaType.getCriteriaInstance());
        method.addBodyLine("Criteria criteria = new Criteria();"); //$NON-NLS-1$
        method.addBodyLine("return criteria;"); //$NON-NLS-1$
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        method = new Method("clear"); //$NON-NLS-1$
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addBodyLine("oredCriteria.clear();"); //$NON-NLS-1$
        method.addBodyLine("orderByClause = null;"); //$NON-NLS-1$
        method.addBodyLine("distinct = false;"); //$NON-NLS-1$
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        // now generate the inner class that holds the AND conditions
        topLevelClass.addInnerClass(getGeneratedCriteriaInnerClass(topLevelClass));

        topLevelClass.addInnerClass(getCriteriaInnerClass());

        topLevelClass.addInnerClass(getCriterionInnerClass());

        List<CompilationUnit> answer = new ArrayList<>();
        if (context.getPlugins().modelExampleClassGenerated(topLevelClass, introspectedTable)) {
            answer.add(topLevelClass);
        }
        return answer;
    }

    private InnerClass getCriterionInnerClass() {
        InnerClass answer = new InnerClass(new FullyQualifiedJavaType("Criterion")); //$NON-NLS-1$
        answer.setVisibility(JavaVisibility.PUBLIC);
        answer.setStatic(true);
        context.getCommentGenerator().addClassComment(answer, introspectedTable);

        Field field = new Field("condition", FullyQualifiedJavaType.getStringInstance()); //$NON-NLS-1$
        field.setVisibility(JavaVisibility.PRIVATE);
        answer.addField(field);
        answer.addMethod(getGetter(field));

        field = new Field("value", FullyQualifiedJavaType.getObjectInstance()); //$NON-NLS-1$
        field.setVisibility(JavaVisibility.PRIVATE);
        answer.addField(field);
        answer.addMethod(getGetter(field));

        field = new Field("secondValue", FullyQualifiedJavaType.getObjectInstance()); //$NON-NLS-1$
        field.setVisibility(JavaVisibility.PRIVATE);
        answer.addField(field);
        answer.addMethod(getGetter(field));

        field = new Field("noValue", FullyQualifiedJavaType.getBooleanPrimitiveInstance()); //$NON-NLS-1$
        field.setVisibility(JavaVisibility.PRIVATE);
        answer.addField(field);
        answer.addMethod(getGetter(field));

        field = new Field("singleValue", FullyQualifiedJavaType.getBooleanPrimitiveInstance()); //$NON-NLS-1$
        field.setVisibility(JavaVisibility.PRIVATE);
        answer.addField(field);
        answer.addMethod(getGetter(field));

        field = new Field("betweenValue", FullyQualifiedJavaType.getBooleanPrimitiveInstance()); //$NON-NLS-1$
        field.setVisibility(JavaVisibility.PRIVATE);
        answer.addField(field);
        answer.addMethod(getGetter(field));

        field = new Field("listValue", FullyQualifiedJavaType.getBooleanPrimitiveInstance()); //$NON-NLS-1$
        field.setVisibility(JavaVisibility.PRIVATE);
        answer.addField(field);
        answer.addMethod(getGetter(field));

        field = new Field("typeHandler", FullyQualifiedJavaType.getStringInstance()); //$NON-NLS-1$
        field.setVisibility(JavaVisibility.PRIVATE);
        answer.addField(field);
        answer.addMethod(getGetter(field));

        Method method = new Method("Criterion"); //$NON-NLS-1$
        method.setVisibility(JavaVisibility.PROTECTED);
        method.setConstructor(true);
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "condition")); //$NON-NLS-1$
        method.addBodyLine("super();"); //$NON-NLS-1$
        method.addBodyLine("this.condition = condition;"); //$NON-NLS-1$
        method.addBodyLine("this.typeHandler = null;"); //$NON-NLS-1$
        method.addBodyLine("this.noValue = true;"); //$NON-NLS-1$
        answer.addMethod(method);

        method = new Method("Criterion"); //$NON-NLS-1$
        method.setVisibility(JavaVisibility.PROTECTED);
        method.setConstructor(true);
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "condition")); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType.getObjectInstance(), "value")); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "typeHandler")); //$NON-NLS-1$
        method.addBodyLine("super();"); //$NON-NLS-1$
        method.addBodyLine("this.condition = condition;"); //$NON-NLS-1$
        method.addBodyLine("this.value = value;"); //$NON-NLS-1$
        method.addBodyLine("this.typeHandler = typeHandler;"); //$NON-NLS-1$
        method.addBodyLine("if (value instanceof List<?>) {"); //$NON-NLS-1$
        method.addBodyLine("this.listValue = true;"); //$NON-NLS-1$
        method.addBodyLine("} else {"); //$NON-NLS-1$
        method.addBodyLine("this.singleValue = true;"); //$NON-NLS-1$
        method.addBodyLine("}"); //$NON-NLS-1$
        answer.addMethod(method);

        method = createCriterionConstructor();
        method.addBodyLine("this(condition, value, null);"); //$NON-NLS-1$
        answer.addMethod(method);

        method = createCriterionConstructor();
        method.addParameter(new Parameter(FullyQualifiedJavaType.getObjectInstance(), "secondValue")); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "typeHandler")); //$NON-NLS-1$
        method.addBodyLine("super();"); //$NON-NLS-1$
        method.addBodyLine("this.condition = condition;"); //$NON-NLS-1$
        method.addBodyLine("this.value = value;"); //$NON-NLS-1$
        method.addBodyLine("this.secondValue = secondValue;"); //$NON-NLS-1$
        method.addBodyLine("this.typeHandler = typeHandler;"); //$NON-NLS-1$
        method.addBodyLine("this.betweenValue = true;"); //$NON-NLS-1$
        answer.addMethod(method);

        method = createCriterionConstructor();
        method.addParameter(new Parameter(FullyQualifiedJavaType.getObjectInstance(), "secondValue")); //$NON-NLS-1$
        method.addBodyLine("this(condition, value, secondValue, null);"); //$NON-NLS-1$
        answer.addMethod(method);

        return answer;
    }

    private Method createCriterionConstructor() {
        Method method = new Method("Criterion"); //$NON-NLS-1$
        method.setVisibility(JavaVisibility.PROTECTED);
        method.setConstructor(true);
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "condition")); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType.getObjectInstance(), "value")); //$NON-NLS-1$
        return method;
    }

    private InnerClass getCriteriaInnerClass() {
        InnerClass answer = new InnerClass(FullyQualifiedJavaType.getCriteriaInstance());

        answer.setVisibility(JavaVisibility.PUBLIC);
        answer.setStatic(true);
        answer.setSuperClass(FullyQualifiedJavaType.getGeneratedCriteriaInstance());

        context.getCommentGenerator().addClassComment(answer, introspectedTable, true);

        Method method = new Method("Criteria"); //$NON-NLS-1$
        method.setVisibility(JavaVisibility.PROTECTED);
        method.setConstructor(true);
        method.addBodyLine("super();"); //$NON-NLS-1$
        answer.addMethod(method);

        return answer;
    }

    private InnerClass getGeneratedCriteriaInnerClass(TopLevelClass topLevelClass) {
        Field field;

        InnerClass answer = new InnerClass(FullyQualifiedJavaType.getGeneratedCriteriaInstance());

        answer.setVisibility(JavaVisibility.PROTECTED);
        answer.setStatic(true);
        answer.setAbstract(true);
        context.getCommentGenerator().addClassComment(answer, introspectedTable);

        Method method = new Method("GeneratedCriteria"); //$NON-NLS-1$
        method.setVisibility(JavaVisibility.PROTECTED);
        method.setConstructor(true);
        method.addBodyLine("super();"); //$NON-NLS-1$
        method.addBodyLine("criteria = new ArrayList<>();"); //$NON-NLS-1$
        answer.addMethod(method);

        List<String> criteriaLists = new ArrayList<>();
        criteriaLists.add("criteria"); //$NON-NLS-1$

        for (IntrospectedColumn introspectedColumn : introspectedTable.getNonBLOBColumns()) {
            if (stringHasValue(introspectedColumn.getTypeHandler())) {
                String name = addTypeHandledObjectsAndMethods(introspectedColumn, method, answer);
                criteriaLists.add(name);
            }
        }

        // now generate the isValid method
        method = new Method("isValid"); //$NON-NLS-1$
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getBooleanPrimitiveInstance());
        StringBuilder sb = new StringBuilder();
        Iterator<String> strIter = criteriaLists.iterator();
        sb.append("return "); //$NON-NLS-1$
        sb.append(strIter.next());
        sb.append(".size() > 0"); //$NON-NLS-1$
        if (!strIter.hasNext()) {
            sb.append(';');
        }
        method.addBodyLine(sb.toString());
        while (strIter.hasNext()) {
            sb.setLength(0);
            OutputUtilities.javaIndent(sb, 1);
            sb.append("|| "); //$NON-NLS-1$
            sb.append(strIter.next());
            sb.append(".size() > 0"); //$NON-NLS-1$
            if (!strIter.hasNext()) {
                sb.append(';');
            }
            method.addBodyLine(sb.toString());
        }
        answer.addMethod(method);

        // now generate the getAllCriteria method
        if (criteriaLists.size() > 1) {
            field = new Field("allCriteria", //$NON-NLS-1$
                    new FullyQualifiedJavaType("List<Criterion>")); //$NON-NLS-1$
            field.setVisibility(JavaVisibility.PROTECTED);
            answer.addField(field);
        }

        method = new Method("getAllCriteria"); //$NON-NLS-1$
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("List<Criterion>")); //$NON-NLS-1$
        if (criteriaLists.size() < 2) {
            method.addBodyLine("return criteria;"); //$NON-NLS-1$
        } else {
            method.addBodyLine("if (allCriteria == null) {"); //$NON-NLS-1$
            method.addBodyLine("allCriteria = new ArrayList<>();"); //$NON-NLS-1$

            strIter = criteriaLists.iterator();
            while (strIter.hasNext()) {
                method.addBodyLine(String.format("allCriteria.addAll(%s);", strIter.next())); //$NON-NLS-1$
            }

            method.addBodyLine("}"); //$NON-NLS-1$
            method.addBodyLine("return allCriteria;"); //$NON-NLS-1$
        }
        answer.addMethod(method);

        // now we need to generate the methods that will be used in the SqlMap
        // to generate the dynamic where clause
        topLevelClass.addImportedType(FullyQualifiedJavaType.getNewListInstance());
        topLevelClass.addImportedType(FullyQualifiedJavaType.getNewArrayListInstance());

        FullyQualifiedJavaType listOfCriterion =
                new FullyQualifiedJavaType("java.util.List<Criterion>"); //$NON-NLS-1$
        field = new Field("criteria", listOfCriterion); //$NON-NLS-1$
        field.setVisibility(JavaVisibility.PROTECTED);
        answer.addField(field);

        method = new Method(getGetterMethodName(field.getName(), field.getType()));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(field.getType());
        method.addBodyLine("return criteria;"); //$NON-NLS-1$
        answer.addMethod(method);

        // now add the methods for simplifying the individual field set methods
        method = new Method("addCriterion"); //$NON-NLS-1$
        method.setVisibility(JavaVisibility.PROTECTED);
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "condition")); //$NON-NLS-1$
        method.addBodyLine("if (condition == null) {"); //$NON-NLS-1$
        method.addBodyLine("throw new RuntimeException(\"Value for condition cannot be null\");"); //$NON-NLS-1$
        method.addBodyLine("}"); //$NON-NLS-1$
        method.addBodyLine("criteria.add(new Criterion(condition));"); //$NON-NLS-1$
        if (criteriaLists.size() > 1) {
            method.addBodyLine("allCriteria = null;"); //$NON-NLS-1$
        }
        answer.addMethod(method);

        method = new Method("addCriterion"); //$NON-NLS-1$
        method.setVisibility(JavaVisibility.PROTECTED);
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "condition")); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType.getObjectInstance(), "value")); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "property")); //$NON-NLS-1$
        method.addBodyLine("if (value == null) {"); //$NON-NLS-1$
        method.addBodyLine(
                "throw new RuntimeException(\"Value for \" + property + \" cannot be null\");"); //$NON-NLS-1$
        method.addBodyLine("}"); //$NON-NLS-1$
        method.addBodyLine("criteria.add(new Criterion(condition, value));"); //$NON-NLS-1$
        if (criteriaLists.size() > 1) {
            method.addBodyLine("allCriteria = null;"); //$NON-NLS-1$
        }
        answer.addMethod(method);

        method = new Method("addCriterion"); //$NON-NLS-1$
        method.setVisibility(JavaVisibility.PROTECTED);
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "condition")); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType.getObjectInstance(), "value1")); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType.getObjectInstance(), "value2")); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "property")); //$NON-NLS-1$
        method.addBodyLine("if (value1 == null || value2 == null) {"); //$NON-NLS-1$
        method.addBodyLine(
                "throw new RuntimeException(\"Between values for \" + property + \" cannot be null\");"); //$NON-NLS-1$
        method.addBodyLine("}"); //$NON-NLS-1$
        method.addBodyLine("criteria.add(new Criterion(condition, value1, value2));"); //$NON-NLS-1$
        if (criteriaLists.size() > 1) {
            method.addBodyLine("allCriteria = null;"); //$NON-NLS-1$
        }
        answer.addMethod(method);

        FullyQualifiedJavaType listOfDates =
                new FullyQualifiedJavaType("java.util.List<java.util.Date>"); //$NON-NLS-1$

        if (introspectedTable.hasJDBCDateColumns()) {
            topLevelClass.addImportedType(FullyQualifiedJavaType.getDateInstance());
            topLevelClass.addImportedType(FullyQualifiedJavaType.getNewIteratorInstance());
            method = new Method("addCriterionForJDBCDate"); //$NON-NLS-1$
            method.setVisibility(JavaVisibility.PROTECTED);
            method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "condition")); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType.getDateInstance(), "value")); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "property")); //$NON-NLS-1$
            method.addBodyLine("if (value == null) {"); //$NON-NLS-1$
            method.addBodyLine(
                    "throw new RuntimeException(\"Value for \" + property + \" cannot be null\");"); //$NON-NLS-1$
            method.addBodyLine("}"); //$NON-NLS-1$
            method.addBodyLine(
                    "addCriterion(condition, new java.sql.Date(value.getTime()), property);"); //$NON-NLS-1$
            answer.addMethod(method);

            method = new Method("addCriterionForJDBCDate"); //$NON-NLS-1$
            method.setVisibility(JavaVisibility.PROTECTED);
            method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "condition")); //$NON-NLS-1$
            method.addParameter(new Parameter(listOfDates, "values")); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "property")); //$NON-NLS-1$
            method.addBodyLine("if (values == null || values.size() == 0) {"); //$NON-NLS-1$
            method.addBodyLine(
                    "throw new RuntimeException(\"Value list for \" + property + \"" //$NON-NLS-1$
                            + " cannot be null or empty\");"); //$NON-NLS-1$
            method.addBodyLine("}"); //$NON-NLS-1$
            method.addBodyLine("List<java.sql.Date> dateList = new ArrayList<>();"); //$NON-NLS-1$
            method.addBodyLine("Iterator<Date> iter = values.iterator();"); //$NON-NLS-1$
            method.addBodyLine("while (iter.hasNext()) {"); //$NON-NLS-1$
            method.addBodyLine("dateList.add(new java.sql.Date(iter.next().getTime()));"); //$NON-NLS-1$
            method.addBodyLine("}"); //$NON-NLS-1$
            method.addBodyLine("addCriterion(condition, dateList, property);"); //$NON-NLS-1$
            answer.addMethod(method);

            method = new Method("addCriterionForJDBCDate"); //$NON-NLS-1$
            method.setVisibility(JavaVisibility.PROTECTED);
            method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "condition")); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType.getDateInstance(), "value1")); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType.getDateInstance(), "value2")); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "property")); //$NON-NLS-1$
            method.addBodyLine("if (value1 == null || value2 == null) {"); //$NON-NLS-1$
            method.addBodyLine(
                    "throw new RuntimeException(\"Between values for \" + property + \"" //$NON-NLS-1$
                            + " cannot be null\");"); //$NON-NLS-1$
            method.addBodyLine("}"); //$NON-NLS-1$
            method.addBodyLine(
                    "addCriterion(condition, new java.sql.Date(value1.getTime())," //$NON-NLS-1$
                            + " new java.sql.Date(value2.getTime()), property);"); //$NON-NLS-1$
            answer.addMethod(method);
        }

        if (introspectedTable.hasJDBCTimeColumns()) {
            topLevelClass.addImportedType(FullyQualifiedJavaType.getDateInstance());
            topLevelClass.addImportedType(FullyQualifiedJavaType.getNewIteratorInstance());
            method = new Method("addCriterionForJDBCTime"); //$NON-NLS-1$
            method.setVisibility(JavaVisibility.PROTECTED);
            method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "condition")); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType.getDateInstance(), "value")); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "property")); //$NON-NLS-1$
            method.addBodyLine("if (value == null) {"); //$NON-NLS-1$
            method.addBodyLine(
                    "throw new RuntimeException(\"Value for \" + property + \" cannot be null\");"); //$NON-NLS-1$
            method.addBodyLine("}"); //$NON-NLS-1$
            method.addBodyLine(
                    "addCriterion(condition, new java.sql.Time(value.getTime()), property);"); //$NON-NLS-1$
            answer.addMethod(method);

            method = new Method("addCriterionForJDBCTime"); //$NON-NLS-1$
            method.setVisibility(JavaVisibility.PROTECTED);
            method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "condition")); //$NON-NLS-1$
            method.addParameter(new Parameter(listOfDates, "values")); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "property")); //$NON-NLS-1$
            method.addBodyLine("if (values == null || values.size() == 0) {"); //$NON-NLS-1$
            method.addBodyLine(
                    "throw new RuntimeException(\"Value list for \" + property + \"" //$NON-NLS-1$
                            + " cannot be null or empty\");"); //$NON-NLS-1$
            method.addBodyLine("}"); //$NON-NLS-1$
            method.addBodyLine("List<java.sql.Time> timeList = new ArrayList<>();"); //$NON-NLS-1$
            method.addBodyLine("Iterator<Date> iter = values.iterator();"); //$NON-NLS-1$
            method.addBodyLine("while (iter.hasNext()) {"); //$NON-NLS-1$
            method.addBodyLine("timeList.add(new java.sql.Time(iter.next().getTime()));"); //$NON-NLS-1$
            method.addBodyLine("}"); //$NON-NLS-1$
            method.addBodyLine("addCriterion(condition, timeList, property);"); //$NON-NLS-1$
            answer.addMethod(method);

            method = new Method("addCriterionForJDBCTime"); //$NON-NLS-1$
            method.setVisibility(JavaVisibility.PROTECTED);
            method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "condition")); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType.getDateInstance(), "value1")); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType.getDateInstance(), "value2")); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "property")); //$NON-NLS-1$
            method.addBodyLine("if (value1 == null || value2 == null) {"); //$NON-NLS-1$
            method.addBodyLine(
                    "throw new RuntimeException(\"Between values for \" + property + \"" //$NON-NLS-1$
                            + " cannot be null\");"); //$NON-NLS-1$
            method.addBodyLine("}"); //$NON-NLS-1$
            method.addBodyLine(
                    "addCriterion(condition, new java.sql.Time(value1.getTime())," //$NON-NLS-1$
                            + " new java.sql.Time(value2.getTime()), property);"); //$NON-NLS-1$
            answer.addMethod(method);
        }

        answer.addMethod(getNoValueMethod("isNull", "is null"));
        answer.addMethod(getNoValueMethod("isNotNull", "is not null"));
        answer.addMethod(getSingleValueMethod("eq", "="));
        answer.addMethod(getSingleValueMethod("notEq", "<>"));
        answer.addMethod(getSingleValueMethod("gt", ">"));
        answer.addMethod(getSingleValueMethod("gte", ">="));
        answer.addMethod(getSingleValueMethod("lt", "<"));
        answer.addMethod(getSingleValueMethod("lte", "<="));
        answer.addMethod(getSingleValueMethod("like", "like"));
        answer.addMethod(getSingleValueMethod("notLike", "not like"));
        answer.addMethod(getSetInOrNotInMethod(true));
        answer.addMethod(getSetInOrNotInMethod(false));
        answer.addMethod(getSetBetweenOrNotBetweenMethod(true));
        answer.addMethod(getSetBetweenOrNotBetweenMethod(false));

        return answer;
    }


    private Method getSingleValueMethod(String name, String operator) {

        StringBuilder sb = new StringBuilder();
        sb.append(name);

        Method method = new Method(sb.toString());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "property")); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType.getObjectInstance(), "value")); //$NON-NLS-1$
        method.setReturnType(FullyQualifiedJavaType.getCriteriaInstance());

        sb.setLength(0);
        sb.append("addCriterion(");
        sb.append("property");
        sb.append(' ');
        sb.append("+ \"");
        sb.append(' ');
        sb.append(operator);
        sb.append("\", "); //$NON-NLS-1$
        sb.append("value"); //$NON-NLS-1$
        sb.append(", "); //$NON-NLS-1$
        sb.append("property");
        sb.append(");"); //$NON-NLS-1$
        method.addBodyLine(sb.toString());
        method.addBodyLine("return (Criteria) this;"); //$NON-NLS-1$

        return method;
    }


    private String initializeAndMethodName(IntrospectedColumn introspectedColumn) {
        StringBuilder sb = new StringBuilder();
        sb.append(introspectedColumn.getJavaProperty());
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        sb.insert(0, "and"); //$NON-NLS-1$
        return sb.toString();
    }

    private String initializeAddLine(IntrospectedColumn introspectedColumn) {
        StringBuilder sb = new StringBuilder();
        if (introspectedColumn.isJDBCDateColumn()) {
            sb.append("addCriterionForJDBCDate(\""); //$NON-NLS-1$
        } else if (introspectedColumn.isJDBCTimeColumn()) {
            sb.append("addCriterionForJDBCTime(\""); //$NON-NLS-1$
        } else if (stringHasValue(introspectedColumn.getTypeHandler())) {
            sb.append("add"); //$NON-NLS-1$
            sb.append(introspectedColumn.getJavaProperty());
            sb.setCharAt(3, Character.toUpperCase(sb.charAt(3)));
            sb.append("Criterion(\""); //$NON-NLS-1$
        } else {
            sb.append("addCriterion(\""); //$NON-NLS-1$
        }

        sb.append(MyBatis3FormattingUtilities.getAliasedActualColumnName(introspectedColumn));
        return sb.toString();
    }

    private Method getSetBetweenOrNotBetweenMethod(boolean betweenMethod) {

        StringBuilder sb = new StringBuilder();
        if (betweenMethod) {
            sb.append("between"); //$NON-NLS-1$
        } else {
            sb.append("notBetween"); //$NON-NLS-1$
        }
        Method method = new Method(sb.toString());
        method.setVisibility(JavaVisibility.PUBLIC);
        FullyQualifiedJavaType type = FullyQualifiedJavaType.getObjectInstance();

        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "property")); //$NON-NLS-1$
        method.addParameter(new Parameter(type, "value1")); //$NON-NLS-1$
        method.addParameter(new Parameter(type, "value2")); //$NON-NLS-1$
        method.setReturnType(FullyQualifiedJavaType.getCriteriaInstance());

        sb.setLength(0);
        sb.append("addCriterion(");
        sb.append("property");
        sb.append(" ");
        sb.append("+ \"");
        if (betweenMethod) {
            sb.append(" between"); //$NON-NLS-1$
        } else {
            sb.append(" not between"); //$NON-NLS-1$
        }
        sb.append("\", "); //$NON-NLS-1$
        sb.append("value1, value2"); //$NON-NLS-1$
        sb.append(", "); //$NON-NLS-1$
        sb.append("property");
        sb.append(");"); //$NON-NLS-1$
        method.addBodyLine(sb.toString());
        method.addBodyLine("return (Criteria) this;"); //$NON-NLS-1$

        return method;
    }

    private Method getSetInOrNotInMethod(boolean inMethod) {
        StringBuilder sb = new StringBuilder();
        if (inMethod) {
            sb.append("in"); //$NON-NLS-1$
        } else {
            sb.append("notIn"); //$NON-NLS-1$
        }
        Method method = new Method(sb.toString());
        method.setVisibility(JavaVisibility.PUBLIC);
        FullyQualifiedJavaType type = FullyQualifiedJavaType.getNewListInstance();
        type.addTypeArgument(FullyQualifiedJavaType.getObjectInstance());

        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "property")); //$NON-NLS-1$
        method.addParameter(new Parameter(type, "values")); //$NON-NLS-1$
        method.setReturnType(FullyQualifiedJavaType.getCriteriaInstance());

        sb.setLength(0);
        sb.append("addCriterion(");
        sb.append("property");
        sb.append(" ");
        sb.append("+ \"");
        if (inMethod) {
            sb.append(" in"); //$NON-NLS-1$
        } else {
            sb.append(" not in"); //$NON-NLS-1$
        }
        sb.append("\", values, "); //$NON-NLS-1$
        sb.append("property");
        sb.append(");"); //$NON-NLS-1$
        method.addBodyLine(sb.toString());
        method.addBodyLine("return (Criteria) this;"); //$NON-NLS-1$

        return method;
    }

    private Method getNoValueMethod(String name, String operator) {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        Method method = new Method(sb.toString());
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "property"));

        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getCriteriaInstance());

        sb.setLength(0);
        sb.append("addCriterion("); //$NON-NLS-1$
        sb.append("property");
        sb.append(' ');
        sb.append("+ \"");
        sb.append(' ');
        sb.append(operator);
        sb.append("\");"); //$NON-NLS-1$
        method.addBodyLine(sb.toString());
        method.addBodyLine("return (Criteria) this;"); //$NON-NLS-1$

        return method;
    }

    /**
     * This method adds all the extra methods and fields required to support a
     * user defined type handler on some column.
     *
     * @param introspectedColumn the introspected column
     * @param constructor the constructor
     * @param innerClass the enclosing class
     * @return the name of the List added to the class by this method
     */
    private String addTypeHandledObjectsAndMethods(IntrospectedColumn introspectedColumn, Method constructor,
                                                   InnerClass innerClass) {
        StringBuilder sb = new StringBuilder();

        // add new private field and public accessor in the class
        sb.setLength(0);
        sb.append(introspectedColumn.getJavaProperty());
        sb.append("Criteria"); //$NON-NLS-1$
        String answer = sb.toString();

        Field field = new Field(answer, new FullyQualifiedJavaType("java.util.List<Criterion>")); //$NON-NLS-1$
        field.setVisibility(JavaVisibility.PROTECTED);
        innerClass.addField(field);

        Method method = new Method(getGetterMethodName(field.getName(), field.getType()));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(field.getType());
        sb.insert(0, "return "); //$NON-NLS-1$
        sb.append(';');
        method.addBodyLine(sb.toString());
        innerClass.addMethod(method);

        // add constructor initialization
        sb.setLength(0);
        sb.append(field.getName());
        sb.append(" = new ArrayList<>();"); //$NON-NLS-1$
        constructor.addBodyLine(sb.toString());

        // now add the methods for simplifying the individual field set methods
        sb.setLength(0);
        sb.append("add"); //$NON-NLS-1$
        sb.append(introspectedColumn.getJavaProperty());
        sb.setCharAt(3, Character.toUpperCase(sb.charAt(3)));
        sb.append("Criterion"); //$NON-NLS-1$
        method = new Method(sb.toString());
        method.setVisibility(JavaVisibility.PROTECTED);
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "condition")); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType.getObjectInstance(), "value")); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "property")); //$NON-NLS-1$
        method.addBodyLine("if (value == null) {"); //$NON-NLS-1$
        method.addBodyLine(
                "throw new RuntimeException(\"Value for \" + property + \" cannot be null\");"); //$NON-NLS-1$
        method.addBodyLine("}"); //$NON-NLS-1$

        method.addBodyLine(
                String.format("%s.add(new Criterion(condition, value, \"%s\"));", //$NON-NLS-1$
                        field.getName(), introspectedColumn.getTypeHandler()));
        method.addBodyLine("allCriteria = null;"); //$NON-NLS-1$
        innerClass.addMethod(method);

        sb.setLength(0);
        sb.append("add"); //$NON-NLS-1$
        sb.append(introspectedColumn.getJavaProperty());
        sb.setCharAt(3, Character.toUpperCase(sb.charAt(3)));
        sb.append("Criterion"); //$NON-NLS-1$

        method = new Method(sb.toString());
        method.setVisibility(JavaVisibility.PROTECTED);
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "condition")); //$NON-NLS-1$
        method.addParameter(new Parameter(introspectedColumn.getFullyQualifiedJavaType(), "value1")); //$NON-NLS-1$
        method.addParameter(new Parameter(introspectedColumn.getFullyQualifiedJavaType(), "value2")); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "property")); //$NON-NLS-1$
        if (!introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
            method.addBodyLine("if (value1 == null || value2 == null) {"); //$NON-NLS-1$
            method.addBodyLine(
                    "throw new RuntimeException(\"Between values for \" + property + \"" //$NON-NLS-1$
                            + " cannot be null\");"); //$NON-NLS-1$
            method.addBodyLine("}"); //$NON-NLS-1$
        }

        method.addBodyLine(
                String.format("%s.add(new Criterion(condition, value1, value2, \"%s\"));", //$NON-NLS-1$
                        field.getName(), introspectedColumn.getTypeHandler()));

        method.addBodyLine("allCriteria = null;"); //$NON-NLS-1$
        innerClass.addMethod(method);

        return answer;
    }
}
