package edu.duke.ece651.team16.controller;

import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.input.ScrollEvent;
import static org.mockito.Mockito.*;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class GamePlayControllerTest extends ApplicationTest {

    private GamePlayController controller;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/GamePlay.fxml"));
        AnchorPane root = loader.load();
        controller = loader.getController();
        controller.setMapParser(mock(MapParser.class));
        controller.setClient(mock(Client.class));
        controller.setMyTerritory(new LinkedHashMap<>());
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // @Before
    // public void setUp() {
    // clickOn("#rule");
    // }

    // @Test
    // public void testShowRule() {
    // assertNotNull(controller);
    // assertFalse(controller.exitGame.isVisible());
    // assertFalse(controller.watchUpdate.isVisible());
    // assertTrue(controller.watchUpdate.isDisable());
    // assertFalse(controller.battleTime.isVisible());
    // }

    // @Test
    // public void testZoomIn() {
    // assertNotNull(controller);
    // double initialScaleX = controller.mapImage.getScaleX();
    // double initialScaleY = controller.mapImage.getScaleY();
    // // scroll(controller.mapImage, javafx.scene.input.ScrollEvent.UP);
    // // scroll(controller.mapImage, ScrollEvent.SCROLL_UP);
    // assertTrue(controller.mapImage.getScaleX() > initialScaleX);
    // assertTrue(controller.mapImage.getScaleY() > initialScaleY);
    // }

    // @Test
    // public void testZoomOut() {
    // assertNotNull(controller);
    // double initialScaleX = controller.mapImage.getScaleX();
    // double initialScaleY = controller.mapImage.getScaleY();
    // // scroll(controller.mapImage, ScrollEvent.SCROLL_DOWN);
    // assertTrue(controller.mapImage.getScaleX() < initialScaleX);
    // assertTrue(controller.mapImage.getScaleY() < initialScaleY);
    // }

    // @Test
    // public void testDrag() {
    // assertNotNull(controller);
    // double initialTranslateX = controller.mapImage.getTranslateX();
    // double initialTranslateY = controller.mapImage.getTranslateY();
    // drag(controller.mapImage).moveTo(100, 100);
    // assertTrue(controller.mapImage.getTranslateX() > initialTranslateX);
    // assertTrue(controller.mapImage.getTranslateY() > initialTranslateY);
    // }

}
