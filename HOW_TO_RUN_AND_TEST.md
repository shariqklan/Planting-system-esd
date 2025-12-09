# 🚀 How to Run and Test the Plantation System

Complete step-by-step guide to get the project running and test all endpoints using Postman.

---

## Prerequisites

Before starting, ensure you have:

✅ **Java 17+** installed  
✅ **Maven** installed (or use `mvnw`/`mvnw.cmd`)  
✅ **PostgreSQL** installed and running (or use H2 for development)  
✅ **Postman** or similar API testing tool  
✅ **Git** (optional, for version control)

---

## Part 1: Build and Run the Application

### Step 1: Navigate to Project Directory

```bash
cd c:\fyp-esd\Planting-system-esd
```

### Step 2: Build the Project

Using Maven:
```bash
mvn clean install
```

Or using the wrapper:
```bash
.\mvnw clean install
```

**Expected Output:**
```
[INFO] Building Plantation System 0.0.1-SNAPSHOT
...
[INFO] BUILD SUCCESS
```

### Step 3: Run the Application

Using Maven:
```bash
mvn spring-boot:run
```

Or using the wrapper:
```bash
.\mvnw spring-boot:run
```

**Expected Console Output:**
```
2024-12-06 10:30:45.123  INFO 12345 --- [main] c.e.Plantation_system.PlantationSystemApplication : Starting PlantationSystemApplication
2024-12-06 10:30:48.456  INFO 12345 --- [main] c.e.Plantation_system.PlantationSystemApplication : Started PlantationSystemApplication in 2.345 seconds
```

### Step 4: Verify Application Started

Check the logs for:
```
Tomcat started on port(s): 8080 (http) with context path ''
```

✅ **Application is running on:** `http://localhost:8080`

---

## Part 2: Setup Postman

### Step 1: Open Postman

1. Launch Postman
2. Create a new **Collection** named "Plantation System"
3. Create a new **Environment** named "Development"

### Step 2: Set Environment Variables

In the Environment settings, add:

| Variable | Value | Type |
|----------|-------|------|
| `base_url` | `http://localhost:8080/api` | string |
| `jwt_token` | *(leave empty for now)* | string |
| `admin_token` | *(leave empty for now)* | string |
| `user_id` | `1` | number |

Save the environment and select it for testing.

---

## Part 3: Test Authentication (Essential First)

### Test 3.1: Register a New User

**Endpoint:** `POST {{base_url}}/auth/register`

**Request Body:**
```json
{
  "username": "john_volunteer",
  "email": "john@example.com",
  "password": "TestPassword123!",
  "phone": "1234567890",
  "role": "VOLUNTEER"
}
```

**Expected Response (201 Created):**
```json
{
  "message": "User registered successfully"
}
```

---

### Test 3.2: Register an Admin User

**Endpoint:** `POST {{base_url}}/auth/register`

**Request Body:**
```json
{
  "username": "admin_user",
  "email": "admin@example.com",
  "password": "AdminPassword123!",
  "phone": "9876543210",
  "role": "ADMIN"
}
```

**Expected Response (201 Created):**
```json
{
  "message": "User registered successfully"
}
```

---

### Test 3.3: Login as Volunteer

**Endpoint:** `POST {{base_url}}/auth/login`

**Request Body:**
```json
{
  "username": "john_volunteer",
  "password": "TestPassword123!"
}
```

**Expected Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huX3ZvbHVudGVlciIsImlhdCI6MTcwMTg...",
  "username": "john_volunteer",
  "email": "john@example.com",
  "role": "VOLUNTEER",
  "userId": 1
}
```

💡 **Save the token** in Postman environment variable `jwt_token`

---

### Test 3.4: Login as Admin

**Endpoint:** `POST {{base_url}}/auth/login`

**Request Body:**
```json
{
  "username": "admin_user",
  "password": "AdminPassword123!"
}
```

**Expected Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "admin_user",
  "email": "admin@example.com",
  "role": "ADMIN",
  "userId": 2
}
```

💡 **Save this token** in Postman environment variable `admin_token`

---

## Part 4: Test User Endpoints

