package org.openjfx.model;

import com.google.gson.annotations.SerializedName;

public class User {
  @SerializedName("id")
  private int id;

  @SerializedName("name")
  private String name;   
  
  @SerializedName("username")
  private String username;

  @SerializedName("email")
  private String email;

  @SerializedName("password")
  private String password; 

  @SerializedName("profilePic")
  private Image profilePic;

  @SerializedName("auth")
  private boolean auth;

  public User() {
  }

  public User(int id, String name, String username, String email, String password, Image profilePic) {
    this.id = id;
    this.name = name;
    this.username = username;
    this.email = email;
    this.password = password;
    this.profilePic = profilePic;
    this.auth = false;
  }

  public User(String email, String password) {
    this.email = email;
    this.password = password;
    this.auth = false;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setUsername(String userName) {
    this.username = userName;
  }

  public void setPassword(String password) {
    this.password = password;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }

  public void setProfilePic(Image pfp) {
    this.profilePic = pfp;
  }

  public void setAuth(boolean auth) {
    this.auth = auth;
  }

  public int getId() {
    return id;
  }

  public Image getProfilePic() {
    return profilePic;
  }

  public String getName() {
    return name;
  }

  public String getUsername(){
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }


  public String toString() {
    return "User{" +
    "id=" + id + 
    ", username='" + username + '\'' +
    ", name='" + name + '\'' +
    ", email='" + email + '\'' +
    ", password='" + password + '\'' +
    ", pfp= '" +profilePic + '\'' +
    ", auth= '" +auth+ '\'' +
    '}';
  }}
