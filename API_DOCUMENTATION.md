# Plantation System - REST API Documentation

## Overview
Complete REST API for the Plantation System with support for user management, organizations, meetup activities, donations, and volunteer profiles.

---

## Base URL
```
http://localhost:8080/api
```

---

## Authentication
- **Type:** JWT (JSON Web Tokens)
- **Header:** `Authorization: Bearer <token>`
- **Token obtained from:** `/auth/login` endpoint

---

## Endpoints

### 1. Authentication Endpoints (`/auth`)

#### Register User
```
POST /auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "phone": "03001234567",
  "password": "SecurePass123",
  "role": "VOLUNTEER"  // VOLUNTEER, ORG_ADMIN, ADMIN
}

Response: 201 Created
{
  "message": "User registered successfully"
}
```

#### Login
```
POST /auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "SecurePass123"
}

Response: 200 OK
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "john_doe",
  "email": "john@example.com",
  "role": "VOLUNTEER",
  "userId": 1
}
```

---

### 2. User Endpoints (`/users`)

#### Get User by ID
```
GET /users/{userId}
Authorization: Bearer <token>

Response: 200 OK
{
  "userId": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "phone": "03001234567",
  "mode": "SIMPLE",
  "role": "VOLUNTEER",
  "status": "ACTIVE",
  "createdAt": "2025-12-06T10:30:00Z",
  "updatedAt": "2025-12-06T10:30:00Z"
}
```

#### Get User by Username
```
GET /users/username/{username}
Authorization: Bearer <token>
```

#### Get All Users
```
GET /users
Authorization: Bearer <token>

Response: 200 OK
[
  { user object 1 },
  { user object 2 }
]
```

#### Update User Profile
```
PUT /users/{userId}
Authorization: Bearer <token>
Content-Type: application/json

{
  "email": "newemail@example.com",
  "phone": "03009876543",
  "mode": "SIMPLE",
  "status": "ACTIVE"
}

Response: 200 OK
{ updated user object }
```

#### Change Password
```
POST /users/{userId}/change-password
Authorization: Bearer <token>
Content-Type: application/json

{
  "currentPassword": "OldPass123",
  "newPassword": "NewPass456"
}

Response: 200 OK
{
  "message": "Password changed successfully"
}
```

#### Delete User
```
DELETE /users/{userId}
Authorization: Bearer <token>

Response: 200 OK
{
  "message": "User deleted successfully"
}
```

#### Check Email Exists
```
GET /users/check/email/{email}

Response: 200 OK
{
  "email": "john@example.com",
  "exists": true
}
```

---

### 3. Organization Endpoints (`/organizations`)

#### Create Organization
```
POST /organizations
Authorization: Bearer <token>
Content-Type: application/json

{
  "orgName": "Green Earth Foundation",
  "contactEmail": "contact@greenerath.org",
  "webUrl": "https://greenerath.org",
  "purpose": "Environmental conservation and tree planting"
}

Response: 201 Created
{
  "organizationId": 1,
  "ownerUserId": 1,
  "orgName": "Green Earth Foundation",
  "contactEmail": "contact@greenerath.org",
  "webUrl": "https://greenerath.org",
  "purpose": "Environmental conservation and tree planting",
  "createdAt": "2025-12-06T10:30:00Z"
}
```

#### Get Organization by ID
```
GET /organizations/{organizationId}

Response: 200 OK
{ organization object }
```

#### Get All Organizations
```
GET /organizations

Response: 200 OK
[ organization objects ]
```

#### Get Organizations by Owner
```
GET /organizations/owner/{userId}

Response: 200 OK
[ organization objects owned by user ]
```

#### Search Organizations
```
GET /organizations/search?name=Green

Response: 200 OK
[ matching organization objects ]
```

#### Update Organization
```
PUT /organizations/{organizationId}
Authorization: Bearer <token>
Content-Type: application/json

{
  "orgName": "Updated Name",
  "contactEmail": "newemail@org.com",
  "webUrl": "https://newurl.com",
  "purpose": "Updated purpose"
}

Response: 200 OK
{ updated organization object }
```

#### Delete Organization
```
DELETE /organizations/{organizationId}
Authorization: Bearer <token>

Response: 200 OK
{
  "message": "Organization deleted successfully"
}
```

#### Get Organization Meetup Count
```
GET /organizations/{organizationId}/meetup-count

Response: 200 OK
{
  "organizationId": 1,
  "meetupCount": 5
}
```

---

### 4. Meetup Endpoints (`/meetups`)

