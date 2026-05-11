# Configuration Service

Built and owned a centralized configuration platform for a large healthcare microservice ecosystem at Oracle Health & AI.

## What I built

A `(key, value, scope)` configuration service that replaced scattered Helm/env-based configs with a single, programmatic system.

-   supported feature flags + environment configs
-   enabled hierarchical overrides (global → region → tenant → org)
-   exposed via a shared client library used across 10+ services

```mermaid
%%{init: {'flowchart': {'curve': 'basis'}}}%%
flowchart LR
    subgraph OKE["☸ OKE Cluster"]
        direction TB

        T[" "]

        subgraph Row[" "]
            direction LR

            subgraph App["Healthcare App"]
                direction TB
                F([Frontend])
                A[[Registration Service]]
                B[[Scheduling Service]]
                E[[... 10+ services]]
            end

            Z[[Config Service]]
        end
    end

    Y[("SQL DB")]

    F --> Z
    A --> Z
    B --> Z
    E --> Z
    Z --> Y

    classDef service fill:#DBEAFE,stroke:#2563EB,stroke-width:1.5px,color:#172033
    classDef core fill:#DBEAFE,stroke:#2563EB,stroke-width:1.5px,color:#172033
    classDef storage fill:#FFF3E0,stroke:#FB8C00,stroke-width:1px,color:#172033
    classDef client fill:#F3E8FF,stroke:#8B5CF6,stroke-width:1.5px,color:#172033
    classDef pad fill:transparent,stroke:transparent,color:transparent

    class A,B,E service
    class Z core
    class Y storage
    class F client
    class T pad
    style OKE fill:#F8FAFC,stroke:#60A5FA,stroke-width:2px,stroke-dasharray: 8 4,rx:18px,ry:18px
    style Row fill:transparent,stroke:transparent
    style App fill:#EFF6FF,stroke:#60A5FA,stroke-width:1.5px,rx:16px,ry:16px
```

## Why it mattered

Configuration was becoming unmanageable:

-   duplicated across services + environments
-   hard to reason about overrides
-   tightly coupled to deployments

This system:

-   became the **single source of truth** for configs
-   enabled **safe, flexible overrides per tenant/org**
-   **reduced deployment risk** by decoupling config from Helm

## How it worked

-   Micronaut service running in OKE
-   configs stored in SQL (Oracle Autonomous DB)
-   services fetch config at startup and cache locally
-   resolver selects the most specific scoped value

Example hierarchy:

ORG > TENANT > REGION > GLOBAL

## My impact

-   owned design + implementation end-to-end
-   migrated core microservices onto the platform
-   improved reliability of service startup and deployments
-   became a critical dependency across the system

→ Result: simpler config management, safer deployments, and scalable support for multi-tenant customization
