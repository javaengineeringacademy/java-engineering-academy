# Spring Batch

## Comprehensive Guide to Spring Batch

Spring Batch is a lightweight, comprehensive batch processing framework designed for developing enterprise batch applications. This guide covers jobs, steps, readers, processors, writers, and chunk-oriented processing.

---

## Table of Contents

1. [Job & Step](#job--step)
2. [Chunk-Oriented Processing](#chunk-oriented-processing)
3. [Item Readers](#item-readers)
4. [Item Processors](#item-processors)
5. [Item Writers](#item-writers)
6. [Advanced Patterns](#advanced-patterns)
7. [Best Practices](#best-practices)

---

## Job & Step

### Basic Job Configuration

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-batch</artifactId>
</dependency>
```

```java
@Configuration
@EnableBatchProcessing
public class BatchConfig {
    
    @Bean
    public Job myJob(JobRepository jobRepository, Step step1) {
        return new JobBuilder("myJob", jobRepository)
            .start(step1)
            .build();
    }
    
    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
            .<String, String>chunk(10, transactionManager)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .build();
    }
}
```

### Multi-Step Job

```java
@Bean
public Job multiStepJob(JobRepository jobRepository, 
                        Step step1, 
                        Step step2, 
                        Step step3) {
    return new JobBuilder("multiStepJob", jobRepository)
        .start(step1)
        .next(step2)
        .next(step3)
        .build();
}

@Bean
public Job conditionalJob(JobRepository jobRepository,
                          Step successStep,
                          Step failureStep) {
    return new JobBuilder("conditionalJob", jobRepository)
        .start(successStep)
            .on("COMPLETED")
            .to(failureStep)
        .from(successStep)
            .on("FAILED")
            .fail()
        .end()
        .build();
}

@Bean
public Job complexJob(JobRepository jobRepository,
                      Step step1,
                      Step step2,
                      Step step3,
                      Step step4) {
    return new JobBuilder("complexJob", jobRepository)
        .start(step1)
            .on("COMPLETED")
            .to(step2)
                .on("COMPLETED")
                .to(step3)
            .from(step2)
                .on("FAILED")
                .to(step4)
        .end()
        .build();
}
```

### Job Parameters

```java
@Bean
public Job jobWithParameters(JobRepository jobRepository, Step step) {
    return new JobBuilder("jobWithParameters", jobRepository)
        .start(step)
        .build();
}

// Running job with parameters
@Component
public class JobLauncherRunner implements ApplicationRunner {
    
    @Autowired
    private JobLauncher jobLauncher;
    
    @Autowired
    @Qualifier("jobWithParameters")
    private Job job;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        JobParameters parameters = new JobParametersBuilder()
            .addLong("timestamp", System.currentTimeMillis())
            .addString("inputFile", "data/input.csv")
            .addString("outputFile", "data/output.csv")
            .toJobParameters();
        
        JobExecution execution = jobLauncher.run(job, parameters);
        System.out.println("Job Status: " + execution.getStatus());
    }
}
```

### Job Execution Listener

```java
@Component
public class JobCompletionNotificationListener implements JobExecutionListener {
    
    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("Job Started: " + jobExecution.getJobInstance().getJobName());
    }
    
    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            System.out.println("Job Completed Successfully");
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            System.out.println("Job Failed");
        }
    }
}

// Usage
@Bean
public Job jobWithListener(JobRepository jobRepository, 
                           Step step,
                           JobCompletionNotificationListener listener) {
    return new JobBuilder("jobWithListener", jobRepository)
        .listener(listener)
        .start(step)
        .build();
}
```

---

## Chunk-Oriented Processing

### Basic Chunk Configuration

```java
@Bean
public Step chunkStep(JobRepository jobRepository, 
                      PlatformTransactionManager transactionManager) {
    return new StepBuilder("chunkStep", jobRepository)
        .<Person, PersonDTO>chunk(10, transactionManager)
        .reader(reader())
        .processor(processor())
        .writer(writer())
        .build();
}
```

### Chunk Listener

```java
@Component
public class ChunkListenerImpl implements ChunkListener {
    
    @Override
    public void beforeChunk(ChunkContext context) {
        System.out.println("Starting chunk processing");
    }
    
    @Override
    public void afterChunk(ChunkContext context) {
        System.out.println("Completed chunk processing");
    }
    
    @Override
    public void afterChunkError(ChunkContext context) {
        System.out.println("Error in chunk processing");
    }
}

// Usage
@Bean
public Step stepWithChunkListener(JobRepository jobRepository,
                                  PlatformTransactionManager transactionManager,
                                  ChunkListenerImpl chunkListener) {
    return new StepBuilder("stepWithChunkListener", jobRepository)
        .<Person, PersonDTO>chunk(10, transactionManager)
        .reader(reader())
        .processor(processor())
        .writer(writer())
        .listener(chunkListener)
        .build();
}
```

### Transaction Configuration

```java
@Bean
public Step transactionalStep(JobRepository jobRepository,
                              PlatformTransactionManager transactionManager) {
    return new StepBuilder("transactionalStep", jobRepository)
        .<Person, PersonDTO>chunk(10, transactionManager)
        .reader(reader())
        .processor(processor())
        .writer(writer())
        .transactional()
        .isReaderTransactional(true)
        .startLimit(3) // Limit restart attempts
        .allowStartIfComplete(true)
        .build();
}
```

---

## Item Readers

### FlatFileItemReader

```java
@Bean
public FlatFileItemReader<Person> reader() {
    return new FlatFileItemReaderBuilder<Person>()
        .name("personReader")
        .resource(new ClassPathResource("data/people.csv"))
        .delimited()
        .names(new String[]{"firstName", "lastName", "age", "email"})
        .fieldSetMapper(fieldSet -> {
            Person person = new Person();
            person.setFirstName(fieldSet.readString("firstName"));
            person.setLastName(fieldSet.readString("lastName"));
            person.setAge(fieldSet.readInt("age"));
            person.setEmail(fieldSet.readString("email"));
            return person;
        })
        .build();
}
```

### JdbcCursorItemReader

```java
@Bean
public JdbcCursorItemReader<Person> jdbcReader(DataSource dataSource) {
    return new JdbcCursorItemReaderBuilder<Person>()
        .name("jdbcPersonReader")
        .dataSource(dataSource)
        .sql("SELECT first_name, last_name, age, email FROM people WHERE active = true")
        .rowMapper((rs, rowNum) -> {
            Person person = new Person();
            person.setFirstName(rs.getString("first_name"));
            person.setLastName(rs.getString("last_name"));
            person.setAge(rs.getInt("age"));
            person.setEmail(rs.getString("email"));
            return person;
        })
        .fetchSize(100)
        .maxItemCount(1000)
        .build();
}
```

### JpaPagingItemReader

```java
@Bean
public JpaPagingItemReader<Person> jpaReader(EntityManagerFactory entityManagerFactory) {
    return new JpaPagingItemReaderBuilder<Person>()
        .name("jpaPersonReader")
        .entityManagerFactory(entityManagerFactory)
        .queryString("SELECT p FROM Person p WHERE p.active = true")
        .pageSize(20)
        .build();
}
```

### RepositoryItemReader

```java
@Bean
public RepositoryItemReader<Person> repositoryReader(PersonRepository repository) {
    return new RepositoryItemReaderBuilder<Person>()
        .name("repositoryPersonReader")
        .repository(repository)
        .methodName("findAllByActiveTrue")
        .pageSize(10)
        .sorts(Map.of("lastName", Sort.Direction.ASC))
        .build();
}
```

### Custom Reader

```java
public class CustomReader implements ItemReader<Person> {
    
    private List<Person> people;
    private int currentIndex = 0;
    
    public CustomReader(List<Person> people) {
        this.people = people;
    }
    
    @Override
    public Person read() {
        if (currentIndex < people.size()) {
            return people.get(currentIndex++);
        }
        return null; // End of data
    }
}

// Usage
@Bean
public Step customReaderStep(JobRepository jobRepository,
                             PlatformTransactionManager transactionManager) {
    List<Person> people = loadPeople();
    
    return new StepBuilder("customReaderStep", jobRepository)
        .<Person, PersonDTO>chunk(10, transactionManager)
        .reader(new CustomReader(people))
        .processor(processor())
        .writer(writer())
        .build();
}
```

### Multi-Resource Reader

```java
@Bean
public MultiResourceItemReader<Person> multiResourceReader() {
    return new MultiResourceItemReaderBuilder<Person>()
        .name("multiResourceReader")
        .resources(new ClassPathResource("data/people*.csv"))
        .delegate(reader())
        .build();
}
```

---

## Item Processors

### Basic Processor

```java
@Component
public class PersonProcessor implements ItemProcessor<Person, PersonDTO> {
    
    @Override
    public PersonDTO process(Person person) throws Exception {
        // Filter out invalid records
        if (person.getAge() < 0 || person.getAge() > 150) {
            return null; // Skip this item
        }
        
        // Transform
        PersonDTO dto = new PersonDTO();
        dto.setFullName(person.getFirstName() + " " + person.getLastName());
        dto.setAge(person.getAge());
        dto.setEmail(person.getEmail().toLowerCase());
        dto.setActive(true);
        
        return dto;
    }
}
```

### Composite Processor

```java
@Component
public class CompositeProcessor implements ItemProcessor<Person, PersonDTO> {
    
    private final List<ItemProcessor<Person, PersonDTO>> processors;
    
    public CompositeProcessor(List<ItemProcessor<Person, PersonDTO>> processors) {
        this.processors = processors;
    }
    
    @Override
    public PersonDTO process(Person person) throws Exception {
        PersonDTO result = null;
        
        for (ItemProcessor<Person, PersonDTO> processor : processors) {
            result = processor.process(person);
            if (result == null) {
                return null; // Skip if any processor returns null
            }
        }
        
        return result;
    }
}
```

### Filtering Processor

```java
@Component
public class FilteringProcessor implements ItemProcessor<Person, Person> {
    
    @Override
    public Person process(Person person) throws Exception {
        // Only process adults
        if (person.getAge() >= 18) {
            return person;
        }
        return null; // Filter out minors
    }
}
```

### Validation Processor

```java
@Component
public class ValidationProcessor implements ItemProcessor<Person, Person> {
    
    private final Validator validator;
    
    public ValidationProcessor(Validator validator) {
        this.validator = validator;
    }
    
    @Override
    public Person process(Person person) throws Exception {
        Set<ConstraintViolation<Person>> violations = validator.validate(person);
        
        if (!violations.isEmpty()) {
            throw new ValidationException("Invalid person: " + violations);
        }
        
        return person;
    }
}
```

### ItemProcessorListener

```java
@Component
public class ProcessorListener implements ItemProcessListener<Person, PersonDTO> {
    
    @Override
    public void beforeProcess(Person item) {
        System.out.println("Processing: " + item.getFirstName());
    }
    
    @Override
    public void afterProcess(Person item, PersonDTO result) {
        System.out.println("Processed: " + item.getFirstName() + 
                          " -> " + (result != null ? result.getFullName() : "SKIPPED"));
    }
    
    @Override
    public void onProcessError(Person item, Exception e) {
        System.out.println("Error processing: " + item.getFirstName());
    }
}

// Usage
@Bean
public Step stepWithProcessorListener(JobRepository jobRepository,
                                      PlatformTransactionManager transactionManager,
                                      ProcessorListener listener) {
    return new StepBuilder("stepWithProcessorListener", jobRepository)
        .<Person, PersonDTO>chunk(10, transactionManager)
        .reader(reader())
        .processor(processor())
        .writer(writer())
        .listener(listener)
        .build();
}
```

---

## Item Writers

### FlatFileItemWriter

```java
@Bean
public FlatFileItemWriter<PersonDTO> writer() {
    return new FlatFileItemWriterBuilder<PersonDTO>()
        .name("personWriter")
        .resource(new ClassPathResource("data/output/people.csv"))
        .delimited()
        .names(new String[]{"fullName", "age", "email", "active"})
        .footerCallback(writer -> writer.write("Total records processed: " + 
                                              writer.getLinesWritten()))
        .build();
}
```

### JdbcBatchItemWriter

```java
@Bean
public JdbcBatchItemWriter<PersonDTO> jdbcWriter(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<PersonDTO>()
        .dataSource(dataSource)
        .sql("INSERT INTO people (full_name, age, email, active) " +
             "VALUES (:fullName, :age, :email, :active)")
        .beanMapped()
        .build();
}
```

### JpaItemWriter

```java
@Bean
public JpaItemWriter<Person> jpaWriter(EntityManagerFactory entityManagerFactory) {
    return new JpaItemWriterBuilder<Person>()
        .entityManagerFactory(entityManagerFactory)
        .usePersist(true)
        .build();
}
```

### CompositeWriter

```java
@Bean
public CompositeItemWriter<PersonDTO> compositeWriter() {
    return new CompositeItemWriterBuilder<PersonDTO>()
        .delegates(List.of(fileWriter(), databaseWriter()))
        .build();
}
```

### RepositoryItemWriter

```java
@Bean
public RepositoryItemWriter<Person> repositoryWriter(PersonRepository repository) {
    return new RepositoryItemWriterBuilder<Person>()
        .repository(repository)
        .methodName("saveAll")
        .build();
}
```

### Custom Writer

```java
public class CustomWriter implements ItemWriter<PersonDTO> {
    
    @Override
    public void write(Chunk<? extends PersonDTO> chunk) throws Exception {
        for (PersonDTO person : chunk.getItems()) {
            // Custom writing logic
            System.out.println("Writing: " + person.getFullName());
        }
    }
}

// Usage
@Bean
public Step customWriterStep(JobRepository jobRepository,
                             PlatformTransactionManager transactionManager) {
    return new StepBuilder("customWriterStep", jobRepository)
        .<Person, PersonDTO>chunk(10, transactionManager)
        .reader(reader())
        .processor(processor())
        .writer(new CustomWriter())
        .build();
}
```

### ItemWriterListener

```java
@Component
public class WriterListener implements ItemWriteListener<PersonDTO> {
    
    @Override
    public void beforeWrite(Chunk<? extends PersonDTO> items) {
        System.out.println("Writing " + items.size() + " items");
    }
    
    @Override
    public void afterWrite(Chunk<? extends PersonDTO> items) {
        System.out.println("Successfully wrote " + items.size() + " items");
    }
    
    @Override
    public void onWriteError(Exception exception, Chunk<? extends PersonDTO> items) {
        System.out.println("Error writing items: " + exception.getMessage());
    }
}
```

---

## Advanced Patterns

### Skip Strategy

```java
@Bean
public Step stepWithSkip(JobRepository jobRepository,
                         PlatformTransactionManager transactionManager) {
    return new StepBuilder("stepWithSkip", jobRepository)
        .<Person, PersonDTO>chunk(10, transactionManager)
        .reader(reader())
        .processor(processor())
        .writer(writer())
        .faultTolerant()
        .skipLimit(10)
        .skip(ValidationException.class)
        .skip(FlatFileParseException.class)
        .noSkip(DatabaseAccessException.class)
        .retryLimit(3)
        .retry(DatabaseAccessException.class)
        .build();
}
```

### Retry Policy

```java
@Bean
public Step stepWithRetry(JobRepository jobRepository,
                          PlatformTransactionManager transactionManager) {
    return new StepBuilder("stepWithRetry", jobRepository)
        .<Person, PersonDTO>chunk(10, transactionManager)
        .reader(reader())
        .processor(processor())
        .writer(writer())
        .faultTolerant()
        .retryLimit(5)
        .retry(DatabaseAccessException.class)
        .retry(DeadlockLoserDataAccessException.class)
        .backOffPolicy(new ExponentialBackOffPolicy())
        .build();
}
```

### Restart Configuration

```java
@Bean
public Step stepWithRestart(JobRepository jobRepository,
                            PlatformTransactionManager transactionManager) {
    return new StepBuilder("stepWithRestart", jobRepository)
        .<Person, PersonDTO>chunk(10, transactionManager)
        .reader(reader())
        .processor(processor())
        .writer(writer())
        .allowStartIfComplete(true)
        .startLimit(3) // Allow 3 restart attempts
        .build();
}
```

### Parallel Steps

```java
@Bean
public Job parallelJob(JobRepository jobRepository,
                       Step step1,
                       Step step2,
                       Step step3) {
    
    SimpleFlow flow1 = new FlowBuilder<SimpleFlow>()
        .start(step1).build();
    
    SimpleFlow flow2 = new FlowBuilder<SimpleFlow>()
        .start(step2).build();
    
    SimpleFlow flow3 = new FlowBuilder<SimpleFlow>()
        .start(step3).build();
    
    SimpleFlow parallelFlow = new FlowBuilder<SimpleFlow>()
        .split(new SimpleAsyncTaskExecutor())
        .add(flow1, flow2, flow3)
        .build();
    
    return new JobBuilder("parallelJob", jobRepository)
        .start(parallelFlow)
        .end()
        .build();
}
```

### Decision Step

```java
@Bean
public Job jobWithDecision(JobRepository jobRepository,
                           Step step1,
                           Step step2,
                           Step step3) {
    return new JobBuilder("jobWithDecision", jobRepository)
        .start(step1)
        .next(decision()).on("FAILED").to(step3)
        .from(decision()).on("COMPLETED").to(step2)
        .from(step2).on("*").to(step1)
        .end()
        .build();
}

@Bean
public JobExecutionDecider decision() {
    return new JobExecutionDecider() {
        @Override
        public FlowExecutionStatus decide(JobExecution jobExecution,
                                          StepExecution stepExecution) {
            if (stepExecution.getCommitCount() > 5) {
                return new FlowExecutionStatus("FAILED");
            }
            return new FlowExecutionStatus("COMPLETED");
        }
    };
}
```

---

## Best Practices

### Job Configuration

```java
@Configuration
@EnableBatchProcessing
public class BatchConfig {
    
    @Bean
    public Job job(JobRepository jobRepository,
                   PlatformTransactionManager transactionManager,
                   ItemReader<Person> reader,
                   ItemProcessor<Person, PersonDTO> processor,
                   ItemWriter<PersonDTO> writer) {
        
        return new JobBuilder("myJob", jobRepository)
            .start(new StepBuilder("step1", jobRepository)
                .<Person, PersonDTO>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skipLimit(5)
                .skip(ValidationException.class)
                .build())
            .build();
    }
}
```

### Error Handling

```java
@Component
public class JobErrorListener implements JobExecutionListener {
    
    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.FAILED) {
            // Send notification
            sendFailureNotification(jobExecution);
            
            // Log detailed error
            jobExecution.getAllStepExecutions().forEach(stepExec -> {
                stepExec.getStepExceptions().forEach(exception -> {
                    log.error("Step {} failed: {}", 
                        stepExec.getStepName(), exception.getMessage());
                });
            });
        }
    }
    
    private void sendFailureNotification(JobExecution jobExecution) {
        // Implementation
    }
}
```

### Monitoring

```java
@Component
public class BatchMetrics {
    
