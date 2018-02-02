# dropwizard-sentry
This is a plugin to interface io.sentry to dropwizard. Currently it supports logback plugin and I plan to add support for other logger as well. 

How to use this plugin:
1. Add the jar from maven to your project's dependency
2. Add the fields that you want to set for sentry in yaml as given below:
  appenders:
    - type: file
      threshold: DEBUG
      logFormat: "%-6level [%d{HH:mm:ss.SSS}] [%t] %logger{5} - %X{code} %msg %n"
      currentLogFilename: /opt/logs/ApplicationName/application.log
      archive: false
      timeZone: UTC
      maxFileSize: 10MB
    - type: sentry
      threshold: INFO
      dsn: https://60:d59edce768235d@bugs.companyname.com/90
      fieldMap:
          environment: staging2
          release: 1.0.0
          sample.rate: 1.0
          
fieldMap is a map of key value pair where in you can add other fields that may be supported by sentry in future
