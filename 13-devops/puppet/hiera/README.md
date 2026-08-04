# Puppet Hiera

## Overview

Hiera is Puppet's hierarchical data lookup system that separates data from code, allowing you to override default values based on node facts.

## Hiera Configuration

```yaml
# hiera.yaml
---
version: 5
defaults:
  datadir: data
  data_hash: yaml_data

hierarchy:
  - name: "Per-node data"
    path: "nodes/%{trusted.certname}.yaml"
  
  - name: "Per-OS data"
    path: "os/%{facts.os.family}.yaml"
  
  - name: "Per-environment data"
    path: "environments/%{facts.env}.yaml"
  
  - name: "Common data"
    path: "common.yaml"
```

## Data Files

```yaml
# data/common.yaml
---
nginx::port: 80
nginx::docroot: '/var/www/html'

# data/os/RedHat.yaml
---
nginx::package_name: 'nginx'

# data/os/Debian.yaml
---
nginx::package_name: 'nginx'

# data/nodes/webserver.example.com.yaml
---
nginx::port: 8080
```

## Using Hiera in Manifests

```puppet
class nginx (
  Integer $port = 80,
  String $docroot = '/var/www/html',
) {
  package { 'nginx':
    ensure => installed,
  }

  file { '/etc/nginx/nginx.conf':
    ensure  => file,
    content => epp('nginx/nginx.conf.epp', {
      'port'    => $port,
      'docroot' => $docroot,
    }),
  }
}
```

## Hiera Commands

```bash
# Lookup value
hiera nginx::port

# Lookup with fact
hiera nginx::port certname=webserver.example.com

# Lookup array
hiera -a nginx::ports

# Dump all values
hiera -d
```

## Best Practices

1. **Separate data from code** - Use Hiera for configuration data
2. **Use layers** - Organize data by priority
3. **Use variables** - Reference facts in data files
4. **Document data** - Add comments for complex data
5. **Test lookups** - Use hiera-eyaml for encrypted data
6. **Use eyaml** - Encrypt sensitive data
7. **Implement version control** - Store data files in Git
8. **Use data bindings** - Automatic parameter lookup
9. **Document hierarchy** - Add comments for data hierarchy
10. **Test hiera** - Use rspec-puppet-hiera for testing
