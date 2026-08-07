"""
Module 01: Fundamentals - Strings Solutions
Practice string operations in Python.
"""


def reverse_string(s):
    """Reverse a string without using slicing."""
    result = ""
    for char in s:
        result = char + result
    return result


def count_vowels(s):
    """Count vowels in a string."""
    vowels = "aeiouAEIOU"
    count = 0
    for char in s:
        if char in vowels:
            count += 1
    return count


def is_anagram(s1, s2):
    """Check if two strings are anagrams."""
    s1_clean = s1.lower().replace(" ", "")
    s2_clean = s2.lower().replace(" ", "")

    if len(s1_clean) != len(s2_clean):
        return False

    return sorted(s1_clean) == sorted(s2_clean)


def caesar_cipher(text, shift):
    """Encrypt text using Caesar cipher."""
    result = []
    for char in text:
        if char.isalpha():
            base = ord('A') if char.isupper() else ord('a')
            shifted = (ord(char) - base + shift) % 26 + base
            result.append(chr(shifted))
        else:
            result.append(char)
    return ''.join(result)


def longest_common_substring(s1, s2):
    """Find the longest common substring between two strings."""
    m, n = len(s1), len(s2)
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    max_len = 0
    end_pos = 0

    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if s1[i-1] == s2[j-1]:
                dp[i][j] = dp[i-1][j-1] + 1
                if dp[i][j] > max_len:
                    max_len = dp[i][j]
                    end_pos = i

    return s1[end_pos - max_len:end_pos]


if __name__ == "__main__":
    print("Testing Strings Solutions...")
    assert reverse_string("hello") == "olleh"
    assert count_vowels("Hello World") == 3
    assert is_anagram("listen", "silent") == True
    assert is_anagram("hello", "world") == False
    assert caesar_cipher("Hello", 3) == "Khoor"
    assert longest_common_substring("abcdef", "bcdefg") == "bcdef"
    print("All Strings solutions passed!")
