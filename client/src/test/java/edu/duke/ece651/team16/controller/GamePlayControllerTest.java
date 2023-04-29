package edu.duke.ece651.team16.controller;

import static org.mockito.Mockito.*;

import java.beans.Transient;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.application.Platform;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class GamePlayControllerTest extends ApplicationTest {

    private GamePlayController controller;
    private Client client;
    String map = "{\"red\":[{\"TerritoryName\":\"Headmaster's Office\",\"Rate\":\"Food Rate: 5, Tech Rate: 5\",\"Ally\":\"\",\"Neighbors\":\"(next to: Black Lake: 5, Room of Requirement: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"red:1,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Black Lake\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Headmaster's Office: 5, Whomping Willow: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"red:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Whomping Willow\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Black Lake: 5, Great Hall: 5, Hogwarts Library: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"red:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Hospital Wing\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Quidditch Pitch: 5, Astronomy Tower: 5, Forbidden Forest: 5, Potions Classroom: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"red:0,0,0,0,0,0,0,;\"}],\"blue\":[{\"TerritoryName\":\"Quidditch Pitch\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Hospital Wing: 5, Astronomy Tower: 5, Room of Requirement: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"blue:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Forbidden Forest\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Hospital Wing: 5, Astronomy Tower: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"blue:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Room of Requirement\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Headmaster's Office: 5, Chamber of Secret: 5, Quidditch Pitch: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"blue:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Hogwarts Library\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Whomping Willow: 5, Chamber of Secret: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"blue:0,0,0,0,0,0,0,;\"}],\"yellow\":[{\"TerritoryName\":\"Astronomy Tower\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Hospital Wing: 5, Forbidden Forest: 5, Quidditch Pitch: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"yellow:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Great Hall\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Whomping Willow: 5, Chamber of Secret: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"yellow:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Potions Classroom\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Hospital Wing: 5, Chamber of Secret: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"yellow:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Chamber of Secret\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Hogwarts Library: 5, Room of Requirement: 5, Great Hall: 5, Potions Classroom: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"yellow:0,0,0,0,0,0,0,;\"}]}";

    String map_MoveTo = "{\"red\":[{\"TerritoryName\":\"Headmaster's Office\",\"Rate\":\"Food Rate: 5, Tech Rate: 5\",\"Ally\":\"\",\"Neighbors\":\"(next to: Black Lake: 5, Room of Requirement: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"red:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Black Lake\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Headmaster's Office: 5, Whomping Willow: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"red:1,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Whomping Willow\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Black Lake: 5, Great Hall: 5, Hogwarts Library: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"red:1,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Hospital Wing\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Quidditch Pitch: 5, Astronomy Tower: 5, Forbidden Forest: 5, Potions Classroom: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"red:0,0,0,0,0,0,0,;\"}],\"blue\":[{\"TerritoryName\":\"Quidditch Pitch\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Hospital Wing: 5, Astronomy Tower: 5, Room of Requirement: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"blue:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Forbidden Forest\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Hospital Wing: 5, Astronomy Tower: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"blue:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Room of Requirement\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Headmaster's Office: 5, Chamber of Secret: 5, Quidditch Pitch: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"blue:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Hogwarts Library\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Whomping Willow: 5, Chamber of Secret: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"blue:0,0,0,0,0,0,0,;\"}],\"yellow\":[{\"TerritoryName\":\"Astronomy Tower\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Hospital Wing: 5, Forbidden Forest: 5, Quidditch Pitch: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"yellow:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Great Hall\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Whomping Willow: 5, Chamber of Secret: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"yellow:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Potions Classroom\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Hospital Wing: 5, Chamber of Secret: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"yellow:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Chamber of Secret\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Hogwarts Library: 5, Room of Requirement: 5, Great Hall: 5, Potions Classroom: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"yellow:0,0,0,0,0,0,0,;\"}]}";

    String map_AttackTo = "{\"red\":[{\"TerritoryName\":\"Hogwarts Library\",\"Rate\":\"Food Rate: 5, Tech Rate: 5\",\"Ally\":\"\",\"Neighbors\":\"(next to: Whomping Willow: 5, Chamber of Secret: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"red:1,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Headmaster's Office\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Black Lake: 5, Room of Requirement: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"red:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Black Lake\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Headmaster's Office: 5, Whomping Willow: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"red:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Whomping Willow\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Black Lake: 5, Great Hall: 5, Hogwarts Library: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"red:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Hospital Wing\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Quidditch Pitch: 5, Astronomy Tower: 5, Forbidden Forest: 5, Potions Classroom: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"red:0,0,0,0,0,0,0,;\"}],\"blue\":[{\"TerritoryName\":\"Quidditch Pitch\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Hospital Wing: 5, Astronomy Tower: 5, Room of Requirement: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"blue:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Forbidden Forest\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Hospital Wing: 5, Astronomy Tower: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"blue:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Room of Requirement\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Headmaster's Office: 5, Chamber of Secret: 5, Quidditch Pitch: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"blue:0,0,0,0,0,0,0,;\"}],\"yellow\":[{\"TerritoryName\":\"Astronomy Tower\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Hospital Wing: 5, Forbidden Forest: 5, Quidditch Pitch: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"yellow:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Great Hall\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Whomping Willow: 5, Chamber of Secret: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"yellow:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Potions Classroom\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Hospital Wing: 5, Chamber of Secret: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"yellow:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Chamber of Secret\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"\",\"Neighbors\":\"(next to: Hogwarts Library: 5, Room of Requirement: 5, Great Hall: 5, Potions Classroom: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"yellow:0,0,0,0,0,0,0,;\"}]}";

    String jsonStringLog = "{\"Entry\":\"You are the blue player, what would you like to do?\"}";

    String map_Alliance = "{\"red\":[{\"TerritoryName\":\"Hogwarts Library\",\"Rate\":\"Food Rate: 5, Tech Rate: 5\",\"Ally\":\"blue\",\"Neighbors\":\"(next to: Whomping Willow: 5, Chamber of Secret: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"red:1,0,0,0,0,0,0,;blue:1,0,0,0,0,0,0,\"},{\"TerritoryName\":\"Headmaster's Office\",\"Rate\":\"Food Rate: 5, Tech Rate: 5\",\"Ally\":\"blue\",\"Neighbors\":\"(next to: Black Lake: 5, Room of Requirement: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"red:0,0,0,0,0,0,0,;blue:1,0,0,0,0,0,0,\"},{\"TerritoryName\":\"Black Lake\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"blue\",\"Neighbors\":\"(next to: Headmaster's Office: 5, Whomping Willow: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"red:0,0,0,0,0,0,0,;blue:1,0,0,0,0,0,0,\"},{\"TerritoryName\":\"Whomping Willow\",\"Rate\":\"Food Rate: 5, Tech Rate: 5 \",\"Ally\":\"blue\",\"Neighbors\":\"(next to: Black Lake: 5, Great Hall: 5, Hogwarts Library:5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level:1)\",\"Unit\":\"red:0,0,0,0,0,0,0,;blue:1,0,0,0,0,0,0,\"},{\"TerritoryName\":\"Hospital Wing\",\"Rate\":\"Food Rate: 5, Tech Rate: 5\",\"Ally\":\"blue\",\"Neighbors\":\"(next to: Quidditch Pitch: 5, Astronomy Tower: 5, Forbidden Forest: 5, Potions Classroom: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level:1)\",\"Unit\":\"red:0,0,0,0,0,0,0,;blue:1,0,0,0,0,0,0,\"}],\"blue\":[{\"TerritoryName\":\"Quidditch Pitch\",\"Rate\":\"Food Rate: 5, Tech Rate: 5\",\"Ally\":\"\",\"Neighbors\":\"(next to: Hospital Wing: 5, Astronomy Tower:5, Room of Requirement:5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level:1)\",\"Unit\":\"blue:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"ForbiddenForest\",\"Rate\":\"Food Rate: 5, Tech Rate: 5\",\"Ally\":\"\",\"Neighbors\":\"(next to: Hospital Wing: 5, Astronomy Tower:5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level:1)\",\"Unit\":\"blue:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Room of Requirement\",\"Rate\":\"Food Rate: 5, Tech Rate: 5\",\"Ally\":\"\",\"Neighbors\":\"(next to: Headmaster's Office: 5, Chamber of Secret:5, Quidditch Pitch:5)\",\"Resource\":\"(Food: 20 Tech: 20 TechLevel:1)\",\"Unit\":\"blue:0,0,0,0,0,0,0,;\"}],\"yellow\":[{\"TerritoryName\":\"Astronomy Tower\",\"Rate\":\"Food Rate: 5, Tech Rate: 5\",\"Ally\":\"\",\"Neighbors\":\"(next to: Hospital Wing:5, Forbidden Forest:5, Quidditch Pitch:5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"yellow:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Great Hall\",\"Rate\":\"Food Rate: 5, Tech Rate: 5\",\"Ally\":\"\",\"Neighbors\":\"(next to: Whomping Willow: 5, Chamber of Secret: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level:1)\",\"Unit\":\"yellow:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"PotionsClassroom\",\"Rate\":\"Food Rate: 5, Tech Rate: 5\",\"Ally\":\"\",\"Neighbors\":\"(next to: Hospital Wing: 5, Chamber ofSecret:5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level:1)\",\"Unit\":\"yellow:0,0,0,0,0,0,0,;\"},{\"TerritoryName\":\"Chamber of Secret\",\"Rate\":\"Food Rate: 5, Tech Rate: 5\",\"Ally\":\"\",\"Neighbors\":\"(next to: Hogwarts Library: 5, Room of Requirement: 5, Great Hall: 5, Potions Classroom: 5)\",\"Resource\":\"(Food: 20 Tech: 20 Tech Level: 1)\",\"Unit\":\"yellow:0,0,0,0,0,0,0,;\"}]}";

    private Socket makeSocket() throws IOException {
        Socket mockSocket = Mockito.mock(Socket.class);
        InputStream mockInputStream = Mockito.mock(InputStream.class);
        OutputStream mockOutputStream = Mockito.mock(OutputStream.class);
        Mockito.when(mockSocket.getInputStream()).thenReturn(mockInputStream);
        Mockito.when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);
        return mockSocket;
    }

    private BufferedReader makeMockSocketReader(Socket mockSocket) throws IOException {
        return new BufferedReader(new InputStreamReader(mockSocket.getInputStream()));
    }

    private PrintWriter makeMockSocketSend(Socket mockSocket) throws IOException {
        return new PrintWriter(mockSocket.getOutputStream(), true);
    }

    private PrintStream makeOut() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(byteArrayOutputStream);
        return out;
    }

    private void setClient() throws IOException, Exception {
        Socket mSocket = makeSocket();
        BufferedReader socketReceive = makeMockSocketReader(mSocket);
        PrintWriter socketSend = makeMockSocketSend(mSocket);
        BufferedReader inputSource = mock(BufferedReader.class);
        PrintStream out = makeOut();

        client = new Client(inputSource, out, socketReceive, socketSend);
        client.setClientSocket(mock(Socket.class));
        Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
        socketReceiveField.setAccessible(true);
        BufferedReader mockReader = mock(BufferedReader.class);
        when(mockReader.readLine()).thenReturn("movePromt").thenReturn("move").thenReturn("Valid")
                .thenReturn(map_MoveTo).thenReturn("attackPromt").thenReturn("attack").thenReturn("Valid")
                .thenReturn(map_AttackTo).thenReturn("donepromt").thenReturn("finished stage")
                .thenReturn("stage Complete").thenReturn(jsonStringLog).thenReturn(map_AttackTo)
                .thenReturn("Game continues").thenReturn("do nothing").thenReturn("upgradePromt").thenReturn("upgrade")
                .thenReturn("Valid").thenReturn(map_AttackTo).thenReturn("SpecialPrompt").thenReturn("special")
                .thenReturn("Valid").thenReturn(map).thenReturn("donepromt").thenReturn("finished stage")
                .thenReturn("stage Complete").thenReturn(jsonStringLog).thenReturn(map_AttackTo)
                .thenReturn("Game continues").thenReturn("do nothing").thenReturn("SpecialPrompt").thenReturn("special")
                .thenReturn("Valid").thenReturn(map).thenReturn("donepromt").thenReturn("finished stage")
                .thenReturn("stage Complete").thenReturn(jsonStringLog).thenReturn(map_AttackTo)
                .thenReturn("Game continues").thenReturn("do nothing").thenReturn("SpecialPrompt").thenReturn("special")
                .thenReturn("Valid").thenReturn(map).thenReturn("donepromt").thenReturn("finished stage")
                .thenReturn("stage Complete").thenReturn(jsonStringLog).thenReturn(map_AttackTo)
                .thenReturn("Game continues").thenReturn("do nothing").thenReturn("SpecialPrompt").thenReturn("special")
                .thenReturn("Valid").thenReturn(map).thenReturn("donepromt").thenReturn("finished stage")
                .thenReturn("stage Complete").thenReturn(jsonStringLog).thenReturn(map_AttackTo)
                .thenReturn("Game continues").thenReturn("do nothing").thenReturn("SpecialPrompt").thenReturn("special")
                .thenReturn("Valid").thenReturn(map).thenReturn("donepromt").thenReturn("finished stage")
                .thenReturn("stage Complete").thenReturn(jsonStringLog).thenReturn(map_AttackTo)
                .thenReturn("Game continues").thenReturn("do nothing").thenReturn("researchPromt")
                .thenReturn("research").thenReturn("Valid").thenReturn(
                        map_Alliance)
                .thenReturn("AlliancePrompt")
                .thenReturn("Alliance").thenReturn("Waiting for Alliance").thenReturn(map_Alliance)
                .thenReturn("donepromt").thenReturn("finished stage").thenReturn("stage Complete")
                .thenReturn(jsonStringLog).thenReturn(map_Alliance).thenReturn("Game continues")
                .thenReturn("do nothing").thenReturn("AlliancePrompt").thenReturn("Alliance").thenReturn("Valid")
                .thenReturn(map_Alliance).thenReturn("donepromt").thenReturn("finished stage")
                .thenReturn("stage Complete").thenReturn(jsonStringLog).thenReturn(map_Alliance)
                .thenReturn("Game continues").thenReturn("Choose watch").thenReturn("watchPrompt").thenReturn("valid")
                .thenReturn("stage Complete").thenReturn(jsonStringLog).thenReturn(map_Alliance).thenReturn("red");
        socketReceiveField.set(client, mockReader);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/GamePlay.fxml"));
        AnchorPane root = loader.load();
        controller = loader.getController();
        setClient();
        client.setColor("red");
        controller.setClient(client);
        MapParser mapParser = new MapParser(map);
        mapParser.setPlayer("red");
        controller.setMapParser(mapParser);
        controller.setMapFromChatRoom(map);
        List<String> territory = mapParser.getMyInitTerritory();
        LinkedHashMap<String, String> myTerritory = new LinkedHashMap<String, String>();
        for (String t : territory) {
            myTerritory.put(t, "0");
        }
        controller.setMyTerritory(myTerritory);
        controller.setChatRoomController(mock(ChatRoomController.class));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        waitForFxEvents();
    }

    @Test
    public void test() throws IOException, Exception {
        waitForFxEvents();
        clickOn("#move").clickOn().clickOn();
        clickOn("#office");
        clickOn("#lake");
        waitForFxEvents();
        Node textField1 = lookup(".text-field").nth(0).query(); // locate the first
        Node textField2 = lookup(".text-field").nth(1).query(); // locate the second
        clickOn(textField1).write("0"); // enter the level value
        clickOn(textField2).write("1"); // enter the number value
        clickOn("OK");
        waitForFxEvents();
        clickOn("#attack").clickOn().clickOn();
        clickOn("#willow");
        clickOn("#library");
        Node textField3 = lookup(".text-field").nth(0).query(); // locate the first
        Node textField4 = lookup(".text-field").nth(1).query(); // locate the second
        clickOn(textField3).write("0"); // enter the level value
        clickOn(textField4).write("1"); // enter the number value
        clickOn("OK");
        waitForFxEvents();
        clickOn("#finish");
        waitForFxEvents();
        clickOn(500.0, 500.0);
        clickOn("#endWait");
        waitForFxEvents();

        clickOn("#upgrade").clickOn().clickOn().clickOn();
        clickOn("#library");
        Node textField5 = lookup(".text-field").nth(0).query();
        Node textField6 = lookup(".text-field").nth(1).query();
        Node textField7 = lookup(".text-field").nth(2).query();
        clickOn(textField5).write("1"); // enter the level value
        clickOn(textField6).write("0"); // enter the number value
        clickOn(textField7).write("1"); // enter the number value
        clickOn("OK");
        // clickOn("#research");
        waitForFxEvents();

        // special: 3 2 5 1 4
        clickOn("#special");
        waitForFxEvents();
        clickOn("#finish").clickOn();
        waitForFxEvents();
        clickOn(500.0, 500.0);
        clickOn("#endWait");
        waitForFxEvents();

        clickOn(500.0, 500.0);
        waitForFxEvents();
        clickOn("#special");
        clickOn(500.0, 500.0);
        clickOn(500.0, 500.0);
        waitForFxEvents();
        clickOn("#finish");
        waitForFxEvents();
        clickOn(500.0, 500.0);
        clickOn("#endWait");
        waitForFxEvents();

        clickOn(500.0, 500.0);
        waitForFxEvents();
        clickOn("#special").clickOn();
        clickOn("#hall");
        clickOn(500.0, 500.0);
        clickOn(500.0, 500.0);
        waitForFxEvents();
        clickOn("#finish");
        waitForFxEvents();
        clickOn(500.0, 500.0);
        clickOn("#endWait");
        waitForFxEvents();

        clickOn(500.0, 500.0);
        waitForFxEvents();
        clickOn("#special");
        clickOn(500.0, 500.0);
        clickOn(500.0, 500.0);
        waitForFxEvents();
        clickOn("#finish");
        waitForFxEvents();
        clickOn(500.0, 500.0);
        clickOn("#endWait");
        waitForFxEvents();

        clickOn(500.0, 500.0);
        waitForFxEvents();
        clickOn("#special");
        clickOn(500.0, 500.0);
        clickOn(500.0, 500.0);
        waitForFxEvents();
        clickOn("#finish");
        waitForFxEvents();
        clickOn(500.0, 500.0);
        clickOn("#endWait");
        waitForFxEvents();

        clickOn("#research").clickOn();
        waitForFxEvents();
        clickOn("#alliance");
        Node textField8 = lookup(".text-field").nth(0).query();
        clickOn(textField8).write("blue"); // enter the level value
        clickOn("OK");
        waitForFxEvents();
        clickOn("OK");
        waitForFxEvents();
        clickOn("#finish");
        waitForFxEvents();
        clickOn(500.0, 500.0);
        clickOn("#endWait");
        waitForFxEvents();

        clickOn(500.0, 500.0);
        clickOn("#move").clickOn();
        waitForFxEvents();
        clickOn("#alliance");
        Node textField9 = lookup(".text-field").nth(0).query();
        clickOn(textField9).write("blue"); // enter the level value
        clickOn("OK");
        waitForFxEvents();
        clickOn("#finish");
        waitForFxEvents();
        clickOn(500.0, 500.0);
        clickOn("#endWait");
        waitForFxEvents();

        clickOn(500.0, 500.0);
        waitForFxEvents();
        clickOn("Watch");
        waitForFxEvents();
        clickOn("#watchUpdate").clickOn();
        waitForFxEvents();
    }
}
