package de.codecentric.elasticsearch.challenge.api;

import org.elasticsearch.client.Client;

public abstract class AbstractSearchFacade implements SearchFacade {

  private final Client client;

  public AbstractSearchFacade(Client client) {
    this.client = client;
  }

  protected Client getClient() {
    return client;
  }
}
