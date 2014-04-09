package de.codecentric.elasticsearch.challenge;

import java.util.List;

public class EmailSearchResult {

  private final long total;
  private final List<EmailHit> hits;

  public EmailSearchResult(long total, List<EmailHit> hits) {
    this.total = total;
    this.hits = hits;
  }

  public long getTotal() {
    return total;
  }

  public List<EmailHit> getHits() {
    return hits;
  }

  @Override
  public String toString() {
    return "EmailSearchResult [total=" + total + ", hits=" + hits + "]";
  }
}
