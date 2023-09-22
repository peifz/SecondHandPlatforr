package com.example.secondhandplatforms;

public class User {

      public User(){

      }
       private String  fromUserId;
       private String  username;
       private String  unReadNum;

       public String getFromUserId() {
              return fromUserId;
       }

       public void setFromUserId(String fromUserId) {
              this.fromUserId = fromUserId;
       }

       public String getUsername() {
              return username;
       }

       public void setUsername(String username) {
              this.username = username;
       }

       public String getUnReadNum() {
              return unReadNum;
       }

       public void setUnReadNum(String unReadNum) {
              this.unReadNum = unReadNum;
       }

       public User(String fromUserId, String username, String unReadNum) {
              this.fromUserId = fromUserId;
              this.username = username;
              this.unReadNum = unReadNum;
       }
}
