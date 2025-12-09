# Admin Role Documentation

## Overview

The Plantation System has **three user roles** with different permission levels:

1. **VOLUNTEER** - Regular users who can join meetups and make donations
2. **ORG_ADMIN** - Organization administrators who manage their organization
3. **ADMIN** - System administrators with full system control

---

## Admin Role Capabilities

### What Can Admins Do?

**Admins have full system control including:**

1. **User Management**
   - View all users in the system
   - Change any user's role (VOLUNTEER ↔ ORG_ADMIN ↔ ADMIN)
   - Change any user's status (ACTIVE/INACTIVE/SUSPENDED)
   - Delete any user account from the system

2. **System Statistics & Monitoring**
   - View total user count
   - Get breakdown of users by role
   - View organization statistics
   - View meetup statistics
   - View donation statistics
   - Access comprehensive dashboard with all system metrics

3. **System Health**
   - Check system health status
   - Monitor system performance

### What Can ORG_ADMIN Do?

**Organization Admins can:**
- Create organizations
- Delete their own organizations
- Manage all meetups for their organization
- View donations to their organization
- Manage volunteer profiles in their organization
- View all organizations (but not modify others')

### What Can VOLUNTEER Do?

**Regular Volunteers can:**
- Manage their own user profile
- Create meetups (becomes host)
- Join meetups as participants
- Make donations
- Manage their volunteer profile

---

## Admin Endpoints (Complete Reference)

All admin endpoints are protected by `@PreAuthorize("hasRole('ADMIN')")` and accessible only to users with ADMIN role.

### Base URL
```
/api/admin
```

### User Management Endpoints

#### 1. Get All Users
```
GET /api/admin/users
```
**Response:**
```json
[
  {
    "userId": 1,
    "username": "john_volunteer",
    "email": "john@example.com",
    "role": "VOLUNTEER",
    "status": "ACTIVE",
    "phone": "1234567890"
  },
  ...
]
```

#### 2. Get User by ID
```
GET /api/admin/users/{userId}
```
**Example:**
```
GET /api/admin/users/5
```
**Response:**
```json
{
  "userId": 5,
  "username": "jane_user",
  "email": "jane@example.com",
  "role": "VOLUNTEER",
  "status": "ACTIVE",
  "phone": "9876543210"
}
```

#### 3. Update User Role
```
PUT /api/admin/users/{userId}/role
```
**Request Body:**
```json
{
  "role": "ORG_ADMIN"
}
```
**Valid Roles:** `VOLUNTEER`, `ORG_ADMIN`, `ADMIN`

**Response:**
```json
{
  "userId": 5,
  "username": "jane_user",
  "email": "jane@example.com",
  "role": "ORG_ADMIN",
  "status": "ACTIVE"
}
```

#### 4. Update User Status
```
PUT /api/admin/users/{userId}/status
```
**Request Body:**
```json
{
  "status": "SUSPENDED"
}
```
**Valid Statuses:** `ACTIVE`, `INACTIVE`, `SUSPENDED`

**Response:**
```json
{
  "userId": 5,
  "username": "jane_user",
  "email": "jane@example.com",
  "role": "VOLUNTEER",
  "status": "SUSPENDED"
}
```

#### 5. Delete User
```
DELETE /api/admin/users/{userId}
```
**Response:**
```json
{
  "message": "User deleted successfully"
}
```

### Statistics & Dashboard Endpoints

#### 6. Get User Count
```
GET /api/admin/stats/user-count
```
**Response:**
```json
{
  "totalUsers": 25
}
```

#### 7. Get Users by Role
```
GET /api/admin/stats/users-by-role
```
**Response:**
```json
{
  "VOLUNTEER": 20,
  "ORG_ADMIN": 4,
  "ADMIN": 1,
  "TOTAL": 25
}
```

#### 8. Get Organization Count
```
GET /api/admin/stats/organization-count
```
**Response:**
```json
{
  "totalOrganizations": 5
}
```

#### 9. Get Meetup Count
```
GET /api/admin/stats/meetup-count
```
**Response:**
```json
{
  "totalMeetups": 42
}
```

#### 10. Get Donation Statistics
```
GET /api/admin/stats/donation-stats
```
**Response:**
```json
{
  "totalDonations": 100,
  "approvedDonations": 85,
  "pendingDonations": 10,
  "rejectedDonations": 5
}
```

#### 11. Get System Dashboard
```
GET /api/admin/dashboard
```
**Response:**
```json
{
  "totalUsers": 25,
  "usersByRole": {
    "VOLUNTEER": 20,
    "ORG_ADMIN": 4,
    "ADMIN": 1,
    "TOTAL": 25
  },
  "activeUsers": 23,
  "inactiveUsers": 1,
  "suspendedUsers": 1,
  "totalOrganizations": 5,
  "totalMeetups": 42,
  "donationStats": {
    "totalDonations": 100,
    "approvedDonations": 85,
    "pendingDonations": 10,
    "rejectedDonations": 5
  },
  "systemTime": 1701907200000,
  "systemStatus": "OPERATIONAL"
}
```

#### 12. Get System Health
```
GET /api/admin/health
```
**Response:**
```json
{
  "status": "UP",
  "timestamp": 1701907200000,
  "message": "System is running normally"
}
```

---

## How to Register as Admin

### Method 1: Direct Registration (Development Only)
Use the standard registration endpoint to register, then manually update the role in the database:

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin_user",
    "email": "admin@example.com",
    "password": "SecurePassword123!",
    "phone": "1234567890",
    "role": "ADMIN"
  }'
