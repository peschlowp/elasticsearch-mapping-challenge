package de.codecentric.elasticsearch.challenge;

import java.util.Date;
import java.util.List;

public class Email {

  private final String id;
  private final String sender;
  private final List<String> recipients;
  private final Date timestamp;
  private final String subject;
  private final String text;
  private final List<String> labels;

  public Email(String id, String sender, List<String> recipients, Date timestamp, String subject, String text,
      List<String> labels) {
    this.id = id;
    this.sender = sender;
    this.recipients = recipients;
    this.timestamp = timestamp;
    this.subject = subject;
    this.text = text;
    this.labels = labels;
  }

  public String getId() {
    return id;
  }

  public String getSender() {
    return sender;
  }

  public List<String> getRecipients() {
    return recipients;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public String getSubject() {
    return subject;
  }

  public String getText() {
    return text;
  }

  public List<String> getLabels() {
    return labels;
  }

  @Override
  public String toString() {
    return "Email [id=" + id + ", sender=" + sender + ", recipients=" + recipients + ", timestamp=" + timestamp
        + ", subject=" + subject + ", text=" + text + ", labels=" + labels + "]";
  }
}
