# Image Classification

## Overview

Image classification assigns labels to images based on their visual content. Deep learning, particularly CNNs, has achieved human-level performance on many benchmarks.

## ImageNet

### Dataset
- 1.2 million training images
- 1000 classes
- 50,000 validation images
- Standard benchmark for image classification

### Top Models

| Model | Year | Top-1 Acc | Parameters | FLOPs |
|-------|------|-----------|------------|-------|
| ResNet-50 | 2015 | 76.1% | 25M | 4.1B |
| EfficientNet-B7 | 2019 | 84.3% | 66M | 37B |
| Vision Transformer | 2020 | 88.5% | 86M | 17.6B |
| ConvNeXt-L | 2022 | 87.8% | 198M | 34.4B |

---

## ResNet

### Implementation

```python
import torch
import torch.nn as nn
import torchvision.models as models
import torchvision.transforms as transforms
from PIL import Image

# Load pre-trained ResNet
model = models.resnet50(pretrained=True)
model.eval()

# Preprocessing
transform = transforms.Compose([
    transforms.Resize(256),
    transforms.CenterCrop(224),
    transforms.ToTensor(),
    transforms.Normalize(mean=[0.485, 0.456, 0.406],
                         std=[0.229, 0.224, 0.225])
])

# Inference
def classify_image(image_path):
    image = Image.open(image_path)
    input_tensor = transform(image).unsqueeze(0)
    
    with torch.no_grad():
        output = model(input_tensor)
        probabilities = torch.nn.functional.softmax(output[0], dim=0)
    
    # Get top 5 predictions
    top5_prob, top5_idx = torch.topk(probabilities, 5)
    
    # Load ImageNet labels
    from torchvision.models import ResNet50_Weights
    labels = ResNet50_Weights.DEFAULT.meta["categories"]
    
    for prob, idx in zip(top5_prob, top5_idx):
        print(f"{labels[idx]}: {prob:.4f}")
```

### ResNet from Scratch

```python
class BasicBlock(nn.Module):
    expansion = 1
    
    def __init__(self, in_channels, out_channels, stride=1, downsample=None):
        super().__init__()
        self.conv1 = nn.Conv2d(in_channels, out_channels, 3, stride=stride, padding=1)
        self.bn1 = nn.BatchNorm2d(out_channels)
        self.conv2 = nn.Conv2d(out_channels, out_channels, 3, padding=1)
        self.bn2 = nn.BatchNorm2d(out_channels)
        self.relu = nn.ReLU(inplace=True)
        self.downsample = downsample
    
    def forward(self, x):
        identity = x
        
        out = self.relu(self.bn1(self.conv1(x)))
        out = self.bn2(self.conv2(out))
        
        if self.downsample is not None:
            identity = self.downsample(x)
        
        out += identity
        return self.relu(out)

class ResNet(nn.Module):
    def __init__(self, block, layers, num_classes=1000):
        super().__init__()
        self.in_channels = 64
        
        self.conv1 = nn.Conv2d(3, 64, kernel_size=7, stride=2, padding=3)
        self.bn1 = nn.BatchNorm2d(64)
        self.relu = nn.ReLU(inplace=True)
        self.maxpool = nn.MaxPool2d(kernel_size=3, stride=2, padding=1)
        
        self.layer1 = self._make_layer(block, 64, layers[0])
        self.layer2 = self._make_layer(block, 128, layers[1], stride=2)
        self.layer3 = self._make_layer(block, 256, layers[2], stride=2)
        self.layer4 = self._make_layer(block, 512, layers[3], stride=2)
        
        self.avgpool = nn.AdaptiveAvgPool2d((1, 1))
        self.fc = nn.Linear(512 * block.expansion, num_classes)
    
    def _make_layer(self, block, out_channels, blocks, stride=1):
        downsample = None
        if stride != 1 or self.in_channels != out_channels * block.expansion:
            downsample = nn.Sequential(
                nn.Conv2d(self.in_channels, out_channels * block.expansion,
                         kernel_size=1, stride=stride),
                nn.BatchNorm2d(out_channels * block.expansion)
            )
        
        layers = [block(self.in_channels, out_channels, stride, downsample)]
        self.in_channels = out_channels * block.expansion
        
        for _ in range(1, blocks):
            layers.append(block(self.in_channels, out_channels))
        
        return nn.Sequential(*layers)
    
    def forward(self, x):
        x = self.relu(self.bn1(self.conv1(x)))
        x = self.maxpool(x)
        
        x = self.layer1(x)
        x = self.layer2(x)
        x = self.layer3(x)
        x = self.layer4(x)
        
        x = self.avgpool(x)
        x = torch.flatten(x, 1)
        return self.fc(x)

# ResNet-50
resnet50 = ResNet(BasicBlock, [3, 4, 6, 3])
```

