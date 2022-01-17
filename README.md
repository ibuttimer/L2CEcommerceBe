# L2CEcommerceBe

This is the backend of the E-commerce application from the [Full Stack: Angular and Java Spring Boot](https://www.udemy.com/course/full-stack-angular-spring-boot-tutorial/)
course on [Udemy](https://www.udemy.com/). 

It is a full stack E-commerce application with:
* login/logout 
* product catalog
* shopping cart
* checkout
* Stripe card payments
* JWT, OAuth2, OpenID Connect and SSL/TLS


The companion frontend is available at [L2CeCommerceFe](https://github.com/ibuttimer/L2CeCommerceFe).

## Tech stack
The application consists of:
* [Spring Boot](https://spring.io/projects/spring-boot) application with
  * [Spring Data JPA](https://spring.io/projects/spring-data-jpa) providing REST APIs
  * [Stripe Credit Card Payments](https://stripe.com/) for card payment processing
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


### Backend

Clone or download this repository.

#### Generate key and self-signed certificate

If enabling HTTPS support, follow the procedure outlined at https://github.com/darbyluv2code/fullstack-angular-and-springboot/blob/master/bonus-content/secure-https-communication/keytool-steps.md to generate key and self-signed certificate.

#### Backend okta application

Register for an okta developer account if necessary and create an okta application with the following parameters:

- *Sign-in method*

  OIDC - OpenID Connect

- *Application type*

  Web Application


#### Environment configuration

Set the following environmental variables:

#### Server configuration

- PORT

  Server web port.


- LOG_LEVEL

  Logging level; one of ``ERROR``, ``WARN``, ``INFO``, ``DEBUG``, or ``TRACE``.


- SHOW_SQL

  Set to ``true`` to view sql output in console, otherwise ``false``.


- SSL_ENABLED

  Set to ``true`` to enable HTTPS support, otherwise ``false``.

  **Note:** If HTTPS support is enabled, the [SSL configuration](#ssl-configuration) environmental variables also need to be configured.


#### Database configuration

- DB_CFG

  Set to the database configuration required; one of``postgres`` or ``mysql``. 


- DB_HOST

  Set to database server address.


- DB_PORT

  Set to database server port.


- DB_DATABASE

  Set to database name.


- DB_USERNAME

  Set to database username.


- DB_PASSWORD

  Set to database password.


- DB_SCHEMA

  Set to database schema.


#### SSL configuration

**Note:** These setting are only required if HTTPS support is enabled, see [Server configuration](#server-configuration).

- KEY_ALIAS

  Alias of the key generated in [Generate key and self-signed certificate](#generate-key-and-self-signed-certificate)     

* KEYSTORE_PASSWORD
  
  Password for keystore created in [Generate key and self-signed certificate](#generate-key-and-self-signed-certificate)


* KEYSTORE

  Location in the classpath of the PKCS12 keystore created in [Generate key and self-signed certificate](#generate-key-and-self-signed-certificate)

  E.g. for the keystore *backend/spring-ecommerce/src/main/resources/keystore.p12*, set

  ``
  KEYSTORE=keystore.p12
  ``

#### Okta configuration

* OKTA_CLIENT_ID

  *Client ID* from the *Client Credentials* of the [Backend okta application](#backend-okta-application) in the okta dashboard.


* OKTA_CLIENT_SECRET

  *Client secret* from the *Client Credentials* of the [Backend okta application](#backend-okta-application) in the okta dashboard.


* OKTA_ISSUER

  Set to the [default authorization server](https://developer.okta.com/docs/reference/api/oidc/#_2-okta-as-the-identity-platform-for-your-app-or-api) for the okta developer account.
  Using *Okta domain* from the *General Settings* of the [Backend okta application](#backend-okta-application) in the okta dashboard, set to

  ``
  https://${yourOktaDomain}/oauth2/default
  ``


#### CORS configuration

* ALLOWED_ORIGINS

  A ``;`` separated list of allows origins for client requests. 

  E.g. ``https://localhost:4200`` will allow connections from a client running on local host, port 4200.


#### Stripe configuration

* STRIPE_KEY_SECRET

  *Secret key* from the *API keys* in the Stripe developer dashboard.

  E.g. to use the *Test Mode Standard keys* goto https://dashboard.stripe.com/test/apikeys and copy the *Secret key* 

## Operations

Using the default settings, the application will be hosted at https://localhost:8443/api

### API documentation

Swagger UI: https://localhost:8443/swagger-ui/index.html

