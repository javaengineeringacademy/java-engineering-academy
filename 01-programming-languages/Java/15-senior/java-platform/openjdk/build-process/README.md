# Building OpenJDK from Source

## Prerequisites

### System Requirements
- **Operating System**: Linux, macOS, or Windows (WSL recommended)
- **Disk Space**: ~10GB for full build
- **RAM**: 8GB minimum, 16GB+ recommended

### Required Tools

```bash
# macOS (Homebrew)
brew install autoconf bash coreutils curl gcc gnu-getopt grep make wget

# Ubuntu/Debian
sudo apt-get install build-essential autoconf bash curl zip unzip \
    libx11-dev libxext-dev libxrender-dev libxtst-dev libxt-dev \
    libcups2-dev libfontconfig1-dev libasound2-dev

# CentOS/RHEL
sudo yum groupinstall "Development Tools"
sudo yum install autoconf bash curl zip unzip \
    libX11-devel libXext-devel libXrender-devel libXtst-devel libXt-devel \
    cups-devel fontconfig-devel alsa-lib-devel
```

### Boot JDK
A working JDK (N-1 version) is needed to compile the new JDK:
```bash
# Example: Building JDK 21 requires JDK 20 or 19 as boot JDK
java -version
# openjdk version "20.0.1" 2023-04-18
```

## Clone Repository

```bash
# Clone the JDK repository
git clone https://github.com/openjdk/jdk.git
cd jdk

# Check available branches
git branch -a

# Switch to desired version (e.g., JDK 21)
git checkout jdk-21+35
```

## Configure

The configure script detects your system and prepares the build:

```bash
# Basic configuration
bash configure

# With specific options
bash configure \
    --with-boot-jdk=/path/to/boot/jdk \
    --prefix=/path/to/install \
    --enable-debug \
    --with-jvm-variants=server \
    --with-freetype-include=/usr/include/freetype2 \
    --with-freetype-lib=/usr/lib/x86_64-linux-gnu
```

### Common Configure Options

| Option | Description |
|--------|-------------|
| `--with-boot-jdk` | Path to boot JDK |
| `--prefix` | Installation directory |
| `--enable-debug` | Build with debug symbols |
| `--with-jvm-variants` | JVM variants (server, client, minimal) |
| `--with-debug-level` | Debug level (release, fastdebug, slowdebug) |
| `--disable-warnings-as-errors` | Don't treat warnings as errors |

## Build

```bash
# Full build (optimized)
make images

# Build with specific number of cores
make images JOBS=8

# Build specific component
make jdk

# Build with debug info
make images debug-level=fastdebug
```

### Build Targets

| Target | Description |
|--------|-------------|
| `images` | Complete JDK image |
| `jdk` | JDK only (no JRE) |
| `docs` | Documentation |
| `test` | Run tests |
| `clean` | Clean build artifacts |
| `demos` | Demo applications |

## Run and Test

```bash
# Run the built JDK
./build/*/images/jdk/bin/java -version

# Run a simple test
./build/*/images/jdk/bin/javac Hello.java
./build/*/images/jdk/bin/java Hello

# Run the test suite
make test

# Run specific test
make test TEST="jdk/java/lang/String"
```

## Build Options

### JVM Variants

```bash
# Server JVM (default, optimized for long-running apps)
bash configure --with-jvm-variants=server

# Minimal JVM (small footprint)
bash configure --with-jvm-variants=minimal

# Both server and client
bash configure --with-jvm-variants=server,client
```

### Garbage Collectors

```bash
# Include all GCs
bash configure --with-jvm-features=shenandoahc1,zgc

# Only ZGC
bash configure --with-jvm-features=zgc
```

### Architectures

```bash
# x86_64 (default on most systems)
bash configure

# AArch64 (ARM 64-bit)
bash configure --openjdk-target=aarch64-linux-gnu

# Cross-compilation
bash configure --openjdk-target=arm-linux-gnueabihf
```

## Troubleshooting

### Common Issues

1. **Missing dependencies**: Run configure again and install suggested packages
2. **Boot JDK version mismatch**: Use N-1 version
3. **Permission errors**: Ensure write access to build directory
4. **Memory errors**: Reduce JOBS count or increase system RAM

### Clean Build

```bash
# Remove all build artifacts
make clean

# Full clean and rebuild
make distclean
bash configure
make images
```

## Output

The built JDK will be in:
```
build/<platform>/images/jdk/
├── bin/
│   ├── java
│   ├── javac
│   ├── jar
│   └── ...
├── lib/
│   ├── modules
│   └── ...
└── conf/
```
