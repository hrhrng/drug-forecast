package com.hrhrng.drugforecast.common.cyto;

import java.util.List;

public class CytoData {

    List<Data> data;
    List<String> errors;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
