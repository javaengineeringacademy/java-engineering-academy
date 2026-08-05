# Build vs Buy

## Problem Statement

Should you build a custom solution or use a third-party service? Building gives you control but costs time and maintenance. Buying gives you speed but costs money and reduces control.

## The Core Tension

Building: You own the code, control the roadmap, and can customize exactly. But you pay the full development and maintenance cost.

Buying: You get a working solution today, benefit from the vendor's expertise, and offload maintenance. But you pay ongoing fees, depend on a vendor, and accept their design decisions.

## The TCO Analysis

Total Cost of Ownership is not just the purchase price. Calculate:

**Build costs**:
- Initial development time and salary cost
- Ongoing maintenance (estimated 15-20% of initial development per year)
- Infrastructure and hosting
- Opportunity cost (what else could the team build?)
- Bug fixes and security patches
- Documentation and training

**Buy costs**:
- Subscription or license fees (monthly/annual)
- Integration and configuration time
- Vendor lock-in risk (cost to switch later)
- Customization limitations
- Data portability concerns

## When to Build

**Your core differentiator**: If the feature is what makes your product unique, own it. Netflix builds its recommendation engine. They buy their HR system.

**No good vendor exists**: When existing solutions are inadequate or nonexistent, you must build.

**Regulatory requirements**: When you cannot entrust sensitive data to a third party due to compliance.

**Extreme scale**: When your scale is beyond what vendors support economically.

**Deep integration**: When the feature must be tightly coupled with your internal systems.

**Long-term cost**: When the cumulative vendor fees exceed build and maintain costs within 2-3 years.

## When to Buy

**Commodity features**: Authentication, email delivery, payment processing, analytics. These are solved problems.

**Time-to-market pressure**: When shipping this quarter matters more than owning the solution.

**Lack of expertise**: When the vendor has domain expertise you lack.

**Small team**: When your team is too small to maintain another system.

**Non-core**: When the feature does not differentiate your product.

**Rapidly changing landscape**: When the technology is evolving faster than you can keep up.

## Common Examples

| Capability | Build | Buy | Why |
|-----------|-------|-----|-----|
| Authentication | Possible | Auth0, Clerk | Solved problem, security is hard |
| Email delivery | Rarely | SendGrid, SES | Deliverability is complex |
| Payment processing | Never | Stripe, Square | PCI compliance is brutal |
| Analytics | Sometimes | Mixpanel, Amplitude | Vendor has better data |
| Recommendation engine | Sometimes | Amazon Personalize | Your data is your advantage |
| Search | Depends | Algolia, Elastic Cloud | Your search UX is different |
| CMS | Possible | Contentful | If content is not your core |

## Decision Framework

Score each option from 1-5 on these factors:

**Strategic importance**: How core is this to your product?
**Time sensitivity**: How soon do you need this?
**Team capability**: Does your team have the skills?
**Long-term cost**: What is the 3-year TCO?
**Vendor risk**: How likely is vendor lock-in or failure?

Build wins when strategic importance and team capability are high. Buy wins when time sensitivity is high and strategic importance is low.

## The Hybrid Approach

Often the best answer is to buy the foundation and build the differentiator:

- Buy the payment processor, build the checkout UX
- Buy the email service, build the email templates and logic
- Buy the hosting, build the deployment pipeline
- Buy the CDN, build the cache invalidation strategy

## Interview Relevance

**Common questions**:
- "How would you implement payments in your system?"
- "Would you build your own message queue or use Kafka?"

**What interviewers want**:
- You can identify what is commodity vs what is core
- You understand TCO, not just sticker price
- You can reason about vendor lock-in
- You know when building is a trap

**Red flags**:
- Always saying "build" without considering maintenance cost
- Always saying "buy" without considering strategic importance
- Not considering opportunity cost
- Not acknowledging that some things should never be built in-house

## Key Takeaway

The question is not "build or buy" but "what is our core differentiator, and should we build or buy everything else?" Most teams under-buy, spending months rebuilding commodity features they could have rented for a fraction of the cost.
