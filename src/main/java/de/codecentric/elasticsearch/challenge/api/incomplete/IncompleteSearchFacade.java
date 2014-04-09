package de.codecentric.elasticsearch.challenge.api.incomplete;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import de.codecentric.elasticsearch.challenge.Email;
import de.codecentric.elasticsearch.challenge.EmailHit;
import de.codecentric.elasticsearch.challenge.EmailSearchQuery;
import de.codecentric.elasticsearch.challenge.EmailSearchResult;
import de.codecentric.elasticsearch.challenge.api.AbstractSearchFacade;
import de.codecentric.elasticsearch.challenge.api.Names;

public class IncompleteSearchFacade extends AbstractSearchFacade {

  public IncompleteSearchFacade(Client client) {
    super(client);
  }

  @Override
  public void index(Email email) {
    emailToIndexJson(email);
    getClient().prepareIndex(Names.INDEX, Names.TYPE, email.getId()).setSource(emailToIndexJson(email)).execute()
        .actionGet();
  }

  private XContentBuilder emailToIndexJson(Email email) {
    try {
      return XContentFactory.jsonBuilder().startObject().field("id", email.getId()).endObject();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public EmailSearchResult search(EmailSearchQuery query) {
    SearchResponse response =
        getClient().prepareSearch(Names.INDEX).setTypes(Names.TYPE).setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
            .setQuery(emailToSearchQuery(query)).execute().actionGet();
    return transformResult(response.getHits());
  }

  private QueryBuilder emailToSearchQuery(EmailSearchQuery query) {
    return QueryBuilders.termQuery("id", query.getId());
  }

  private EmailSearchResult transformResult(SearchHits hits) {
    List<EmailHit> emails = new ArrayList<>(hits.getHits().length);
    for (SearchHit hit : hits) {
      emails.add(new EmailHit(hit.getScore(), sourceToEmail(hit.getSource())));
    }
    return new EmailSearchResult(hits.getTotalHits(), emails);
  }

  private Email sourceToEmail(Map<String, Object> source) {
    return new Email((String) source.get("id"), null, null, null, null, null, null);
  }
}
