package de.codecentric.elasticsearch.challenge.api;

import de.codecentric.elasticsearch.challenge.Email;
import de.codecentric.elasticsearch.challenge.EmailSearchQuery;
import de.codecentric.elasticsearch.challenge.EmailSearchResult;

public interface SearchFacade {

  /**
   * Indexes the provided e-mail in Elasticsearch. Does not need to refresh the index, the tests will take care of it
   * themselves.
   * 
   * @param email
   *          the e-mail to index
   */
  void index(Email email);

  /**
   * Searches for e-mails based on the contents of the provided query object.
   * 
   * @param query
   *          the query object specifying the requirements for e-mails to match
   * @return a result object containing the search result
   */
  EmailSearchResult search(EmailSearchQuery query);
}