All requests below need the `Authorization` header:
```
Authorization: Bearer {{jwt_token}}
```

### Test 4.1: Get User Profile

**Endpoint:** `GET {{base_url}}/users/1`

**Expected Response (200 OK):**
```json
{
  "userId": 1,
  "username": "john_volunteer",
  "email": "john@example.com",
  "phone": "1234567890",
  "role": "VOLUNTEER",
  "status": "ACTIVE",
  "mode": null,
  "createdAt": "2024-12-06T10:35:22.123456Z",
  "updatedAt": "2024-12-06T10:35:22.123456Z"
}
```

---

### Test 4.2: Update User Profile

**Endpoint:** `PUT {{base_url}}/users/1`

**Request Body:**
```json
{
  "email": "john.new@example.com",
  "phone": "9999999999"
}
```

**Expected Response (200 OK):**
```json
{
  "userId": 1,
  "username": "john_volunteer",
  "email": "john.new@example.com",
  "phone": "9999999999",
  "role": "VOLUNTEER",
  "status": "ACTIVE"
}
```

---

### Test 4.3: Change Password

**Endpoint:** `POST {{base_url}}/users/1/change-password`

**Request Body:**
```json
{
  "currentPassword": "TestPassword123!",
  "newPassword": "NewPassword456!"
}
```

**Expected Response (200 OK):**
```json
{
  "message": "Password changed successfully"
}
```

---

## Part 5: Test Organization Endpoints

### Test 5.1: Create Organization

**Endpoint:** `POST {{base_url}}/organizations`

**Authorization:** Bearer {{jwt_token}}

**Request Body:**
```json
{
  "organizationName": "Green Planet Foundation",
  "description": "Dedicated to environmental conservation and tree planting",
  "email": "contact@greenplanet.com",
  "phone": "5551234567",
  "address": "123 Eco Street, Nature City"
}
```

**Expected Response (201 Created):**
```json
{
  "organizationId": 1,
  "organizationName": "Green Planet Foundation",
  "description": "Dedicated to environmental conservation and tree planting",
  "email": "contact@greenplanet.com",
  "phone": "5551234567",
  "address": "123 Eco Street, Nature City",
  "ownerUserId": 1,
  "createdAt": "2024-12-06T10:40:15.123456Z",
  "updatedAt": "2024-12-06T10:40:15.123456Z"
}
```

💡 **Save the organizationId** (will use in next tests)

---

### Test 5.2: Get All Organizations

**Endpoint:** `GET {{base_url}}/organizations`

**Expected Response (200 OK):**
```json
[
  {
    "organizationId": 1,
    "organizationName": "Green Planet Foundation",
    "description": "Dedicated to environmental conservation and tree planting",
    ...
  }
]
```

---

### Test 5.3: Get Organization by ID

**Endpoint:** `GET {{base_url}}/organizations/1`

**Expected Response (200 OK):**
```json
{
  "organizationId": 1,
  "organizationName": "Green Planet Foundation",
  "description": "Dedicated to environmental conservation and tree planting",
  ...
}
```

---

### Test 5.4: Search Organizations by Name

**Endpoint:** `GET {{base_url}}/organizations/search?name=Green`

**Expected Response (200 OK):**
```json
[
  {
    "organizationId": 1,
    "ownerUserId": 1,
    "orgName": "Green Planet Foundation",
    "contactEmail": "manager@foundation.org",
    "webUrl": "https://greenplanet.org",
    "purpose": "Dedicated to environmental conservation and tree planting",
    "createdAt": "2024-12-06T10:40:15.123456Z"
  }
]
```

---

### Test 5.5: Get Organization Meetup Count

**Endpoint:** `GET {{base_url}}/organizations/1/meetup-count`

**Expected Response (200 OK):**
```json
{
  "organizationId": 1,
  "meetupCount": 2
}
```

---

## Part 6: Test Meetup Endpoints

### Test 6.1: Create Meetup

**Endpoint:** `POST {{base_url}}/meetups`

**Authorization:** Bearer {{jwt_token}}

