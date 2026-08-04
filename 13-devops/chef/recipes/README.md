# Chef Recipes

## Overview

Chef recipes are Ruby-based files that define the desired state of system resources.

## Resources

### Package Resource
```ruby
package 'nginx' do
  action :install
end
```

### Service Resource
```ruby
service 'nginx' do
  supports :status => true, :restart => true, :reload => true
  action [:enable, :start]
end
```

### Template Resource
```ruby
template '/etc/nginx/nginx.conf' do
  source 'nginx.conf.erb'
  owner 'root'
  group 'root'
  mode '0644'
  variables(
    :port => node['nginx']['port'],
    :workers => node['nginx']['workers']
  )
  notifies :restart, 'service[nginx]'
end
```

### File Resource
```ruby
file '/var/www/html/index.html' do
  content '<h1>Hello World</h1>'
  owner 'www-data'
  group 'www-data'
  mode '0644'
end
```

## Guards

```ruby
package 'nginx' do
  action :install
  not_if { ::File.exist?('/usr/sbin/nginx') }
end

execute 'update-apt' do
  command 'apt-get update'
  action :run
  only_if { ::File.exist?('/etc/apt/sources.list') }
end
```

## Best Practices

1. **Use descriptive names** - Clear resource names
2. **Implement guards** - Use not_if and only_if
3. **Use notifications** - Notify on changes
4. **Use attributes** - Parameterize recipes
5. **Implement idempotency** - Ensure recipes can run multiple times
6. **Test recipes** - Use Test Kitchen
7. **Document recipes** - Add comments and README
8. **Use version control** - Store recipes in Git
9. **Implement code review** - Review recipes before deployment
10. **Use Chef Lint** - Lint recipes for best practices
