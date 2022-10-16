/**
  * Copyright 2022 bejson.com 
  */
package com.hrhrng.drugforecast.common.jsonobject;
import java.util.List;

/**
 * Auto-generated: 2022-08-16 23:13:33
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Data {

    private List<Hits> hits;
    private Pagination pagination;
    public void setHits(List<Hits> hits) {
         this.hits = hits;
     }
     public List<Hits> getHits() {
         return hits;
     }

    public void setPagination(Pagination pagination) {
         this.pagination = pagination;
     }
     public Pagination getPagination() {
         return pagination;
     }

}