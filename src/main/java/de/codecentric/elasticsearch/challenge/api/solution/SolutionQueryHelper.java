package de.codecentric.elasticsearch.challenge.api.solution;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder.Operator;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.index.query.PrefixFilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeFilterBuilder;
import org.elasticsearch.index.query.TermFilterBuilder;
import org.elasticsearch.index.query.TermsFilterBuilder;

import de.codecentric.elasticsearch.challenge.EmailSearchQuery;
import de.codecentric.elasticsearch.challenge.NamedQuery;

public final class SolutionQueryHelper {

  public static QueryBuilder emailToSearchQuery(EmailSearchQuery query) {
    QueryBuilder queryPart = createQueryPart(query);
    FilterBuilder filterPart = createFilterPart(query);

    return new FilteredQueryBuilder(queryPart, filterPart);
  }

  private static QueryBuilder createQueryPart(EmailSearchQuery query) {
    if (StringUtils.isBlank(query.getSubject()) && StringUtils.isBlank(query.getText())) {
      return QueryBuilders.matchAllQuery();
    }

    BoolQueryBuilder allQueryParts = QueryBuilders.boolQuery();
    if (StringUtils.isNotBlank(query.getSubject())) {
      allQueryParts
          .must(QueryBuilders.matchQuery(SolutionConstants.SUBJECT, query.getSubject()).operator(Operator.AND));
    }
    if (StringUtils.isNotBlank(query.getText())) {
      String[] textTerms = StringUtils.split(query.getText());
      for (String textTerm : textTerms) {
        allQueryParts.must(QueryBuilders.multiMatchQuery(textTerm, SolutionConstants.TEXT, SolutionConstants.TEXT_EN)
            .type(Type.CROSS_FIELDS));
      }
    }
    return allQueryParts;
  }

  private static FilterBuilder createFilterPart(EmailSearchQuery query) {
    List<FilterBuilder> filters = new ArrayList<>();

    if (StringUtils.isNotBlank(query.getId())) {
      filters.add(new TermFilterBuilder(SolutionConstants.ID, query.getId()));
    }
    if (StringUtils.isNotBlank(query.getSender())) {
      filters.add(new PrefixFilterBuilder(SolutionConstants.SENDER, query.getSender()));
    }
    if (StringUtils.isNotBlank(query.getRecipient())) {
      filters.add(new PrefixFilterBuilder(SolutionConstants.RECIPIENTS, query.getRecipient()));
    }
    if (query.getFromTimestamp() != null || query.getToTimestamp() != null) {
      RangeFilterBuilder timestampRangeFilter = FilterBuilders.rangeFilter(SolutionConstants.TIMESTAMP);
      if (query.getFromTimestamp() != null) {
        timestampRangeFilter.gte(query.getFromTimestamp());
      }
      if (query.getToTimestamp() != null) {
        timestampRangeFilter.lte(query.getToTimestamp());
      }
      filters.add(timestampRangeFilter);
    }
    if (CollectionUtils.isNotEmpty(query.getLabels())) {
      filters.add(new TermsFilterBuilder(SolutionConstants.LABELS, query.getLabels()).execution("and"));
    }
    if (query.getNamed() == NamedQuery.RECIPIENTS_CONTAIN_SENDER) {
      filters.add(new TermFilterBuilder(SolutionConstants.RECIPIENTS_CONTAIN_SENDER, true));
    }

    if (filters.isEmpty()) {
      return FilterBuilders.matchAllFilter();
    } else {
      return FilterBuilders.boolFilter().must(filters.toArray(new FilterBuilder[filters.size()]));
    }
  }

  private SolutionQueryHelper() {
    super();
  }
}
