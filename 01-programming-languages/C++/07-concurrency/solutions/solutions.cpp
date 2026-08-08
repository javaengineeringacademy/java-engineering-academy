// Module 07: Concurrency — Solutions
// Study these after attempting the exercises yourself.

#include <iostream>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <vector>
#include <atomic>
#include <queue>
#include <functional>
#include <cassert>

// ============================================================================
// Exercise 1 Solution: Basic Thread
// ============================================================================

void print_thread_info() {
    std::cout << "Thread ID: " << std::this_thread::get_id() << "\n";
}

void exercise1() {
    std::cout << "\n=== Exercise 1: Basic Thread ===\n";

    std::thread t1(print_thread_info);
    t1.join();
    std::cout << "Exercise 1 passed!\n";
}

// ============================================================================
// Exercise 2 Solution: Multiple Threads
// ============================================================================

void exercise2() {
    std::cout << "\n=== Exercise 2: Multiple Threads ===\n";

    std::vector<std::thread> threads;
    for (int i = 0; i < 5; i++) {
        threads.emplace_back([i]() {
            std::cout << "Thread " << i << " running\n";
        });
    }
    for (auto &t : threads) t.join();
    std::cout << "Exercise 2 passed!\n";
}

// ============================================================================
// Exercise 3 & 4 Solution: Race Condition Fix with Mutex
// ============================================================================

class Counter {
private:
    int count;
    mutable std::mutex mtx;
public:
    Counter() : count(0) {}

    void increment() {
        std::lock_guard<std::mutex> lock(mtx);
        ++count;
    }

    int get_count() const {
        std::lock_guard<std::mutex> lock(mtx);
        return count;
    }
};

void exercise3_4() {
    std::cout << "\n=== Exercise 3 & 4: Counter (Thread-Safe) ===\n";

    Counter counter;
    std::vector<std::thread> threads;
    for (int i = 0; i < 10; i++) {
        threads.emplace_back([&counter]() {
            for (int j = 0; j < 1000; j++) {
                counter.increment();
            }
        });
    }
    for (auto &t : threads) t.join();
    std::cout << "Final count: " << counter.get_count() << " (expected: 10000)\n";
    assert(counter.get_count() == 10000);
}

// ============================================================================
// Exercise 5 Solution: Thread-Safe Queue
// ============================================================================

template<typename T>
class ThreadSafeQueue {
private:
    std::queue<T> data_queue;
    mutable std::mutex mtx;
    std::condition_variable cv;

public:
    void push(T value) {
        std::lock_guard<std::mutex> lock(mtx);
        data_queue.push(std::move(value));
        cv.notify_one();
    }

    bool try_pop(T &value) {
        std::lock_guard<std::mutex> lock(mtx);
        if (data_queue.empty()) return false;
        value = std::move(data_queue.front());
        data_queue.pop();
        return true;
    }

    void wait_and_pop(T &value) {
        std::unique_lock<std::mutex> lock(mtx);
        cv.wait(lock, [this] { return !data_queue.empty(); });
        value = std::move(data_queue.front());
        data_queue.pop();
    }

    bool empty() const {
        std::lock_guard<std::mutex> lock(mtx);
        return data_queue.empty();
    }
};

void exercise5() {
    std::cout << "\n=== Exercise 5: Thread-Safe Queue ===\n";

    ThreadSafeQueue<int> tsq;
    tsq.push(10);
    tsq.push(20);
    int val;
    tsq.try_pop(val);
    assert(val == 10);
    std::cout << "Popped: " << val << " (expected: 10)\n";

    tsq.try_pop(val);
    assert(val == 20);
    assert(tsq.empty());
    std::cout << "Exercise 5 passed!\n";
}

// ============================================================================
// Exercise 6 Solution: std::atomic
// ============================================================================

void exercise6() {
    std::cout << "\n=== Exercise 6: Atomic Counter ===\n";

    std::atomic<int> atomic_count(0);
    std::vector<std::thread> threads;
    for (int i = 0; i < 10; i++) {
        threads.emplace_back([&atomic_count]() {
            for (int j = 0; j < 1000; j++) {
                atomic_count++;
            }
        });
    }
    for (auto &t : threads) t.join();
    std::cout << "Atomic count: " << atomic_count << " (expected: 10000)\n";
    assert(atomic_count == 10000);
}

