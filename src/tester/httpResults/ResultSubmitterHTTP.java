/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tester.httpResults;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class ResultSubmitterHTTP {
    
    private CloseableHttpClient client = HttpClients.createDefault();
    
    public static void main(String args[]) throws IOException{
    
        ResultSubmitterHTTP s = new ResultSubmitterHTTP();
        File file = new File("results\\assesmentResults\\100 -sortfix_obom");
        InputStream is = new FileInputStream(file);
        
        String htmlResp = s.submitResultFile(is,"100 -sortfix_obom");
        
        Document doc = Jsoup.parse(htmlResp);            
        System.out.println(s.getConfirmationID(doc));
        System.out.println(s.getDocumentResults(doc).get("Total"));
    
    }
    /**
     * Submits results to https://wingless.cs.washington.edu/assessment/servlet 
     * returns html reply         
     * @param resultStream stream representing results file defined in assesment
     * @param fileName
     * @return html response string
     * @throws IOException 
     */
    public String submitResultFile(InputStream resultStream, String fileName) throws IOException {


        HttpPost httpPost = new HttpPost("https://wingless.cs.washington.edu/assessment/servlet");
         
        
        
        InputStreamBody fileBody = new InputStreamBody(resultStream,ContentType.DEFAULT_BINARY,fileName);
        StringBody stringBody1 = new StringBody("fiit-moabc", ContentType.MULTIPART_FORM_DATA);
        // 
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        
        
            
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("uploadFile", new ByteArrayBody(IOUtils.toByteArray(resultStream), ContentType.TEXT_HTML, fileName));
        builder.addPart("toolName", stringBody1);
        HttpEntity entity = builder.build();

        httpPost.setEntity(entity);

        HttpResponse response = client.execute(httpPost);
        InputStream responseSream = response.getEntity().getContent();

        String htmlResponse = IOUtils.toString(responseSream, "UTF-8");
        responseSream.close();
        
        return htmlResponse;
            
            
        
    
    }

    /**
     * Uses document returned by submitResultFile
     * @param doc
     * @return confirmationID of test
     */
    public String getConfirmationID(Document doc) {    
        
        Elements bolds = doc.getElementsByTag("b");        
        Element bold = bolds.get(0);
        String boldContent = bold.html();
        boldContent = boldContent.trim();
        
        /*//http://stackoverflow.com/questions/4672806/java-simplest-way-to-get-last-word-in-a-string*/
        
        return boldContent.substring(boldContent.lastIndexOf(" ")+1);
        
    
    } 
    
    public HashMap<String, HashMap<String, String>> getDocumentResults(Document doc) {
        
        HashMap<String, HashMap<String, String>> results = new HashMap<>();
        Elements trs = doc.getElementsByTag("tr");
        
        // parse table header
        Elements headers = trs.get(0).children();
        ArrayList<String> stringHeaders = new ArrayList<>();        
        for(Element e:headers){
        
            stringHeaders.add(e.html().trim());
        
        }        
        trs.remove(0);
        
        //parseData
        Elements data;
        HashMap<String, String> rowDataMap;
        String rowName;
        for(Element tr:trs){
            
            
            data = tr.children();
        
            rowDataMap = new HashMap<>();
            rowName = data.get(0).html().trim();
            
            for(int i = 1; i < data.size(); i++){
            
                rowDataMap.put(stringHeaders.get(i), data.get(i).html().trim());
            
            }
            
            results.put(rowName, rowDataMap);
        
        }        
        
        return results;
        
    }
    
    
    
}