**Request Body:**
```json
{
  "meetupName": "Beach Cleanup Drive",
  "meetupDate": "2024-12-20",
  "startTime": "09:00:00",
  "duration": "PT2H",
  "description": "Join us for a beach cleanup activity",
  "capacity": 50,
  "organizationId": 1
}
```

**Expected Response (201 Created):**
```json
{
  "meetupId": 1,
  "meetupName": "Beach Cleanup Drive",
  "meetupDate": "2024-12-20",
  "startTime": "09:00:00",
  "duration": "120",
  "description": "Join us for a beach cleanup activity",
  "hostUserId": 1,
  "organizationId": 1,
  "capacity": 50,
  "status": "SCHEDULED",
  "createdAt": "2024-12-06T10:45:30.123456Z"
}
```

---

### Test 6.2: Get All Meetups

**Endpoint:** `GET {{base_url}}/meetups`

**Expected Response (200 OK):**
```json
[
  {
    "meetupId": 1,
    "meetupName": "Beach Cleanup Drive",
    ...
  }
]
```

---

### Test 6.3: Get Meetups by Organization

**Endpoint:** `GET {{base_url}}/meetups/organization/1`

**Expected Response (200 OK):**
```json
[
  {
    "meetupId": 1,
    "meetupName": "Beach Cleanup Drive",
    "organizationId": 1,
    ...
  }
]
```

---

### Test 6.4: Get Meetups by Date Range

**Endpoint:** `GET {{base_url}}/meetups/date-range?startDate=2024-12-01&endDate=2024-12-31`

**Expected Response (200 OK):**
```json
[
  {
    "meetupId": 1,
    "meetupName": "Beach Cleanup Drive",
    "meetupDate": "2024-12-20",
    ...
  }
]
```

---

### Test 6.5: Get Meetup Participant Count

**Endpoint:** `GET {{base_url}}/meetups/1/participant-count`

**Expected Response (200 OK):**
```json
{
  "meetupId": 1,
  "participantCount": 0
}
```

---

### Test 6.6: Update Meetup

**Endpoint:** `PUT {{base_url}}/meetups/1`

**Authorization:** Bearer {{jwt_token}}

**Request Body:**
```json
{
  "meetupName": "Beach Cleanup Drive - Updated",
  "capacity": 75,
  "description": "Updated description for beach cleanup"
}
```

**Expected Response (200 OK):**
```json
{
  "meetupId": 1,
  "meetupName": "Beach Cleanup Drive - Updated",
  "capacity": 75,
  ...
}
```

---

## Part 7: Test Meetup Participant Endpoints

### Test 7.1: Join Meetup

**Endpoint:** `POST {{base_url}}/meetup-participants/{meetupId}/join/{participantId}`

**Authorization:** Bearer {{jwt_token}}

**Expected Response (201 Created):**
```json
{
  "participantId": "1-1",
  "meetupId": 1,
  "participantId": 1,
  "hoursContributed": 0,
  "status": "PENDING"
}
```

---

### Test 7.2: Get Meetup Participants

**Endpoint:** `GET {{base_url}}/meetup-participants/meetup/1`

**Expected Response (200 OK):**
```json
[
  {
    "participantId": "1-1",
    "meetupId": 1,
    "participantId": 1,
    "hoursContributed": 0,
    "status": "PENDING"
  }
]
```

---

### Test 7.3: Mark Attendance

**Endpoint:** `PUT {{base_url}}/meetup-participants/1/1/attendance`

**Authorization:** Bearer {{jwt_token}}

**Request Body:**
```json
{
  "attended": true
}
```

**Expected Response (200 OK):**
```json
{
  "participantId": "1-1",
  "meetupId": 1,
  "participantId": 1,
  "status": "ATTENDED",
  "hoursContributed": 0
}
```

---

### Test 7.4: Add Hours Contributed

**Endpoint:** `PUT {{base_url}}/meetup-participants/1/1/hours`

**Authorization:** Bearer {{jwt_token}}

**Request Body:**
```json
{
  "hours": 3.5
}
```

**Expected Response (200 OK):**
```json
{
  "participantId": "1-1",
  "meetupId": 1,
  "participantId": 1,
  "hoursContributed": 3.5,
  "status": "ATTENDED"
}
```

