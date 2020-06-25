package com.example.demo;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) throws Exception {
        String customClassPath = "/Users/wenbinsong/self/testclassloader/temp/";
        addPath(customClassPath);
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/test", "root", "root");
        File f = new File(customClassPath);
        URLClassLoader loader = new URLClassLoader(new URL[]{f.toURI().toURL() });
        //Class<?> cls2 = loader.loadClass("com.example.demo.Employes");
        Class<?> cls2 = ClassLoader.getSystemClassLoader().loadClass("com.example.demo.Employes");
        //Class<?> cls2 = Class.forName("com.example.demo.Employes");
        Object instance = cls2.newInstance();
        new DemoApplication().doTest(instance);
        SpringApplication.run(DemoApplication.class, args);
    }

    public static URLClassLoader addPath(String s) throws Exception {
        File f = new File(s);
        URL u = f.toURI().toURL();
        //URL u = new URL(s);
        URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class urlClass = URLClassLoader.class;
        Method method = urlClass.getDeclaredMethod("addURL", new Class[]{URL.class});
        method.setAccessible(true);
        method.invoke(urlClassLoader, new Object[]{u});

        return urlClassLoader;
    }

    public void doTest(Object instance) {

        Model m = (Model) instance;

        m.set("first_name", "John1");
        m.set("last_name", "Doe1");
        m.saveIt();
    }

}
