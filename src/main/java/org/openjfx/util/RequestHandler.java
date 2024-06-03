package org.openjfx.util;

import com.google.gson.Gson;
import com.ydev00.model.user.User;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.jetty.http.HttpStatus;

import static spark.route.HttpMethod.post;

/**
 * Classe responsável por gerenciar requisições HTTP.
 */
public class RequestHandler {
  private String url;
  private String method;
  private Gson gson;
  private HttpClient httpClient = HttpClientBuilder.create().build();

  /**
   * Construtor padrão que inicializa a URL padrão do servidor e o objeto Gson.
   */
  public RequestHandler() {
    this.url = "http://localhost:8080/";
    this.gson = new Gson();
  }

  /**
   * Envia uma requisição HTTP para o servidor.
   *
   * @param route A rota do endpoint.
   * @param method O método HTTP (GET, POST, PUT, DELETE).
   * @param object O objeto a ser enviado na requisição.
   * @param user O usuário autenticado.
   * @param <T> O tipo do objeto a ser enviado.
   * @return A resposta HTTP.
   */
  public <T> HttpResponse sendRequest(String route, String method, T object, User user) {
    HttpResponse response = null;
    StringEntity stringEntity;

    try {
      if(object == null || route == null || method == null)
        throw new Exception("Incomplete request");

      String uri = url + route;

      System.out.println("Method:" + method);
      System.out.println("Object:" + object);
      System.out.println("URI:" + uri);

      switch (method) {
        case "GET":
          if(user == null) {
            response = getRequest(object, uri);
            break;
          }
          response = getRequest(object, uri, user);
          break;
        case "POST":
          if (user == null) {
            response = postRequest(object, uri);
            break;
          }
          response = postRequest(object, uri, user);
          break;
        case "PUT":
          response = putRequest(object, uri);
          break;
        case "DELETE":
          response = deleteRequest(object, uri, user);
          break;
        default:
          throw new Exception("Invalid method");
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
    return response;
  }

  private <T> HttpResponse getRequest(T object,String uri) throws Exception{
    HttpGet get = new HttpGet(uri);
    get.addHeader("Content-Type", "application/json");
    return httpClient.execute(get);
  }

  private <T> HttpResponse getRequest(T object, String uri, User user) throws Exception{
    HttpGet get = new HttpGet(uri);
    get.addHeader("Content-Type", "application/json");
    get.addHeader("username", user.getUsername());
    get.addHeader("auth", String.valueOf(user.getAuth()));
    get.addHeader("root", String.valueOf(user.isRoot()));
    return httpClient.execute(get);
  }

  private <T> HttpResponse postRequest(T object, String uri) throws Exception{
    HttpPost post = new HttpPost(uri);
    post.addHeader("Content-Type", "application/json");
    StringEntity stringEntity = new StringEntity(gson.toJson(object));
    post.setEntity(stringEntity);
    return httpClient.execute(post);
  }

  private <T> HttpResponse postRequest(T object, String uri, User user) throws Exception{
    HttpPost post = new HttpPost(uri);
    post.addHeader("Content-Type", "application/json");
    post.addHeader("username", user.getUsername());
    post.addHeader("auth", String.valueOf(user.getAuth()));
    post.addHeader("root", String.valueOf(user.isRoot()));
    StringEntity stringEntity = new StringEntity(gson.toJson(object));
    post.setEntity(stringEntity);

    return httpClient.execute(post);
  }

  private <T> HttpResponse putRequest(T object, String uri) throws Exception{
    HttpPut put = new HttpPut(uri);
    put.addHeader("Content-Type", "application/json");
    StringEntity stringEntity = new StringEntity(object.toString());
    put.setEntity(stringEntity);
    return httpClient.execute(put);
  }

  private <T> HttpResponse putRequest(T object, String uri, User user) throws Exception{
    HttpPut put = new HttpPut(uri);
    StringEntity stringEntity = new StringEntity(gson.toJson(object));
    put.setEntity(stringEntity);
    put.addHeader("Content-Type", "application/json");
    put.addHeader("username", user.getUsername());
    put.addHeader("auth", String.valueOf(user.getAuth()));
    put.addHeader("root", String.valueOf(user.isRoot()));
    return httpClient.execute(put);
  }

  private <T> HttpResponse deleteRequest(T obj, String uri) throws Exception{
    HttpDelete delete = new HttpDelete(uri);
    delete.addHeader("Content-Type", "application/json");
    return httpClient.execute(delete);
  }

  private <T> HttpResponse deleteRequest(T obj, String uri, User user) throws Exception{
    HttpDelete delete = new HttpDelete(uri);
    delete.addHeader("Content-Type", "application/json");
    delete.addHeader("username", user.getUsername());
    delete.addHeader("auth", String.valueOf(user.getAuth()));
    delete.addHeader("root", String.valueOf(user.isRoot()));
    return httpClient.execute(delete);
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
}