---

## Part 8: Test Donation Endpoints

### Test 8.1: Create Donation

**Endpoint:** `POST {{base_url}}/donations`

**Authorization:** Bearer {{jwt_token}}

**Request Body:**
```json
{
  "amountDonated": 5000,
  "currency": "PKR",
  "targetType": "ORGANIZATION",
  "targetId": 1,
  "message": "Supporting tree planting initiatives",
  "idempotencyKey": "donation-001-unique"
}
```

**Expected Response (201 Created):**
```json
{
  "donationId": 1,
  "amountDonated": 5000,
  "currency": "PKR",
  "donationDate": "2024-12-06T10:50:45.123456Z",
  "donorUserId": 1,
  "targetType": "ORGANIZATION",
  "targetId": 1,
  "status": "PENDING",
  "message": "Supporting tree planting initiatives",
  "idempotencyKey": "donation-001-unique"
}
```

---

### Test 8.2: Get All Donations

**Endpoint:** `GET {{base_url}}/donations`

**Expected Response (200 OK):**
```json
[
  {
    "donationId": 1,
    "amountDonated": 5000,
    ...
  }
]
```

---

### Test 8.3: Get Donations by Donor

**Endpoint:** `GET {{base_url}}/donations/donor/1`

**Authorization:** Bearer {{jwt_token}}

**Expected Response (200 OK):**
```json
[
  {
    "donationId": 1,
    "donorUserId": 1,
    "amountDonated": 5000,
    ...
  }
]
```

---

### Test 8.4: Approve Donation (Admin Only)

**Endpoint:** `POST {{base_url}}/donations/1/approve`

**Authorization:** Bearer {{admin_token}}

**Expected Response (200 OK):**
```json
{
  "donationId": 1,
  "status": "APPROVED",
  "amountDonated": 5000,
  ...
}
```

---

### Test 8.5: Get Total Donations for Organization

**Endpoint:** `GET {{base_url}}/donations/target/total?targetType=ORGANIZATION&targetId=1`

**Expected Response (200 OK):**
```json
{
  "targetType": "ORGANIZATION",
  "targetId": 1,
  "totalDonations": 5000,
  "totalCount": 1
}
```

---

## Part 9: Test Volunteer Profile Endpoints

### Test 9.1: Create Volunteer Profile

**Endpoint:** `POST {{base_url}}/volunteer-profiles`

**Authorization:** Bearer {{jwt_token}}

**Request Body:**
```json
{
  "bio": "Passionate about environmental conservation",
  "skills": ["Tree Planting", "Organization", "Leadership"],
  "hoursBudget": 100
}
```

**Expected Response (201 Created):**
```json
{
  "profileId": 1,
  "userId": 1,
  "bio": "Passionate about environmental conservation",
  "skills": ["Tree Planting", "Organization", "Leadership"],
  "hoursBudget": 100,
  "hoursContributed": 0,
  "rating": 0.0,
  "totalRatings": 0
}
```

---

### Test 9.2: Get Volunteer Profile

**Endpoint:** `GET {{base_url}}/volunteer-profiles/user/1`

**Expected Response (200 OK):**
```json
{
  "profileId": 1,
  "userId": 1,
  "bio": "Passionate about environmental conservation",
  "skills": ["Tree Planting", "Organization", "Leadership"],
  ...
}
```

---

### Test 9.3: Get Top Rated Volunteers

**Endpoint:** `GET {{base_url}}/volunteer-profiles/top-rated?limit=10`

**Expected Response (200 OK):**
```json
[
  {
    "profileId": 1,
    "userId": 1,
    "rating": 4.5,
    "totalRatings": 12,
    ...
  }
]
```

---

## Part 10: Test Admin Endpoints

All admin endpoints require:
```
Authorization: Bearer {{admin_token}}
```

### Test 10.1: Get All Users (Admin Only)

**Endpoint:** `GET {{base_url}}/admin/users`

