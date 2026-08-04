# Chef Best Practices

## Overview

This guide covers best practices for writing, organizing, and maintaining Chef cookbooks and recipes.

## Code Organization

```
chef/
├── cookbooks/
│   ├── mycompany/
│   │   ├── recipes/
│   │   ├── templates/
│   │   ├── attributes/
│   │   └── metadata.rb
│   └── nginx/
├── data_bags/
├── environments/
├── roles/
└── Berksfile
```

## Recipe Best Practices

### Use Resources
```ruby
# Good
package 'nginx' do
  action :install
end

# Bad
execute 'install nginx' do
  command 'apt-get install -y nginx'
end
```

### Use Attributes
```ruby
# Good
package node['nginx']['package_name'] do
  action :install
end

# Bad
package 'nginx' do
  action :install
end
```

### Use Guards
```ruby
package 'nginx' do
  action :install
  not_if { ::File.exist?('/usr/sbin/nginx') }
end
```

## Testing

### ChefSpec
```ruby
# spec/unit/recipes/default_spec.rb
require 'chefspec'

describe 'my_cookbook::default' do
  let(:chef_run) { ChefSpec::ServerRunner.converge(described_recipe) }

  it 'installs nginx' do
    expect(chef_run).to install_package('nginx')
  end

  it 'starts nginx' do
    expect(chef_run).to start_service('nginx')
  end
end
```

### Test Kitchen
```yaml
# .kitchen.yml
---
driver:
  name: docker

provisioner:
  name: chef_solo

platforms:
  - name: ubuntu-20.04
  - name: debian-10

suites:
  - name: default
    run_list:
      - recipe[my_cookbook::default]
```

## Best Practices Summary

1. **Use cookbooks** - Organize code into cookbooks
2. **Use attributes** - Parameterize configurations
3. **Use templates** - Template configuration files
4. **Implement idempotency** - Ensure recipes can run multiple times
5. **Use resources** - Use built-in resources
6. **Test cookbooks** - Use Test Kitchen and ChefSpec
7. **Document cookbooks** - Add README and metadata
8. **Use Chef Supermarket** - Leverage community cookbooks
9. **Implement code review** - Review cookbooks before deployment
10. **Use version control** - Store cookbooks in Git
