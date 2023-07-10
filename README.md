# L2CEcommerceBe

This is the backend of the E-commerce application from the [Full Stack: Angular and Java Spring Boot](https://www.udemy.com/course/full-stack-angular-spring-boot-tutorial/)
course on [Udemy](https://www.udemy.com/). 

It is a full stack E-commerce application with:
* login/logout 
* product catalog
* shopping cart
* checkout
* card payments
* JWT, OAuth2, OpenID Connect and SSL/TLS


The companion frontend is available at [L2CeCommerceFe](https://github.com/ibuttimer/L2CeCommerceFe).

## Tech stack
The application consists of:
* [Spring Boot](https://spring.io/projects/spring-boot) application with
  * [Spring Data JPA](https://spring.io/projects/spring-data-jpa) providing REST APIs
  * [Stripe](https://stripe.com/) for card payment processing
  * JWT, oauth2, OpenID Connect provided by [okta](https://www.okta.com/)
* Database supported:
  * [MySQL](https://www.mysql.com/)
  * [PostgreSQL](https://www.postgresql.org/)
* [Angular](https://angular.io/) frontend application

## Development

### Database

#### MySQL

Run the following scripts in the listed order in the MySQL Workbench:
* [01-create-user.sql](starter-files/db-scripts/mysql/01-create-user.sql) 

  Create the database user.

* [02-create-products.sql](starter-files/db-scripts/mysql/02-create-products.sql)

  Create the database schema and add sample data.


#### PostgreSQL

Run the following scripts in the listed order in the MySQL Workbench:
* [02-create-products.sql](starter-files/db-scripts/mysql/02-create-products.sql)

  Create the database schema and add sample data.

##### psql

Use the PostgreSQL `psql` tool to run the script 

```shell
$ psql -h <db_host> -d <db_database> -U <db_username> -f <path to sql file>
```

##### heroku-postgresql

If utilising a [Heroku Postgres](https://devcenter.heroku.com/articles/heroku-postgresql) datastore, the script may be run using the [heroku pg:psql command](https://devcenter.heroku.com/articles/heroku-cli-commands#heroku-pg-psql-database) of the [Heroku CLI](https://devcenter.heroku.com/articles/heroku-cli).

```shell
$ heroku pg:psql <db_database> --app <application> -f <path to sql file>
```


### Backend

Clone or download this repository.

#### Generate key and self-signed certificate

If enabling HTTPS support, follow the procedure outlined at https://github.com/darbyluv2code/fullstack-angular-and-springboot/blob/master/bonus-content/secure-https-communication/keytool-steps.md to generate key and self-signed certificate.

````shell
keytool -genkeypair -alias luv2code -keystore src/main/resources/luv2code-keystore.p12 -keypass secret -storeType PKCS12 -storepass secret -keyalg RSA -keysize 2048 -validity 365 -dname "C=IE, ST=Dublin, L=Dublin, O=luv2code, OU=Training Backend, CN=localhost" -ext "SAN=dns:localhost"
````

#### Backend okta application

Register for an okta developer account if necessary and create an okta application with the following parameters:

- *Sign-in method*

  OIDC - OpenID Connect

- *Application type*

  Web Application


#### Environment configuration

Set the following environmental variables:

#### Server configuration

| Variable    | Description                                                                                                                                                                                                                  | Comment |
|-------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------|
| PORT        | Server web port                                                                                                                                                                                                              |         |
| LOG_LEVEL   | Logging level; one of ``ERROR``, ``WARN``, ``INFO``, ``DEBUG``, or ``TRACE``. Default ``INFO``.                                                                                                                              |         |
| SHOW_SQL    | Set to ``true`` to view sql output in console, otherwise ``false``. Default ``false``.                                                                                                                                       |         |
| SSL_ENABLED | Set to ``true`` to enable HTTPS support, otherwise ``false``. Default ``true``. <br/> **Note:** If HTTPS support is enabled, the [SSL configuration](#ssl-configuration) environmental variables also need to be configured. |         |


#### Database configuration

| Variable    | Description                                                                  | Comment |
|-------------|------------------------------------------------------------------------------|---------|
| DB_CFG      | Set to the database configuration required; one of``postgres`` or ``mysql``. |         |
| DB_HOST     | Set to database server address.                                              | [1]     |
| DB_PORT     | Set to database server port.                                                 | [1]     |
| DB_DATABASE | Set to database name.                                                        | [1]     |
| DB_SCHEMA   | Set to database schema.                                                      |         |
| DB_USERNAME | Set to database username.                                                    | [2]     |
| DB_PASSWORD | Set to database password.                                                    | [2]     |

If utilising an external Database As A Service (DBaaS), the variable values may be extracted from the database URL;
e.g. `postgres://<db_username>:<db_password>@<db_host>:<db_port>/<db_database>`

**[1]** In a Heroku deployment using an attached Heroku Postgres database, the official Heroku buildpacks for Java will 
automatically create the SPRING_DATASOURCE_URL environment variable, so this variable will be ignored.

**[2]** In a Heroku deployment using an attached Heroku Postgres database, the official Heroku buildpacks for Java will 
automatically create SPRING_DATASOURCE_USERNAME, and SPRING_DATASOURCE_PASSWORD environment variables.
This variable should be set to the appropriate SPRING_DATASOURCE_* variable.


#### SSL configuration

**Note:** These setting are only required if HTTPS support is enabled, see [Server configuration](#server-configuration).

| Variable          | Description                                                                                                                                                                                                                                                             | Comment |
|-------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------|
| KEY_ALIAS         | Alias of the key generated in [Generate key and self-signed certificate](#generate-key-and-self-signed-certificate).                                                                                                                                                    |         |
| KEYSTORE_PASSWORD | Password for keystore created in [Generate key and self-signed certificate](#generate-key-and-self-signed-certificate).                                                                                                                                                 |         |
| KEYSTORE          | Location in the classpath of the PKCS12 keystore created in [Generate key and self-signed certificate](#generate-key-and-self-signed-certificate).<br/> E.g. for the keystore *backend/spring-ecommerce/src/main/resources/keystore.p12*, set ``KEYSTORE=keystore.p12`` |         |


#### Okta configuration

| Variable           | Description                                                                                                                                                                                                                                                                                                                                                                       | Comment |
|--------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------|
| OKTA_CLIENT_ID     | *Client ID* from the *Client Credentials* of the [Backend okta application](#backend-okta-application) in the okta dashboard.                                                                                                                                                                                                                                                     |         |
| OKTA_CLIENT_SECRET | *Client secret* from the *Client Credentials* of the [Backend okta application](#backend-okta-application) in the okta dashboard.                                                                                                                                                                                                                                                 |         |
| OKTA_ISSUER        | Set to the [default authorization server](https://developer.okta.com/docs/reference/api/oidc/#_2-okta-as-the-identity-platform-for-your-app-or-api) for the okta developer account.<br/> Using *Okta domain* from the *General Settings* of the [Backend okta application](#backend-okta-application) in the okta dashboard, set to ``https://${yourOktaDomain}/oauth2/default``. |         |


#### CORS configuration

| Variable        | Description                                                                                                                                                                | Comment |
|-----------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------|
| ALLOWED_ORIGINS | A ``;`` separated list of allows origins for client requests.<br/> E.g. ``https://localhost:4200`` will allow connections from a client running on local host, port 4200.  |         |


#### Stripe configuration

| Variable          | Description                                                                                                                                                                                   | Comment |
|-------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------|
| STRIPE_KEY_SECRET | *Secret key* from the *API keys* in the Stripe developer dashboard.<br/> E.g. to use the *Test Mode Standard keys* goto https://dashboard.stripe.com/test/apikeys and copy the *Secret key*.  |         |


#### Client configuration

| Variable      | Description                                                                                              | Comment |
|---------------|----------------------------------------------------------------------------------------------------------|---------|
| SAMPLE_CLIENT | Url of a sample client instance to redirect to via root page, e.g. `https://l2cecommercefe.onrender.com` |         |

## Operations

Using the default settings, the application will be hosted at https://localhost:8443/api

### API documentation

Swagger UI: https://localhost:8443/swagger-ui/index.html

