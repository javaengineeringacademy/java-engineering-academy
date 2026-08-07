# Pillow

## Why Pillow Exists

Every Python developer who needs to process images — resize thumbnails, apply filters, add watermarks — quickly discovers that raw file I/O and libraries like `imghdr` don't provide actual image manipulation. Pillow (a fork of PIL) was created to fill this gap: it provides a simple, Pythonic API for opening, manipulating, and saving images in many formats. It's the standard for image processing in Python.

## What You'll Learn

By the end of this section, you'll be able to:

- Open, resize, crop, and save images in multiple formats
- Apply filters and transformations for image enhancement
- Draw text, shapes, and watermarks on images

## When to Use Pillow

| Use Case | Why Pillow | Alternative |
|----------|-----------|-------------|
| Thumbnail generation | Simple resize API | OpenCV |
| Image filtering | Built-in blur, sharpen, edge detect | OpenCV |
| Watermarking | Draw API for text overlay | ImageMagick |
| Format conversion | PNG → JPEG, GIF → WebP | CLI tools |
| Batch processing | Loop with PIL.Image.open | Shell scripts |
| Image analysis | Basic pixel manipulation | scikit-image |

## How Pillow Works Internally

Pillow represents images as `Image` objects that wrap pixel data in memory. When you open an image, Pillow reads the file format (PNG, JPEG, etc.) and decodes it into an internal pixel buffer. Operations like `resize()` create new Image objects with transformed pixel data.

The `ImageDraw` module provides a canvas-like API for drawing on images. It supports text rendering (using TrueType fonts), shapes (rectangles, ellipses, polygons), and compositing (paste one image onto another). Filters are implemented as convolution kernels applied to the pixel data.

```python
from PIL import Image, ImageDraw, ImageFilter

# Open and resize
img = Image.open('photo.jpg')
img.thumbnail((200, 200))
img.save('thumbnail.jpg')

# Apply filter
blurred = img.filter(ImageFilter.GaussianBlur(radius=5))

# Draw text
draw = ImageDraw.Draw(img)
draw.text((10, 10), "Watermark", fill='white')

# Convert format
img.save('output.webp', 'WEBP')
```

## Production Checklist

### ✅ Before using Pillow in production:

☐ I know the time/space complexity
☐ I know common mistakes
☐ I know alternatives
☐ I know limitations
☐ I know how to debug it
☐ I've tested with realistic data volume
☐ I've profiled for performance

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic syntax
- Can write working code

### Level 2: Understands
- Knows time/space complexity
- Understands edge cases

### Level 3: Deep Knowledge
- Knows internal implementation
- Can explain trade-offs

### Level 4: Expert
- Can optimize for specific use cases
- Can debug in production

### Level 5: Master
- Can design custom implementations
- Can teach others

## Common Myths

### ❌ Myth 1: Pillow is only for basic image operations
**Reality:** Pillow supports advanced operations like compositing, masking, and pixel-level manipulation. For ML-grade computer vision, use OpenCV, but for most web and document processing, Pillow is sufficient.

### ❌ Myth 2: PIL and Pillow are different libraries
**Reality:** Pillow is the maintained fork of PIL (Python Imaging Library). PIL is abandoned; Pillow is the modern replacement. Always use `from PIL import Image`.

### ❌ Myth 3: Image processing is always CPU-intensive
**Reality:** Many operations (resize, format conversion) are fast for typical web images (< 5MB). Profile before optimizing.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Image processing and manipulation |
| Complexity | O(n) for pixel operations |
| Thread Safe | Yes (per Image object) |
| Best Alternative | OpenCV for computer vision |
| When to Use | Thumbnails, filters, format conversion |
| When to Avoid | Video processing, ML-grade vision |

## Related Topics

- [01-numpy](../01-numpy/) - Pixel data as arrays
- [13-beautifulsoup](../13-beautifulsoup/) - Web scraping images
- [14-matplotlib](../14-matplotlib/) - Image visualization
