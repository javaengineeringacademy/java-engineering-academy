# Template Method Pattern (JavaScript)

## Overview

The Template Method pattern defines the skeleton of an algorithm in a base class,
letting subclasses override specific steps. JavaScript's prototype chain and higher-order
functions enable template implementations.

## When to Use

- Common algorithm structure with varying implementations
- Eliminating code duplication
- Enforcing algorithm structure
- Subclass customization points

## JavaScript Implementation

### Class-Based Template

```javascript
class DataMiner {
  mine() {
    this.openFile();
    this.extractData();
    this.parseData();
    this.analyzeData();
    this.sendReport();
    this.closeFile();
  }

  openFile() { console.log('Opening file'); }
  extractData() { console.log('Extracting data'); }
  parseData() { console.log('Parsing data'); }
  analyzeData() { console.log('Analyzing'); }
  sendReport() { console.log('Sending report'); }
  closeFile() { console.log('Closing file'); }
}

class CSVDataMiner extends DataMiner {
  openFile() { console.log('Opening CSV'); }
  extractData() { console.log('Extracting CSV'); }
  sendReport() { console.log('Sending CSV report'); }
  closeFile() { console.log('Closing CSV'); }
}
```

### Functional Template

```javascript
function createTemplate(steps) {
  return function execute() {
    steps.forEach(step => step());
  };
}

const steps = [
  () => console.log('Step 1'),
  () => console.log('Step 2'),
  () => console.log('Step 3')
];

const pipeline = createTemplate(steps);
```

### Pipeline Template

```javascript
function createPipeline(...stages) {
  return {
    execute(input) {
      return stages.reduce((acc, stage) => stage(acc), input);
    }
  };
}

const process = createPipeline(
  (x) => x + 1,
  (x) => x * 2,
  (x) => x - 3
);

console.log(process.execute(5));
```

### Hook Methods

```javascript
class WebCrawler {
  async crawl() {
    if (this.beforeCrawl()) {
      await this.connect();
      await this.download();
      await this.process();
      this.afterCrawl();
    }
  }

  beforeCrawl() { return true; }
  afterCrawl() {}
  async connect() { console.log('Connecting'); }
  async download() { console.log('Downloading'); }
  async process() { console.log('Processing'); }
}
```

## Best Practices

- Keep template method small
- Use hook methods for optional steps
- Document customization points
- Consider using composition for simple templates
- Avoid calling virtual methods from constructor

## Interview Questions

1. How does Template Method differ from Strategy?
2. What are hook methods?
3. Can template methods be async?
4. How do you handle template method with parameters?
5. When should you use Template Method vs composition?

## References

- MDN: Template Method Pattern
- "Learning JavaScript Design Patterns" by Addy Osmani
- "Clean Code" by Robert C. Martin
