# Backend-for-staff-application-simplified-version

<ul>
  <li> A Java Backend project
  <li> User Login Credentials: (user@gmail.com/useruser) 
  <li> Admin Login Credentials: (admin@gmail.com/adminadmin)
</ul>

<hr>

## About project

This project is backend of clothing store where users can log in or register and get data in JSON format.
In this project user can have ADMIN, USER or unauthorized user.

<hr>
Unauthorized user can:
<ul> 
  <li> log in
  <li> register
  <li> get all clothes
  <li> get all clothes by id
</ul>
<hr>
Simple user can:
<ul> 
  <li> get information about your own account
  <li> update your account
  <li> delete your account
  <li> get address of post office
</ul>

<ul> 
  <li> get the contents of basket
  <li> add clothes to basket
  <li> delete chose clothes from basket
  <li> update amount of chose clothes in basket 
  <li> add person bonuses to basket
  <li> delete person bonuses from basket 
  
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
  <li> get order by id
  <li> set status received to order
</ul>


Examples of http methods you can see in: <a href="https://github.com/ryhaldenys/Backend-for-staff-application-simplified-version/blob/master/examples-of-http-methods.http">http methods examples</a>
<br>
## Software And Tools Required
<ul>
  <li> IntelliJ IDEA [Ultimate]
  <li> Java [JDK 17+] 
  <li> Postgres SQL
</ul>

## Dummy Database Initialization
STEP 1: Download PostgreSql if it is not already installed on your laptop <br>
STEP 2: Open PostgreSql Command Prompt or pgAdmin <br>
STEP 3: Enter or create password, than, if you use pgAdmin, open script cosole <br> 
STEP 4: Copy paste PostgreSql Commands from <a href="https://github.com/ryhaldenys/Backend-for-staff-application-simplified-version/blob/master/script.sql">Sql script</a> and run <br>
<br>

##  Importing and Running The Project Through IntelliJ IDEA
STEP 0: Download JDK 17 + , if not already installed <br>
STEP 1: Open IntelliJ IDEA. [Install, if not already installed.] <br>
STEP 2: Click on Get from VSC > Paste The Repository Url as: <br><code>https://github.com/ryhaldenys/Backend-for-staff-application-simplified-version.git</code> and enter Clone <br>
STEP 3: Go inside src/main/resources > application-dev.properties and update the value of database details as per your usage, like spring.datasource.url,spring.datasource.password and spring.datasource.username according to your installed postgresql admin user credentials. <br>
STEP 4: Go inside src/main/java/ua.staff than open StaffApplication class and run project.<br>
STEP 5: Check Running The Site At http://localhost:8083/api/clothes <br>
STEP 6: Default Email, Password And Id For Admin Is admin@gmail.com, adminadmin And 2
STEP 6: Default Email, Password And Id For Admin Is user@gmail.com, useruser And 1 


