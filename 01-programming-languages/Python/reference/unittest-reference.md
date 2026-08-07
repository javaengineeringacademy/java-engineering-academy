# Python unittest Reference

## What is unittest?

unittest is Python's built-in testing framework. It provides tools for creating and running tests, including test discovery, assertions, fixtures, and mocking.

## Why does unittest matter?

Understanding unittest helps you:
- Write reliable tests
- Ensure code quality
- Catch bugs early
- Maintain code confidence

---

## 1. Basic Test Case

```python
import unittest

class TestStringMethods(unittest.TestCase):
    def test_upper(self):
        self.assertEqual('hello'.upper(), 'HELLO')
    
    def test_lower(self):
        self.assertEqual('HELLO'.lower(), 'hello')
    
    def test_split(self):
        self.assertEqual('hello world'.split(), ['hello', 'world'])

if __name__ == '__main__':
    unittest.main()
```

---

## 2. Assertions

```python
import unittest

class TestAssertions(unittest.TestCase):
    def test_assertions(self):
        # Equality
        self.assertEqual(a, b)
        self.assertNotEqual(a, b)
        
        # Truth
        self.assertTrue(x)
        self.assertFalse(x)
        
        # Identity
        self.assertIs(a, b)
        self.assertIsNot(a, b)
        
        # Membership
        self.assertIn(a, b)
        self.assertNotIn(a, b)
        
        # Type
        self.assertIsInstance(a, Type)
        self.assertNotIsInstance(a, Type)
        
        # Exception
        with self.assertRaises(Exception):
            raise Exception()
        
        # Approximate
        self.assertAlmostEqual(a, b, places=7)
        
        # Sequence
        self.assertListEqual(a, b)
        self.assertTupleEqual(a, b)
        self.assertDictEqual(a, b)
        self.assertSetEqual(a, b)
```

---

## 3. Fixtures

```python
import unittest

class TestWithFixtures(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        # Run once before all tests
        cls.data = 'expensive resource'
    
    @classmethod
    def tearDownClass(cls):
        # Run once after all tests
        pass
    
    def setUp(self):
        # Run before each test
        self.data = 'test data'
    
    def tearDown(self):
        # Run after each test
        pass
    
    def test_something(self):
        self.assertIsNotNone(self.data)
```

---

## 4. Test Discovery

```python
# Command line
python -m unittest discover

# With pattern
python -m unittest discover -p "test_*.py"

# With start directory
python -m unittest discover -s tests
```

---

## 5. Mocking

```python
from unittest import mock

class TestWithMock(unittest.TestCase):
    @mock.patch('module.function')
    def test_with_mock(self, mock_func):
        mock_func.return_value = 'mocked'
        result = module.function()
        self.assertEqual(result, 'mocked')
        mock_func.assert_called_once()
    
    @mock.patch.object(module, 'MyClass')
    def test_with_mock_class(self, MockClass):
        instance = MockClass.return_value
        instance.method.return_value = 'mocked'
        result = module.MyClass().method()
        self.assertEqual(result, 'mocked')
```

---

## 6. Skipping Tests

```python
import unittest
import sys

class TestWithSkips(unittest.TestCase):
    @unittest.skip('Skip this test')
    def test_skipped(self):
        pass
    
    @unittest.skipIf(sys.version_info < (3, 8), 'Requires Python 3.8+')
    def test_conditional_skip(self):
        pass
    
    @unittest.skipUnless(sys.platform == 'win32', 'Windows only')
    def test_windows_only(self):
        pass
    
    def test_expected_failure(self):
        with self.assertRaises(NotImplementedError):
            not_implemented_function()
```

---

## 7. Parameterized Tests

```python
import unittest
from parameterized import parameterized

class TestWithParameterized(unittest.TestCase):
    @parameterized.expand([
        (1, 2, 3),
        (4, 5, 9),
        (10, 20, 30),
    ])
    def test_addition(self, a, b, expected):
        self.assertEqual(a + b, expected)
```

---

## One-Minute Revision Table

| Method | Description | Example |
|--------|-------------|---------|
| **assertEqual** | Check equality | `self.assertEqual(a, b)` |
| **assertNotEqual** | Check inequality | `self.assertNotEqual(a, b)` |
| **assertTrue** | Check truth | `self.assertTrue(x)` |
| **assertFalse** | Check falsehood | `self.assertFalse(x)` |
| **assertIs** | Check identity | `self.assertIs(a, b)` |
| **assertIn** | Check membership | `self.assertIn(a, b)` |
| **assertIsInstance** | Check type | `self.assertIsInstance(a, Type)` |
| **assertRaises** | Check exception | `self.assertRaises(Exception)` |
| **setUp** | Run before test | `def setUp(self):` |
| **tearDown** | Run after test | `def tearDown(self):` |
| **setUpClass** | Run once before all | `@classmethod def setUpClass(cls):` |
| **tearDownClass** | Run once after all | `@classmethod def tearDownClass(cls):` |

---

## Common Mistakes

### 1. Not Using Fixtures

```python
# WRONG
class Test(unittest.TestCase):
    def test1(self):
        data = setup_expensive_resource()
        # test
    
    def test2(self):
        data = setup_expensive_resource()  # Duplicated
        # test

# RIGHT
class Test(unittest.TestCase):
    def setUp(self):
        self.data = setup_expensive_resource()
    
    def test1(self):
        # test
    
    def test2(self):
        # test
```

### 2. Not Cleaning Up

```python
# RIGHT
class Test(unittest.TestCase):
    def setUp(self):
        self.file = open('temp.txt', 'w')
    
    def tearDown(self):
        self.file.close()
        os.remove('temp.txt')
```

### 3. Not Using Mock

```python
# WRONG (testing implementation)
def test_database(self):
    db = Database()
    db.connect()
    # test connection

# RIGHT (mock database)
@mock.patch('module.Database')
def test_database(self, MockDB):
    db = MockDB()
    db.connect.return_value = True
    # test behavior
```

---

## Production Notes

1. **Use setUp/tearDown for fixtures** - Proper resource management
2. **Use mock for external dependencies** - Don't test implementations
3. **Use test discovery** - Organize tests properly
4. **Use parameterized tests** - Reduce duplication
5. **Use skipping for conditional tests** - Platform-specific, etc.
6. **Use assertions appropriately** - Choose the right assertion
7. **Test edge cases** - Empty, null, boundary conditions
8. **Keep tests independent** - Don't depend on other tests
9. **Use descriptive test names** - Test names should describe behavior
10. **Run tests regularly** - Integrate with CI/CD

---

## Further Reading

- Python documentation on unittest module
- Python documentation on unittest.mock module
- Python testing HOWTO
