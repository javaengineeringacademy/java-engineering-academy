# Exception Handling Best Practices: Solutions (Part 2)

> Solution 4. See [Part 1](README-Part1.md) for Solutions 1–3, [Part 3](README-Part3.md) for Solutions 5–6.

---

## Solution 4: Write Tests for Exception Paths

```java
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private InventoryService inventory;

    @Mock
    private NotificationService notifications;

    @InjectMocks
    private ProductService productService;

    private CreateProductRequest validRequest() {
        return new CreateProductRequest(
            "Widget",
            "Electronics",
            new BigDecimal("29.99")
        );
    }

    @Test
    @DisplayName("Should create product successfully")
    void shouldCreateProductSuccessfully() {
        // Arrange
        CreateProductRequest request = validRequest();
        when(repository.existsByName("Widget")).thenReturn(false);

        // Act
        Product result = productService.createProduct(request);

        // Assert
        assertNotNull(result);
        assertEquals("Widget", result.getName());
        verify(repository).save(any(Product.class));
        verify(inventory).initializeStock(anyString(), eq(0));
        verify(notifications).notifyProductCreated(any(Product.class));
    }

    @Test
    @DisplayName("Should throw when name is null")
    void shouldThrowWhenNameIsNull() {
        // Arrange
        CreateProductRequest request = new CreateProductRequest(
            null, "Electronics", new BigDecimal("29.99")
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productService.createProduct(request)
        );

        assertTrue(exception.getMessage().contains("name"));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw when name is blank")
    void shouldThrowWhenNameIsBlank() {
        // Arrange
        CreateProductRequest request = new CreateProductRequest(
            "   ", "Electronics", new BigDecimal("29.99")
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productService.createProduct(request)
        );

        assertTrue(exception.getMessage().contains("name"));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw when price is zero")
    void shouldThrowWhenPriceIsZero() {
        // Arrange
        CreateProductRequest request = new CreateProductRequest(
            "Widget", "Electronics", BigDecimal.ZERO
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productService.createProduct(request)
        );

        assertTrue(exception.getMessage().contains("price"));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw when price is negative")
    void shouldThrowWhenPriceIsNegative() {
        // Arrange
        CreateProductRequest request = new CreateProductRequest(
            "Widget", "Electronics", new BigDecimal("-5.00")
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productService.createProduct(request)
        );

        assertTrue(exception.getMessage().contains("price"));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw when product name already exists")
    void shouldThrowWhenNameAlreadyExists() {
        // Arrange
        CreateProductRequest request = validRequest();
        when(repository.existsByName("Widget")).thenReturn(true);

        // Act & Assert
        DuplicateProductException exception = assertThrows(
            DuplicateProductException.class,
            () -> productService.createProduct(request)
        );

        assertEquals("Widget", exception.getProductName());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw when repository save fails")
    void shouldThrowWhenRepositorySaveFails() {
        // Arrange
        CreateProductRequest request = validRequest();
        when(repository.existsByName("Widget")).thenReturn(false);
        when(repository.save(any()))
            .thenThrow(new ProductRepositoryException("DB error"));

        // Act & Assert
        assertThrows(
            ProductRepositoryException.class,
            () -> productService.createProduct(request)
        );
        verify(inventory, never()).initializeStock(anyString(), anyInt());
        verify(notifications, never()).notifyProductCreated(any());
    }

    @Test
    @DisplayName("Should create product even when notification fails")
    void shouldCreateProductEvenWhenNotificationFails() {
        // Arrange
        CreateProductRequest request = validRequest();
        when(repository.existsByName("Widget")).thenReturn(false);
        doThrow(new NotificationException("Service unavailable"))
            .when(notifications).notifyProductCreated(any());

        // Act
        Product result = productService.createProduct(request);

        // Assert: Product created despite notification failure
        assertNotNull(result);
        verify(repository).save(any(Product.class));
        verify(inventory).initializeStock(anyString(), eq(0));
    }

    @Test
    @DisplayName("Should verify save before inventory initialization")
    void shouldVerifySaveBeforeInventory() {
        // Arrange
        CreateProductRequest request = validRequest();
        when(repository.existsByName("Widget")).thenReturn(false);

        InOrder inOrder = inOrder(repository, inventory, notifications);

        // Act
        productService.createProduct(request);

        // Assert: Save before inventory before notifications
        inOrder.verify(repository).save(any(Product.class));
        inOrder.verify(inventory).initializeStock(anyString(), eq(0));
        inOrder.verify(notifications).notifyProductCreated(any(Product.class));
    }
}
```

### Test Coverage Summary

| Scenario | Test Method | Expected Outcome |
|---|---|---|
| Happy path | `shouldCreateProductSuccessfully` | Product created, all services called |
| Null name | `shouldThrowWhenNameIsNull` | IllegalArgumentException, no save |
| Blank name | `shouldThrowWhenNameIsBlank` | IllegalArgumentException, no save |
| Zero price | `shouldThrowWhenPriceIsZero` | IllegalArgumentException, no save |
| Negative price | `shouldThrowWhenPriceIsNegative` | IllegalArgumentException, no save |
| Duplicate name | `shouldThrowWhenNameAlreadyExists` | DuplicateProductException, no save |
| Repository failure | `shouldThrowWhenRepositorySaveFails` | ProductRepositoryException, no inventory |
| Notification failure | `shouldCreateProductEvenWhenNotificationFails` | Product created, notification failure handled |
| Operation order | `shouldVerifySaveBeforeInventory` | Save -> inventory -> notifications verified |

---

*See also: [Decision Guide](../decision.md) | [Part 1: Solutions 1–3](README-Part1.md) | [Part 3: Solutions 5–6](README-Part3.md)*
