package de.codecentric.elasticsearch.challenge.api;

import org.elasticsearch.common.xcontent.XContentBuilder;

public interface MappingDefinition {

  /**
   * Provides the mapping to use for the newly created Elasticsearch index.
   * 
   * @return the mapping definition
   */
  XContentBuilder getMapping();
}
