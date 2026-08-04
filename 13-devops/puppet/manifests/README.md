# Puppet Manifests

## Overview

Puppet manifests are written in Puppet's declarative language to define the desired state of system resources.

## Resources

### Package Resource
```puppet
package { 'nginx':
  ensure => installed,
}

package { 'git':
  ensure => present,
}
```

### File Resource
```puppet
file { '/etc/nginx/nginx.conf':
  ensure  => file,
  owner   => 'root',
  group   => 'root',
  mode    => '0644',
  content => template('nginx/nginx.conf.erb'),
}
```

### Service Resource
```puppet
service { 'nginx':
  ensure  => running,
  enable  => true,
}
```

## Classes

```puppet
class nginx {
  package { 'nginx':
    ensure => installed,
  }

  file { '/etc/nginx/nginx.conf':
    ensure  => file,
    require => Package['nginx'],
  }

  service { 'nginx':
    ensure  => running,
    require => Package['nginx'],
  }
}
```

## Defined Types

```puppet
define nginx::vhost (
  String $domain,
  String $root,
  Integer $port = 80,
) {
  file { "/etc/nginx/sites-available/${name}":
    ensure  => file,
    content => epp('nginx/vhost.epp', {
      'domain' => $domain,
      'root'   => $root,
      'port'   => $port,
    }),
  }

  file { "/etc/nginx/sites-enabled/${name}":
    ensure  => link,
    target  => "/etc/nginx/sites-available/${name}",
    require => File["/etc/nginx/sites-available/${name}"],
  }
}
```

## Best Practices

1. **Use descriptive names** - Clear resource names
2. **Implement ordering** - Use require and before
3. **Use variables** - Parameterize manifests
4. **Use templates** - Template configuration files
5. **Implement idempotency** - Ensure manifests can run multiple times
6. **Test with rspec-puppet** - Unit test manifests
7. **Document manifests** - Add comments and README
8. **Use version control** - Store manifests in Git
9. **Implement code review** - Review manifests before deployment
10. **Use Puppet Lint** - Lint manifests for best practices
