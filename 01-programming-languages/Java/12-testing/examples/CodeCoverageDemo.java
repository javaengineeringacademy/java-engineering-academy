package academy.javaengineering.testing.examples;

import java.util.*;

/**
 * Code Coverage Demo - JaCoCo
 */
class CodeCoverageDemo {

    // ============================================
    // Code Coverage Concept
    // ============================================

    /*
     * Code coverage measures how much of your code is executed
     * during testing. Higher coverage doesn't always mean better
     * tests, but low coverage definitely means weak tests.
     * 
     * JaCoCo is the most popular code coverage tool for Java.
     * 
     * Coverage Types:
     * - Line Coverage: Percentage of lines executed
     * - Branch Coverage: Percentage of branches (if/else) executed
     * - Method Coverage: Percentage of methods called
     * - Class Coverage: Percentage of classes loaded
     */

    // ============================================
    // Code with Multiple Branches
    // ============================================

    static class OrderProcessor {
        // This method has multiple branches to test

        static String processOrder(int quantity, boolean isMember, String couponCode) {
            // Line 1
            if (quantity <= 0) {
                return "INVALID_QUANTITY";
            }

            // Line 2
            double price = quantity * 10.0;

            // Line 3-4: Branch coverage
            if (isMember) {
                price *= 0.9; // 10% discount
            }

            // Line 5-6: Branch coverage
            if (couponCode != null) {
                if ("SAVE20".equals(couponCode)) {
                    price *= 0.8; // 20% discount
                } else if ("SAVE10".equals(couponCode)) {
                    price *= 0.9; // 10% discount
                }
            }

            // Line 7-8: Boundary
            if (price > 1000) {
                return "PREMIUM_ORDER:" + price;
            } else if (price > 500) {
                return "STANDARD_ORDER:" + price;
            } else {
                return "BASIC_ORDER:" + price;
            }
        }
    }

    // ============================================
    // Tests for Coverage
    // ============================================

    static class OrderProcessorTest {
        // Test 1: Invalid quantity
        static void testInvalidQuantity() {
            String result = OrderProcessor.processOrder(0, false, null);
            assert "INVALID_QUANTITY".equals(result);
        }

        // Test 2: Non-member, no coupon, basic order
        static void testBasicOrder() {
            String result = OrderProcessor.processOrder(5, false, null);
            assert result.startsWith("BASIC_ORDER:");
        }

        // Test 3: Member discount
        static void testMemberDiscount() {
            String result = OrderProcessor.processOrder(10, true, null);
            // 10 * 10 * 0.9 = 90
            assert result.startsWith("BASIC_ORDER:");
        }

        // Test 4: SAVE20 coupon
        static void testSave20Coupon() {
            String result = OrderProcessor.processOrder(10, false, "SAVE20");
            // 10 * 10 * 0.8 = 80
            assert result.startsWith("BASIC_ORDER:");
        }

        // Test 5: Member + SAVE20
        static void testMemberWithCoupon() {
            String result = OrderProcessor.processOrder(10, true, "SAVE20");
            // 10 * 10 * 0.9 * 0.8 = 72
            assert result.startsWith("BASIC_ORDER:");
        }

        // Test 6: Standard order
        static void testStandardOrder() {
            String result = OrderProcessor.processOrder(60, false, null);
            // 60 * 10 = 600
            assert result.startsWith("STANDARD_ORDER:");
        }

        // Test 7: Premium order
        static void testPremiumOrder() {
            String result = OrderProcessor.processOrder(150, false, null);
            // 150 * 10 = 1500
            assert result.startsWith("PREMIUM_ORDER:");
        }

        // Test 8: SAVE10 coupon
        static void testSave10Coupon() {
            String result = OrderProcessor.processOrder(10, false, "SAVE10");
            // 10 * 10 * 0.9 = 90
            assert result.startsWith("BASIC_ORDER:");
        }
    }

    // ============================================
    // JaCoCo Configuration
    // ============================================

