package com.hrhrng.drugforecast.common.jsonobject.Enrichr;

/**
 * Copyright 2022 bejson.com
 */

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Auto-generated: 2022-10-30 21:9:50
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class ResData {

    @JsonProperty("DSigDB")
    private List<List<List<Object>>> dsigDB;

    public List<List<List<Object>>> getDsigDB() {
        return dsigDB;
    }

    public void setDsigDB(List<List<List<Object>>> dsigDB) {
        this.dsigDB = dsigDB;
    }
}