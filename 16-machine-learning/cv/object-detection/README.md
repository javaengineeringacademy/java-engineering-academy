# Object Detection

## Overview

Object detection localizes and classifies objects in images, providing bounding boxes and class labels.

## YOLO (You Only Look Once)

### Implementation

```python
from ultralytics import YOLO

# Load pre-trained YOLO
model = YOLO('yolov8n.pt')

# Inference
results = model('image.jpg')

# Process results
for result in results:
    boxes = result.boxes
    for box in boxes:
        x1, y1, x2, y2 = box.xyxy[0].tolist()
        confidence = box.conf[0].item()
        class_id = int(box.cls[0].item())
        class_name = result.names[class_id]
        
        print(f"{class_name}: {confidence:.2f} at [{x1:.0f}, {y1:.0f}, {x2:.0f}, {y2:.0f}]")

# Training
model.train(data='coco128.yaml', epochs=100, imgsz=640)
```

### YOLO Variants

| Model | Speed | mAP | Use Case |
|-------|-------|-----|----------|
| YOLOv8n | Fast | 37.3 | Edge devices |
| YOLOv8s | Fast | 44.9 | Balanced |
| YOLOv8m | Medium | 50.2 | General |
| YOLOv8l | Slow | 52.9 | Accuracy |

---

## Faster R-CNN

### Implementation

```python
import torchvision
from torchvision.models.detection import fasterrcnn_resnet50_fpn

# Load pre-trained model
model = fasterrcnn_resnet50_fpn(pretrained=True)
model.eval()

# Inference
def detect_objects(image_path):
    from PIL import Image
    import torchvision.transforms as transforms
    
    image = Image.open(image_path)
    transform = transforms.Compose([transforms.ToTensor()])
    image_tensor = transform(image).unsqueeze(0)
    
    with torch.no_grad():
        predictions = model(image_tensor)
    
    boxes = predictions[0]['boxes']
    labels = predictions[0]['labels']
    scores = predictions[0]['scores']
    
    # Filter by confidence
    mask = scores > 0.5
    boxes = boxes[mask]
    labels = labels[mask]
    scores = scores[mask]
    
    return boxes, labels, scores
```

---

## SSD (Single Shot Detector)

### Architecture

```python
import torchvision
from torchvision.models.detection import ssd300_vgg16

# Load SSD
model = ssd300_vgg16(pretrained=True)
model.eval()

# Inference
def ssd_detect(image_tensor):
    with torch.no_grad():
        predictions = model(image_tensor)
    
    return predictions
```

---

## DETR (Detection Transformer)

### Implementation

```python
from transformers import DetrForObjectDetection, DetrImageProcessor
from PIL import Image

# Load DETR
processor = DetrImageProcessor.from_pretrained("facebook/detr-resnet-50")
model = DetrForObjectDetection.from_pretrained("facebook/detr-resnet-50")

# Inference
image = Image.open("image.jpg")
inputs = processor(images=image, return_tensors="pt")

with torch.no_grad():
    outputs = model(**inputs)

# Post-process
target_sizes = torch.tensor([image.size[::-1]])
results = processor.post_process(outputs, target_sizes=target_sizes)

for result in results:
    for score, label, box in zip(result["scores"], result["labels"], result["boxes"]):
        if score > 0.5:
            print(f"Label: {model.config.id2label[label.item()]}")
            print(f"Score: {score.item():.4f}")
            print(f"Box: {box.tolist()}")
```

---

## Evaluation Metrics

### mAP (mean Average Precision)

```python
def calculate_map(predictions, ground_truths, iou_threshold=0.5):
    """Calculate mean Average Precision"""
    # Implementation of mAP calculation
    # For each class:
    # 1. Sort predictions by confidence
    # 2. Calculate IoU with ground truth
    # 3. Determine true/false positives
    # 4. Calculate precision-recall curve
    # 5. Calculate AP
    pass
```

### IoU (Intersection over Union)

```python
def calculate_iou(box1, box2):
    """Calculate IoU between two boxes"""
    x1 = max(box1[0], box2[0])
    y1 = max(box1[1], box2[1])
    x2 = min(box1[2], box2[2])
    y2 = min(box1[3], box2[3])
    
    intersection = max(0, x2 - x1) * max(0, y2 - y1)
    
    area1 = (box1[2] - box1[0]) * (box1[3] - box1[1])
    area2 = (box2[2] - box2[0]) * (box2[3] - box2[1])
    
    union = area1 + area2 - intersection
    
    return intersection / union if union > 0 else 0
```

---

## Non-Maximum Suppression (NMS)

```python
def nms(boxes, scores, iou_threshold=0.5):
    """Non-Maximum Suppression"""
    indices = scores.argsort()[::-1]
    keep = []
    
    while len(indices) > 0:
        current = indices[0]
        keep.append(current)
        
        if len(indices) == 1:
            break
        
        remaining = indices[1:]
        ious = np.array([calculate_iou(boxes[current], boxes[i]) for i in remaining])
        
        indices = remaining[ious < iou_threshold]
    
    return keep
```

---

## Best Practices

1. **Choose based on speed**: YOLO for real-time, R-CNN for accuracy
2. **Data augmentation**: Mosaic, mixup for detection
3. **Anchor tuning**: Match dataset object sizes
4. **NMS threshold**: Tune for your use case
5. **Multi-scale training**: Improve robustness

## Further Reading

- "You Only Look Once" (YOLO paper)
- "Faster R-CNN" by Ren et al.
- "End-to-End Object Detection with Transformers" (DETR)
