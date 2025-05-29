# Development Starter

A Spring Boot starter providing quick development setup with JPA, Web, H2, and common configurations.

## Why This Starter?

During development and prototyping phases, developers often need to:
- Set up basic database configurations repeatedly
- Configure common development tools and settings
- Deal with boilerplate code for basic features
- Maintain consistency across multiple projects
- Quickly prototype ideas without extensive setup

This starter addresses these challenges by providing a pre-configured development environment that's ready to use, allowing developers to focus on building features rather than setting up infrastructure.

## Features

- Spring Data JPA configuration
- Web starter configuration
- H2 database setup
- Common development configurations
- Pre-configured development properties
- Sensible defaults for local development

## Installation

Add this dependency to your project:

```xml
<dependency>
    <groupId>io.github.rp-1701</groupId>
    <artifactId>dev-starter</artifactId>
    <version>0.0.1</version>
</dependency>
```

## Usage

This starter will automatically configure:

1. JPA with H2 database
   - In-memory database for development
   - Auto-configured entity scanning
   - Basic JPA properties

2. Web endpoints
   - Basic error handling
   - Common REST configurations
   - Development-friendly CORS settings

3. Common development settings
   - Development-specific logging
   - Hot reload support
   - Debug configurations

## Example

```java
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

That's it! The starter will automatically configure everything else.

## Documentation

- [Publishing Guide](PUBLISHING.md) - Instructions for publishing updates to Maven Central
- [SSH Setup Guide](SSH_SETUP.md) - Detailed guide for setting up SSH with multiple GitHub accounts

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details. 