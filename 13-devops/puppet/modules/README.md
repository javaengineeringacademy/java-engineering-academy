# Puppet Modules

## Overview

Puppet modules are self-contained bundles of code and data that encapsulate reusable Puppet code.

## Module Structure

```
nginx/
├── manifests/
│   ├── init.pp
│   ├── install.pp
│   ├── config.pp
│   └── service.pp
├── templates/
│   └── nginx.conf.erb
├── files/
│   └── index.html
├── spec/
│   └── classes/
│       └── nginx_spec.rb
├── metadata.json
└── README.md
```

## Module Manifests

```puppet
# manifests/init.pp
class nginx (
  Integer $port = 80,
  String $docroot = '/var/www/html',
) {
  include nginx::install
  include nginx::config
  include nginx::service
}

# manifests/install.pp
class nginx::install {
  package { 'nginx':
    ensure => installed,
  }
}

# manifests/config.pp
class nginx::config (
  Integer $port = $nginx::port,
  String $docroot = $nginx::docroot,
) {
  file { '/etc/nginx/nginx.conf':
    ensure  => file,
    content => epp('nginx/nginx.conf.epp', {
      'port'    => $port,
      'docroot' => $docroot,
    }),
    require => Class['nginx::install'],
    notify  => Class['nginx::service'],
  }
}

# manifests/service.pp
class nginx::service {
  service { 'nginx':
    ensure  => running,
    enable  => true,
    require => Class['nginx::install'],
  }
}
```

## Using Modules

```puppet
# site.pp
node 'webserver.example.com' {
  class { 'nginx':
    port    => 8080,
    docroot => '/opt/myapp',
  }
}
```

## Puppet Forge

```bash
# Install module
puppet module install nginx

# Search modules
puppet module search nginx

# List installed modules
puppet module list
```

## Best Practices

1. **Keep modules small** - Single responsibility principle
2. **Use parameters** - Parameterize modules
3. **Document modules** - Add README and metadata
4. **Test modules** - Use rspec-puppet
5. **Use Puppet Forge** - Share and reuse modules
6. **Version modules** - Use semantic versioning
7. **Implement dependencies** - Use metadata.json for dependencies
8. **Use templates** - Template configuration files
9. **Implement idempotency** - Ensure modules can run multiple times
10. **Use Puppet Lint** - Lint modules for best practices
