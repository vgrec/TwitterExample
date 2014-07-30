package org.codelearn.twitter.models;
import java.io.Serializable;

public class Tweet implements Serializable {
	private String id;
    private String title;
    private String body;
    private static final long serialVersionUID = 1L;

	public Tweet(String id, String title, String body) {
		this.id = id;
		this.title = title;
		this.body = body;
	}

	public Tweet() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}