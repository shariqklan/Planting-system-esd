# 🧪 Clean Postman Testing Flow - PostgreSQL Edition

**Database:** PostgreSQL (plantation_db)  
**Base URL:** `http://localhost:8080/api`  
**Date:** December 12, 2025

---

## ✅ PRE-REQUISITES

1. PostgreSQL running on localhost:5432
2. Database `plantation_db` exists with empty tables
3. Application running: `./mvnw spring-boot:run`
4. Postman ready with Authorization headers

---

## 📋 COMPLETE TEST FLOW (Follow in Order)

### PHASE 1: REGISTER USERS

#### 1.1 Register Volunteer User
**POST** `http://localhost:8080/api/auth/register`

**Body:**
```json
{
  "username": "john_volunteer",
  "email": "john@example.com",
  "password": "TestPassword123!",
  "phone": "03001234567",
  "role": "VOLUNTEER"
}
```

**Expected:** ✅ 201 Created
```json
{
  "message": "User registered successfully"
}
```

**Note:** john_volunteer will get `userId = 1`

---

#### 1.2 Register Second Volunteer
**POST** `http://localhost:8080/api/auth/register`

**Body:**
```json
{
  "username": "sara_volunteer",
  "email": "sara@example.com",
  "password": "TestPassword123!",
  "phone": "03001112222",
  "role": "VOLUNTEER"
}
```

**Expected:** ✅ 201 Created

**Note:** sara_volunteer will get `userId = 2`

---

### PHASE 2: LOGIN & GET TOKENS

#### 2.1 Login as john_volunteer
**POST** `http://localhost:8080/api/auth/login`

**Body:**
```json
{
  "username": "john_volunteer",
  "password": "TestPassword123!"
}
```

**Expected Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "john_volunteer",
  "email": "john@example.com",
  "role": "VOLUNTEER",
  "userId": 1
}
```

**⚠️ ACTION REQUIRED:**
1. Copy the `token` value
2. Save it as `john_token` (you'll use this for all john's requests)
3. Confirm `userId = 1`

---

#### 2.2 Login as sara_volunteer
**POST** `http://localhost:8080/api/auth/login`

**Body:**
```json
{
  "username": "sara_volunteer",
  "password": "TestPassword123!"
}
```

**⚠️ ACTION REQUIRED:**
1. Copy the `token` value
2. Save it as `sara_token`
3. Confirm `userId = 2`

---

### PHASE 3: CREATE VOLUNTEER PROFILES

#### 3.1 Create Profile for john_volunteer
**POST** `http://localhost:8080/api/volunteer-profiles`

**Headers:**
```
Authorization: Bearer <john_token>
Content-Type: application/json
```

**Body:**
```json
{
  "bio": "Environmental enthusiast with 5 years experience in tree planting",
  "skills": ["Tree Planting", "Community Organization", "Event Management"]
}
```

**Expected Response:**
```json
{
  "profileId": 1,
  "userId": 1,
  "bio": "Environmental enthusiast with 5 years experience in tree planting",
  "skills": ["Tree Planting", "Community Organization", "Event Management"],
  "totalHours": 0.00,
  "rating": 0.0,
  "joinedMeetupsCount": 0,
  "createdAt": "2025-12-12T...",
  "updatedAt": "2025-12-12T..."
}
```

**✅ VERIFY:**
- `profileId = 1`
- `userId = 1` (matches john_volunteer)
- `totalHours = 0.00`

---

#### 3.2 Create Profile for sara_volunteer
**POST** `http://localhost:8080/api/volunteer-profiles`

**Headers:**
```
Authorization: Bearer <sara_token>
Content-Type: application/json
```

**Body:**
```json
{
  "bio": "Passionate about environmental conservation and sustainability",
  "skills": ["Tree Planting", "Social Media", "Photography"]
}
```

**Expected:**
- `profileId = 2`
- `userId = 2` (matches sara_volunteer)

---

### PHASE 4: CREATE ORGANIZATION

#### 4.1 Create Organization (using john_volunteer)
**POST** `http://localhost:8080/api/organizations`

**Headers:**
```
Authorization: Bearer <john_token>
Content-Type: application/json
```

**Body:**
```json
{
  "orgName": "Green Earth Foundation",
  "contactEmail": "contact@greenearth.org",
  "webUrl": "https://greenearth.org",
  "purpose": "Environmental conservation and tree planting initiatives"
}
```

**Expected Response:**
```json
{
  "organizationId": 1,
  "ownerUserId": 1,
  "orgName": "Green Earth Foundation",
  "contactEmail": "contact@greenearth.org",
  "webUrl": "https://greenearth.org",
  "purpose": "Environmental conservation and tree planting initiatives",
  "createdAt": "2025-12-12T...",
  "updatedAt": "2025-12-12T..."
}
```

**✅ VERIFY:**
- `organizationId = 1`
- `ownerUserId = 1` (john is the owner)

---

### PHASE 5: CREATE MEETUP

#### 5.1 Create Meetup for Organization
**POST** `http://localhost:8080/api/meetups`

**Headers:**
```
Authorization: Bearer <john_token>
Content-Type: application/json
```

**Body:**
```json
{
  "meetupName": "Winter Tree Planting Drive 2025",
  "meetupDate": "2025-12-20",
  "startTime": "09:00:00",
  "duration": "PT4H",
  "location": "Lahore Central Park",
  "description": "Join us to plant 500 trees in the community park",
  "organizationId": 1,
  "capacity": 100
}
```