#### Create Meetup
```
POST /meetups
Authorization: Bearer <token>
Content-Type: application/json

{
  "meetupName": "Summer Tree Planting Drive",
  "meetupDate": "2025-12-15",
  "startTime": "09:00:00",
  "duration": "PT3H",  // ISO 8601 format or minutes
  "description": "Plant 500 trees in the park",
  "organizationId": 1,
  "capacity": 100
}

Response: 201 Created
{ created meetup object }
```

#### Get Meetup by ID
```
GET /meetups/{meetupId}

Response: 200 OK
{ meetup object }
```

#### Get All Meetups
```
GET /meetups

Response: 200 OK
[ meetup objects ]
```

#### Get Meetups by Organization
```
GET /meetups/organization/{organizationId}

Response: 200 OK
[ meetup objects for organization ]
```

#### Get Meetups by Host
```
GET /meetups/host/{hostUserId}

Response: 200 OK
[ meetup objects hosted by user ]
```

#### Get Meetups by Status
```
GET /meetups/status/{status}  // SCHEDULED, COMPLETED, CANCELLED

Response: 200 OK
[ meetup objects with given status ]
```

#### Get Upcoming Meetups
```
GET /meetups/upcoming/{organizationId}

Response: 200 OK
[ upcoming scheduled meetup objects ]
```

#### Get Meetups by Date Range
```
GET /meetups/date-range?startDate=2025-12-01&endDate=2025-12-31

Response: 200 OK
[ meetup objects in date range ]
```

#### Update Meetup
```
PUT /meetups/{meetupId}
Authorization: Bearer <token>
Content-Type: application/json

{
  "meetupName": "Updated name",
  "description": "Updated description",
  "capacity": 150,
  "status": "SCHEDULED"
}

Response: 200 OK
{ updated meetup object }
```

#### Delete Meetup
```
DELETE /meetups/{meetupId}
Authorization: Bearer <token>

Response: 200 OK
{
  "message": "Meetup deleted successfully"
}
```

#### Update Meetup Status
```
PATCH /meetups/{meetupId}/status?status=COMPLETED
Authorization: Bearer <token>

Response: 200 OK
{
  "message": "Meetup status updated successfully"
}
```

#### Get Participant Count
```
GET /meetups/{meetupId}/participant-count

Response: 200 OK
{
  "meetupId": 1,
  "participantCount": 25
}
```

---

### 5. Meetup Participant Endpoints (`/meetup-participants`)

#### Join Meetup
```
POST /meetup-participants/{meetupId}/join/{participantId}
Authorization: Bearer <token>

Response: 201 Created
{
  "meetupId": 1,
  "participantId": 5,
  "status": "JOINED",
  "joinedAt": "2025-12-06T10:30:00Z"
}
```

#### Leave Meetup
```
DELETE /meetup-participants/{meetupId}/leave/{participantId}
Authorization: Bearer <token>

Response: 200 OK
{
  "message": "Left meetup successfully"
}
```

#### Get Participants by Meetup
```
GET /meetup-participants/meetup/{meetupId}

Response: 200 OK
[ participant objects ]
```

#### Get Meetups by Participant
```
GET /meetup-participants/participant/{participantId}

Response: 200 OK
[ meetup objects ]
```

#### Mark Attendance
```
POST /meetup-participants/{meetupId}/attendance/{participantId}
Authorization: Bearer <token>

Response: 200 OK
{
  "message": "Attendance marked successfully"
}
```

#### Update Hours Contributed
```
PATCH /meetup-participants/{meetupId}/hours/{participantId}
Authorization: Bearer <token>
Content-Type: application/json

{
  "hours": 3.5
}

Response: 200 OK
{
  "message": "Hours updated successfully"
}
```

#### Add Note
```
POST /meetup-participants/{meetupId}/note/{participantId}
Authorization: Bearer <token>
Content-Type: application/json

{
  "note": "Great volunteer, very helpful"
}

Response: 200 OK
{
  "message": "Note added successfully"
}
```

#### Get Attendees
```
GET /meetup-participants/{meetupId}/attendees

Response: 200 OK
[ attended participants ]
```

#### Get Attendance Count
```
GET /meetup-participants/{meetupId}/attendance-count

Response: 200 OK
{
  "meetupId": 1,
  "attendanceCount": 20
}
```

#### Check if User Joined
```
GET /meetup-participants/{meetupId}/is-joined/{participantId}

Response: 200 OK
{
  "meetupId": 1,
  "participantId": 5,
  "isJoined": true
}
```

---

### 6. Donation Endpoints (`/donations`)

