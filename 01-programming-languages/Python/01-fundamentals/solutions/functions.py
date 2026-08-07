"""
Module 01: Fundamentals - Functions Solutions
Practice function implementation in Python.
"""


def retry_on_failure(max_retries=3, delay=0.1):
    """Decorator that retries a function on failure."""
    import time

    def decorator(func):
        def wrapper(*args, **kwargs):
            last_exception = None
            for attempt in range(max_retries):
                try:
                    return func(*args, **kwargs)
                except Exception as e:
                    last_exception = e
                    if attempt < max_retries - 1:
                        time.sleep(delay)
            raise last_exception
        return wrapper
    return decorator


def validate_input(*types):
    """Decorator that validates input types."""
    def decorator(func):
        def wrapper(*args, **kwargs):
            for i, (arg, expected_type) in enumerate(zip(args, types)):
                if not isinstance(arg, expected_type):
                    raise TypeError(
                        f"Argument {i} must be {expected_type.__name__}, "
                        f"got {type(arg).__name__}"
                    )
            return func(*args, **kwargs)
        return wrapper
    return decorator


def count_word_frequencies(text):
    """Count frequency of each word in text (case-insensitive)."""
    import re
    words = re.findall(r'\b\w+\b', text.lower())
    freq = {}
    for word in words:
        freq[word] = freq.get(word, 0) + 1
    return freq


def top_n_words(word_freq, n=5):
    """Return the top N most common words."""
    sorted_words = sorted(word_freq.items(), key=lambda x: x[1], reverse=True)
    return [word for word, count in sorted_words[:n]]


if __name__ == "__main__":
    print("Testing Functions Solutions...")

    # Test retry decorator
    call_count = 0

    @retry_on_failure(max_retries=3)
    def failing_then_succeeding():
        nonlocal call_count
        call_count += 1
        if call_count < 3:
            raise ValueError("Not yet")
        return "success"

    assert failing_then_succeeding() == "success"
    assert call_count == 3

    # Test validate_input decorator
    @validate_input(int, int)
    def add(a, b):
        return a + b

    assert add(1, 2) == 3
    try:
        add("1", 2)
        assert False, "Should have raised TypeError"
    except TypeError:
        pass

    # Test word frequency
    text = "The cat sat on the mat. The cat is fat."
    freq = count_word_frequencies(text)
    assert freq["the"] == 3
    assert freq["cat"] == 2
    top = top_n_words(freq, 2)
    assert "the" in top

    print("All Functions solutions passed!")
