package org.example;

import java.io.*;
import java.net.HttpURLConnection; //Отправка запроса
import java.net.URL; //URL объекты
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import java.util.Arrays;

public class QuoteServices {
    public static String getQuotes(String urlString, int code){
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return parseXML(response.toString(), code);
        }
        catch (IOException e){
            System.out.println("Ошибка соединения");
            return null;
        }
    }
    private static String parseXML(String xmlData, int code){
        StringBuilder rates = new StringBuilder();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlData)));

            if (code == 1) {
                NodeList valuteList = doc.getElementsByTagName("Valute");
                for (int i = 0; i < valuteList.getLength(); i++) {
                    Node valuteNode = valuteList.item(i);

                    String charCode = valuteNode.getChildNodes().item(1).getTextContent();
                    String value = valuteNode.getChildNodes().item(5).getTextContent();

                    rates.append(charCode).append(": ").append(" ₽").append(value).append("\n");
                }
            }
            else if (code == 2){
                String[] metals  = new String[] {"Золото", "Серебро", "Платина", "Палладий"};
                NodeList metalList = doc.getElementsByTagName("Record");
                for (int i = 0; i < metalList.getLength(); i++) {
                    Node metalNode = metalList.item(i);

                    String buy = metalNode.getChildNodes().item(0).getTextContent();
                    String sell = metalNode.getChildNodes().item(1).getTextContent();

                    rates.append(metals[i]).append(": ").append(" Покупка - ").append(buy);
                    rates.append("₽ Продажа - ").append(sell).append("₽").append("\n");
                }
            }
            else{
                String[] currencyCode  = new String[] {"\uD83C\uDDFA\uD83C\uDDF8USD", "\uD83C\uDDEA\uD83C\uDDFAEUR", "\uD83C\uDDE8\uD83C\uDDF3CNY"};
                NodeList valuteList = doc.getElementsByTagName("Valute");
                int j = 0;
                for (int i = 0; i < valuteList.getLength(); i++) {
                    Node valuteNode = valuteList.item(i);
                    String charCode = valuteNode.getChildNodes().item(1).getTextContent();
                    if(Arrays.stream(currencyCode).anyMatch(s -> s.contains(charCode))) {
                        String value = valuteNode.getChildNodes().item(5).getTextContent();
                        rates.append(currencyCode[j]).append(": ").append(" ₽").append(value).append("\n");
                        j++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            rates.append("Ошибка парсинга XML.");
        }
        return rates.toString();
    }

}