#### Create Donation
```
POST /donations
Authorization: Bearer <token>
Content-Type: application/json

{
  "amountDonated": 5000,
  "currency": "PKR",
  "targetType": "MEETUP",  // MEETUP or ORGANIZATION
  "targetId": 1,
  "message": "Happy to contribute",
  "idempotencyKey": "unique-key-123"
}

Response: 201 Created
{
  "donationId": 1,
  "amountDonated": 5000,
  "currency": "PKR",
  "donationDate": "2025-12-06T10:30:00Z",
  "donorUserId": 1,
  "targetType": "MEETUP",
  "targetId": 1,
  "status": "PENDING",
  "message": "Happy to contribute",
  "idempotencyKey": "unique-key-123"
}
```

#### Get Donation by ID
```
GET /donations/{donationId}

Response: 200 OK
{ donation object }
```

#### Get All Donations
```
GET /donations

Response: 200 OK
[ donation objects ]
```

#### Get Donations by Donor
```
GET /donations/donor/{donorUserId}

Response: 200 OK
[ donation objects by donor ]
```

#### Get Donations by Status
```
GET /donations/status/{status}  // PENDING, COMPLETED, REJECTED, CANCELLED

Response: 200 OK
[ donation objects with status ]
```

#### Get Donations by Target
```
GET /donations/target/{targetId}/{targetType}

Response: 200 OK
[ donation objects for target ]
```

#### Get Total Donations
```
GET /donations/total/{targetId}/{targetType}

Response: 200 OK
{
  "targetId": 1,
  "targetType": "MEETUP",
  "total": 50000,
  "count": 10
}
```

#### Approve Donation
```
POST /donations/{donationId}/approve
Authorization: Bearer <token>

Response: 200 OK
{
  "message": "Donation approved successfully"
}
```

#### Reject Donation
```
POST /donations/{donationId}/reject
Authorization: Bearer <token>
Content-Type: application/json

{
  "reason": "Insufficient funds"
}

Response: 200 OK
{
  "message": "Donation rejected successfully"
}
```

#### Cancel Donation
```
POST /donations/{donationId}/cancel
Authorization: Bearer <token>

Response: 200 OK
{
  "message": "Donation cancelled successfully"
}
```

#### Update Donation
```
PUT /donations/{donationId}
Authorization: Bearer <token>
Content-Type: application/json

{
  "message": "Updated message"
}

Response: 200 OK
{ updated donation object }
```

#### Delete Donation
```
DELETE /donations/{donationId}
Authorization: Bearer <token>

Response: 200 OK
{
  "message": "Donation deleted successfully"
}
```

---

### 7. Volunteer Profile Endpoints (`/volunteer-profiles`)

#### Create Volunteer Profile
```
POST /volunteer-profiles
Authorization: Bearer <token>
Content-Type: application/json

{
  "bio": "Environmental enthusiast with 5 years experience",
  "skills": ["Tree Planting", "Landscaping", "Organization"]
}

Response: 201 Created
{
  "profileId": 1,
  "userId": 1,
  "bio": "Environmental enthusiast with 5 years experience",
  "skills": ["Tree Planting", "Landscaping", "Organization"],
  "totalHours": 0,
  "rating": 0,
  "joinedMeetupsCount": 0,
  "createdAt": "2025-12-06T10:30:00Z",
  "updatedAt": "2025-12-06T10:30:00Z"
}
```

#### Get Volunteer Profile by ID
```
GET /volunteer-profiles/{profileId}

Response: 200 OK
{ volunteer profile object }
```

#### Get Profile by User ID
```
GET /volunteer-profiles/user/{userId}

Response: 200 OK
{ volunteer profile object }
```

#### Get All Volunteer Profiles
```
GET /volunteer-profiles

Response: 200 OK
[ volunteer profile objects ]
```

#### Update Volunteer Profile
```
PUT /volunteer-profiles/{profileId}
Authorization: Bearer <token>
Content-Type: application/json

{
  "bio": "Updated bio",
  "skills": ["Skill1", "Skill2"]
}

Response: 200 OK
{ updated profile object }
```

#### Add Skill
```
POST /volunteer-profiles/{profileId}/skills
Authorization: Bearer <token>
Content-Type: application/json

{
  "skill": "Event Management"
}

Response: 200 OK
{
  "message": "Skill added successfully"
}
```

#### Remove Skill
```
DELETE /volunteer-profiles/{profileId}/skills
Authorization: Bearer <token>
Content-Type: application/json

{
  "skill": "Event Management"
}

Response: 200 OK
{
  "message": "Skill removed successfully"
}
```

#### Update Total Hours
```
PATCH /volunteer-profiles/{profileId}/hours
Authorization: Bearer <token>
Content-Type: application/json

{
  "hours": 5.5
}

Response: 200 OK
{
  "message": "Hours updated successfully"
}
```

