import chapter2.Utils;
import javafx.scene.control.ComboBox;
import org.bouncycastle.util.encoders.Base64;
import org.w3c.dom.*;

import javax.rmi.CORBA.Util;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.security.Key;

public class SettingsManager {

    File file;
    DocumentBuilder documentBuilder = null;
    DocumentBuilderFactory documentBuilderFactory;
    Document document;

    public SettingsManager (){
        setDocument("./src/main/java/Settingsdata.xml");
    }

    public void addAllElements(String elements, ComboBox cb, int checkValue, String value){
        NodeList nList = document.getElementsByTagName(elements);
        if(elements != "keysize") {
            for (int j = 0; j < nList.getLength(); j++) {

                org.w3c.dom.Node entry = nList.item(j);
                Element eElement = (Element) entry;
                eElement.getElementsByTagName(value).item(0).getTextContent();
                if (checkValue != -1) {
                    cb.getItems().add(eElement.getElementsByTagName(value).item(0).getTextContent());
                }

            }
        }else{
            for (int j = 0; j < nList.getLength(); j++) {

                nList.item(j).getTextContent();
                if (checkValue != -1 && checkValue != 0) {
                    cb.getItems().add(nList.item(j).getTextContent());
                }

            }
        }
    }

    public void addSubelements (String element, int id, String elements, String value,ComboBox cb){

        NodeList nList1 = document.getElementsByTagName(element);
        org.w3c.dom.Node node = nList1.item(id);
        Element eElement0 = (Element) node;
        NodeList nList2 = document.getElementsByTagName(elements);
        for(int i = 0; i < eElement0.getElementsByTagName(value).getLength(); i++ ){

            if(elements != "keysize") {
                int currentID = Integer.parseInt(eElement0.getElementsByTagName(value).item(i).getTextContent());
                org.w3c.dom.Node entry = nList2.item(currentID);
                Element eElement1 = (Element) entry;
                eElement1.getElementsByTagName("name").item(0).getTextContent();
                cb.getItems().add(eElement1.getElementsByTagName("name").item(0).getTextContent());
            }else {
                int currentID = Integer.parseInt(eElement0.getElementsByTagName(value).item(i).getTextContent());
                nList2.item(currentID).getTextContent();
                cb.getItems().add(nList2.item(currentID).getTextContent());
            }
        }

    }


    public int getIndex (String elements, int keysize) {
        NodeList nList = document.getElementsByTagName(elements);
        int index = 0;
        for(int i = 0; i < nList.getLength(); i++ ){

            org.w3c.dom.Node entry = nList.item(i);
            Element eElement = (Element) entry;
            int currentKeysize = Integer.parseInt(nList.item(i).getTextContent());
            if(keysize == currentKeysize) {
                index = i;
            }

        }

        return index;

    }

    public int getIndex (String elements, String name) {
        NodeList nList = document.getElementsByTagName(elements);
        int index = 0;
        for(int i = 0; i < nList.getLength(); i++ ){

            org.w3c.dom.Node entry = nList.item(i);
            Element eElement = (Element) entry;
            String currentName =  eElement.getElementsByTagName("name").item(0).getTextContent();
            if(name == currentName) {

                index = i;
            }

        }

        return index;

    }

    public void getElementsByBoolean (String elements, String boolelement, ComboBox cb) {

        NodeList algoList = document.getElementsByTagName(elements);

        for(int i = 0; i < algoList.getLength(); i++ ) {

            org.w3c.dom.Node entry = algoList.item(i);
            Element eElement = (Element) entry;
            boolean needed = Boolean.parseBoolean(eElement.getElementsByTagName(boolelement).item(0).getTextContent());
            if (needed == true) {

                eElement.getElementsByTagName("name").item(0).getTextContent();
                System.out.println(eElement.getElementsByTagName("name").item(0).getTextContent());

                cb.getItems().add(eElement.getElementsByTagName("name").item(0).getTextContent());
            } else {

            }

        }

    }

    public  String getElementName (String element, int id) {
        NodeList algoList = document.getElementsByTagName(element);
        org.w3c.dom.Node entry = algoList.item(id);
        Element eElement = (Element) entry;

        return eElement.getElementsByTagName("name").item(0).getTextContent();
    }

    public void setDocument(String path){
        file = new File(path);
        System.out.println(file);
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Document getDocument () {
        return document;
    }

    public int getBlocksize (String element, int id) {
        NodeList nList = document.getElementsByTagName(element);
        org.w3c.dom.Node node = nList.item(id);
        Element eElement = (Element) node;
        System.out.println("BS: "+Integer.parseInt(eElement.getElementsByTagName("blocksize").item(0).getTextContent()));
        return Integer.parseInt(eElement.getElementsByTagName("blocksize").item(0).getTextContent());
    }

    public void check () {

        System.out.println ("Root element of the doc is " + document.getDocumentElement().getNodeName());
        String algorithm = document.getElementsByTagName("algorithms").item(0).getTextContent();
        String mode = document.getElementsByTagName("modes").item(0).getTextContent();
        String padding = document.getElementsByTagName("paddings").item(0).getTextContent();
        System.out.println(algorithm + mode + padding);

    }

    public void setKey(byte[] keystring) throws TransformerException {
        setDocument("./src/main/java/KeyFile.xml");

        Node keylist = document.getElementsByTagName("keylist").item(0);

        Element key = document.createElement("key");
        key.appendChild(document.createTextNode(Base64.toBase64String(keystring)));
        keylist.appendChild(key);


        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            DOMSource domSource = new DOMSource(document);
            Transformer transformer = null;
            transformer = transformerFactory.newTransformer();
            StreamResult streamResult = new StreamResult(new File("./src/main/java/KeyFile.xml"));
            transformer.transform(domSource, streamResult);
            System.out.println("DONE");
        } catch (Exception e) {
            e.printStackTrace();
        }


        setDocument("./src/main/java/Settingsdata.xml");
    }

    public byte[] getKey(int count){
        setDocument("./src/main/java/KeyFile.xml");
        byte[] key= null;

        NodeList keylist = document.getElementsByTagName("key");
        key = Base64.decode(keylist.item(count).getTextContent().getBytes());
        System.out.println("HIER1: "+Utils.toHex(key));


        setDocument("./src/main/java/Settingsdata.xml");

        return key;

    }

    public void setIterationcount(int count){
        setDocument("./src/main/java/KeyFile.xml");

        Node icList = document.getElementsByTagName("iterationcount").item(0);

        icList.setTextContent(String.valueOf(count));

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            DOMSource domSource = new DOMSource(document);
            Transformer transformer = null;
            transformer = transformerFactory.newTransformer();
            StreamResult streamResult = new StreamResult(new File("./src/main/java/KeyFile.xml"));
            transformer.transform(domSource, streamResult);
            System.out.println("DONE");
        } catch (Exception e) {
            e.printStackTrace();
        }

        setDocument("./src/main/java/Settingsdata.xml");
    }

    public int getIterationcount(){
        setDocument("./src/main/java/KeyFile.xml");
        int count;

        NodeList icList = document.getElementsByTagName("iterationcount");
        System.out.println(icList.item(0).getTextContent());
        count = Integer.parseInt(icList.item(0).getTextContent());
        System.out.println(Integer.parseInt(icList.item(0).getTextContent()));

        setDocument("./src/main/java/Settingsdata.xml");

        return count;

    }

}