```

**Important Note:** If you try to register with `"role": "ADMIN"`, the system will accept it IF you provide that field in the request. Otherwise it defaults to VOLUNTEER.

### Method 2: Admin Creates Admin (After First Admin Exists)
Once you have one ADMIN account, that admin can:
1. Register a new user with VOLUNTEER role
2. Use the update role endpoint to change them to ADMIN

```bash
# Step 1: Register as volunteer
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "new_admin",
    "email": "newadmin@example.com",
    "password": "SecurePassword123!",
    "phone": "1234567890",
    "role": "VOLUNTEER"
  }'

# Step 2: Login as existing admin to get token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin_user",
    "password": "SecurePassword123!"
  }'

# Step 3: Use admin token to promote new user to ADMIN
curl -X PUT http://localhost:8080/api/admin/users/{newUserId}/role \
  -H "Authorization: Bearer {ADMIN_JWT_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "role": "ADMIN"
  }'
```

### Method 3: Database Direct Update (For Development/Initialization)
Execute SQL directly on the database:

```sql
-- For PostgreSQL
UPDATE "user" SET role = 'ADMIN' WHERE user_id = 1;

-- For H2 (Development)
UPDATE USER SET ROLE = 'ADMIN' WHERE USER_ID = 1;
```

---

## Testing Admin Endpoints

### Step 1: Register and Login as Admin

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin_user",
    "email": "admin@example.com",
    "password": "SecurePassword123!",
    "phone": "1234567890",
    "role": "ADMIN"
  }'

# Login to get JWT token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin_user",
    "password": "SecurePassword123!"
  }'

# Response will include:
# {
#   "token": "eyJhbGciOiJIUzI1NiJ9...",
#   "username": "admin_user",
#   "email": "admin@example.com",
#   "role": "ADMIN",
#   "userId": 1
# }
```

### Step 2: Use Admin Token to Access Admin Endpoints

```bash
# Get all users
curl -X GET http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."

# Get dashboard
curl -X GET http://localhost:8080/api/admin/dashboard \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."

# Update user role
curl -X PUT http://localhost:8080/api/admin/users/2/role \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "role": "ORG_ADMIN"
  }'
```

---

## Role-Based Access Control Summary

| Feature | VOLUNTEER | ORG_ADMIN | ADMIN |
|---------|-----------|-----------|-------|
| View own profile | ✅ | ✅ | ✅ |
| Update own profile | ✅ | ✅ | ✅ |
| Change own password | ✅ | ✅ | ✅ |
| Create organization | ❌ | ✅ | ✅ |
| Delete own organization | ❌ | ✅ | ✅ |
| Create meetup | ✅ | ✅ | ✅ |
| Delete own meetup | ✅ | ✅ | ✅ |
| View all users | ❌ | ❌ | ✅ |
| Delete any user | ❌ | ❌ | ✅ |
| Change user role | ❌ | ❌ | ✅ |
| Change user status | ❌ | ❌ | ✅ |
| View system statistics | ❌ | ❌ | ✅ |
| Access dashboard | ❌ | ❌ | ✅ |

---

## Security Notes

⚠️ **Important Security Considerations:**

1. **Admin accounts are powerful** - Only create admin accounts for trusted system administrators
2. **JWT Token expiration** - Tokens have a limited lifetime. Always use the token from the latest login
3. **Password security** - Admins should use strong passwords (min 8 chars, mix of upper/lower/numbers/special chars)
4. **Audit trail** - All admin actions are logged in the ActivityLog table
5. **Protected endpoints** - Non-admin users will receive 403 Forbidden if they try to access admin endpoints

---

## Example: Creating a Full Admin Account from Scratch

### Using Postman or cURL:

1. **Register as ADMIN:**
```
POST /api/auth/register
Content-Type: application/json

{
  "username": "system_admin",
  "email": "sysadmin@plantation.com",
  "password": "Admin@123456",
  "phone": "5551234567",
  "role": "ADMIN"
}
```

2. **Login to get token:**
```
POST /api/auth/login
Content-Type: application/json

{
  "username": "system_admin",
  "password": "Admin@123456"
}
```

3. **Save the JWT token from response and use in all admin endpoints:**
```
Authorization: Bearer <TOKEN_HERE>
```

4. **Start using admin features:**
```
GET /api/admin/dashboard
Authorization: Bearer <TOKEN_HERE>
```

---

## Troubleshooting

### Issue: "Access Denied" or 403 Forbidden
- **Cause:** User doesn't have ADMIN role
- **Solution:** Make sure the user's role is set to ADMIN before using admin endpoints

### Issue: "Invalid token" or 401 Unauthorized
- **Cause:** Token is missing, expired, or invalid
- **Solution:** Login again to get a fresh token

### Issue: Role update not working
- **Cause:** Invalid role name provided
- **Solution:** Use one of the valid roles: `VOLUNTEER`, `ORG_ADMIN`, `ADMIN`

### Issue: Cannot access admin endpoints from non-admin account
- **Cause:** Current user doesn't have ADMIN role
- **Solution:** This is intentional security - only admins can access admin endpoints

---

## Next Steps

After setting up your admin account, you can:
1. Monitor system health via `/api/admin/health`
2. View system statistics via `/api/admin/stats/*` endpoints
3. Manage users via `/api/admin/users/*` endpoints
4. Access the comprehensive dashboard via `/api/admin/dashboard`
5. Create more admin accounts as needed for your team
