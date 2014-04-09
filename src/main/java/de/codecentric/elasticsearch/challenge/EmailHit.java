package de.codecentric.elasticsearch.challenge;

public class EmailHit {

  private final double score;
  private final Email email;

  public EmailHit(double score, Email email) {
    this.score = score;
    this.email = email;
  }

  public double getScore() {
    return score;
  }

  public Email getEmail() {
    return email;
  }

  @Override
  public String toString() {
    return "EmailHit [score=" + score + ", email=" + email + "]";
  }
}
