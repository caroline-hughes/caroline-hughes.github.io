# Rules Engine

Centralized platform for defining and executing configurable business logic across a healthcare microservice ecosystem.

<p><span class="project-pill project-pill-java">Java</span> <span class="project-pill project-pill-rules">Drools</span> <span class="project-pill project-pill-search">Semantic Index</span></p>

## What I built

A rules engine that allowed teams (and eventually tenants) to define **dynamic business logic** outside of application code.

-   rules written in **Drools (DRL)** and executed via KIE sessions
-   supported both **event-driven workflows** and **runtime validation rules**
-   exposed via a shared Java execution library used across services
-   rule _instances_ (data-driven configs) stored and retrieved from the Semantic Index

Instead of hardcoding logic into services, behavior could be defined and changed through rules.

---

## Why it mattered

In healthcare systems, “rules” are everywhere:

-   workflows (e.g. no-show → notify patient)
-   constraints (e.g. scheduling limits, resource availability)
-   organization-specific policies

Previously, these were:

-   hardcoded into services
-   duplicated across systems
-   difficult to customize per tenant/org

This created two problems:

1. **Lack of flexibility** — changes required code + deployment
2. **One-size-fits-all logic** — hard to support tenant-specific behavior

The Rules Engine solved this by making logic **configurable, dynamic, and tenant-aware**.

---

## Example use cases

**Event-driven workflow**

-   “If appointment becomes overdue → send notification to patient”

**Runtime validation**

-   “Dr. Jones can take max 3 cardiology appointments on Mondays”
-   “Radiology machine must be available during appointment time”

These rules could be defined once, stored centrally, and executed wherever needed.

---

## How it worked

### Core concepts

**1. Rule definitions (DRL)**

-   rules written in Drools Rule Language
-   grouped into **KIE sessions** (logical sets of rules)
-   session chosen based on the calling service / domain

**2. Rule instances (data layer)**

-   rules often required dynamic data (e.g. quotas, thresholds)
-   stored as structured records in the Semantic Index
-   fetched at runtime and injected into rule execution

**3. Execution model**

At runtime:

1. service calls rules engine via shared library
2. engine loads relevant rule set (KIE session)
3. fetches applicable rule instances (cached when possible)
4. executes rules against provided context
5. returns results (violations, actions, etc.)

---

### Design decisions

-   **Rules loaded once into memory (KIE sessions)**  
    → fast execution, minimal runtime overhead

-   **Split rule sets by domain/service**  
    → avoided loading unnecessary rules, improved performance

-   **Caching layer for rule instances**  
    → reduced dependency on Semantic Index at runtime

---

## My impact

-   designed and implemented the execution layer + service integration
-   structured rule sets into logical KIE sessions by domain
-   built and refined rule execution APIs used by multiple services
-   worked through runtime issues (JDK compatibility, Drools quirks, performance)
-   helped teams model real-world healthcare constraints as executable rules

→ Result: moved business logic out of code and into a flexible, reusable system that supported tenant-specific behavior at scale
