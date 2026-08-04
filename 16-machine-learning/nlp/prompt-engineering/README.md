# Prompt Engineering

## Overview

Prompt engineering is the practice of designing inputs to guide language models toward desired outputs.

## Basic Techniques

### Zero-Shot Prompting

```python
def zero_shot(prompt):
    return f"""{prompt}"""

# Example
prompt = zero_shot("Classify the sentiment: 'This product is amazing!'")
```

### Few-Shot Prompting

```python
def few_shot(examples, query):
    prompt = "Examples:\n"
    for ex in examples:
        prompt += f"Input: {ex['input']}\nOutput: {ex['output']}\n\n"
    prompt += f"Input: {query}\nOutput:"
    return prompt

# Example
examples = [
    {"input": "I love this!", "output": "positive"},
    {"input": "This is terrible.", "output": "negative"}
]
query = "It's okay, nothing special."
prompt = few_shot(examples, query)
```

### Chain-of-Thought (CoT)

```python
def chain_of_thought(question):
    return f"""Question: {question}

Let's think step by step:
Step 1: 
Step 2: 
Step 3: 

Answer:"""

# Example
question = "If a train travels at 60 mph for 2.5 hours, how far does it go?"
prompt = chain_of_thought(question)
```

### Zero-Shot CoT

```python
def zero_shot_cot(question):
    return f"""Question: {question}

Let's think step by step."""

prompt = zero_shot_cot("What is 15% of 200?")
```

---

## Advanced Techniques

### Self-Consistency

```python
def self_consistency(question, n_samples=5):
    prompt = f"""Question: {question}

Provide {n_samples} different reasoning paths and the final answer for each.

Path 1:
Reasoning: ...
Answer: ...

Path 2:
Reasoning: ...
Answer: ...

Select the most common answer as the final answer."""
    return prompt
```

### Tree of Thoughts

```python
def tree_of_thoughts(problem):
    return f"""Problem: {problem}

Thought 1: Consider approach A...
  - Pros: ...
  - Cons: ...

Thought 2: Consider approach B...
  - Pros: ...
  - Cons: ...

Thought 3: Consider approach C...
  - Pros: ...
  - Cons: ...

Best approach: ..."""
```

### ReAct (Reasoning + Acting)

```python
def react_prompt(question, tools):
    tools_desc = "\n".join([f"- {t['name']}: {t['description']}" for t in tools])
    
    return f"""Answer the following questions as best you can. You have access to the following tools:

{tools_desc}

Use the following format:

Question: the input question you must answer
Thought: you should always think about what to do
Action: the action to take
Observation: the result of the action
... (this Thought/Action/Action Input/Observation can repeat N times)
Thought: I now know the final answer
Final Answer: the final answer to the original input question

Begin!

Question: {question}
Thought:"""
```

---

## Prompt Templates

### Classification Template

```python
classification_template = """Classify the following text into one of these categories: {categories}

Text: {text}

Category:"""
```

### Summarization Template

```python
summarization_template = """Please summarize the following text in {length} sentences:

Text: {text}

Summary:"""
```

### Code Generation Template

```python
code_template = """Write a {language} function that {description}.

Requirements:
{requirements}

Function:"""
```

---

## Best Practices

1. **Be specific**: Clear instructions yield better results
2. **Provide examples**: Few-shot improves accuracy
3. **Use delimiters**: Separate instructions from content
4. **Specify format**: Request structured output
5. **Iterate and test**: Refine prompts based on outputs

## Common Pitfalls

1. **Ambiguity**: Vague instructions lead to inconsistent outputs
2. **Too long**: Exceeding context limits truncates input
3. **Leading questions**: Bias the model's response
4. **Missing context**: Assuming the model knows implicit information

## Further Reading

- "Prompt Engineering Guide" (DAIR.AI)
- OpenAI prompt engineering guide
- LangChain prompt templates
