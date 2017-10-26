package com.bbd.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OpinionEventExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public OpinionEventExample() {
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

        public Criteria andEventNameIsNull() {
            addCriterion("event_name is null");
            return (Criteria) this;
        }

        public Criteria andEventNameIsNotNull() {
            addCriterion("event_name is not null");
            return (Criteria) this;
        }

        public Criteria andEventNameEqualTo(String value) {
            addCriterion("event_name =", value, "eventName");
            return (Criteria) this;
        }

        public Criteria andEventNameNotEqualTo(String value) {
            addCriterion("event_name <>", value, "eventName");
            return (Criteria) this;
        }

        public Criteria andEventNameGreaterThan(String value) {
            addCriterion("event_name >", value, "eventName");
            return (Criteria) this;
        }

        public Criteria andEventNameGreaterThanOrEqualTo(String value) {
            addCriterion("event_name >=", value, "eventName");
            return (Criteria) this;
        }

        public Criteria andEventNameLessThan(String value) {
            addCriterion("event_name <", value, "eventName");
            return (Criteria) this;
        }

        public Criteria andEventNameLessThanOrEqualTo(String value) {
            addCriterion("event_name <=", value, "eventName");
            return (Criteria) this;
        }

        public Criteria andEventNameLike(String value) {
            addCriterion("event_name like", value, "eventName");
            return (Criteria) this;
        }

        public Criteria andEventNameNotLike(String value) {
            addCriterion("event_name not like", value, "eventName");
            return (Criteria) this;
        }

        public Criteria andEventNameIn(List<String> values) {
            addCriterion("event_name in", values, "eventName");
            return (Criteria) this;
        }

        public Criteria andEventNameNotIn(List<String> values) {
            addCriterion("event_name not in", values, "eventName");
            return (Criteria) this;
        }

        public Criteria andEventNameBetween(String value1, String value2) {
            addCriterion("event_name between", value1, value2, "eventName");
            return (Criteria) this;
        }

        public Criteria andEventNameNotBetween(String value1, String value2) {
            addCriterion("event_name not between", value1, value2, "eventName");
            return (Criteria) this;
        }

        public Criteria andEventGroupIsNull() {
            addCriterion("event_group is null");
            return (Criteria) this;
        }

        public Criteria andEventGroupIsNotNull() {
            addCriterion("event_group is not null");
            return (Criteria) this;
        }

        public Criteria andEventGroupEqualTo(String value) {
            addCriterion("event_group =", value, "eventGroup");
            return (Criteria) this;
        }

        public Criteria andEventGroupNotEqualTo(String value) {
            addCriterion("event_group <>", value, "eventGroup");
            return (Criteria) this;
        }

        public Criteria andEventGroupGreaterThan(String value) {
            addCriterion("event_group >", value, "eventGroup");
            return (Criteria) this;
        }

        public Criteria andEventGroupGreaterThanOrEqualTo(String value) {
            addCriterion("event_group >=", value, "eventGroup");
            return (Criteria) this;
        }

        public Criteria andEventGroupLessThan(String value) {
            addCriterion("event_group <", value, "eventGroup");
            return (Criteria) this;
        }

        public Criteria andEventGroupLessThanOrEqualTo(String value) {
            addCriterion("event_group <=", value, "eventGroup");
            return (Criteria) this;
        }

        public Criteria andEventGroupLike(String value) {
            addCriterion("event_group like", value, "eventGroup");
            return (Criteria) this;
        }

        public Criteria andEventGroupNotLike(String value) {
            addCriterion("event_group not like", value, "eventGroup");
            return (Criteria) this;
        }

        public Criteria andEventGroupIn(List<String> values) {
            addCriterion("event_group in", values, "eventGroup");
            return (Criteria) this;
        }

        public Criteria andEventGroupNotIn(List<String> values) {
            addCriterion("event_group not in", values, "eventGroup");
            return (Criteria) this;
        }

        public Criteria andEventGroupBetween(String value1, String value2) {
            addCriterion("event_group between", value1, value2, "eventGroup");
            return (Criteria) this;
        }

        public Criteria andEventGroupNotBetween(String value1, String value2) {
            addCriterion("event_group not between", value1, value2, "eventGroup");
            return (Criteria) this;
        }

        public Criteria andMonitorIsNull() {
            addCriterion("monitor is null");
            return (Criteria) this;
        }

        public Criteria andMonitorIsNotNull() {
            addCriterion("monitor is not null");
            return (Criteria) this;
        }

        public Criteria andMonitorEqualTo(String value) {
            addCriterion("monitor =", value, "monitor");
            return (Criteria) this;
        }

        public Criteria andMonitorNotEqualTo(String value) {
            addCriterion("monitor <>", value, "monitor");
            return (Criteria) this;
        }

        public Criteria andMonitorGreaterThan(String value) {
            addCriterion("monitor >", value, "monitor");
            return (Criteria) this;
        }

        public Criteria andMonitorGreaterThanOrEqualTo(String value) {
            addCriterion("monitor >=", value, "monitor");
            return (Criteria) this;
        }

        public Criteria andMonitorLessThan(String value) {
            addCriterion("monitor <", value, "monitor");
            return (Criteria) this;
        }

        public Criteria andMonitorLessThanOrEqualTo(String value) {
            addCriterion("monitor <=", value, "monitor");
            return (Criteria) this;
        }

        public Criteria andMonitorLike(String value) {
            addCriterion("monitor like", value, "monitor");
            return (Criteria) this;
        }

        public Criteria andMonitorNotLike(String value) {
            addCriterion("monitor not like", value, "monitor");
            return (Criteria) this;
        }

        public Criteria andMonitorIn(List<String> values) {
            addCriterion("monitor in", values, "monitor");
            return (Criteria) this;
        }

        public Criteria andMonitorNotIn(List<String> values) {
            addCriterion("monitor not in", values, "monitor");
            return (Criteria) this;
        }

        public Criteria andMonitorBetween(String value1, String value2) {
            addCriterion("monitor between", value1, value2, "monitor");
            return (Criteria) this;
        }

        public Criteria andMonitorNotBetween(String value1, String value2) {
            addCriterion("monitor not between", value1, value2, "monitor");
            return (Criteria) this;
        }

        public Criteria andRegionIsNull() {
            addCriterion("region is null");
            return (Criteria) this;
        }

        public Criteria andRegionIsNotNull() {
            addCriterion("region is not null");
            return (Criteria) this;
        }

        public Criteria andRegionEqualTo(String value) {
            addCriterion("region =", value, "region");
            return (Criteria) this;
        }

        public Criteria andRegionNotEqualTo(String value) {
            addCriterion("region <>", value, "region");
            return (Criteria) this;
        }

        public Criteria andRegionGreaterThan(String value) {
            addCriterion("region >", value, "region");
            return (Criteria) this;
        }

        public Criteria andRegionGreaterThanOrEqualTo(String value) {
            addCriterion("region >=", value, "region");
            return (Criteria) this;
        }

        public Criteria andRegionLessThan(String value) {
            addCriterion("region <", value, "region");
            return (Criteria) this;
        }

        public Criteria andRegionLessThanOrEqualTo(String value) {
            addCriterion("region <=", value, "region");
            return (Criteria) this;
        }

        public Criteria andRegionLike(String value) {
            addCriterion("region like", value, "region");
            return (Criteria) this;
        }

        public Criteria andRegionNotLike(String value) {
            addCriterion("region not like", value, "region");
            return (Criteria) this;
        }

        public Criteria andRegionIn(List<String> values) {
            addCriterion("region in", values, "region");
            return (Criteria) this;
        }

        public Criteria andRegionNotIn(List<String> values) {
            addCriterion("region not in", values, "region");
            return (Criteria) this;
        }

        public Criteria andRegionBetween(String value1, String value2) {
            addCriterion("region between", value1, value2, "region");
            return (Criteria) this;
        }

        public Criteria andRegionNotBetween(String value1, String value2) {
            addCriterion("region not between", value1, value2, "region");
            return (Criteria) this;
        }

        public Criteria andEventLevelIsNull() {
            addCriterion("event_level is null");
            return (Criteria) this;
        }

        public Criteria andEventLevelIsNotNull() {
            addCriterion("event_level is not null");
            return (Criteria) this;
        }

        public Criteria andEventLevelEqualTo(String value) {
            addCriterion("event_level =", value, "eventLevel");
            return (Criteria) this;
        }

        public Criteria andEventLevelNotEqualTo(String value) {
            addCriterion("event_level <>", value, "eventLevel");
            return (Criteria) this;
        }

        public Criteria andEventLevelGreaterThan(String value) {
            addCriterion("event_level >", value, "eventLevel");
            return (Criteria) this;
        }

        public Criteria andEventLevelGreaterThanOrEqualTo(String value) {
            addCriterion("event_level >=", value, "eventLevel");
            return (Criteria) this;
        }

        public Criteria andEventLevelLessThan(String value) {
            addCriterion("event_level <", value, "eventLevel");
            return (Criteria) this;
        }

        public Criteria andEventLevelLessThanOrEqualTo(String value) {
            addCriterion("event_level <=", value, "eventLevel");
            return (Criteria) this;
        }

        public Criteria andEventLevelLike(String value) {
            addCriterion("event_level like", value, "eventLevel");
            return (Criteria) this;
        }

        public Criteria andEventLevelNotLike(String value) {
            addCriterion("event_level not like", value, "eventLevel");
            return (Criteria) this;
        }

        public Criteria andEventLevelIn(List<String> values) {
            addCriterion("event_level in", values, "eventLevel");
            return (Criteria) this;
        }

        public Criteria andEventLevelNotIn(List<String> values) {
            addCriterion("event_level not in", values, "eventLevel");
            return (Criteria) this;
        }

        public Criteria andEventLevelBetween(String value1, String value2) {
            addCriterion("event_level between", value1, value2, "eventLevel");
            return (Criteria) this;
        }

        public Criteria andEventLevelNotBetween(String value1, String value2) {
            addCriterion("event_level not between", value1, value2, "eventLevel");
            return (Criteria) this;
        }

        public Criteria andMerchantIsNull() {
            addCriterion("merchant is null");
            return (Criteria) this;
        }

        public Criteria andMerchantIsNotNull() {
            addCriterion("merchant is not null");
            return (Criteria) this;
        }

        public Criteria andMerchantEqualTo(String value) {
            addCriterion("merchant =", value, "merchant");
            return (Criteria) this;
        }

        public Criteria andMerchantNotEqualTo(String value) {
            addCriterion("merchant <>", value, "merchant");
            return (Criteria) this;
        }

        public Criteria andMerchantGreaterThan(String value) {
            addCriterion("merchant >", value, "merchant");
            return (Criteria) this;
        }

        public Criteria andMerchantGreaterThanOrEqualTo(String value) {
            addCriterion("merchant >=", value, "merchant");
            return (Criteria) this;
        }

        public Criteria andMerchantLessThan(String value) {
            addCriterion("merchant <", value, "merchant");
            return (Criteria) this;
        }

        public Criteria andMerchantLessThanOrEqualTo(String value) {
            addCriterion("merchant <=", value, "merchant");
            return (Criteria) this;
        }

        public Criteria andMerchantLike(String value) {
            addCriterion("merchant like", value, "merchant");
            return (Criteria) this;
        }

        public Criteria andMerchantNotLike(String value) {
            addCriterion("merchant not like", value, "merchant");
            return (Criteria) this;
        }

        public Criteria andMerchantIn(List<String> values) {
            addCriterion("merchant in", values, "merchant");
            return (Criteria) this;
        }

        public Criteria andMerchantNotIn(List<String> values) {
            addCriterion("merchant not in", values, "merchant");
            return (Criteria) this;
        }

        public Criteria andMerchantBetween(String value1, String value2) {
            addCriterion("merchant between", value1, value2, "merchant");
            return (Criteria) this;
        }

        public Criteria andMerchantNotBetween(String value1, String value2) {
            addCriterion("merchant not between", value1, value2, "merchant");
            return (Criteria) this;
        }

        public Criteria andBrandIsNull() {
            addCriterion("brand is null");
            return (Criteria) this;
        }

        public Criteria andBrandIsNotNull() {
            addCriterion("brand is not null");
            return (Criteria) this;
        }

        public Criteria andBrandEqualTo(String value) {
            addCriterion("brand =", value, "brand");
            return (Criteria) this;
        }

        public Criteria andBrandNotEqualTo(String value) {
            addCriterion("brand <>", value, "brand");
            return (Criteria) this;
        }

        public Criteria andBrandGreaterThan(String value) {
            addCriterion("brand >", value, "brand");
            return (Criteria) this;
        }

        public Criteria andBrandGreaterThanOrEqualTo(String value) {
            addCriterion("brand >=", value, "brand");
            return (Criteria) this;
        }

        public Criteria andBrandLessThan(String value) {
            addCriterion("brand <", value, "brand");
            return (Criteria) this;
        }

        public Criteria andBrandLessThanOrEqualTo(String value) {
            addCriterion("brand <=", value, "brand");
            return (Criteria) this;
        }

        public Criteria andBrandLike(String value) {
            addCriterion("brand like", value, "brand");
            return (Criteria) this;
        }

        public Criteria andBrandNotLike(String value) {
            addCriterion("brand not like", value, "brand");
            return (Criteria) this;
        }

        public Criteria andBrandIn(List<String> values) {
            addCriterion("brand in", values, "brand");
            return (Criteria) this;
        }

        public Criteria andBrandNotIn(List<String> values) {
            addCriterion("brand not in", values, "brand");
            return (Criteria) this;
        }

        public Criteria andBrandBetween(String value1, String value2) {
            addCriterion("brand between", value1, value2, "brand");
            return (Criteria) this;
        }

        public Criteria andBrandNotBetween(String value1, String value2) {
            addCriterion("brand not between", value1, value2, "brand");
            return (Criteria) this;
        }

        public Criteria andAddressIsNull() {
            addCriterion("address is null");
            return (Criteria) this;
        }

        public Criteria andAddressIsNotNull() {
            addCriterion("address is not null");
            return (Criteria) this;
        }

        public Criteria andAddressEqualTo(String value) {
            addCriterion("address =", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotEqualTo(String value) {
            addCriterion("address <>", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThan(String value) {
            addCriterion("address >", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThanOrEqualTo(String value) {
            addCriterion("address >=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThan(String value) {
            addCriterion("address <", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThanOrEqualTo(String value) {
            addCriterion("address <=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLike(String value) {
            addCriterion("address like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotLike(String value) {
            addCriterion("address not like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressIn(List<String> values) {
            addCriterion("address in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotIn(List<String> values) {
            addCriterion("address not in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressBetween(String value1, String value2) {
            addCriterion("address between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotBetween(String value1, String value2) {
            addCriterion("address not between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andMerchantTelIsNull() {
            addCriterion("merchant_tel is null");
            return (Criteria) this;
        }

        public Criteria andMerchantTelIsNotNull() {
            addCriterion("merchant_tel is not null");
            return (Criteria) this;
        }

        public Criteria andMerchantTelEqualTo(String value) {
            addCriterion("merchant_tel =", value, "merchantTel");
            return (Criteria) this;
        }

        public Criteria andMerchantTelNotEqualTo(String value) {
            addCriterion("merchant_tel <>", value, "merchantTel");
            return (Criteria) this;
        }

        public Criteria andMerchantTelGreaterThan(String value) {
            addCriterion("merchant_tel >", value, "merchantTel");
            return (Criteria) this;
        }

        public Criteria andMerchantTelGreaterThanOrEqualTo(String value) {
            addCriterion("merchant_tel >=", value, "merchantTel");
            return (Criteria) this;
        }

        public Criteria andMerchantTelLessThan(String value) {
            addCriterion("merchant_tel <", value, "merchantTel");
            return (Criteria) this;
        }

        public Criteria andMerchantTelLessThanOrEqualTo(String value) {
            addCriterion("merchant_tel <=", value, "merchantTel");
            return (Criteria) this;
        }

        public Criteria andMerchantTelLike(String value) {
            addCriterion("merchant_tel like", value, "merchantTel");
            return (Criteria) this;
        }

        public Criteria andMerchantTelNotLike(String value) {
            addCriterion("merchant_tel not like", value, "merchantTel");
            return (Criteria) this;
        }

        public Criteria andMerchantTelIn(List<String> values) {
            addCriterion("merchant_tel in", values, "merchantTel");
            return (Criteria) this;
        }

        public Criteria andMerchantTelNotIn(List<String> values) {
            addCriterion("merchant_tel not in", values, "merchantTel");
            return (Criteria) this;
        }

        public Criteria andMerchantTelBetween(String value1, String value2) {
            addCriterion("merchant_tel between", value1, value2, "merchantTel");
            return (Criteria) this;
        }

        public Criteria andMerchantTelNotBetween(String value1, String value2) {
            addCriterion("merchant_tel not between", value1, value2, "merchantTel");
            return (Criteria) this;
        }

        public Criteria andConsumerIsNull() {
            addCriterion("consumer is null");
            return (Criteria) this;
        }

        public Criteria andConsumerIsNotNull() {
            addCriterion("consumer is not null");
            return (Criteria) this;
        }

        public Criteria andConsumerEqualTo(String value) {
            addCriterion("consumer =", value, "consumer");
            return (Criteria) this;
        }

        public Criteria andConsumerNotEqualTo(String value) {
            addCriterion("consumer <>", value, "consumer");
            return (Criteria) this;
        }

        public Criteria andConsumerGreaterThan(String value) {
            addCriterion("consumer >", value, "consumer");
            return (Criteria) this;
        }

        public Criteria andConsumerGreaterThanOrEqualTo(String value) {
            addCriterion("consumer >=", value, "consumer");
            return (Criteria) this;
        }

        public Criteria andConsumerLessThan(String value) {
            addCriterion("consumer <", value, "consumer");
            return (Criteria) this;
        }

        public Criteria andConsumerLessThanOrEqualTo(String value) {
            addCriterion("consumer <=", value, "consumer");
            return (Criteria) this;
        }

        public Criteria andConsumerLike(String value) {
            addCriterion("consumer like", value, "consumer");
            return (Criteria) this;
        }

        public Criteria andConsumerNotLike(String value) {
            addCriterion("consumer not like", value, "consumer");
            return (Criteria) this;
        }

        public Criteria andConsumerIn(List<String> values) {
            addCriterion("consumer in", values, "consumer");
            return (Criteria) this;
        }

        public Criteria andConsumerNotIn(List<String> values) {
            addCriterion("consumer not in", values, "consumer");
            return (Criteria) this;
        }

        public Criteria andConsumerBetween(String value1, String value2) {
            addCriterion("consumer between", value1, value2, "consumer");
            return (Criteria) this;
        }

        public Criteria andConsumerNotBetween(String value1, String value2) {
            addCriterion("consumer not between", value1, value2, "consumer");
            return (Criteria) this;
        }

        public Criteria andConsumerTelIsNull() {
            addCriterion("consumer_tel is null");
            return (Criteria) this;
        }

        public Criteria andConsumerTelIsNotNull() {
            addCriterion("consumer_tel is not null");
            return (Criteria) this;
        }

        public Criteria andConsumerTelEqualTo(String value) {
            addCriterion("consumer_tel =", value, "consumerTel");
            return (Criteria) this;
        }

        public Criteria andConsumerTelNotEqualTo(String value) {
            addCriterion("consumer_tel <>", value, "consumerTel");
            return (Criteria) this;
        }

        public Criteria andConsumerTelGreaterThan(String value) {
            addCriterion("consumer_tel >", value, "consumerTel");
            return (Criteria) this;
        }

        public Criteria andConsumerTelGreaterThanOrEqualTo(String value) {
            addCriterion("consumer_tel >=", value, "consumerTel");
            return (Criteria) this;
        }

        public Criteria andConsumerTelLessThan(String value) {
            addCriterion("consumer_tel <", value, "consumerTel");
            return (Criteria) this;
        }

        public Criteria andConsumerTelLessThanOrEqualTo(String value) {
            addCriterion("consumer_tel <=", value, "consumerTel");
            return (Criteria) this;
        }

        public Criteria andConsumerTelLike(String value) {
            addCriterion("consumer_tel like", value, "consumerTel");
            return (Criteria) this;
        }

        public Criteria andConsumerTelNotLike(String value) {
            addCriterion("consumer_tel not like", value, "consumerTel");
            return (Criteria) this;
        }

        public Criteria andConsumerTelIn(List<String> values) {
            addCriterion("consumer_tel in", values, "consumerTel");
            return (Criteria) this;
        }

        public Criteria andConsumerTelNotIn(List<String> values) {
            addCriterion("consumer_tel not in", values, "consumerTel");
            return (Criteria) this;
        }

        public Criteria andConsumerTelBetween(String value1, String value2) {
            addCriterion("consumer_tel between", value1, value2, "consumerTel");
            return (Criteria) this;
        }

        public Criteria andConsumerTelNotBetween(String value1, String value2) {
            addCriterion("consumer_tel not between", value1, value2, "consumerTel");
            return (Criteria) this;
        }

        public Criteria andOpinionCountIsNull() {
            addCriterion("opinion_count is null");
            return (Criteria) this;
        }

        public Criteria andOpinionCountIsNotNull() {
            addCriterion("opinion_count is not null");
            return (Criteria) this;
        }

        public Criteria andOpinionCountEqualTo(Integer value) {
            addCriterion("opinion_count =", value, "opinionCount");
            return (Criteria) this;
        }

        public Criteria andOpinionCountNotEqualTo(Integer value) {
            addCriterion("opinion_count <>", value, "opinionCount");
            return (Criteria) this;
        }

        public Criteria andOpinionCountGreaterThan(Integer value) {
            addCriterion("opinion_count >", value, "opinionCount");
            return (Criteria) this;
        }

        public Criteria andOpinionCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("opinion_count >=", value, "opinionCount");
            return (Criteria) this;
        }

        public Criteria andOpinionCountLessThan(Integer value) {
            addCriterion("opinion_count <", value, "opinionCount");
            return (Criteria) this;
        }

        public Criteria andOpinionCountLessThanOrEqualTo(Integer value) {
            addCriterion("opinion_count <=", value, "opinionCount");
            return (Criteria) this;
        }

        public Criteria andOpinionCountIn(List<Integer> values) {
            addCriterion("opinion_count in", values, "opinionCount");
            return (Criteria) this;
        }

        public Criteria andOpinionCountNotIn(List<Integer> values) {
            addCriterion("opinion_count not in", values, "opinionCount");
            return (Criteria) this;
        }

        public Criteria andOpinionCountBetween(Integer value1, Integer value2) {
            addCriterion("opinion_count between", value1, value2, "opinionCount");
            return (Criteria) this;
        }

        public Criteria andOpinionCountNotBetween(Integer value1, Integer value2) {
            addCriterion("opinion_count not between", value1, value2, "opinionCount");
            return (Criteria) this;
        }

        public Criteria andWarnCountIsNull() {
            addCriterion("warn_count is null");
            return (Criteria) this;
        }

        public Criteria andWarnCountIsNotNull() {
            addCriterion("warn_count is not null");
            return (Criteria) this;
        }

        public Criteria andWarnCountEqualTo(Integer value) {
            addCriterion("warn_count =", value, "warnCount");
            return (Criteria) this;
        }

        public Criteria andWarnCountNotEqualTo(Integer value) {
            addCriterion("warn_count <>", value, "warnCount");
            return (Criteria) this;
        }

        public Criteria andWarnCountGreaterThan(Integer value) {
            addCriterion("warn_count >", value, "warnCount");
            return (Criteria) this;
        }

        public Criteria andWarnCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("warn_count >=", value, "warnCount");
            return (Criteria) this;
        }

        public Criteria andWarnCountLessThan(Integer value) {
            addCriterion("warn_count <", value, "warnCount");
            return (Criteria) this;
        }

        public Criteria andWarnCountLessThanOrEqualTo(Integer value) {
            addCriterion("warn_count <=", value, "warnCount");
            return (Criteria) this;
        }

        public Criteria andWarnCountIn(List<Integer> values) {
            addCriterion("warn_count in", values, "warnCount");
            return (Criteria) this;
        }

        public Criteria andWarnCountNotIn(List<Integer> values) {
            addCriterion("warn_count not in", values, "warnCount");
            return (Criteria) this;
        }

        public Criteria andWarnCountBetween(Integer value1, Integer value2) {
            addCriterion("warn_count between", value1, value2, "warnCount");
            return (Criteria) this;
        }

        public Criteria andWarnCountNotBetween(Integer value1, Integer value2) {
            addCriterion("warn_count not between", value1, value2, "warnCount");
            return (Criteria) this;
        }

        public Criteria andFileReasonIsNull() {
            addCriterion("file_reason is null");
            return (Criteria) this;
        }

        public Criteria andFileReasonIsNotNull() {
            addCriterion("file_reason is not null");
            return (Criteria) this;
        }

        public Criteria andFileReasonEqualTo(String value) {
            addCriterion("file_reason =", value, "fileReason");
            return (Criteria) this;
        }

        public Criteria andFileReasonNotEqualTo(String value) {
            addCriterion("file_reason <>", value, "fileReason");
            return (Criteria) this;
        }

        public Criteria andFileReasonGreaterThan(String value) {
            addCriterion("file_reason >", value, "fileReason");
            return (Criteria) this;
        }

        public Criteria andFileReasonGreaterThanOrEqualTo(String value) {
            addCriterion("file_reason >=", value, "fileReason");
            return (Criteria) this;
        }

        public Criteria andFileReasonLessThan(String value) {
            addCriterion("file_reason <", value, "fileReason");
            return (Criteria) this;
        }

        public Criteria andFileReasonLessThanOrEqualTo(String value) {
            addCriterion("file_reason <=", value, "fileReason");
            return (Criteria) this;
        }

        public Criteria andFileReasonLike(String value) {
            addCriterion("file_reason like", value, "fileReason");
            return (Criteria) this;
        }

        public Criteria andFileReasonNotLike(String value) {
            addCriterion("file_reason not like", value, "fileReason");
            return (Criteria) this;
        }

        public Criteria andFileReasonIn(List<String> values) {
            addCriterion("file_reason in", values, "fileReason");
            return (Criteria) this;
        }

        public Criteria andFileReasonNotIn(List<String> values) {
            addCriterion("file_reason not in", values, "fileReason");
            return (Criteria) this;
        }

        public Criteria andFileReasonBetween(String value1, String value2) {
            addCriterion("file_reason between", value1, value2, "fileReason");
            return (Criteria) this;
        }

        public Criteria andFileReasonNotBetween(String value1, String value2) {
            addCriterion("file_reason not between", value1, value2, "fileReason");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIsNull() {
            addCriterion("is_delete is null");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIsNotNull() {
            addCriterion("is_delete is not null");
            return (Criteria) this;
        }

        public Criteria andIsDeleteEqualTo(Byte value) {
            addCriterion("is_delete =", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotEqualTo(Byte value) {
            addCriterion("is_delete <>", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteGreaterThan(Byte value) {
            addCriterion("is_delete >", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_delete >=", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteLessThan(Byte value) {
            addCriterion("is_delete <", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteLessThanOrEqualTo(Byte value) {
            addCriterion("is_delete <=", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIn(List<Byte> values) {
            addCriterion("is_delete in", values, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotIn(List<Byte> values) {
            addCriterion("is_delete not in", values, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteBetween(Byte value1, Byte value2) {
            addCriterion("is_delete between", value1, value2, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotBetween(Byte value1, Byte value2) {
            addCriterion("is_delete not between", value1, value2, "isDelete");
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