// ============================================================================
// Exercise 7 Solution: Producer-Consumer Pattern
// ============================================================================

std::mutex pc_mutex;
std::condition_variable pc_cv;
std::queue<int> pc_data;
bool pc_finished = false;

void producer() {
    for (int i = 1; i <= 10; i++) {
        {
            std::lock_guard<std::mutex> lock(pc_mutex);
            pc_data.push(i);
            std::cout << "Produced: " << i << "\n";
        }
        pc_cv.notify_one();
        std::this_thread::sleep_for(std::chrono::milliseconds(10));
    }
    {
        std::lock_guard<std::mutex> lock(pc_mutex);
        pc_finished = true;
    }
    pc_cv.notify_all();
}

void consumer() {
    while (true) {
        std::unique_lock<std::mutex> lock(pc_mutex);
        pc_cv.wait(lock, [] { return !pc_data.empty() || pc_finished; });

        while (!pc_data.empty()) {
            int val = pc_data.front();
            pc_data.pop();
            lock.unlock();
            std::cout << "  Consumed: " << val << "\n";
            lock.lock();
        }

        if (pc_finished && pc_data.empty()) break;
    }
}

void exercise7() {
    std::cout << "\n=== Exercise 7: Producer-Consumer ===\n";

    pc_finished = false;
    std::thread prod(producer);
    std::thread cons(consumer);
    prod.join();
    cons.join();
    std::cout << "Exercise 7 passed!\n";
}

// ============================================================================
// Exercise 8 Solution: Thread-Safe Singleton
// ============================================================================

class Singleton {
    static Singleton* instance;
    static std::mutex mtx;
    int value_;

    Singleton() : value_(42) {}

public:
    Singleton(const Singleton&) = delete;
    Singleton& operator=(const Singleton&) = delete;

    static Singleton& getInstance() {
        std::lock_guard<std::mutex> lock(mtx);
        if (!instance) {
            instance = new Singleton();
        }
        return *instance;
    }

    int getValue() const { return value_; }
    void setValue(int v) { value_ = v; }
};

Singleton* Singleton::instance = nullptr;
std::mutex Singleton::mtx;

void exercise8() {
    std::cout << "\n=== Exercise 8: Singleton ===\n";

    auto& s1 = Singleton::getInstance();
    auto& s2 = Singleton::getInstance();
    assert(&s1 == &s2);
    s1.setValue(100);
    assert(s2.getValue() == 100);
    std::cout << "Singleton value: " << s2.getValue() << "\n";
    std::cout << "Exercise 8 passed!\n";
}

// ============================================================================
// Exercise 9 Solution: Parallel Sum
// ============================================================================

int parallel_sum(const std::vector<int> &data, int num_threads) {
    std::vector<int> partial_sums(num_threads, 0);
    std::vector<std::thread> threads;

    int chunk_size = data.size() / num_threads;
    int remainder = data.size() % num_threads;
    int start = 0;

    for (int i = 0; i < num_threads; i++) {
        int end = start + chunk_size + (i < remainder ? 1 : 0);
        threads.emplace_back([&data, &partial_sums, i, start, end]() {
            int sum = 0;
            for (int j = start; j < end; j++) {
                sum += data[j];
            }
            partial_sums[i] = sum;
        });
        start = end;
    }

    for (auto &t : threads) t.join();

    int total = 0;
    for (int s : partial_sums) total += s;
    return total;
}

void exercise9() {
    std::cout << "\n=== Exercise 9: Parallel Sum ===\n";

    std::vector<int> data(1000);
    for (int i = 0; i < 1000; i++) data[i] = i + 1;
    int sum = parallel_sum(data, 4);
    std::cout << "Parallel sum: " << sum << " (expected: 500500)\n";
    assert(sum == 500500);
}

// ============================================================================
// Main
// ============================================================================

int main() {
    std::cout << "=== Module 07: Concurrency Solutions ===\n";

    exercise1();
    exercise2();
    exercise3_4();
    exercise5();
    exercise6();
    exercise7();
    exercise8();
    exercise9();

    std::cout << "\nAll exercises completed!\n";
    return 0;
}
