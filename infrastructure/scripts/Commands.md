

# Describe DB engine
`aws rds describe-db-engine-versions`

# Steps for initial setup
* Via Console
  * Create IAM group 
  * Attach IAM administrator policy to the group
  * Create user and attach it to the group
  * Get access key and secret key and use for terraform
* Create S3 bucket role

# Ping Redis
* python ping_redis.py <host>

# Troubleshooting
* Install Redis module for python
  * `sudo pip install redis`