package academy.javaengineering.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Demonstrates Elasticsearch repository.
 */
public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, String> {

    List<ProductDocument> findByName(String name);

    List<ProductDocument> findByCategory(String category);

    List<ProductDocument> findByPriceBetween(double min, double max);

    List<ProductDocument> findByDescriptionContaining(String text);
}
