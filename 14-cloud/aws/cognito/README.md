# Amazon Cognito

## Overview

Amazon Cognito provides authentication, authorization, and user management for web and mobile apps.

## Components

```
┌─────────────────────────────────────────────────────────┐
│                    Amazon Cognito                         │
│  ┌──────────────────┐  ┌──────────────────┐            │
│  │   User Pools     │  │  Identity Pools  │            │
│  │   (Directory)    │  │   (Federation)   │            │
│  └────────┬─────────┘  └────────┬─────────┘            │
│           │                      │                       │
│           └──────────────────────┘                       │
│                      │                                   │
│              ┌───────┴───────┐                           │
│              │   Tokens      │                           │
│              └───────────────┘                           │
└─────────────────────────────────────────────────────────┘
```

## User Pools

### Create User Pool
```bash
aws cognito-idp create-user-pool \
  --pool-name MyUserPool \
  --policies '{
    "PasswordPolicy": {
      "MinimumLength": 8,
      "RequireUppercase": true,
      "RequireLowercase": true,
      "RequireNumbers": true,
      "RequireSymbols": true
    }
  }' \
  --auto-verified-attributes email \
  --username-attributes email
```

### User Pool Features
- **Sign-up and sign-in**
- **MFA (SMS/Token)**
- **Email verification**
- **Phone verification**
- **Custom attributes**
- **Social sign-in**

### Create App Client
```bash
aws cognito-idp create-user-pool-client \
  --user-pool-id us-east-1_xxxxxxx \
  --client-name MyWebApp \
  --explicit-auth-flows ALLOW_USER_PASSWORD_AUTH ALLOW_REFRESH_TOKEN_AUTH \
  --generate-secret false
```

## Identity Pools (Federated Identities)

### Create Identity Pool
```bash
aws cognito-identity create-identity-pool \
  --identity-pool-name MyIdentityPool \
  --allow-unauthenticated-identities false \
  --cognito-identity-providers '[
    {
      "ProviderName": "cognito-idp.us-east-1.amazonaws.com/us-east-1_xxxxxxx",
      "ClientId": "your-client-id"
    }
  ]'
```

### Identity Pool Features
- **AWS credentials** for users
- **Unauthenticated access** (optional)
- **Multiple provider federation**
- **Custom IAM roles** per user

## Authentication Flows

### User Pool Authentication
```javascript
// React example with Amplify
import { Auth } from 'aws-amplify';

// Sign up
await Auth.signUp({
  username: 'user@example.com',
  password: 'MyP@ssw0rd',
  attributes: {
    email: 'user@example.com',
    name: 'John Doe'
  }
});

// Sign in
const user = await Auth.signIn('user@example.com', 'MyP@ssw0rd');
```

### Token Exchange
```javascript
// Get current session
const session = await Auth.currentSession();
const accessToken = session.getAccessToken().getJwtToken();
const idToken = session.getIdToken().getJwtToken();
```

## Social Sign-In

### Supported Providers
- Google
- Facebook
- Apple
- Amazon
- SAML 2.0
- OpenID Connect

### Configure Social Provider
```bash
aws cognito-idp create-user-pool-client \
  --user-pool-id us-east-1_xxxxxxx \
  --client-name MyWebApp \
  --supported-identity-providers Facebook Google \
  --provider-details '{
    "Facebook": {
      "client_id": "your-fb-client-id",
      "client_secret": "your-fb-client-secret"
    },
    "Google": {
      "client_id": "your-google-client-id",
      "client_secret": "your-google-client-secret"
    }
  }'
```

## Hosted UI

```bash
# Enable hosted UI
aws cognito-idp create-user-pool-client \
  --user-pool-id us-east-1_xxxxxxx \
  --client-name MyWebApp \
  --callback-urls '["https://myapp.com/callback"]' \
  --logout-urls '["https://myapp.com/logout"]' \
  --allowed-o-auth-flows code \
  --allowed-o-auth-scopes openid email profile \
  --allowed-o-auth-flows-user-pool-client
```

## Custom Attributes

```bash
# Create user pool with custom attributes
aws cognito-idp create-user-pool \
  --pool-name MyUserPool \
  --schema '[
    {
      "Name": "custom:company",
      "AttributeDataType": "String",
      "Mutable": true,
      "Required": false
    }
  ]'
```

## MFA Configuration

```bash
# Enable MFA
aws cognito-idp set-user-pool-mfa-config \
  --user-pool-id us-east-1_xxxxxxx \
  --sms-mfa-configuration '{
    "SmsAuthenticationMessage": "Your code is {####}",
    "SmsConfiguration": {
      "SnsCallerArn": "arn:aws:iam::123456789012:role/SNSRole",
      "ExternalId": "your-external-id"
    }
  }' \
  --software-token-mfa-configuration '{ "Enabled": true }'
```

## Lambda Triggers

```bash
# Set pre-sign-up trigger
aws cognito-idp update-user-pool \
  --user-pool-id us-east-1_xxxxxxx \
  --lambda-config '{
    "PreSignUp": "arn:aws:lambda:us-east-1:123456789012:function:preSignUp",
    "PostConfirmation": "arn:aws:lambda:us-east-1:123456789012:function:postConfirmation",
    "CustomMessage": "arn:aws:lambda:us-east-1:123456789012:function:customMessage"
  }'
```

### Trigger Types
| Trigger          | Description                    |
|------------------|--------------------------------|
| PreSignUp        | Before user confirmation       |
| PostConfirmation | After user confirmation        |
| PreAuthentication| Before authentication          |
| PostAuthentication| After authentication          |
| CustomMessage    | Custom email/SMS messages      |
| DefineAuthChallenge | Custom auth flow           |

## App Integration

### React Example
```javascript
import { Amplify } from 'aws-amplify';
import { withAuthenticator } from '@aws-amplify/ui-react';
import '@aws-amplify/ui-react/styles.css';

Amplify.configure({
  Auth: {
    region: 'us-east-1',
    userPoolId: 'us-east-1_xxxxxxx',
    userPoolWebClientId: 'your-client-id'
  }
});

function App() {
  return <h1>Welcome to My App</h1>;
}

export default withAuthenticator(App);
```

## Security

### Token Best Practices
- **Validate tokens** on backend
- **Use short-lived access tokens**
- **Refresh tokens** securely
- **Store tokens** securely

### User Pool Security
- Enable MFA
- Use strong password policies
- Implement account recovery
- Monitor sign-in attempts

## Cost Optimization

- **Free tier**: 50,000 MAUs (Monthly Active Users)
- **MFA**: $0.05 per SMS message
- **Hosted UI**: Included
- **Social providers**: Included

## Best Practices

1. **Use User Pools** for user management
2. **Use Identity Pools** for AWS credentials
3. **Implement MFA** for security
4. **Use Lambda triggers** for customization
5. **Enable hosted UI** for quick setup
6. **Implement social sign-in** for UX
7. **Use custom attributes** for user data
8. **Monitor sign-in** attempts
9. **Implement account recovery**
10. **Use token validation** on backend
