package de.codecentric.elasticsearch.challenge;

import java.util.Date;
import java.util.List;

public class EmailSearchQuery {

  /**
   * Exact match on e-mail ID.
   */
  private final String id;

  /**
   * Exact match on e-mail sender address.
   */
  private final String sender;

  /**
   * Exact match on one of the e-mail recipient addresses.
   */
  private final String recipient;

  /**
   * Lower bound (inclusive) on e-mail timestamp.
   */
  private final Date fromTimestamp;

  /**
   * Upper bound (inclusive) on e-mail timestamp.
   */
  private final Date toTimestamp;

  /**
   * Fulltext match in e-mail subject.
   */
  private final String subject;

  /**
   * Fulltext match in e-mail text.
   */
  private final String text;

  /**
   * Exact match on all of the provided e-mail labels.
   */
  private final List<String> labels;

  public EmailSearchQuery(String id, String sender, String recipient, Date fromTimestamp, Date toTimestamp,
      String subject, String text, List<String> labels) {
    this.id = id;
    this.sender = sender;
    this.recipient = recipient;
    this.fromTimestamp = fromTimestamp;
    this.toTimestamp = toTimestamp;
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

  public String getRecipient() {
    return recipient;
  }

  public Date getFromTimestamp() {
    return fromTimestamp;
  }

  public Date getToTimestamp() {
    return toTimestamp;
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
    return "EmailSearchQuery [id=" + id + ", sender=" + sender + ", recipient=" + recipient + ", fromTimestamp="
        + fromTimestamp + ", toTimestamp=" + toTimestamp + ", subject=" + subject + ", text=" + text + ", labels="
        + labels + "]";
  }
}
