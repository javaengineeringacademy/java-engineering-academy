from PIL import Image, ImageFilter, ImageDraw, ImageFont

# Open an image
img = Image.open('input.jpg')

# Resize
img_resized = img.resize((200, 200))
img_resized.save('output_resized.jpg')

# Apply filter
img_blurred = img.filter(ImageFilter.GaussianBlur(radius=5))
img_blurred.save('output_blurred.jpg')

# Draw on image
img_draw = img.copy()
draw = ImageDraw.Draw(img_draw)
draw.rectangle([50, 50, 150, 150], outline='red', width=3)
draw.text((60, 60), "Hello", fill='white')
img_draw.save('output_drawn.jpg')

# Convert to grayscale
img_gray = img.convert('L')
img_gray.save('output_gray.jpg')

print("Image processing complete!")
