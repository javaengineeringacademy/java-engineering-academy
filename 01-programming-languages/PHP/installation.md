# PHP Installation

## Homebrew (macOS)

```bash
brew install php
php -v

# Install specific version
brew install php@8.2

# Start PHP-FPM
brew services start php

# Switch versions
brew link php@8.2
```

## APT (Debian/Ubuntu)

```bash
sudo apt update
sudo apt install php php-cli php-fpm php-mysql php-mbstring php-xml php-curl

# Install specific version
sudo add-apt-repository ppa:ondrej/php
sudo apt update
sudo apt install php8.2 php8.2-fpm

# Start PHP-FPM
sudo systemctl start php8.2-fpm
sudo systemctl enable php8.2-fpm
```

## Docker

```bash
# Official PHP image
docker run -it --rm php:8.2-cli php -v

# With Apache
docker run -d -p 8080:80 php:8.2-apache

# With FPM (for Nginx)
docker run -d php:8.2-fpm

# Custom Dockerfile
FROM php:8.2-fpm
RUN docker-php-ext-install pdo_mysql mbstring
COPY php.ini /usr/local/etc/php/conf.d/custom.ini
```

## PHP-FPM Setup

```bash
# Configuration file locations
/etc/php/8.2/fpm/php.ini
/etc/php/8.2/fpm/pool.d/www.conf

# Pool configuration
[www]
user = www-data
group = www-data
listen = /run/php/php8.2-fpm.sock
listen.owner = www-data
listen.group = www-data
pm = dynamic
pm.max_children = 50
pm.start_servers = 5
pm.min_spare_servers = 5
pm.max_spare_servers = 35
pm.max_requests = 500
```

## Source Compilation

```bash
# Download and compile
wget https://www.php.net/distributions/php-8.2.0.tar.gz
tar -xzf php-8.2.0.tar.gz
cd php-8.2.0

./configure --prefix=/usr/local/php \
    --with-pdo-mysql \
    --with-mbstring \
    --with-curl \
    --with-openssl \
    --enable-fpm

make
sudo make install
```

## Verification

```bash
php -v                    # Check version
php -m                    # List modules
php -i | grep "php.ini"   # Find config file
php -S localhost:8000      # Built-in server
```

## Windows Installation

1. Download from php.net
2. Extract to `C:\php`
3. Add `C:\php` to PATH
4. Copy `php.ini-development` to `php.ini`
5. Enable extensions in php.ini

Verify: `php -v` in Command Prompt.