**Expected Response:**
```json
{
  "meetupId": 1,
  "hostUserId": 1,
  "organizationId": 1,
  "meetupName": "Winter Tree Planting Drive 2025",
  "meetupDate": "2025-12-20",
  "startTime": "09:00:00",
  "duration": "PT4H",
  "location": "Lahore Central Park",
  "description": "Join us to plant 500 trees in the community park",
  "capacity": 100,
  "status": "SCHEDULED",
  "createdAt": "2025-12-12T...",
  "updatedAt": "2025-12-12T..."
}
```

**✅ VERIFY:**
- `meetupId = 1`
- `hostUserId = 1` (john created it)
- `organizationId = 1`
- `status = SCHEDULED`
- `capacity = 100`

---

### PHASE 6: MEETUP PARTICIPATION

#### 6.1 John Joins His Own Meetup
**POST** `http://localhost:8080/api/meetup-participants/1/join/1`

**URL Breakdown:**
- `/1` = meetupId (Winter Tree Planting Drive)
- `/join/1` = profileId (john's volunteer profile)

**Headers:**
```
Authorization: Bearer <john_token>
```

**Expected Response:**
```json
{
  "meetupId": 1,
  "participantId": 1,
  "status": "JOINED",
  "joinedAt": "2025-12-12T...",
  "attendedAt": null,
  "hoursContributed": null,
  "note": null
}
```

**✅ VERIFY:**
- `status = JOINED`
- `participantId = 1` (john's profileId)
- `attendedAt = null` (not attended yet)

---

#### 6.2 Sara Joins the Meetup
**POST** `http://localhost:8080/api/meetup-participants/1/join/2`

**URL Breakdown:**
- `/1` = meetupId
- `/join/2` = profileId (sara's volunteer profile)

**Headers:**
```
Authorization: Bearer <sara_token>
```

**Expected:**
- `participantId = 2` (sara's profileId)
- `status = JOINED`

---

#### 6.3 Mark John's Attendance
**POST** `http://localhost:8080/api/meetup-participants/1/attendance/1`

**Headers:**
```
Authorization: Bearer <john_token>
```

**Expected:**
```json
{
  "message": "Attendance marked successfully"
}
```

**Verify:** GET `/api/meetup-participants/meetup/1` should show john with `status = ATTENDED`

---

#### 6.4 Update John's Volunteer Hours
**PATCH** `http://localhost:8080/api/meetup-participants/1/hours/1`

**Headers:**
```
Authorization: Bearer <john_token>
Content-Type: application/json
```

**Body:**
```json
{
  "hours": 4.0
}
```

**Expected:**
```json
{
  "message": "Hours updated successfully"
}
```

---

#### 6.5 Verify John's Profile Updated
**GET** `http://localhost:8080/api/volunteer-profiles/1`

**Headers:**
```
Authorization: Bearer <john_token>
```

**Expected:**
```json
{
  "profileId": 1,
  "userId": 1,
  "bio": "Environmental enthusiast with 5 years experience in tree planting",
  "skills": ["Tree Planting", "Community Organization", "Event Management"],
  "totalHours": 4.00,
  "rating": 0.0,
  "joinedMeetupsCount": 1,
  "createdAt": "...",
  "updatedAt": "..."
}
```

**✅ VERIFY:**
- `totalHours = 4.00` (updated from 0.00!)
- `joinedMeetupsCount = 1`

---

### PHASE 7: DONATIONS

#### 7.1 Create Donation for Meetup
**POST** `http://localhost:8080/api/donations`

**Headers:**
```
Authorization: Bearer <john_token>
Content-Type: application/json
```

**Body:**
```json
{
  "amountDonated": 5000,
  "currency": "PKR",
  "targetType": "MEETUP",
  "targetId": 1,
  "message": "Happy to support this great cause!",
  "idempotencyKey": "donation-john-001"
}
```

**Expected Response:**
```json
{
  "donationId": 1,
  "donorUserId": 1,
  "amountDonated": 5000.00,
  "currency": "PKR",
  "targetType": "MEETUP",
  "targetId": 1,
  "message": "Happy to support this great cause!",
  "status": "PENDING",
  "idempotencyKey": "donation-john-001",
  "createdAt": "2025-12-12T...",
  "updatedAt": "2025-12-12T..."
}
```

**✅ VERIFY:**
- `donationId = 1`
- `donorUserId = 1` (john donated)
- `status = PENDING`
- `targetType = MEETUP`, `targetId = 1`

---

#### 7.2 Approve Donation
**POST** `http://localhost:8080/api/donations/1/approve`

**Headers:**
```
Authorization: Bearer <john_token>
```

**Expected:**
```json
{
  "message": "Donation approved successfully"
}
```

**Verify:** GET `/api/donations/1` should show `status = COMPLETED`

---

#### 7.3 Get Total Donations for Meetup
**GET** `http://localhost:8080/api/donations/total/1/MEETUP`

**Headers:**
```
Authorization: Bearer <john_token>
```

**Expected:**
```json
{
  "totalAmount": 5000.00,
  "currency": "PKR",
  "donationCount": 1
}
```

---

### PHASE 8: NOTIFICATIONS

#### 8.1 Create Notification for John
**POST** `http://localhost:8080/api/notifications?userId=1`

**Headers:**
```
Authorization: Bearer <john_token>
Content-Type: application/json
```

**Body:**
```json
{
  "type": "MEETUP",
  "channel": "IN_APP",
  "subject": "Meetup Reminder",
  "body": "Your meetup 'Winter Tree Planting Drive 2025' starts tomorrow!",
  "payload": "{}",
  "status": "PENDING"
}
```

**Expected:**
```json
{
  "notificationId": 1,
  "userId": 1,
  "type": "MEETUP",
  "channel": "IN_APP",
  "subject": "Meetup Reminder",
  "body": "Your meetup 'Winter Tree Planting Drive 2025' starts tomorrow!",
  "status": "PENDING",
  "createdAt": "2025-12-12T..."
}
```

---

#### 8.2 Get John's Unread Notifications
**GET** `http://localhost:8080/api/notifications/user/1/unread`

**Headers:**
```
Authorization: Bearer <john_token>
```

**Expected:** Array with 1 notification (status = PENDING)

---

#### 8.3 Mark Notification as Read
**POST** `http://localhost:8080/api/notifications/1/read`

**Headers:**
```
Authorization: Bearer <john_token>
```

**Expected:**
```json
{
  "message": "Notification marked as read"
}
```

---

### PHASE 9: ACTIVITY LOGS

#### 9.1 View All Activity Logs
**GET** `http://localhost:8080/api/activity-logs`

**Headers:**
```
Authorization: Bearer <john_token>
```

**Expected:** Array of all actions performed (USER_CREATED, ORG_CREATED, MEETUP_CREATED, etc.)

---

#### 9.2 View John's Activity Only
**GET** `http://localhost:8080/api/activity-logs/user/1`

**Headers:**
```
Authorization: Bearer <john_token>
```

**Expected:** Array of john's actions only

---

#### 9.3 View Activity by Action Type
**GET** `http://localhost:8080/api/activity-logs/action/CREATE`

**Headers:**
```
Authorization: Bearer <john_token>
```

**Expected:** All CREATE actions

---

## 🔍 VERIFICATION CHECKLIST

After completing all phases, verify in PostgreSQL:

```sql
-- Connect to database
psql -U postgres -d plantation_db

-- Check all tables have data
SELECT 'users' AS table_name, COUNT(*) FROM users
UNION ALL SELECT 'volunteer_profiles', COUNT(*) FROM volunteer_profiles
UNION ALL SELECT 'organizations', COUNT(*) FROM organizations
UNION ALL SELECT 'meetups', COUNT(*) FROM meetups
UNION ALL SELECT 'meetup_participants', COUNT(*) FROM meetup_participants
UNION ALL SELECT 'donations', COUNT(*) FROM donations
UNION ALL SELECT 'notifications', COUNT(*) FROM notifications
UNION ALL SELECT 'activity_logs', COUNT(*) FROM activity_logs;
```

**Expected Counts:**
- users: 2 (john, sara)
- volunteer_profiles: 2
- organizations: 1
- meetups: 1
- meetup_participants: 2 (john, sara both joined)
- donations: 1
- notifications: 1
- activity_logs: 10+ (all actions logged)

---

## 📊 KEY MAPPINGS TO REMEMBER

| Entity | User | ID Mapping |
|--------|------|------------|
| john_volunteer | User Account | userId = 1 |
| john_volunteer | Volunteer Profile | profileId = 1 |
| sara_volunteer | User Account | userId = 2 |
| sara_volunteer | Volunteer Profile | profileId = 2 |
| Green Earth Foundation | Organization | organizationId = 1 |
| Winter Tree Planting | Meetup | meetupId = 1 |

**Critical Understanding:**
- **userId ≠ profileId** (separate tables, separate IDs)
- Meetup participation uses **profileId**, not userId
- JWT token authenticates the **user**, but actions reference **profile**

---

## 🎓 VIVA PREPARATION POINTS

### 1. Authentication Flow
**Q:** "How does JWT authentication work?"  
**A:** "User logs in with username/password → Backend validates credentials → BCrypt verifies password hash → JWT token generated with userId, role, email → Token expires after 24 hours → Client includes token in Authorization header for all requests → JwtAuthenticationFilter validates token before processing request."

---

### 2. Profile vs User Separation
**Q:** "Why separate user and volunteer_profile tables?"  
**A:** "Users table handles authentication and account management—anyone can be a user. Volunteer_profiles table handles volunteer-specific data like skills, hours, ratings. This separation follows Single Responsibility Principle: not all users are volunteers (some might only donate), and it allows scaling to different user types without bloating the users table."

---

### 3. Composite Keys
**Q:** "What's the primary key for meetup_participants?"  
**A:** "Composite key: (meetupId, participantId). This ensures one volunteer can only join a specific meetup once. The @EmbeddedId annotation with MeetupParticipantId class implements this in JPA."

---

### 4. Status Lifecycles
**Q:** "Explain meetup participant status flow."  
**A:** "JOINED → user registered for meetup. ATTENDED → user physically attended (marked by organizer/admin). This distinction prevents inflated metrics—only attended participants count toward volunteer hours and impact reports. The attendedAt timestamp proves actual participation."

---

### 5. Idempotency
**Q:** "What prevents duplicate donations?"  
**A:** "The idempotencyKey field. If a user accidentally submits the same donation twice (network retry, double-click), the backend checks if that key already exists. If yes, returns 409 Conflict. This prevents duplicate charges and ensures data integrity."

---

### 6. PostgreSQL vs H2
**Q:** "Why switch from H2 to PostgreSQL?"  
**A:** "H2 is in-memory, data lost on restart—good for development only. PostgreSQL is production-grade: data persists, supports concurrent connections, better performance under load, enterprise-ready for deployment. Our application.yml has dev profile (H2) and prod profile (PostgreSQL) for different environments."

---

### 7. Hours Aggregation
**Q:** "How are total volunteer hours calculated?"  
**A:** "When PATCH /meetup-participants/{meetupId}/hours/{profileId} is called, the service updates two places: (1) meetup_participants.hoursContributed for that specific meetup, (2) volunteer_profiles.totalHours adds those hours to cumulative total. This enables ranking volunteers by activity level."

---

### 8. Role-Based Access
**Q:** "How do you enforce permissions?"  
**A:** "Spring Security's SecurityFilterChain. JWT token contains user's role (VOLUNTEER, ADMIN, ORG_ADMIN). @PreAuthorize annotations on controller methods check roles. Example: only organization owner or ADMIN can update meetups. JwtAuthenticationFilter validates token and loads user authority before request reaches controller."

---

## 🚀 YOU'RE READY!

This flow tests:
- ✅ Authentication (register, login, JWT)
- ✅ Authorization (role-based access)
- ✅ CRUD operations (all entities)
- ✅ Business logic (status transitions, hours aggregation)
- ✅ Data persistence (PostgreSQL)
- ✅ Audit trail (activity logs)
- ✅ Relationships (user → profile → participation → hours)

**Time to evaluation:** You now understand the complete system flow! 🎉

---

---

# 🧪 EXTENDED TESTING FLOW - Advanced Endpoints

**These are BONUS tests for comprehensive API coverage. Run after completing the main flow.**

---

## PHASE 10: USER MANAGEMENT (Advanced)

### 10.1 Get User by ID
**GET** `http://localhost:8080/api/users/1`

**Headers:**
```
Authorization: Bearer <john_token>
```

**Expected:** 200 OK with user details

---

### 10.2 Get All Users (ADMIN ONLY)
**GET** `http://localhost:8080/api/users`

**Headers:**
```
Authorization: Bearer <john_token>
```

**Expected:** 403 Forbidden (john is VOLUNTEER, not ADMIN)

---

### 10.3 Update User Profile
**PUT** `http://localhost:8080/api/users/1`

**Headers:**
```
Authorization: Bearer <john_token>
Content-Type: application/json
```

**Body:**
```json
{
  "email": "john_updated@example.com",
  "phone": "03009999999"
}
```

**Expected:** 200 OK with updated user

---

### 10.4 Change Password
**POST** `http://localhost:8080/api/users/1/change-password`

**Headers:**
```
Authorization: Bearer <john_token>
Content-Type: application/json
```

**Body:**
```json
{
  "oldPassword": "TestPassword123!",
  "newPassword": "NewPassword456!"
}
```

**Expected:** 200 OK

---

### 10.5 Check Email Exists
**GET** `http://localhost:8080/api/users/check/email/john_updated@example.com`

**Expected:** 200 OK with `{ "exists": true }`

---

### 10.6 Check Username Exists
**GET** `http://localhost:8080/api/users/check/username/sara_volunteer`

**Expected:** 200 OK with `{ "exists": true }`

---

### 10.7 Get User by Username
**GET** `http://localhost:8080/api/users/username/john_volunteer`

**Expected:** 200 OK with user details

---

## PHASE 11: ORGANIZATION MANAGEMENT (Advanced)

### 11.1 Get All Organizations
**GET** `http://localhost:8080/api/organizations`

**Expected:** 200 OK with array of organizations

---

### 11.2 Get Organizations by Owner
**GET** `http://localhost:8080/api/organizations/owner/1`

**Expected:** 200 OK - returns organizations owned by john

---

### 11.3 Search Organizations by Name
**GET** `http://localhost:8080/api/organizations/search?name=Green`

**Expected:** 200 OK - returns matching organizations

---

### 11.4 Get Meetup Count for Organization
**GET** `http://localhost:8080/api/organizations/1/meetup-count`

**Expected:** 200 OK with `{ "count": 1 }`

---

### 11.5 Update Organization
**PUT** `http://localhost:8080/api/organizations/1`

**Headers:**
```
Authorization: Bearer <john_token>
Content-Type: application/json
```

**Body:**
```json
{
  "orgName": "Green Earth Foundation - Updated",
  "purpose": "Updated purpose"
}
```

**Expected:** 200 OK with updated organization

---

### 11.6 Delete Organization
**DELETE** `http://localhost:8080/api/organizations/1`

**Headers:**
```
Authorization: Bearer <john_token>
```

**Expected:** 200 OK with success message

---

## PHASE 12: MEETUP MANAGEMENT (Advanced)

### 12.1 Get All Meetups
**GET** `http://localhost:8080/api/meetups`

**Expected:** 200 OK with array of meetups

---

### 12.2 Get Meetups by Organization
**GET** `http://localhost:8080/api/meetups/organization/1`

**Expected:** 200 OK - returns meetups for organization 1

---

### 12.3 Get Meetups by Host User
**GET** `http://localhost:8080/api/meetups/host/1`

**Expected:** 200 OK - returns meetups hosted by john

---

### 12.4 Get Meetups by Status
**GET** `http://localhost:8080/api/meetups/status/SCHEDULED`

**Expected:** 200 OK - returns scheduled meetups

---

### 12.5 Get Upcoming Meetups for Organization
**GET** `http://localhost:8080/api/meetups/upcoming/1`

**Expected:** 200 OK - returns future meetups

---

### 12.6 Get Meetups by Date Range
**GET** `http://localhost:8080/api/meetups/date-range?startDate=2025-12-01&endDate=2025-12-31`

**Expected:** 200 OK - returns meetups in date range

---

### 12.7 Get Participant Count for Meetup
**GET** `http://localhost:8080/api/meetups/1/participant-count`

**Expected:** 200 OK with `{ "count": 2 }`

---

### 12.8 Update Meetup
**PUT** `http://localhost:8080/api/meetups/1`

**Headers:**
```
Authorization: Bearer <john_token>
Content-Type: application/json
```

**Body:**
```json
{
  "meetupName": "Winter Tree Planting Drive - Extended",
  "capacity": 150
}
```

**Expected:** 200 OK with updated meetup

---

### 12.9 Update Meetup Status
**PATCH** `http://localhost:8080/api/meetups/1/status?status=COMPLETED`

**Headers:**
```
Authorization: Bearer <john_token>
```

**Expected:** 200 OK - status changed to COMPLETED

---

### 12.10 Delete Meetup
**DELETE** `http://localhost:8080/api/meetups/1`

**Headers:**
```
Authorization: Bearer <john_token>
```

**Expected:** 200 OK

---

## PHASE 13: MEETUP PARTICIPATION (Advanced)

### 13.1 Get All Participants for a Meetup
**GET** `http://localhost:8080/api/meetup-participants/meetup/1`

**Expected:** 200 OK with array of participants

---

### 13.2 Get All Meetups for a Participant
**GET** `http://localhost:8080/api/meetup-participants/participant/1`

**Expected:** 200 OK with meetups john joined

---

### 13.3 Mark Attendance for Participant
**POST** `http://localhost:8080/api/meetup-participants/1/attendance/1`

**Headers:**
```
Authorization: Bearer <john_token>
```

**Expected:** 200 OK

---

### 13.4 Add Note to Participation
**POST** `http://localhost:8080/api/meetup-participants/1/note/1`

**Headers:**
```
Authorization: Bearer <john_token>
Content-Type: application/json
```

**Body:**
```json
{
  "note": "Excellent participation and dedication!"
}
```

**Expected:** 200 OK

---

### 13.5 Get List of Attendees
**GET** `http://localhost:8080/api/meetup-participants/1/attendees`

**Expected:** 200 OK with attendees list

---

### 13.6 Get Attendance Count
**GET** `http://localhost:8080/api/meetup-participants/1/attendance-count`

**Expected:** 200 OK with `{ "count": 1 }`

---

### 13.7 Check if User Joined Meetup
**GET** `http://localhost:8080/api/meetup-participants/1/is-joined/1`

**Expected:** 200 OK with `{ "joined": true }`

---

### 13.8 Leave Meetup
**DELETE** `http://localhost:8080/api/meetup-participants/1/leave/1`

**Headers:**
```
Authorization: Bearer <john_token>
```

**Expected:** 200 OK - john removed from meetup

---

## PHASE 14: VOLUNTEER PROFILE MANAGEMENT (Advanced)

### 14.1 Get All Volunteer Profiles
**GET** `http://localhost:8080/api/volunteer-profiles`

**Expected:** 200 OK with array of profiles

---

### 14.2 Get Volunteer Profile by User ID
**GET** `http://localhost:8080/api/volunteer-profiles/user/1`

**Expected:** 200 OK with john's profile

---

### 14.3 Update Volunteer Profile
**PUT** `http://localhost:8080/api/volunteer-profiles/1`

**Headers:**
```
Authorization: Bearer <john_token>
Content-Type: application/json
```

**Body:**
```json
{
  "bio": "Updated bio - 10 years experience now"
}
```

**Expected:** 200 OK with updated profile

---

### 14.4 Add Skill to Profile
**POST** `http://localhost:8080/api/volunteer-profiles/1/skills`

**Headers:**
```
Authorization: Bearer <john_token>
Content-Type: application/json
```

**Body:**
```json
{
  "skill": "Leadership"
}
```

**Expected:** 200 OK

---

### 14.5 Remove Skill from Profile
**DELETE** `http://localhost:8080/api/volunteer-profiles/1/skills`

**Headers:**
```
Authorization: Bearer <john_token>
Content-Type: application/json
```

**Body:**
```json
{
  "skill": "Photography"
}
```

**Expected:** 200 OK

---

### 14.6 Update Total Volunteer Hours
**PATCH** `http://localhost:8080/api/volunteer-profiles/1/hours`

**Headers:**
```
Authorization: Bearer <john_token>
Content-Type: application/json
```

**Body:**
```json
{
  "hours": 10.5
}
```

**Expected:** 200 OK

---

### 14.7 Update Volunteer Rating
**PATCH** `http://localhost:8080/api/volunteer-profiles/1/rating`

**Headers:**
```
Authorization: Bearer <john_token>
Content-Type: application/json
```

**Body:**
```json
{
  "rating": 4.5
}
```

**Expected:** 200 OK

---

### 14.8 Get Top Rated Volunteers
**GET** `http://localhost:8080/api/volunteer-profiles/top-rated?minRating=4.0`

**Expected:** 200 OK with volunteers rated 4.0+

---

### 14.9 Get Most Active Volunteers
**GET** `http://localhost:8080/api/volunteer-profiles/most-active`

**Expected:** 200 OK sorted by hours contributed

---

### 14.10 Delete Volunteer Profile
**DELETE** `http://localhost:8080/api/volunteer-profiles/1`

**Headers:**
```
Authorization: Bearer <john_token>
```

**Expected:** 200 OK

---

## PHASE 15: DONATION MANAGEMENT (Advanced)

### 15.1 Get All Donations
**GET** `http://localhost:8080/api/donations`

**Expected:** 200 OK with array of donations

---

### 15.2 Get Donations by Donor
**GET** `http://localhost:8080/api/donations/donor/1`

**Expected:** 200 OK - john's donations

---

### 15.3 Get Donations by Status
**GET** `http://localhost:8080/api/donations/status/COMPLETED`

**Expected:** 200 OK - completed donations only

---

### 15.4 Get Donations for Target
**GET** `http://localhost:8080/api/donations/target/1/MEETUP`

**Expected:** 200 OK - donations for meetup 1

---

### 15.5 Get Total Donations for Target
**GET** `http://localhost:8080/api/donations/total/1/MEETUP`

**Expected:** 200 OK with `{ "totalAmount": 5000.00, "currency": "PKR", "donationCount": 1 }`

---

### 15.6 Reject Donation with Reason
**POST** `http://localhost:8080/api/donations/1/reject`

**Headers:**
```
Authorization: Bearer <john_token>
Content-Type: application/json
```

**Body:**
```json
{
  "reason": "Cannot accept due to policy violation"
}
```

**Expected:** 200 OK

---

### 15.7 Cancel Donation
**POST** `http://localhost:8080/api/donations/1/cancel`

**Expected:** 200 OK

---

### 15.8 Update Donation
**PUT** `http://localhost:8080/api/donations/1`

**Headers:**
```
Authorization: Bearer <john_token>
Content-Type: application/json
```

**Body:**
```json
{
  "message": "Updated message"
}
```

**Expected:** 200 OK

---

### 15.9 Delete Donation
**DELETE** `http://localhost:8080/api/donations/1`

**Expected:** 200 OK

---

### 15.10 Test Idempotency (Duplicate Donation)
**POST** `http://localhost:8080/api/donations`

**Headers:**
```
Authorization: Bearer <john_token>
Content-Type: application/json
```

**Body:**
```json
{
  "amountDonated": 3000,
  "currency": "PKR",
  "targetType": "MEETUP",
  "targetId": 1,
  "message": "Duplicate donation",
  "idempotencyKey": "donation-john-001"
}
```

**Expected:** 409 Conflict - duplicate idempotency key detected

---

## PHASE 16: NOTIFICATION MANAGEMENT (Advanced)

### 16.1 Get All Notifications
**GET** `http://localhost:8080/api/notifications`

**Expected:** 200 OK with array of notifications

---

### 16.2 Get Notifications for User
**GET** `http://localhost:8080/api/notifications/user/1`

**Expected:** 200 OK with john's notifications

---

### 16.3 Get Unread Count
**GET** `http://localhost:8080/api/notifications/user/1/unread-count`

**Expected:** 200 OK with `{ "unreadCount": 0 }` (marked as read earlier)

---

### 16.4 Mark Notification as Delivered
**POST** `http://localhost:8080/api/notifications/1/delivered`

**Expected:** 200 OK

---

### 16.5 Create Multiple Notifications
**POST** `http://localhost:8080/api/notifications?userId=1`

**Headers:**
```
Authorization: Bearer <john_token>
Content-Type: application/json
```

**Body:**
```json
{
  "type": "DONATION",
  "channel": "EMAIL",
  "subject": "Donation Received",
  "body": "Thank you for your donation!",
  "payload": "{}",
  "status": "PENDING"
}
```

**Expected:** 201 Created

---

## PHASE 17: ACTIVITY LOGGING & AUDIT TRAIL

### 17.1 Get Activity by Entity Type
**GET** `http://localhost:8080/api/activity-logs/entity/MEETUP/1`

**Expected:** 200 OK - all actions on meetup 1

---

### 17.2 Get Activity by User and Action Type
**GET** `http://localhost:8080/api/activity-logs/action/UPDATE`

**Expected:** 200 OK - all UPDATE actions

---

### 17.3 Verify Complete Audit Trail
Query PostgreSQL:
```sql
SELECT * FROM activity_logs ORDER BY created_at DESC LIMIT 20;
```

**Expected:** All operations logged with timestamps

---

## PHASE 18: ERROR SCENARIOS & EDGE CASES

### 18.1 Unauthorized Access (No Token)
**GET** `http://localhost:8080/api/users/1`

**Expected:** 401 Unauthorized

---

### 18.2 Invalid Token Format
**GET** `http://localhost:8080/api/users/1`

**Headers:**
```
Authorization: Bearer invalid_token_xxx
```

**Expected:** 401 Unauthorized

---

### 18.3 Resource Not Found
**GET** `http://localhost:8080/api/users/99999`

**Expected:** 404 Not Found

---

### 18.4 Forbidden Access (Role Check)
**GET** `http://localhost:8080/api/users` (requires ADMIN)

**Headers:**
```
Authorization: Bearer <john_token>
```

**Expected:** 403 Forbidden

---

### 18.5 Validation Error (Missing Required Field)
**POST** `http://localhost:8080/api/auth/register`

**Body:**
```json
{
  "username": "test_user"
}
```

**Expected:** 400 Bad Request - missing fields

---

### 18.6 Duplicate Resource
**POST** `http://localhost:8080/api/auth/register`

**Body:**
```json
{
  "username": "john_volunteer",
  "email": "duplicate@example.com",
  "password": "Password123!",
  "phone": "03001234567",
  "role": "VOLUNTEER"
}
```

**Expected:** 400 Conflict - username already exists

---

### 18.7 Invalid Meetup Status
**PATCH** `http://localhost:8080/api/meetups/1/status?status=INVALID_STATUS`

**Headers:**
```
Authorization: Bearer <john_token>
```

**Expected:** 400 Bad Request

---

### 18.8 Join Non-existent Meetup
**POST** `http://localhost:8080/api/meetup-participants/99999/join/1`

**Headers:**
```
Authorization: Bearer <john_token>
```

**Expected:** 404 Not Found

---

### 18.9 Profile Already Exists
**POST** `http://localhost:8080/api/volunteer-profiles`

**Headers:**
```
Authorization: Bearer <john_token>
Content-Type: application/json
```

**Body:**
```json
{
  "bio": "Another profile",
  "skills": ["Test"]
}
```

**Expected:** 400 Bad Request - profile already exists

---

### 18.10 Unauthorized Organization Update
**PUT** `http://localhost:8080/api/organizations/1`

**Headers:**
```
Authorization: Bearer <sara_token>
Content-Type: application/json
```

**Body:**
```json
{
  "orgName": "Hacked Name"
}
```

**Expected:** 403 Forbidden - sara is not owner

---

## 🎓 ADVANCED VIVA TALKING POINTS

### Database Relationships
**Q:** "Explain the database relationships in your system."  
**A:** "We have 11 tables with foreign key relationships:
- users ← organizations (owner)
- users ← volunteer_profiles (one-to-one)
- organizations ← meetups (one-to-many)
- users ← meetups (host)
- meetups ← meetup_participants (composite FK)
- volunteer_profiles ← meetup_participants (FK)
- users ← donations (donor)
- meetups ← donations (target)
- organizations ← donations (target)
- users ← notifications (one-to-many)
- users ← activity_logs (one-to-many)
These relationships enforce data integrity through cascading updates/deletes."

---

### Transaction Management
**Q:** "How do you handle concurrent operations?"  
**A:** "We use Spring's @Transactional annotation to ensure ACID properties. When updating volunteer hours, we fetch the current value, increment it, and persist atomically. HikariCP manages connection pooling (10 max, 2 min idle) to handle concurrent requests safely. Optimistic locking via @Version field prevents race conditions on critical updates."

---

### API Security
**Q:** "What security measures protect your API?"  
**A:** "Multi-layered approach: (1) JWT authentication with 7-day expiration, (2) BCrypt password hashing (10 rounds), (3) @PreAuthorize role-based access control on sensitive endpoints, (4) input validation with @Valid on all DTOs, (5) CORS enabled only for specified origins, (6) Activity logging captures all modifications for audit trail."

---

### Performance Optimization
**Q:** "How does your system scale?"  
**A:** "We use pagination on list endpoints (implied), proper indexing on frequently queried fields (userId, meetupId), query optimization with JPA projections for read-only operations, connection pooling to reuse database connections, and stateless JWT auth eliminates session storage overhead."

---

### Error Handling & Resilience
**Q:** "How do you handle errors?"  
**A:** "Centralized exception handling through @ControllerAdvice (implicit in HTTP status codes), graceful degradation when services fail, retry logic on idempotent operations (donations), proper HTTP status codes (404 for missing, 409 for conflicts, 400 for validation), and detailed error messages for debugging."

---

## 📊 FINAL VERIFICATION

Run this PostgreSQL query for comprehensive stats:

```sql
SELECT 
  'Users' AS category, COUNT(*) AS count FROM users
UNION ALL SELECT 'Organizations', COUNT(*) FROM organizations
UNION ALL SELECT 'Meetups', COUNT(*) FROM meetups
UNION ALL SELECT 'Participants', COUNT(*) FROM meetup_participants
UNION ALL SELECT 'Donations', COUNT(*) FROM donations
UNION ALL SELECT 'Volunteers', COUNT(*) FROM volunteer_profiles
UNION ALL SELECT 'Notifications', COUNT(*) FROM notifications
UNION ALL SELECT 'Audit Logs', COUNT(*) FROM activity_logs
ORDER BY count DESC;
```

**This completes comprehensive API testing! 🎉**

---

---

# 👨‍💼 ADMIN TESTING FLOW - Restricted Admin Access

**IMPORTANT:** These endpoints require ADMIN role. Regular VOLUNTEER users will get 403 Forbidden.

---

## SETUP: Create Admin User

### Create Admin Account
**POST** `http://localhost:8080/api/auth/register`

**Body:**
```json
{
  "username": "admin_user",
  "email": "admin@plantation.com",
  "password": "AdminPassword123!",
  "phone": "03005555555",
  "role": "ADMIN"
}
```

**Expected:** 201 Created

---

### Login as Admin
**POST** `http://localhost:8080/api/auth/login`

**Body:**
```json
{
  "username": "admin_user",
  "password": "AdminPassword123!"
}
```

**Expected Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "admin_user",
  "email": "admin@plantation.com",
  "role": "ADMIN",
  "userId": 3
}
```

**⚠️ ACTION REQUIRED:**
1. Copy the `token` value
2. Save it as `admin_token`
3. Confirm `role = ADMIN`

---

## ADMIN ENDPOINTS

### ADMIN 1: Get All Users
**GET** `http://localhost:8080/api/admin/users`

**Headers:**
```
Authorization: Bearer <admin_token>
```

**Expected:** 200 OK with array of all users (john, sara, admin)

---

### ADMIN 2: Get Specific User
**GET** `http://localhost:8080/api/admin/users/1`

**Headers:**
```
Authorization: Bearer <admin_token>
```

**Expected:** 200 OK with user details (john_volunteer)

---

### ADMIN 3: Update User Role
**PUT** `http://localhost:8080/api/admin/users/2/role`

**Headers:**
```
Authorization: Bearer <admin_token>
Content-Type: application/json
```

**Body:**
```json
{
  "role": "ORG_ADMIN"
}
```

**Expected:** 200 OK - sara's role changed to ORG_ADMIN

**Verification:** GET `/api/admin/users/2` should show role as ORG_ADMIN

---

### ADMIN 4: Update User Status
**PUT** `http://localhost:8080/api/admin/users/1/status`

**Headers:**
```
Authorization: Bearer <admin_token>
Content-Type: application/json
```

**Body:**
```json
{
  "status": "INACTIVE"
}
```

**Expected:** 200 OK - john's status changed to INACTIVE

---

### ADMIN 5: Delete User
**DELETE** `http://localhost:8080/api/admin/users/1`

**Headers:**
```
Authorization: Bearer <admin_token>
```

**Expected:** 200 OK with message "User deleted successfully"

**Note:** This cascades deletes all related data (profiles, organizations, etc.)

---

### ADMIN 6: Get Total User Count
**GET** `http://localhost:8080/api/admin/stats/user-count`

**Headers:**
```
Authorization: Bearer <admin_token>
```

**Expected:** 200 OK
```json
{
  "totalUsers": 3
}
```

---

### ADMIN 7: Get Users by Role
**GET** `http://localhost:8080/api/admin/stats/users-by-role`

**Headers:**
```
Authorization: Bearer <admin_token>
```

**Expected:** 200 OK
```json
{
  "VOLUNTEER": 2,
  "ADMIN": 1,
  "ORG_ADMIN": 1
}
```

---

### ADMIN 8: Get Total Organization Count
**GET** `http://localhost:8080/api/admin/stats/organization-count`

**Headers:**
```
Authorization: Bearer <admin_token>
```

**Expected:** 200 OK
```json
{
  "totalOrganizations": 1
}
```

---

### ADMIN 9: Get Total Meetup Count
**GET** `http://localhost:8080/api/admin/stats/meetup-count`

**Headers:**
```
Authorization: Bearer <admin_token>
```

**Expected:** 200 OK
```json
{
  "totalMeetups": 1
}
```

---

### ADMIN 10: Get Donation Statistics
**GET** `http://localhost:8080/api/admin/stats/donation-stats`

**Headers:**
```
Authorization: Bearer <admin_token>
```

**Expected:** 200 OK with donation metrics
```json
{
  "totalDonations": 1,
  "totalAmount": 5000.00,
  "completedDonations": 1,
  "pendingDonations": 0,
  "rejectedDonations": 0,
  "averageDonation": 5000.00
}
```

---

### ADMIN 11: Get Dashboard Stats
**GET** `http://localhost:8080/api/admin/dashboard`

**Headers:**
```
Authorization: Bearer <admin_token>
```

**Expected:** 200 OK with complete system overview
```json
{
  "totalUsers": 3,
  "totalVolunteers": 2,
  "totalOrganizations": 1,
  "totalMeetups": 1,
  "totalDonations": 1,
  "usersByRole": { "VOLUNTEER": 2, "ADMIN": 1, "ORG_ADMIN": 1 },
  "meetupStats": { "total": 1, "completed": 0, "scheduled": 1 },
  "donationStats": { "total": 5000.00, "completed": 1 }
}
```

---

### ADMIN 12: System Health Check
**GET** `http://localhost:8080/api/admin/health`

**Headers:**
```
Authorization: Bearer <admin_token>
```

**Expected:** 200 OK
```json
{
  "status": "UP",
  "timestamp": 1734591234567,
  "message": "System is running normally"
}
```

---

## 🚫 ADMIN PERMISSION TESTS (Should Fail)

### Test 1: Volunteer Accessing Admin Endpoints
**GET** `http://localhost:8080/api/admin/users`

**Headers:**
```
Authorization: Bearer <john_token>
```

**Expected:** 403 Forbidden - john is VOLUNTEER, not ADMIN

---

### Test 2: Volunteer Getting User Stats
**GET** `http://localhost:8080/api/admin/stats/user-count`

**Headers:**
```
Authorization: Bearer <john_token>
```

**Expected:** 403 Forbidden

---

### Test 3: No Token Access
**GET** `http://localhost:8080/api/admin/users`

**Expected:** 401 Unauthorized

---

## 🎓 ADMIN VIVA TALKING POINTS

### Role-Based Authorization
**Q:** "How do you ensure only admins access admin endpoints?"  
**A:** "Spring Security @PreAuthorize('hasRole(\"ADMIN\")') on the class level. Every admin endpoint checks JWT token contains ADMIN role. If volunteer tries to access /api/admin/*, SecurityFilterChain blocks with 403 Forbidden before method is even called. This is enforced at controller level via annotation."

---

### Admin Capabilities
**Q:** "What can admins do that regular users cannot?"  
**A:** "Admins can: (1) View all users in system, (2) Change any user's role (VOLUNTEER → ORG_ADMIN), (3) Deactivate/reactivate user accounts, (4) Delete any user (cascades deletes), (5) View comprehensive system statistics (users, orgs, meetups, donations), (6) Access dashboard with all KPIs, (7) Monitor system health. Regular volunteers can only manage their own data."

---

### Audit Trail for Admin Actions
**Q:** "How do you track admin modifications?"  
**A:** "Every admin action (role change, user delete, status update) calls activityLogService.logActivity() to log the action with admin userId, action type, entity affected, and details. This creates immutable audit trail in activity_logs table. Query: SELECT * FROM activity_logs WHERE user_id=3 AND action LIKE 'UPDATE%' shows all admin modifications."

---

### Statistics & Dashboard
**Q:** "What metrics does the admin dashboard show?"  
**A:** "Complete system overview: total users by role, total organizations, total meetups with status breakdown, total donations with amount aggregation, volunteer profile count, notification delivery stats, audit log entries. This enables business intelligence—admins can identify trends, top volunteers, most popular organizations, donation trends over time."

---

## 📊 ADMIN FINAL VERIFICATION

After completing admin tests, run this query:

```sql
SELECT u.user_id, u.username, u.role, u.created_at
FROM users u
ORDER BY u.created_at DESC;
```

**Expected:** Shows admin_user (userId=3, role=ADMIN) created after john and sara

---

```sql
SELECT al.action, al.entity_type, COUNT(*) as count
FROM activity_logs al
WHERE al.user_id = 3
GROUP BY al.action, al.entity_type
ORDER BY count DESC;
```

**Expected:** All admin actions logged (UPDATE_ROLE, UPDATE_STATUS, DELETE, etc.)

---

**You now have complete system understanding—from volunteer workflows to admin governance! 🎉**

