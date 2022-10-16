/**
  * Copyright 2022 bejson.com 
  */
package com.hrhrng.drugforecast.common.jsonobject;

import java.util.List;

public class Hits {

    private String id;
    private String file_id;

    private String file_name;

    private List<AssociatedEntitie> associated_entities;

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }
    public String getFile_name() {
        return file_name;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
         this.id = id;
     }
    public String getFile_id() {
    return file_id;
}
    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public List<AssociatedEntitie> getAssociated_entities() {
        return associated_entities;
    }

    public void setAssociated_entities(List<AssociatedEntitie> associated_entities) {
        this.associated_entities = associated_entities;
    }
}