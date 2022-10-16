/**
  * Copyright 2022 bejson.com 
  */
package com.hrhrng.drugforecast.common.jsonobject;


public class JsonRootBean {

    private Data data;
    private Warnings warnings;
    public void setData(Data data) {
         this.data = data;
     }
     public Data getData() {
         return data;
     }

    public void setWarnings(Warnings warnings) {
         this.warnings = warnings;
     }



     public Warnings getWarnings() {
         return warnings;
     }

}