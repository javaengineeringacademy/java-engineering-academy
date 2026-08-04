# Puppet Fundamentals

## Overview

Puppet is a configuration management tool that uses a declarative language to define system state.

## Manifests

### Basic Manifest
```puppet
# site.pp
node 'webserver.example.com' {
  package { 'nginx':
    ensure => installed,
  }

  file { '/etc/nginx/nginx.conf':
    ensure  => file,
    owner   => 'root',
    group   => 'root',
    mode    => '0644',
    content => template('nginx/nginx.conf.erb'),
    require => Package['nginx'],
  }

  service { 'nginx':
    ensure  => running,
    enable  => true,
    require => Package['nginx'],
  }
}
```

## Classes

```puppet
# manifests/nginx.pp
class nginx {
  package { 'nginx':
    ensure => installed,
  }

  file { '/etc/nginx/nginx.conf':
    ensure  => file,
    owner   => 'root',
    group   => 'root',
    mode    => '0644',
    content => template('nginx/nginx.conf.erb'),
    require => Package['nginx'],
  }

  service { 'nginx':
    ensure  => running,
    enable  => true,
    require => Package['nginx'],
  }
}
```

## Parameters

```puppet
class nginx (
  String $port = 80,
  Boolean $enable_ssl = false,
) {
  package { 'nginx':
    ensure => installed,
  }

  file { '/etc/nginx/nginx.conf':
    ensure  => file,
    content => epp('nginx/nginx.conf.epp', {
      'port' => $port,
      'enable_ssl' => $enable_ssl,
    }),
    require => Package['nginx'],
  }
}
```

## Best Practices

1. **Use modules** - Organize code into modules
2. **Use Hiera** - Separate data from code
3. **Use templates** - Template configuration files
4. **Implement idempotency** - Ensure manifests can run multiple times
5. **Use parameters** - Parameterize classes
6. **Test with rspec-puppet** - Unit test manifests
7. **Document modules** - Add README and metadata
8. **Use Puppet Forge** - Leverage community modules
9. **Implement code review** - Review manifests before deployment
10. **Use version control** - Store manifests in Git
