package com.hrhrng.drugforecast.common.stub;

import java.util.List;

public class Manifest {

    Data data;

    Warnings warnings;

    public Manifest(Data data) {
        this.data = data;
    }

    public Manifest(Data data, Warnings warnings) {
        this.data = data;
        this.warnings = warnings;
    }

    class Data {

        public Data(List<Entry> hits, Pagination pagination) {
            this.hits = hits;
            this.pagination = pagination;
        }

        List<Entry> hits;
        Pagination pagination;

        class Entry {
            public Entry(String id, String file_id) {
                this.id = id;
                this.file_id = file_id;
            }

            String id;
            String file_id;
        }

        class Pagination {
            private int count;
            private int total;
            private int size;
            private int from;
            private String sort;
            private int page;
            private int pages;

            public Pagination(int count, int total, int size, int from, String sort, int page, int pages) {
                this.count = count;
                this.total = total;
                this.size = size;
                this.from = from;
                this.sort = sort;
                this.page = page;
                this.pages = pages;
            }
        }
    }
    class Warnings {

    }


}
