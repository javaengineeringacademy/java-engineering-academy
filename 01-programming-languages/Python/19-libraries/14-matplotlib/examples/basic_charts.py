import matplotlib.pyplot as plt
import numpy as np

# Line plot
x = np.linspace(0, 10, 100)
plt.figure()
plt.plot(x, np.sin(x), label='sin(x)')
plt.plot(x, np.cos(x), label='cos(x)')
plt.xlabel('x')
plt.ylabel('y')
plt.title('Trigonometric Functions')
plt.legend()
plt.savefig('line_plot.png')
plt.close()

# Bar chart
categories = ['A', 'B', 'C', 'D']
values = [15, 30, 45, 20]
plt.figure()
plt.bar(categories, values)
plt.title('Bar Chart')
plt.savefig('bar_chart.png')
plt.close()

# Scatter plot
x = np.random.randn(50)
y = np.random.randn(50)
plt.figure()
plt.scatter(x, y, alpha=0.5)
plt.title('Scatter Plot')
plt.savefig('scatter_plot.png')
plt.close()

print("Charts created successfully!")
