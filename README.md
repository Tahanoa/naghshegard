# 🌍 Iran Travel Blog - Tourism Platform

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.x-green.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> A comprehensive tourism platform for discovering and sharing Iran's beautiful destinations. Users can explore places, write travelogues, share experiences, and engage with a community of travelers.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Security](#security)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

**Iran Travel Blog** is a full-stack web application that allows users to:

- 🏛️ Discover and share tourist attractions across Iran
- 📝 Write and publish travelogues with photos
- ⭐ Rate and review places
- ❤️ Like and bookmark favorite destinations
- 🗺️ View locations on interactive maps
- 👥 Connect with other travelers

## ✨ Features

### 👤 User Features

- **Authentication & Authorization**
  - Secure JWT-based authentication
  - Email verification for registration
  - Password reset functionality
  - Role-based access control (User, Moderator, Admin)

- **Tourist Places**
  - Browse approved places with pagination
  - Search and filter by name, description
  - View place details with photo gallery
  - Interactive map integration (Leaflet + Google Maps)
  - Rate and review places
  - Like/unlike places
  - Track view counts and popularity

- **Travelogues**
  - Create and publish travelogues
  - Upload multiple photos per travelogue
  - Tag visited places
  - Like and view count tracking
  - Search and filter travelogues

- **User Dashboard**
  - View personal places and travelogues
  - Manage content (edit/delete)
  - Track activity statistics

### 👑 Admin Features

- **Admin Dashboard**
  - Comprehensive statistics dashboard
  - Manage all content (places, travelogues, reviews)
  - Approve/reject submissions
  - Bulk approval actions
  - User management (roles, status)

- **Moderator Capabilities**
  - Manage content with limited permissions
  - View all analytics
  - Approve/reject pending content

## 🛠️ Tech Stack

### Backend

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Core language |
| Spring Boot | 3.2.x | Framework |
| Spring Security | 6.x | Authentication & Authorization |
| JWT | 0.12.x | Token-based authentication |
| Hibernate | 6.x | ORM |
| PostgreSQL | 16 | Database |
| Maven | 3.9.x | Build tool |
| JavaMailSender | - | Email service |
| Thymeleaf | 3.x | Email templates |

### Frontend

| Technology | Purpose |
|------------|---------|
| HTML5 + CSS3 | Structure & styling |
| Vanilla JavaScript | Client-side logic |
| Leaflet | Interactive maps |
| Google Maps | Location sharing |
| Font Awesome | Icons |
| Responsive Design | Mobile-friendly |

### Security Features

- 🔐 JWT token-based authentication (stateless)
- 🍪 HttpOnly cookie storage for security
- 🛡️ Password encryption with BCrypt
- 🔒 Role-based access control (RBAC)
- ✅ Email verification
- 🔑 Secure password reset flow
- 📧 CSRF protection

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────┐
│                    Frontend                         │
│  (HTML + CSS + JavaScript + Thymeleaf)             │
└──────────────────┬──────────────────────────────────┘
                   │ REST API
┌──────────────────▼──────────────────────────────────┐
│                   Backend                           │
│  ┌──────────────────────────────────────────────┐   │
│  │              Controllers                     │   │
│  │  (WebController + REST APIs)                │   │
│  └──────────────────┬───────────────────────────┘   │
│  ┌──────────────────▼───────────────────────────┐   │
│  │               Services                       │   │
│  │  (Business Logic Layer)                     │   │
│  └──────────────────┬───────────────────────────┘   │
│  ┌──────────────────▼───────────────────────────┐   │
│  │              Repository                      │   │
│  │  (Data Access Layer - JPA)                 │   │
│  └──────────────────┬───────────────────────────┘   │
└─────────────────────┼─────────────────────────────┘
                      │
┌─────────────────────▼─────────────────────────────┐
│              PostgreSQL Database                   │
│  (Users, Places, Travelogues, Reviews, Photos)    │
└────────────────────────────────────────────────────┘
```

## 🚀 Getting Started

### Prerequisites

- JDK 17 or later
- PostgreSQL 14+
- Maven 3.9+
- IDE (IntelliJ IDEA/Eclipse/VSCode)

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/iran-travel-blog.git
cd iran-travel-blog
```

2. **Configure Database**
```sql
CREATE DATABASE tblog;
CREATE USER your_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE tblog TO your_user;
```

3. **Configure application.properties**
```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/tblog
spring.datasource.username=your_user
spring.datasource.password=your_password

# Email (for verification)
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password

# JWT
jwt.secret=your_super_secret_key_min_64_characters
jwt.expiration=86400000
```

4. **Build and Run**
```bash
mvn clean install
mvn spring-boot:run
```

5. **Access Application**
```
http://localhost:8080
```

### Default Admin Account

```json
{
  "username": "admin",
  "password": "admin123",
  "email": "admin@example.com"
}
```

## 📚 API Documentation

### Authentication Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login and get JWT token |
| POST | `/api/auth/register/send-code` | Send verification code |
| POST | `/api/auth/register/verify` | Verify code and register |
| POST | `/api/auth/forgot-password/send-code` | Send reset code |
| POST | `/api/auth/forgot-password/reset` | Reset password |
| GET | `/api/auth/me` | Get current user info |

### Place Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/places/public` | Get approved places (paginated) |
| GET | `/api/places/public/latest` | Get latest places |
| GET | `/api/places/public/most-viewed` | Most viewed places |
| GET | `/api/places/public/most-liked` | Most liked places |
| GET | `/api/places/public/search` | Search places |
| GET | `/api/places/my-places` | Get user's places |
| POST | `/api/places/add` | Add new place |
| GET | `/api/places/{id}` | Get place details |
| POST | `/api/places/{id}/like` | Like a place |
| DELETE | `/api/places/{id}` | Delete place (owner/admin) |

