# Vision Transformers

## Overview

Vision Transformers (ViT) apply transformer architecture to image recognition by splitting images into patches and treating them as tokens.

## Vision Transformer (ViT)

### Implementation

```python
import torch
import torch.nn as nn

class PatchEmbedding(nn.Module):
    def __init__(self, img_size=224, patch_size=16, in_channels=3, embed_dim=768):
        super().__init__()
        self.img_size = img_size
        self.patch_size = patch_size
        self.num_patches = (img_size // patch_size) ** 2
        
        self.proj = nn.Conv2d(in_channels, embed_dim, 
                              kernel_size=patch_size, stride=patch_size)
    
    def forward(self, x):
        x = self.proj(x)  # [B, E, H/P, W/P]
        x = x.flatten(2).transpose(1, 2)  # [B, N, E]
        return x

class MultiHeadAttention(nn.Module):
    def __init__(self, embed_dim, num_heads, dropout=0.1):
        super().__init__()
        self.num_heads = num_heads
        self.head_dim = embed_dim // num_heads
        
        self.qkv = nn.Linear(embed_dim, embed_dim * 3)
        self.proj = nn.Linear(embed_dim, embed_dim)
        self.dropout = nn.Dropout(dropout)
        self.scale = self.head_dim ** -0.5
    
    def forward(self, x):
        B, N, C = x.shape
        qkv = self.qkv(x).reshape(B, N, 3, self.num_heads, self.head_dim).permute(2, 0, 3, 1, 4)
        q, k, v = qkv.unbind(0)
        
        attn = (q @ k.transpose(-2, -1)) * self.scale
        attn = attn.softmax(dim=-1)
        attn = self.dropout(attn)
        
        x = (attn @ v).transpose(1, 2).reshape(B, N, C)
        return self.proj(x)

class TransformerBlock(nn.Module):
    def __init__(self, embed_dim, num_heads, mlp_ratio=4.0, dropout=0.1):
        super().__init__()
        self.norm1 = nn.LayerNorm(embed_dim)
        self.attn = MultiHeadAttention(embed_dim, num_heads, dropout)
        self.norm2 = nn.LayerNorm(embed_dim)
        self.mlp = nn.Sequential(
            nn.Linear(embed_dim, int(embed_dim * mlp_ratio)),
            nn.GELU(),
            nn.Dropout(dropout),
            nn.Linear(int(embed_dim * mlp_ratio), embed_dim),
            nn.Dropout(dropout)
        )
    
    def forward(self, x):
        x = x + self.attn(self.norm1(x))
        x = x + self.mlp(self.norm2(x))
        return x

class VisionTransformer(nn.Module):
    def __init__(self, img_size=224, patch_size=16, in_channels=3, 
                 num_classes=1000, embed_dim=768, depth=12, num_heads=12):
        super().__init__()
        self.patch_embed = PatchEmbedding(img_size, patch_size, in_channels, embed_dim)
        num_patches = self.patch_embed.num_patches
        
        self.cls_token = nn.Parameter(torch.zeros(1, 1, embed_dim))
        self.pos_embed = nn.Parameter(torch.zeros(1, num_patches + 1, embed_dim))
        self.pos_drop = nn.Dropout(0.1)
        
        self.blocks = nn.Sequential(*[
            TransformerBlock(embed_dim, num_heads) for _ in range(depth)
        ])
        
        self.norm = nn.LayerNorm(embed_dim)
        self.head = nn.Linear(embed_dim, num_classes)
    
    def forward(self, x):
        B = x.shape[0]
        x = self.patch_embed(x)
        
        cls_tokens = self.cls_token.expand(B, -1, -1)
        x = torch.cat([cls_tokens, x], dim=1)
        x = x + self.pos_embed
        x = self.pos_drop(x)
        
        x = self.blocks(x)
        x = self.norm(x)
        
        cls_token = x[:, 0]
        return self.head(cls_token)

# ViT-B/16
vit = VisionTransformer(
    img_size=224, patch_size=16, embed_dim=768,
    depth=12, num_heads=12, num_classes=1000
)

x = torch.randn(2, 3, 224, 224)
output = vit(x)
print(f"Output shape: {output.shape}")
```

---

## DeiT (Data-efficient Image Transformers)

### Implementation

