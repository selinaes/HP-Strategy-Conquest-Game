package edu.duke.ece651.team16.controller;

// import static org.junit.jupiter.api.Assertions.*;

// import org.junit.jupiter.api.BeforeAll;

// import org.junit.jupiter.api.Test;
// import javafx.embed.swing.JFXPanel;
// import javafx.scene.control.Alert;

import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlertBoxTest extends ApplicationTest {

  private AlertBox alertBox;

  @BeforeAll
  public static void setUpClass() throws Exception {
    // Initialize JavaFX runtime using FxToolkit
    FxToolkit.registerPrimaryStage(() -> new Stage());
  }

  @Test
  public void testDisplayImageAlert() {
    // Create an instance of AlertBox
    alertBox = new AlertBox();
    // Call the method to display an image alert
    alertBox.displayImageAlert("Test", "/path/to/image.png");
    // Verify that the image alert is displayed correctly
    assertEquals(1, lookup("#Close").queryAll().size());
    assertEquals(1, lookup("#ImageView").queryAll().size());
    assertEquals(1, lookup("#Alert").queryAll().size());
  }

  @Test
  public void testShowAlert() {
    // Create an instance of AlertBox
    alertBox = new AlertBox();
    // Call the method to display a text alert
    alertBox.showAlert("Test", "This is a test message");
    // Verify that the text alert is displayed correctly
    assertEquals("Test", lookup("#AlertTitle").query().getText());
    assertEquals("This is a test message", lookup("#AlertMessage").query().getText());
    assertEquals(1, lookup("#Alert").queryAll().size());
  }
}
