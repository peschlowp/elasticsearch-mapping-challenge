package de.codecentric.elasticsearch.challenge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import de.codecentric.elasticsearch.challenge.api.MappingDefinition;
import de.codecentric.elasticsearch.challenge.api.Names;
import de.codecentric.elasticsearch.challenge.api.SearchFacade;
import de.codecentric.elasticsearch.challenge.api.incomplete.IncompleteMappingDefinition;
import de.codecentric.elasticsearch.challenge.api.incomplete.IncompleteSearchFacade;
import de.codecentric.elasticsearch.challenge.node.ElasticsearchTestNode;

public class MappingChallengeTest {

  private static MappingDefinition mappingDefinition;
  private static SearchFacade searchFacade;

  @ClassRule
  public static ElasticsearchTestNode testNode = new ElasticsearchTestNode();

  @BeforeClass
  public static void setup() {
    createMappingDefinition();
    createSearchFacade();
    try {
      createIndex();
      indexTestDocuments();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  private static void createMappingDefinition() {
    // TODO Use your own MappingDefinition implementation here.
    mappingDefinition = new IncompleteMappingDefinition();
  }

  private static void createSearchFacade() {
    // TODO Use your own SearchFacade implementation here.
    searchFacade = new IncompleteSearchFacade(getClient());
  }

  private static void createIndex() throws IOException {
    getClient().admin().indices().create(new CreateIndexRequest(Names.INDEX)).actionGet();
    getClient().admin().indices().preparePutMapping(Names.INDEX).setType(Names.TYPE)
        .setSource(mappingDefinition.getMapping()).execute().actionGet();
  }

  private static void indexTestDocuments() throws IOException {
    searchFacade.index(createEmail1());
    searchFacade.index(createEmail2());
    getClient().admin().indices().refresh(new RefreshRequest(Names.INDEX)).actionGet();
  }

  private static Email createEmail1() {
    String id = "1";
    String sender = "patrick.peschlow@codecentric.de";
    List<String> recipients =
        Arrays.asList("christian.uhl@codecentric.de", "dennis.probst@codecentric.de", "raimar.falke@codecentric.de",
            "rene.mjartan@codecentric.de");
    Date timestamp = new DateTime().withYear(2014).withMonthOfYear(4).withDayOfMonth(9).toDate();
    String subject = "Gute Bücher";
    String text =
        "Ich habe ein paar interessante Bücher gefunden: \"Eventual Consistent Resilience\" und \"Don't Search - Find!\". Schaut doch mal rein.";
    List<String> labels = Arrays.asList("Important", "Elasticsearch");
    return new Email(id, sender, recipients, timestamp, subject, text, labels);
  }

  private static Email createEmail2() {
    String id = "2";
    String sender = "patrick.peschlow@codecentric.de";
    List<String> recipients = Arrays.asList("patrick.peschlow@codecentric.de");
    Date timestamp = new DateTime().withYear(2014).withMonthOfYear(4).withDayOfMonth(10).toDate();
    String subject = "Reminder";
    String text = "Hangout um 15 Uhr!";
    List<String> labels = Collections.emptyList();
    return new Email(id, sender, recipients, timestamp, subject, text, labels);
  }

  private static Client getClient() {
    return testNode.getClient();
  }

  private void verifyMatches(EmailSearchResult result, String... ids) {
    if (ids == null || ids.length == 0) {
      assertEquals(0, result.getTotal());
      assertTrue(result.getHits().isEmpty());
    } else {
      assertEquals(ids.length, result.getTotal());
      assertEquals(ids.length, result.getHits().size());
      List<String> expectedIds = Arrays.asList(ids);
      for (EmailHit hit : result.getHits()) {
        assertTrue(expectedIds.contains(hit.getEmail().getId()));
      }
    }
  }

  private void verifyNoMatches(EmailSearchResult result) {
    verifyMatches(result);
  }

  @Test
  public void shouldMatchById() {
    EmailSearchQuery query = EmailSearchQueryBuilder.start().id("1").build();

    EmailSearchResult result = searchFacade.search(query);

    verifyMatches(result, "1");
  }

  @Test
  public void shouldHaveEmptyResultForUnknownId() {
    EmailSearchQuery query = EmailSearchQueryBuilder.start().id("unknownId").build();

    EmailSearchResult result = searchFacade.search(query);

    verifyNoMatches(result);
  }

  @Test
  public void shouldMatchBySenderAddress() {
    EmailSearchQuery query = EmailSearchQueryBuilder.start().sender("patrick.peschlow@codecentric.de").build();

    EmailSearchResult result = searchFacade.search(query);

    verifyMatches(result, "1", "2");
  }

  @Test
  public void shouldMatchByAnyPrefixOfSenderAddress_ExampleWithOneLetter() {
    EmailSearchQuery query = EmailSearchQueryBuilder.start().sender("p").build();

    EmailSearchResult result = searchFacade.search(query);

    verifyMatches(result, "1", "2");
  }

  @Test
  public void shouldMatchByAnyPrefixOfSenderAddress_ExampleWithTenLetters() {
    EmailSearchQuery query = EmailSearchQueryBuilder.start().sender("patrick.pe").build();

    EmailSearchResult result = searchFacade.search(query);

    verifyMatches(result, "1", "2");
  }

  @Test
  public void shouldMatchByAnyPrefixOfSenderAddress_ExampleWithAllButOneLetters() {
    EmailSearchQuery query = EmailSearchQueryBuilder.start().sender("patrick.peschlow@codecentric.d").build();

    EmailSearchResult result = searchFacade.search(query);

    verifyMatches(result, "1", "2");
  }

  @Test
  public void shouldHaveEmptyResultForUnknownSenderAddress() {
    EmailSearchQuery query = EmailSearchQueryBuilder.start().sender("unknown@codecentric.de").build();

    EmailSearchResult result = searchFacade.search(query);

    verifyNoMatches(result);
  }

  @Test
  public void shouldMatchByRecipientAddress() {
    EmailSearchQuery query = EmailSearchQueryBuilder.start().recipient("christian.uhl@codecentric.de").build();

    EmailSearchResult result = searchFacade.search(query);

    verifyMatches(result, "1");
  }

  @Test
  public void shouldMatchByAnotherRecipientAddress() {
    EmailSearchQuery query = EmailSearchQueryBuilder.start().recipient("dennis.probst@codecentric.de").build();

    EmailSearchResult result = searchFacade.search(query);

    verifyMatches(result, "1");
  }

  @Test
  public void shouldMatchByAnyPrefixOfRecipientAddress_ExampleWithOneLetter() {
    EmailSearchQuery query = EmailSearchQueryBuilder.start().recipient("r").build();

    EmailSearchResult result = searchFacade.search(query);

    verifyMatches(result, "1");
  }

  @Test
  public void shouldMatchByAnyPrefixOfRecipientAddress_ExampleWithTenLetters() {
    EmailSearchQuery query = EmailSearchQueryBuilder.start().recipient("raimar.fal").build();

    EmailSearchResult result = searchFacade.search(query);

    verifyMatches(result, "1");
  }

  @Test
  public void shouldMatchByAnyPrefixOfRecipientAddress_ExampleWithAllButOneLetters() {
    EmailSearchQuery query = EmailSearchQueryBuilder.start().recipient("raimar.falke@codecentric.d").build();

    EmailSearchResult result = searchFacade.search(query);

    verifyMatches(result, "1");
  }

  @Test
  public void shouldHaveEmptyResultForUnknownRecipientAddress() {
    EmailSearchQuery query = EmailSearchQueryBuilder.start().recipient("unknown@codecentric.de").build();

    EmailSearchResult result = searchFacade.search(query);

    verifyNoMatches(result);
  }

  @Test
  public void shouldMatchByLowerTimestampBound() {
    EmailSearchQuery query =
        EmailSearchQueryBuilder.start()
            .fromTimestamp(new DateTime().withYear(2014).withMonthOfYear(4).withDayOfMonth(1).toDate()).build();

    EmailSearchResult result = searchFacade.search(query);

    verifyMatches(result, "1", "2");
  }

  @Test
  public void shouldMatchByUpperTimestampBound() {
    EmailSearchQuery query =
        EmailSearchQueryBuilder.start()
            .toTimestamp(new DateTime().withYear(2014).withMonthOfYear(4).withDayOfMonth(30).toDate()).build();

    EmailSearchResult result = searchFacade.search(query);

    verifyMatches(result, "1", "2");
  }

  @Test
  public void shouldNotMatchIfLowerTimestampBoundIsTooLarge() {
    EmailSearchQuery query =
        EmailSearchQueryBuilder.start()
            .fromTimestamp(new DateTime().withYear(2014).withMonthOfYear(4).withDayOfMonth(30).toDate()).build();

    EmailSearchResult result = searchFacade.search(query);

    verifyNoMatches(result);
  }

  @Test
  public void shouldNotMatchIfLowerTimestampBoundIsTooLow() {
    EmailSearchQuery query =
        EmailSearchQueryBuilder.start()
            .toTimestamp(new DateTime().withYear(2014).withMonthOfYear(4).withDayOfMonth(1).toDate()).build();

    EmailSearchResult result = searchFacade.search(query);

    verifyNoMatches(result);
  }

  @Test
  public void shouldMatchByLowerAndUpperTimestampBounds() {
    EmailSearchQuery query =
        EmailSearchQueryBuilder.start()
            .fromTimestamp(new DateTime().withYear(2014).withMonthOfYear(4).withDayOfMonth(1).toDate())
            .toTimestamp(new DateTime().withYear(2014).withMonthOfYear(4).withDayOfMonth(30).toDate()).build();

    EmailSearchResult result = searchFacade.search(query);

    verifyMatches(result, "1", "2");
  }

  @Test
  public void shouldMatchBySingleLabel() {
    EmailSearchQuery query = EmailSearchQueryBuilder.start().labels(Arrays.asList("Important")).build();

    EmailSearchResult result = searchFacade.search(query);

    verifyMatches(result, "1");
  }

  @Test
  public void shouldMatchByMultipleLabels() {
    EmailSearchQuery query =
        EmailSearchQueryBuilder.start().labels(Arrays.asList("Important", "Elasticsearch")).build();

    EmailSearchResult result = searchFacade.search(query);

    verifyMatches(result, "1");
  }

  @Test
  public void shouldNotMatchLabelWhenCaseDiffers() {
    EmailSearchQuery query = EmailSearchQueryBuilder.start().labels(Arrays.asList("elasticsearch")).build();

    EmailSearchResult result = searchFacade.search(query);

    verifyNoMatches(result);
  }

  @Test
  public void shouldHaveEmptyResultForUnknownLabel() {
    EmailSearchQuery query = EmailSearchQueryBuilder.start().labels(Arrays.asList("unknownLabel")).build();

    EmailSearchResult result = searchFacade.search(query);

    verifyNoMatches(result);
  }

  @Test
  public void shouldNotMatchOnlySomeLabels() {
    EmailSearchQuery query =
        EmailSearchQueryBuilder.start().labels(Arrays.asList("Elasticsearch unknownLabel")).build();

    EmailSearchResult result = searchFacade.search(query);

    verifyNoMatches(result);
  }

  @Test
  public void shouldMatchSingleWordInSubject() {
    EmailSearchQuery query = EmailSearchQueryBuilder.start().subject("Buch").build();

    EmailSearchResult result = searchFacade.search(query);

    verifyMatches(result, "1");
  }

  @Test
  public void shouldMatchMultipleWordsInSubject() {
    EmailSearchQuery query = EmailSearchQueryBuilder.start().subject("Buch gut").build();

    EmailSearchResult result = searchFacade.search(query);

    verifyMatches(result, "1");
  }

  @Test
  public void shouldNotMatchOnlySomeWordsInSubject() {
    EmailSearchQuery query = EmailSearchQueryBuilder.start().subject("Buch gut preiswert").build();

    EmailSearchResult result = searchFacade.search(query);

    verifyNoMatches(result);
  }

  @Test
  public void shouldNotMatchAnArbitraryPrefixOfSomeSubjectWord() {
    EmailSearchQuery query = EmailSearchQueryBuilder.start().subject("buc").build();

    EmailSearchResult result = searchFacade.search(query);

    verifyNoMatches(result);
  }

  @Test
  public void shouldMatchSingleWordInText() {
    EmailSearchQuery query = EmailSearchQueryBuilder.start().text("Buch").build();

    EmailSearchResult result = searchFacade.search(query);

    verifyMatches(result, "1");
  }

  @Test
  public void shouldMatchMultipleWordsInText() {
    EmailSearchQuery query = EmailSearchQueryBuilder.start().text("buch interessant").build();

    EmailSearchResult result = searchFacade.search(query);

    verifyMatches(result, "1");
  }

  @Test
  public void shouldNotMatchOnlySomeWordsInText() {
    EmailSearchQuery query = EmailSearchQueryBuilder.start().text("buch interessant nosql").build();

    EmailSearchResult result = searchFacade.search(query);

    verifyNoMatches(result);
  }

  @Test
  public void shouldMatchMultipleLanguagesInText() {
    EmailSearchQuery query = EmailSearchQueryBuilder.start().text("buch consistency").build();

    EmailSearchResult result = searchFacade.search(query);

    verifyMatches(result, "1");
  }

  @Test
  public void shouldNotMatchAnArbitraryPrefixOfSomeTextWord() {
    EmailSearchQuery query = EmailSearchQueryBuilder.start().text("buc").build();

    EmailSearchResult result = searchFacade.search(query);

    verifyNoMatches(result);
  }

  @Test
  public void shouldMatchIfRecipientsContainSender() {
    EmailSearchQuery query = EmailSearchQueryBuilder.start().named(NamedQuery.RECIPIENTS_CONTAIN_SENDER).build();

    EmailSearchResult result = searchFacade.search(query);

    verifyMatches(result, "2");
  }
}
