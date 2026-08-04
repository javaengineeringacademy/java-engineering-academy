# Ansible Vault

## Overview

Ansible Vault is a feature for encrypting sensitive data like passwords, keys, and certificates within Ansible playbooks and roles.

## Creating Encrypted Files

```bash
# Create encrypted file
ansible-vault create secrets.yml

# Encrypt existing file
ansible-vault encrypt vars/secrets.yml

# Edit encrypted file
ansible-vault edit secrets.yml

# Decrypt file
ansible-vault decrypt secrets.yml

# View encrypted file
ansible-vault view secrets.yml
```

## Using Vault in Playbooks

### Prompt for Password
```bash
ansible-playbook playbook.yml --ask-vault-pass
```

### Password File
```bash
ansible-playbook playbook.yml --vault-password-file=~/.vault_pass
```

### Encrypted Variables
```yaml
# group_vars/all/vault.yml (encrypted)
vault_db_password: !vault |
  $ANSIBLE_VAULT;1.1;AES256
  383066313839626435666631666339356235316633386538666131666635616238
  6434643933363561303038383034393230336434313363613736323736383239
  3161373435333761343838343131353861383039373766646266363731666162
  3738306237666530313862306438616666343330643730626464653135306563
  6437313330363538363932666430313035383333636231376135333330333833
  3062393837613338666332356431663234306161643230643036323533623034
  3661383434373966383839613162343739356262356338326131343865333631
  6136623332333862383132383363666462343062386266623638396535363634
  656635303435363438396433323734333935

# Reference in playbook
- name: Configure database
  hosts: dbservers
  vars_files:
    - vars/vault.yml
  
  tasks:
    - name: Set database password
      mysql_user:
        name: admin
        password: "{{ vault_db_password }}"
```

## Encrypting Strings

```bash
# Encrypt a string
ansible-vault encrypt_string 'secretpassword' --name 'db_password'

# Output
db_password: !vault |
  $ANSIBLE_VAULT;1.1;AES256
  ...
```

## Best Practices

1. **Encrypt sensitive data** - Always encrypt passwords and keys
2. **Use vault-password-file** - Avoid typing passwords
3. **Separate vault files** - Keep vault files separate from main configs
4. **Use vault IDs** - Encrypt with multiple vault passwords
5. **Implement access controls** - Restrict vault access
6. **Document vault usage** - Add comments for encrypted variables
7. **Test vault operations** - Verify encryption and decryption
8. **Use vault rotation** - Rotate encrypted secrets regularly
9. **Implement backup** - Backup vault passwords securely
10. **Monitor vault usage** - Track vault operations
