# Python Fundamentals

## Overview
Python is a high-level, interpreted language known for readability and versatility. Used in web, data science, AI.

## Topics
- Variables and Types
- Control Flow
- Functions
- Lists and Tuples
- Dictionaries and Sets
- Classes and Objects
- Exception Handling
- File I/O
- Modules and Packages
- Virtual Environments

## Learning Objectives
- Write Pythonic code
- Use Python's rich standard library
- Create reusable modules

## Prerequisites
- Basic programming knowledge

## Architecture

```mermaid
graph TD
    A[Python Core] --> B[Web Frameworks]
    A --> C[Data Science]
    B --> D[ML/AI Libraries]
    C --> D
    D --> E[Production Deployment]

    B --> B1[FastAPI]
    B --> B2[Django]
    B --> B3[Flask]
    C --> C1[Pandas]
    C --> C2[NumPy]
    D --> D1[TensorFlow]
    D --> D2[PyTorch]

    style A fill:#f9f,stroke:#333,stroke-width:2px
    style D fill:#bbf,stroke:#333,stroke-width:2px
    style E fill:#bfb,stroke:#333,stroke-width:2px
```

## When to Use

```mermaid
graph TD
    Start{Project Type} -->|Data Analysis| Python[Choose Python]
    Start -->|Web App| Web[Choose Framework]
    Start -->|Scripting| Script[Choose Python]
    Start -->|Machine Learning| ML[Choose Python]

    Python -->|Data Science| Pandas[Pandas + NumPy]
    Python -->|Visualization| Matplotlib[Matplotlib/Seaborn]

    Web -->|Fast API| FastAPI[FastAPI]
    Web -->|Full Stack| Django[Django]
    Web -->|Micro| Flask[Flask]

    ML -->|Deep Learning| DL[TensorFlow/PyTorch]
    ML -->|Classical ML| Sklearn[Scikit-learn]

    style Python fill:#f96,stroke:#333,stroke-width:2px
    style FastAPI fill:#6cf,stroke:#333,stroke-width:2px
    style DL fill:#bfb,stroke:#333,stroke-width:2px
```
