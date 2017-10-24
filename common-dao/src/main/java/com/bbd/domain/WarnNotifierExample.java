package com.bbd.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WarnNotifierExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public WarnNotifierExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
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
            criteria = new ArrayList<Criterion>();
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

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andSettingIdIsNull() {
            addCriterion("setting_id is null");
            return (Criteria) this;
        }

        public Criteria andSettingIdIsNotNull() {
            addCriterion("setting_id is not null");
            return (Criteria) this;
        }

        public Criteria andSettingIdEqualTo(Long value) {
            addCriterion("setting_id =", value, "settingId");
            return (Criteria) this;
        }

        public Criteria andSettingIdNotEqualTo(Long value) {
            addCriterion("setting_id <>", value, "settingId");
            return (Criteria) this;
        }

        public Criteria andSettingIdGreaterThan(Long value) {
            addCriterion("setting_id >", value, "settingId");
            return (Criteria) this;
        }

        public Criteria andSettingIdGreaterThanOrEqualTo(Long value) {
            addCriterion("setting_id >=", value, "settingId");
            return (Criteria) this;
        }

        public Criteria andSettingIdLessThan(Long value) {
            addCriterion("setting_id <", value, "settingId");
            return (Criteria) this;
        }

        public Criteria andSettingIdLessThanOrEqualTo(Long value) {
            addCriterion("setting_id <=", value, "settingId");
            return (Criteria) this;
        }

        public Criteria andSettingIdIn(List<Long> values) {
            addCriterion("setting_id in", values, "settingId");
            return (Criteria) this;
        }

        public Criteria andSettingIdNotIn(List<Long> values) {
            addCriterion("setting_id not in", values, "settingId");
            return (Criteria) this;
        }

        public Criteria andSettingIdBetween(Long value1, Long value2) {
            addCriterion("setting_id between", value1, value2, "settingId");
            return (Criteria) this;
        }

        public Criteria andSettingIdNotBetween(Long value1, Long value2) {
            addCriterion("setting_id not between", value1, value2, "settingId");
            return (Criteria) this;
        }

        public Criteria andNotifierIsNull() {
            addCriterion("notifier is null");
            return (Criteria) this;
        }

        public Criteria andNotifierIsNotNull() {
            addCriterion("notifier is not null");
            return (Criteria) this;
        }

        public Criteria andNotifierEqualTo(String value) {
            addCriterion("notifier =", value, "notifier");
            return (Criteria) this;
        }

        public Criteria andNotifierNotEqualTo(String value) {
            addCriterion("notifier <>", value, "notifier");
            return (Criteria) this;
        }

        public Criteria andNotifierGreaterThan(String value) {
            addCriterion("notifier >", value, "notifier");
            return (Criteria) this;
        }

        public Criteria andNotifierGreaterThanOrEqualTo(String value) {
            addCriterion("notifier >=", value, "notifier");
            return (Criteria) this;
        }

        public Criteria andNotifierLessThan(String value) {
            addCriterion("notifier <", value, "notifier");
            return (Criteria) this;
        }

        public Criteria andNotifierLessThanOrEqualTo(String value) {
            addCriterion("notifier <=", value, "notifier");
            return (Criteria) this;
        }

        public Criteria andNotifierLike(String value) {
            addCriterion("notifier like", value, "notifier");
            return (Criteria) this;
        }

        public Criteria andNotifierNotLike(String value) {
            addCriterion("notifier not like", value, "notifier");
            return (Criteria) this;
        }

        public Criteria andNotifierIn(List<String> values) {
            addCriterion("notifier in", values, "notifier");
            return (Criteria) this;
        }

        public Criteria andNotifierNotIn(List<String> values) {
            addCriterion("notifier not in", values, "notifier");
            return (Criteria) this;
        }

        public Criteria andNotifierBetween(String value1, String value2) {
            addCriterion("notifier between", value1, value2, "notifier");
            return (Criteria) this;
        }

        public Criteria andNotifierNotBetween(String value1, String value2) {
            addCriterion("notifier not between", value1, value2, "notifier");
            return (Criteria) this;
        }

        public Criteria andEmailNotifyIsNull() {
            addCriterion("email_notify is null");
            return (Criteria) this;
        }

        public Criteria andEmailNotifyIsNotNull() {
            addCriterion("email_notify is not null");
            return (Criteria) this;
        }

        public Criteria andEmailNotifyEqualTo(Integer value) {
            addCriterion("email_notify =", value, "EmailNotify");
            return (Criteria) this;
        }

        public Criteria andEmailNotifyNotEqualTo(Integer value) {
            addCriterion("email_notify <>", value, "EmailNotify");
            return (Criteria) this;
        }

        public Criteria andEmailNotifyGreaterThan(Integer value) {
            addCriterion("email_notify >", value, "EmailNotify");
            return (Criteria) this;
        }

        public Criteria andEmailNotifyGreaterThanOrEqualTo(Integer value) {
            addCriterion("email_notify >=", value, "EmailNotify");
            return (Criteria) this;
        }

        public Criteria andEmailNotifyLessThan(Integer value) {
            addCriterion("email_notify <", value, "EmailNotify");
            return (Criteria) this;
        }

        public Criteria andEmailNotifyLessThanOrEqualTo(Integer value) {
            addCriterion("email_notify <=", value, "EmailNotify");
            return (Criteria) this;
        }

        public Criteria andEmailNotifyIn(List<Integer> values) {
            addCriterion("email_notify in", values, "EmailNotify");
            return (Criteria) this;
        }

        public Criteria andEmailNotifyNotIn(List<Integer> values) {
            addCriterion("email_notify not in", values, "EmailNotify");
            return (Criteria) this;
        }

        public Criteria andEmailNotifyBetween(Integer value1, Integer value2) {
            addCriterion("email_notify between", value1, value2, "EmailNotify");
            return (Criteria) this;
        }

        public Criteria andEmailNotifyNotBetween(Integer value1, Integer value2) {
            addCriterion("email_notify not between", value1, value2, "EmailNotify");
            return (Criteria) this;
        }

        public Criteria andEmailIsNull() {
            addCriterion("email is null");
            return (Criteria) this;
        }

        public Criteria andEmailIsNotNull() {
            addCriterion("email is not null");
            return (Criteria) this;
        }

        public Criteria andEmailEqualTo(String value) {
            addCriterion("email =", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotEqualTo(String value) {
            addCriterion("email <>", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThan(String value) {
            addCriterion("email >", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThanOrEqualTo(String value) {
            addCriterion("email >=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThan(String value) {
            addCriterion("email <", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThanOrEqualTo(String value) {
            addCriterion("email <=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLike(String value) {
            addCriterion("email like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotLike(String value) {
            addCriterion("email not like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailIn(List<String> values) {
            addCriterion("email in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotIn(List<String> values) {
            addCriterion("email not in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailBetween(String value1, String value2) {
            addCriterion("email between", value1, value2, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotBetween(String value1, String value2) {
            addCriterion("email not between", value1, value2, "email");
            return (Criteria) this;
        }

        public Criteria andSmsNotifyIsNull() {
            addCriterion("sms_notify is null");
            return (Criteria) this;
        }

        public Criteria andSmsNotifyIsNotNull() {
            addCriterion("sms_notify is not null");
            return (Criteria) this;
        }

        public Criteria andSmsNotifyEqualTo(Integer value) {
            addCriterion("sms_notify =", value, "SmsNotify");
            return (Criteria) this;
        }

        public Criteria andSmsNotifyNotEqualTo(Integer value) {
            addCriterion("sms_notify <>", value, "SmsNotify");
            return (Criteria) this;
        }

        public Criteria andSmsNotifyGreaterThan(Integer value) {
            addCriterion("sms_notify >", value, "SmsNotify");
            return (Criteria) this;
        }

        public Criteria andSmsNotifyGreaterThanOrEqualTo(Integer value) {
            addCriterion("sms_notify >=", value, "SmsNotify");
            return (Criteria) this;
        }

        public Criteria andSmsNotifyLessThan(Integer value) {
            addCriterion("sms_notify <", value, "SmsNotify");
            return (Criteria) this;
        }

        public Criteria andSmsNotifyLessThanOrEqualTo(Integer value) {
            addCriterion("sms_notify <=", value, "SmsNotify");
            return (Criteria) this;
        }

        public Criteria andSmsNotifyIn(List<Integer> values) {
            addCriterion("sms_notify in", values, "SmsNotify");
            return (Criteria) this;
        }

        public Criteria andSmsNotifyNotIn(List<Integer> values) {
            addCriterion("sms_notify not in", values, "SmsNotify");
            return (Criteria) this;
        }

        public Criteria andSmsNotifyBetween(Integer value1, Integer value2) {
            addCriterion("sms_notify between", value1, value2, "SmsNotify");
            return (Criteria) this;
        }

        public Criteria andSmsNotifyNotBetween(Integer value1, Integer value2) {
            addCriterion("sms_notify not between", value1, value2, "SmsNotify");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNull() {
            addCriterion("phone is null");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNotNull() {
            addCriterion("phone is not null");
            return (Criteria) this;
        }

        public Criteria andPhoneEqualTo(String value) {
            addCriterion("phone =", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotEqualTo(String value) {
            addCriterion("phone <>", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThan(String value) {
            addCriterion("phone >", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThanOrEqualTo(String value) {
            addCriterion("phone >=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThan(String value) {
            addCriterion("phone <", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThanOrEqualTo(String value) {
            addCriterion("phone <=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLike(String value) {
            addCriterion("phone like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotLike(String value) {
            addCriterion("phone not like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneIn(List<String> values) {
            addCriterion("phone in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotIn(List<String> values) {
            addCriterion("phone not in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneBetween(String value1, String value2) {
            addCriterion("phone between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotBetween(String value1, String value2) {
            addCriterion("phone not between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andCreateByIsNull() {
            addCriterion("create_by is null");
            return (Criteria) this;
        }

        public Criteria andCreateByIsNotNull() {
            addCriterion("create_by is not null");
            return (Criteria) this;
        }

        public Criteria andCreateByEqualTo(Long value) {
            addCriterion("create_by =", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByNotEqualTo(Long value) {
            addCriterion("create_by <>", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByGreaterThan(Long value) {
            addCriterion("create_by >", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByGreaterThanOrEqualTo(Long value) {
            addCriterion("create_by >=", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByLessThan(Long value) {
            addCriterion("create_by <", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByLessThanOrEqualTo(Long value) {
            addCriterion("create_by <=", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByIn(List<Long> values) {
            addCriterion("create_by in", values, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByNotIn(List<Long> values) {
            addCriterion("create_by not in", values, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByBetween(Long value1, Long value2) {
            addCriterion("create_by between", value1, value2, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByNotBetween(Long value1, Long value2) {
            addCriterion("create_by not between", value1, value2, "createBy");
            return (Criteria) this;
        }

        public Criteria andGmtCreateIsNull() {
            addCriterion("gmt_create is null");
            return (Criteria) this;
        }

        public Criteria andGmtCreateIsNotNull() {
            addCriterion("gmt_create is not null");
            return (Criteria) this;
        }

        public Criteria andGmtCreateEqualTo(Date value) {
            addCriterion("gmt_create =", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateNotEqualTo(Date value) {
            addCriterion("gmt_create <>", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateGreaterThan(Date value) {
            addCriterion("gmt_create >", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateGreaterThanOrEqualTo(Date value) {
            addCriterion("gmt_create >=", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateLessThan(Date value) {
            addCriterion("gmt_create <", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateLessThanOrEqualTo(Date value) {
            addCriterion("gmt_create <=", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateIn(List<Date> values) {
            addCriterion("gmt_create in", values, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateNotIn(List<Date> values) {
            addCriterion("gmt_create not in", values, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateBetween(Date value1, Date value2) {
            addCriterion("gmt_create between", value1, value2, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateNotBetween(Date value1, Date value2) {
            addCriterion("gmt_create not between", value1, value2, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andModifiedByIsNull() {
            addCriterion("modified_by is null");
            return (Criteria) this;
        }

        public Criteria andModifiedByIsNotNull() {
            addCriterion("modified_by is not null");
            return (Criteria) this;
        }

        public Criteria andModifiedByEqualTo(Long value) {
            addCriterion("modified_by =", value, "modifiedBy");
            return (Criteria) this;
        }

        public Criteria andModifiedByNotEqualTo(Long value) {
            addCriterion("modified_by <>", value, "modifiedBy");
            return (Criteria) this;
        }

        public Criteria andModifiedByGreaterThan(Long value) {
            addCriterion("modified_by >", value, "modifiedBy");
            return (Criteria) this;
        }

        public Criteria andModifiedByGreaterThanOrEqualTo(Long value) {
            addCriterion("modified_by >=", value, "modifiedBy");
            return (Criteria) this;
        }

        public Criteria andModifiedByLessThan(Long value) {
            addCriterion("modified_by <", value, "modifiedBy");
            return (Criteria) this;
        }

        public Criteria andModifiedByLessThanOrEqualTo(Long value) {
            addCriterion("modified_by <=", value, "modifiedBy");
            return (Criteria) this;
        }

        public Criteria andModifiedByIn(List<Long> values) {
            addCriterion("modified_by in", values, "modifiedBy");
            return (Criteria) this;
        }

        public Criteria andModifiedByNotIn(List<Long> values) {
            addCriterion("modified_by not in", values, "modifiedBy");
            return (Criteria) this;
        }

        public Criteria andModifiedByBetween(Long value1, Long value2) {
            addCriterion("modified_by between", value1, value2, "modifiedBy");
            return (Criteria) this;
        }

        public Criteria andModifiedByNotBetween(Long value1, Long value2) {
            addCriterion("modified_by not between", value1, value2, "modifiedBy");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedIsNull() {
            addCriterion("gmt_modified is null");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedIsNotNull() {
            addCriterion("gmt_modified is not null");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedEqualTo(Date value) {
            addCriterion("gmt_modified =", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedNotEqualTo(Date value) {
            addCriterion("gmt_modified <>", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedGreaterThan(Date value) {
            addCriterion("gmt_modified >", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedGreaterThanOrEqualTo(Date value) {
            addCriterion("gmt_modified >=", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedLessThan(Date value) {
            addCriterion("gmt_modified <", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedLessThanOrEqualTo(Date value) {
            addCriterion("gmt_modified <=", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedIn(List<Date> values) {
            addCriterion("gmt_modified in", values, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedNotIn(List<Date> values) {
            addCriterion("gmt_modified not in", values, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedBetween(Date value1, Date value2) {
            addCriterion("gmt_modified between", value1, value2, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedNotBetween(Date value1, Date value2) {
            addCriterion("gmt_modified not between", value1, value2, "gmtModified");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
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