```python
class DeiT(VisionTransformer):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.dist_token = nn.Parameter(torch.zeros(1, 1, self.patch_embed.proj.out_channels))
        self.pos_embed = nn.Parameter(
            torch.zeros(1, self.patch_embed.num_patches + 2, self.patch_embed.proj.out_channels)
        )
        self.head_dist = nn.Linear(self.patch_embed.proj.out_channels, self.num_classes)
    
    def forward(self, x):
        B = x.shape[0]
        x = self.patch_embed(x)
        
        cls_tokens = self.cls_token.expand(B, -1, -1)
        dist_tokens = self.dist_token.expand(B, -1, -1)
        x = torch.cat([cls_tokens, dist_tokens, x], dim=1)
        x = x + self.pos_embed
        
        x = self.blocks(x)
        x = self.norm(x)
        
        cls_token = x[:, 0]
        dist_token = x[:, 1]
        
        return (self.head(cls_token) + self.head_dist(dist_token)) / 2
```

---

## Swin Transformer

### Implementation

```python
class WindowAttention(nn.Module):
    def __init__(self, dim, window_size, num_heads):
        super().__init__()
        self.num_heads = num_heads
        head_dim = dim // num_heads
        self.scale = head_dim ** -0.5
        
        self.qkv = nn.Linear(dim, dim * 3)
        self.proj = nn.Linear(dim, dim)
        
        self.relative_position_bias_table = nn.Parameter(
            torch.zeros((2 * window_size - 1) * (2 * window_size - 1), num_heads)
        )
    
    def forward(self, x):
        B_, N, C = x.shape
        qkv = self.qkv(x).reshape(B_, N, 3, self.num_heads, C // self.num_heads).permute(2, 0, 3, 1, 4)
        q, k, v = qkv.unbind(0)
        
        q = q * self.scale
        attn = q @ k.transpose(-2, -1)
        
        return self.proj((attn.softmax(dim=-1) @ v).transpose(1, 2).reshape(B_, N, C))

class SwinTransformerBlock(nn.Module):
    def __init__(self, dim, num_heads, window_size=7, shift_size=0):
        super().__init__()
        self.norm1 = nn.LayerNorm(dim)
        self.attn = WindowAttention(dim, window_size, num_heads)
        self.norm2 = nn.LayerNorm(dim)
        self.mlp = nn.Sequential(
            nn.Linear(dim, dim * 4),
            nn.GELU(),
            nn.Linear(dim * 4, dim)
        )
        self.window_size = window_size
        self.shift_size = shift_size
    
    def forward(self, x):
        shortcut = x
        x = self.norm1(x)
        x = self.attn(x)
        x = shortcut + x
        
        shortcut = x
        x = self.norm2(x)
        x = self.mlp(x)
        return shortcut + x
```

---

## Using Hugging Face

```python
from transformers import ViTForImageClassification, ViTFeatureExtractor
from PIL import Image

# Load pre-trained ViT
model = ViTForImageClassification.from_pretrained('google/vit-base-patch16-224')
feature_extractor = ViTFeatureExtractor.from_pretrained('google/vit-base-patch16-224')

# Inference
image = Image.open('image.jpg')
inputs = feature_extractor(images=image, return_tensors='pt')

with torch.no_grad():
    outputs = model(**inputs)
    logits = outputs.logits
    predicted_class = logits.argmax(-1).item()

print(f"Predicted class: {model.config.id2label[predicted_class]}")
```

---

## Comparison

| Model | Params | FLOPs | Top-1 Acc |
|-------|--------|-------|-----------|
| ViT-B/16 | 86M | 17.6B | 84.2% |
| DeiT-B | 86M | 17.6B | 83.8% |
| Swin-B | 88M | 15.4B | 83.5% |
| ViT-L/16 | 307M | 61.6B | 87.8% |

## Best Practices

1. **Pre-training**: Use large datasets (ImageNet-21k)
2. **Patch size**: Smaller patches capture more detail
3. **Data augmentation**: Strong augmentation helps
4. **Learning rate**: Cosine schedule with warmup
5. **Resolution**: Higher resolution improves accuracy

## Further Reading

- "An Image is Worth 16x16 Words" (ViT)
- "Training data-efficient image transformers" (DeiT)
- "Swin Transformer" by Liu et al.