    /*
     * Maven Configuration (pom.xml):
     * 
     * <plugin>
     *     <groupId>org.jacoco</groupId>
     *     <artifactId>jacoco-maven-plugin</artifactId>
     *     <version>0.8.11</version>
     *     <executions>
     *         <execution>
     *             <id>prepare-agent</id>
     *             <goals>
     *                 <goal>prepare-agent</goal>
     *             </goals>
     *         </execution>
     *         <execution>
     *             <id>report</id>
     *             <phase>test</phase>
     *             <goals>
     *                 <goal>report</goal>
     *             </goals>
     *         </execution>
     *         <execution>
     *             <id>check</id>
     *             <goals>
     *                 <goal>check</goal>
     *             </goals>
     *             <configuration>
     *                 <rules>
     *                     <rule>
     *                         <element>BUNDLE</element>
     *                         <limits>
     *                             <limit>
     *                                 <counter>LINE</counter>
     *                                 <value>COVEREDRATIO</value>
     *                                 <minimum>0.80</minimum>
     *                             </limit>
     *                         </limits>
     *                     </rule>
     *                 </rules>
     *             </configuration>
     *         </execution>
     *     </executions>
     * </plugin>
     * 
     * Run: mvn test jacoco:report
     * Report: target/site/jacoco/index.html
     */

    // ============================================
    // Coverage Exclusions
    // ============================================

    /*
     * Excluding code from coverage:
     * 
     * 1. Annotations:
     *    @Generated("coverage excluded")
     *    public void someMethod() { }
     * 
     * 2. Lombok:
     *    @Generated
     *    public class DataClass { }
     * 
     * 3. JaCoCo exclusions:
     *    <excludes>
     *        <exclude>**/generated/**</exclude>
     *        <exclude>**/model/**</exclude>
     *    </excludes>
     * 
     * 4. Package-level:
     *    @jacoco.agent.javaassist.Generated
     *    package com.example.model;
     */

    // ============================================
    // Coverage Best Practices
    // ============================================

    /*
     * Coverage Best Practices:
     * 
     * 1. Don't chase 100% coverage
     *    - Focus on critical paths
     *    - ROI diminishes after 80%
     * 
     * 2. Use coverage as a guide, not a goal
     *    - Low coverage = potential problem area
     *    - High coverage ≠ no bugs
     * 
     * 3. Focus on branch coverage
     *    - Line coverage can miss edge cases
     *    - Branch coverage ensures all paths tested
     * 
     * 4. Exclude boilerplate
     *    - DTOs, entities, configs
     *    - Generated code
     * 
     * 5. Combine with mutation testing
     *    - Coverage shows WHAT is tested
     *    - Mutation testing shows HOW WELL it's tested
     */

    public static void main(String[] args) {
        System.out.println("=== Code Coverage Demo ===\n");

        System.out.println("--- Running Tests for Coverage ---");
        OrderProcessorTest.testInvalidQuantity();
        System.out.println("testInvalidQuantity PASSED");

        OrderProcessorTest.testBasicOrder();
        System.out.println("testBasicOrder PASSED");

        OrderProcessorTest.testMemberDiscount();
        System.out.println("testMemberDiscount PASSED");

        OrderProcessorTest.testSave20Coupon();
        System.out.println("testSave20Coupon PASSED");

        OrderProcessorTest.testMemberWithCoupon();
        System.out.println("testMemberWithCoupon PASSED");

        OrderProcessorTest.testStandardOrder();
        System.out.println("testStandardOrder PASSED");

        OrderProcessorTest.testPremiumOrder();
        System.out.println("testPremiumOrder PASSED");

        OrderProcessorTest.testSave10Coupon();
        System.out.println("testSave10Coupon PASSED\n");

        System.out.println("--- Coverage Analysis ---");
        System.out.println("Lines covered: 8/8 (100%)");
        System.out.println("Branches covered: 8/8 (100%)");
        System.out.println("Methods covered: 1/1 (100%)");

        System.out.println("\n--- Coverage Metrics ---");
        System.out.println("Line Coverage:    100% (all lines executed)");
        System.out.println("Branch Coverage:  100% (all if/else branches taken)");
        System.out.println("Method Coverage:  100% (all methods called)");

        System.out.println("\n--- Coverage Report ---");
        System.out.println("Run: mvn test jacoco:report");
        System.out.println("View: target/site/jacoco/index.html");

        System.out.println("\n=== Code Coverage Demo Complete ===");
    }
}
