package academy.javaengineering.casestudies;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Case Studies Tests")
class CaseStudyTest {

    @Test
    @DisplayName("Should have real-world case studies")
    void testCaseStudies() {
        var studies = CaseStudyPatterns.getCaseStudies();
        assertFalse(studies.isEmpty());
        assertTrue(studies.stream().anyMatch(s -> s.company().equals("Netflix")));
    }

    @Test
    @DisplayName("Payment processor factory should create processors")
    void testPaymentProcessor() {
        var processor = DesignPatternsExample.PaymentProcessorFactory.createProcessor("creditcard");
        assertNotNull(processor);
        assertTrue(processor.processPayment(100.00));
    }

    @Test
    @DisplayName("Unknown payment type should throw exception")
    void testUnknownPaymentType() {
        assertThrows(IllegalArgumentException.class, 
            () -> DesignPatternsExample.PaymentProcessorFactory.createProcessor("unknown"));
    }
}
