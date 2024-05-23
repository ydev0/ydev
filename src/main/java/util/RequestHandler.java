package util;

import com.ydev00.util.Message;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.jetty.http.HttpStatus;

public class RequestHandler {
  private String url;
  private String method;
  private String body;



  public RequestHandler() {
    this.url = "localhost:8080";
  }

public RequestHandler(String url) {
    this.url = url;
  }

  public RequestHandler(String url, String method, String body, String headers) {
    this.url = url;
    this.method = method;
    this.body = body;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }


  public void sendRequest(String route, String method, String body, String headers) {
    HttpClient httpClient = HttpClientBuilder.create().build();
    StringEntity stringEntity;
    HttpResponse response;

    try {
      switch (method) {
        case "GET":
          HttpGet request = new HttpGet(url + route);
          response = httpClient.execute(request);
          break;
        case "POST":
          HttpPost post = new HttpPost(url + route);
          stringEntity = new StringEntity(body);
          post.setEntity(stringEntity);
          response = httpClient.execute(post);
          break;
        case "PUT":
          HttpPut put = new HttpPut(url + route);
          stringEntity = new StringEntity(body);
          put.setEntity(stringEntity);
          response = httpClient.execute(put);
          break;
        case "DELETE":
          HttpDelete delete = new HttpDelete(url + route);
          response = httpClient.execute(delete);
          break;
        default:
          break;
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

}
