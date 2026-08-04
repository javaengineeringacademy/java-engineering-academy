# Generative Adversarial Networks (GANs)

## Overview

GANs consist of two networks competing against each other: a generator that creates fake data and a discriminator that distinguishes real from fake.

## Basic GAN

### Implementation

```python
import torch
import torch.nn as nn
import torch.optim as optim
from torchvision import datasets, transforms
from torch.utils.data import DataLoader

class Generator(nn.Module):
    def __init__(self, latent_dim=100, img_shape=(1, 28, 28)):
        super().__init__()
        self.img_shape = img_shape
        
        self.model = nn.Sequential(
            nn.Linear(latent_dim, 128),
            nn.LeakyReLU(0.2, inplace=True),
            nn.Linear(128, 256),
            nn.BatchNorm1d(256),
            nn.LeakyReLU(0.2, inplace=True),
            nn.Linear(256, 512),
            nn.BatchNorm1d(512),
            nn.LeakyReLU(0.2, inplace=True),
            nn.Linear(512, 1024),
            nn.BatchNorm1d(1024),
            nn.LeakyReLU(0.2, inplace=True),
            nn.Linear(1024, int(torch.prod(torch.tensor(img_shape)))),
            nn.Tanh()
        )
    
    def forward(self, z):
        img = self.model(z)
        return img.view(img.size(0), *self.img_shape)

class Discriminator(nn.Module):
    def __init__(self, img_shape=(1, 28, 28)):
        super().__init__()
        self.model = nn.Sequential(
            nn.Linear(int(torch.prod(torch.tensor(img_shape))), 512),
            nn.LeakyReLU(0.2, inplace=True),
            nn.Linear(512, 256),
            nn.LeakyReLU(0.2, inplace=True),
            nn.Linear(256, 1),
            nn.Sigmoid()
        )
    
    def forward(self, img):
        flat = img.view(img.size(0), -1)
        return self.model(flat)

# Training
def train_gan(epochs=200, batch_size=64, lr=0.0002, latent_dim=100):
    device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
    
    # Data
    transform = transforms.Compose([
        transforms.ToTensor(),
        transforms.Normalize([0.5], [0.5])
    ])
    dataloader = DataLoader(
        datasets.MNIST('./data', train=True, download=True, transform=transform),
        batch_size=batch_size, shuffle=True
    )
    
    # Models
    generator = Generator(latent_dim).to(device)
    discriminator = Discriminator().to(device)
    
    # Loss
    criterion = nn.BCELoss()
    optimizer_G = optim.Adam(generator.parameters(), lr=lr, betas=(0.5, 0.999))
    optimizer_D = optim.Adam(discriminator.parameters(), lr=lr, betas=(0.5, 0.999))
    
    for epoch in range(epochs):
        for i, (real_imgs, _) in enumerate(dataloader):
            batch_size = real_imgs.size(0)
            real_imgs = real_imgs.to(device)
            
            # Labels
            real_labels = torch.ones(batch_size, 1).to(device)
            fake_labels = torch.zeros(batch_size, 1).to(device)
            
            # Train Discriminator
            z = torch.randn(batch_size, latent_dim).to(device)
            fake_imgs = generator(z)
            
            real_loss = criterion(discriminator(real_imgs), real_labels)
            fake_loss = criterion(discriminator(fake_imgs.detach()), fake_labels)
            d_loss = (real_loss + fake_loss) / 2
            
            optimizer_D.zero_grad()
            d_loss.backward()
            optimizer_D.step()
            
            # Train Generator
            z = torch.randn(batch_size, latent_dim).to(device)
            fake_imgs = generator(z)
            g_loss = criterion(discriminator(fake_imgs), real_labels)
            
            optimizer_G.zero_grad()
            g_loss.backward()
            optimizer_G.step()
        
        if (epoch + 1) % 10 == 0:
            print(f"Epoch [{epoch+1}/{epochs}] D_loss: {d_loss.item():.4f} G_loss: {g_loss.item():.4f}")
```

---

## DCGAN (Deep Convolutional GAN)

### Implementation

```python
class DCGANGenerator(nn.Module):
    def __init__(self, latent_dim=100, channels=3):
        super().__init__()
        self.main = nn.Sequential(
            nn.ConvTranspose2d(latent_dim, 512, 4, 1, 0),
            nn.BatchNorm2d(512),
            nn.ReLU(True),
            nn.ConvTranspose2d(512, 256, 4, 2, 1),
            nn.BatchNorm2d(256),
            nn.ReLU(True),
            nn.ConvTranspose2d(256, 128, 4, 2, 1),
            nn.BatchNorm2d(128),
            nn.ReLU(True),
            nn.ConvTranspose2d(128, channels, 4, 2, 1),
            nn.Tanh()
        )
    
    def forward(self, z):
        return self.main(z.view(-1, z.size(1), 1, 1))

class DCGANDiscriminator(nn.Module):
    def __init__(self, channels=3):
        super().__init__()
        self.main = nn.Sequential(
            nn.Conv2d(channels, 128, 4, 2, 1),
            nn.LeakyReLU(0.2, inplace=True),
            nn.Conv2d(128, 256, 4, 2, 1),
            nn.BatchNorm2d(256),
            nn.LeakyReLU(0.2, inplace=True),
            nn.Conv2d(256, 512, 4, 2, 1),
            nn.BatchNorm2d(512),
            nn.LeakyReLU(0.2, inplace=True),
            nn.Conv2d(512, 1, 4, 1, 0),
            nn.Sigmoid()
        )
    
    def forward(self, x):
        return self.main(x).view(-1, 1)
```

