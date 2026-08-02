package academy.javaengineering.database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void shouldCreateTransferService() {
        TransactionExample.TransferService service =
                new TransactionExample.TransferService("jdbc:h2:mem:test");
        assertNotNull(service);
    }
}
