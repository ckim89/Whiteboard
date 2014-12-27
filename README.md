Fall 2014 Databases Final project:

WHITEBOARD ===============================================================
By:
Elaine Chao
Daniel Kim (Chang Bum Kim)

What we envisioned:
We wanted to create something like Blackboard (an educational tool used by many
institutions, along with Johns Hopkins, to input grades and provide feedback to
students). We decided to call our project Whiteboard. 

We used Java Swing for the GUI and MySQL for our database. (of course, depending
on the users, the database and information will be different). Because we needed
to test, we have added information on classes and fake students. This is all in
the loading.sql file. 

HOW TO USE:
We chose to use Java because we did not necessarily want a web app. However, this
creates issues when running on other machines. So, here is a list of
instructions to run our code/GUI.

1) Because we are not using the JHU CS database (the JHU ugrad machines do not support
java connection to the database), we created our own local database. To do this
on an independent machine, first create a new connection called local_db to
mysql and under that connection create a database called "Whiteboard". In that
Database, use the provided sql file (loading.sql) to fill all the tables needed
for the GUI.

2) Because we are using Java, we need to incorporate a driver that allows us to
create connections to mysql through Java. This driver can be found on the
internet via: http://dev.mysql.com/downloads/connector/j/ Once the driver has
been downloaded, you must take the JAR file called:
mysql-connector-java-5.0.8-bin.jar and include it within the build path. 

3) You should now be able to run the Driver file after you have compiled. In
case this installation does not work on your machine, please note that we have
screenshots of every functionality of our GUI, which uses "Whiteboard" a
database we made on a local connection. These screenshots can be found in the
"screenshot" directory. 

our functionalities include: login view grades (for students only) change grades
(for instructors only) change password (for both instructors and students)

all of these things are found in screenshots as well in the screenshot
directory.

if you have any questions, feel free to ask at ckim89@jhu.edu

