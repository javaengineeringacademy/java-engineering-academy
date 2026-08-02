package academy.javaengineering.reactive;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

/**
 * Demonstrates reactive streams implementation.
 */
public class ReactiveStreamsExample {

    public static class SimpleProcessor extends SubmissionPublisher<String> 
            implements Flow.Processor<String, String> {
        
        private Flow.Subscription subscription;

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            subscription.request(1);
        }

        @Override
        public void onNext(String item) {
            submit("Processed: " + item.toUpperCase());
            subscription.request(1);
        }

        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
        }

        @Override
        public void onComplete() {
            close();
        }
    }

    public static void demoReactiveStream() {
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
        SimpleProcessor processor = new SimpleProcessor();
        
        publisher.subscribe(processor);
        processor.subscribe(new Flow.Subscriber<>() {
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(String item) {
                System.out.println("Received: " + item);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Stream completed");
            }
        });

        publisher.submit("hello");
        publisher.submit("reactive");
        publisher.close();
    }
}
