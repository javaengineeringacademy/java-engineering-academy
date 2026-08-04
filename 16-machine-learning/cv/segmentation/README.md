# Image Segmentation

## Overview

Image segmentation partitions an image into regions, assigning a label to each pixel.

## Semantic Segmentation

### U-Net

```python
import torch
import torch.nn as nn

class UNet(nn.Module):
    def __init__(self, in_channels=1, out_channels=1):
        super().__init__()
        
        # Encoder
        self.enc1 = self.conv_block(in_channels, 64)
        self.enc2 = self.conv_block(64, 128)
        self.enc3 = self.conv_block(128, 256)
        self.enc4 = self.conv_block(256, 512)
        
        # Bottleneck
        self.bottleneck = self.conv_block(512, 1024)
        
        # Decoder
        self.upconv4 = nn.ConvTranspose2d(1024, 512, kernel_size=2, stride=2)
        self.dec4 = self.conv_block(1024, 512)
        self.upconv3 = nn.ConvTranspose2d(512, 256, kernel_size=2, stride=2)
        self.dec3 = self.conv_block(512, 256)
        self.upconv2 = nn.ConvTranspose2d(256, 128, kernel_size=2, stride=2)
        self.dec2 = self.conv_block(256, 128)
        self.upconv1 = nn.ConvTranspose2d(128, 64, kernel_size=2, stride=2)
        self.dec1 = self.conv_block(128, 64)
        
        # Output
        self.out_conv = nn.Conv2d(64, out_channels, kernel_size=1)
    
    def conv_block(self, in_ch, out_ch):
        return nn.Sequential(
            nn.Conv2d(in_ch, out_ch, 3, padding=1),
            nn.BatchNorm2d(out_ch),
            nn.ReLU(inplace=True),
            nn.Conv2d(out_ch, out_ch, 3, padding=1),
            nn.BatchNorm2d(out_ch),
            nn.ReLU(inplace=True)
        )
    
    def forward(self, x):
        # Encoder
        e1 = self.enc1(x)
        e2 = self.enc2(nn.MaxPool2d(2)(e1))
        e3 = self.enc3(nn.MaxPool2d(2)(e2))
        e4 = self.enc4(nn.MaxPool2d(2)(e3))
        
        # Bottleneck
        b = self.bottleneck(nn.MaxPool2d(2)(e4))
        
        # Decoder with skip connections
        d4 = self.dec4(torch.cat([self.upconv4(b), e4], dim=1))
        d3 = self.dec3(torch.cat([self.upconv3(d4), e3], dim=1))
        d2 = self.dec2(torch.cat([self.upconv2(d3), e2], dim=1))
        d1 = self.dec1(torch.cat([self.upconv1(d2), e1], dim=1))
        
        return self.out_conv(d1)

# Usage
model = UNet(in_channels=3, out_channels=21)
x = torch.randn(1, 3, 224, 224)
output = model(x)
print(f"Output shape: {output.shape}")
```

---

## Instance Segmentation

### Mask R-CNN

```python
from torchvision.models.detection import maskrcnn_resnet50_fpn

# Load pre-trained Mask R-CNN
model = maskrcnn_resnet50_fpn(pretrained=True)
model.eval()

# Inference
def instance_segmentation(image_tensor):
    with torch.no_grad():
        predictions = model([image_tensor])
    
    pred = predictions[0]
    return {
        'boxes': pred['boxes'],
        'labels': pred['labels'],
        'scores': pred['scores'],
        'masks': pred['masks']
    }
```

---

## Panoptic Segmentation

### Panoptic FPN

```python
from torchvision.models.detection import panoptic_fpn_resnet101

# Load Panoptic FPN
model = panoptic_fpn_resnet101(pretrained=True)
model.eval()

# Inference
def panoptic_segmentation(image_tensor):
    with torch.no_grad():
        predictions = model([image_tensor])
    
    return predictions
```

---

## Evaluation Metrics

### IoU (Intersection over Union)

```python
def calculate_iou(pred_mask, true_mask):
    intersection = (pred_mask & true_mask).sum()
    union = (pred_mask | true_mask).sum()
    return intersection / union if union > 0 else 0

def mean_iou(pred_masks, true_masks, num_classes):
    ious = []
    for c in range(num_classes):
        pred = pred_masks == c
        true = true_masks == c
        ious.append(calculate_iou(pred, true))
    return np.mean(ious)
```

### Dice Score

```python
def dice_score(pred_mask, true_mask):
    intersection = (pred_mask & true_mask).sum()
    return 2 * intersection / (pred_mask.sum() + true_mask.sum())
```

---

## Best Practices

1. **Data augmentation**: Random crops, flips, color jitter
2. **Loss functions**: Dice loss, Focal loss for imbalanced data
3. **Multi-scale training**: Handle different object sizes
4. **Post-processing**: CRF for semantic, NMS for instance
5. **Evaluation**: Use mIoU for semantic, AP for instance

## Further Reading

- "U-Net: Convolutional Networks for Biomedical Image Segmentation"
- "Mask R-CNN" by He et al.
- Panoptic Segmentation paper
