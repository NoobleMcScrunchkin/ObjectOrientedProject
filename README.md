# BS2202: Object Oriented Software Development Summative

## Installation & Configuration
To begin using this Library application you must have a `.env` file in the same directory as the `.jar` file that is being run. 

This is an example of a configuration file:
```.env
DB_HOST=localhost
DB_NAME=library
DB_USER=username
DB_PASS=password
```

Upon running the application it will connect to the database and automatically create the tables required for usage.

The default admin user has the username `Admin` and the password `Password01`

After this the user should be able to add products, loans and verify users, assuming they are authorized to do so.