package de.codecentric.elasticsearch.challenge.api.incomplete;

import java.io.IOException;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import de.codecentric.elasticsearch.challenge.api.MappingDefinition;
import de.codecentric.elasticsearch.challenge.api.Names;

public class IncompleteMappingDefinition implements MappingDefinition {

  @Override
  public XContentBuilder getMapping() {
    try {
      // Returns a dummy mapping.
      return XContentFactory.jsonBuilder().startObject().startObject(Names.TYPE).startObject("properties")
          .startObject("dummyfield").field("type", "string").endObject().endObject().endObject().endObject();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}
