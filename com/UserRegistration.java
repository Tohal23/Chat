package com;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class UserRegistration {

    public static boolean checkRegistration(String login) throws ParserConfigurationException, IOException, SAXException, NoSuchAlgorithmException {
        login = toHEX(getHash(login));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document xml = builder.parse(new File("src/passwords.xml"));
        org.w3c.dom.Element main = xml.getDocumentElement();
        NodeList users = main.getChildNodes();
            for (int i = 0; i < users.getLength(); i++) {
                Node child = users.item(i);
                try {
                    if (child.getAttributes().item(0).getNodeValue().equals(login) ) {
                        return true;
                    }
                }
                catch (NullPointerException e) {

                }
            }
        return false;
    }

    public static void registration(String login, String password) {
        File xmlFile = new File("src/passwords.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            addElement(doc, login, password);

            doc.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("src/passwords.xml"));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
            System.out.println("XML успешно изменен!");

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    private static void addElement(Document doc, String login, String pass) throws NoSuchAlgorithmException {
        Node main = doc.getDocumentElement();
        Element user = doc.createElement("password");
        user.setAttribute("login", toHEX(getHash(login)));
        user.appendChild(doc.createTextNode(toHEX(getHash(pass))));
        main.appendChild(user);
    }

    public static boolean deleteUsername(String login) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        boolean flag = false;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document xml = builder.parse(new File("src/passwords.xml"));
        org.w3c.dom.Element main = xml.getDocumentElement();
        NodeList users = main.getChildNodes();

        for (int i = 0; i < users.getLength(); i++) {
            Node child = users.item(i);
            try {
                if (child.getAttributes().item(0).getNodeValue().equals(toHEX(getHash(login))) ) {
                    Node oldChild = child.getParentNode();
                    oldChild.removeChild(child);
                    flag = true;
                }
            }
            catch (NullPointerException e) {

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

//        xml.getDocumentElement().normalize();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(xml);
        StreamResult result = new StreamResult(new File("src/passwords.xml"));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
        System.out.println("XML успешно изменен!");

        return flag;
    }

    public static boolean checkPassword(String login, String answerForPassword) throws ParserConfigurationException, IOException, SAXException, NoSuchAlgorithmException {
        String hashPassword = toHEX(getHash(answerForPassword));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document xml = builder.parse(new File("src/passwords.xml"));
        Element main = xml.getDocumentElement();
        NodeList users = main.getChildNodes();
        for (int i = 0; i < users.getLength(); i++) {
            Node child = users.item(i);
            try {
                if (child.getAttributes().item(0).getNodeValue().equals(toHEX(getHash(login))) ) {
                    if (child.getTextContent().equals(hashPassword)) {
                        return true;
                    } else  {
                        return false;
                    }
                }
            }
            catch (NullPointerException e) {

            }
        }
        return false;
    }

    public static byte[] getHash(String str) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return md5.digest(str.getBytes());
    }

    public static String toHEX(byte[] hashPassword) {
        StringBuilder buffer = new StringBuilder();
        for (byte b : hashPassword) {
            buffer.append(String.format("%02X ", b));
        }
        return buffer.toString();
    }
}