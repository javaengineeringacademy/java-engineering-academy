# Convolutional Neural Networks (CNN)

## Overview

CNNs are specialized neural networks for grid-like data (images), using convolutional layers to automatically learn spatial features.

## CNN Layers

### Convolutional Layer

```python
import torch
import torch.nn as nn

class ConvLayer(nn.Module):
    def __init__(self, in_channels, out_channels, kernel_size, stride=1, padding=0):
        super().__init__()
        self.conv = nn.Conv2d(in_channels, out_channels, kernel_size, stride, padding)
        self.relu = nn.ReLU()
    
    def forward(self, x):
        return self.relu(self.conv(x))

# Example
conv = ConvLayer(3, 64, kernel_size=3, stride=1, padding=1)
x = torch.randn(1, 3, 32, 32)
out = conv(x)
print(f"Output shape: {out.shape}")  # [1, 64, 32, 32]
```

### Pooling Layer

```python
class PoolingLayer(nn.Module):
    def __init__(self, pool_type='max', kernel_size=2):
        super().__init__()
        if pool_type == 'max':
            self.pool = nn.MaxPool2d(kernel_size)
        elif pool_type == 'avg':
            self.pool = nn.AvgPool2d(kernel_size)
        elif pool_type == 'adaptive':
            self.pool = nn.AdaptiveAvgPool2d((1, 1))
    
    def forward(self, x):
        return self.pool(x)

# Example
max_pool = nn.MaxPool2d(2)
avg_pool = nn.AvgPool2d(2)
adaptive_pool = nn.AdaptiveAvgPool2d((1, 1))

x = torch.randn(1, 64, 32, 32)
print(f"Max pool: {max_pool(x).shape}")  # [1, 64, 16, 16]
print(f"Avg pool: {avg_pool(x).shape}")  # [1, 64, 16, 16]
print(f"Adaptive: {adaptive_pool(x).shape}")  # [1, 64, 1, 1]
```

### Batch Normalization

```python
class ConvBNReLU(nn.Module):
    def __init__(self, in_ch, out_ch):
        super().__init__()
        self.conv = nn.Conv2d(in_ch, out_ch, 3, padding=1, bias=False)
        self.bn = nn.BatchNorm2d(out_ch)
        self.relu = nn.ReLU(inplace=True)
    
    def forward(self, x):
        return self.relu(self.bn(self.conv(x)))
```

---

## Classic Architectures

### LeNet-5

```python
class LeNet5(nn.Module):
    def __init__(self, num_classes=10):
        super().__init__()
        self.features = nn.Sequential(
            nn.Conv2d(1, 6, 5),
            nn.Tanh(),
            nn.AvgPool2d(2, 2),
            nn.Conv2d(6, 16, 5),
            nn.Tanh(),
            nn.AvgPool2d(2, 2)
        )
        self.classifier = nn.Sequential(
            nn.Linear(16 * 5 * 5, 120),
            nn.Tanh(),
            nn.Linear(120, 84),
            nn.Tanh(),
            nn.Linear(84, num_classes)
        )
    
    def forward(self, x):
        x = self.features(x)
        x = x.view(x.size(0), -1)
        return self.classifier(x)
```

### VGG

```python
class VGG(nn.Module):
    def __init__(self, num_classes=1000):
        super().__init__()
        self.features = self._make_layers([64, 64, 'M', 128, 128, 'M', 
                                           256, 256, 256, 'M', 512, 512, 512, 'M'])
        self.classifier = nn.Sequential(
            nn.Linear(512 * 7 * 7, 4096),
            nn.ReLU(True),
            nn.Dropout(),
            nn.Linear(4096, 4096),
            nn.ReLU(True),
            nn.Dropout(),
            nn.Linear(4096, num_classes)
        )
    
    def _make_layers(self, cfg):
        layers = []
        in_channels = 3
        for x in cfg:
            if x == 'M':
                layers.append(nn.MaxPool2d(2, 2))
            else:
                layers.extend([
                    nn.Conv2d(in_channels, x, 3, padding=1),
                    nn.ReLU(True)
                ])
                in_channels = x
        return nn.Sequential(*layers)
```

