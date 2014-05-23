package de.codecentric.elasticsearch.challenge.api.solution;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;

import de.codecentric.elasticsearch.challenge.Email;
import de.codecentric.elasticsearch.challenge.EmailSearchQuery;
import de.codecentric.elasticsearch.challenge.EmailSearchResult;
import de.codecentric.elasticsearch.challenge.api.AbstractSearchFacade;
import de.codecentric.elasticsearch.challenge.api.Names;

public class SolutionSearchFacade extends AbstractSearchFacade {

  public SolutionSearchFacade(Client client) {
    super(client);
  }

  @Override
  public void index(Email email) {
    getClient().prepareIndex(Names.INDEX, Names.TYPE, email.getId())
        .setSource(SolutionIndexHelper.emailToIndexJson(email)).execute().actionGet();
  }

  @Override
  public EmailSearchResult search(EmailSearchQuery query) {
    SearchResponse response =
        getClient().prepareSearch(Names.INDEX).setTypes(Names.TYPE).setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
            .setQuery(SolutionQueryHelper.emailToSearchQuery(query)).execute().actionGet();
    return SolutionResultHelper.transformResult(response.getHits());
  }
}
