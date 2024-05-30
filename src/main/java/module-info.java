module org.openjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.web;
    requires java.base;
    requires java.sql;
    requires com.google.gson;
    requires java.net.http;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires java.sql.rowset;
    requires jetty.http;
    requires spark.core;
    requires org.apache.commons.io;
    requires ydev.app;
    requires java.desktop;
    opens org.openjfx to javafx.fxml;
    exports org.openjfx;
    exports org.openjfx.components;
    opens org.openjfx.components to javafx.fxml;
}