**Expected Response (200 OK):**
```json
[
  {
    "userId": 1,
    "username": "john_volunteer",
    "email": "john@example.com",
    "role": "VOLUNTEER",
    "status": "ACTIVE"
  },
  {
    "userId": 2,
    "username": "admin_user",
    "email": "admin@example.com",
    "role": "ADMIN",
    "status": "ACTIVE"
  }
]
```

---

### Test 10.2: Update User Role (Admin Only)

**Endpoint:** `PUT {{base_url}}/admin/users/1/role`

**Authorization:** Bearer {{admin_token}}

**Request Body:**
```json
{
  "role": "ORG_ADMIN"
}
```

**Expected Response (200 OK):**
```json
{
  "userId": 1,
  "username": "john_volunteer",
  "role": "ORG_ADMIN",
  "status": "ACTIVE"
}
```

---

### Test 10.3: Update User Status (Admin Only)

**Endpoint:** `PUT {{base_url}}/admin/users/1/status`

**Authorization:** Bearer {{admin_token}}

**Request Body:**
```json
{
  "status": "SUSPENDED"
}
```

**Expected Response (200 OK):**
```json
{
  "userId": 1,
  "username": "john_volunteer",
  "status": "SUSPENDED"
}
```

---

### Test 10.4: Get Dashboard Statistics (Admin Only)

**Endpoint:** `GET {{base_url}}/admin/dashboard`

**Authorization:** Bearer {{admin_token}}

**Expected Response (200 OK):**
```json
{
  "totalUsers": 2,
  "usersByRole": {
    "VOLUNTEER": 0,
    "ORG_ADMIN": 1,
    "ADMIN": 1,
    "TOTAL": 2
  },
  "activeUsers": 1,
  "inactiveUsers": 0,
  "suspendedUsers": 1,
  "totalOrganizations": 1,
  "totalMeetups": 1,
  "donationStats": {
    "totalDonations": 1,
    "approvedDonations": 1,
    "pendingDonations": 0,
    "rejectedDonations": 0
  },
  "systemStatus": "OPERATIONAL"
}
```

---

### Test 10.5: Get Users by Role Statistics

**Endpoint:** `GET {{base_url}}/admin/stats/users-by-role`

**Authorization:** Bearer {{admin_token}}

**Expected Response (200 OK):**
```json
{
  "VOLUNTEER": 0,
  "ORG_ADMIN": 1,
  "ADMIN": 1,
  "TOTAL": 2
}
```

---

### Test 10.6: System Health Check

**Endpoint:** `GET {{base_url}}/admin/health`

**Authorization:** Bearer {{admin_token}}

**Expected Response (200 OK):**
```json
{
  "status": "UP",
  "timestamp": 1701907200000,
  "message": "System is running normally"
}
```

---

## Part 11: Test Notification Endpoints

### Test 11.1: Get User Notifications

**Endpoint:** `GET {{base_url}}/notifications/user/1`

**Authorization:** Bearer {{jwt_token}}

**Expected Response (200 OK):**
```json
[
  {
    "notificationId": 1,
    "userId": 1,
    "message": "You have been invited to a meetup",
    "status": "UNREAD",
    "createdAt": "2024-12-06T10:55:22.123456Z"
  }
]
```

---

### Test 11.2: Get Unread Notification Count

**Endpoint:** `GET {{base_url}}/notifications/user/1/unread-count`

**Authorization:** Bearer {{jwt_token}}

**Expected Response (200 OK):**
```json
{
  "userId": 1,
  "unreadCount": 1
}
```

---

### Test 11.3: Mark Notification as Read

**Endpoint:** `PUT {{base_url}}/notifications/1/mark-read`

**Authorization:** Bearer {{jwt_token}}

**Expected Response (200 OK):**
```json
{
  "notificationId": 1,
  "status": "READ",
  "message": "You have been invited to a meetup"
}
```

---

## Part 12: Test Activity Log Endpoints

### Test 12.1: Get User Activity Log

**Endpoint:** `GET {{base_url}}/activity-logs/user/1`

**Authorization:** Bearer {{jwt_token}}