### Travelogue Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/travelogues/public` | Get approved travelogues |
| GET | `/api/travelogues/public/latest` | Latest travelogues |
| GET | `/api/travelogues/public/{id}` | Get travelogue details |
| GET | `/api/travelogues/my-travelogues` | Get user's travelogues |
| POST | `/api/travelogues/add` | Create new travelogue |
| POST | `/api/travelogues/{id}/like` | Like a travelogue |
| DELETE | `/api/travelogues/{id}` | Delete travelogue |

### Review Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/reviews/add` | Add review |
| GET | `/api/reviews/place/{placeId}` | Get place reviews |
| DELETE | `/api/reviews/{id}` | Delete review |

### Admin Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/admin/dashboard/stats` | Dashboard statistics |
| GET | `/api/admin/places/all` | All places |
| PUT | `/api/admin/places/{id}/approve` | Approve place |
| PUT | `/api/admin/places/{id}/reject` | Reject place |
| PUT | `/api/admin/travelogues/{id}/approve` | Approve travelogue |
| PUT | `/api/admin/reviews/{id}/approve` | Approve review |
| GET | `/api/admin/users/all` | All users |
| PUT | `/api/admin/users/{id}/role` | Change user role |
| PUT | `/api/admin/users/{id}/toggle-status` | Enable/disable user |

## 📊 Database Schema

### Entity Relationship Diagram

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│    Users    │────▶│  Tourist    │────▶│   Photos    │
│             │     │   Places    │     │             │
│  - id       │     │  - id       │     │  - id       │
│  - username │     │  - name     │     │  - image_data│
│  - email    │     │  - desc     │     │  - place_id │
│  - password │     │  - user_id  │     │  - travelogue│
│  - role     │     │  - status   │     └─────────────┘
│  - enabled  │     └──────┬──────┘
└──────┬──────┘            │
       │                   │
       │   ┌───────────────▼───────────────┐
       │   │             Reviews            │
       │   │  - id                         │
       │   │  - rating                     │
       │   │  - comment                    │
       │   │  - user_id                    │
       │   │  - place_id                   │
       │   │  - approved                   │
       │   └───────────────┬───────────────┘
       │                   │
       │   ┌───────────────▼───────────────┐
       │   │          Travelogues           │
       │   │  - id                         │
       │   │  - title                      │
       │   │  - content                    │
       │   │  - user_id                    │
       │   │  - visited_places             │
       │   │  - view_count                 │
       │   │  - like_count                 │
       │   │  - approved                   │
       │   └───────────────┬───────────────┘
       │                   │
       │   ┌───────────────▼───────────────┐
       │   │      Verification Codes        │
       │   │  - id                         │
       │   │  - email                      │
       │   │  - code                       │
       │   │  - type (REGISTER/RESET)      │
       │   │  - expires_at                 │
       │   │  - used                       │
       │   └───────────────────────────────┘
```

### Key Relationships

- **User → Tourist Place**: One-to-Many (A user can introduce many places)
- **User → Travelogue**: One-to-Many (A user can write many travelogues)
- **User → Review**: One-to-Many (A user can write many reviews)
- **Tourist Place → Review**: One-to-Many (A place can have many reviews)
- **Tourist Place → Photo**: One-to-Many (A place can have many photos)
- **Travelogue → Photo**: One-to-Many (A travelogue can have many photos)

## 🔐 Security

### Authentication Flow

```
1. User registers → Email verification code sent
2. User verifies code → Account activated
3. User logs in → JWT token generated
4. Token stored in HttpOnly cookie
5. Every request validated via JWT filter
6. Role-based authorization enforced
```

### Role Permissions

| Permission | User | Moderator | Admin |
|------------|------|-----------|-------|
| View content | ✅ | ✅ | ✅ |
| Create places | ✅ | ✅ | ✅ |
| Edit own content | ✅ | ✅ | ✅ |
| Delete own content | ✅ | ✅ | ✅ |
| Approve content | ❌ | ✅ | ✅ |
| Manage users | ❌ | ❌ | ✅ |
| Bulk actions | ❌ | ❌ | ✅ |
| Access admin panel | ❌ | ✅ | ✅ |

## 📈 Performance Optimizations

- **Caching**: Spring Cache for frequently accessed data
- **Pagination**: All list endpoints support pagination
- **Lazy Loading**: JPA lazy loading for relationships
- **Image Optimization**: Byte array storage in PostgreSQL
- **Indexing**: Database indexes on frequently queried fields

## 🤝 Contributing

1. Fork the repository
2. Create feature branch: `git checkout -b feature/amazing-feature`
3. Commit changes: `git commit -m 'Add amazing feature'`
4. Push to branch: `git push origin feature/amazing-feature`
5. Open Pull Request

### Code Style

- Follow Java 17 conventions
- Use meaningful variable names (Persian/English)
- Add comments for complex logic
- Write unit tests for new features

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Spring Boot** - Amazing framework
- **PostgreSQL** - Reliable database
- **Leaflet** - Interactive maps
- **Font Awesome** - Beautiful icons
- **OpenStreetMap** - Free map data

## 📧 Contact

- **Project Maintainer**: [Taha]
- **Email**: tahafarzaneh3524@gmail.com
- **GitHub**: [Your GitHub](https://github.com/Tahanoa)

---

<div align="center">
  <strong>🌟 Made with ❤️ for Iran's Tourism 🌟</strong>
</div>
