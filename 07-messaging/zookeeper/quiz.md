# Zookeeper Quiz

## Questions

### Question 1: What is Zookeeper?
**A)** A messaging queue system  
**B)** A distributed coordination service  
**C)** A database management system  
**D)** A load balancer  

---

### Question 2: What does ZAB stand for?
**A)** Zookeeper Application Broadcasting  
**B)** Zookeeper Atomic Broadcast  
**C)** Zookeeper Authentication Backend  
**D)** Zookeeper Backup System  

---

### Question 3: Which node type is automatically removed when the client session ends?
**A)** Persistent  
**B)** Sequential  
**C)** Ephemeral  
**D)** Persistent Sequential  

---

### Question 4: What is the maximum recommended size for znode data?
**A)** 10 KB  
**B)** 100 KB  
**C)** 1 MB  
**D)** 10 MB  

---

### Question 5: Which of these is NOT a valid ACL scheme?
**A)** world  
**B)** digest  
**C)** password  
**D)** ip  

---

### Question 6: What happens when a watch triggers?
**A)** Watch remains active  
**B)** Watch is automatically removed  
**C)** Watch is moved to another znode  
**D)** Watch is sent to all clients  

---

### Question 7: Which Java client is recommended for Zookeeper?
**A)** Zookeeper Native Client  
**B)** Apache Curator  
**C)** Netflix Zuul  
**D)** Spring Cloud  

---

### Question 8: What port does Zookeeper use for client connections by default?
**A)** 2181  
**B)** 2888  
**C)** 3888  
**D)** 8080  

---

### Question 9: What is an Observer node in Zookeeper?
**A)** A node that handles writes  
**B)** A node that participates in elections  
**C)** A read-only node that scales read capacity  
**D)** A node that monitors other nodes  

---

### Question 10: Which consensus protocol does etcd use (for comparison)?
**A)** ZAB  
**B)** Raft  
**C)** Paxos  
**D)** Gossip  

---

## Answers

### Answer 1: B
**Zookeeper is a distributed coordination service** that provides consistency, naming, and synchronization services for distributed applications.

### Answer 2: B
**ZAB = Zookeeper Atomic Broadcast** - This is the consensus protocol that ensures consistency across Zookeeper servers.

### Answer 3: C
**Ephemeral znodes** are automatically deleted when the client session that created them ends. This is useful for service registration and presence tracking.

### Answer 4: C
**1 MB** is the maximum recommended size. Larger znodes can impact performance and should be avoided. Store large data externally.

### Answer 5: C
**password** is not a valid ACL scheme. Valid schemes are: world, auth, digest, ip, and sasl.

### Answer 6: B
**Watch is automatically removed** after it triggers. This is a one-time notification. To continue watching, you must re-register the watch.

### Answer 7: B
**Apache Curator** is the recommended Java client. It provides connection management, retry policies, and recipes for common patterns.

### Answer 8: A
**2181** is the default client connection port. Ports 2888 and 3888 are used for follower-to-leader and leader election respectively.

### Answer 9: C
**Observer nodes are read-only** and do not participate in voting or elections. They help scale read capacity without impacting write performance.

### Answer 10: B
**Raft** is used by etcd (and Consul). ZAB is specific to Zookeeper. Raft is designed to be easier to understand than Paxos.

---

## Score Guide

- **9-10 correct**: Expert level - You have deep understanding of Zookeeper
- **7-8 correct**: Advanced - Good grasp of core concepts
- **5-6 correct**: Intermediate - Review the architecture and data model
- **3-4 correct**: Beginner - Go through the basics again
- **0-2 correct**: Start with the README.md overview
