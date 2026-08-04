# Apache Oozie

Apache Oozie is a workflow scheduler system for managing Apache Hadoop jobs. It coordinates multiple jobs (MapReduce, Pig, Hive, Sqoop) into a directed acyclic graph (DAG) of workflows.

## Table of Contents

1. [Workflow Jobs](#workflow-jobs)
2. [Coordinator Jobs](#coordinator-jobs)
3. [Bundle Jobs](#bundle-jobs)
4. [Action Nodes](#action-nodes)
5. [Decision Nodes](#decision-nodes)
6. [EL Functions](#el-functions)
7. [SLA](#sla)
8. [Bundling](#bundling)
9. [Oozie vs Airflow](#oozie-vs-airflow)

---

## Workflow Jobs

Workflow jobs define the sequence of actions:

### Workflow Definition

```xml
<workflow-app name="my-workflow" xmlns="uri:oozie:workflow:0.5">
    <start to="check-input"/>
    
    <decision name="check-input">
        <switch>
            <case to="process-data">
                ${fs:exists('/data/input/${year}/${month}/${day}')}
            </case>
            <default to="no-data"/>
        </switch>
    </decision>
    
    <action name="process-data">
        <map-reduce>
            <job-tracker>${jobTracker}</job-tracker>
            <name-node>${nameNode}</name-node>
            <configuration>
                <property>
                    <name>mapred.mapper.class</name>
                    <value>com.example.Mapper</value>
                </property>
            </configuration>
        </map-reduce>
        <ok to="cleanup"/>
        <error to="handle-error"/>
    </action>
    
    <action name="no-data">
        <java>
            <job-tracker>${jobTracker}</job-tracker>
            <name-node>${nameNode}</name-node>
            <main-class>com.example.NoDataHandler</main-class>
        </java>
        <ok to="end"/>
        <error to="handle-error"/>
    </action>
    
    <action name="cleanup">
        <shell xmlns="uri:oozie:shell-action:0.1">
            <job-tracker>${jobTracker}</job-tracker>
            <name-node>${nameNode}</name-node>
            <exec>/bin/bash</exec>
            <argument>-c</argument>
            <argument>hadoop fs -rm -r /tmp/staging</argument>
        </shell>
        <ok to="end"/>
        <error to="handle-error"/>
    </action>
    
    <kill name="handle-error">
        <message>Workflow failed: ${wf:lastErrorNode()}</message>
    </kill>
    
    <end name="end"/>
</workflow-app>
```

### Workflow Properties

- **name**: Workflow identifier
- **start**: First action to execute
- **end**: Terminal node
- **kill**: Error handling node
- **decision**: Conditional branching
- **action**: Work to perform

### Workflow Status

- **PREP**: Created but not submitted
- **RUNNING**: Currently executing
- **SUCCEEDED**: Completed successfully
- **KILLED**: Terminated by user
- **FAILED**: Failed due to error

---

## Coordinator Jobs

Coordinator jobs schedule workflows based on time and data:

### Coordinator Definition

```xml
<coordinator-app name="my-coordinator"
    xmlns="uri:oozie:coordinator:0.4"
    frequency="${coord:days(1)}"
    start="2024-01-01T00:00Z"
    end="2024-12-31T00:00Z"
    timezone="UTC">
    
    <controls>
        <timeout>3600</timeout>
        <concurrency>1</concurrency>
        <execution>FIFO</execution>
        <throttle>12</throttle>
    </controls>
    
    <datasets>
        <dataset name="input-dataset" frequency="${coord:days(1)}">
            <uri-template>hdfs:///data/input/${year}/${month}/${day}</uri-template>
            <initial-instance>2024-01-01T00:00Z</initial-instance>
            <event-instance>${coord:current(0)}</event-instance>
        </dataset>
    </datasets>
    
    <input-events>
        <input name="input" dataset="input-dataset">
            <start-instance>${coord:current(0)}</start-instance>
            <end-instance>${coord:current(0)}</end-instance>
        </input>
    </input-events>
    
    <action>
        <workflow>
            <app-path>${oozie.wf.application.path}</app-path>
            <configuration>
                <property>
                    <name>input_dir</name>
                    <value>${coord:dataIn('input')}</value>
                </property>
            </configuration>
        </workflow>
    </action>
</coordinator-app>
```

### Coordinator Features

- **Time-based scheduling**: Cron-like triggers
- **Data availability**: Trigger on data arrival
- **SLA monitoring**: Track workflow deadlines
- **Concurrency control**: Limit parallel runs
- **Execution policies**: FIFO, LIFO, LAST_ONLY

### Coordinator Status

- **PREP**: Created
- **RUNNING**: Active and scheduling
- **SUCCEEDED**: All workflows completed
- **KILLED**: Terminated by user
- **FAILED**: Error occurred

---

## Bundle Jobs

Bundle jobs group multiple coordinators:

### Bundle Definition

```xml
<bundle-app name="my-bundle"
    xmlns="uri:oozie:bundle:0.2"
    start="2024-01-01T00:00Z"
    end="2024-12-31T00:00Z">
    
    <coordinator name="daily-coordinator"
        freq="${coord:days(1)}"
        start="2024-01-01T00:00Z"
        end="2024-12-31T00:00Z"
        timezone="UTC">
        <app-path>hdfs:///oozie/coordinators/daily-coordinator.xml</app-path>
        <configuration>
            <property>
                <name>input_path</name>
                <value>/data/daily</value>
            </property>
        </configuration>
    </coordinator>
    
    <coordinator name="hourly-coordinator"
        freq="${coord:hours(1)}"
        start="2024-01-01T00:00Z"
        end="2024-12-31T00:00Z"
        timezone="UTC">
        <app-path>hdfs:///oozie/coordinators/hourly-coordinator.xml</app-path>
        <configuration>
            <property>
                <name>input_path</name>
                <value>/data/hourly</value>
            </property>
        </configuration>
    </coordinator>
</bundle-app>
```

### Bundle Management

```bash
# Submit bundle
oozie job -oozie http://localhost:11000/oozie -run \
    -Doozie.bundle.application.path=hdfs:///oozie/bundles/my-bundle.xml

# Kill bundle
oozie job -oozie http://localhost:11000/oozie -kill <bundle-id>

# Suspend bundle
oozie job -oozie http://localhost:11000/oozie -suspend <bundle-id>

# Resume bundle
oozie job -oozie http://localhost:11000/oozie -resume <bundle-id>

# Status
oozie job -oozie http://localhost:11000/oozie -status <bundle-id>
```

---

## Action Nodes

### Map-Reduce Action

```xml
<action name="mapreduce">
    <map-reduce>
        <job-tracker>${jobTracker}</job-tracker>
        <name-node>${nameNode}</name-node>
        <configuration>
            <property>
                <name>mapred.input.dir</name>
                <value>${input}</value>
            </property>
            <property>
                <name>mapred.output.dir</name>
                <value>${output}</value>
            </property>
            <property>
                <name>mapred.mapper.class</name>
                <value>org.apache.hadoop.examples.WordCount$Mapper</value>
            </property>
            <property>
                <name>mapred.reducer.class</name>
                <value>org.apache.hadoop.examples.WordCount$Reducer</value>
            </property>
        </configuration>
    </map-reduce>
    <ok to="end"/>
    <error to="fail"/>
</action>
```

### Hive Action

```xml
<action name="hive">
    <hive xmlns="uri:oozie:hive-action:0.2">
        <job-tracker>${jobTracker}</job-tracker>
        <name-node>${nameNode}</name-node>
        <script>/path/to/query.hql</script>
        <param>input=${input}</param>
        <param>output=${output}</param>
    </hive>
    <ok to="end"/>
    <error to="fail"/>
</action>
```

### Pig Action

```xml
<action name="pig">
    <pig>
        <job-tracker>${jobTracker}</job-tracker>
        <name-node>${nameNode}</name-node>
        <script>/path/to/script.pig</script>
        <param>INPUT=${input}</param>
        <param>OUTPUT=${output}</param>
    </pig>
    <ok to="end"/>
    <error to="fail"/>
</action>
```

### Sqoop Action

```xml
<action name="sqoop">
    <sqoop xmlns="uri:oozie:sqoop-action:0.2">
        <job-tracker>${jobTracker}</job-tracker>
        <name-node>${nameNode}</name-node>
        <command>import --connect jdbc:mysql://localhost/db
            --table users --target-dir /data/users</command>
        <arg>--username</arg>
        <arg>user</arg>
        <arg>--password</arg>
        <arg>pass</arg>
    </sqoop>
    <ok to="end"/>
    <error to="fail"/>
</action>
```

### Shell Action

```xml
<action name="shell">
    <shell xmlns="uri:oozie:shell-action:0.1">
        <job-tracker>${jobTracker}</job-tracker>
        <name-node>${nameNode}</name-node>
        <exec>/bin/bash</exec>
        <argument>-c</argument>
        <argument>echo "Hello from Oozie"</argument>
    </shell>
    <ok to="end"/>
    <error to="fail"/>
</action>
```

### Java Action

```xml
<action name="java">
    <java>
        <job-tracker>${jobTracker}</job-tracker>
        <name-node>${nameNode}</name-node>
        <main-class>com.example.MyApplication</main-class>
        <arg>--input</arg>
        <arg>${input}</arg>
        <arg>--output</arg>
        <arg>${output}</arg>
        <file>/path/to/lib.jar</file>
    </java>
    <ok to="end"/>
    <error to="fail"/>
</action>
```

---

## Decision Nodes

Decision nodes control workflow branching:

### Basic Decision

```xml
<decision name="check-date">
    <switch>
        <case to="weekday-job">
            ${fn:dayOfWeek(coord:actionOptional()) == 1}
        </case>
        <case to="weekend-job">
            ${fn:dayOfWeek(coord:actionOptional()) == 7}
        </case>
        <default to="weekday-job"/>
    </switch>
</decision>
```

### Data Availability Check

```xml
<decision name="check-data">
    <switch>
        <case to="process-data">
            ${fs:exists('/data/input/${year}/${month}/${day}')}
        </case>
        <default to="no-data"/>
    </switch>
</decision>
```

### File Size Check

```xml
<decision name="check-file-size">
    <switch>
        <case to="process-file">
            ${fs:fileSize('/data/input/file.csv') > 1024 * 1024}
        </case>
        <default to="skip"/>
    </switch>
</decision>
```

### Complex Decision

```xml
<decision name="complex-check">
    <switch>
        <case to="high-priority">
            ${priority == 'high' && fs:exists('/data/input/urgent')}
        </case>
        <case to="medium-priority">
            ${priority == 'medium'}
        </case>
        <default to="low-priority"/>
    </switch>
</decision>
```

---

## EL Functions

Expression Language functions for workflow logic:

### Workflow Functions

```xml
${wf:id()}              <!-- Current workflow ID -->
${wf:name()}            <!-- Workflow name -->
${wf:run()}             <!-- Run number -->
${wf:lastErrorNode()}   <!-- Last failed node -->
${wf:errorCode()}       <!-- Error code -->
${wf:errorMessage()}    <!-- Error message -->
${wf:transition('node')}  <!-- Node transition -->
${wf:absoluteUrl('/path')} <!-- HDFS absolute URL -->
```

### Coordinator Functions

```xml
${coord:id()}           <!-- Coordinator ID -->
${coord:name()}         <!-- Coordinator name -->
${coord:run()}          <!-- Run number -->
${coord:status()}       <!-- Coordinator status -->
${coord:dataIn('name')} <!-- Input data path -->
${coord:dataOut('name')} <!-- Output data path -->
${coord:current(0)}     <!-- Current instance -->
${coord:previous(1)}    <!-- Previous instance -->
${coord:next(1)}        <!-- Next instance }
${coord:days(1)}        <!-- Frequency in days -->
${coord:hours(1)}       <!-- Frequency in hours -->
${coord:minutes(1)}     <!-- Frequency in minutes -->
```

### FS Functions

```xml
${fs:exists('/path')}           <!-- Check file exists -->
${fs:fileSize('/path')}         <!-- Get file size -->
${fs:dirSize('/path')}          <!-- Get directory size -->
${fs:filenames('/path/*')}      <!-- List files -->
${fs:isFile('/path')}           <!-- Check if file -->
${fs:isDir('/path')}            <!-- Check if directory -->
${fs:rename('/old', '/new')}    <!-- Rename file -->
${fs:delete('/path')}           <!-- Delete file -->
${fs:mkdir('/path')}            <!-- Create directory -->
${fs:touchz('/path')}           <!-- Create empty file -->
${fs:chmod(755, '/path')}       <!-- Change permissions }
${fs:chown('user', '/path')}    <!-- Change ownership }
```

### Date/Time Functions

```xml
${coord:dateOffset(coord:current(0), -1, 'DAY')}   <!-- Date offset -->
${coord:formatDate(coord:current(0), 'yyyy-MM-dd')} <!-- Format date -->
${fn:dayOfWeek(coord:actionOptional())}             <!-- Day of week }
```

---

## SLA

Service Level Agreements for monitoring:

### SLA Definition

```xml
<coordinator-app name="coordinator-with-sla"
    xmlns="uri:oozie:coordinator:0.4"
    xmlns:sla="uri:oozie:sla:0.2"
    frequency="${coord:days(1)}"
    start="2024-01-01T00:00Z"
    end="2024-12-31T00:00Z"
    timezone="UTC">
    
    <sla:info>
        <sla:sla-events>
            <sla:workflow-submission/>
            <sla:workflow-start/>
            <sla:workflow-end/>
        </sla:sla-events>
        <sla:notification-msg>SLA notification: ${sla:nominalTime()}</sla:notification-msg>
        <sla:contact>admin@example.com</sla:contact>
        <sla:alert-server>http://localhost:11000/oozie</sla:alert-server>
        <sla:sla-summary>${coord:name()}</sla:sla-summary>
    </sla:info>
    
    <!-- ... rest of coordinator ... -->
</coordinator-app>
```

### SLA Events

- **workflow-submission**: When workflow is submitted
- **workflow-start**: When workflow begins execution
- **workflow-end**: When workflow completes

### SLA Configuration

```properties
# oozie-site.xml
<property>
    <name>oozie.sla.service.impl</name>
    <value>org.apache.oozie.sla.DefaultSLAService</value>
</property>
<property>
    <name>oozie.sla.event.processor.class</name>
    <value>org.apache.oozie.sla.event.SLAEventProcessor</value>
</property>
```

---

## Bundling

Bundling multiple coordinators:

### Bundle Benefits

1. **Grouping**: Organize related coordinators
2. **Lifecycle**: Manage all coordinators together
3. **Dependency**: Define coordinator dependencies
4. **Monitoring**: Single point of monitoring
5. **Control**: Suspend/resume entire bundle

### Bundle Best Practices

1. Group by business domain
2. Define clear dependencies
3. Use meaningful naming
4. Set appropriate timeouts
5. Monitor SLA compliance

---

## Oozie vs Airflow

| Feature | Oozie | Airflow |
|---------|-------|---------|
| Configuration | XML-based | Python code |
| Workflow Definition | Declarative | Imperative |
| Scheduling | Time/data-based | Cron-like |
| Operators | Limited | Extensive |
| UI | Basic | Advanced |
| Ecosystem | Hadoop-focused | Platform-agnostic |
| Community | Mature | Active |
| Learning Curve | Moderate | Moderate |
| Extensibility | Limited | High |
| Cloud Support | Limited | Extensive |

### When to Use Oozie

- Pure Hadoop ecosystem
- Existing Hadoop infrastructure
- Simple workflow requirements
- Time-based scheduling

### When to Use Airflow

- Multi-platform environments
- Complex dependencies
- Rich UI requirements
- Extensive integrations

### Migration Considerations

1. Assess workflow complexity
2. Evaluate ecosystem dependencies
3. Plan for training requirements
4. Consider tooling integration
5. Test thoroughly before migration

---

## Further Reading

- [Oozie Documentation](https://oozie.apache.org/)
- [Oozie Tutorial](https://oozie.apache.org/docs/4.3.1/index.html)
- [Oozie Examples](https://oozie.apache.org/docs/4.3.1/index.html)
