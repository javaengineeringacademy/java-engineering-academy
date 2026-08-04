# Open Source Licenses

## Table of Contents

- [Introduction](#introduction)
- [Why Licenses Matter](#why-licenses-matter)
- [Types of Licenses](#types-of-licenses)
- [Major Open Source Licenses](#major-open-source-licenses)
- [License Compatibility](#license-compatibility)
- [Choosing a License](#choosing-a-license)
- [License Compliance](#license-compliance)
- [Common Scenarios](#common-scenarios)
- [Resources](#resources)

---

## Introduction

An open source license is a legal instrument governing the use, modification, and distribution of software. It defines the rights and obligations of users, contributors, and maintainers.

Understanding licenses is crucial for anyone involved in open source, whether you're using, contributing to, or maintaining open source software.

---

## Why Licenses Matter

### Legal Protection

- **For Users**: Clarifies what you can and cannot do with the software
- **For Contributors**: Defines rights to contributed code
- **For Maintainers**: Protects against misuse

### Community Building

- **Transparency**: Clear rules for everyone
- **Trust**: Users know their rights
- **Collaboration**: Enables contributions

### Business Considerations

- **Compliance**: Avoid legal issues
- **Integration**: Know what licenses are compatible
- **Distribution**: Understand obligations when sharing

---

## Types of Licenses

### Permissive Licenses

**Characteristics:**
- Minimal restrictions on use
- Allow proprietary use
- Require attribution
- Permit modifications

**Examples:**
- MIT License
- BSD License
- Apache License 2.0
- ISC License

**Best for:**
- Maximizing adoption
- Commercial use
- Library/framework projects

### Copyleft Licenses

**Characteristics:**
- Require derivative works to use same license
- Preserve open source nature
- Require source code availability
- Strong protection of user freedom

**Examples:**
- GNU GPL (v2, v3)
- GNU LGPL
- AGPL

**Best for:**
- Protecting user freedom
- Ensuring derivatives remain open
- Community-driven projects

### Weak Copyleft Licenses

**Characteristics:**
- Copyleft applies only to modified files
- Allow linking with proprietary code
- Less restrictive than strong copyleft

**Examples:**
- Mozilla Public License (MPL)
- Eclipse Public License (EPL)
- Common Development and Distribution License (CDDL)

**Best for:**
- Balancing openness and flexibility
- Projects wanting some copyleft protection
- Enterprise environments

---

## Major Open Source Licenses

### MIT License

**Overview:**
The most popular open source license. Very permissive with minimal restrictions.

**Key Terms:**
- ✅ Commercial use
- ✅ Modification
- ✅ Distribution
- ✅ Private use
- ✅ Liability limitation
- ❗ Include copyright notice
- ❗ Include license text

**Use Cases:**
- Libraries and frameworks
- Small to medium projects
- Projects wanting maximum adoption

**Example:**
```
MIT License

Copyright (c) [year] [fullname]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

### Apache License 2.0

**Overview:**
Permissive license with explicit patent grant and contributor license agreement.

**Key Terms:**
- ✅ Commercial use
- ✅ Modification
- ✅ Distribution
- ✅ Patent use
- ✅ Private use
- ❗ Include license and NOTICE
- ❗ State changes
- ❗ Include copyright notice

**Use Cases:**
- Enterprise projects
- Projects wanting patent protection
- Large-scale projects

### GNU GPL v3

**Overview:**
Strong copyleft license ensuring derivatives remain open source.

**Key Terms:**
- ✅ Commercial use
- ✅ Modification
- ✅ Distribution
- ✅ Patent use
- ✅ Private use
- ❗ Source code must be available
- ❗ Derivative works must use GPL
- ❗ Changes must be documented
- ❗ Include license text

**Use Cases:**
- Projects protecting user freedom
- Community-driven software
- Projects against proprietary derivatives

### GNU GPL v2

**Overview:**
Original GPL version, still widely used.

**Key Terms:**
- Same as GPL v3
- ❗ No tivoization clause (hardware restrictions)
- ❗ No explicit patent grant

**Note:** Many projects use "GPL v2 or later" for flexibility.

### BSD Licenses

**2-Clause BSD (Simplified):**
- Most permissive
- Similar to MIT
- Minimal restrictions

**3-Clause BSD:**
- Adds non-endorsement clause
- Prevents using contributor names for promotion

**Use Cases:**
- Academic projects
- Libraries wanting minimal restrictions

### Mozilla Public License (MPL)

**Overview:**
Weak copyleft license applying to modified files.

**Key Terms:**
- ✅ Commercial use
- ✅ Modification
- ✅ Distribution
- ❗ Modified files must include license
- ❗ Source code for modified files
- ❗ Allows proprietary linking

**Use Cases:**
- Balancing openness and flexibility
- Projects wanting file-level copyleft
- Enterprise environments

---

## License Compatibility

### Compatibility Matrix

| License | Can Use | Can Combine With | Must Use Same License |
|---------|---------|------------------|----------------------|
| MIT | ✅ | Any | No |
| Apache 2.0 | ✅ | MIT, BSD, Apache | No |
| GPL v2 | ✅ | GPL v2, GPL v2+ | Yes (GPL v2) |
| GPL v3 | ✅ | GPL v3, GPL v3+ | Yes (GPL v3) |
| LGPL | ✅ | Any (for linking) | Yes (for modifications) |
| MPL | ✅ | Most | Yes (for modified files) |

### Compatibility Issues

**Common Incompatibilities:**
- GPL v2 and Apache 2.0 (without "or later")
- GPL v3 and proprietary software
- AGPL with most other licenses

**Solutions:**
- Use "GPL v2 or later" for more flexibility
- Choose compatible licenses
- Get explicit permission

---

## Choosing a License

### Decision Tree

```
Do you want to allow proprietary use?
├── Yes → Do you want to require attribution?
│   ├── Yes → MIT or Apache 2.0
│   └── No → Unlicense or CC0
└── No → Do you want derivatives to be open source?
    ├── Yes → GPL v3
    └── Partially → LGPL or MPL
```

### By Project Type

#### Libraries and Frameworks
- **Recommended:** MIT or Apache 2.0
- **Reason:** Maximum adoption, minimal restrictions

#### Applications
- **Recommended:** GPL v3 or AGPL
- **Reason:** Protect user freedom, ensure derivatives remain open

#### Enterprise Software
- **Recommended:** Apache 2.0 or MPL
- **Reason:** Patent protection, business-friendly

#### Personal Projects
- **Recommended:** MIT
- **Reason:** Simple, permissive, widely understood

### Considerations

1. **Community Goals**: What do you want to achieve?
2. **Adoption**: How widely do you want the software used?
3. **Commercial Use**: Do you want to allow proprietary use?
4. **Contributions**: How do you want to handle contributions?
5. **Legal Protection**: What protection do you need?

---

## License Compliance

### Requirements by License

#### MIT/Apache 2.0
- Include copyright notice
- Include license text
- State changes (Apache 2.0)

#### GPL
- Make source code available
- Include license text
- State changes
- Provide build instructions
- Use same license for derivatives

#### LGPL
- Allow linking without copyleft
- Provide source for modifications
- Use same license for modified files

### Compliance Checklist

```markdown
## License Compliance Checklist

### For Using Open Source
- [ ] Identify all licenses in use
- [ ] Verify compatibility
- [ ] Include required notices
- [ ] Understand obligations

### For Distributing Software
- [ ] Include all required licenses
- [ ] Provide source code (if required)
- [ ] Document changes
- [ ] Include copyright notices

### For Contributing
- [ ] Understand project license
- [ ] Sign CLA (if required)
- [ ] Follow contribution guidelines
- [ ] Don't introduce incompatible licenses
```

### Tools for Compliance

- **FOSSA**: License compliance automation
- **Black Duck**: Open source security and compliance
- **Snyk**: License scanning
- **Licensee**: License detection
- **FOSSology**: License scanning tool

---

## Common Scenarios

### Scenario 1: Using MIT Library in GPL Project

**Question:** Can I use an MIT-licensed library in my GPL project?

**Answer:** Yes! MIT is compatible with GPL. You must:
- Include MIT license and copyright notice
- Your project remains GPL

### Scenario 2: Modifying GPL Code

**Question:** Can I modify GPL code and keep it proprietary?

**Answer:** No. Derivative works must use GPL license.

### Scenario 3: Company Using Open Source

**Question:** What licenses are safe for enterprise use?

**Answer:** Permissive licenses (MIT, Apache 2.0, BSD) are generally safe. GPL can have compliance implications.

### Scenario 4: Dual Licensing

**Question:** Can I offer my project under multiple licenses?

**Answer:** Yes! Common approach:
- Open source version (GPL)
- Commercial version (proprietary license)

---

## Resources

### License Information

- [Choose a License](https://choosealicense.com/) - License selection tool
- [Open Source Initiative](https://opensource.org/licenses) - License definitions
- [GNU Licenses](https://www.gnu.org/licenses/) - GPL family
- [SPDX License List](https://spdx.org/licenses/) - Standard license identifiers

### Compliance Tools

- [FOSSA](https://fossa.com/) - License compliance
- [Black Duck](https://www.blackduck.com/) - Open source management
- [Snyk](https://snyk.io/) - License scanning

### Reading and Learning

- [Open Source Guide](https://opensource.guide/legal/)
- [TLDRLegal](https://tldrlegal.com/) - License summaries
- [Choose a License](https://choosealicense.com/) - Interactive guide

---

**Previous**: [Code of Conduct](../code-of-conduct/README.md)
**Next**: [Governance](../governance/README.md)
