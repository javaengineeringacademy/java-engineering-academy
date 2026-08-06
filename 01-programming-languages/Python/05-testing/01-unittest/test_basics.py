"""unittest: TestCase, assertEqual, setUp, tearDown."""

import unittest

# ── Code to Test ─────────────────────────────────────────────────────
def add(a, b):
    return a + b

def divide(a, b):
    if b == 0:
        raise ValueError("Cannot divide by zero")
    return a / b

class Calculator:
    def __init__(self):
        self.history = []

    def add(self, a, b):
        result = a + b
        self.history.append(("add", a, b, result))
        return result

    def subtract(self, a, b):
        result = a - b
        self.history.append(("subtract", a, b, result))
        return result

    def get_history(self):
        return self.history.copy()

# ── Basic Test Case ──────────────────────────────────────────────────
class TestMathFunctions(unittest.TestCase):
    def test_add_positive(self):
        self.assertEqual(add(2, 3), 5)

    def test_add_negative(self):
        self.assertEqual(add(-1, -1), -2)

    def test_add_zero(self):
        self.assertEqual(add(0, 0), 0)

    def test_divide(self):
        self.assertAlmostEqual(divide(10, 3), 3.333, places=3)

    def test_divide_by_zero(self):
        with self.assertRaises(ValueError):
            divide(1, 0)

# ── setUp and tearDown ──────────────────────────────────────────────
class TestCalculator(unittest.TestCase):
    def setUp(self):
        """Run before each test method."""
        self.calc = Calculator()

    def tearDown(self):
        """Run after each test method."""
        self.calc = None

    def test_add(self):
        result = self.calc.add(2, 3)
        self.assertEqual(result, 5)

    def test_subtract(self):
        result = self.calc.subtract(10, 3)
        self.assertEqual(result, 7)

    def test_history(self):
        self.calc.add(1, 2)
        self.calc.subtract(5, 3)
        history = self.calc.get_history()
        self.assertEqual(len(history), 2)
        self.assertEqual(history[0][0], "add")

# ── Assert Methods ──────────────────────────────────────────────────
class TestAssertMethods(unittest.TestCase):
    def test_equality(self):
        self.assertEqual(1 + 1, 2)
        self.assertNotEqual(1, 2)

    def test_boolean(self):
        self.assertTrue(True)
        self.assertFalse(False)

    def test_membership(self):
        self.assertIn(3, [1, 2, 3])
        self.assertNotIn(4, [1, 2, 3])

    def test_exceptions(self):
        with self.assertRaises(ZeroDivisionError):
            1 / 0

        with self.assertRaisesRegex(ValueError, "invalid"):
            int("abc")

    def test_approximate(self):
        self.assertAlmostEqual(0.1 + 0.2, 0.3, places=7)

# ── Test Organization ───────────────────────────────────────────────
class TestStringMethods(unittest.TestCase):
    def setUp(self):
        self.s = "Hello, World"

    def test_upper(self):
        self.assertEqual(self.s.upper(), "HELLO, WORLD")

    def test_lower(self):
        self.assertEqual(self.s.lower(), "hello, world")

    def test_split(self):
        self.assertEqual(self.s.split(", "), ["Hello", "World"])

    def test_contains(self):
        self.assertIn("World", self.s)

# ── Skipping Tests ──────────────────────────────────────────────────
class TestSkipped(unittest.TestCase):
    @unittest.skip("Skipping this test")
    def test_skipped(self):
        self.fail("Should not run")

    @unittest.skipIf(1 + 1 == 2, "Always skip")
    def test_conditional_skip(self):
        pass

    @unittest.skipUnless(False, "Skip unless True")
    def test_unless_skip(self):
        pass

# ── Test Suite ──────────────────────────────────────────────────────
def suite():
    test_suite = unittest.TestSuite()
    test_suite.addTest(TestMathFunctions('test_add_positive'))
    test_suite.addTest(TestCalculator('test_add'))
    return test_suite

if __name__ == "__main__":
    unittest.main(verbosity=2)
