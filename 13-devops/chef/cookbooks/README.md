# Chef Cookbooks

## Overview

Chef cookbooks are packages of Chef code that contain all the components needed to configure a system.

## Cookbook Structure

```
my_cookbook/
├── recipes/
├── templates/
├── files/
├── attributes/
├── libraries/
├── providers/
├── resources/
├── spec/
├── test/
├── metadata.rb
└── README.md
```

## Metadata

```ruby
# metadata.rb
name 'my_cookbook'
maintainer 'Your Name'
maintainer_email 'you@example.com'
license 'Apache-2.0'
description 'My awesome cookbook'
version '1.0.0'

depends 'nginx', '~> 5.0'
depends 'postgresql', '~> 7.0'

supports 'ubuntu', '20.04'
supports 'debian', '10'
```

## Berkshelf

```ruby
# Berksfile
source 'https://community.chef.io'

metadata

cookbook 'nginx', '~> 5.0'
cookbook 'postgresql', '~> 7.0'
```

```bash
# Install dependencies
berks install

# Upload to Chef Server
berks upload

# Vendoring
berks vendor vendor/cookbooks
```

## Best Practices

1. **Keep cookbooks small** - Single responsibility principle
2. **Use metadata** - Document dependencies and platforms
3. **Use Berkshelf** - Manage cookbook dependencies
4. **Test cookbooks** - Use Test Kitchen and ChefSpec
5. **Document cookbooks** - Add README and metadata
6. **Use Chef Supermarket** - Share and reuse cookbooks
7. **Version cookbooks** - Use semantic versioning
8. **Use attributes** - Parameterize configurations
9. **Implement idempotency** - Ensure cookbooks can run multiple times
10. **Use Chef Lint** - Lint cookbooks for best practices
