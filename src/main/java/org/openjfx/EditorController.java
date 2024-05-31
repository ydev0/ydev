package org.openjfx;
import com.google.gson.Gson;
import com.ydev00.model.thread.Article;
import com.ydev00.model.thread.Thrd;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.DirectoryChooser;
import org.apache.http.HttpResponse;
import org.openjfx.components.Sidebar;
import org.openjfx.util.RequestHandler;
import org.openjfx.util.SceneSwitcher;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class EditorController implements Initializable, SceneSwitcher {
    @FXML
    FlowPane topPane;
    @FXML
    HTMLEditor htmlEditor;
    @FXML
    TextField titleField;
    @FXML
    TextArea textArea;
    @FXML
    BorderPane editorPane;
    @FXML
    Sidebar sidebar;

    DirectoryChooser directoryChooser;

    public void submit(ActionEvent event) throws IOException {
        RequestHandler requestHandler = new RequestHandler();
        Gson gson = new Gson();

        String title = titleField.getText();
        String HTML = htmlEditor.getHtmlText();
        String text = textArea.getText();

        if(text.isEmpty() && HTML.isEmpty() && title.isEmpty()){
            System.out.println("Empty fields");
            return;
        }

        Article article = new Article(title, HTML);
        Thrd thread = new Thrd(text);
        thread.setArticle(article);

        HttpResponse response = requestHandler.sendRequest("home/t/new", "POST", thread, App.loggedUser);
        Reader reader = new InputStreamReader(response.getEntity().getContent());
        Thrd threadResponse = gson.fromJson(reader, Thrd.class);
        if(threadResponse.getId() == 0){
            System.out.println("Unable to send thread");
            return;
        }
        else {

        }
    }

    public void saveDraft(ActionEvent event){
        directoryChooser = new DirectoryChooser();
        String title = titleField.getText();
        String HTML = htmlEditor.getHtmlText();
        directoryChooser.setTitle("Selecione uma pasta para salvar o seu artigo");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        FileWriter fileWriter = null;
        File directory = directoryChooser.showDialog(htmlEditor.getScene().getWindow());
        File htmlFile = new File(directory.getAbsolutePath() + File.separator + title + ".html");
        try {
            fileWriter = new FileWriter(htmlFile);
            fileWriter.write(HTML);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        topPane.prefWidthProperty().bind(editorPane.widthProperty());
        topPane.prefHeightProperty().bind(editorPane.heightProperty().divide(20));

        sidebar.prefWidthProperty().bind(editorPane.widthProperty().divide(7));
        sidebar.prefHeightProperty().bind(editorPane.heightProperty());

    }
}
