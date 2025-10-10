# JAXRS Client Factory

Create client from JAXRS annotated interface.

If `WhateverApi` is a JAXRS annotated interface, a client can be created with:

```java
WhateverApi client = new JaxrsClientFactory()
.create(JaxrsClientOptionsBuilders.createClient(baseUrl, WhateverApi.class)
            .header("static-header-1", "static-header-1-value")
            .build());
```

The `WhateverApi` might be generated from an OpenAPI spec, see [gradle-conventions](https://github.com/Forsakringskassan/gradle-conventions).

