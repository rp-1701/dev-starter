# SSH Setup Guide for Multiple GitHub Accounts

This guide explains how to set up and manage SSH keys for multiple GitHub accounts (e.g., personal and work accounts) on the same machine.

## Prerequisites
- Git installed on your machine
- GitHub account(s)
- Terminal/Command Line access

## Step 1: Generate SSH Keys

Generate a separate SSH key for your personal GitHub account:
```bash
# Generate SSH key (replace with your GitHub email)
ssh-keygen -t ed25519 -C "your.github@email.com" -f ~/.ssh/github_personal
```

When prompted:
- Enter a secure passphrase (recommended)
- The key pair will be saved as:
  - Private key: `~/.ssh/github_personal`
  - Public key: `~/.ssh/github_personal.pub`

## Step 2: Configure SSH

1. Create or edit your SSH config file:
```bash
nano ~/.ssh/config
```

2. Add the following configuration:
```
# Default GitHub Account
Host github.com
    AddKeysToAgent yes
    UseKeychain yes
    IdentityFile ~/.ssh/id_ed25519

# Personal GitHub Account
Host github.com-personal
    HostName github.com
    User git
    IdentityFile ~/.ssh/github_personal
```

## Step 3: Add SSH Key to GitHub

1. Copy your public key:
```bash
cat ~/.ssh/github_personal.pub
```

2. Add the key to GitHub:
   - Go to GitHub → Settings → SSH and GPG keys
   - Click "New SSH key"
   - Give it a meaningful title (e.g., "Personal MacBook")
   - Paste your public key
   - Click "Add SSH key"

## Step 4: Add Key to SSH Agent

1. Start the SSH agent:
```bash
eval "$(ssh-agent -s)"
```

2. Add your key:
```bash
ssh-add ~/.ssh/github_personal
```

## Step 5: Test Connection

Test your SSH connection:
```bash
# Test personal account connection
ssh -T git@github.com-personal
```

You should see a message like:
```
Hi username! You've successfully authenticated...
```

## Step 6: Repository Setup

When cloning or setting up a new repository:

1. For new repositories:
```bash
# Clone with custom SSH host
git clone git@github.com-personal:username/repo.git

# Or set up a new repository
git remote add origin git@github.com-personal:username/repo.git
```

2. For existing repositories, update the remote URL:
```bash
git remote set-url origin git@github.com-personal:username/repo.git
```

## Troubleshooting

1. **Permission Denied Error**
   ```bash
   # Check if SSH agent has your key
   ssh-add -l
   
   # Add key if not present
   ssh-add ~/.ssh/github_personal
   ```

2. **Multiple Keys**
   - Ensure each key has a unique name
   - Verify correct host in SSH config
   - Check remote URL matches SSH config host

3. **Key Not Working**
   ```bash
   # Test SSH connection with verbose output
   ssh -vT git@github.com-personal
   ```

## Best Practices

1. Use different keys for different accounts
2. Always use meaningful key names
3. Backup your SSH keys securely
4. Use strong passphrases
5. Keep your SSH config organized
6. Regularly verify your SSH connections

## Additional Resources

- [GitHub's SSH Guide](https://docs.github.com/en/authentication/connecting-to-github-with-ssh)
- [SSH Config Documentation](https://www.ssh.com/academy/ssh/config) 