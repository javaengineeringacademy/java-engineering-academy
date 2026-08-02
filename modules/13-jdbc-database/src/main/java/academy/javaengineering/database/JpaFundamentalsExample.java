package academy.javaengineering.database;

import java.util.*;

/**
 * JPA Fundamentals - EntityManager, JPQL.
 */
public class JpaFundamentalsExample {

    public interface Repository<T> {
        void save(T entity);
        Optional<T> findById(Long id);
        void delete(Long id);
        List<T> findAll();
    }

    public static class InMemoryRepository<T> implements Repository<T> {
        private final Map<Long, T> store = new HashMap<>();
        private long idCounter = 1;

        @Override
        public void save(T entity) { store.put(idCounter++, entity); }

        @Override
        public Optional<T> findById(Long id) { return Optional.ofNullable(store.get(id)); }

        @Override
        public void delete(Long id) { store.remove(id); }

        @Override
        public List<T> findAll() { return new ArrayList<>(store.values()); }
    }

    public static void main(String[] args) {
        Repository<String> repo = new InMemoryRepository<>();
        repo.save("User1");
        repo.save("User2");
        System.out.println("All: " + repo.findAll());
        System.out.println("Find by ID: " + repo.findById(1L).orElse("Not found"));
    }
}