    @Autowired
    private JobRepository jobRepository;
    
    public Map<String, Object> getJobStatistics(String jobName) {
        List<JobInstance> instances = jobRepository.getJobInstances(jobName, 0, 100);
        
        long totalExecutions = instances.stream()
            .flatMap(instance -> jobRepository.getJobExecutions(instance).stream())
            .count();
        
        long failedExecutions = instances.stream()
            .flatMap(instance -> jobRepository.getJobExecutions(instance).stream())
            .filter(exec -> exec.getStatus() == BatchStatus.FAILED)
            .count();
        
        return Map.of(
            "jobName", jobName,
            "totalInstances", instances.size(),
            "totalExecutions", totalExecutions,
            "failedExecutions", failedExecutions,
            "successRate", calculateSuccessRate(totalExecutions, failedExecutions)
        );
    }
    
    private double calculateSuccessRate(long total, long failed) {
        if (total == 0) return 0.0;
        return ((double) (total - failed) / total) * 100;
    }
}
```

---

## Common Pitfalls

### 1. Not Using Fault Tolerance

```java
// Bad - No fault tolerance
@Bean
public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("step", jobRepository)
        .<Person, PersonDTO>chunk(10, transactionManager)
        .reader(reader())
        .processor(processor())
        .writer(writer())
        .build();
}

// Good - With fault tolerance
@Bean
public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("step", jobRepository)
        .<Person, PersonDTO>chunk(10, transactionManager)
        .reader(reader())
        .processor(processor())
        .writer(writer())
        .faultTolerant()
        .skipLimit(5)
        .skip(ValidationException.class)
        .build();
}
```

### 2. Large Chunk Size

```java
// Bad - Too large chunk size
.chunk(10000, transactionManager)

// Good - Appropriate chunk size
.chunk(100, transactionManager)
```

### 3. Not Monitoring Jobs

```java
// Bad - No monitoring
@Bean
public Job job(JobRepository jobRepository, Step step) {
    return new JobBuilder("job", jobRepository)
        .start(step)
        .build();
}

// Good - With monitoring
@Bean
public Job job(JobRepository jobRepository, Step step, JobCompletionNotificationListener listener) {
    return new JobBuilder("job", jobRepository)
        .listener(listener)
        .start(step)
        .build();
}
```

---

## Further Reading

- [Spring Batch Official Documentation](https://spring.io/projects/spring-batch)
- [Spring Batch Reference](https://docs.spring.io/spring-batch/docs/current/reference/html/)
- [Baeldung Spring Batch](https://www.baeldung.com/spring-batch)
