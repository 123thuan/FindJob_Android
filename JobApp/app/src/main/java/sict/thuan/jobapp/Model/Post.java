package sict.thuan.jobapp.Model;

public class Post {
    private String postid;
    private String postimage;
    private String name;
    private String state;
    private String request;
    private String description;
    private String publisher;

    public Post(String postid, String postimage, String name, String state, String request, String description, String publisher) {
        this.postid = postid;
        this.postimage = postimage;
        this.name = name;
        this.state = state;
        this.request = request;
        this.description = description;
        this.publisher = publisher;
    }



    public Post() {
    }
    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
