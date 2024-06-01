package org.openjfx;
import com.google.gson.Gson;
import com.ydev00.model.thread.Article;
import com.ydev00.model.thread.Thrd;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.DirectoryChooser;
import javafx.event.ActionEvent;
import org.apache.http.HttpResponse;
import org.openjfx.components.Sidebar;
import org.openjfx.util.RequestHandler;
import org.openjfx.util.SceneSwitcher;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class EditorController implements Initializable, SceneSwitcher {
    @FXML
    private FlowPane topPane;
    @FXML
    private HTMLEditor htmlEditor;
    @FXML
    private TextField titleField;
    @FXML
    private TextArea textArea;
    @FXML
    private BorderPane editorPane;
    @FXML
    private Sidebar sidebar;
    private DirectoryChooser directoryChooser;

    private RequestHandler requestHandler = new RequestHandler();
    private Gson gson = new Gson();

    public void submit(ActionEvent event) throws IOException {

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

        HttpResponse response = requestHandler.sendRequest("t/new", "POST", thread, App.loggedUser);
        Reader reader = new InputStreamReader(response.getEntity().getContent());
        Thrd threadResponse = gson.fromJson(reader, Thrd.class);

        if(threadResponse.getId() == 0){
            System.out.println("Unable to send thread");
            return;
        }

        htmlEditor.setHtmlText("");
        textArea.setText("");
        titleField.setText("");
    }

    public void saveDraft(ActionEvent event) throws IOException {
        directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Selecione uma pasta para salvar o seu artigo");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        String title = titleField.getText();
        String HTML = htmlEditor.getHtmlText();
        FileWriter fileWriter;

        File directory = directoryChooser.showDialog(htmlEditor.getScene().getWindow());
        File htmlFile = new File(directory.getAbsolutePath() + File.separator + title + ".html");

        fileWriter = new FileWriter(htmlFile);
        fileWriter.write(HTML);
        fileWriter.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        topPane.prefWidthProperty().bind(editorPane.widthProperty());
        topPane.prefHeightProperty().bind(editorPane.heightProperty().divide(20));

        sidebar.prefWidthProperty().bind(editorPane.widthProperty().divide(7));
        sidebar.prefHeightProperty().bind(editorPane.heightProperty());

        htmlEditor.prefWidthProperty().bind(editorPane.widthProperty().divide(1.5));
        titleField.prefWidthProperty().bind(editorPane.widthProperty().divide(1.5));
        textArea.prefWidthProperty().bind(editorPane.widthProperty().divide(1.5));
    }
}
