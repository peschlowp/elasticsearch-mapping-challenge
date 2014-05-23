package de.codecentric.elasticsearch.challenge.api.solution;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import de.codecentric.elasticsearch.challenge.Email;
import de.codecentric.elasticsearch.challenge.EmailHit;
import de.codecentric.elasticsearch.challenge.EmailSearchResult;
import de.codecentric.elasticsearch.challenge.util.Utils;

public class SolutionResultHelper {

  public static EmailSearchResult transformResult(SearchHits hits) {
    List<EmailHit> emails = new ArrayList<>(hits.getHits().length);
    for (SearchHit hit : hits) {
      emails.add(new EmailHit(hit.getScore(), sourceToEmail(hit.getSource())));
    }
    return new EmailSearchResult(hits.getTotalHits(), emails);
  }

  @SuppressWarnings("unchecked")
  private static Email sourceToEmail(Map<String, Object> source) {
    String id = (String) source.get(SolutionConstants.ID);
    String sender = (String) source.get(SolutionConstants.SENDER);
    List<String> recipients = (List<String>) source.get(SolutionConstants.RECIPIENTS);
    Date timestamp = Utils.fromISO8601((String) source.get(SolutionConstants.TIMESTAMP));
    String subject = (String) source.get(SolutionConstants.SUBJECT);
    String text = (String) source.get(SolutionConstants.TEXT);
    List<String> labels = (List<String>) source.get(SolutionConstants.LABELS);
    return new Email(id, sender, recipients, timestamp, subject, text, labels);
  }
}