### GoogLeNet (Inception)

```python
class InceptionModule(nn.Module):
    def __init__(self, in_ch, ch1x1, ch3x3_reduce, ch3x3, ch5x5_reduce, ch5x5, pool_proj):
        super().__init__()
        self.branch1 = nn.Sequential(
            nn.Conv2d(in_ch, ch1x1, 1),
            nn.ReLU(True)
        )
        self.branch2 = nn.Sequential(
            nn.Conv2d(in_ch, ch3x3_reduce, 1),
            nn.ReLU(True),
            nn.Conv2d(ch3x3_reduce, ch3x3, 3, padding=1),
            nn.ReLU(True)
        )
        self.branch3 = nn.Sequential(
            nn.Conv2d(in_ch, ch5x5_reduce, 1),
            nn.ReLU(True),
            nn.Conv2d(ch5x5_reduce, ch5x5, 5, padding=2),
            nn.ReLU(True)
        )
        self.branch4 = nn.Sequential(
            nn.MaxPool2d(3, stride=1, padding=1),
            nn.Conv2d(in_ch, pool_proj, 1),
            nn.ReLU(True)
        )
    
    def forward(self, x):
        return torch.cat([self.branch1(x), self.branch2(x), 
                         self.branch3(x), self.branch4(x)], dim=1)
```

### ResNet

```python
class ResidualBlock(nn.Module):
    def __init__(self, in_channels, out_channels, stride=1):
        super().__init__()
        self.conv1 = nn.Conv2d(in_channels, out_channels, 3, stride=stride, padding=1)
        self.bn1 = nn.BatchNorm2d(out_channels)
        self.conv2 = nn.Conv2d(out_channels, out_channels, 3, padding=1)
        self.bn2 = nn.BatchNorm2d(out_channels)
        self.relu = nn.ReLU(inplace=True)
        
        self.shortcut = nn.Sequential()
        if stride != 1 or in_channels != out_channels:
            self.shortcut = nn.Sequential(
                nn.Conv2d(in_channels, out_channels, 1, stride=stride),
                nn.BatchNorm2d(out_channels)
            )
    
    def forward(self, x):
        residual = self.shortcut(x)
        out = self.relu(self.bn1(self.conv1(x)))
        out = self.bn2(self.conv2(out))
        out += residual
        return self.relu(out)
```

---

## Feature Visualization

```python
def visualize_filters(model):
    import matplotlib.pyplot as plt
    
    first_layer = next(model.children())[0]
    filters = first_layer.weight.data.cpu()
    
    fig, axes = plt.subplots(8, 8, figsize=(10, 10))
    for i, ax in enumerate(axes.flat):
        if i < filters.shape[0]:
            ax.imshow(filters[i, 0], cmap='gray')
        ax.axis('off')
    plt.tight_layout()
    plt.show()

def visualize_activations(model, image):
    activations = {}
    
    def hook_fn(module, input, output):
        activations[module] = output
    
    # Register hooks
    for layer in model.children():
        layer.register_forward_hook(hook_fn)
    
    # Forward pass
    with torch.no_grad():
        model(image)
    
    # Visualize
    import matplotlib.pyplot as plt
    for layer, activation in activations.items():
        plt.figure(figsize=(12, 4))
        for i in range(min(8, activation.shape[1])):
            plt.subplot(1, 8, i+1)
            plt.imshow(activation[0, i].cpu().numpy(), cmap='viridis')
            plt.axis('off')
        plt.suptitle(f'{layer.__class__.__name__}')
        plt.show()
```

---

## Best Practices

1. **Architecture choice**: Start with ResNet/EfficientNet
2. **Data augmentation**: Random crop, flip, color jitter
3. **Learning rate**: Cosine annealing with warmup
4. **Regularization**: Weight decay, dropout, label smoothing
5. **Transfer learning**: Use pre-trained models

## Further Reading

- "Deep Learning" by Goodfellow et al.
- "Very Deep Convolutional Networks" (VGG)
- "Deep Residual Learning" (ResNet)
