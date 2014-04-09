package de.codecentric.elasticsearch.challenge;

import java.util.Date;
import java.util.List;

public class EmailSearchQueryBuilder {

  public static EmailSearchQueryBuilder start() {
    return new EmailSearchQueryBuilder();
  }

  private String id;
  private String sender;
  private String recipient;
  private Date fromTimestamp;
  private Date toTimestamp;
  private String subject;
  private String text;
  private List<String> labels;

  public EmailSearchQuery build() {
    return new EmailSearchQuery(id, sender, recipient, fromTimestamp, toTimestamp, subject, text, labels);
  }

  public EmailSearchQueryBuilder id(String id) {
    this.id = id;
    return this;
  }

  public EmailSearchQueryBuilder sender(String sender) {
    this.sender = sender;
    return this;
  }

  public EmailSearchQueryBuilder recipient(String recipient) {
    this.recipient = recipient;
    return this;
  }

  public EmailSearchQueryBuilder fromTimestamp(Date fromTimestamp) {
    this.fromTimestamp = fromTimestamp;
    return this;
  }

  public EmailSearchQueryBuilder toTimestamp(Date toTimestamp) {
    this.toTimestamp = toTimestamp;
    return this;
  }

  public EmailSearchQueryBuilder subject(String subject) {
    this.subject = subject;
    return this;
  }

  public EmailSearchQueryBuilder text(String text) {
    this.text = text;
    return this;
  }

  public EmailSearchQueryBuilder labels(List<String> labels) {
    this.labels = labels;
    return this;
  }

  @Override
  public String toString() {
    return "EmailSearchQueryBuilder [id=" + id + ", sender=" + sender + ", recipient=" + recipient + ", fromTimestamp="
        + fromTimestamp + ", toTimestamp=" + toTimestamp + ", subject=" + subject + ", text=" + text + ", labels="
        + labels + "]";
  }
}
