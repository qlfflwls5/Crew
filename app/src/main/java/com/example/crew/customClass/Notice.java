package com.example.crew.customClass;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Notice {
    private String subject;
    private String content;
    private String date;
    private int replyCount;


    public Notice(String subject, String content, String date, int replyCount){
        this.subject = subject;
        this.content = content;
        this.date = date;
        this.replyCount = replyCount;
    }

    public String getSubject(){
        return this.subject;
    }
    public void setSubject(String name) {
        this.subject = name;
    }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getDate() { return date;  }
    public void setDate(String date) { this.date = date;  }

    public int getReplyCount() { return replyCount;  }
    public void setReplyCount(int replyCount) { this.replyCount = replyCount;  }
}
