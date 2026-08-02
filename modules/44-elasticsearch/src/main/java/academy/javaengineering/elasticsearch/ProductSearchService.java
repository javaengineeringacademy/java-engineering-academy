package academy.javaengineering.elasticsearch;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Demonstrates Elasticsearch search service.
 */
@Service
public class ProductSearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    public ProductSearchService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public List<ProductDocument> searchByName(String name) {
        Criteria criteria = Criteria.where("name").contains(name);
        CriteriaQuery query = new CriteriaQuery(criteria);
        
        SearchHits<ProductDocument> hits = elasticsearchOperations.search(
            query, ProductDocument.class);
        
        return hits.getSearchHits().stream()
            .map(hit -> hit.getContent())
            .toList();
    }

    public List<ProductDocument> searchByCategoryAndPrice(String category, 
                                                           double minPrice, 
                                                           double maxPrice) {
        Criteria criteria = Criteria.where("category").is(category)
            .and(Criteria.where("price").between(minPrice, maxPrice));
        CriteriaQuery query = new CriteriaQuery(criteria);
        
        SearchHits<ProductDocument> hits = elasticsearchOperations.search(
            query, ProductDocument.class);
        
        return hits.getSearchHits().stream()
            .map(hit -> hit.getContent())
            .toList();
    }
}
