//package com.hrhrng.drugforecast.helper;
//
//import com.hrhrng.drugforecast.common.stub.HttpStub;
//import org.dom4j.Document;
//import org.dom4j.DocumentException;
//import org.dom4j.Element;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//import java.util.Optional;
//
//public class GEOHelper {
//    @Autowired
//    HttpStub httpStub;
//    static String baseEsearchUrl = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi/esearch.fcgi?";
//    static String baseEsummaryUrl = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?";
//    public static String buildEsearchUrl(String disease) {
//        StringBuffer a = new StringBuffer();
//        a.append(baseEsearchUrl)
//                .append("db=gds&")
//                .append("term=(")
//                .append(disease)
//                .append(")+AND+\"Homo+sapiens\"[porgn]+AND+(\"gse\"[Filter]+AND+\"Genome+binding/occupancy+profiling+by+high+throughput+sequen\"[Filter])");
//        System.out.println(a);
//        return a.toString();
//    }
//    public static String buildEfetchUrl(String id) {
//        StringBuffer a = new StringBuffer();
//        a.append(baseEsummaryUrl)
//                .append("db=gds&")
//                .append("id=")
//                .append(id);
//        return a.toString();
//    }
//    public static String filter(List<String> list) {
//        Optional<String> a = list.stream().parallel().filter(GEOHelper::filterForEach).findFirst();
//        if (a.isPresent()) {
//            return getFileUrl(null);
//        } else {
//            return null;
//        }
//    }
//
//    private static String getFileUrl(Document document) {
//        document.getRootElement().elements("Item");
//        return "";
//    }
//
//    private static boolean filterForEach (String s) {
//
//        return true;
//    }
//
//    public List<String> eSearch(String disease) throws DocumentException {
//        String url = GEOHelper.buildEsearchUrl(disease);
//        Document document = httpStub.query(url);
//        Element e = document.getRootElement();
//        Element l = e.element("IdList");
//        List<String> list = (List) l.elements("Id").stream().map(item -> ((Element) item).getData());
//        return list;
//    }
//
//    public Document eSummary(String id) throws DocumentException {
//        String url = GEOHelper.buildEfetchUrl(id);
//        Document document = httpStub.query(id);
//
//        return httpStub.query(id);
//    }
//}
