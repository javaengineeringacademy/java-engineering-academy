# Puppet Best Practices

## Overview

This guide covers best practices for writing, organizing, and maintaining Puppet manifests and modules.

## Code Organization

```
puppet/
├── site-modules/
│   └── mycompany/
│       ├── manifests/
│       ├── templates/
│       └── files/
├── modules/
│   ├── nginx/
│   └── postgresql/
├── data/
│   ├── common.yaml
│   └── nodes/
├── hiera.yaml
├── Puppetfile
└── site.pp
```

## Manifest Best Practices

### Use Classes
```puppet
# Good
class nginx {
  package { 'nginx':
    ensure => installed,
  }
}

# Bad
package { 'nginx':
  ensure => installed,
}
```

### Use Parameters
```puppet
class nginx (
  Integer $port = 80,
  String $docroot = '/var/www/html',
) {
  # Implementation
}
```

### Use Ordering
```puppet
class nginx {
  package { 'nginx':
    ensure => installed,
  }

  file { '/etc/nginx/nginx.conf':
    require => Package['nginx'],
    notify  => Service['nginx'],
  }

  service { 'nginx':
    require => Package['nginx'],
  }
}
```

## Testing

### rspec-puppet
```ruby
# spec/classes/nginx_spec.rb
require 'spec_helper'

describe 'nginx', :class => true do
  it { should compile.with_all_deps }
  
  it { should contain_package('nginx').with_ensure('installed') }
  it { should contain_service('nginx').with_ensure('running') }
end
```

## Best Practices Summary

1. **Use modules** - Organize code into modules
2. **Use Hiera** - Separate data from code
3. **Use parameters** - Parameterize classes
4. **Implement idempotency** - Ensure manifests can run multiple times
5. **Use ordering** - Use require and notify
6. **Test manifests** - Use rspec-puppet
7. **Document modules** - Add README and metadata
8. **Use Puppet Forge** - Leverage community modules
9. **Implement code review** - Review manifests before deployment
10. **Use version control** - Store manifests in Git
