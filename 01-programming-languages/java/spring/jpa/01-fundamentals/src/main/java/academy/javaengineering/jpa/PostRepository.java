package academy.javaengineering.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for Post entity.
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // Derived queries
    List<Post> findByAuthorId(Long authorId);
    List<Post> findByStatus(PostStatus status);
    List<Post> findByTitleContainingIgnoreCase(String title);

    // JPQL with joins
    @Query("SELECT p FROM Post p WHERE p.author.name = :authorName")
    List<Post> findByAuthorName(@Param("authorName") String authorName);

    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t.name = :tagName")
    List<Post> findByTagName(@Param("tagName") String tagName);

    // Aggregate queries
    @Query("SELECT p.author.name, COUNT(p) FROM Post p GROUP BY p.author.name")
    List<Object[]> countPostsByAuthor();
}
