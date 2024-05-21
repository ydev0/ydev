package org.openjfx;
import com.google.gson.Gson;
import com.ydev00.model.thread.Article;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditorController implements Initializable {
    @FXML
    HTMLEditor htmlEditor;
    @FXML
    TextField titleField;

    DirectoryChooser directoryChooser;

    public void submit(ActionEvent event) throws IOException {
        String title = titleField.getText();
        String HTML = htmlEditor.getHtmlText();
        Article article = new Article(title, HTML);

        Gson gson = new Gson();
        HttpClient httpClient = HttpClientBuilder.create().build();
        StringEntity stringEntity = new StringEntity(gson.toJson(article));
        HttpPost request = new HttpPost("http://localhost:8080/home/t/new");
        request.setEntity(stringEntity);
        HttpResponse response = httpClient.execute(request);
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

    }
}
