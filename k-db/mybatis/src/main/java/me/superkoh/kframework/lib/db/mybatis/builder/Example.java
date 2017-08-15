package me.superkoh.kframework.lib.db.mybatis.builder;

import me.superkoh.kframework.core.lang.BaseObject;

import java.util.ArrayList;
import java.util.List;

public class Example {
    protected List<Criteria> oredCriteria;

    public Example() {
        oredCriteria = new ArrayList<>();
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = new Criteria();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        oredCriteria.clear();
        Criteria criteria = new Criteria();
        oredCriteria.add(criteria);
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
    }

    public static class Criteria extends BaseObject {

        protected List<Criterion> criteria;

        protected Criteria() {
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String col, String operator) {
            if (col == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(col, operator));
        }

        protected void addCriterion(String col, String operator, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(col, operator, value));
        }

        protected void addCriterion(String col, String operator, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(col, operator, value1, value2));
        }

        public Criteria andIsNull(String col) {
            addCriterion(col, "is null");
            return  this;
        }

        public Criteria andIsNotNull(String col) {
            addCriterion(col, "is not null");
            return  this;
        }

        public Criteria andEqualTo(String col, Object value) {
            addCriterion(col, "=", value, "id");
            return  this;
        }

        public Criteria andNotEqualTo(String col, Object value) {
            addCriterion(col, "<>", value, "id");
            return  this;
        }

        public Criteria andGreaterThan(String col, Object value) {
            addCriterion(col, ">", value, "id");
            return  this;
        }

        public Criteria andGreaterThanOrEqualTo(String col, Object value) {
            addCriterion(col, ">=", value, "id");
            return  this;
        }

        public Criteria andLessThan(String col, Object value) {
            addCriterion(col, "<", value, "id");
            return  this;
        }

        public Criteria andLessThanOrEqualTo(String col, Object value) {
            addCriterion(col, "<=", value, "id");
            return  this;
        }

        public Criteria andIn(String col, List<Object> values) {
            addCriterion(col, "in", values, "id");
            return  this;
        }

        public Criteria andNotIn(String col, List<Object> values) {
            addCriterion(col, "not in", values, "id");
            return  this;
        }

        public Criteria andBetween(String col, Object value1, Object value2) {
            addCriterion(col, "between", value1, value2, "id");
            return  this;
        }

        public Criteria andIdNotBetween(String col, Object value1, Object value2) {
            addCriterion(col, "not between", value1, value2, "id");
            return  this;
        }

        public Criteria andLike(String col, Object value) {
            addCriterion(col, "like", value, "parentIdentity");
            return  this;
        }

        public Criteria andNotLike(String col, Object value) {
            addCriterion(col, "not like", value, "parentIdentity");
            return  this;
        }
    }

    public static class Criterion extends BaseObject {
        private String column;
        private String operator;
        private String condition;
        private Object value;
        private Object secondValue;
        private boolean noValue;
        private boolean singleValue;
        private boolean betweenValue;
        private boolean listValue;

        public String getColumn() {
            return column;
        }

        public String getOperator() {
            return operator;
        }

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

        public Criterion(String column, String operator) {
            this.column = column;
            this.operator = operator;
            this.condition = column + " " + operator + " ";
            this.noValue = true;
        }

        public Criterion(String column, String operator, Object value) {
            this.column = column;
            this.operator = operator;
            this.condition = column + " " + operator + " ";
            this.value = value;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        public Criterion(String column, String operator, Object value, Object secondValue) {
            this.column = column;
            this.operator = operator;
            this.condition = column + " " + operator + " ";
            this.value = value;
            this.secondValue = secondValue;
            this.betweenValue = true;
        }
    }
}