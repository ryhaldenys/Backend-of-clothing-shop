# Backend-for-staff-application-simplified-version

<ul>
  <li> A Java Backend project
  <li> User login credentials: (user@gmail.com/useruser) 
  <li> Admin login credentials: (admin@gmail.com/adminadmin)
</ul>

<hr>

## About project

This project is the backend of the clothing store where users can log in or register and get data in JSON format. The user can have ADMIN, USER, or unauthorized user role in this project.

<hr>
An unauthorized user can:
<ul> 
  <li> log in
  <li> register
  <li> get all clothes
  <li> get all clothes by id
</ul>
<hr>
Simple user can:
<ul> 
  <li> get information about your account
  <li> update your account
 <li> delete your account
 <li> get the address of the post office
 </ul>

<ul> 
 <li> get the contents of the basket
 <li> add clothes to the basket
 <li> delete chosen clothes from the basket
 <li> update amount of chosen clothes in the basket
 <li> add person bonuses to the basket
 <li> delete person bonuses from the basket
</ul>

<ul> 
 <li> get your orders
 <li> get your order by id
 <li> create order
 <li> cancel order 
</ul>
<hr>

Admin can:
<ul>
  <li> do all that user can do
  <li> get all users
  <li> save clothes
  <li> delete clothes  
  <li> add images and sizes to clothes 
  <li> delete images and sizes from clothes
  <li> get all orders
  <li> get  an order by id
  <li> set status received to order
</ul>


Examples of HTTP methods can see in: <a href="https://github.com/ryhaldenys/Backend-for-staff-application-simplified-version/blob/master/examples-of-http-methods.http">http methods examples</a>
<br>
## Software And Tools Required
<ul>
  <li> IntelliJ IDEA [Ultimate]
  <li> Java [JDK 17+] 
  <li> Postgres SQL
</ul>

## Dummy Database Initialization
STEP 1: Download PostgreSQL if it does not already exist on your laptop <br>
STEP 2: Open PostgreSQL command prompt or pgAdmin <br>
STEP 3: Enter or create a password, then if you use pgAdmin, open script console <br> 
STEP 4: Copy paste PostgreSQL commands from <a href="https://github.com/ryhaldenys/Backend-for-staff-application-simplified-version/blob/master/script.sql">Sql script</a> and run <br>
<br>

##  Importing and Running The Project Through IntelliJ IDEA
STEP 0: Download JDK 17 + , if not already installed <br>
STEP 1: Open IntelliJ IDEA. [Install, if not already installed.] <br>
STEP 2: Click on Get from VSC > Paste The Repository Url as: <br><code>https://github.com/ryhaldenys/Backend-for-staff-application-simplified-version.git</code> and enter Clone <br>
STEP 3: Go inside src/main/resources > application-dev.properties and update the value of database details as per your usage, like <code>spring.datasource.url </code>, <code>spring.datasource.password</code> , and <code>spring.datasource.username</code> according to your installed PostgreSQL admin user credentials. <br>
STEP 4: Go inside src/main/java/ua.staff then open StaffApplication class and run the project.<br>
STEP 5: Check running the site at http://localhost:8083/api/clothes <br>
STEP 6: Default email, password, and id for admin is admin@gmail.com, adminadmin, and 2 <br>
STEP 7: Default email, password, and id for admin is user@gmail.com, useruser and 1 

