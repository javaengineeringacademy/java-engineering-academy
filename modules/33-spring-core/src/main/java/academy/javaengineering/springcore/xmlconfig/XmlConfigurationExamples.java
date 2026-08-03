package academy.javaengineering.springcore.xmlconfig;

/**
 * XML Configuration examples for Spring beans.
 */
public class XmlConfigurationExamples {

    // This class demonstrates XML configuration concepts
    // Actual XML files would be in src/main/resources/

    /*
    Example Spring XML Configuration:
    
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://www.springframework.org/schema/beans
               http://www.springframework.org/schema/beans/spring-beans.xsd">
    
        <!-- 1. Constructor Injection -->
        <bean id="emailService" 
              class="academy.javaengineering.springcore.xmlconfig.XmlConfigExample$EmailService">
            <constructor-arg value="smtp.gmail.com"/>
            <constructor-arg value="587"/>
        </bean>
    
        <!-- 2. Setter Injection -->
        <bean id="databaseService" 
              class="academy.javaengineering.springcore.xmlconfig.XmlConfigExample$DatabaseService">
            <property name="url" value="jdbc:mysql://localhost:3306/mydb"/>
            <property name="username" value="admin"/>
            <property name="password" value="secret"/>
        </bean>
    
        <!-- 3. Interface Injection -->
        <bean id="reportingService" 
              class="academy.javaengineering.springcore.di.AdvancedDIExamples$ReportingService">
            <property name="notificationService" ref="emailNotificationService"/>
        </bean>
    
        <!-- 4. Bean with Init/Destroy -->
        <bean id="lifecycleBean" 
              class="academy.javaengineering.springcore.beanlifecycle.BeanLifecycleExample$MyBean"
              init-method="initMethod" destroy-method="destroyMethod">
            <property name="name" value="Lifecycle Demo"/>
        </bean>
    
        <!-- 5. Alias -->
        <alias name="emailService" alias="mailSender"/>
    
        <!-- 6. Import other XML -->
        <import resource="classpath:other-beans.xml"/>
    
    </beans>
    */
}
