package de.codecentric.elasticsearch.challenge.api;

public final class Names {

  /**
   * Name of the Elasticsearch index where e-mails are stored.
   */
  public static final String INDEX = "mailbox";

  /**
   * Elasticsearch type for e-mails
   */
  public static final String TYPE = "email";

  private Names() {
    super();
  }
}
