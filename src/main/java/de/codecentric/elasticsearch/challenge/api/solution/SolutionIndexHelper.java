package de.codecentric.elasticsearch.challenge.api.solution;

import java.io.IOException;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import de.codecentric.elasticsearch.challenge.Email;

public final class SolutionIndexHelper {

  public static XContentBuilder emailToIndexJson(Email email) {
    try {
      XContentBuilder indexJson = XContentFactory.jsonBuilder().startObject();
      addIdField(email, indexJson);
      addSenderField(email, indexJson);
      addRecipientField(email, indexJson);
      addTimestampField(email, indexJson);
      addSubjectField(email, indexJson);
      addTextField(email, indexJson);
      addLabelsField(email, indexJson);
      addRecipientsContainSenderField(email, indexJson);
      return indexJson.endObject();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  private static XContentBuilder addIdField(Email email, XContentBuilder indexJson) throws IOException {
    return indexJson.field(SolutionConstants.ID, email.getId());
  }

  private static XContentBuilder addSenderField(Email email, XContentBuilder indexJson) throws IOException {
    return indexJson.field(SolutionConstants.SENDER, email.getSender());
  }

  private static XContentBuilder addRecipientField(Email email, XContentBuilder indexJson) throws IOException {
    return indexJson.field(SolutionConstants.RECIPIENTS, email.getRecipients());
  }

  private static XContentBuilder addTimestampField(Email email, XContentBuilder indexJson) throws IOException {
    return indexJson.field(SolutionConstants.TIMESTAMP, email.getTimestamp());
  }

  private static XContentBuilder addSubjectField(Email email, XContentBuilder indexJson) throws IOException {
    return indexJson.field(SolutionConstants.SUBJECT, email.getSubject());
  }

  private static XContentBuilder addTextField(Email email, XContentBuilder indexJson) throws IOException {
    return indexJson.field(SolutionConstants.TEXT, email.getText());
  }

  private static XContentBuilder addLabelsField(Email email, XContentBuilder indexJson) throws IOException {
    if (email.getLabels() == null) {
      return indexJson;
    }
    return indexJson.field(SolutionConstants.LABELS, email.getLabels());
  }

  private static XContentBuilder addRecipientsContainSenderField(Email email, XContentBuilder indexJson)
      throws IOException {
    return indexJson.field(SolutionConstants.RECIPIENTS_CONTAIN_SENDER,
        email.getRecipients().contains(email.getSender()));
  }

  private SolutionIndexHelper() {
    super();
  }
}