---

## StyleGAN

### Architecture Features
- Mapping network
- Adaptive instance normalization
- Progressive growing

### Implementation

```python
class MappingNetwork(nn.Module):
    def __init__(self, latent_dim=512, hidden_dim=512):
        super().__init__()
        self.layers = nn.Sequential(
            nn.Linear(latent_dim, hidden_dim),
            nn.ReLU(),
            nn.Linear(hidden_dim, hidden_dim),
            nn.ReLU(),
            nn.Linear(hidden_dim, hidden_dim),
            nn.ReLU(),
            nn.Linear(hidden_dim, hidden_dim)
        )
    
    def forward(self, z):
        return self.layers(z)

class AdaIN(nn.Module):
    def __init__(self, channels, style_dim):
        super().__init__()
        self.norm = nn.InstanceNorm2d(channels)
        self.style_scale = nn.Linear(style_dim, channels)
        self.style_bias = nn.Linear(style_dim, channels)
    
    def forward(self, x, style):
        scale = self.style_scale(style).unsqueeze(2).unsqueeze(3)
        bias = self.style_bias(style).unsqueeze(2).unsqueeze(3)
        return self.norm(x) * (1 + scale) + bias
```

---

## CycleGAN

### Implementation

```python
class ResidualBlock(nn.Module):
    def __init__(self, channels):
        super().__init__()
        self.block = nn.Sequential(
            nn.ReflectionPad2d(1),
            nn.Conv2d(channels, channels, 3),
            nn.InstanceNorm2d(channels),
            nn.ReLU(inplace=True),
            nn.ReflectionPad2d(1),
            nn.Conv2d(channels, channels, 3),
            nn.InstanceNorm2d(channels)
        )
    
    def forward(self, x):
        return x + self.block(x)

class Generator(nn.Module):
    def __init__(self, num_residual_blocks=9):
        super().__init__()
        model = [
            nn.ReflectionPad2d(3),
            nn.Conv2d(3, 64, 7),
            nn.InstanceNorm2d(64),
            nn.ReLU(inplace=True)
        ]
        
        # Downsampling
        for _ in range(2):
            model += [
                nn.Conv2d(64, 128, 3, stride=2, padding=1),
                nn.InstanceNorm2d(128),
                nn.ReLU(inplace=True)
            ]
        
        # Residual blocks
        for _ in range(num_residual_blocks):
            model += [ResidualBlock(128)]
        
        # Upsampling
        for _ in range(2):
            model += [
                nn.ConvTranspose2d(128, 64, 3, stride=2, padding=1, output_padding=1),
                nn.InstanceNorm2d(64),
                nn.ReLU(inplace=True)
            ]
        
        model += [
            nn.ReflectionPad2d(3),
            nn.Conv2d(64, 3, 7),
            nn.Tanh()
        ]
        
        self.model = nn.Sequential(*model)
    
    def forward(self, x):
        return self.model(x)
```

---

## Evaluation Metrics

### FID (Fréchet Inception Distance)

```python
from pytorch_fid import fid_score

# Calculate FID
fid_value = fid_score.calculate_fid_given_paths(
    [real_images_path, fake_images_path],
    batch_size=50,
    device='cuda',
    dims=2048
)
print(f"FID: {fid_value:.4f}")
```

### IS (Inception Score)

```python
def calculate_inception_score(images, model, batch_size=32):
    preds = []
    for i in range(0, len(images), batch_size):
        batch = images[i:i+batch_size]
        with torch.no_grad():
            pred = model(batch)
        preds.append(torch.softmax(pred, dim=1).cpu().numpy())
    
    preds = np.concatenate(preds, axis=0)
    py = np.mean(preds, axis=0)
    kl = preds * (np.log(preds + 1e-10) - np.log(py + 1e-10))
    kl = np.mean(np.sum(kl, axis=1))
    return np.exp(kl)
```

---

## Best Practices

1. **Training stability**: Use spectral normalization
2. **Mode collapse**: Use minibatch discrimination
3. **Progressive growing**: Start with low resolution
4. **Evaluation**: Use FID and IS metrics
5. **Data quality**: Clean, diverse training data

## Further Reading

- "Generative Adversarial Networks" by Goodfellow et al.
- "Unsupervised Representation Learning with DCGAN"
- "Progressive Growing of GANs"
