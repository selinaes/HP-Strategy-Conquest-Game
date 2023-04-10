package edu.duke.ece651.team16.controller;

import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlertBoxTest extends ApplicationTest {

    private AlertBox alertBox;

    @BeforeAll
    static void setUpClass() {
        // initialize JavaFX toolkit
        JFXPanel jfxPanel = new JFXPanel();
    }

    @Override
    public void start(Stage stage) throws Exception {
        alertBox = new AlertBox();
    }

    @Test
    void displayImageAlert() throws TimeoutException {
        String imageUrl = "https://via.placeholder.com/150";
        WaitForAsyncUtils.asyncFx(() -> alertBox.displayImageAlert("Image Alert", imageUrl));

        FxRobot robot = new FxRobot();
        robot.sleep(1, TimeUnit.SECONDS);

        assertFalse(robot.lookup("#closeButton").tryQuery().isPresent());
    }

    @Test
    void showAlert() throws TimeoutException {
        WaitForAsyncUtils.asyncFx(() -> alertBox.showAlert("Text Alert", "This is a text alert."));

        FxRobot robot = new FxRobot();
        robot.sleep(1, TimeUnit.SECONDS);

        assertTrue(robot.lookup(".alert").tryQuery().isPresent());
    }
}
