/*
 * Exercise: Threads in C++
 * Difficulty: ★★★★☆ (4/5)
 * Learning Objectives:
 *   - Create and manage threads with std::thread
 *   - Understand thread synchronization
 *   - Practice using mutexes and locks
 *   - Learn about condition variables
 */

#include <iostream>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <vector>
#include <atomic>
#include <functional>
using namespace std;

/*
 * TODO 1: Create a basic thread
 * - Function that prints thread ID
 * - Join the thread
 */

void print_thread_info() {
    /* Your code here - print this_thread::get_id() */
}

/*
 * TODO 2: Create multiple threads
 * - Launch 5 threads that each print their index
 * - Join all threads
 */

/*
 * TODO 3: Demonstrate race conditions
 * - Create a shared counter
 * - Have multiple threads increment it
 * - Show the incorrect result without synchronization
 */

/*
 * TODO 4: Fix the race condition using std::mutex
 * - Use lock_guard for automatic locking
 */
class Counter {
private:
    int count;
    mutex mtx;

public:
    Counter() : count(0) {}
    void increment() {
        /* Your code here */
    }
    int get_count() const {
        return count;
    }
};

/*
 * TODO 5: Implement a thread-safe queue
 * - Use mutex for synchronization
 * - Use condition_variable for blocking operations
 */
template<typename T>
class ThreadSafeQueue {
private:
    queue<T> data_queue;
    mutable mutex mtx;
    condition_variable cv;

public:
    void push(T value) {
        /* Your code here */
    }

    bool try_pop(T &value) {
        /* Your code here */
        return false;
    }

    void wait_and_pop(T &value) {
        /* Your code here */
    }

    bool empty() const {
        /* Your code here */
        return true;
    }
};

/*
 * TODO 6: Demonstrate std::atomic
 * - Use atomic<int> for a counter
 * - Show it works correctly without mutex
 */

/*
 * TODO 7: Create a producer-consumer pattern
 * - Producer thread generates data
 * - Consumer thread processes data
 * - Use condition_variable for synchronization
 */

/*
 * TODO 8: Demonstrate thread-safe singleton pattern
 * - Use std::call_once or static local variable
 */

/*
 * TODO 9: Implement parallel sum
 * - Split array into chunks
 * - Sum each chunk in a separate thread
 * - Combine results
 */
int parallel_sum(const vector<int> &data, int num_threads) {
    /* Your code here */
    return 0;
}

int main() {
    /* Test cases */
    /*
    cout << "=== Thread Test Cases ===" << endl;

    // Test basic thread
    cout << "\n--- Basic Thread ---" << endl;
    thread t1(print_thread_info);
    t1.join();

    // Test multiple threads
    cout << "\n--- Multiple Threads ---" << endl;
    vector<thread> threads;
    for (int i = 0; i < 5; i++) {
        threads.emplace_back([i]() {
            cout << "Thread " << i << " running" << endl;
        });
    }
    for (auto &t : threads) t.join();

    // Test race condition fix
    cout << "\n--- Counter (Thread-Safe) ---" << endl;
    Counter counter;
    vector<thread> counter_threads;
    for (int i = 0; i < 10; i++) {
        counter_threads.emplace_back([&counter]() {
            for (int j = 0; j < 1000; j++) {
                counter.increment();
            }
        });
    }
    for (auto &t : counter_threads) t.join();
    cout << "Final count: " << counter.get_count() << " (expected: 10000)" << endl;

    // Test ThreadSafeQueue
    cout << "\n--- Thread-Safe Queue ---" << endl;
    ThreadSafeQueue<int> tsq;
    tsq.push(10);
    tsq.push(20);
    int val;
    tsq.try_pop(val);
    cout << "Popped: " << val << " (expected: 10)" << endl;

    // Test atomic
    cout << "\n--- Atomic Counter ---" << endl;
    atomic<int> atomic_count(0);
    vector<thread> atomic_threads;
    for (int i = 0; i < 10; i++) {
        atomic_threads.emplace_back([&atomic_count]() {
            for (int j = 0; j < 1000; j++) {
                atomic_count++;
            }
        });
    }
    for (auto &t : atomic_threads) t.join();
    cout << "Atomic count: " << atomic_count << " (expected: 10000)" << endl;

    // Test parallel sum
    cout << "\n--- Parallel Sum ---" << endl;
    vector<int> data(1000);
    for (int i = 0; i < 1000; i++) data[i] = i + 1;
    int sum = parallel_sum(data, 4);
    cout << "Parallel sum: " << sum << " (expected: 500500)" << endl;
    */

    return 0;
}
