package academy.javaengineering.interview;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SpringInterviewQuestionsTest {

    @Test
    void shouldRegisterAndRetrieveBean() {
        SpringInterviewQuestions.ApplicationContext ctx = new SpringInterviewQuestions.ApplicationContext();
        SpringInterviewQuestions.ServiceB b = new SpringInterviewQuestions.ServiceB();
        ctx.registerBean("serviceB", b);
        assertEquals(b, ctx.getBean("serviceB"));
    }

    @Test
    void shouldInjectDependencies() {
        SpringInterviewQuestions.ApplicationContext ctx = new SpringInterviewQuestions.ApplicationContext();
        ctx.registerBean("serviceB", new SpringInterviewQuestions.ServiceB());
        SpringInterviewQuestions.ServiceB b = (SpringInterviewQuestions.ServiceB) ctx.getBean("serviceB");
        SpringInterviewQuestions.ServiceA a = new SpringInterviewQuestions.ServiceA(b);
        assertEquals("A->B", a.process());
    }
}
