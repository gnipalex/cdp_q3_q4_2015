package com.epam.hnyp.oxm.sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.epam.hnyp.oxm.sample.schema.Person;


public class App {
    
    private Jaxb2Marshaller marshaller;
    
    public App(Jaxb2Marshaller marshaller) {
        this.marshaller = marshaller;
    }
    
    public Person createPerson() {
        Person person = new Person();
        person.setAge(77);
        person.setName("this is person");
        person.setMale(true);
        return person;
    }
    
    public void savePersonToXml(Person person, File xmlFile) throws IOException {
        try (OutputStream os = new FileOutputStream(xmlFile)) {
            marshaller.marshal(person, new StreamResult(os));
        }
    }
    
    public Person readPersonFromXml(File xmlFile) throws IOException {
        try (InputStream is = new FileInputStream(xmlFile)) {
            return (Person) marshaller.unmarshal(new StreamSource(is));
        }
    }
    
    public static void main(String[] args) throws IOException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("app-context.xml");
        Jaxb2Marshaller marshaller = ctx.getBean("jaxb2Marshaller", Jaxb2Marshaller.class);
        App app = new App(marshaller);
        
        File ericPersonXmlFile = new ClassPathResource("eric-person.xml").getFile();
        Person ericPerson = app.readPersonFromXml(ericPersonXmlFile);
        System.out.println("read person name : " + ericPerson.getName());
        
        File newPersonFile = new File("new-person.xml");
        app.savePersonToXml(app.createPerson(), newPersonFile);
    }

}
