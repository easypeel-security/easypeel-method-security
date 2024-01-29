# Contributing to Easypeel Method Security

Easypeel Method Security is released under the Apache 2.0 license. If you would like to contribute
something, or want to hack on the code this document should help you get started.

## Code of Conduct

This project adheres to the Contributor Covenant [code of conduct](CODE_OF_CONDUCT.md).
By participating, you are expected to uphold this code

## Reporting Security Vulnerabilities

If you discover a security vulnerability within Easypeel Method Security, Please
email [penekhun@gmail.com].

[penekhun@gmail.com]: mailto:penekhun@gmail.com

## Code Conventions and Housekeeping

This project follows the style guide specified in `config/checkstyle/checkstyle.xml`.
The guide is enforced in production and test code, and builds will fail if the guide is not
followed. (It is more loosely enforced in test code).

- Configuration files for Intellij users (checkstyle, copyright ... ) exist under `.idea`.
- Add yourself as an @author to the .java files that you modify substantially (more than cosmetic
  changes).
- Add some Javadocs.
- A few unit tests would help a lot as well - someone has to do it.
- Verification tasks, including tests and Checkstyle, can be executed by running ./gradlew check
  from the project root.
