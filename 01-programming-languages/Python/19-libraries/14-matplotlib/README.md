# Matplotlib

## Why Matplotlib Exists

Every Python developer who analyzes data needs to visualize results. Numbers in a terminal don't reveal patterns the way charts and graphs do. Matplotlib was created to provide publication-quality plotting in Python. It supports line plots, scatter plots, histograms, bar charts, and dozens of other chart types. It's the foundation for all Python visualization libraries.

## What You'll Learn

By the end of this section, you'll be able to:

- Create line plots, scatter plots, bar charts, and histograms
- Customize plot appearance with styles, labels, and legends
- Save figures in multiple formats (PNG, SVG, PDF)

## When to Use Matplotlib

| Use Case | Why Matplotlib | Alternative |
|----------|---------------|-------------|
| Line plots | Publication-quality output | Plotly |
| Scatter plots | Customizable markers and colors | Seaborn |
| Bar charts | Simple grouped/stacked bars | Plotly |
| Histograms | Flexible binning and normalization | Seaborn |
| Subplots | Multi-panel figures | Seaborn |
| Static reports | High-resolution output | Plotly |

## How Matplotlib Works Internally

Matplotlib uses a layered architecture: Figure (canvas), Axes (plot area), and Artists (visual elements). When you call `plt.plot()`, it creates Line2D artists and adds them to the current Axes. The rendering pipeline converts these artists into pixels using backends (Agg for PNG, PDF for vector output).

The pyplot interface (`plt.plot()`, `plt.show()`) is a convenience layer that manages state like MATLAB. For more control, use the object-oriented API: `fig, ax = plt.subplots(); ax.plot()`. This is recommended for production code because it's explicit and avoids global state.

```python
import matplotlib.pyplot as plt

# Simple line plot
x = [1, 2, 3, 4, 5]
y = [2, 4, 6, 8, 10]
plt.plot(x, y, label='Growth')
plt.xlabel('X')
plt.ylabel('Y')
plt.title('Simple Plot')
plt.legend()
plt.savefig('plot.png')

# Object-oriented API
fig, axes = plt.subplots(1, 2, figsize=(10, 4))
axes[0].plot(x, y)
axes[1].scatter(x, y)
plt.savefig('subplots.png')
```

## Production Checklist

### ✅ Before using Matplotlib in production:

☐ I know the time/space complexity
☐ I know common mistakes
☐ I know alternatives
☐ I know limitations
☐ I know how to debug it
☐ I've tested with realistic data volume
☐ I've profiled for performance

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic syntax
- Can write working code

### Level 2: Understands
- Knows time/space complexity
- Understands edge cases

### Level 3: Deep Knowledge
- Knows internal implementation
- Can explain trade-offs

### Level 4: Expert
- Can optimize for specific use cases
- Can debug in production

### Level 5: Master
- Can design custom implementations
- Can teach others

## Common Myths

### ❌ Myth 1: Matplotlib is only for static plots
**Reality:** Matplotlib supports interactive backends (TkAgg, Qt5Agg) and can create animations. For fully interactive web plots, use Plotly or Bokeh.

### ❌ Myth 2: The pyplot API is the best way to use Matplotlib
**Reality:** The object-oriented API is recommended for production code. It's explicit, avoids global state, and is easier to test.

### ❌ Myth 3: Matplotlib is too verbose
**Reality:** Seaborn (built on Matplotlib) provides high-level APIs for common statistical plots. Use Seaborn for quick exploration, Matplotlib for customization.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Publication-quality plotting |
| Complexity | O(n) for most plots |
| Thread Safe | No (pyplot state) |
| Best Alternative | Plotly for interactive |
| When to Use | Static reports, scientific papers |
| When to Avoid | Interactive dashboards |

## Related Topics

- [01-numpy](../01-numpy/) - Data for plots
- [02-pandas](../02-pandas/) - DataFrame plotting integration
- [12-pillow](../12-pillow/) - Image manipulation