#### Update Rating
```
PATCH /volunteer-profiles/{profileId}/rating
Authorization: Bearer <token>
Content-Type: application/json

{
  "rating": 4.5
}

Response: 200 OK
{
  "message": "Rating updated successfully"
}
```

#### Get Top Rated Volunteers
```
GET /volunteer-profiles/top-rated?minRating=4.0

Response: 200 OK
[ volunteer profile objects with rating >= 4.0 ]
```

#### Get Most Active Volunteers
```
GET /volunteer-profiles/most-active

Response: 200 OK
[ volunteer profile objects sorted by activity ]
```

#### Delete Volunteer Profile
```
DELETE /volunteer-profiles/{profileId}
Authorization: Bearer <token>

Response: 200 OK
{
  "message": "Volunteer profile deleted successfully"
}
```

---

### 8. Notification Endpoints (`/notifications`)

#### Create Notification
```
POST /notifications?userId=1
Authorization: Bearer <token>
Content-Type: application/json

{
  "type": "MEETUP",
  "channel": "IN_APP",
  "subject": "New Meetup Available",
  "body": "A new plantation meetup has been scheduled",
  "payload": "{}",
  "status": "PENDING"
}

Response: 201 Created
{ notification object }
```

#### Get Notification by ID
```
GET /notifications/{notificationId}

Response: 200 OK
{ notification object }
```

#### Get Notifications by User
```
GET /notifications/user/{userId}

Response: 200 OK
[ notification objects ]
```

#### Get Unread Notifications
```
GET /notifications/user/{userId}/unread

Response: 200 OK
[ unread notification objects ]
```

#### Get Unread Count
```
GET /notifications/user/{userId}/unread-count

Response: 200 OK
{
  "userId": 1,
  "unreadCount": 3
}
```

#### Mark as Read
```
POST /notifications/{notificationId}/read
Authorization: Bearer <token>

Response: 200 OK
{
  "message": "Notification marked as read"
}
```

#### Mark as Delivered
```
POST /notifications/{notificationId}/delivered
Authorization: Bearer <token>

Response: 200 OK
{
  "message": "Notification marked as delivered"
}
```

#### Delete Notification
```
DELETE /notifications/{notificationId}
Authorization: Bearer <token>

Response: 200 OK
{
  "message": "Notification deleted successfully"
}
```

---

### 9. Activity Log Endpoints (`/activity-logs`)

#### Get User Activity Log
```
GET /activity-logs/user/{userId}

Response: 200 OK
[ activity log objects ]
```

#### Get Activity by Action Type
```
GET /activity-logs/action/{actionType}  // CREATE, UPDATE, DELETE, JOIN, etc.

Response: 200 OK
[ activity log objects ]
```

#### Get Activity by Entity
```
GET /activity-logs/entity/{entityType}/{entityId}

Response: 200 OK
[ activity log objects for entity ]
```

#### Get All Activity Logs
```
GET /activity-logs

Response: 200 OK
[ all activity log objects ]
```

---

## Error Responses

### Common Error Codes

#### 400 Bad Request
```json
{
  "error": "Invalid request parameters or validation failed"
}
```

#### 401 Unauthorized
```json
{
  "error": "Invalid or missing authentication token"
}
```

#### 403 Forbidden
```json
{
  "error": "You don't have permission to access this resource"
}
```

#### 404 Not Found
```json
{
  "error": "Resource not found"
}
```

#### 409 Conflict
```json
{
  "error": "Resource already exists or operation conflicts"
}
```

#### 500 Internal Server Error
```json
{
  "error": "Internal server error occurred"
}
```

---

## Data Types

### Common Fields
- **userId**: Long
- **organizationId**: Long
- **meetupId**: Long
- **profileId**: Long
- **donationId**: Long
- **notificationId**: Long
- **logId**: Long

### Enums
- **Role**: VOLUNTEER, ORG_ADMIN, ADMIN
- **GenericStatus**: ACTIVE, INACTIVE, SUSPENDED
- **Mode**: SIMPLE (or others)
- **DonationTargetType**: MEETUP, ORGANIZATION
- **DonationStatus**: PENDING, COMPLETED, REJECTED, CANCELLED
- **MeetupStatus**: SCHEDULED, COMPLETED, CANCELLED

---

## Notes
- All timestamps are in ISO 8601 format (UTC)
- All monetary amounts are in the specified currency (default PKR)
- Pagination not shown in documentation but can be added as needed
- CORS is enabled for all origins
- All endpoints except `/auth/**` require valid JWT token