**Expected Response (200 OK):**
```json
[
  {
    "logId": 1,
    "userId": 1,
    "action": "UPDATE",
    "entityType": "USER",
    "entityId": 1,
    "description": "Updated profile",
    "ipAddress": "127.0.0.1",
    "userAgent": "PostmanRuntime/7.x",
    "timestamp": "2024-12-06T10:50:15.123456Z"
  }
]
```

---

### Test 12.2: Get Activity Logs by Action Type

**Endpoint:** `GET {{base_url}}/activity-logs/action/UPDATE`

**Expected Response (200 OK):**
```json
[
  {
    "logId": 1,
    "action": "UPDATE",
    "entityType": "USER",
    "description": "Updated profile",
    "timestamp": "2024-12-06T10:50:15.123456Z"
  }
]
```

---

## Error Handling Tests

### Test: Invalid Credentials

**Endpoint:** `POST {{base_url}}/auth/login`

**Request Body:**
```json
{
  "username": "nonexistent_user",
  "password": "WrongPassword"
}
```

**Expected Response (401 Unauthorized):**
```json
{
  "error": "Invalid username or password"
}
```

---

### Test: Unauthorized Access

**Endpoint:** `GET {{base_url}}/admin/users`

**Authorization:** Bearer {{jwt_token}} (volunteer token, not admin)

**Expected Response (403 Forbidden):**
```json
{
  "status": "FORBIDDEN",
  "message": "Access Denied"
}
```

---

### Test: Resource Not Found

**Endpoint:** `GET {{base_url}}/users/99999`

**Expected Response (404 Not Found):**
```json
{
  "error": "User not found with ID: 99999"
}
```

---

## Troubleshooting

### Issue: "Connection refused" or "Cannot connect to localhost:8080"

**Solution:**
1. Ensure application is running: `mvn spring-boot:run`
2. Wait 5-10 seconds after seeing "Tomcat started"
3. Check that port 8080 is not in use: `netstat -ano | findstr :8080`

### Issue: "Invalid token" or "Unauthorized"

**Solution:**
1. Make sure you used the token from login response
2. Verify token hasn't expired
3. Login again to get a fresh token
4. Set correct environment variable in Postman

### Issue: Database connection error

**Solution:**
1. Check `application.yml` for correct database configuration
2. Verify PostgreSQL is running (if using PostgreSQL)
3. For H2 in-memory database, no setup needed

### Issue: "Unknown property 'jwt'" in application.yml

**Solution:**
This is just a warning. The JWT configuration is read correctly. You can ignore it or add the JWT config to application.yml as a valid property.

---

## Testing Workflow Summary

Follow this order for testing:

1. ✅ **Auth Tests** - Register and login (get tokens)
2. ✅ **User Tests** - Get/update profile, change password
3. ✅ **Organization Tests** - Create, read, search organizations
4. ✅ **Meetup Tests** - Create meetups, get meetups, update
5. ✅ **Meetup Participant Tests** - Join meetups, mark attendance, add hours
6. ✅ **Donation Tests** - Create donations, approve donations, get stats
7. ✅ **Volunteer Profile Tests** - Create profile, get stats
8. ✅ **Admin Tests** - View users, update roles, get dashboard
9. ✅ **Notification Tests** - Get notifications, mark read
10. ✅ **Activity Log Tests** - Get activity history
11. ✅ **Error Tests** - Test error scenarios

---

## Sample Postman Collection

You can import this collection template into Postman:

```json
{
  "info": {
    "name": "Plantation System API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Auth",
      "item": [
        {
          "name": "Register Volunteer",
          "request": {
            "method": "POST",
            "url": "{{base_url}}/auth/register"
          }
        },
        {
          "name": "Login",
          "request": {
            "method": "POST",
            "url": "{{base_url}}/auth/login"
          }
        }
      ]
    }
  ]
}
```

---

## Next Steps

After testing all endpoints:

1. ✅ Verify all responses match expected outputs
2. ✅ Test error scenarios (invalid inputs, unauthorized access)
3. ✅ Check database for created records
4. ✅ Review application logs for any warnings
5. ✅ Consider creating seed data for demo purposes
6. ✅ Document any custom behavior or edge cases

---

**Happy Testing! 🎉**
