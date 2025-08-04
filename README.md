# outcome-auditing-api-tests
API test suite for the `outcome-auditing` using [api-test-runner](https://github.com/hmrc/api-test-runner) library.  

## Running the tests

Prior to executing the tests ensure you have:
 - Installed/configured [service manager](https://github.com/hmrc/service-manager).  

Run the following commands to start services locally:

    sm2 --start OUTCOME_AUDITING --appendArgs '{
        "OUTCOME_AUDITING": [
            "-J-Dauditing.consumer.baseUri.port=6001",
            "-J-Dauditing.consumer.baseUri.host=localhost",
            "-J-Dauditing.enabled=true"
        ]
    }'

## Running specs

Execute the `run_specs.sh` script:

`./run-specs.sh`

This script takes two parameters:

- `<ENV>` which is set to `local` by default
- `<DAST>` which is set to `false` by default, but is used to run Dynamic Application Security Testing (DAST) tests by setting this value to `true` in Jenkins builds

## Scalafmt

Check all project files are formatted as expected by running the following script:

```bash
./scalafmt.sh
```

## License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
