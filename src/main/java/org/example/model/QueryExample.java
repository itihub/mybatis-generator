package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QueryExample {

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public QueryExample() {
        oredCriteria = new ArrayList<>();
    }


    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        public void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        public void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        public void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }

        private String processAlias(String alias) {
            if (alias == null) {
                alias = "";
            } else {
                alias = alias.charAt(alias.length() - 1) == '.' ? alias : alias + ".";
            }
            return alias;
        }

        public Criteria isNull(String alias, String property) {
            criteria.add(new Criterion(processAlias(alias) + property + " is null"));
            return this;
        }

        public Criteria isNull(String property) {
            isNull(null, property);
            return this;
        }

        public Criteria isNotNull(String alias, String property) {
            criteria.add(new Criterion(processAlias(alias) + property + " is not null"));
            return this;
        }

        public Criteria isNotNull(String property) {
            isNotNull(null, property);
            return this;
        }

        public Criteria eq(String alias, String property, Object value) {
            criteria.add(new Criterion(processAlias(alias) + property  + " =", value));
            return this;
        }

        public Criteria eq(String property, Object value) {
            eq(null, property, value);
            return this;
        }

        public Criteria notEq(String alias, String property, Object value) {
            criteria.add(new Criterion(processAlias(alias) + property + " <>", value));
            return this;
        }

        public Criteria notEq(String property, Object value) {
            notEq(null, property, value);
            return this;
        }

        public Criteria gt(String alias, String property, Object value) {
            criteria.add(new Criterion(processAlias(alias) + property + " >", value));
            return this;
        }

        public Criteria gt(String property, Object value) {
            gt(null, property + " >", value);
            return this;
        }

        public Criteria gte(String alias, String property, Object value) {
            criteria.add(new Criterion(processAlias(alias) + property + " >=", value));
            return this;
        }

        public Criteria gte(String property, Object value) {
            gte(null, property + " >=", value);
            return this;
        }

        public Criteria lt(String alias, String property, Object value) {
            criteria.add(new Criterion(processAlias(alias) + property + " <", value));
            return this;
        }

        public Criteria lt(String property, Object value) {
            lt(null, property + " <", value);
            return this;
        }

        public Criteria lte(String alias, String property, Object value) {
            criteria.add(new Criterion(processAlias(alias) + property + " <=", value));
            return this;
        }

        public Criteria lte(String property, Object value) {
            lte(null, property + " <=", value);
            return this;
        }

        public Criteria in(String alias, String property, Object value) {
            criteria.add(new Criterion(processAlias(alias) + property + " in", value));
            return this;
        }

        public Criteria in(String property, Object value) {
            in(null, property + " in", value);
            return this;
        }

        public Criteria notIn(String alias, String property, Object value) {
            criteria.add(new Criterion(processAlias(alias) + property + " not in", value));
            return this;
        }

        public Criteria notIn(String property, Object value) {
            notIn(null, property + " not in", value);
            return this;
        }

        public Criteria between(String alias, String property, Object value1, Object value2) {
            criteria.add(new Criterion(processAlias(alias) + property + " between", value1, value2));
            return this;
        }

        public Criteria between(String property, Object value1, Object value2) {
            between(null, property + " between", value1, value2);
            return this;
        }

        public Criteria notBetween(String alias, String property, Object value1, Object value2) {
            criteria.add(new Criterion(processAlias(alias) + property + " not between", value1, value2));
            return this;
        }

        public Criteria notBetween(String property, Object value1, Object value2) {
            notBetween(null, property + " not between", value1, value2);
            return this;
        }

    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}