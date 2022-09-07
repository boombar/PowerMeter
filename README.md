# Power Meter

## Application description

CRUD Rest API application with CSV uploading functionality

## Assumptions

- A profile is household consumption percentage for (any) year
- One meter is tied to exactly one profile, and one profile has several meters
- Each meter must conform to profile fractions
- Uploading CSV will overwrite existing data if present
- Uploading CSV will return all valid uploaded data and list of errors

## Running application

Application is run by calling

```
gradlew bootRun
```

An upload folder is required in the project root, name can be parametrized in application.properties

```
monitoring-folder=upload
```

RestApi is available at:

```
localhost:8080
```

Swagger Documentation is available at:

```
localhost:8080/swagger-ui/index.html#/
```

## Running tests

There are unit tests covering happy flow

```
gradlew test
```

## Future steps

- Implement paging, sorting, filtering
- Validate DTOs with custom validations and validators
- When updating fractions, consider checking if existing readings are inline
- Instead of each meter conforming to profile fractions, change to sum of consumption of all meters for month should
  conform to month fraction
- Check CSV headers when uploading
- Use headers instead of filename for folder upload
- Trim whitespace in csv, consider switching to SuperCSV lib
- Handle better upload log file
- Implement security
- Increase coverage with unit and integration tests to at least 90%
- Write feature & automation tests
- Write javadoc

 
