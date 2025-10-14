module game_store {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.web;

  requires org.controlsfx.controls;
  requires com.dlsc.formsfx;
  requires net.synedra.validatorfx;
  requires org.kordamp.ikonli.javafx;
  requires org.kordamp.bootstrapfx.core;
  requires eu.hansolo.tilesfx;
  requires com.almasb.fxgl.all;
  requires jbcrypt;
  requires java.sql;
  requires javafx.graphics;

  opens game_store to javafx.fxml;
  opens game_store.app.models to javafx.base;
//  exports game_store;
  exports game_store.app;
  opens game_store.app to javafx.fxml;
  exports game_store.app.controllers;
  opens game_store.app.controllers to javafx.fxml;
//  exports game_store.app;
//  opens game_store.app to javafx.fxml;
}