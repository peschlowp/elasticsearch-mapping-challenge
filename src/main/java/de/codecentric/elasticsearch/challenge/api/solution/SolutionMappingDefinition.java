package de.codecentric.elasticsearch.challenge.api.solution;

import java.io.IOException;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import de.codecentric.elasticsearch.challenge.api.MappingDefinition;
import de.codecentric.elasticsearch.challenge.api.Names;

public class SolutionMappingDefinition implements MappingDefinition {

  @Override
  public XContentBuilder getMapping() {
    try {
      XContentBuilder propertiesObj =
          XContentFactory.jsonBuilder().startObject().startObject(Names.TYPE).startObject("_all")
              .field("enabled", "false").endObject().startObject("properties");
      addIdField(propertiesObj);
      addSenderField(propertiesObj);
      addRecipientField(propertiesObj);
      addTimestampField(propertiesObj);
      addSubjectField(propertiesObj);
      addTextField(propertiesObj);
      addLabelsField(propertiesObj);
      addRecipientsContainSenderField(propertiesObj);
      return propertiesObj.endObject().endObject().endObject();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  private XContentBuilder addIdField(XContentBuilder propertiesObj) throws IOException {
    return propertiesObj.startObject(SolutionConstants.ID).field("type", "string").field("index", "not_analyzed")
        .endObject();
  }

  private XContentBuilder addSenderField(XContentBuilder propertiesObj) throws IOException {
    return propertiesObj.startObject(SolutionConstants.SENDER).field("type", "string").field("index", "not_analyzed")
        .endObject();
  }

  private XContentBuilder addRecipientField(XContentBuilder propertiesObj) throws IOException {
    return propertiesObj.startObject(SolutionConstants.RECIPIENTS).field("type", "string")
        .field("index", "not_analyzed").endObject();
  }

  private XContentBuilder addTimestampField(XContentBuilder propertiesObj) throws IOException {
    return propertiesObj.startObject(SolutionConstants.TIMESTAMP).field("type", "date").field("index", "not_analyzed")
        .endObject();
  }

  private XContentBuilder addSubjectField(XContentBuilder propertiesObj) throws IOException {
    return propertiesObj.startObject(SolutionConstants.SUBJECT).field("type", "string").field("index", "analyzed")
        .field("analyzer", "german").endObject();
  }

  private XContentBuilder addTextField(XContentBuilder propertiesObj) throws IOException {
    return propertiesObj.startObject(SolutionConstants.TEXT).field("type", "string").field("index", "analyzed")
        .field("analyzer", "german").field("fields").startObject().field("en").startObject().field("type", "string")
        .field("index", "analyzed").field("analyzer", "english").endObject().endObject().endObject();
  }

  private XContentBuilder addLabelsField(XContentBuilder propertiesObj) throws IOException {
    return propertiesObj.startObject(SolutionConstants.LABELS).field("type", "string").field("index", "not_analyzed")
        .endObject();
  }

  private XContentBuilder addRecipientsContainSenderField(XContentBuilder propertiesObj) throws IOException {
    return propertiesObj.startObject(SolutionConstants.RECIPIENTS_CONTAIN_SENDER).field("type", "boolean")
        .field("index", "not_analyzed").endObject();
  }
}
