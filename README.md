# Keycloak OAuth2 Authentication with JWT

## Overview

**Keycloak** for OAuth2 authentication in a Spring Boot application. It utilizes a **JWT Converter** to manage token claims, enforce validation, and handle role-based access control.

## Key Concepts

- **OAuth2**: A protocol that enables third-party applications to obtain limited access to user accounts without exposing passwords.
- **JWT (JSON Web Token)**: A compact, URL-safe means of representing claims to be transferred between two parties. It allows the verification of the token's authenticity.
- **Keycloak**: An open-source Identity and Access Management solution that provides authentication and authorization services.

## Architecture

1. **User Authentication**: Users authenticate via Keycloak and receive a JWT upon successful login.
2. **Role-Based Access Control**: Access to resources is determined by user roles specified in the JWT.
3. **Email Domain Verification**: Users must possess an email address from **Abdelmalek Essaâdi University** (`@etu.uae.ac.ma`) to authenticate.

## Security Configuration

- **HttpSecurity Configuration**: Secures endpoints based on roles. Only users with the appropriate roles can access specific paths.
- **OAuth2 Login**: Integrates OAuth2 login for user authentication.
- **Stateless Authentication**: Utilizes JWT tokens for authentication, eliminating the need for server-side sessions.

## JWT Converter

- **Role Extraction**: Extracts roles from the JWT's `realm_access` claim.
- **Email Validation**: Validates the user's email domain to ensure it matches the required format.
- **Authentication Token Creation**: Generates an authentication token based on the JWT and associated authorities.

