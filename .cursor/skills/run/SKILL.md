---
name: run
description: >-
  Launch PactorRATT_Alpha without incrementing the build number.
  Use when the user invokes /run, or says run, launch, or start the app.
disable-model-invocation: true
---

# Run PactorRATT_Alpha

Launch immediately. Do not check for an existing copy, compile output, or the build number.

From the project root, start this in the background and do not wait for it to exit:

```powershell
.\.tools\apache-maven-3.9.6\bin\mvn.cmd -q compiler:compile exec:java "-Dexec.mainClass=com.pactorratt.alpha.app.PactorRattAlphaApp"
```

Do not use `mvn compile` or `mvn package`. Those run the `initialize` phase and increment `build.number.properties`.

Tell the user the app is launching.
