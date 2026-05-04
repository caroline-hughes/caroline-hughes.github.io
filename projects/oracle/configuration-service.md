# Configuration Service

`Java / Micronaut` `SQL / Liquibase` `Oracle Kubernetes Engine`

The Config Service was a foundational service I owned for about a year, while working within Oracle Health & AI (OHAI).

In a nutshell, it was a simple `(key, value, scope)` store, housing the 'configurations' or 'settings' of all microservices in the cluster. Think feature flags, and other tunable parameters.
For instance, the key `example.registration.addressFormat` might have one value for US region tenants, and another for EU tenants. Or, United Healthcare might want all it's tenants to have `example.featureflags.flagX` enabled, while other orgs don't use that feature at all.

With many microservices, many environments, and the need to configure at the org/tenant/region level, the dimension space of our configs was exploding - we needed a better way to manage them than helm charts and environment variables.

We built this Configuration Service to migrate away from helm, for these key reasons:
1. Configs varied by region / country / tenant / org
- with helm, tiered settings creates a messy matrix: `service × environment × region × tenant × override`. our service collapsed that matrix (and the need to manage many helm charts) into a single source of truth, with ordered scopes and overrides

2. Helm tightly couples config changes to deployments
- we decoupled application version environment / tenant configuration

3. Helm values are not easily accessible at runtime, and not strongly typed
- with the new service, configs are accessible by a library call only when needed (no need to load env vars), and can be cached
- with many complex setting types, we could enforce type-safety at the service layer, rather than relying on individual services to do so

As the lead of the service, I
- worked daily to enhance/extend to the library methods and the API,
- planned, documented and executed the plan to migrate the first batch of caller microservices over to the Config Service... ensuring thorough testing and a successful first deployment rollout,
- served as first point of contact for questions around our API for config creation/update, scoping questions, and configuration bugs
- became a pro at coordinating/managing service availability and consistency.. as the service was critical to app startup and various pieces of runtime functionality.

## Cluster-wide startup dependency

Inside one OKE cluster, the frontend and 15+ services shared one config dependency and resolved keys through the same scoped lookup contract.

```mermaid
flowchart LR
    subgraph OKE["OKE Cluster"]
        direction LR

        subgraph App["Patient Admin App"]
            direction TB
            F["Frontend App"]
            A["Registration Service"]
            B["Scheduling Service"]
            C["Insurance Service"]
            D["Financial Clearance"]
            E["... 10+ more<br/>services"]
            H((" "))
        end

        Z["Config Service"]
    end

    Y[("SQL DB")]

    F --> H
    A --> H
    B --> H
    C --> H
    D --> H
    E --> H
    H -->|"get(config_key, level)"| Z
    Z -->|"KV entries + scoped overrides"| Y

    classDef service fill:#E3F2FD,stroke:#1E88E5,stroke-width:1px,color:#172033
    classDef core fill:#EDE7F6,stroke:#5E35B1,stroke-width:1px,color:#172033
    classDef storage fill:#FFF3E0,stroke:#FB8C00,stroke-width:1px,color:#172033
    classDef client fill:#FFFFFF,stroke:#94A3B8,stroke-width:1px,color:#172033
    classDef join fill:#F8FAFC,stroke:#94A3B8,stroke-width:1px,color:#F8FAFC

    class A,B,C,D,E service
    class Z core
    class Y storage
    class F client
    class H join
    style OKE fill:#F8FAFC,stroke:#93C5FD,stroke-width:1.5px
    style App fill:#FFFFFF,stroke:#CBD5E1,stroke-width:1px
```

## Scoped overrides

Config resolves from most-specific to least-specific scope, with global defaults as the fallback.

```mermaid
flowchart TD
    A["Request config key<br/>registration.timeoutSeconds"] --> B{"Org override?"}
    B -->|yes| B1["Use org value"]
    B -->|no| C{"Tenant override?"}
    C -->|yes| C1["Use tenant value"]
    C -->|no| D{"Region override?"}
    D -->|yes| D1["Use region value"]
    D -->|no| E["Use global default"]

    style A fill:#E3F2FD,stroke:#1E88E5,stroke-width:1px
    style B fill:#EDE7F6,stroke:#5E35B1,stroke-width:1px
    style C fill:#EDE7F6,stroke:#5E35B1,stroke-width:1px
    style D fill:#EDE7F6,stroke:#5E35B1,stroke-width:1px
    style B1 fill:#E8F5E9,stroke:#43A047,stroke-width:1px
    style C1 fill:#E8F5E9,stroke:#43A047,stroke-width:1px
    style D1 fill:#E8F5E9,stroke:#43A047,stroke-width:1px
    style E fill:#FFF3E0,stroke:#FB8C00,stroke-width:1px
```

## Flexible storage

A single config key can exist at multiple scopes. The resolver chooses the best match.

```mermaid
erDiagram
    CONFIG_ENTRY {
        string key
        string value
        string value_type
        string scope_type
        string scope_id
        datetime created_at
        datetime updated_at
    }
```

| key | scope | scope_id | value |
| --- | --- | --- | --- |
| `registration.timeoutSeconds` | GLOBAL | `*` | `30` |
| `registration.timeoutSeconds` | REGION | `us-west` | `45` |
| `registration.timeoutSeconds` | TENANT | `tenant-a` | `60` |
| `registration.timeoutSeconds` | ORG | `org-123` | `90` |

## Startup fetch

Services load configuration at startup and keep it in memory for predictable runtime behavior.

```mermaid
sequenceDiagram
    participant S as Backend Service
    participant C as Config Service
    participant DB as SQL DB
    participant Cache as In-Memory Cache

    S->>C: Fetch config for service + scope
    C->>DB: Query matching config entries
    DB-->>C: Return scoped values
    C-->>S: Return resolved config
    S->>Cache: Store locally

    Note over S,Cache: Runtime requests use local config
```
