# Quiz: Artificial & Synthetic Monitoring

## Questions

### 1. What is the primary difference between synthetic monitoring and real user monitoring (RUM)?

A) Synthetic monitoring is more expensive than RUM  
B) Synthetic monitoring simulates user actions, while RUM captures actual user behavior  
C) RUM can detect outages proactively, while synthetic cannot  
D) Synthetic monitoring requires more configuration than RUM  

---

### 2. Which of the following is NOT a type of synthetic monitoring?

A) HTTP/API monitoring  
B) Browser synthetic monitoring  
C) User session recording  
D) DNS monitoring  

---

### 3. What is the main benefit of synthetic monitoring for SLA verification?

A) It measures actual user experience  
B) It provides consistent, repeatable measurements independent of user traffic  
C) It captures all user journeys  
D) It is cheaper than real user monitoring  

---

### 4. In a synthetic transaction script, what is the purpose of recording both success/failure status AND response time?

A) To calculate total cost  
B) To determine user satisfaction  
C) To monitor both availability and performance  
D) To track user engagement  

---

### 5. What is the recommended approach for monitoring critical applications?

A) Use only synthetic monitoring  
B) Use only real user monitoring  
C) Use a hybrid approach combining synthetic and real user monitoring  
D) Use whichever is cheaper  

---

### 6. Which protocol requires special handling for synthetic monitoring due to its binary nature?

A) HTTP  
B) REST  
C) gRPC  
D) WebSocket  

---

### 7. What is the purpose of "ramp up" in load testing?

A) To immediately apply maximum load  
B) To gradually increase load to identify breaking points  
C) To reduce load during testing  
D) To maintain constant load  

---

### 8. In chaos engineering, what is the "blast radius"?

A) The total cost of the experiment  
B) The duration of the experiment  
C) The scope of systems and users affected by the experiment  
D) The number of engineers involved  

---

### 9. What is a key consideration when setting up synthetic monitors from multiple geographic locations?

A) Reducing monitoring costs  
B) Increasing alert frequency  
C) Ensuring consistent monitoring results across regions  
D) Minimizing network traffic  

---

### 10. Which metric is most important for detecting SSL certificate issues proactively?

A) Response time  
B) Days until certificate expires  
C) Number of concurrent connections  
D) Bandwidth usage  

---

## Answer Key

| Question | Answer | Explanation |
|----------|--------|-------------|
| 1 | B | Synthetic monitoring simulates user actions programmatically, while RUM captures actual user behavior from real browsers. |
| 2 | C | User session recording is a real user monitoring technique, not synthetic. Synthetic monitoring uses automated scripts. |
| 3 | B | Synthetic monitoring provides consistent measurements because it uses the same inputs every time, unlike RUM which varies with actual user behavior. |
| 4 | C | Combining availability (success/failure) and performance (response time) metrics gives a complete picture of service health. |
| 5 | C | A hybrid approach is recommended because synthetic monitoring excels at proactive detection, while RUM provides insights into actual user experience. |
| 6 | C | gRPC uses HTTP/2 with binary Protocol Buffers, requiring specialized tools and approaches compared to text-based protocols. |
| 7 | B | Ramp up gradually increases load to help identify performance degradation points and system limits without overwhelming the system. |
| 8 | C | Blast radius defines the potential impact scope of a chaos experiment, helping teams control risk. |
| 9 | C | Multi-region monitoring helps detect regional issues and ensures users worldwide have good experience. |
| 10 | B | Tracking days until expiration allows proactive renewal before service disruption occurs. |

---

## Scoring

- **9-10 correct:** Expert level - You have deep understanding of synthetic monitoring concepts
- **7-8 correct:** Advanced - Good grasp with minor gaps to fill
- **5-6 correct:** Intermediate - Solid foundation, review advanced topics
- **3-4 correct:** Beginner - Focus on core concepts and practice
- **0-2 correct:** Review recommended materials and try again

---

## Further Study

After completing this quiz, review the following topics:
- [Decision Guide](decision.md) - When to use synthetic vs real user monitoring
- [References](references.md) - Official documentation and tutorials
- [Implementation](README.md#implementation) - Setting up synthetic monitors
