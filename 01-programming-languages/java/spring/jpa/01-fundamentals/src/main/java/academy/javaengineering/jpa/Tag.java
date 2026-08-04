package academy.javaengineering.jpa;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * JPA Entity demonstrating Many-to-Many relationship.
 */
@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<Post> posts = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "post_tags",
        joinColumns = @JoinColumn(name = "tag_id"),
        inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<Post> taggedPosts = new HashSet<>();

    // Constructors
    public Tag() {}

    public Tag(String name) {
        this.name = name;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<Post> getPosts() { return posts; }
    public void setPosts(Set<Post> posts) { this.posts = posts; }

    public Set<Post> getTaggedPosts() { return taggedPosts; }
    public void setTaggedPosts(Set<Post> taggedPosts) { this.taggedPosts = taggedPosts; }

    @Override
    public String toString() {
        return "Tag{id=" + id + ", name='" + name + "'}";
    }
}
