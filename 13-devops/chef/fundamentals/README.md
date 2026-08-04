# Chef Fundamentals

## Overview

Chef is a configuration management tool that uses Ruby-based DSL to define infrastructure as code.

## Cookbook Structure

```
my_cookbook/
├── recipes/
│   ├── default.rb
│   ├── install.rb
│   └── configure.rb
├── templates/
│   └── nginx.conf.erb
├── files/
│   └── index.html
├── attributes/
│   └── default.rb
├── metadata.rb
└── README.md
```

## Recipes

### Basic Recipe
```ruby
# recipes/default.rb
package 'nginx' do
  action :install
end

service 'nginx' do
  supports :status => true, :restart => true, :reload => true
  action [:enable, :start]
end

template '/etc/nginx/nginx.conf' do
  source 'nginx.conf.erb'
  owner 'root'
  group 'root'
  mode '0644'
  notifies :restart, 'service[nginx]', :delayed
end
```

### With Attributes
```ruby
# recipes/default.rb
node['nginx']['sites'].each do |site_name, site_config|
  template "/etc/nginx/sites-available/#{site_name}" do
    source 'site.conf.erb'
    variables(
      :server_name => site_config['server_name'],
      :document_root => site_config['document_root']
    )
    notifies :reload, 'service[nginx]'
  end
end
```

## Attributes

```ruby
# attributes/default.rb
default['nginx']['version'] = '1.18.0'
default['nginx']['port'] = 80
default['nginx']['sites'] = {
  'default' => {
    'server_name' => 'localhost',
    'document_root' => '/var/www/html'
  }
}
```

## Best Practices

1. **Use cookbooks** - Organize code into cookbooks
2. **Use attributes** - Parameterize configurations
3. **Use templates** - Template configuration files
4. **Implement idempotency** - Ensure recipes can run multiple times
5. **Use resources** - Use built-in resources
6. **Test with Test Kitchen** - Test cookbooks
7. **Document cookbooks** - Add README and metadata
8. **Use Chef Supermarket** - Leverage community cookbooks
9. **Implement code review** - Review cookbooks before deployment
10. **Use version control** - Store cookbooks in Git