---

## EfficientNet

### Implementation

```python
from torchvision.models import efficientnet_b0, EfficientNet_B0_Weights

# Load EfficientNet
model = efficientnet_b0(weights=EfficientNet_B0_Weights.DEFAULT)
model.eval()

# Inference
def classify_with_efficientnet(image_path):
    image = Image.open(image_path)
    weights = EfficientNet_B0_Weights.DEFAULT
    transform = weights.transforms()
    
    input_tensor = transform(image).unsqueeze(0)
    
    with torch.no_grad():
        output = model(input_tensor)
        probabilities = torch.nn.functional.softmax(output[0], dim=0)
    
    top5_prob, top5_idx = torch.topk(probabilities, 5)
    labels = weights.meta["categories"]
    
    for prob, idx in zip(top5_prob, top5_idx):
        print(f"{labels[idx]}: {prob:.4f}")
```

---

## Transfer Learning

### Fine-Tuning

```python
import torch.optim as optim
from torch.utils.data import DataLoader, Dataset

class CustomDataset(Dataset):
    def __init__(self, images, labels, transform=None):
        self.images = images
        self.labels = labels
        self.transform = transform
    
    def __len__(self):
        return len(self.images)
    
    def __getitem__(self, idx):
        image = self.images[idx]
        label = self.labels[idx]
        
        if self.transform:
            image = self.transform(image)
        
        return image, label

# Load pre-trained model
model = models.resnet50(pretrained=True)

# Replace classifier
num_classes = 10
model.fc = nn.Linear(model.fc.in_features, num_classes)

# Freeze early layers
for param in list(model.parameters())[:-10]:
    param.requires_grad = False

# Training setup
criterion = nn.CrossEntropyLoss()
optimizer = optim.Adam(filter(lambda p: p.requires_grad, model.parameters()), lr=0.001)

# Training loop
def train_model(model, train_loader, num_epochs=10):
    model.train()
    
    for epoch in range(num_epochs):
        running_loss = 0.0
        correct = 0
        total = 0
        
        for images, labels in train_loader:
            optimizer.zero_grad()
            outputs = model(images)
            loss = criterion(outputs, labels)
            loss.backward()
            optimizer.step()
            
            running_loss += loss.item()
            _, predicted = outputs.max(1)
            total += labels.size(0)
            correct += predicted.eq(labels).sum().item()
        
        print(f"Epoch {epoch+1}: Loss={running_loss/len(train_loader):.4f}, "
              f"Acc={100.*correct/total:.2f}%")
```

---

## Data Augmentation

```python
from torchvision import transforms

train_transform = transforms.Compose([
    transforms.RandomResizedCrop(224),
    transforms.RandomHorizontalFlip(),
    transforms.ColorJitter(brightness=0.2, contrast=0.2, saturation=0.2),
    transforms.RandomRotation(15),
    transforms.ToTensor(),
    transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
])

val_transform = transforms.Compose([
    transforms.Resize(256),
    transforms.CenterCrop(224),
    transforms.ToTensor(),
    transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
])
```

---

## Best Practices

1. **Start with pre-trained models**: Transfer learning saves time
2. **Data augmentation**: Prevent overfitting
3. **Learning rate scheduling**: Cosine annealing or warmup
4. **Regularization**: Weight decay, dropout
5. **Ensemble**: Combine multiple models

## Further Reading

- "Deep Residual Learning for Image Recognition" (ResNet)
- "EfficientNet: Rethinking Model Scaling" 
- PyTorch vision models documentation
